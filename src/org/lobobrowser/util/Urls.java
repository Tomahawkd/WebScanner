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
 * Created on Jun 12, 2005
 */
package org.lobobrowser.util;

import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

public class Urls {
	private static final DateFormat PATTERN_RFC1123 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);

	static {
		PATTERN_RFC1123.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	private Urls() {
		super();
	}

	/**
	 * Whether the URL is a file in the local file system.
	 */
	private static boolean isLocalFile(java.net.URL url) {
		String scheme = url.getProtocol();
		return "file".equalsIgnoreCase(scheme) && !hasHost(url);
	}

	private static boolean hasHost(java.net.URL url) {
		String host = url.getHost();
		return host != null && !"".equals(host);
	}

	/**
	 * Creates an absolute URL in a manner equivalent to
	 * major browsers.
	 */
	public static URL createURL(URL baseUrl, String relativeUrl) throws java.net.MalformedURLException {
		return new URL(baseUrl, relativeUrl);
	}

	public static String getCharset(URLConnection connection) {
		String contentType = connection.getContentType();
		if (contentType == null) {
			return getDefaultCharset(connection);
		}
		StringTokenizer tok = new StringTokenizer(contentType, ";");
		if (tok.hasMoreTokens()) {
			tok.nextToken();
			while (tok.hasMoreTokens()) {
				String assignment = tok.nextToken().trim();
				int eqIdx = assignment.indexOf('=');
				if (eqIdx != -1) {
					String varName = assignment.substring(0, eqIdx).trim();
					if ("charset".equalsIgnoreCase(varName)) {
						String varValue = assignment.substring(eqIdx + 1);
						return Strings.unquote(varValue.trim());
					}
				}
			}
		}
		return getDefaultCharset(connection);
	}

	private static String getDefaultCharset(URLConnection connection) {
		URL url = connection.getURL();
		if (Urls.isLocalFile(url)) {
			String charset = System.getProperty("file.encoding");
			return charset == null ? "ISO-8859-1" : charset;
		} else {
			return "ISO-8859-1";
		}
	}

}
