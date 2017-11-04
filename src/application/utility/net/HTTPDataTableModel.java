package application.utility.net;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.Map;

class HTTPDataTableModel implements TableModel {

	private HTTPHeaderMapImpl headerMap;

	HTTPDataTableModel(HTTPHeaderMapImpl headerMap) {
		this.headerMap = headerMap;
	}

	@Override
	public int getRowCount() {
		return headerMap.size();
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
			case 0:
				return "Type";
			case 1:
				return "Name";
			case 2:
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
		return columnIndex == 2 && headerMap.getType() == HTTPHeaderMap.REQUEST;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Map.Entry<HTTPHeader, String> entry = headerMap.getAtIndex(rowIndex);
		switch (columnIndex) {
			case 0:
				return entry.getKey().getType();
			case 1:
				return entry.getKey().getName();
			case 2:
				return entry.getValue();
			default:
				return null;
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex == 2 && aValue instanceof String) {
			Map.Entry<HTTPHeader, String> entry = headerMap.getAtIndex(rowIndex);
			headerMap.put(entry.getKey().getType(), entry.getKey().getName(), (String) aValue);
		}
	}

	@Override
	public void addTableModelListener(TableModelListener l) {

	}

	@Override
	public void removeTableModelListener(TableModelListener l) {

	}
}
