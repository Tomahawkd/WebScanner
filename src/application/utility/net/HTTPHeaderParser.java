package application.utility.net;

import application.utility.net.Exceptions.IllegalHeaderDataException;

class HTTPHeaderParser {

	private static final String[] methods = {
			"GET", "POST", "HEAD", "OPTIONS", "PUT", "DELETE", "TRACE"
	};


	static void parseHeader(HTTPHeaderMapImpl container, String line)
			throws IndexOutOfBoundsException, IllegalHeaderDataException, IllegalArgumentException {
		switch (container.getType()) {
			case HTTPHeaderMap.REQUEST:
				parseRequestHeader(container, line);
				break;

			case HTTPHeaderMap.RESPONSE:
				parseResponseHeader(container, line);
				break;

			default:
				throw new IllegalArgumentException("Type not exist");
		}
	}

	private static void parseRequestHeader(HTTPHeaderMapImpl container, String line)
			throws IndexOutOfBoundsException, IllegalHeaderDataException {

		if (line.contains(": ")) {
			String[] infoSet = line.split(": ");
			if ("Cookie".equals(infoSet[0])) {
				String[] cookieSet = infoSet[1].split(";");
				for (String cookie : cookieSet) {
					cookie = cookie.trim();
					String cookieName = cookie.substring(0, cookie.indexOf("="));
					String cookieValue = cookie.substring(cookie.indexOf("=") + 1);
					container.put(new HTTPHeader("Cookie", cookieName), cookieValue);
				}
			} else if ("Connection".equals(infoSet[0])) {
				container.put(new HTTPHeader("Header", "Connection"), "close");
			} else if ("Content-Type".equals(infoSet[0])) {
				container.put(new HTTPHeader("Content", "Content-Type"), infoSet[1]);
			} else {
				container.put(new HTTPHeader("Header", infoSet[0]), infoSet[1]);
			}
			return;
		}

		if (line.split(" ").length == 3) {
			String[] infoSet = line.split(" ");
			String requestMethod = infoSet[0];
			for (String method : methods) {
				if (line.contains(method)) {
					container.put(new HTTPHeader("Method", "MethodType"), requestMethod);
					container.put(new HTTPHeader("URL", "Path"), infoSet[1]);
					container.put(new HTTPHeader("Version", "VersionInfo"), infoSet[2]);
					return;
				}
			}
			throw new IllegalHeaderDataException("Method " + requestMethod + " unsupported");
		}
		throw new IllegalHeaderDataException("The request data " + line + " is invalid");
	}

	private static void parseResponseHeader(HTTPHeaderMapImpl container, String line)
			throws IndexOutOfBoundsException, IllegalHeaderDataException {

		if (line.contains(": ")) {
			String[] infoSet = line.split(": ");
			if ("Set-Cookie".equals(infoSet[0])) {
				String[] cookieSet = infoSet[1].split(";");
				for (String cookie : cookieSet) {
					cookie = cookie.trim();
					String cookieName = cookie.substring(0, cookie.indexOf("="));
					String cookieValue = cookie.substring(cookie.indexOf("=") + 1);
					container.put(new HTTPHeader("Set-Cookie", cookieName), cookieValue);
				}
			} else if ("Content-Type".equals(infoSet[0])) {
				container.put(new HTTPHeader("Content", "Content-Type"), infoSet[1]);
			} else {
				container.put(new HTTPHeader("Header", infoSet[0]), infoSet[1]);
			}
			return;
		} else {
			String[] infoSet = line.split(" ", 3);
			if (line.startsWith("HTTP") && !infoSet[0].equals("") &&
					!infoSet[1].equals("") && !infoSet[2].equals("")) {
				container.put(new HTTPHeader("Version", "VersionInfo"), infoSet[0]);
				container.put(new HTTPHeader("Status", "Code"), infoSet[1]);
				container.put(new HTTPHeader("Status", "Message"), infoSet[2]);
				return;
			}
		}
		throw new IllegalHeaderDataException("The response data " + line + " is invalid");
	}
}
