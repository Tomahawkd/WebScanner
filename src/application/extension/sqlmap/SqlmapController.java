package application.extension.sqlmap;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

public class SqlmapController {

	private ArrayList<String> cmd = new ArrayList<>();
	private JTextArea outputTextArea;

	SqlmapController() {
		cmd.add("python");
		cmd.add(this.getClass().getResource("/extension/sqlmap/sqlmap.py").getPath());
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
		if (outputTextArea == null)
			throw new IllegalArgumentException("output field is not set");
		int exitValue;
		try {
			String[] cmd = new String[this.cmd.size()];
			this.cmd.toArray(cmd);
			Process pro = Runtime.getRuntime().exec(cmd);
			InputStream in = pro.getInputStream();
			OutputStream out = pro.getOutputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

			Thread executeThread = new Thread(() -> {
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
							while(CommandLineListener.getInstance().isContinue()) {
								Thread.sleep(10L);
							}
							String input = CommandLineListener.getInstance().getCommand();
							CommandLineListener.getInstance().suspendThread();
							out.write((input).getBytes(Charset.defaultCharset()));
							out.flush();
						}
					}
				} catch (IOException | InterruptedException e) {
					pro.destroy();
					CommandLineListener.getInstance().setThreadError();
				}
			}, "sqlmap Excution Thread");
			executeThread.start();

			if (CommandLineListener.getInstance().getThreadStatus() == 2) {
				throw new IOException("Exception catched in thread");
			}
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
