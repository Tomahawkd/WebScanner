package application.utility.net;

import application.utility.net.Exceptions.IllegalHeaderDataException;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Context {

	private HTTPHeaderMap header;
	private String data;

	Context(int type) {
		this.header = new HTTPHeaderMap(type);
		this.data = "";
	}

	public Context(HTTPHeaderMap header, byte[] data) {
		this.header = header;
		setData(data);
	}

	public HTTPHeaderMap getHeader() {
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

	public String getContext() throws IllegalHeaderDataException {
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
