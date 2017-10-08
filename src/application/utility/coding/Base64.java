package application.utility.coding;

import javax.swing.*;

public class Base64 {
	private static Base64 instance;

	public static Base64 getInstance() {
		if (instance == null) instance = new Base64();
		return instance;
	}

	public static void clean() {
		instance = null;
	}

	public String decode(String source) {
		try {
			return new String(java.util.Base64.getDecoder().decode(source));
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null,
					"The base64 data is invalid",
					"Data Invalid",
					JOptionPane.INFORMATION_MESSAGE);
			return "";
		}
	}

	public String encode(String data) {
		return new String(java.util.Base64.getEncoder().encode(data.getBytes()));
	}
}
