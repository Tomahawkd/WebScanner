package application.extension.sqlmap;

public class Handler {

	private SqlmapController controller;

	public SqlmapController getController() {
		if (controller == null) controller = new SqlmapController();
		return controller;
	}
}
