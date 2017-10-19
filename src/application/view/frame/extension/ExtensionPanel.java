package application.view.frame.extension;

import javax.swing.*;

class ExtensionPanel extends JTabbedPane {

	/**
	 *
	 */
	private static final long serialVersionUID = -8797634462228397688L;

	ExtensionPanel() {

		JPanel sqlmapPanel = new SqlMapPane();
		addTab("sqlmap", null, sqlmapPanel, null);

	}


}
