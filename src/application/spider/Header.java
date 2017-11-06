package application.spider;

import java.net.URL;
import java.util.ArrayList;

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
}
