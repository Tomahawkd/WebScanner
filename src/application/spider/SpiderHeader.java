package application.spider;

import java.net.URL;
import java.util.ArrayList;

public class SpiderHeader {

	private ArrayList<String> headers;

	SpiderHeader() {
		headers = new ArrayList<>();
	}

	SpiderHeader(URL url) {
		headers = new ArrayList<>();
		setHeader(url);
	}

	void setHeader(URL url) {
		this.headers.add("GET " + url.getFile() + " HTTP/1.1");
		this.headers.add("Host: " + url.getHost());
		this.headers.add("Connection: close");
	}

	public void addHeader(String header) {
		this.headers.add(header);
	}

	public String getHeader() {
		StringBuilder headerBuilder = new StringBuilder();
		for (String header : headers) {
			headerBuilder.append(header).append("\r\n");
		}
		return headerBuilder.toString();
	}
}