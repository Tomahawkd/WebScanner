package application.utility.table;

import application.utility.net.Header;
import application.utility.net.HeaderMap;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.Map;

class HeaderTableModel implements TableModel {

	private HeaderMap headerMap;

	HeaderTableModel(HeaderMap headerMap) {
		this.headerMap = headerMap;
	}

	@Override
	public int getRowCount() {
		return headerMap.size();
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
			case 0:
				return "Name";
			case 1:
				return "Value";
			default:
				return null;
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Map.Entry<String, Header> entry = headerMap.getAtIndex(rowIndex);
		switch (columnIndex) {
			case 0:
				return entry.getKey();
			case 1:
				return entry.getValue().toString();
			default:
				return null;
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

	}

	@Override
	public void addTableModelListener(TableModelListener l) {

	}

	@Override
	public void removeTableModelListener(TableModelListener l) {

	}
}
