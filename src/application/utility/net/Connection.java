package application.utility.net;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Connection {

	private DataHandler dataHandler;
	private HTTPURLConnection httpurlConnection;

	public Connection() {
		dataHandler = new DataHandler();
		httpurlConnection = new HTTPURLConnection(dataHandler.getRequestContext(),
				dataHandler.getResponseContext());
	}

	public Connection(URL url) {
		dataHandler = new DataHandler();
		httpurlConnection = new HTTPURLConnection(dataHandler.getRequestContext(),
				dataHandler.getResponseContext(), url);
	}

	@Deprecated
	public Connection(String urlStr) throws MalformedURLException {
		dataHandler = new DataHandler();
		httpurlConnection = new HTTPURLConnection(dataHandler.getRequestContext(),
				dataHandler.getResponseContext(), urlStr);
	}

	public void connect() throws IndexOutOfBoundsException, IOException {
		httpurlConnection.openConnection();
	}

	public DataHandler getDataHandler() {
		return dataHandler;
	}

	public void setURL(URL url) {
		httpurlConnection.setURL(url);
	}

	@Deprecated
	public void setURL(String urlStr) throws MalformedURLException {
		httpurlConnection.setURL(urlStr);
	}

	public URL getURL() {
		return httpurlConnection.getURL();
	}

	public void setConnectionTimeout(int timeout) {
		httpurlConnection.setTimeout(timeout);
	}

	public int getConnectionTimeout() {
		return httpurlConnection.getTimeout();
	}
}
