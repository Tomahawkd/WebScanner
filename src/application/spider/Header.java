package application.spider;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class Header {

	private ArrayList<String> headers;

	Header() {
		headers = new ArrayList<>();
	}

	Header(URL url) {
		headers = new ArrayList<>();
		setHeader(url);
	}

	void setHeader(URL url) {
		this.headers.add("GET " +
				url.toExternalForm()
						.substring((url.getProtocol() + "://" + url.getHost() + ":" + url.getPort()).length()) +
				" HTTP/1.1");
		this.headers.add("Host: " + url.getHost());
		this.headers.add("Connection: close");
	}

	public String getHeader() {
		StringBuilder headerBuilder = new StringBuilder();
		for (String header : headers) {
			headerBuilder.append(header).append("\r\n");
		}
		return headerBuilder.toString();
	}

	public void setCookie(Map<String, String> cookies) {
		StringBuilder cookieBuilder = new StringBuilder();
		cookieBuilder.append("Cookie: ");
		for (Map.Entry<String, String> entry : cookies.entrySet()) {
			cookieBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("; ");
		}
		String cookie = cookieBuilder.substring(0, cookieBuilder.length() - 2);
		if (!cookie.equals("Cookie")) {
			this.headers.add(cookie);
		}
	}
}
