package application.utility.net;

import application.utility.net.Exceptions.IllegalHeaderDataException;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

public class ContextImpl implements Context {

	private HTTPHeaderMapImpl header;
	private String data;

	ContextImpl(int type) {
		this.header = new HTTPHeaderMapImpl(type);
		this.data = "";
	}

	public ContextImpl(HTTPHeaderMapImpl header, byte[] data) {
		this.header = header;
		setData(data);
	}

	@Override
	public URL getURL() {
		return null;
	}

	@Override
	public String getVersion() {
		return null;
	}

	@Override
	public String getMethod() {
		return null;
	}

	@Override
	public String getStatusCode() {
		return null;
	}

	@Override
	public String getStatusMessage() {
		return null;
	}

	public HTTPHeaderMapImpl getHeader() {
		return header;
	}

	void addHeader(String line) throws IndexOutOfBoundsException, IllegalHeaderDataException {
		line = line.trim();
		HTTPHeaderParser.parseHeader(this.header, line);
	}

	public String getData() {
		return data;
	}

	public void setData(byte[] data) {
		try {
			String[] contentType = header.get("Content", "Content-Type").split(";");
			String charset = contentType[1].split("charset=")[1].trim();
			this.data = new String(data, charset);
		} catch (IllegalHeaderDataException e) {
			this.data = "";
		} catch (IndexOutOfBoundsException | UnsupportedEncodingException e) {
			this.data = new String(data);
		}
	}

	public String toContextString() throws IllegalHeaderDataException {
		if (header.isEmpty()) return "";
		return HTTPHeaderBuilder.buildHeader(header) + data;
	}

	public Map<String, String> getCookie() {
		LinkedHashMap<String, String> cookies = new LinkedHashMap<>();
		for (Map.Entry<HTTPHeader, String> entry : header.get("Set-Cookie")) {
			cookies.put(entry.getKey().getName(), entry.getValue());
		}
		return cookies;
	}

	void clear() {
		header.clear();
		data = "";
	}
}
