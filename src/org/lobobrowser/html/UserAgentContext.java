package org.lobobrowser.html;

/**
 * Provides information about the user agent (browser) driving
 * the parser and/or renderer.
 * <p>
 * A simple implementation of this interface is provided in
 * {@link org.lobobrowser.html.test.SimpleUserAgentContext}.
 *
 * @see HtmlRendererContext#getUserAgentContext()
 * @see org.lobobrowser.html.parser.DocumentBuilderImpl#DocumentBuilderImpl(UserAgentContext)
 */
public interface UserAgentContext {
	/**
	 * Creates an instance of {@link org.lobobrowser.html.HttpRequest} which
	 * can be used by the renderer to load images, scripts, external style sheets,
	 * and implement the Javascript XMLHttpRequest class (AJAX).
	 */
	HttpRequest createHttpRequest();

	/**
	 * Returns a boolean value indicating whether
	 * remote (non-inline) CSS documents should be loaded.
	 */
	boolean isExternalCSSEnabled();

	/**
	 * Gets the name of the user's operating system.
	 */
	String getPlatform();

	/**
	 * Should return the string used in
	 * the User-Agent header.
	 */
	String getUserAgent();

	/**
	 * Method used to implement Javascript <code>document.cookie</code> property.
	 */
	String getCookie(java.net.URL url);

	/**
	 * Method used to implement <code>document.cookie</code> property.
	 *
	 * @param cookieSpec Specification of cookies, as they
	 *                   would appear in the Set-Cookie
	 *                   header value of HTTP.
	 */
	void setCookie(java.net.URL url, String cookieSpec);

	/**
	 * Returns true if the current media matches the name provided.
	 *
	 * @param mediaName Media name, which
	 *                  may be <code>screen</code>, <code>tty</code>, etc. (See <a href="http://www.w3.org/TR/REC-html40/types.html#type-media-descriptors">HTML Specification</a>).
	 */
	boolean isMedia(String mediaName);

}
