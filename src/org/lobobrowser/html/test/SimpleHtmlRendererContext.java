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
 * Created on Oct 22, 2005
 */
package org.lobobrowser.html.test;

import org.lobobrowser.html.*;
import org.lobobrowser.html.domimpl.FrameNode;
import org.lobobrowser.html.domimpl.HTMLDocumentImpl;
import org.lobobrowser.html.gui.HtmlPanel;
import org.lobobrowser.html.parser.DocumentBuilderImpl;
import org.lobobrowser.html.parser.InputSourceImpl;
import org.lobobrowser.util.Urls;
import org.lobobrowser.util.io.BufferExceededException;
import org.lobobrowser.util.io.RecordedInputStream;
import org.w3c.dom.html2.HTMLCollection;
import org.w3c.dom.html2.HTMLElement;
import org.w3c.dom.html2.HTMLLinkElement;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.*;

/**
 * The <code>SimpleHtmlRendererContext</code> class implements
 * the {@link org.lobobrowser.html.HtmlRendererContext} interface.
 * Note that this class provides rudimentary implementations
 * of most callback methods. Overridding some of the methods
 * in this class will usually be necessary in a professional application.
 * <p>
 */
public class SimpleHtmlRendererContext implements HtmlRendererContext {

	private final HtmlPanel htmlPanel;
	private final HtmlRendererContext parentRcontext;

	/**
	 * Constructs a SimpleHtmlRendererContext.
	 *
	 * @param contextComponent The component that will render HTML.
	 * @deprecated Use constructor that takes <code>HtmlPanel</code> and <code>UserAgentContext</code>
	 */
	public SimpleHtmlRendererContext(HtmlPanel contextComponent) {
		this(contextComponent, (UserAgentContext) null);
	}

	/**
	 * Constructs a SimpleHtmlRendererContext.
	 *
	 * @param contextComponent The component that will render HTML.
	 * @see SimpleUserAgentContext
	 */
	public SimpleHtmlRendererContext(HtmlPanel contextComponent, UserAgentContext ucontext) {
		super();
		this.htmlPanel = contextComponent;
		this.parentRcontext = null;
		this.bcontext = ucontext;
	}

	/**
	 * Constructs a SimpleHtmlRendererContext that is a child of another
	 * <code>{@link HtmlRendererContext}</code>.
	 *
	 * @param contextComponent The component that will render HTML.
	 * @param parentRcontext   The parent's renderer context.
	 */
	SimpleHtmlRendererContext(HtmlPanel contextComponent, HtmlRendererContext parentRcontext) {
		super();
		this.htmlPanel = contextComponent;
		this.parentRcontext = parentRcontext;
		this.bcontext = parentRcontext == null ? null : parentRcontext.getUserAgentContext();
	}

	/**
	 * Gets a collection of current document frames, by querying
	 * the document currently held by the local
	 * {@link org.lobobrowser.html.gui.HtmlPanel}
	 * instance.
	 */
	public HTMLCollection getFrames() {
		Object rootNode = this.htmlPanel.getRootNode();
		if (rootNode instanceof HTMLDocumentImpl) {
			return ((HTMLDocumentImpl) rootNode).getFrames();
		} else {
			return null;
		}
	}

	/**
	 * Implements the link click handler by invoking {@link #navigate(URL, String)}.
	 */
	public void linkClicked(HTMLElement linkNode, URL url, String target) {
		this.navigate(url, target);
	}

	/**
	 * Gets the connection proxy used in {@link #navigate(URL, String)}.
	 * This implementation calls {@link SimpleUserAgentContext#getProxy()}
	 * if {@link #getUserAgentContext()} returns an instance assignable to {@link SimpleUserAgentContext}.
	 * The method may be overridden to provide a different proxy setting.
	 */
	protected Proxy getProxy() {
		Object ucontext = this.getUserAgentContext();
		if (ucontext instanceof SimpleUserAgentContext) {
			return ((SimpleUserAgentContext) ucontext).getProxy();
		}
		return Proxy.NO_PROXY;
	}

	/**
	 * Implements simple navigation with incremental
	 * rendering by invoking {@link #submitForm(String, URL, String, String, FormInput[])}
	 * with a <code>GET</code> request method.
	 */
	public void navigate(final URL href, String target) {
		this.submitForm("GET", href, target, null, null);
	}

