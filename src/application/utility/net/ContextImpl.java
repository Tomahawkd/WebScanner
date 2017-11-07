package application.utility.net;

import application.utility.net.Exceptions.IllegalHeaderDataException;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import static application.utility.net.Header.CRLF;

class ContextImpl implements EditableContext {

	private URL url;
	private HeaderMapImpl requestHeader;
	private HeaderMapImpl responseHeader;
	private String requestData;
	private String responseData;

	ContextImpl() {
		this.requestHeader = new HeaderMapImpl(HeaderMap.REQUEST);
		this.responseHeader = new HeaderMapImpl(HeaderMap.RESPONSE);
		this.requestData = "";
		this.responseData = "";
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	@Override
	public URL getURL() {
		String path = ((RequestHeaderInfo) requestHeader.get("Header Information")).getUrlPath();
		if (path == null) return url;
		try {
			return new URL(getHost() + path);
		} catch (MalformedURLException e) {
			return url;
		}
	}

	@Override
	public String getHost() {
		return url.getProtocol() + "://" + url.getHost() +
				(url.getPort() == -1 ? ":80" : ":" + url.getPort());
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
		Header header = requestHeader.get("Header Information");
		if (header != null)
			return ((ResponseHeaderInfo) header).getStatusCode();
		else return "";
	}

	@Override
	public String getStatusMessage() {
		Header header = requestHeader.get("Header Information");
		if (header != null)
			return ((ResponseHeaderInfo) header).getStatusMessage();
		else return "";
	}

	public HeaderMap getRequestHeader() {
		return requestHeader;
	}

	public HeaderMap getResponseHeader() {
		return responseHeader;
	}

	@Override
	public String getRequestData() {
		return requestData;
	}

	@Override
	public String getResponseData() {
		return responseData;
	}

	@Override
	public String getMINEType() {
		Header header = requestHeader.get("Content-Type");
		if (header != null)
			return header.toString();
		else return "";
	}

	public String getCookie() {
		return Session.getInstance().getWithURL(url.getHost());
	}

	public void setRequestForm(String form) throws IllegalHeaderDataException {
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

	public void addResponse(String line) throws IllegalHeaderDataException {
		line = line.trim();
		this.responseHeader.add(line);
	}

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

	public void clearRequest() {
		requestHeader.clear();
		requestData = "";
	}

	public void clearResponse() {
		responseHeader.clear();
		responseData = "";
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
					Session.getInstance().updateCookie(url.getHost(), infoSet[1]);
					put(infoSet[0], Session.getInstance().getCookies(url.getHost()));
				} else {
					put(infoSet[0], new HeaderData(infoSet[1]));
				}
				return;
			}

			switch (type) {
				case HeaderMap.REQUEST:
					put("Header Information", new RequestHeaderInfo(line));
				case HeaderMap.RESPONSE:
					put("Header Information", new ResponseHeaderInfo(line));
				default:
					throw new IllegalArgumentException("Unsupported type value: " + type);
			}
		}
	}
}
