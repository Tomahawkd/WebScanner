package org.lobobrowser.html.test;

import org.lobobrowser.html.HttpRequest;
import org.lobobrowser.html.UserAgentContext;

import java.util.*;

/**
 * Simple implementation of {@link org.lobobrowser.html.UserAgentContext}.
 * This class is provided for user convenience.
 * Usually this class should be extended in order to provide appropriate
 * user agent information and more robust content loading routines.
 * Its setters can be called to modify certain user agent defaults.
 */
public class SimpleUserAgentContext implements UserAgentContext {
	private static final Set<String> mediaNames = new HashSet<>();

	static {
		// Media names claimed by this context.
		Set<String> mn = mediaNames;
		mn.add("screen");
		mn.add("tv");
		mn.add("tty");
		mn.add("all");
	}

	/**
	 * This implementation returns true for certain media names,
	 * such as <code>screen</code>.
	 */
	public boolean isMedia(String mediaName) {
		return mediaNames.contains(mediaName.toLowerCase());
	}

	/**
	 * Creates a {@link org.lobobrowser.html.test.SimpleHttpRequest} instance.
	 * The {@link org.lobobrowser.html.HttpRequest}</code> object returned by this method is
	 * used to load images, scripts, style sheets, and to implement
	 * the Javascript XMLHttpRequest class.
	 * Override if a custom mechanism to make requests is needed.
	 */
	public HttpRequest createHttpRequest() {
		return new SimpleHttpRequest(this, this.getProxy());
	}

	private java.net.Proxy proxy = java.net.Proxy.NO_PROXY;

	/**
	 * Gets the connection proxy used in requests created
	 * by {@link #createHttpRequest()} by default. This implementation returns
	 * the value of a local field.
	 *
	 * @see #setProxy(java.net.Proxy)
	 */
	protected java.net.Proxy getProxy() {
		return this.proxy;
	}

	/**
	 * Sets the value of the proxy normally returned by
	 * {@link #getProxy()}.
	 *
	 * @param proxy A <code>java.net.Proxy</code> instance.
	 */
	public void setProxy(java.net.Proxy proxy) {
		this.proxy = proxy;
	}

	/**
	 * Returns the value of Java property <code>os.name</code>.
	 * It may be overridden to provide a different value.
	 */
	public String getPlatform() {
		return System.getProperty("os.name");
	}

	/**
	 * Gets the User-Agent string. This implementation returns
	 * the value of a local field.
	 *
	 */
	public String getUserAgent() {
		return "Mozilla/4.0 (compatible; MSIE 6.0;) Cobra/Simple";
	}

	/**
	 * This implementation uses the default <code>java.net.CookieHandler</code>,
	 * if any, to get cookie information for the given URL. If no cookie handler
	 * is available, this method returns the empty string.
	 */
	public String getCookie(java.net.URL url) {
		java.net.CookieHandler handler = java.net.CookieHandler.getDefault();
		if (handler == null) {
			return "";
		}
		Map results;
		try {
			results = handler.get(url.toURI(), new HashMap<>());
		} catch (Exception err) {
			return "";
		}
		if (results == null) {
			return "";
		}
		StringBuilder buffer = new StringBuilder();
		Iterator i = results.entrySet().iterator();
		boolean firstTime = true;
		while (i.hasNext()) {
			Map.Entry entry = (Map.Entry) i.next();
			String key = (String) entry.getKey();
			if ("Cookie".equalsIgnoreCase(key) || "Cookie2".equalsIgnoreCase(key)) {
				List list = (List) entry.getValue();
				for (Object aList : list) {
					String value = (String) aList;
					if (firstTime) {
						firstTime = false;
					} else {
						buffer.append("; ");
					}
					buffer.append(value);
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * This method uses the default CookieHandler, if one is available,
	 * to set a cookie value.
	 */
	public void setCookie(java.net.URL url, String cookieSpec) {
		java.net.CookieHandler handler = java.net.CookieHandler.getDefault();
		if (handler == null) {
			return;
		}
		Map<String, List<String>> headers = new HashMap<>(2);
		headers.put("Set-Cookie", Collections.singletonList(cookieSpec));
		try {
			handler.put(url.toURI(), headers);
		} catch (Exception ignored) {
		}
	}

	/**
	 * Determines whether external CSS loading should be enabled.
	 * This implementation returns the value of a local field
	 * defaulting to <code>true</code>.
	 *
	 */
	public boolean isExternalCSSEnabled() {
		return true;
	}

}
