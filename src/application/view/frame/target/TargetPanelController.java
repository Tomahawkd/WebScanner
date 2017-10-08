package application.view.frame.target;

public class TargetPanelController {

	private static TargetPanel targetPanel;

	public static TargetPanel getInstance() {
		if (targetPanel == null) targetPanel = new TargetPanel();
		return targetPanel;
	}
}
