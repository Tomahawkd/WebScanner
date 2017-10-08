package application.utility.net;

import application.utility.net.Exceptions.IllegalHeaderDataException;

import java.io.UnsupportedEncodingException;

public class Context {

	private HTTPHeaderMap<HTTPHeader, String> header;
	private String data;

	Context(int type) {
		this.header = new HTTPHeaderMap<>(type);
		this.data = "";
	}

	public Context(HTTPHeaderMap<HTTPHeader, String> header, byte[] data) {
		this.header = header;
		setData(data);
	}

	public HTTPHeaderMap<HTTPHeader, String> getHeader() {
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

	public void clear() {
		header.clear();
		data = "";
	}
}
