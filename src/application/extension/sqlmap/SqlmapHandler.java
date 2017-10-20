package application.extension.sqlmap;

public class SqlmapHandler {

	private static SqlmapController controller;

	public static SqlmapController getController() {
		if (controller == null) controller = new SqlmapController();
		return controller;
	}
}
