package application.utility.net.data;

import application.utility.net.Exceptions.IllegalHeaderDataException;

import java.io.Serializable;
import java.net.URL;

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
	HeaderMap getHeader();

	String getCookie();

	String getMINEType();

	String getData();

	String toFormString() throws IllegalHeaderDataException;
}
