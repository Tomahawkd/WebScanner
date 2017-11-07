package application.view.contentViewer;

import javax.swing.*;
import javax.swing.table.TableModel;

class HeaderViewer extends JScrollPane {

	private JTable paramTable;

	HeaderViewer() {

		paramTable = new JTable();
		paramTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		setViewportView(paramTable);
	}

	void setModel(TableModel dataModel) {
		paramTable.setModel(dataModel);
	}

	void updateTableUI() {
		paramTable.updateUI();
	}
}
