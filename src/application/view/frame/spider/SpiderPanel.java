package application.view.frame.spider;

import javax.swing.*;

public class SpiderPanel extends JTabbedPane {

	/**
	 *
	 */
	private static final long serialVersionUID = -4505246649942842808L;
	private ControlPane controlPane;

	SpiderPanel() {

		controlPane = new ControlPane();
		addTab("Control", null, controlPane, null);

		JScrollPane optionPane = new JScrollPane();
		addTab("Options", null, optionPane, null);

		JPanel panel = new JPanel();
		optionPane.setViewportView(panel);
	}

	public void updateRequestCounter(int requestCount) {
		controlPane.updateRequestCounter(requestCount);
	}

	public void updateQueueCounter(int queueCount) {
		controlPane.updateQueueCounter(queueCount);
	}
}
