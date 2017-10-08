package application.view.frame.alert;

public class AlertPanelController {

	private static AlertPanel panel;

	public static AlertPanel getInstance() {
		if (panel == null) panel = new AlertPanel();
		return panel;
	}

	public static void updateTable() {
		getInstance().updateTable();
	}

}
