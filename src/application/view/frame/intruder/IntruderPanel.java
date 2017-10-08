package application.view.frame.intruder;

import javax.swing.*;

class IntruderPanel extends JTabbedPane {

	/**
	 *
	 */
	private static final long serialVersionUID = 2455076640135993784L;

	private PositionPane positionPane;

	IntruderPanel() {

		TargetPane targetPane = new TargetPane();
		addTab("Target", null, targetPane, null);

		positionPane = new PositionPane();
		addTab("Position", null, positionPane, null);

		JPanel payloadPane = new JPanel();
		addTab("Payload", null, payloadPane, null);

		JPanel optionPane = new JPanel();
		addTab("Options", null, optionPane, null);

	}

	int getPositionCount() {
		return positionPane.getPositionCount();
	}
}
