package application.extension.sqlmapHandler;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class sqlmapController {

	private ArrayList<String> cmd = new ArrayList<>();

	sqlmapController() {
		cmd.add("python");
		cmd.add("src/extension/sqlmap/sqlmap.py");
	}

	public void setCommand(String... cmd) {
		this.cmd.addAll(Arrays.asList(cmd));
	}

	public String getCommand() {
		StringBuilder sb = new StringBuilder();
		for (String cmdStr: cmd) {
			sb.append(cmdStr).append(" ");
		}
		return sb.toString();
	}

	public int exec() {
		int exitValue;
		try {
			String[] cmd = new String[this.cmd.size()];
			this.cmd.toArray(cmd);
			Process pro = Runtime.getRuntime().exec(cmd);
			InputStream in = pro.getInputStream();
			OutputStream out = pro.getOutputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

			int charNum;
			StringBuilder sb = new StringBuilder();
			while ((charNum = bufferedReader.read()) != -1) {
				sb.append((char) charNum);
				//TODO redirect to text field
				System.out.print((char) charNum);
				if (charNum == '\n' || charNum == '\r') {
					sb = new StringBuilder();
				}
				if (sb.toString().endsWith("[Y/n] ") ||
						sb.toString().endsWith("[Y/N] ") ||
						sb.toString().endsWith("[y/n] ") ||
						sb.toString().endsWith("[y/N] ")) {
					//TODO redirect to text field
					Scanner scanner = new Scanner(System.in);
					String input = scanner.next();
					out.write((input.equalsIgnoreCase("y") ? "y\n" : "n\n")
							.getBytes(Charset.defaultCharset()));
					out.flush();
				}
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
