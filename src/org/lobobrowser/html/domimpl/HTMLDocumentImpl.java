/*    GNU LESSER GENERAL PUBLIC LICENSE
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
 * Created on Sep 3, 2005
 */
package org.lobobrowser.html.domimpl;

import org.lobobrowser.html.HtmlRendererContext;
import org.lobobrowser.html.HttpRequest;
import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.io.WritableLineReader;
import org.lobobrowser.html.parser.HtmlParser;
import org.lobobrowser.html.style.RenderState;
import org.lobobrowser.html.style.StyleSheetAggregator;
import org.lobobrowser.html.style.StyleSheetRenderState;
import org.lobobrowser.util.Domains;
import org.lobobrowser.util.Urls;
import org.lobobrowser.util.WeakValueHashMap;
import org.lobobrowser.util.io.EmptyReader;
import org.w3c.dom.*;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.html2.HTMLCollection;
import org.w3c.dom.html2.HTMLDocument;
import org.w3c.dom.html2.HTMLElement;
import org.w3c.dom.html2.HTMLLinkElement;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.*;

/**
 * Implementation of the W3C <code>HTMLDocument</code> interface.
 */
public class HTMLDocumentImpl extends NodeImpl implements HTMLDocument, DocumentView {
	private final ElementFactory factory;
	private final HtmlRendererContext rcontext;
	private final UserAgentContext ucontext;
	private final Map<String, Element> elementsById = new WeakValueHashMap();
	private String documentURI;
	private java.net.URL documentURL;

	private WritableLineReader reader;

	public HTMLDocumentImpl(HtmlRendererContext rcontext) {
		this(rcontext.getUserAgentContext(), rcontext, null, null);
	}

	public HTMLDocumentImpl(UserAgentContext ucontext) {
		this(ucontext, null, null, null);
	}

	public HTMLDocumentImpl(final UserAgentContext ucontext, final HtmlRendererContext rcontext, WritableLineReader reader, String documentURI) {
		this.factory = ElementFactory.getInstance();
		this.rcontext = rcontext;
		this.ucontext = ucontext;
		this.reader = reader;
		this.documentURI = documentURI;
		try {
			java.net.URL docURL = new java.net.URL(documentURI);
			SecurityManager sm = System.getSecurityManager();
			if (sm != null) {
				sm.checkPermission(new java.net.SocketPermission(docURL.getHost(), "connect"));
			}
			this.documentURL = docURL;
			this.domain = docURL.getHost();
		} catch (java.net.MalformedURLException ignored) {
		}
		this.document = this;
	}

	/**
	 * Caller should synchronize on document.
	 */
	void setElementById(String id, Element element) {
		synchronized (this) {
			this.elementsById.put(id, element);
		}
	}

	private volatile String baseURI;

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.NodeImpl#getbaseURI()
	 */
	public String getBaseURI() {
		String buri = this.baseURI;
		return buri == null ? this.documentURI : buri;
	}

	void setBaseURI(String value) {
		this.baseURI = value;
	}

	private String defaultTarget;

	String getDefaultTarget() {
		return this.defaultTarget;
	}

	void setDefaultTarget(String value) {
		this.defaultTarget = value;
	}

	public String getTextContent() throws DOMException {
		return null;
	}

	public void setTextContent(String textContent) throws DOMException {
	}

