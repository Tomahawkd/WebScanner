/*
    GNU LESSER GENERAL PUBLIC LICENSE
    Copyright (C) 2006 The Lobo Project

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

    Contact info: lobochief@users.sourceforge.net
*/
/*
 * Created on Nov 19, 2005
 */
package org.lobobrowser.html.gui;

import org.lobobrowser.html.HtmlRendererContext;
import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.domimpl.DocumentNotificationListener;
import org.lobobrowser.html.domimpl.ElementImpl;
import org.lobobrowser.html.domimpl.HTMLDocumentImpl;
import org.lobobrowser.html.domimpl.NodeImpl;
import org.lobobrowser.html.parser.DocumentBuilderImpl;
import org.lobobrowser.html.parser.InputSourceImpl;
import org.lobobrowser.html.renderer.FrameContext;
import org.lobobrowser.html.renderer.NodeRenderer;
import org.lobobrowser.html.renderer.RenderableSpot;
import org.lobobrowser.html.style.RenderState;
import org.lobobrowser.util.EventDispatch2;
import org.lobobrowser.util.gui.WrapperLayout;
import org.w3c.dom.Document;
import org.w3c.dom.Text;
import org.w3c.dom.html2.HTMLFrameSetElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;

/**
 * The <code>HtmlPanel</code> class is a Swing
 * component that can render a HTML DOM. It uses
 * either {@link HtmlBlockPanel} or {@link FrameSetPanel}
 * internally, depending on whether the document
 * is determined to be a FRAMESET or not.
 * <p>
 * Invoke method {@link #setDocument(Document, HtmlRendererContext)}
 * in order to schedule a document for rendering.
 */
public class HtmlPanel extends JComponent implements FrameContext {
	private final EventDispatch2 selectionDispatch = new SelectionDispatch();
	private final javax.swing.Timer notificationTimer;
	private final DocumentNotificationListener notificationListener;
	private final Runnable notificationImmediateAction;
	private static final int NOTIF_TIMER_DELAY = 300;

	private volatile boolean isFrameSet = false;
	private volatile NodeRenderer nodeRenderer = null;
	private volatile NodeImpl rootNode;
	private volatile Insets defaultMarginInsets = new Insets(8, 8, 8, 8);
	private volatile int defaultOverflowX = RenderState.OVERFLOW_AUTO;
	private volatile int defaultOverflowY = RenderState.OVERFLOW_SCROLL;

	private volatile HtmlBlockPanel htmlBlockPanel;
	private volatile FrameSetPanel frameSetPanel;


	/**
	 * Constructs an <code>HtmlPanel</code>.
	 */
	public HtmlPanel() {
		super();
		this.setLayout(WrapperLayout.getInstance());
		this.setOpaque(false);
		this.notificationTimer = new javax.swing.Timer(NOTIF_TIMER_DELAY, new NotificationTimerAction());
		this.notificationTimer.setRepeats(false);
		this.notificationListener = new LocalDocumentNotificationListener();
		this.notificationImmediateAction = this::processNotifications;
	}

	/**
	 * Scrolls the body area to the node given, if it is
	 * part of the current document.
	 * <p>
	 * This method should be called from the GUI thread.
	 *
	 * @param node A DOM node.
	 */
	private void scrollTo(org.w3c.dom.Node node) {
		HtmlBlockPanel htmlBlock = this.htmlBlockPanel;
		if (htmlBlock != null) {
			htmlBlock.scrollTo(node);
		}
	}

	private void setUpAsBlock(UserAgentContext ucontext, HtmlRendererContext rcontext) {
		HtmlBlockPanel shp = this.createHtmlBlockPanel(ucontext, rcontext);
		int preferredWidth = -1;
		shp.setPreferredWidth(preferredWidth);
		shp.setDefaultMarginInsets(this.defaultMarginInsets);
		shp.setDefaultOverflowX(this.defaultOverflowX);
		shp.setDefaultOverflowY(this.defaultOverflowY);
		this.htmlBlockPanel = shp;
		this.frameSetPanel = null;
		this.removeAll();
		this.add(shp);
		this.nodeRenderer = shp;
	}

	private void setUpFrameSet(NodeImpl fsrn) {
		this.isFrameSet = true;
		this.htmlBlockPanel = null;
		FrameSetPanel fsp = this.createFrameSetPanel();
		this.frameSetPanel = fsp;
		this.nodeRenderer = fsp;
		this.removeAll();
		this.add(fsp);
		fsp.setRootNode(fsrn);
	}

	/**
	 * Method invoked internally to create a {@link HtmlBlockPanel}.
	 * It is made available so it can be overridden.
	 */
	private HtmlBlockPanel createHtmlBlockPanel(UserAgentContext ucontext, HtmlRendererContext rcontext) {
		return new HtmlBlockPanel(java.awt.Color.WHITE, true, ucontext, rcontext, this);
	}

