package application.spider;

import application.utility.net.Context;
import application.utility.net.CoreData;
import application.view.frame.spider.SpiderPanelController;

import java.io.IOException;
import java.net.URL;

public class SpiderConnection {

	private CoreData data;
	private Header header;
	private static int requestCount = 0;

	SpiderConnection() {
		data = new CoreData();
		header = new Header();
	}

	SpiderConnection(URL url) {
		data = new CoreData(url);
		header = new Header(url);
	}

	void setURL(URL url) {
		data.setURL(url);
		header.setHeader(url);
	}

	void connect() throws IOException {
		data.setRequest(header.getHeader());
		data.connect();
		requestCount++;
		SpiderPanelController.getInstance().updateRequestCounter(requestCount);
	}

	public Context getContext() {
		return data.getContext();
	}
}
