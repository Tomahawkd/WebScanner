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
package org.lobobrowser.html.test;

import org.lobobrowser.html.HttpRequest;
import org.lobobrowser.html.ReadyStateChangeListener;
import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.util.EventDispatch;
import org.lobobrowser.util.Urls;
import org.lobobrowser.util.io.IORoutines;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/**
 * The <code>SimpleHttpRequest</code> class implements
 * the {@link org.lobobrowser.html.HttpRequest} interface.
 * The <code>HttpRequest</code> implementation provided
 * by this class is simple, with no caching. It creates
 * a new thread for each new asynchronous request.
 *
 * @author J. H. S.
 */
public class SimpleHttpRequest implements HttpRequest {
	private int readyState;
	private int status;
	private byte[] responseBytes;
	private final UserAgentContext context;
	private final Proxy proxy;

	private boolean isAsync;
	private java.net.URL requestURL;
	private String requestMethod;

	/**
	 * The <code>URLConnection</code> is assigned to
	 * this field while it is ongoing.
	 */
	protected java.net.URLConnection connection;


	SimpleHttpRequest(UserAgentContext context, java.net.Proxy proxy) {
		super();
		this.context = context;
		this.proxy = proxy;
	}

	public synchronized int getReadyState() {
		return this.readyState;
	}