	/**
	 * Method invoked internally to create a {@link FrameSetPanel}.
	 * It is made available so it can be overridden.
	 */
	private FrameSetPanel createFrameSetPanel() {
		return new FrameSetPanel();
	}

	/**
	 * Sets an HTML DOM node and invalidates the component so it is
	 * rendered as soon as possible in the GUI thread.
	 * <p>
	 * If this method is called from a thread that is not the GUI
	 * dispatch thread, the document is scheduled to be set later.
	 *
	 * @param node     This should
	 *                 normally be a Document instance obtained with
	 *                 {@link org.lobobrowser.html.parser.DocumentBuilderImpl}.
	 *                 <p>
	 * @param rcontext A renderer context.
	 * @see org.lobobrowser.html.parser.DocumentBuilderImpl#parse(org.xml.sax.InputSource)
	 * @see org.lobobrowser.html.test.SimpleHtmlRendererContext
	 */
	public void setDocument(final Document node, final HtmlRendererContext rcontext) {
		if (java.awt.EventQueue.isDispatchThread()) {
			this.setDocumentImpl(node, rcontext);
		} else {
			java.awt.EventQueue.invokeLater(() -> HtmlPanel.this.setDocumentImpl(node, rcontext));
		}
	}

	/**
	 * Scrolls to the element identified by the given ID in
	 * the current document.
	 * <p>
	 * If this method is invoked outside the GUI thread,
	 * the operation is scheduled to be performed as soon
	 * as possible in the GUI thread.
	 *
	 * @param nameOrId The name or ID of the element in the document.
	 */
	public void scrollToElement(final String nameOrId) {
		if (EventQueue.isDispatchThread()) {
			this.scrollToElementImpl(nameOrId);
		} else {
			EventQueue.invokeLater(() -> scrollToElementImpl(nameOrId));
		}
	}

	private void scrollToElementImpl(String nameOrId) {
		NodeImpl node = this.rootNode;
		if (node instanceof HTMLDocumentImpl) {
			HTMLDocumentImpl doc = (HTMLDocumentImpl) node;
			org.w3c.dom.Element element = doc.getElementById(nameOrId);
			if (element != null) {
				this.scrollTo(element);
			}
		}
	}

	private void setDocumentImpl(Document node, HtmlRendererContext rcontext) {
		// Expected to be called in the GUI thread.
		if (!(node instanceof HTMLDocumentImpl)) {
			throw new IllegalArgumentException("Only nodes of type HTMLDocumentImpl are currently supported. Use DocumentBuilderImpl.");
		}
		HTMLDocumentImpl prevDocument = (HTMLDocumentImpl) this.rootNode;
		if (prevDocument != null) {
			prevDocument.removeDocumentNotificationListener(this.notificationListener);
		}
		HTMLDocumentImpl nodeImpl = (HTMLDocumentImpl) node;
		nodeImpl.addDocumentNotificationListener(this.notificationListener);
		this.rootNode = nodeImpl;
		NodeImpl fsrn = this.getFrameSetRootNode(nodeImpl);
		boolean newIfs = fsrn != null;
		if (newIfs != this.isFrameSet || this.getComponentCount() == 0) {
			this.isFrameSet = newIfs;
			if (newIfs) {
				this.setUpFrameSet(fsrn);
			} else {
				this.setUpAsBlock(rcontext.getUserAgentContext(), rcontext);
			}
		}
		NodeRenderer nr = this.nodeRenderer;
		if (nr != null) {
			// These subcomponents should take care
			// of revalidation.
			if (newIfs) {
				nr.setRootNode(fsrn);
			} else {
				nr.setRootNode(nodeImpl);
			}
		} else {
			this.invalidate();
			this.validate();
			this.repaint();
		}
	}

	/**
	 * Renders HTML given as a string.
	 *
	 * @param htmlSource The HTML source code.
	 * @param uri        A base URI used to resolve item URIs.
	 * @param rcontext   The {@link HtmlRendererContext} instance.
	 * @see org.lobobrowser.html.test.SimpleHtmlRendererContext
	 * @see #setDocument(Document, HtmlRendererContext)
	 */
	public void setHtml(String htmlSource, String uri, HtmlRendererContext rcontext) {
		try {
			DocumentBuilderImpl builder = new DocumentBuilderImpl(rcontext.getUserAgentContext(), rcontext);
			try (Reader reader = new StringReader(htmlSource)) {
				InputSourceImpl is = new InputSourceImpl(reader, uri);
				Document document = builder.parse(is);
				this.setDocument(document, rcontext);
			}
		} catch (java.io.IOException | org.xml.sax.SAXException ioe) {
			throw new IllegalStateException("Unexpected condition.", ioe);
		}
	}