	private String title;

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private String domain;

	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		String oldDomain = this.domain;
		if (oldDomain != null && Domains.isValidCookieDomain(domain, oldDomain)) {
			this.domain = domain;
		} else {
			throw new SecurityException("Cannot set domain to '" + domain + "' when current domain is '" + oldDomain + "'");
		}
	}

	public HTMLElement getBody() {
		synchronized (this) {
			return this.body;
		}
	}

	private HTMLCollection images;
	private HTMLCollection links;
	private HTMLCollection forms;
	private HTMLCollection frames;

	public HTMLCollection getImages() {
		synchronized (this) {
			if (this.images == null) {
				this.images = new DescendentHTMLCollection(this, new ImageFilter(), this.treeLock);
			}
			return this.images;
		}
	}

	public HTMLCollection getLinks() {
		synchronized (this) {
			if (this.links == null) {
				this.links = new DescendentHTMLCollection(this, new LinkFilter(), this.treeLock);
			}
			return this.links;
		}
	}

	public HTMLCollection getForms() {
		synchronized (this) {
			if (this.forms == null) {
				this.forms = new DescendentHTMLCollection(this, new FormFilter(), this.treeLock);
			}
			return this.forms;
		}
	}

	public HTMLCollection getFrames() {
		synchronized (this) {
			if (this.frames == null) {
				this.frames = new DescendentHTMLCollection(this, new FrameFilter(), this.treeLock);
			}
			return this.frames;
		}
	}

	public String getCookie() {
		SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
			return (String) AccessController.doPrivileged((PrivilegedAction<Object>) () -> ucontext.getCookie(documentURL));
		} else {
			return this.ucontext.getCookie(this.documentURL);
		}
	}

	public void setCookie(final String cookie) throws DOMException {
		SecurityManager sm = System.getSecurityManager();
		if (sm != null) {
			AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
				ucontext.setCookie(documentURL, cookie);
				return null;
			});
		} else {
			this.ucontext.setCookie(this.documentURL, cookie);
		}
	}

	public void open() {
		synchronized (this.treeLock) {
			if (this.reader != null) {
				if (this.reader instanceof LocalWritableLineReader) {
					try {
						this.reader.close();
					} catch (IOException ignored) {
					}
					this.reader = null;
				} else {
					return;
				}
			}
			this.removeAllChildrenImpl();
			this.reader = new LocalWritableLineReader(new EmptyReader());
		}
	}

	/**
	 * Loads the document from the reader provided when the
	 * current instance of <code>HTMLDocumentImpl</code> was constructed.
	 * It then closes the reader.
	 */
	public void load() throws IOException, SAXException {
		this.load(true);
	}

	public void load(boolean closeReader) throws IOException, SAXException {
		WritableLineReader reader;
		synchronized (this.treeLock) {
			this.removeAllChildrenImpl();
			this.setTitle(null);
			this.setBaseURI(null);
			this.setDefaultTarget(null);
			this.styleSheets.clear();
			this.styleSheetAggregator = null;
			reader = this.reader;
		}
		if (reader != null) {
			try {
				HtmlParser parser = new HtmlParser(this.ucontext, this);
				parser.parse(reader);
			} finally {
				if (closeReader) {
					try {
						reader.close();
					} catch (Exception ignored) {
					}
					synchronized (this.treeLock) {
						this.reader = null;
					}
				}
			}
		}
	}

	public void close() {
		synchronized (this.treeLock) {
			if (this.reader instanceof LocalWritableLineReader) {
				try {
					this.reader.close();
				} catch (java.io.IOException ioe) {
					// ignore
				}
				this.reader = null;
			}
		}
	}

	public void write(String text) {
		synchronized (this.treeLock) {
			if (this.reader != null) {
				try {
					this.reader.write(text);
				} catch (IOException ignored) {
				}
			}
		}
	}

	private void openBufferChanged(String text) {
		HtmlParser parser = new HtmlParser(this.ucontext, this);
		StringReader strReader = new StringReader(text);
		try {
			parser.parse(strReader);
		} catch (Exception ignored) {
		}
	}

	private DocumentType doctype;

	public DocumentType getDoctype() {
		return this.doctype;
	}

	public void setDoctype(DocumentType doctype) {
		this.doctype = doctype;
	}

	public Element getDocumentElement() {
		synchronized (this.treeLock) {
			ArrayList<Node> nl = this.nodeList;
			if (nl != null) {
				for (Node node : nl) {
					if (node instanceof Element) {
						return (Element) node;
					}
				}
			}
			return null;
		}
	}

	public Element createElement(String tagName)
			throws DOMException {
		return this.factory.createElement(this, tagName);
	}

	/* (non-Javadoc)
	 * @see org.w3c.dom.Document#createDocumentFragment()
	 */
	public DocumentFragment createDocumentFragment() {
		DocumentFragmentImpl node = new DocumentFragmentImpl();
		node.setOwnerDocument(this);
		return node;
	}

	public Text createTextNode(String data) {
		TextImpl node = new TextImpl(data);
		node.setOwnerDocument(this);
		return node;
	}

	public Comment createComment(String data) {
		CommentImpl node = new CommentImpl(data);
		node.setOwnerDocument(this);
		return node;
	}

	public CDATASection createCDATASection(String data)
			throws DOMException {
		CDataSectionImpl node = new CDataSectionImpl(data);
		node.setOwnerDocument(this);
		return node;
	}

	public ProcessingInstruction createProcessingInstruction(
			String target, String data) throws DOMException {
		HTMLProcessingInstruction node = new HTMLProcessingInstruction(target, data);
		node.setOwnerDocument(this);
		return node;
	}

	public Attr createAttribute(String name) throws DOMException {
		return new AttrImpl(name);
	}

	public EntityReference createEntityReference(String name)
			throws DOMException {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "HTML document");
	}

	/**
	 * Gets all elements that match the given tag name.
	 *
	 * @param tagname The element tag name or an asterisk
	 *                character (*) to match all elements.
	 */
	public NodeList getElementsByTagName(String tagname) {
		if ("*".equals(tagname)) {
			return this.getNodeList(new ElementFilter());
		} else {
			return this.getNodeList(new TagNameFilter(tagname));
		}
	}

	public Node importNode(Node importedNode, boolean deep)
			throws DOMException {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Not implemented");
	}

	public Element createElementNS(String namespaceURI,
	                               String qualifiedName) throws DOMException {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "HTML document");
	}

	public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "HTML document");
	}

	public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "HTML document");
	}

	public Element getElementById(String elementId) {
		Element element;
		synchronized (this) {
			element = this.elementsById.get(elementId);
		}
		return element;
	}

	public String getInputEncoding() {
		return "";
	}

	public String getXmlEncoding() {
		return "";
	}

	private boolean xmlStandalone;

	public boolean getXmlStandalone() {
		return this.xmlStandalone;
	}

	public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
		this.xmlStandalone = xmlStandalone;
	}

	private String xmlVersion = null;

	public String getXmlVersion() {
		return this.xmlVersion;
	}

	public void setXmlVersion(String xmlVersion) throws DOMException {
		this.xmlVersion = xmlVersion;
	}

	private boolean strictErrorChecking = true;

	public boolean getStrictErrorChecking() {
		return this.strictErrorChecking;
	}

	public void setStrictErrorChecking(boolean strictErrorChecking) {
		this.strictErrorChecking = strictErrorChecking;
	}

	public String getDocumentURI() {
		return this.documentURI;
	}

	public void setDocumentURI(String documentURI) {
		this.documentURI = documentURI;
	}

	public Node adoptNode(Node source) throws DOMException {
		if (source instanceof NodeImpl) {
			NodeImpl node = (NodeImpl) source;
			node.setOwnerDocument(this, true);
			return node;
		} else {
			throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "Invalid Node implementation");
		}
	}

	private DOMConfiguration domConfig;

	public DOMConfiguration getDomConfig() {
		synchronized (this) {
			if (this.domConfig == null) {
				this.domConfig = new DOMConfigurationImpl();
			}
			return this.domConfig;
		}
	}

	public void normalizeDocument() {
		synchronized (this.treeLock) {
			this.visitImpl(Node::normalize);
		}
	}

	public Node renameNode(Node n, String namespaceURI,
	                       String qualifiedName) throws DOMException {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "No renaming");
	}

	private DOMImplementation domImplementation;

	/* (non-Javadoc)
	 * @see org.w3c.dom.Document#getImplementation()
	 */
	public DOMImplementation getImplementation() {
		synchronized (this) {
			if (this.domImplementation == null) {
				this.domImplementation = new DOMImplementationImpl(this.ucontext);
			}
			return this.domImplementation;
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.NodeImpl#getLocalName()
	 */
	public String getLocalName() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.NodeImpl#getNodeName()
	 */
	public String getNodeName() {
		return "#document";
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.NodeImpl#getNodeType()
	 */
	public short getNodeType() {
		return Node.DOCUMENT_NODE;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.NodeImpl#getNodeValue()
	 */
	public String getNodeValue() throws DOMException {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.NodeImpl#setNodeValue(java.lang.String)
	 */
	public void setNodeValue(String nodeValue) throws DOMException {
		throw new DOMException(DOMException.INVALID_MODIFICATION_ERR, "Cannot set node value of document");
	}

	public final HtmlRendererContext getHtmlRendererContext() {
		return this.rcontext;
	}

	public UserAgentContext getUserAgentContext() {
		return this.ucontext;
	}

	public final URL getFullURL(String uri) {
		try {
			String baseURI = this.getBaseURI();
			URL documentURL = baseURI == null ? null : new URL(baseURI);
			return Urls.createURL(documentURL, uri);
		} catch (MalformedURLException mfu) {
			// Try agan, without the baseURI.
			try {
				return new URL(uri);
			} catch (MalformedURLException mfu2) {
				return null;
			}
		}
	}

	public String getURL() {
		return this.documentURI;
	}

	private HTMLElement body;

	public void setBody(HTMLElement body) {
		synchronized (this) {
			this.body = body;
		}
	}

	private final Collection<CSSStyleSheet> styleSheets = new CSSStyleSheetList();

	@Override
	public AbstractView getDefaultView() {
		return null;
	}

	public class CSSStyleSheetList extends ArrayList<CSSStyleSheet> {
		public int getLength() {
			return this.size();
		}

		public CSSStyleSheet item(int index) {
			return get(index);
		}
	}

	final void addStyleSheet(CSSStyleSheet ss) {
		synchronized (this.treeLock) {
			this.styleSheets.add(ss);
			this.styleSheetAggregator = null;
			this.forgetRenderState();
			ArrayList<Node> nl = this.nodeList;
			if (nl != null) {
				for (Node node : nl) {
					if (node instanceof HTMLElementImpl) {
						((HTMLElementImpl) node).forgetStyle(true);
					}
				}
			}
		}
		this.allInvalidated();
	}

	void allInvalidated(boolean forgetRenderStates) {
		if (forgetRenderStates) {
			synchronized (this.treeLock) {
				this.styleSheetAggregator = null;
				this.forgetRenderState();
				ArrayList<Node> nl = this.nodeList;
				if (nl != null) {
					for (Node node : nl) {
						if (node instanceof HTMLElementImpl) {
							((HTMLElementImpl) node).forgetStyle(true);
						}
					}
				}
			}
		}
		this.allInvalidated();
	}

	private StyleSheetAggregator styleSheetAggregator = null;

	final StyleSheetAggregator getStyleSheetAggregator() {
		synchronized (this.treeLock) {
			StyleSheetAggregator ssa = this.styleSheetAggregator;
			if (ssa == null) {
				ssa = new StyleSheetAggregator(this);
				try {
					ssa.addStyleSheets(this.styleSheets);
				} catch (MalformedURLException ignored) {
				}
				this.styleSheetAggregator = ssa;
			}
			return ssa;
		}
	}

	private final ArrayList<DocumentNotificationListener> documentNotificationListeners = new ArrayList<>(1);

	/**
	 * Adds a document notification listener, which is informed about
	 * changes to the document.
	 *
	 * @param listener An instance of {@link DocumentNotificationListener}.
	 */
	public void addDocumentNotificationListener(DocumentNotificationListener listener) {
		synchronized (this.documentNotificationListeners) {
			this.documentNotificationListeners.add(listener);
		}
	}

	public void removeDocumentNotificationListener(DocumentNotificationListener listener) {
		synchronized (this.documentNotificationListeners) {
			this.documentNotificationListeners.remove(listener);
		}
	}

	void sizeInvalidated(NodeImpl node) {
		ArrayList<DocumentNotificationListener> listenersList = this.documentNotificationListeners;
		int size;
		synchronized (listenersList) {
			size = listenersList.size();
		}
		for (int i = 0; i < size; i++) {
			try {
				DocumentNotificationListener dnl = listenersList.get(i);
				dnl.sizeInvalidated(node);
			} catch (IndexOutOfBoundsException ignored) {
			}
		}
	}

	/**
	 * Called if something such as a color or
	 * decoration has changed. This would be
	 * something which does not affect the
	 * rendered size, and can be revalidated
	 * with a simple repaint.
	 */
	void lookInvalidated(NodeImpl node) {
		ArrayList<DocumentNotificationListener> listenersList = this.documentNotificationListeners;
		int size;
		synchronized (listenersList) {
			size = listenersList.size();
		}
		for (int i = 0; i < size; i++) {
			try {
				DocumentNotificationListener dnl = listenersList.get(i);
				dnl.lookInvalidated(node);
			} catch (IndexOutOfBoundsException ignored) {
			}
		}

	}

	/**
	 * Changed if the position of the node in a
	 * parent has changed.
	 */
	void positionInParentInvalidated(NodeImpl node) {
		ArrayList<DocumentNotificationListener> listenersList = this.documentNotificationListeners;
		int size;
		synchronized (listenersList) {
			size = listenersList.size();
		}
		for (int i = 0; i < size; i++) {
			try {
				DocumentNotificationListener dnl = listenersList.get(i);
				dnl.positionInvalidated(node);
			} catch (IndexOutOfBoundsException ignored) {
			}
		}
	}

	/**
	 * This is called when the node has changed, but
	 * it is unclear if it's a size change or a look
	 * change. An attribute change should trigger this.
	 */
	public void invalidated(NodeImpl node) {
		ArrayList<DocumentNotificationListener> listenersList = this.documentNotificationListeners;
		int size;
		synchronized (listenersList) {
			size = listenersList.size();
		}
		for (int i = 0; i < size; i++) {
			try {
				DocumentNotificationListener dnl = listenersList.get(i);
				dnl.invalidated(node);
			} catch (IndexOutOfBoundsException ignored) {
			}
		}
	}

	/**
	 * This is called when children of the node might
	 * have changed.
	 */
	void structureInvalidated(NodeImpl node) {
		ArrayList<DocumentNotificationListener> listenersList = this.documentNotificationListeners;
		int size;
		synchronized (listenersList) {
			size = listenersList.size();
		}
		for (int i = 0; i < size; i++) {
			try {
				DocumentNotificationListener dnl = listenersList.get(i);
				dnl.structureInvalidated(node);
			} catch (IndexOutOfBoundsException ignored) {
			}
		}
	}

	void nodeLoaded(NodeImpl node) {
		ArrayList<DocumentNotificationListener> listenersList = this.documentNotificationListeners;
		int size;
		synchronized (listenersList) {
			size = listenersList.size();
		}
		for (int i = 0; i < size; i++) {
			try {
				DocumentNotificationListener dnl = listenersList.get(i);
				dnl.nodeLoaded(node);
			} catch (IndexOutOfBoundsException ignored) {
			}
		}
	}

	/**
	 * Informs listeners that the whole document has been
	 * invalidated.
	 */
	private void allInvalidated() {
		ArrayList<DocumentNotificationListener> listenersList = this.documentNotificationListeners;
		int size;
		synchronized (listenersList) {
			size = listenersList.size();
		}
		for (int i = 0; i < size; i++) {
			try {
				DocumentNotificationListener dnl = listenersList.get(i);
				dnl.allInvalidated();
			} catch (IndexOutOfBoundsException ignored) {
			}
		}
	}

	protected RenderState createRenderState(RenderState prevRenderState) {
		return new StyleSheetRenderState(this);
	}

	private final Map<String, ImageInfo> imageInfos = new HashMap<>(4);
	private final ImageEvent BLANK_IMAGE_EVENT = new ImageEvent(this, null);

	/**
	 * Loads images asynchronously such that they are shared if loaded
	 * simultaneously from the same URI.
	 * Informs the listener immediately if an image is already known.
	 */
	void loadImage(String relativeUri, ImageListener imageListener) {
		HtmlRendererContext rcontext = this.getHtmlRendererContext();
		if (rcontext == null || !rcontext.isImageLoadingEnabled()) {
			// Ignore image loading when there's no renderer context.
			// Consider Cobra users who are only using the parser.
			imageListener.imageLoaded(BLANK_IMAGE_EVENT);
			return;
		}
		final URL url = this.getFullURL(relativeUri);
		if (url == null) {
			imageListener.imageLoaded(BLANK_IMAGE_EVENT);
			return;
		}
		final String urlText = url.toExternalForm();
		final Map<String, ImageInfo> map = this.imageInfos;
		ImageEvent event = null;
		synchronized (map) {
			ImageInfo info = map.get(urlText);
			if (info != null) {
				if (info.loaded) {
					event = info.imageEvent;
				} else {
					info.addListener(imageListener);
				}
			} else {
				UserAgentContext uac = rcontext.getUserAgentContext();
				final HttpRequest httpRequest = uac.createHttpRequest();
				final ImageInfo newInfo = new ImageInfo();
				map.put(urlText, newInfo);
				newInfo.addListener(imageListener);
				httpRequest.addReadyStateChangeListener(() -> {
					if (httpRequest.getReadyState() == HttpRequest.STATE_COMPLETE) {
						java.awt.Image newImage = httpRequest.getResponseImage();
						ImageEvent newEvent = newImage == null ? null : new ImageEvent(HTMLDocumentImpl.this, newImage);
						ImageListener[] listeners;
						synchronized (map) {
							newInfo.imageEvent = newEvent;
							newInfo.loaded = true;
							listeners = newEvent == null ? null : newInfo.getListeners();
							// Must remove from map in the locked block
							// that got the listeners. Otherwise a new
							// listener might miss the event??
							map.remove(urlText);
						}
						if (listeners != null) {
							for (ImageListener listener : listeners) {
								// Call holding no locks
								listener.imageLoaded(newEvent);
							}
						}
					}
				});
				SecurityManager sm = System.getSecurityManager();
				if (sm == null) {
					try {
						httpRequest.open("GET", url, true);
						httpRequest.send(null);
					} catch (java.io.IOException ignored) {
					}
				} else {
					AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
						// Code might have restrictions on accessing
						// items from elsewhere.
						try {
							httpRequest.open("GET", url, true);
							httpRequest.send(null);
						} catch (IOException ignored) {
						}
						return null;
					});
				}
			}
		}
		if (event != null) {
			// Call holding no locks.
			imageListener.imageLoaded(event);
		}
	}

	protected Node createSimilarNode() {
		return new HTMLDocumentImpl(this.ucontext, this.rcontext, this.reader, this.documentURI);
	}

	private static class ImageInfo {
		ImageEvent imageEvent;
		public boolean loaded;
		private ArrayList<ImageListener> listeners = new ArrayList<>(1);

		void addListener(ImageListener listener) {
			this.listeners.add(listener);
		}

		ImageListener[] getListeners() {
			return this.listeners.toArray(ImageListener.EMPTY_ARRAY);
		}
	}

	private class ImageFilter implements NodeFilter {
		public boolean accept(Node node) {
			return "IMG".equalsIgnoreCase(node.getNodeName());
		}
	}

	private class LinkFilter implements NodeFilter {
		public boolean accept(Node node) {
			return node instanceof HTMLLinkElement;
		}
	}

	private class FormFilter implements NodeFilter {
		public boolean accept(Node node) {
			String nodeName = node.getNodeName();
			return "FORM".equalsIgnoreCase(nodeName);
		}
	}

	private class FrameFilter implements NodeFilter {
		public boolean accept(Node node) {
			return node instanceof org.w3c.dom.html2.HTMLFrameElement ||
					node instanceof org.w3c.dom.html2.HTMLIFrameElement;
		}
	}

	private class ElementFilter implements NodeFilter {
		ElementFilter() {
		}

		public boolean accept(Node node) {
			return node instanceof Element;
		}
	}

	private class TagNameFilter implements NodeFilter {
		private final String name;

		TagNameFilter(String name) {
			this.name = name;
		}

		public boolean accept(Node node) {
			return node instanceof Element && this.name.equalsIgnoreCase(((Element) node).getTagName());
		}
	}

	/**
	 * Tag class that also notifies document
	 * when text is written to an open buffer.
	 *
	 * @author J. H. S.
	 */
	private class LocalWritableLineReader extends WritableLineReader {

		LocalWritableLineReader(Reader reader) {
			super(reader);
		}

		public void write(String text) throws IOException {
			super.write(text);
			if ("".equals(text)) {
				openBufferChanged(text);
			}
		}
	}
}
