package application.utility.file;

import application.utility.file.cache.ContextCache;
import application.utility.file.cache.ContextList;
import application.utility.thread.TaskControl;

import javax.swing.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ContextTempFileHandler {

	private static ContextTempFileHandler handler;
	private File tempFile;
	private boolean operating;
	private String token;

	public static ContextTempFileHandler getHandler() {
		if (handler == null) handler = new ContextTempFileHandler();
		return handler;
	}

	private ContextTempFileHandler() {
		try {
			tempFile = File.createTempFile("WebScannerBak", null);
			tempFile.deleteOnExit();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Temporary file creation failed",
					"Temporary File Error",
					JOptionPane.ERROR_MESSAGE);
			tempFile = null;
		}
	}

	public void start() {
		if (tempFile != null) {
			operating = true;
			token = TaskControl.getInstance().getController().addControllableTask(() -> {
				while (operating) {
					try {
						try {
							writeData(ContextCache.getInstance().toFileData());
						} catch (IOException e) {
							JOptionPane.showMessageDialog(null,
									"Temporary file write in failed",
									"Temporary File Error",
									JOptionPane.ERROR_MESSAGE);
						}
						Thread.sleep(1000 * 60 * 10);
					} catch (InterruptedException ignored) {
					}
				}
			});

		}
	}

	public void stop() {
		operating = false;
		TaskControl.getInstance().getController().stopControllableTask(token);
	}

	private void writeData(Map<String, ContextList> data) throws IOException {
		ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(tempFile));
		output.writeObject(data);
	}

	public Map<String, ContextList> readData()
			throws IOException, ClassCastException, ClassNotFoundException {
		ObjectInputStream input = new ObjectInputStream(new FileInputStream(tempFile));
		Map map = (Map) input.readObject();
		return checkData(map);
	}

	public String checkFile() {
		File parent = tempFile.getParentFile();
		if (parent != null) {
			String[] list = parent.list();

			for (String fileName : list != null ? list : new String[0])
				if (Pattern.compile("^WebScannerBak.*.tmp$")
						.matcher(fileName).matches()) return fileName;
		}
		return "";
	}
	
	private Map<String, ContextList> checkData(Map map) {
		Map<String, ContextList> listMap = new HashMap<>();
		for (Object mapping : map.entrySet()) {
			if (mapping instanceof Map.Entry) {
				if (((Map.Entry) mapping).getKey() instanceof String
						&& (((Map.Entry) mapping).getValue() instanceof ContextList)) {
					listMap.put((String)((Map.Entry) mapping).getKey(),
							(ContextList) ((Map.Entry) mapping).getValue());
				} else {
			JOptionPane.showMessageDialog(null,
					"File data does not match",
					"File Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
			}
		}
		return listMap;
	}
}