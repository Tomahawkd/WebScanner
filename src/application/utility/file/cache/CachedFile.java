package application.utility.file.cache;

import javax.swing.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

class CachedFile {

	private Map<Integer, File> tempFileMap;

	CachedFile() {
		tempFileMap = new HashMap<>();
	}

	void writeData(String host, ContextList data) throws IOException {
		int token = host.hashCode();
		try {
			if (tempFileMap.get(token) != null) {
				ObjectOutputStream output = new ObjectOutputStream(
						new FileOutputStream(tempFileMap.get(token)));
				output.writeObject(data);
			} else {
				File tempFile = File.createTempFile("WebScannerCache" + token, ".cache");
				tempFile.deleteOnExit();
				ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(tempFile));
				output.writeObject(data);
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Temporary file creation failed",
					"Temporary File Error",
					JOptionPane.ERROR_MESSAGE);
			throw new IOException(e.getMessage());
		}
	}

	ContextList readData(String host)
			throws IOException, ClassCastException, ClassNotFoundException {
		File tempFile = tempFileMap.get(host.hashCode());
		if (tempFile != null) {
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(tempFile));
			return (ContextList) input.readObject();

		}

		return null;
	}
}