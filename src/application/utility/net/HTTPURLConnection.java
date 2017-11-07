package application.utility.net;

import application.utility.coding.GZip;
import application.utility.net.Exceptions.IllegalHeaderDataException;

import javax.net.ssl.SSLSocketFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

class HTTPURLConnection {

	/**
	 *
	 */
	private EditableContext context;
	private int timeout = 30000;

	HTTPURLConnection(EditableContext context) {
		this.context = context;
	}

	/**
	 * @throws IllegalArgumentException throws when the URL is null or the context context is null
	 * @throws IOException              socket exceptions
	 */
	void openConnection()
			throws IndexOutOfBoundsException, IllegalArgumentException, IOException {

		if (context.getURL() == null) throw new IllegalArgumentException("URL cannot be null");
		if (context == null) throw new IllegalArgumentException("Request data cannot be null");

		Socket socket = null;
		OutputStream out;
		InputStream in;

		try {

			// Get the server's host and port
			String host = context.getURL().getHost();
			int port = context.getURL().getPort() == -1 ? 80 : context.getURL().getPort();

			socket = getSocket();

			/*
			 * Connect to server
			 */

			socket.connect(new InetSocketAddress(host, port), timeout);

			// Send HTTP context
			out = socket.getOutputStream();
			out.write(context.getRequestForm().getBytes());

			// Accept HTTP response
			in = socket.getInputStream();

			//Header
			context.clearResponse();
			int isClose;
			ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
			while ((isClose = in.read()) != -1) {
				bytesOut.write(isClose);
				if ('\n' == isClose) {
					String str = new String(bytesOut.toByteArray()).trim();
					bytesOut.reset();
					if ("".equals(str)) break;
					context.addResponse(str);
				}
			}

			//body
			int count;
			String transferEncoding = "";
			Header transferEncodingHeader = this.context.getResponseHeader().get("Transfer-Encoding");
			if (transferEncodingHeader != null) transferEncoding = transferEncodingHeader.toString();

			if ("chunked".equals(transferEncoding)) {
				bytesOut = parseChunk(in);
			} else {
				try {
					Header contentLengthHeader = this.context.getResponseHeader().get("Content-Length");
					if (contentLengthHeader != null) {
						int contentLength = Integer.valueOf(contentLengthHeader.toString());
						byte[] buf = new byte[1024];
						int countNum = 0;
						while ((count = in.read(buf)) != -1) {
							bytesOut.write(buf, 0, count);
							countNum = countNum + count;
							if (contentLength == countNum) {
								break;
							}
						}
					}
				} catch (IllegalHeaderDataException | NumberFormatException ignored) {
				}
			}

			byte[] contentBuf = bytesOut.toByteArray();

			//GZip
			try {
				String contentEncoding = "";
				Header contentEncodingHeader = this.context.getResponseHeader().get("Content-Encoding");
				if (contentEncodingHeader != null) contentEncoding = contentEncodingHeader.toString();
				if ("gzip".equals(contentEncoding)) {
					contentBuf = GZip.getInstance().decode(contentBuf);
					GZip.clean();
				}
			} catch (IllegalHeaderDataException ignored) {
			}

			// Save origin byte data array
			context.setResponseData(contentBuf);

		} finally {
			if (null != socket && socket.isConnected() && !socket.isClosed()) {

				// Close socket
				socket.close();

			}
		}

	}

	void setTimeout(int timeout) throws IllegalArgumentException {
		if (timeout < 0) throw new IllegalArgumentException("Timeout cannot be negative");
		this.timeout = timeout;
	}

	int getTimeout() {
		return timeout;
	}

	private Socket getSocket() throws IOException {
		Socket socket;

		if (context.getURL().getProtocol().equalsIgnoreCase("https")) {
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
