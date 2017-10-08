package application.utility.coding;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class URLCoder {
	private static URLCoder instance;

	public static URLCoder getInstance() {
		if (instance == null) instance = new URLCoder();
		return instance;
	}

	public static void clean() {
		instance = null;
	}

	public String encode(String source) throws UnsupportedEncodingException {
		return this.encode(source, "utf-8");
	}

	public String encode(String source, String encoding) throws UnsupportedEncodingException {
		return URLEncoder.encode(source, encoding);
	}

	public String decode(String source) throws UnsupportedEncodingException {
		return this.decode(source, "utf-8");
	}

	public String decode(String source, String encoding) throws UnsupportedEncodingException {
		return URLDecoder.decode(source, encoding);
	}
}
