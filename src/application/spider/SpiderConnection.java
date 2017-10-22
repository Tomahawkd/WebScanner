package application.spider;

import application.utility.net.Connection;
import application.utility.net.Context;
import application.view.frame.spider.SpiderPanelController;

import java.io.IOException;
import java.net.URL;

public class SpiderConnection {

	private Connection connection;
	private Header header;
	private static int requestCount = 0;

	SpiderConnection() {
		connection = new Connection();
		header = new Header();
	}

	SpiderConnection(URL url) {
		connection = new Connection(url);
		header = new Header(url);
	}

	void setURL(URL url) {
		connection.setURL(url);
		header.setHeader(url);
	}

	void connectWithCookie(Cookie cookie) throws IOException {
		header.setCookie(cookie.getCookie());
		connection.getDataHandler().setRequest(header.getHeader());
		connection.connect();
		requestCount++;
		SpiderPanelController.getInstance().updateRequestCounter(requestCount);
		cookie.addCookie(connection.getDataHandler().getResponseContext().getCookie());
	}

	public Context getContext() {
		return connection.getDataHandler().getResponseContext();
	}
}
