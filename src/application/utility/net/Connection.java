package application.utility.net;

import application.utility.net.data.CoreData;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Connection {

	private CoreData coreData;
	private HTTPURLConnection httpurlConnection;

	public Connection() {
		coreData = new CoreData();
		httpurlConnection = new HTTPURLConnection(coreData.getRequestContext(),
				coreData.getResponseContext());
	}

	public Connection(URL url) {
		coreData = new CoreData();
		coreData.setURL(url);
		httpurlConnection = new HTTPURLConnection(coreData.getRequestContext(),
				coreData.getResponseContext(), url);
	}

	@Deprecated
	public Connection(String urlStr) throws MalformedURLException {
		coreData = new CoreData();
		httpurlConnection = new HTTPURLConnection(coreData.getRequestContext(),
				coreData.getResponseContext(), urlStr);
	}

	public void connect() throws IndexOutOfBoundsException, IOException {
		httpurlConnection.openConnection();
	}

	public CoreData getCoreData() {
		return coreData;
	}

	public void setURL(URL url) {
		coreData.setURL(url);
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
