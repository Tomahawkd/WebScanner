package application.utility.table;

import application.utility.net.HeaderMap;

import javax.swing.table.TableModel;

public class TableModelGenerator {

	private static TableModelGenerator tableModelGenerator;

	public static TableModelGenerator getInstance() {
		if (tableModelGenerator == null) tableModelGenerator = new TableModelGenerator();
		return tableModelGenerator;
	}

	public TableModel generateHeaderModel(HeaderMap headerMap) {
		return new HeaderTableModel(headerMap);
	}
}
