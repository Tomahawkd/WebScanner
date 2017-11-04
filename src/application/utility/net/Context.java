package application.utility.net;

import application.utility.net.Exceptions.IllegalHeaderDataException;

import java.io.Serializable;
import java.net.URL;
import java.util.Map;

public interface Context extends Serializable {

	URL getURL();

	/*
	 * Status
	 */
	String getVersion();

	// Request Header
	String getMethod();

	//Response Header
	String getStatusCode();
	String getStatusMessage();

	/*
	 * Data
	 */
	HTTPHeaderMap getHeader();

	Map<String, String> getCookie();

	String getData();

	String toContextString() throws IllegalHeaderDataException;
}
