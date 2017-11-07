package application.utility.net;

import application.utility.net.Exceptions.IllegalHeaderDataException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RequestHeaderInfo extends HeaderInfo {

	private static final String[] methods = {
		"GET", "POST", "HEAD", "OPTIONS", "PUT", "DELETE", "TRACE"
	};

	private String method;
	private String urlPath;
	private String version;

	RequestHeaderInfo(String firstRequestHeader) throws IllegalHeaderDataException {

		checkHeader(firstRequestHeader);

		String[] infoSet = firstRequestHeader.split(" ");
		String requestMethod = infoSet[0];
		for (String method : methods) {
			if (firstRequestHeader.contains(method)) {
				this.method = requestMethod;
				this.urlPath = infoSet[1];
				this.version = infoSet[2];
				return;
			}
		}
		throw new IllegalHeaderDataException("Method " + requestMethod + " unsupported");
	}

	private void checkHeader(String firstHeader) throws IllegalHeaderDataException {

		Matcher m = Pattern.compile("^[A-Z]*[\\s]/.*[\\s]HTTP/[0-9].[0-9]$")
				.matcher(firstHeader);
		if(!m.find()) {
			throw new IllegalHeaderDataException("Invalid HTTP request header");
		}
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
	public String toFormalHeader() {
		return method + " " + urlPath + " " + version + CRLF;
	}
}