package application.utility.net;

import application.utility.net.Exceptions.IllegalHeaderDataException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RequestHeaderInfo extends HeaderInfo {

	private String method;
	private String urlPath;
	private String version;

	RequestHeaderInfo(String firstRequestHeader) throws IllegalHeaderDataException {
		setValue(firstRequestHeader);
	}

	private void checkHeader(String firstHeader) throws IllegalHeaderDataException {

		Matcher m = Pattern
				.compile("^(GET|POST|HEAD|OPTIONS|PUT|DLELTE|TRACE)*[\\s]/.*[\\s]HTTP/[0-9].[0-9]$")
				.matcher(firstHeader);
		if (!m.matches()) {
			throw new IllegalHeaderDataException("Invalid HTTP request header");
		}
	}

	@Override
	public void setValue(String value) throws IllegalHeaderDataException {
		checkHeader(value);
		String[] valueSet = value.split(" ");
		if (valueSet.length != 3) throw new IllegalHeaderDataException("Invalid HTTP header");
		this.method = valueSet[0];
		this.urlPath = valueSet[1];
		this.version = valueSet[2];
	}

	String getMethod() {
		return method;
	}

	String getUrlPath() {
		return urlPath;
	}

	String getVersion() {
		return version;
	}

	@Override
	public String toString() {
		return method + " " + urlPath + " " + version;
	}
}