	public synchronized String getResponseText() {
		byte[] bytes = this.responseBytes;
		java.net.URLConnection connection = this.connection;
		String encoding = connection == null ? "ISO-8859-1" : Urls.getCharset(connection);
		if (encoding == null) {
			encoding = "ISO-8859-1";
		}
		try {
			return bytes == null ? null : new String(bytes, encoding);
		} catch (UnsupportedEncodingException uee) {
			try {
				return new String(bytes, "ISO-8859-1");
			} catch (UnsupportedEncodingException uee2) {
				// Ignore this time
				return null;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.HttpRequest#getResponseImage()
	 */
	public synchronized Image getResponseImage() {
		byte[] bytes = this.responseBytes;
		if (bytes == null) {
			return null;
		}
		return Toolkit.getDefaultToolkit().createImage(bytes);
	}

	public synchronized int getStatus() {
		return this.status;
	}

	public void abort() {
		URLConnection c;
		synchronized (this) {
			c = this.connection;
		}
		if (c instanceof HttpURLConnection) {
			((HttpURLConnection) c).disconnect();
		} else if (c != null) {
			try {
				c.getInputStream().close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	public void open(String method, String url) throws IOException {
		this.open(method, url, true);
	}

	public void open(String method, URL url) throws IOException {
		this.open(method, url, true, null, null);
	}

	public void open(String method, URL url, boolean asyncFlag) throws IOException {
		this.open(method, url, asyncFlag, null, null);
	}

	public void open(String method, String url, boolean asyncFlag) throws IOException {
		URL urlObj = Urls.createURL(null, url);
		this.open(method, urlObj, asyncFlag, null);
	}

	public void open(String method, java.net.URL url, boolean asyncFlag,
	                 String userName) throws IOException {
		this.open(method, url, asyncFlag, userName, null);
	}

	/**
	 * Opens the request. Call {@link #send(String)} to complete it.
	 *
	 * @param method    The request method.
	 * @param url       The request URL.
	 * @param asyncFlag Whether the request should be asynchronous.
	 * @param userName  The user name of the request (not supported.)
	 * @param password  The password of the request (not supported.)
	 */
	public void open(final String method, final java.net.URL url, boolean asyncFlag,
	                 final String userName, final String password) throws java.io.IOException {
		this.abort();
		Proxy proxy = this.proxy;
		URLConnection c = proxy == null || proxy == Proxy.NO_PROXY ? url.openConnection() : url.openConnection(proxy);
		synchronized (this) {
			this.connection = c;
			this.isAsync = asyncFlag;
			this.requestMethod = method;
			this.requestURL = url;
		}
		this.changeState(HttpRequest.STATE_LOADING, 0, null);
	}

	/**
	 * Sends POST content, if any, and causes the request
	 * to proceed.
	 * <p>
	 * In the case of asynchronous requests, a new thread
	 * is created.
	 *
	 * @param content POST content or <code>null</code> if there's no such content.
	 */
	public void send(final String content) throws java.io.IOException {
		final java.net.URL url = this.requestURL;
		if (url == null) {
			throw new IOException("No URL has been provided.");
		}
		if (this.isAsync) {
			// Should use a thread pool instead
			new Thread("SimpleHttpRequest-" + url.getHost()) {
				public void run() {
					try {
						sendSync(content);
					} catch (Throwable ignored) {
					}
				}
			}.start();
		} else {
			sendSync(content);
		}
	}

	/**
	 * This is the charset used to post data provided
	 * to {@link #send(String)}. It returns "UTF-8" unless overridden.
	 */
	private String getPostCharset() {
		return "UTF-8";
	}

	/**
	 * This is a synchronous implementation of {@link #send(String)} method
	 * functionality.
	 * It may be overridden to change the behavior of the class.
	 *
	 * @param content POST content if any. It may be <code>null</code>.
	 * @throws IOException io
	 */
	private void sendSync(String content) throws IOException {
		try {
			// FireFox posts a "loading" state twice as well.
			this.changeState(HttpRequest.STATE_LOADING, 0, null);
			URLConnection c;
			synchronized (this) {
				c = this.connection;
			}
			c.setRequestProperty("User-Agent", this.context.getUserAgent());
			int istatus;
			java.io.InputStream err;
			if (c instanceof HttpURLConnection) {
				HttpURLConnection hc = (HttpURLConnection) c;
				String method = this.requestMethod;
				if (method == null) {
					throw new java.io.IOException("Null method.");
				}
				method = method.toUpperCase();
				hc.setRequestMethod(method);
				if ("POST".equals(method) && content != null) {
					hc.setDoOutput(true);
					byte[] contentBytes = content.getBytes(this.getPostCharset());
					hc.setFixedLengthStreamingMode(contentBytes.length);
					OutputStream out = hc.getOutputStream();
					try {
						out.write(contentBytes);
					} finally {
						out.flush();
					}
				}
				istatus = hc.getResponseCode();
				hc.getResponseMessage();
				err = hc.getErrorStream();
			} else {
				istatus = 0;
				err = null;
			}
			synchronized (this) {
				/*
	  Response headers are set in this string after
	  a response is received.
	 */
				this.getAllResponseHeaders(c);
				c.getHeaderFields();
			}
			this.changeState(HttpRequest.STATE_LOADED, istatus, null);
			java.io.InputStream in = err == null ? c.getInputStream() : err;
			int contentLength = c.getContentLength();
			this.changeState(HttpRequest.STATE_INTERACTIVE, istatus, null);
			byte[] bytes = IORoutines.load(in, contentLength == -1 ? 4096 : contentLength);
			this.changeState(HttpRequest.STATE_COMPLETE, istatus, bytes);
		} finally {
			synchronized (this) {
				this.connection = null;
			}
		}
	}

	private final EventDispatch readyEvent = new EventDispatch();

	public void addReadyStateChangeListener(final ReadyStateChangeListener listener) {
		readyEvent.addListener(event -> listener.readyStateChanged());
	}

	private void changeState(int readyState, int status, byte[] bytes) {
		synchronized (this) {
			this.readyState = readyState;
			this.status = status;
			this.responseBytes = bytes;
		}
		this.readyEvent.fireEvent(null);
	}

	private String getAllResponseHeaders(URLConnection c) {
		int idx = 0;
		String value;
		StringBuilder buf = new StringBuilder();
		while ((value = c.getHeaderField(idx)) != null) {
			String key = c.getHeaderFieldKey(idx);
			buf.append(key);
			buf.append(": ");
			buf.append(value);
			idx++;
		}
		return buf.toString();
	}
}