	/**
	 * Implements simple navigation and form submission with incremental
	 * rendering and target processing, including
	 * frame lookup. Should be overridden to allow for
	 * more robust browser navigation and form submission.
	 * <p>
	 * <b>Notes:</b>
	 * <ul>
	 * <li>Document encoding is defined by {@link #getDocumentCharset(URLConnection)}.
	 * <li>Caching is not implemented.
	 * <li>Cookies are not implemented.
	 * <li>Incremental rendering is not optimized for
	 * ignorable document change notifications.
	 * <li>Other HTTP features are not implemented.
	 * <li>The only form encoding type supported is <code>application/x-www-form-urlencoded</code>.
	 * <li>Navigation is normally asynchronous. See {@link #isNavigationAsynchronous()}.
	 * </ul>
	 *
	 * @see #navigate(URL, String)
	 */
	public void submitForm(final String method, final java.net.URL action, final String target, final String enctype, final FormInput[] formInputs) {
		// This method implements simple incremental rendering.
		if (target != null) {
			HtmlRendererContext topCtx = this.getTop();
			HTMLCollection frames = topCtx.getFrames();
			if (frames != null) {
				org.w3c.dom.Node frame = frames.namedItem(target);
				if (frame instanceof FrameNode) {
					BrowserFrame bframe = ((FrameNode) frame).getBrowserFrame();
					if (bframe == null) {
						throw new IllegalStateException("Frame node without a BrowserFrame instance: " + frame);
					}
					if (bframe.getHtmlRendererContext() != this) {
						bframe.loadURL(action);
						return;
					}
				}
			}
			String actualTarget = target.trim().toLowerCase();
			if ("_top".equals(actualTarget)) {
				this.getTop().navigate(action, null);
				return;
			} else if ("_parent".equals(actualTarget)) {
				HtmlRendererContext parent = this.getParent();
				if (parent != null) {
					parent.navigate(action, null);
					return;
				}
			} else if ("_blank".equals(actualTarget)) {
				this.open(action, "cobra.blank", "", false);
				return;
			}
		}

		// Make request asynchronously.
		if (this.isNavigationAsynchronous()) {
			new Thread(() -> {
				try {
					SimpleHtmlRendererContext.this.submitFormSync(method, action, target, formInputs);
				} catch (Exception ignored) {
				}
			}).start();
		} else {
			try {
				SimpleHtmlRendererContext.this.submitFormSync(method, action, target, formInputs);
			} catch (Exception ignored) {
			}
		}
	}

	/**
	 * Indicates whether navigation (via {@link #submitForm(String, URL, String, String, FormInput[])}) should be asynchronous.
	 * This overridable implementation returns <code>true</code>.
	 */
	private boolean isNavigationAsynchronous() {
		return true;
	}

