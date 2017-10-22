package application.spider;

import java.util.LinkedHashMap;
import java.util.Map;

class Cookie {
	private final Map<String, String> cookie;

	Cookie() {
		cookie = new LinkedHashMap<>();
	}

	Map<String, String> getCookie() {
		return cookie;
	}

	synchronized void addCookie(Map<String, String> cookie) {
		this.cookie.putAll(cookie);
	}
}
