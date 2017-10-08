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
 * Created on Apr 16, 2005
 */
package org.lobobrowser.html.gui;

import org.lobobrowser.html.HtmlRendererContext;
import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.domimpl.HTMLElementImpl;
import org.lobobrowser.html.domimpl.NodeImpl;
import org.lobobrowser.html.domimpl.UINode;
import org.lobobrowser.html.renderer.*;
import org.lobobrowser.html.style.RenderState;
import org.lobobrowser.util.Nodes;
import org.lobobrowser.util.Objects;
import org.w3c.dom.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.util.*;

/**
 * A Swing component that renders a HTML block, given
 * by a DOM root or an internal element, typically a DIV.
 * This component <i>cannot</i> render FRAMESETs. <code>HtmlBlockPanel</code>
 * is used by {@link HtmlPanel} whenever the DOM
 * is determined <i>not</i> to be a FRAMESET.
 *
 * @author J. H. S.
 * @see HtmlPanel
 * @see FrameSetPanel
 */
public class HtmlBlockPanel extends JComponent implements NodeRenderer, RenderableContainer, ClipboardOwner {
	protected final FrameContext frameContext;
	private final UserAgentContext ucontext;
	protected final HtmlRendererContext rcontext;

	private RenderableSpot startSelection;
	private RenderableSpot endSelection;
	private RBlock rblock;
	private int preferredWidth = -1;
	private Insets defaultMarginInsets = null;
	//protected Insets defaultPaddingInsets = null;
	private int defaultOverflowX = RenderState.OVERFLOW_AUTO;
	private int defaultOverflowY = RenderState.OVERFLOW_SCROLL;

