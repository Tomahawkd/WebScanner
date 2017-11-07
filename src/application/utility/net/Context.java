package application.utility.net;

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
	String getHost();

	//Response Header
	String getStatusCode();
	String getStatusMessage();

	/*
	 * Data
	 */
	HeaderMap getRequestHeader();
	HeaderMap getResponseHeader();

	String getRequestData();
	String getResponseData();

	String getRequestForm() throws IllegalHeaderDataException;
	String getResponseForm() throws IllegalHeaderDataException;

	String getCookie();

	String getMINEType();

	String getParams();
}
