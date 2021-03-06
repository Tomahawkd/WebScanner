package application.spider;

import application.utility.net.Context;
import application.utility.net.Form;
import application.view.frame.spider.SpiderPanelController;

import java.io.IOException;
import java.net.URL;

public class SpiderConnection {

	private Form data;
	private SpiderHeader header;
	private static int requestCount = 0;

	SpiderConnection() {
		data = new Form();
		header = new SpiderHeader();
	}

	SpiderConnection(URL url) {
		data = new Form(url);
		header = new SpiderHeader(url);
	}

	void setURL(URL url) {
		data.setHostURL(url);
		header.setHeader(url);
	}

	void connect() throws IOException {
		data.setRequestForm(header.getHeader());
		data.connect();
		requestCount++;
		SpiderPanelController.getInstance().updateRequestCounter(requestCount);
	}

	public Context getContext() {
		return data.getContext();
	}
}
