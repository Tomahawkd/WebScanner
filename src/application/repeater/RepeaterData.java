package application.repeater;

import application.utility.net.Form;

public class RepeaterData {

	private static Form data;

	public static Form getInstance() {
		if (data == null) data = new Form();
		return data;
	}
}