package application.view.frame.scanner;

public class ScannerPanelController {

	private static ScannerPanel scannerPanel;

	public static ScannerPanel getInstance() {
		if (scannerPanel == null) scannerPanel = new ScannerPanel();
		return scannerPanel;
	}
}
