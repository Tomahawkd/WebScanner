package application.extension.sqlmap;

import application.alertHandler.AlertHandler;

public class CommandLineListener {

	private boolean continueThread = true;
	private String command = "";
	private static CommandLineListener textListener;
	private int threadStatus;
	private int exitValue;

	public static CommandLineListener getInstance() {
		if (textListener == null) textListener = new CommandLineListener();
		return textListener;
	}

	public void setCommand(String command) {
		if (!command.equals("")) {
			this.command = command + "\n";
			continueThread();
		}
	}

	public String getCommand() {
		return command;
	}

	public void setExitValue(int exitValue) {
		this.exitValue = exitValue;
	}

	public int getExitValue() {
		return exitValue;
	}

	boolean isContinue() {
		return continueThread;
	}

	synchronized void suspendThread() {
		continueThread = true;
	}

	private synchronized void continueThread() {
		continueThread = false;
	}

	int getThreadStatus() {
		return threadStatus;
	}

	void setThreadError() {
		this.threadStatus = 2;
		AlertHandler.getInstance().addAlert("Extension.SQLmap",
				"SQL map process exited unexpectedly");
	}


}
