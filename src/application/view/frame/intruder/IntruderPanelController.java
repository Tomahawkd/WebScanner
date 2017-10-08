package application.view.frame.intruder;

public class IntruderPanelController {

	private static IntruderPanel panel;

	public static IntruderPanel getInstance() {
		if (panel == null) panel = new IntruderPanel();
		return panel;
	}
}