	/**
	 * Gets the HTML DOM node currently rendered if any.
	 */
	public NodeImpl getRootNode() {
		return this.rootNode;
	}

	private boolean resetIfFrameSet() {
		NodeImpl nodeImpl = this.rootNode;
		NodeImpl fsrn = this.getFrameSetRootNode(nodeImpl);
		boolean newIfs = fsrn != null;
		if (newIfs != this.isFrameSet || this.getComponentCount() == 0) {
			this.isFrameSet = newIfs;
			if (newIfs) {
				this.setUpFrameSet(fsrn);
				NodeRenderer nr = this.nodeRenderer;
				nr.setRootNode(fsrn);
				// Set proper bounds and repaint.
				this.validate();
				this.repaint();
				return true;
			}
		}
		return false;
	}

	private NodeImpl getFrameSetRootNode(NodeImpl node) {
		if (node instanceof Document) {
			ElementImpl element = (ElementImpl) ((Document) node).getDocumentElement();
			if (element != null && "HTML".equalsIgnoreCase(element.getTagName())) {
				return this.getFrameSet(element);
			} else {
				return this.getFrameSet(node);
			}
		} else {
			return null;
		}
	}

	private NodeImpl getFrameSet(NodeImpl node) {
		NodeImpl[] children = node.getChildrenArray();
		if (children == null) {
			return null;
		}
		NodeImpl frameSet = null;
		for (NodeImpl child : children) {
			if (child instanceof Text) {

			} else if (child instanceof ElementImpl) {
				String tagName = child.getNodeName();
				if ("HEAD".equalsIgnoreCase(tagName) ||
						"NOFRAMES".equalsIgnoreCase(tagName) ||
						"TITLE".equalsIgnoreCase(tagName) ||
						"META".equalsIgnoreCase(tagName) ||
						"SCRIPT".equalsIgnoreCase(tagName) ||
						"NOSCRIPT".equalsIgnoreCase(tagName)) {

				} else if ("FRAMESET".equalsIgnoreCase(tagName)) {
					frameSet = child;
					break;
				} else {
					if (this.hasSomeHtml((ElementImpl) child)) {
						return null;
					}
				}
			}
		}
		return frameSet;
	}

