package application.view.frame.scanner;


import javax.swing.*;

class ScannerPanel extends JTabbedPane {

	/**
	 *
	 */
	private static final long serialVersionUID = -3583293288486939710L;


	ScannerPanel() {

		IssueDefinitionPane issuePane = new IssueDefinitionPane();
		addTab("Issue Definition", null, issuePane, null);

		LiveScanPane liveScanPane = new LiveScanPane();
		addTab("Live Scanning", null, liveScanPane, null);

		JPanel optionPane = new JPanel();
		addTab("Options", null, optionPane, null);


	}
}
