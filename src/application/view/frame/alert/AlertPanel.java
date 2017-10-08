package application.view.frame.alert;

import application.alertHandler.AlertHandler;

import javax.swing.*;
import java.awt.*;

class AlertPanel extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 8893948662126191227L;

	private JTable alertTable;

	AlertPanel() {
		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		alertTable = new JTable();
		alertTable.setModel(AlertHandler.getInstance().getDataModel());
		alertTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		scrollPane.setViewportView(alertTable);
	}

	void updateTable() {
		alertTable.updateUI();
	}

}