	private boolean hasSomeHtml(ElementImpl element) {
		String tagName = element.getTagName();
		if ("HEAD".equalsIgnoreCase(tagName) || "TITLE".equalsIgnoreCase(tagName) || "META".equalsIgnoreCase(tagName)) {
			return false;
		}
		NodeImpl[] children = element.getChildrenArray();
		if (children != null) {
			for (NodeImpl child : children) {
				if (child instanceof Text) {
					String textContent = child.getTextContent();
					if (textContent != null && !"".equals(textContent.trim())) {
						return false;
					}
				} else if (child instanceof ElementImpl) {
					if (this.hasSomeHtml((ElementImpl) child)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Internal method used to expand the selection to the given point.
	 * <p>
	 * Note: This method should be invoked in the GUI thread.
	 */
	public void expandSelection(RenderableSpot rpoint) {
		HtmlBlockPanel block = this.htmlBlockPanel;
		if (block != null) {
			block.setSelectionEnd(rpoint);
			block.repaint();
			this.selectionDispatch.fireEvent(new SelectionChangeEvent(this, block.isSelectionAvailable()));
		}
	}

	/**
	 * Internal method used to reset the selection so that
	 * it is empty at the given point. This is what is called
	 * when the user clicks on a point in the document.
	 * <p>
	 * Note: This method should be invoked in the GUI thread.
	 */
	public void resetSelection(RenderableSpot rpoint) {
		HtmlBlockPanel block = this.htmlBlockPanel;
		if (block != null) {
			block.setSelectionStart(rpoint);
			block.setSelectionEnd(rpoint);
			block.repaint();
		}
		this.selectionDispatch.fireEvent(new SelectionChangeEvent(this, false));
	}

	/**
	 * Copies the current selection, if any, into the clipboard.
	 * This method has no effect in FRAMESETs at the moment.
	 */
	public boolean copy() {
		HtmlBlockPanel block = this.htmlBlockPanel;
		return block != null && block.copy();
	}

	/**
	 * Adds listener of selection changes. Note that it does
	 * not have any effect on FRAMESETs.
	 *
	 * @param listener An instance of {@link SelectionChangeListener}.
	 */
	void addSelectionChangeListener(SelectionChangeListener listener) {
		this.selectionDispatch.addListener(listener);
	}

	/**
	 * Sets the default margin insets. Note that in the root block,
	 * the margin behaves like padding.
	 * <p>
	 * This method has no effect on FRAMESETs.
	 *
	 * @param insets The default margin insets.
	 */
	public void setDefaultMarginInsets(Insets insets) {
		this.defaultMarginInsets = insets;
		HtmlBlockPanel block = this.htmlBlockPanel;
		if (block != null) {
			block.setDefaultMarginInsets(insets);
		}
	}

	/**
	 * Sets the default horizontal overflow.
	 * <p>
	 * This method has no effect on FRAMESETs.
	 *
	 * @param overflow See {@link org.lobobrowser.html.style.RenderState}.
	 */
	public void setDefaultOverflowX(int overflow) {
		this.defaultOverflowX = overflow;
		HtmlBlockPanel block = this.htmlBlockPanel;
		if (block != null) {
			block.setDefaultOverflowX(overflow);
		}
	}

	/**
	 * Sets the default vertical overflow.
	 * <p>
	 * This method has no effect on FRAMESETs.
	 *
	 * @param overflow See {@link org.lobobrowser.html.style.RenderState}.
	 */
	public void setDefaultOverflowY(int overflow) {
		this.defaultOverflowY = overflow;
		HtmlBlockPanel block = this.htmlBlockPanel;
		if (block != null) {
			block.setDefaultOverflowY(overflow);
		}
	}

	private final ArrayList<DocumentNotification> notifications = new ArrayList<>(1);

	private void addNotification(DocumentNotification notification) {
		// This can be called in a random thread.
		synchronized (this.notifications) {
			this.notifications.add(notification);
		}
		if (EventQueue.isDispatchThread()) {
			// In this case we want the notification to be processed
			// immediately. However, we don't want potential recursions
			// to occur when a Javascript property is set in the GUI thread.
			// Additionally, many property values may be set in one
			// event block.
			EventQueue.invokeLater(this.notificationImmediateAction);
		} else {
			this.notificationTimer.restart();
		}
	}

	/**
	 * Invalidates the layout of the given node and schedules it
	 * to be layed out later. Multiple invalidations may be
	 * processed in a single document layout.
	 */
	public void delayedRelayout(NodeImpl node) {
		synchronized (this.notifications) {
			this.notifications.add(new DocumentNotification(DocumentNotification.SIZE, node));
		}
		this.notificationTimer.restart();
	}

	private void processNotifications() {
		// This is called in the GUI thread.
		DocumentNotification[] notifsArray;
		synchronized (this.notifications) {
			int size = this.notifications.size();
			if (size == 0) {
				return;
			}
			notifsArray = new DocumentNotification[size];
			notifsArray = this.notifications.toArray(notifsArray);
			this.notifications.clear();
		}
		for (DocumentNotification dn : notifsArray) {
			if (dn.node instanceof HTMLFrameSetElement && this.htmlBlockPanel != null) {
				if (this.resetIfFrameSet()) {
					return;
				}
			}
		}
		HtmlBlockPanel blockPanel = this.htmlBlockPanel;
		if (blockPanel != null) {
			blockPanel.processDocumentNotifications(notifsArray);
		}
		FrameSetPanel frameSetPanel = this.frameSetPanel;
		if (frameSetPanel != null) {
			frameSetPanel.processDocumentNotifications(notifsArray);
		}
	}

	private class SelectionDispatch extends EventDispatch2 {
		/* (non-Javadoc)
		 * @see org.xamjwg.util.EventDispatch2#dispatchEvent(java.util.EventListener, java.util.EventObject)
		 */
		protected void dispatchEvent(EventListener listener, EventObject event) {
			((SelectionChangeListener) listener).selectionChanged((SelectionChangeEvent) event);
		}
	}

	private class LocalDocumentNotificationListener implements DocumentNotificationListener {
		public void allInvalidated() {
			HtmlPanel.this.addNotification(new DocumentNotification(DocumentNotification.GENERIC, null));
		}

		public void invalidated(NodeImpl node) {
			HtmlPanel.this.addNotification(new DocumentNotification(DocumentNotification.GENERIC, node));
		}

		public void lookInvalidated(NodeImpl node) {
			HtmlPanel.this.addNotification(new DocumentNotification(DocumentNotification.LOOK, node));
		}

		public void positionInvalidated(NodeImpl node) {
			HtmlPanel.this.addNotification(new DocumentNotification(DocumentNotification.POSITION, node));
		}

		public void sizeInvalidated(NodeImpl node) {
			HtmlPanel.this.addNotification(new DocumentNotification(DocumentNotification.SIZE, node));
		}

		public void externalScriptLoading(NodeImpl node) {
			// Ignorable here.
		}

		public void nodeLoaded(NodeImpl node) {
			HtmlPanel.this.addNotification(new DocumentNotification(DocumentNotification.GENERIC, node));
		}

		public void structureInvalidated(NodeImpl node) {
			HtmlPanel.this.addNotification(new DocumentNotification(DocumentNotification.GENERIC, node));
		}
	}

	private class NotificationTimerAction implements java.awt.event.ActionListener {
		public void actionPerformed(ActionEvent e) {
			HtmlPanel.this.processNotifications();
		}
	}
}
