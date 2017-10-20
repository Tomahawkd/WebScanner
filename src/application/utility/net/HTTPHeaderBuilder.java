package application.utility.net;

import application.utility.net.Exceptions.IllegalHeaderDataException;

import java.util.Map;

class HTTPHeaderBuilder {

	private static final String CRLF = "\r\n";

	static String buildHeader(HTTPHeaderMap container)
			throws IllegalHeaderDataException {

		switch (container.getType()) {
			case HTTPHeaderMap.REQUEST:
				return buildRequestHeader(container);
			case HTTPHeaderMap.RESPONSE:
				return buildResponseHeader(container);
			default:
				throw new IllegalArgumentException("Unsupported type");
		}
	}

	private static String buildRequestHeader(HTTPHeaderMap container)
			throws IllegalHeaderDataException {
		StringBuilder builder = new StringBuilder();

		builder.append(container.get("Method", "MethodType")).append(" ");
		builder.append(container.get("URL", "Path")).append(" ");
		builder.append(container.get("Version", "VersionInfo")).append(CRLF);

		StringBuilder cookieBuilder = new StringBuilder();
		cookieBuilder.append("Cookie: ");

		for (Map.Entry<HTTPHeader, String> mapping : container.entrySet()) {
			if (!mapping.getKey().equals("Method", "MethodType") &&
					!mapping.getKey().equals("URL", "Path") &&
					!mapping.getKey().equals("Version", "VersionInfo")) {

				if (mapping.getKey().equals("Header", "Content-Length")) continue;

				if (mapping.getKey().getType().equals("Cookie")) {
					cookieBuilder.append(mapping.getKey().getName()).append("=")
							.append(mapping.getValue()).append("; ");

				} else if (mapping.getKey().getName().equals("Connection")) {
					builder.append(mapping.getKey().getName()).append(": ")
							.append("close").append(CRLF);

				} else {
					builder.append(mapping.getKey().getName()).append(": ")
							.append(mapping.getValue()).append(CRLF);
				}
			}
		}

		if (!builder.toString().contains("Connection: ")) {
			builder.append("Connection: close").append(CRLF);
		}

		String cookie = cookieBuilder.substring(0, cookieBuilder.length() - 2);
		if (!cookie.equals("Cookie")) {
			builder.append(cookie).append(CRLF);
		}

		if (container.getMethod().equals("POST")) {
			builder.append("Content-Length: ")
					.append(container.get("Header", "Content-Length")).append(CRLF);
		}

		builder.append(CRLF);

		return builder.toString();
	}

	private static String buildResponseHeader(HTTPHeaderMap container)
			throws IllegalHeaderDataException {
		StringBuilder builder = new StringBuilder();

		builder.append(container.get("Version", "VersionInfo")).append(" ");
		builder.append(container.get("Status", "Code")).append(" ");
		builder.append(container.get("Status", "Message")).append(CRLF);

		StringBuilder cookieBuilder = new StringBuilder();
		cookieBuilder.append("Set-Cookie: ");

		for (Map.Entry<HTTPHeader, String> mapping : container.entrySet()) {
			if (!mapping.getKey().equals("Version", "VersionInfo") &&
					!mapping.getKey().equals("Status", "Code") &&
					!mapping.getKey().equals("Status", "Message")) {

				if (mapping.getKey().getType().equals("Set-Cookie")) {
					cookieBuilder.append(mapping.getKey().getName()).append("=")
							.append(mapping.getValue()).append("; ");
				} else {
					builder.append(mapping.getKey().getName()).append(": ")
							.append(mapping.getValue()).append(CRLF);
				}
			}
		}

		String cookie = cookieBuilder.substring(0, cookieBuilder.length() - 2);
		if (!cookie.equals("Set-Cookie")) {
			builder.append(cookie).append(CRLF);
		}

		builder.append(CRLF);

		return builder.toString();
	}
}
