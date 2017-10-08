package application.repeater;

import application.utility.net.Connection;

public class RepeaterData {

	private static Connection connection;

	public static Connection getInstance() {
		if (connection == null) connection = new Connection();
		return connection;
	}
}
