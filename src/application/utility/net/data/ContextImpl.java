package application.utility.net.data;

import application.utility.net.Exceptions.IllegalHeaderDataException;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import static application.utility.net.data.Header.CRLF;

class ContextImpl implements EditableContext {

	private URL url;
	private HeaderMapImpl header;
	private String data;

	ContextImpl(int type) {
		this.header = new HeaderMapImpl(type);
		this.data = "";
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	@Override
	public URL getURL() {
		if (header.getType() != HeaderMap.REQUEST || url == null) {
			return url;
		} else {
			String path = ((RequestHeaderInfo) header.get("Header Information")).getUrlPath();
			if (path == null) return url;
			String host = url.getProtocol() + "://" + url.getHost();
			String port = url.getPort() == -1 ? ":80" : ":" +url.getPort();
			try {
				return new URL(host + port + path);
			} catch (MalformedURLException e) {
				return url;
			}
		}
	}

	@Override
	public String getVersion() {
		return ((HeaderInfo) header.get("Header Information")).getVersion();
	}

	@Override
	public String getMethod() {
		if (header.getType() == HeaderMap.REQUEST)
			return ((RequestHeaderInfo) header.get("Header Information")).getMethod();
		else return null;
	}

	@Override
	public String getStatusCode() {
		if (header.getType() == HeaderMap.RESPONSE)
			return ((ResponseHeaderInfo) header.get("Header Information")).getStatusCode();
		else return null;
	}

	@Override
	public String getStatusMessage() {
		if (header.getType() == HeaderMap.RESPONSE)
			return ((ResponseHeaderInfo) header.get("Header Information")).getStatusMessage();
		else return null;
	}

	public HeaderMapImpl getHeader() {
		return header;
	}

	@Override
	public String getMINEType() {
		if (header.getType() == HeaderMap.RESPONSE)
			return header.get("Content-Type").toString();
		else return null;
	}

	public String getCookie() {
		return Session.getInstance().getWithURL(url.getHost());
	}

	public String getData() {
		return data;
	}

	public void setForm(String form) throws IllegalHeaderDataException {
		String[] formSet = form.split("\n\n");
		setHeader(formSet[0]);
		if (header.getType() == HeaderMap.REQUEST &&
				((RequestHeaderInfo) header.get("Header Information")).getMethod().equals("POST")) {
			try {
				String data = formSet[1];
				if (!data.equals("")) {
					int contentLength = data.getBytes().length;
					String line = "Content-Length: " + contentLength;
					addHeaderLine(line);
				}
				setData(data.getBytes());
			} catch (IndexOutOfBoundsException ignored) {
			}
		}
	}

	public void setHeader(String header) throws IllegalHeaderDataException {
		String[] requestHeader = header.split("\n");
		for (String headerLine : requestHeader) {
			addHeaderLine(headerLine);
		}
	}

	public void addHeaderLine(String line) throws IllegalHeaderDataException {
		line = line.trim();
		this.header.add(line);
	}

	public void setData(byte[] data) {
		try {
			Header contentType = header.get("Content-Type");
			if (contentType != null) {
				String[] typeInfo = contentType.toFormalHeader().split(";");
				String charset = typeInfo[1].split("charset=")[1].trim();
				this.data = new String(data, charset);
			} else {
				throw new IndexOutOfBoundsException();
			}
		} catch (IndexOutOfBoundsException | UnsupportedEncodingException e) {
			this.data = new String(data);
		}
	}

	public String toFormString() throws IllegalHeaderDataException {
		if (header.isEmpty()) return "";
		return header.toFormalHeader() + data;
	}

	public void clear() {
		header.clear();
		data = "";
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
				case HeaderMap.REQUEST: put("Header Information", new RequestHeaderInfo(line));
				case HeaderMap.RESPONSE: put("Header Information", new ResponseHeaderInfo(line));
				default: throw new IllegalArgumentException("Unsupported type value: " + type);
			}
		}
	}
}
