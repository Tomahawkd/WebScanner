package application.view.contentViewer;

import javax.swing.*;
import javax.swing.table.TableModel;

class TableViewer extends JScrollPane {

	private JTable table;

	TableViewer() {

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		setViewportView(table);
	}

	void setModel(TableModel dataModel) {
		table.setModel(dataModel);
	}

	void updateTableUI() {
		table.updateUI();
	}
}