	HtmlBlockPanel(Color background, boolean opaque, UserAgentContext pcontext, HtmlRendererContext rcontext, FrameContext frameContext) {
		this.setLayout(null);
		this.setAutoscrolls(true);
		this.frameContext = frameContext;
		this.ucontext = pcontext;
		this.rcontext = rcontext;
		this.setOpaque(opaque);
		this.setBackground(background);
		ActionListener actionListener = e -> {
			String command = e.getActionCommand();
			if ("copy".equals(command)) {
				copy();
			}
		};
		if (!GraphicsEnvironment.isHeadless()) {
			this.registerKeyboardAction(actionListener, "copy", KeyStroke.getKeyStroke(KeyEvent.VK_COPY, 0), JComponent.WHEN_FOCUSED);
			this.registerKeyboardAction(actionListener, "copy", KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), JComponent.WHEN_FOCUSED);
		}
		this.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				onMouseClick(e);
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
				onMouseExited(e);
			}

			public void mousePressed(MouseEvent e) {
				onMousePressed(e);
			}

			public void mouseReleased(MouseEvent e) {
				onMouseReleased(e);
			}
		});
		this.addMouseMotionListener(new MouseMotionListener() {
			/* (non-Javadoc)
			 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
			 */
			public void mouseDragged(MouseEvent e) {
				onMouseDragged(e);
			}

			/* (non-Javadoc)
			 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
			 */
			public void mouseMoved(MouseEvent arg0) {
				onMouseMoved(arg0);
			}
		});
		this.addMouseWheelListener(this::onMouseWheelMoved);
	}

	/**
	 * Scrolls the body area to the given location.
	 * <p>
	 * This method should be called from the GUI thread.
	 *
	 * @param bounds    The bounds in the scrollable block area that should
	 *                  become visible.
	 * @param xIfNeeded If this parameter is true, scrolling will only occur if the
	 *                  requested bounds are not currently visible horizontally.
	 * @param yIfNeeded If this parameter is true, scrolling will only occur if the
	 *                  requested bounds are not currently visible vertically.
	 */
	private void scrollTo(Rectangle bounds, boolean xIfNeeded, boolean yIfNeeded) {
		RBlock block = this.rblock;
		if (block != null) {
			block.scrollTo(bounds, xIfNeeded, yIfNeeded);
		}
	}

	/**
	 * Scrolls the body area to the node given, if it is
	 * part of the current document.
	 * <p>
	 * This method should be called from the GUI thread.
	 *
	 * @param node A DOM node.
	 */
	void scrollTo(Node node) {
		Rectangle bounds = this.getNodeBounds(node, true);
		if (bounds == null) {
			return;
		}
		this.scrollTo(bounds, true, false);
	}

	/**
	 * Gets the rectangular bounds of the given node.
	 * <p>
	 * This method should be called from the GUI thread.
	 *
	 * @param node                 A node in the current document.
	 * @param relativeToScrollable Whether the bounds should be relative to the
	 *                             scrollable body area. Otherwise, they are
	 *                             relative to the root block (which is the
	 *                             essentially the same as being relative to this
	 *                             <code>HtmlBlockPanel</code> minus Swing
	 *                             borders).
	 */
	private Rectangle getNodeBounds(Node node, boolean relativeToScrollable) {
		RBlock block = this.rblock;
		if (block == null) {
			return null;
		}
		// Find UINode first
		Node currentNode = node;
		UINode uiNode = null;
		while (currentNode != null) {
			if (currentNode instanceof HTMLElementImpl) {
				HTMLElementImpl element = (HTMLElementImpl) currentNode;
				uiNode = element.getUINode();
				if (uiNode != null) {
					break;
				}
			}
			currentNode = currentNode.getParentNode();
		}
		if (uiNode == null) {
			return null;
		}
		RCollection relativeTo = relativeToScrollable ? block.getRBlockViewport() : block;
		if (node == currentNode) {
			BoundableRenderable br = (BoundableRenderable) uiNode;
			Point guiPoint = br.getOriginRelativeTo(relativeTo);
			Dimension size = br.getSize();
			return new Rectangle(guiPoint, size);
		} else {
			return this.scanNodeBounds((RCollection) uiNode, node, relativeTo);
		}
	}

	/**
	 * Gets an aggregate of the bounds of renderer leaf nodes.
	 */
	private Rectangle scanNodeBounds(RCollection root, Node node, RCollection relativeTo) {
		Iterator i = root.getRenderables();
		Rectangle resultBounds = null;
		BoundableRenderable prevBoundable = null;
		if (i != null) {
			while (i.hasNext()) {
				Renderable r = (Renderable) i.next();
				Rectangle subBounds = null;
				if (r instanceof RCollection) {
					RCollection rc = (RCollection) r;
					prevBoundable = rc;
					subBounds = this.scanNodeBounds(rc, node, relativeTo);
				} else if (r instanceof BoundableRenderable) {
					BoundableRenderable br = (BoundableRenderable) r;
					prevBoundable = br;
					if (Nodes.isSameOrAncestorOf(node, (Node) r.getModelNode())) {
						Point origin = br.getOriginRelativeTo(relativeTo);
						Dimension size = br.getSize();
						subBounds = new Rectangle(origin, size);
					}
				} else {
					// This would have to be a RStyleChanger. We rely on these
					// when the target node has blank content.
					if (Nodes.isSameOrAncestorOf(node, (Node) r.getModelNode())) {
						int xInRoot = prevBoundable == null ? 0 : prevBoundable.getX() + prevBoundable.getWidth();
						Point rootOrigin = root.getOriginRelativeTo(relativeTo);
						subBounds = new Rectangle(rootOrigin.x + xInRoot, rootOrigin.y, 0, root.getHeight());
					}
				}
				if (subBounds != null) {
					if (resultBounds == null) {
						resultBounds = subBounds;
					} else {
						resultBounds = subBounds.union(resultBounds);
					}
				}
			}
		}
		return resultBounds;
	}

	/**
	 * Allows {@link #getPreferredSize()} to render the HTML block
	 * in order to determine the preferred size of this component.
	 * Note that <code>getPreferredSize()<code> is a potentially time-consuming
	 * operation if the preferred width is set.
	 *
	 * @param width The preferred blocked width. Use <code>-1</code> to unset.
	 */
	void setPreferredWidth(int width) {
		this.preferredWidth = width;
	}

	/**
	 * If the preferred size has been set with {@link #setPreferredSize(Dimension)},
	 * then that size is returned. Otherwise a preferred size is calculated by
	 * rendering the HTML DOM, provided one is available and a preferred width other
	 * than <code>-1</code> has been set with {@link #setPreferredWidth(int)}.
	 * An arbitrary preferred size is returned in other scenarios.
	 */
	public Dimension getPreferredSize() {
		// Expected to be invoked in the GUI thread.
		if (this.isPreferredSizeSet()) {
			return super.getPreferredSize();
		}
		final int pw = this.preferredWidth;
		if (pw != -1) {
			final RBlock block = this.rblock;
			if (block != null) {
				// Layout should always be done in the GUI thread.
				if (EventQueue.isDispatchThread()) {
					block.layout(pw, 0, false, false, RenderState.OVERFLOW_VISIBLE, RenderState.OVERFLOW_VISIBLE, true);
				} else {
					try {
						EventQueue.invokeAndWait(() -> block.layout(pw, 0, false, false, RenderState.OVERFLOW_VISIBLE, RenderState.OVERFLOW_VISIBLE, true));
					} catch (Exception ignored) {
					}
				}
				// Adjust for permanent vertical scrollbar.
				int newPw = Math.max(block.width + block.getVScrollBarWidth(), pw);
				return new Dimension(newPw, block.height);
			}
		}
		return new Dimension(600, 400);
	}

	public boolean copy() {
		String selection = HtmlBlockPanel.this.getSelectionText();
		if (selection != null) {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(new StringSelection(selection), HtmlBlockPanel.this);
			return true;
		} else {
			return false;
		}
	}

	void setSelectionEnd(RenderableSpot rpoint) {
		this.endSelection = rpoint;
	}

	void setSelectionStart(RenderableSpot rpoint) {
		this.startSelection = rpoint;
	}

	/**
	 * Sets the root node to render. This method should
	 * be invoked in the GUI dispatch thread.
	 */
	public void setRootNode(NodeImpl node) {
		if (node != null) {
			RBlock block = new RBlock(node, 0, this.ucontext, this.rcontext, this.frameContext, this);
			block.setDefaultMarginInsets(this.defaultMarginInsets);
			//block.setDefaultPaddingInsets(this.defaultPaddingInsets);
			block.setDefaultOverflowX(this.defaultOverflowX);
			block.setDefaultOverflowY(this.defaultOverflowY);
			node.setUINode(block);
			this.rblock = block;
		} else {
			this.rblock = null;
		}
		this.invalidate();
		this.validateAll();
		this.repaint();
	}

	private void validateAll() {
		Component toValidate = this;
		for (; ; ) {
			Container parent = toValidate.getParent();
			if (parent == null || parent.isValid()) {
				break;
			}
			toValidate = parent;
		}
		toValidate.validate();
	}

	private void revalidatePanel() {
		// Called in the GUI thread.
		this.invalidate();
		this.validate();
		this.repaint();
	}

	private void onMouseClick(MouseEvent event) {
		// Rely on AWT mouse-click only for double-clicks
		RBlock block = this.rblock;
		if (block != null) {
			int button = event.getButton();
			int clickCount = event.getClickCount();
			if (button == MouseEvent.BUTTON1 && clickCount > 1) {
				Point point = event.getPoint();
				block.onDoubleClick(event, point.x, point.y);
			} else if (button == MouseEvent.BUTTON3 && clickCount == 1) {
				block.onRightClick(event, event.getX(), event.getY());
			}
		}
	}

	private BoundableRenderable mousePressTarget;

	private void onMousePressed(MouseEvent event) {
		this.requestFocus();
		RBlock block = this.rblock;
		if (block != null) {
			Point point = event.getPoint();
			this.mousePressTarget = block;
			int rx = point.x;
			int ry = point.y;
			block.onMousePressed(event, point.x, point.y);
			RenderableSpot rp = block.getLowestRenderableSpot(rx, ry);
			if (rp != null) {
				this.frameContext.resetSelection(rp);
			} else {
				this.frameContext.resetSelection(null);
			}
		}
	}

	private void onMouseReleased(MouseEvent event) {
		RBlock block = this.rblock;
		if (block != null) {
			Point point = event.getPoint();
			int rx = point.x;
			int ry = point.y;
			if (event.getButton() == MouseEvent.BUTTON1) {
				block.onMouseClick(event, rx, ry);
			}
			block.onMouseReleased(event, rx, ry);
			BoundableRenderable oldTarget = this.mousePressTarget;
			if (oldTarget != null) {
				this.mousePressTarget = null;
				if (oldTarget != block) {
					oldTarget.onMouseDisarmed(event);
				}
			}
		} else {
			this.mousePressTarget = null;
		}
	}

	private void onMouseExited(MouseEvent event) {
		BoundableRenderable oldTarget = this.mousePressTarget;
		if (oldTarget != null) {
			this.mousePressTarget = null;
			oldTarget.onMouseDisarmed(event);
		}
	}

	private void onMouseWheelMoved(MouseWheelEvent mwe) {
		RBlock block = this.rblock;
		if (block != null) {
			switch (mwe.getScrollType()) {
				case MouseWheelEvent.WHEEL_UNIT_SCROLL:
					int units = mwe.getWheelRotation() * mwe.getScrollAmount();
					block.scrollByUnits(JScrollBar.VERTICAL, units);
					break;
			}
		}
	}

	private void onMouseDragged(MouseEvent event) {
		RBlock block = this.rblock;
		if (block != null) {
			Point point = event.getPoint();
			RenderableSpot rp = block.getLowestRenderableSpot(point.x, point.y);
			if (rp != null) {
				this.frameContext.expandSelection(rp);
			}
			block.ensureVisible(point);
		}
	}

	private void onMouseMoved(MouseEvent event) {
		RBlock block = this.rblock;
		if (block != null) {
			Point point = event.getPoint();
			block.onMouseMoved(event, point.x, point.y, false, null);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	//protected void paintComponent(Graphics g) {
	public void paint(Graphics g) {
		// We go against Sun's advice and override
		// paint() instead of paintComponent(). Scrollbars
		// do not repaint correctly if we use
		// paintComponent.
		if (this.isOpaque()) {
			// Background not painted by default in JComponent.
			Rectangle clipBounds = g.getClipBounds();
			g.setColor(this.getBackground());
			g.fillRect(clipBounds.x, clipBounds.y, clipBounds.width, clipBounds.height);
		}
		if (g instanceof Graphics2D) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		RBlock block = this.rblock;
		if (block != null) {
			block.paint(g);
			// Paint FrameContext selection
			RenderableSpot start = this.startSelection;
			RenderableSpot end = this.endSelection;
			if (start != null && end != null && !start.equals(end)) {
				block.paintSelection(g, false, start, end);
			}
		}
	}


	public void doLayout() {
		try {
			Dimension size = this.getSize();
			this.clearComponents();
			RBlock block = this.rblock;
			if (block != null) {
				block.layout(size.width, size.height, true, true, null, false);
				//Only set origin
				block.setOrigin(0, 0);
				block.updateWidgetBounds(0, 0);
				this.updateGUIComponents();
			} else {
				if (this.getComponentCount() > 0) {
					this.removeAll();
				}
			}
		} catch (Throwable ignored) {
		}
	}

	private String getSelectionText() {
		RenderableSpot start = this.startSelection;
		RenderableSpot end = this.endSelection;
		if (start != null && end != null) {
			StringBuffer buffer = new StringBuffer();
			this.rblock.extractSelectionText(buffer, false, start, end);
			return buffer.toString();
		} else {
			return null;
		}
	}

	protected void paintChildren(Graphics g) {
		// Overridding with NOP. For various reasons,
		// the regular mechanism for painting children
		// needs to be handled by Cobra.
	}

	public Color getPaintedBackgroundColor() {
		return this.isOpaque() ? this.getBackground() : null;
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.ClipboardOwner#lostOwnership(java.awt.datatransfer.Clipboard, java.awt.datatransfer.Transferable)
	 */
	public void lostOwnership(Clipboard arg0, Transferable arg1) {
	}

	public void relayout() {
		// Expected to be called in the GUI thread.
		// Renderable branch should be invalidated at this
		// point, but this GUI component not necessarily.
		this.revalidatePanel();
	}

	public void invalidateLayoutUpTree() {
		// Called when renderable branch is invalidated.
		// We shouldn't do anything here. Changes in renderer
		// tree do not have any bearing on validity of GUI
		// component.
	}

	public void updateAllWidgetBounds() {
		this.rblock.updateWidgetBounds(0, 0);
	}

	public Point getGUIPoint(int clientX, int clientY) {
		// This is the GUI!
		return new Point(clientX, clientY);
	}

	public void focus() {
		this.grabFocus();
	}

	private boolean processingDocumentNotification = false;

	void processDocumentNotifications(DocumentNotification[] notifications) {
		// Called in the GUI thread.
		if (this.processingDocumentNotification) {
			// This should not be possible. Even if
			// Javascript modifies the DOM during
			// parsing, this should be executed in
			// the GUI thread, not the parser thread.
			throw new IllegalStateException("Recursive");
		}
		this.processingDocumentNotification = true;
		try {
			//Note: It may be assumed that usually only generic 
			//notifications come in batches. Other types
			//of noitifications probably come one by one.
			boolean topLayout = false;
			java.util.ArrayList<RElement> repainters = null;
			for (DocumentNotification dn : notifications) {
				int type = dn.type;
				switch (type) {
					case DocumentNotification.GENERIC:
					case DocumentNotification.SIZE: {
						NodeImpl node = dn.node;
						if (node == null) {
							this.rblock.invalidateLayoutDeep();
						} else {
							UINode uiNode = node.findUINode();
							if (uiNode != null) {
								RElement relement = (RElement) uiNode;
								relement.invalidateLayoutUpTree();
							}
						}
						topLayout = true;
						break;
					}
					case DocumentNotification.POSITION: {
						NodeImpl node = dn.node;
						NodeImpl parent = (NodeImpl) node.getParentNode();
						if (parent != null) {
							UINode uiNode = parent.findUINode();
							if (uiNode != null) {
								RElement relement = (RElement) uiNode;
								relement.invalidateLayoutUpTree();
							}
						}
						topLayout = true;
						break;
					}
					case DocumentNotification.LOOK: {
						NodeImpl node = dn.node;
						UINode uiNode = node.findUINode();
						if (uiNode != null) {
							if (repainters == null) {
								repainters = new ArrayList<>(1);
							}
							RElement relement = (RElement) uiNode;
							repainters.add(relement);
						}
						break;
					}
					default:
						break;
				}
			}
			if (topLayout) {
				this.revalidatePanel();
			} else {
				if (repainters != null) {
					for (Object repainter : repainters) {
						RElement element = (RElement) repainter;
						element.repaint();
					}
				}
			}
		} finally {
			this.processingDocumentNotification = false;
		}
	}

	public void addDelayedPair(DelayedPair pair) {
		// NOP
	}

	public RenderableContainer getParentContainer() {
		return null;
	}

	public Collection getDelayedPairs() {
		return null;
	}

	public void clearDelayedPairs() {
	}

	private Set<Component> components;

	private void clearComponents() {
		Set c = this.components;
		if (c != null) {
			c.clear();
		}
	}

	public void addComponent(Component component) {
		Set<Component> c = this.components;
		if (c == null) {
			c = new HashSet<>();
			this.components = c;
		}
		if (c.add(component)) {
		} else {
		}
	}

	private void updateGUIComponents() {
		// We use this method, instead of removing all components and
		// adding them back, because removal of components can cause
		// them to lose focus.

		Set<Component> c = this.components;
		if (c == null) {
			if (this.getComponentCount() != 0) {
				this.removeAll();
			}
		} else {
			// Remove children not in the set.
			Set<Component> workingSet = new HashSet<>();
			workingSet.addAll(c);
			int count = this.getComponentCount();
			for (int i = 0; i < count; ) {
				Component component = this.getComponent(i);
				if (!c.contains(component)) {
					this.remove(i);
					count = this.getComponentCount();
				} else {
					i++;
					workingSet.remove(component);
				}
			}
			// Add components in set that were not previously children.
			for (Component component : workingSet) {
				this.add(component);
			}
		}
	}

	/**
	 * Sets the default margin insets. Note that in the root block,
	 * the margin behaves like padding.
	 *
	 * @param defaultMarginInsets The default margin insets.
	 */
	void setDefaultMarginInsets(Insets defaultMarginInsets) {
		if (!Objects.equals(this.defaultMarginInsets, defaultMarginInsets)) {
			this.defaultMarginInsets = defaultMarginInsets;
			RBlock block = this.rblock;
			if (block != null) {
				block.setDefaultMarginInsets(defaultMarginInsets);
				block.relayoutIfValid();
			}
		}
	}

	void setDefaultOverflowX(int defaultOverflowX) {
		if (defaultOverflowX != this.defaultOverflowX) {
			this.defaultOverflowX = defaultOverflowX;
			RBlock block = this.rblock;
			if (block != null) {
				block.setDefaultOverflowX(defaultOverflowX);
				block.relayoutIfValid();
			}
		}
	}

	void setDefaultOverflowY(int defaultOverflowY) {
		if (this.defaultOverflowY != defaultOverflowY) {
			this.defaultOverflowY = defaultOverflowY;
			RBlock block = this.rblock;
			if (block != null) {
				block.setDefaultOverflowY(defaultOverflowY);
				block.relayoutIfValid();
			}
		}
	}


}