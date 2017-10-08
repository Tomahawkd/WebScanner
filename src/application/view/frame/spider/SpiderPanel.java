package application.view.frame.spider;

import javax.swing.*;

class SpiderPanel extends JTabbedPane {

	/**
	 *
	 */
	private static final long serialVersionUID = -4505246649942842808L;


	SpiderPanel() {

		ControlPane controlPane = new ControlPane();
		addTab("Control", null, controlPane, null);

		JScrollPane optionPane = new JScrollPane();
		addTab("Options", null, optionPane, null);

		JPanel panel = new JPanel();
		optionPane.setViewportView(panel);
	}
}
