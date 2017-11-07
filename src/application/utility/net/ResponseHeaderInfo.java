package application.utility.net;

import application.utility.net.Exceptions.IllegalHeaderDataException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResponseHeaderInfo extends HeaderInfo implements Header {

	private String version;
	private String statusCode;
	private String statusMessage;

	ResponseHeaderInfo(String firstResponseHeader)
			throws IndexOutOfBoundsException, IllegalHeaderDataException {
		checkHeader(firstResponseHeader);
		this.version = firstResponseHeader.substring(0, firstResponseHeader.indexOf(" "));
		this.statusCode = firstResponseHeader
				.substring(firstResponseHeader.indexOf(" ") + 1, firstResponseHeader.lastIndexOf(" "));
		this.statusMessage = firstResponseHeader
				.substring(firstResponseHeader.lastIndexOf(" ") + 1, firstResponseHeader.length());
	}

	private void checkHeader(String firstHeader) throws IllegalHeaderDataException {

		Matcher m = Pattern.compile("^HTTP/[0-9].[0-9][\\s]([0-9][0-9][0-9])[\\s].*$")
				.matcher(firstHeader);
		if(!m.find()) {
			throw new IllegalHeaderDataException("Invalid HTTP response header");
		}
	}

	String getStatusCode() {
		return statusCode;
	}

	String getStatusMessage() {
		return statusMessage;
	}

	String getVersion() {
		return version;
	}

	@Override
	public String toFormalHeader() {
		return version + " " + statusCode + " " + statusMessage + CRLF;
	}
}