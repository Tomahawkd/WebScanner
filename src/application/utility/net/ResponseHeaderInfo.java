package application.utility.net;

import application.utility.net.Exceptions.IllegalHeaderDataException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ResponseHeaderInfo extends HeaderInfo {

	private String version;
	private String statusCode;
	private String statusMessage;

	ResponseHeaderInfo(String firstResponseHeader) throws IllegalHeaderDataException {
		setValue(firstResponseHeader);
	}

	private ResponseHeaderInfo(String version, String statusCode, String statusMessage) {
		this.version = version;
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
	}

	private void checkHeader(String firstHeader) throws IllegalHeaderDataException {

		Matcher m = Pattern
				.compile("^HTTP/[0-9].[0-9][\\s]([0-9][0-9][0-9])[\\s].*$")
				.matcher(firstHeader);
		if (!m.matches()) {
			throw new IllegalHeaderDataException("Invalid HTTP response header: " + firstHeader);
		}
	}

	@Override
	public void setValue(String value) throws IllegalHeaderDataException {
		checkHeader(value);
		String[] valueSet = value.split(" ", 3);
		if (valueSet.length != 3) throw new IllegalHeaderDataException("Invalid HTTP header");
		this.version = valueSet[0];
		this.statusCode = valueSet[1];
		this.statusMessage = valueSet[2];
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
	public String toString() {
		return version + " " + statusCode + " " + statusMessage;
	}

	@Override
	public ResponseHeaderInfo copy() {
		return new ResponseHeaderInfo(version, statusCode, statusMessage);
	}
}