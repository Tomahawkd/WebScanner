package application.utility.net;

import application.utility.net.Exceptions.IllegalHeaderDataException;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static application.utility.net.Header.CRLF;

class ContextImpl implements EditableContext {

	private URL host;
	private HeaderMapImpl requestHeader;
	private HeaderMapImpl responseHeader;
	private String requestData;
	private String responseData;
	private String issues;

	ContextImpl() {
		this.requestHeader = new HeaderMapImpl(HeaderMap.REQUEST);
		this.responseHeader = new HeaderMapImpl(HeaderMap.RESPONSE);
		this.requestData = "";
		this.responseData = "";
		this.issues = "";
	}

	@Override
	public void setHostURL(URL host) {
		this.host = host;
	}

	@Override
	public URL getURL() {
		Header info = requestHeader.get("Header Information");
		if (info == null) return null;

		String path = ((RequestHeaderInfo) info).getUrlPath();
		if (path == null) return null;
		try {
			return new URL(getHost() + path);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	@Override
	public URL getHostURL() {
		return host;
	}

	@Override
	public String getHost() {
		return host.getProtocol() + "://" + host.getHost() +
				(host.getPort() == -1 ? ":80" : ":" + host.getPort());
	}

	public String getPath() {
		Header header = requestHeader.get("Header Information");
		if (header != null)
			return ((RequestHeaderInfo) header).getUrlPath();
		else return "";
	}

	@Override
	public String getVersion() {
		Header header = requestHeader.get("Header Information");
		if (header != null)
			return ((RequestHeaderInfo) header).getVersion();
		else return "";
	}

	@Override
	public String getMethod() {
		Header header = requestHeader.get("Header Information");
		if (header != null)
			return ((RequestHeaderInfo) header).getMethod();
		else return "";
	}

	@Override
	public String getStatusCode() {
		Header header = responseHeader.get("Header Information");
		if (header != null)
			return ((ResponseHeaderInfo) header).getStatusCode();
		else return "";
	}

	@Override
	public String getStatusMessage() {
		Header header = responseHeader.get("Header Information");
		if (header != null)
			return ((ResponseHeaderInfo) header).getStatusMessage();
		else return "";
	}

	@Override
	public HeaderMap getRequestHeader() {
		return requestHeader;
	}

	@Override
	public HeaderMap getResponseHeader() {
		return responseHeader;
	}

	@Override
	public String getRequestData() {
		return requestData;
	}

	@Override
	public String getParams() {
		String method = getMethod();
		if (method.equals("")) return "";
		switch (method) {
			case "GET":
				Header info = requestHeader.get("Header Information");
				if (info == null) return "";
				String path = ((RequestHeaderInfo) info).getUrlPath();
				if (path == null) return "";
				else {
					try {
						return path.split("[?]")[1];
					} catch (IndexOutOfBoundsException e) {
						return "";
					}
				}
			case "POST":
				Matcher m = Pattern.compile(".*=.*")
						.matcher(requestData);
				if (m.matches()) return requestData;
				else return "";
			default:
				return "";
		}
	}

	@Override
	public String getResponseData() {
		return responseData;
	}

	@Override
	public String getMINEType() {
		Header header = responseHeader.get("Content-Type");
		if (header != null)
			return header.toString();
		else return "";
	}

	@Override
	public String getCookie() {
		return Session.getInstance().getWithURL(host.getHost());
	}


	@Override
	public String getIssues() {
		return issues;
	}

	@Override
	public void addIssue(String issue) {
		if (issue.contains("&")) return;
		issues = issues.concat("&").concat(issue);
	}

	@Override
	public void setRequestForm(String form) throws IllegalHeaderDataException {
		if (form == null || form.equals("")) return;
		String[] formSet = form.split("\n\n");
		setRequestHeader(formSet[0]);
		if (((RequestHeaderInfo) requestHeader.get("Header Information")).getMethod().equals("POST")) {
			try {
				String data = formSet[1];
				if (!data.equals("")) {
					int contentLength = data.getBytes().length;
					String line = "Content-Length: " + contentLength;
					line = line.trim();
					this.requestHeader.add(line);
				}
				requestData = data;
			} catch (IndexOutOfBoundsException ignored) {
			}
		}
	}

	private void setRequestHeader(String header) throws IllegalHeaderDataException {
		String[] requestHeader = header.split("\n");
		for (String headerLine : requestHeader) {
			headerLine = headerLine.trim();
			this.requestHeader.add(headerLine);
		}
	}

	@Override
	public void setResponseForm(String form) throws IllegalHeaderDataException {
		if (form == null || form.equals("")) return;
		try {
			String[] formSet = form.split("\r\n\r\n");
			String[] headers = formSet[0].split("\r\n");
			for (String header : headers) addResponse(header);
			setResponseData(formSet[1].getBytes());
		} catch (IndexOutOfBoundsException ignored) {
		}

	}

	@Override
	public void addResponse(String line) throws IllegalHeaderDataException {
		line = line.trim();
		this.responseHeader.add(line);
	}

	@Override
	public void setResponseData(byte[] data) {
		try {
			Header contentType = responseHeader.get("Content-Type");
			if (contentType != null) {
				String[] typeInfo = contentType.toFormalHeader().split(";");
				String charset = typeInfo[1].split("charset=")[1].trim();
				this.responseData = new String(data, charset);
			} else {
				throw new IndexOutOfBoundsException();
			}
		} catch (IndexOutOfBoundsException | UnsupportedEncodingException e) {
			this.responseData = new String(data);
		}
	}

	@Override
	public String getRequestForm() throws IllegalHeaderDataException {
		if (requestHeader.isEmpty()) return "";
		return requestHeader.toFormalHeader() + requestData;
	}

	@Override
	public String getResponseForm() throws IllegalHeaderDataException {
		if (responseHeader.isEmpty()) return "";
		return responseHeader.toFormalHeader() + responseData;
	}

	@Override
	public void clearRequest() {
		requestHeader.clear();
		requestData = "";
	}

	@Override
	public void clearResponse() {
		responseHeader.clear();
		responseData = "";
	}

	@Override
	public Context copy() {
		ContextImpl copy = new ContextImpl();
		copy.setForm(this.host, this.requestHeader, this.requestData,
				this.responseHeader, this.responseData);
		return copy;
	}

	private void setForm(URL host, HeaderMapImpl requestHeader, String requestData,
	                     HeaderMapImpl responseHeader, String responseData) {
		clearRequest();
		clearResponse();
		this.host = host;
		this.requestData = requestData;
		this.responseData = responseData;

		//Deep copy
		this.requestHeader.clear();
		for (Map.Entry<String, Header> mapping : requestHeader.entrySet()) {
			this.requestHeader.put(mapping.getKey(), mapping.getValue().copy());
		}
		this.responseHeader.clear();
		for (Map.Entry<String, Header> mapping : responseHeader.entrySet()) {
			this.responseHeader.put(mapping.getKey(), mapping.getValue().copy());
		}
	}

	private class HeaderMapImpl
			extends LinkedHashMap<String, Header> implements EditableHeaderMap {

		private int type;

		public int getType() {
			return type;
		}

		void setType(int type) {
			if (type == REQUEST || type == RESPONSE) this.type = type;
			else throw new IllegalArgumentException("Unsupported type");
		}

		HeaderMapImpl(int type) {
			super();
			setType(type);
		}

		public Header get(String key) {
			return super.get(key);
		}

		public Map.Entry<String, Header> getAtIndex(int index) throws IndexOutOfBoundsException {

			int count = 0;
			for (Map.Entry<String, Header> mapping : this.entrySet()) {
				if (index == count) return mapping;
				count++;
			}
			throw new IndexOutOfBoundsException();
		}

		@Override
		public String toFormalHeader() {
			StringBuilder builder = new StringBuilder();
			builder.append(this.get("Header Information").toFormalHeader());

			for (Map.Entry<String, Header> mapping : this.entrySet()) {
				if (!mapping.getKey().equals("Header Information")) {
					builder.append(mapping.getKey()).append(": ").append(mapping.getValue().toFormalHeader());
				}
			}
			builder.append(CRLF);
			return builder.toString();
		}

		void add(String line) throws IllegalHeaderDataException, IllegalArgumentException {
			if (line.contains(": ")) {
				String[] infoSet = line.split(": ");
				if (infoSet[0].contains("Cookie")) {
					Session.getInstance().updateCookie(host.getHost(), infoSet[1]);
					put(infoSet[0], Session.getInstance().getCookies(host.getHost()));
				} else {
					put(infoSet[0], new HeaderData(infoSet[1]));
				}
				return;
			}

			switch (type) {
				case HeaderMap.REQUEST:
					put("Header Information", new RequestHeaderInfo(line));
					break;
				case HeaderMap.RESPONSE:
					put("Header Information", new ResponseHeaderInfo(line));
					break;
				default:
					throw new IllegalArgumentException("Unsupported type value: " + type);
			}
		}
	}
}
