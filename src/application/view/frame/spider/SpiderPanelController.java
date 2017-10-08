package application.view.frame.spider;

public class SpiderPanelController {
	private static SpiderPanel panel;

	public static SpiderPanel getInstance() {
		if (panel == null) panel = new SpiderPanel();
		return panel;
	}
}
