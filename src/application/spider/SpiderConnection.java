package application.spider;

import application.utility.net.Connection;
import application.utility.net.data.EditableContext;
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

	void connectWithCookie() throws IOException {
		connection.getCoreData().setRequest(header.getHeader());
		connection.connect();
		requestCount++;
		SpiderPanelController.getInstance().updateRequestCounter(requestCount);
	}

	public EditableContext getContext() {
		return connection.getCoreData().getResponseContext();
	}
}
