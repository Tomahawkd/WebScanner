package application.utility.coding;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashCoder {

	private static HashCoder instance;
	private MessageDigest md;

	public static HashCoder getInstance(HashType algorithm) throws NoSuchAlgorithmException {
		if (instance == null) instance = new HashCoder(algorithm.toString());
		return instance;
	}

	private HashCoder(String algorithm) throws NoSuchAlgorithmException {
		md = MessageDigest.getInstance(algorithm);
	}

	public static void clean() {
		instance = null;
	}

	public String getHash(String source) {
		StringBuilder md5str = new StringBuilder();
		byte[] buff = md.digest(source.getBytes());
		int digital;
		for (byte aByte : buff) {
			digital = aByte;

			if (digital < 0) {
				digital += 256;
			}
			if (digital < 16) {
				md5str.append("0");
			}
			md5str.append(Integer.toHexString(digital));

		}
		return md5str.toString();
	}
}
