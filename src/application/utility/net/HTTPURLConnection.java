package application.utility.net;

import application.utility.coding.GZip;
import application.utility.net.Exceptions.IllegalHeaderDataException;

import javax.net.ssl.SSLSocketFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

public class HTTPURLConnection {

	/**
	 *
	 */
	private URL url;
	private ContextImpl request;
	private ContextImpl response;
	private int timeout = 30000;

	HTTPURLConnection(ContextImpl request, ContextImpl response) {
		this.request = request;
		this.response = response;
	}

	HTTPURLConnection(ContextImpl request, ContextImpl response, URL url) {
		this(request, response);
		this.url = url;
	}

	HTTPURLConnection(ContextImpl request, ContextImpl response, String urlStr) throws MalformedURLException {
		this(request, response, new URL(urlStr));
	}

	/**
	 * @throws IllegalArgumentException throws when the URL is null or the request context is null
	 * @throws IOException              socket exceptions
	 */
	void openConnection()
			throws IndexOutOfBoundsException, IllegalArgumentException, IOException {

		if (url == null) throw new IllegalArgumentException("URL cannot be null");
		if (request == null) throw new IllegalArgumentException("Request data cannot be null");

		Socket socket = null;
		OutputStream out;
		InputStream in;

		try {

			// Get the server's host and port
			String host = url.getHost();
			int port = url.getPort() == -1 ? 80 : url.getPort();

			socket = getSocket();

			/*
			 * Connect to server
			 */

			socket.connect(new InetSocketAddress(host, port), timeout);

			// Send HTTP request
			out = socket.getOutputStream();
			out.write(request.getContext().getBytes());

			// Accept HTTP response
			in = socket.getInputStream();

			//Header
			response.clear();
			int isClose;
			ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
			while ((isClose = in.read()) != -1) {
				bytesOut.write(isClose);
				if ('\n' == isClose) {
					String str = new String(bytesOut.toByteArray()).trim();
					bytesOut.reset();
					if ("".equals(str)) break;
					response.addHeader(str);
				}
			}

			//body
			int count;
			String transferEncoding = "";
			try {
				transferEncoding = this.response.getHeader().get("Header", "Transfer-Encoding");
			} catch (IllegalHeaderDataException ignored) {
			}
			if ("chunked".equals(transferEncoding)) {
				bytesOut = parseChunk(in);
			} else {
				try {
					int contentLength = Integer.valueOf(this.response.getHeader()
							.get("Header", "Content-Length"));
					byte[] buf = new byte[1024];
					int countNum = 0;
					while ((count = in.read(buf)) != -1) {
						bytesOut.write(buf, 0, count);
						countNum = countNum + count;
						if (contentLength == countNum) {
							break;
						}
					}
				} catch (IllegalHeaderDataException | NumberFormatException ignored) {
				}
			}

			byte[] contentBuf = bytesOut.toByteArray();

			//GZip
			try {
				String contentEncoding = this.response.getHeader().get("Header", "Content-Encoding");
				if ("gzip".equals(contentEncoding)) {
					contentBuf = GZip.getInstance().decode(contentBuf);
					GZip.clean();
				}
			} catch (IllegalHeaderDataException ignored) {
			}

			// Save origin byte data array
			response.setData(contentBuf);

		} finally {
			if (null != socket && socket.isConnected() && !socket.isClosed()) {

				// Close socket
				socket.close();

			}
		}

	}

	public void setURL(String urlStr) throws MalformedURLException {
		this.url = new URL(urlStr);
	}

	public void setURL(URL url) {
		this.url = url;
	}

	public URL getURL() {
		return url;
	}

	void setTimeout(int timeout) throws IllegalArgumentException {
		if (timeout < 0) {
			this.timeout = 30000;
			throw new IllegalArgumentException("Timeout cannot be negative");
		} else {
			this.timeout = timeout;
		}
	}

	int getTimeout() {
		return timeout;
	}

	private Socket getSocket() throws IOException {
		Socket socket;

		if (url.getProtocol().equalsIgnoreCase("https")) {
			socket = SSLSocketFactory.getDefault().createSocket();
		} else {
			socket = new Socket();
		}

		// Properties
		socket.setTcpNoDelay(true);
		socket.setReuseAddress(true);
		socket.setSoTimeout(timeout);
		socket.setSoLinger(true, 5);
		socket.setSendBufferSize(1024);
		socket.setReceiveBufferSize(1024);
		socket.setKeepAlive(true);
		socket.setTrafficClass(0x04 | 0x10);
		socket.setPerformancePreferences(2, 1, 3);

		return socket;
	}

	private ByteArrayOutputStream parseChunk(InputStream in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			out = parseChunk(out, in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}

	private ByteArrayOutputStream parseChunk(ByteArrayOutputStream out, InputStream in) throws IOException {
		ByteArrayOutputStream temp = new ByteArrayOutputStream();
		int ch;
		while ((ch = in.read()) != -1) {
			if ('\r' == ch) {
				continue;
			}
			if ('\n' == ch) {
				break;
			}
			temp.write(ch);
		}
		String tempStr = new String(temp.toByteArray());
		if (!"".equals(tempStr.trim())) {
			int count = Integer.parseInt(new String(temp.toByteArray()).trim(), 16);
			if (count == 0) {
				return out;
			}
			int num = 0;
			while (num < count) {
				int chr = in.read();
				out.write(chr);
				num++;
			}
		}
		out = parseChunk(out, in);

		return out;
	}
}
