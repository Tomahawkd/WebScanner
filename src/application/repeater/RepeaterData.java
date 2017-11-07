package application.repeater;

import application.utility.net.CoreData;

public class RepeaterData {

	private static CoreData data;

	public static CoreData getInstance() {
		if (data == null) data = new CoreData();
		return data;
	}
}
