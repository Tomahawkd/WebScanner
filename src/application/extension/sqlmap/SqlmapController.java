package application.extension.sqlmap;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class SqlmapController {

	private ArrayList<String> cmd = new ArrayList<>();
	private JTextArea outputTextArea;
	private JTextField inputTextField;

	SqlmapController() {
		cmd.add("python");
		cmd.add("src/extension/sqlmap/sqlmap.py");
	}

	public void setCommand(String... cmd) {
		this.cmd.addAll(Arrays.asList(cmd));
	}

	public String getCommand() {
		StringBuilder sb = new StringBuilder();
		for (String cmdStr : cmd) {
			sb.append(cmdStr).append(" ");
		}
		return sb.toString();
	}

	public void setOutput(JTextArea textArea) {
		this.outputTextArea = textArea;
	}

	public int exec() {
		if (outputTextArea == null || inputTextField == null)
			throw new IllegalArgumentException("I/O field is not set");
		int exitValue;
		try {
			String[] cmd = new String[this.cmd.size()];
			this.cmd.toArray(cmd);
			Process pro = Runtime.getRuntime().exec(cmd);
			InputStream in = pro.getInputStream();
			OutputStream out = pro.getOutputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

			Runnable runnable = () -> {
				try {
					int charNum;
					StringBuilder sb = new StringBuilder();
					while ((charNum = bufferedReader.read()) != -1) {
						sb.append((char) charNum);
						if (charNum == '\n' || charNum == '\r') {
							outputTextArea.append(sb.toString());
							sb = new StringBuilder();
						}
						if (sb.toString().endsWith("[Y/n] ") ||
								sb.toString().endsWith("[Y/N] ") ||
								sb.toString().endsWith("[y/n] ") ||
								sb.toString().endsWith("[y/N] ")) {
							outputTextArea.append(sb.toString());
							sb = new StringBuilder();

							//TODO
							Scanner scanner = new Scanner(System.in);
							String input = scanner.next();
							out.write((input.equalsIgnoreCase("y") ? "y\n" : "n\n")
									.getBytes(Charset.defaultCharset()));
							out.flush();
						}
					}
				} catch (IOException e) {
					pro.destroy();
				}
			};
			Thread executeThread = new Thread(runnable);

			pro.waitFor();
			exitValue = pro.exitValue();
		} catch (IOException | InterruptedException e) {
			exitValue = 2;
		} catch (ArrayStoreException ignore) {
			exitValue = -1;
		}
		return exitValue;
	}
}
