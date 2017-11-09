package application.utility.net;

import application.utility.net.Exceptions.IllegalHeaderDataException;

import java.io.IOException;
import java.net.URL;

public class Form {

	private EditableContext context;
	private HTTPURLConnection connection;

	public Form() {
		context = new ContextImpl();
		connection = new HTTPURLConnection(context);
	}

	public Form(URL hostURL) {
		this();
		setHostURL(hostURL);
	}

	public void setHostURL(URL hostURL) {
		context.setHostURL(hostURL);
	}

	public Context getContext() {
		return context;
	}

	public void setRequestForm(String requestStr) throws IllegalHeaderDataException {
		if (requestStr == null || requestStr.equals("")) return;
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