	/**
	 * Submits a form and/or navigates by making
	 * a <i>synchronous</i> request. This method is invoked
	 * by {@link #submitForm(String, URL, String, String, FormInput[])}.
	 *
	 * @param method     The request method.
	 * @param action     The action URL.
	 * @param target     The target identifier.
	 * @param formInputs The form inputs.
	 * @throws IOException io
	 * @throws org.xml.sax.SAXException xml
	 * @see #submitForm(String, URL, String, String, FormInput[])
	 */
	private void submitFormSync(final String method, final java.net.URL action, final String target, final FormInput[] formInputs) throws IOException, org.xml.sax.SAXException {
		final String actualMethod = method.toUpperCase();
		URL resolvedURL;
		if ("GET".equals(actualMethod) && formInputs != null) {
			boolean firstParam = true;
			URL noRefAction = new URL(action.getProtocol(), action.getHost(), action.getPort(), action.getFile());
			StringBuilder newUrlBuffer = new StringBuilder(noRefAction.toExternalForm());
			if (action.getQuery() == null) {
				newUrlBuffer.append("?");
			} else {
				newUrlBuffer.append("&");
			}
			for (FormInput parameter : formInputs) {
				String name = parameter.getName();
				String encName = URLEncoder.encode(name, "UTF-8");
				if (parameter.isText()) {
					if (firstParam) {
						firstParam = false;
					} else {
						newUrlBuffer.append("&");
					}
					String valueStr = parameter.getTextValue();
					String encValue = URLEncoder.encode(valueStr, "UTF-8");
					newUrlBuffer.append(encName);
					newUrlBuffer.append("=");
					newUrlBuffer.append(encValue);
				}
			}
			resolvedURL = new java.net.URL(newUrlBuffer.toString());
		} else {
			resolvedURL = action;
		}
		URL urlForLoading;
		if (resolvedURL.getProtocol().equalsIgnoreCase("file")) {
			// Remove query so it works.
			try {
				String ref = action.getRef();
				String refText = ref == null || ref.length() == 0 ? "" : "#" + ref;
				urlForLoading = new URL(resolvedURL.getProtocol(), action.getHost(), action.getPort(), action.getPath() + refText);
			} catch (java.net.MalformedURLException throwable) {
				urlForLoading = action;
			}
		} else {
			urlForLoading = resolvedURL;
		}
		// Using potentially different URL for loading.
		Proxy proxy = SimpleHtmlRendererContext.this.getProxy();
		boolean isPost = "POST".equals(actualMethod);
		URLConnection connection = proxy == null || proxy == Proxy.NO_PROXY ? urlForLoading.openConnection() : urlForLoading.openConnection(proxy);

		connection.setRequestProperty("User-Agent", getUserAgentContext().getUserAgent());
		connection.setRequestProperty("Cookie", "");
		if (connection instanceof HttpURLConnection) {
			HttpURLConnection hc = (HttpURLConnection) connection;
			hc.setRequestMethod(actualMethod);
			hc.setInstanceFollowRedirects(false);
		}
		if (isPost) {
			connection.setDoOutput(true);
			ByteArrayOutputStream bufOut = new ByteArrayOutputStream();
			boolean firstParam = true;
			if (formInputs != null) {
				for (FormInput parameter : formInputs) {
					String name = parameter.getName();
					String encName = URLEncoder.encode(name, "UTF-8");
					if (parameter.isText()) {
						if (firstParam) {
							firstParam = false;
						} else {
							bufOut.write((byte) '&');
						}
						String valueStr = parameter.getTextValue();
						String encValue = URLEncoder.encode(valueStr, "UTF-8");
						bufOut.write(encName.getBytes("UTF-8"));
						bufOut.write((byte) '=');
						bufOut.write(encValue.getBytes("UTF-8"));
					}
				}
			}
			// Do not add a line break to post content. Some servers
			// can be picky about that (namely, java.net).
			byte[] postContent = bufOut.toByteArray();
			if (connection instanceof HttpURLConnection) {
				((HttpURLConnection) connection).setFixedLengthStreamingMode(postContent.length);
			}
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			//connection.setRequestProperty("Content-Length", String.valueOf(postContent.length));
			OutputStream postOut = connection.getOutputStream();
			postOut.write(postContent);
			postOut.flush();
		}
		if (connection instanceof HttpURLConnection) {
			HttpURLConnection hc = (HttpURLConnection) connection;
			int responseCode = hc.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == HttpURLConnection.HTTP_SEE_OTHER) {
				String location = hc.getHeaderField("Location");
				if (location != null) {
					URL href;
					href = Urls.createURL(action, location);
					SimpleHtmlRendererContext.this.navigate(href, target);
				}
				return;
			}
		}
		try (InputStream in = connection.getInputStream()) {
			RecordedInputStream rin = new RecordedInputStream(in, 1000000);
			InputStream bin = new BufferedInputStream(rin, 8192);
			String actualURI = urlForLoading.toExternalForm();
			// Only create document, don't parse.
			HTMLDocumentImpl document = this.createDocument(new InputSourceImpl(bin, actualURI, getDocumentCharset(connection)));
			// Set document in HtmlPanel. Safe to call outside GUI thread.
			HtmlPanel panel = htmlPanel;
			panel.setDocument(document, SimpleHtmlRendererContext.this);
			// Now start loading.
			document.load();
			String ref = urlForLoading.getRef();
			if (ref != null && ref.length() != 0) {
				panel.scrollToElement(ref);
			}
			try {
				rin.getString();
			} catch (BufferExceededException ignored) {
			}
		}
	}

	/**
	 * Creates a blank document instance. This method
	 * is invoked whenever navigation or form submission
	 * occur. It is provided so it can be overridden
	 * to create specialized document implmentations.
	 *
	 * @param inputSource The document input source.
	 * @throws IOException io
	 * @throws org.xml.sax.SAXException xml
	 */
	private HTMLDocumentImpl createDocument(org.xml.sax.InputSource inputSource) throws IOException, org.xml.sax.SAXException {
		DocumentBuilderImpl builder = new DocumentBuilderImpl(this.getUserAgentContext(), SimpleHtmlRendererContext.this);
		return (HTMLDocumentImpl) builder.createDocument(inputSource);
	}

	/**
	 * This method is invoked by {@link #submitForm(String, URL, String, String, FormInput[])}
	 * to determine the charset of a document. The charset is determined by looking
	 * at the <code>Content-Type</code> header.
	 *
	 * @param connection A URL connection.
	 */
	private String getDocumentCharset(URLConnection connection) {
		String encoding = Urls.getCharset(connection);
		return encoding == null ? "ISO-8859-1" : encoding;
	}

	// Methods useful to Window below:

	/**
	 * Opens a simple message dialog.
	 */
	public void alert(String message) {
		JOptionPane.showMessageDialog(this.htmlPanel, message);
	}

	/**
	 * It should give up focus on the current browser window. This implementation does nothing
	 * and should be overridden.
	 */
	public void blur() {
	}

	/**
	 * It should close the current browser window. This implementation does nothing
	 * and should be overridden.
	 */
	public void close() {
	}

	/**
	 * It should request focus for the current browser window. This implementation does nothing
	 * and should be overridden.
	 */
	public void focus() {
	}

	/**
	 * @deprecated Use {@link #open(URL, String, String, boolean)}.
	 */
	public final HtmlRendererContext open(String url, String windowName, String windowFeatures, boolean replace) {
		URL urlObj;
		try {
			urlObj = new URL(url);
		} catch (MalformedURLException mfu) {
			throw new IllegalArgumentException("Malformed URL: " + url);
		}
		return this.open(urlObj, windowName, windowFeatures, replace);
	}

	/**
	 * It should open a new browser window. This implementation does nothing
	 * and should be overridden.
	 *
	 * @param url            The requested URL.
	 * @param windowName     A window identifier.
	 * @param windowFeatures Window features specified in a format equivalent to
	 *                       that of window.open() in Javascript.
	 * @param replace        Whether an existing window with the same name should be replaced.
	 */
	public HtmlRendererContext open(java.net.URL url, String windowName, String windowFeatures, boolean replace) {
		return null;
	}

	/**
	 * It should return the name of the browser window, if this
	 * renderer context is for the top frame in the window. This
	 * implementation returns a blank string, so it should be overridden.
	 */
	public String getName() {
		return "";
	}

	public HtmlRendererContext getParent() {
		return this.parentRcontext;
	}

	public String getStatus() {
		return "";
	}

	public void setStatus(String message) {
	}

	public HtmlRendererContext getTop() {
		HtmlRendererContext ancestor = this.parentRcontext;
		if (ancestor == null) {
			return this;
		}
		return ancestor.getTop();
	}

	public BrowserFrame createBrowserFrame() {
		return new SimpleBrowserFrame(this);
	}

	/**
	 * Returns <code>null</code>. This method should be overridden
	 * to provide OBJECT, EMBED or APPLET functionality.
	 */
	public HtmlObject getHtmlObject(HTMLElement element) {
		return null;
	}

	private UserAgentContext bcontext = null;

	/**
	 * If a {@link org.lobobrowser.html.UserAgentContext} instance
	 * was provided in the constructor, then that instance is returned.
	 * Otherwise, an instance of {@link SimpleUserAgentContext} is
	 * created and returned.
	 * <p>
	 * The context returned by this method is used by local request
	 * facilities and other parts of the renderer.
	 */
	public UserAgentContext getUserAgentContext() {
		synchronized (this) {
			if (this.bcontext == null) {
				this.bcontext = new SimpleUserAgentContext();
			}
			return this.bcontext;
		}
	}

	/**
	 * Should be overridden to return true if the link
	 * has been visited.
	 */
	public boolean isVisitedLink(HTMLLinkElement link) {
		return false;
	}

	/**
	 * This method must be overridden to implement a context menu.
	 */
	public boolean onContextMenu(HTMLElement element, MouseEvent event) {
		return true;
	}

	/**
	 * This method can be overridden to receive notifications when the
	 * mouse leaves an element.
	 */
	public void onMouseOut(HTMLElement element, MouseEvent event) {
	}

	/**
	 * This method can be overridden to receive notifications when the
	 * mouse first enters an element.
	 */
	public void onMouseOver(HTMLElement element, MouseEvent event) {
	}

	public boolean isImageLoadingEnabled() {
		return true;
	}

	public boolean onDoubleClick(HTMLElement element, MouseEvent event) {
		return true;
	}

	public boolean onMouseClick(HTMLElement element, MouseEvent event) {
		return true;
	}

	/**
	 * It should navigate back one page. This implementation does nothing
	 * and should be overridden.
	 */
	public void back() {
	}

	public void forward() {
	}

}
