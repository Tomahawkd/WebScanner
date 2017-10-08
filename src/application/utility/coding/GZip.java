package application.utility.coding;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZip {
	private static GZip instance;

	public static GZip getInstance() {
		if (instance == null) instance = new GZip();
		return instance;
	}

	public static void clean() {
		instance = null;
	}

	public byte[] decode(String source) throws IOException {
		return this.decode(source, "utf-8");
	}

	public byte[] decode(String source, String encoding) throws IOException {
		return this.decode(source.getBytes(encoding));
	}

	public byte[] decode(byte[] source) throws IOException {
		GZIPInputStream in = new GZIPInputStream(new ByteArrayInputStream(source));
		BufferedInputStream is = new BufferedInputStream(in);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int count;
		while ((count = is.read(buf)) != -1) {
			os.write(buf, 0, count);
		}
		return os.toByteArray();
	}

	public byte[] encode(String source) throws IOException {
		return this.encode(source, "utf-8");
	}

	public byte[] encode(String source, String encoding) throws IOException {
		return this.encode(source.getBytes(encoding));
	}

	public byte[] encode(byte[] source) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(source);
		gzip.close();
		return out.toByteArray();
	}
}
