package application.utility.net;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class Cookies implements EditableHeader {

	private Map<String, String> cookieMap;

	Cookies(String cookiesString) {
		cookieMap = new ConcurrentHashMap<>();
		addCookies(cookiesString);
	}

	private Cookies(Map<String, String> cookieMap) {
		this.cookieMap = new ConcurrentHashMap<>();
		this.cookieMap.putAll(cookieMap);
	}

	void addCookies(String cookiesString) {
		if (cookiesString == null || cookiesString.equals("")) return;
		String[] cookieSet = cookiesString.split(";");
		for (String cookie : cookieSet) {
			cookie = cookie.trim();
			String cookieName = cookie.substring(0, cookie.indexOf("="));
			String cookieValue = cookie.substring(cookie.indexOf("=") + 1);
			cookieMap.put(cookieName, cookieValue);
		}
	}

	@Override
	public void setValue(String value) {
		addCookies(value);
	}

	@Override
	public String toString() {
		if (this.cookieMap.size() == 0) return "";

		StringBuilder cookieBuilder = new StringBuilder();
		for(Map.Entry<String, String> cookie : this.cookieMap.entrySet()) {
			cookieBuilder.append(cookie.getKey()).append("=").append(cookie.getValue()).append("; ");
		}

		return cookieBuilder.substring(0, cookieBuilder.length() - 2);
	}

	@Override
	public Cookies copy() {
		return new Cookies(this.cookieMap);
	}
}