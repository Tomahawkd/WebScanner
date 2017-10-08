package application.view.frame.proxy;

import javax.swing.*;

class ProxyPanel extends JTabbedPane {

	/**
	 *
	 */
	private static final long serialVersionUID = -3819032896323763438L;

	ProxyPanel() {

		InterceptPane interceptPane = new InterceptPane();
		addTab("Intercept", null, interceptPane, null);

		HistoryPane historyPane = new HistoryPane();
		addTab("History", null, historyPane, null);

		//TODO
		JPanel optionPane = new JPanel();
		addTab("Options", null, optionPane, null);

	}
}
