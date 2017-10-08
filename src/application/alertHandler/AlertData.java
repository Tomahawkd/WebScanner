package application.alertHandler;

class AlertData {

	private String time;
	private String tool;
	private String message;

	AlertData(String time, String tool, String message) {
		this.time = time;
		this.tool = tool;
		this.message = message;
	}

	String getAtIndex(int index) {
		switch (index) {
			case 0:
				return time;
			case 1:
				return tool;
			case 2:
				return message;
			default:
				return null;
		}
	}
}
