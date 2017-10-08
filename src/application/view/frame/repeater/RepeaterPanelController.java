package application.view.frame.repeater;

public class RepeaterPanelController {

	private static RepeaterPanel panel;

	public static RepeaterPanel getInstance() {
		if (panel == null) panel = new RepeaterPanel();
		return panel;
	}
}
