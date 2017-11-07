package application.utility.net;

import application.utility.net.Exceptions.IllegalHeaderDataException;

import java.io.IOException;
import java.net.URL;

public class CoreData {

	private EditableContext context;
	private HTTPURLConnection connection;

	public CoreData() {
		context = new ContextImpl();
		connection = new HTTPURLConnection(context);
	}

	public CoreData(URL url) {
		this();
		setURL(url);
	}

	public void setURL(URL url) {
		context.setUrl(url);
	}

	public Context getContext() {
		return context;
	}

	public void setRequest(String requestStr) throws IllegalHeaderDataException {
		context.clearRequest();
		context.setRequestForm(requestStr);
	}

	public void connect() throws IndexOutOfBoundsException, IOException {
		connection.openConnection();
	}

	public void setConnectionTimeout(int timeout) {
		connection.setTimeout(timeout);
	}

	public int getConnectionTimeout() {
		return connection.getTimeout();
	}
}