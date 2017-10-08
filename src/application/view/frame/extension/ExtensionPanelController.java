package application.view.frame.extension;

public class ExtensionPanelController {

	private static ExtensionPanel panel;

	public static ExtensionPanel getInstance() {
		if (panel == null) panel = new ExtensionPanel();
		return panel;
	}
}
