package application.utility.net.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Session {

	private static Session session;
	private Map<String, String> cookies;

	public static Session getInstance() {
		if (session == null) session = new Session();
		return session;
	}

	private Session() {
		cookies = new ConcurrentHashMap<>();
	}

	void updateCookie(String host, String cookies) {
		String cookieStr = this.cookies.get(host);
		Cookies cookieSet = getCookies(host);
		cookieSet.addCookies(cookies);
		if (cookieStr != null) {
			this.cookies.put(cookieStr, cookieSet.toString());
		} else {
			this.cookies.put(host, cookieSet.toString());
		}
	}

	Cookies getCookies(String host) {
		return new Cookies(this.cookies.get(host));
	}

	String getWithURL(String host) {
		String cookie = cookies.get(host);
		return cookie == null ? "" : "Cookie: " + cookie;
	}
}