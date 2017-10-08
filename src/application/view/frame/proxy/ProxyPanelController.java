package application.view.frame.proxy;

public class ProxyPanelController {
	private static ProxyPanel panel;

	public static ProxyPanel getInstance() {
		if (panel == null) panel = new ProxyPanel();
		return panel;
	}
}
