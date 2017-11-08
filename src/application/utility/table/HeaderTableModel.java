package application.utility.table;

import application.utility.net.EditableHeader;
import application.utility.net.Exceptions.IllegalHeaderDataException;
import application.utility.net.Header;
import application.utility.net.HeaderMap;

import java.util.Map;

class HeaderTableModel extends PairTableModel {

	private HeaderMap headerMap;

	HeaderTableModel(HeaderMap headerMap) {
		this.headerMap = headerMap;
	}

	@Override
	public int getRowCount() {
		return headerMap.size();
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
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return headerMap.getType() == HeaderMap.REQUEST && columnIndex == 1;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (isCellEditable(rowIndex, columnIndex)) {
			if (aValue instanceof String) {
				Map.Entry<String, Header> entry = headerMap.getAtIndex(rowIndex);
				EditableHeader header = (EditableHeader) entry.getValue();
				try {
					header.setValue((String) aValue);
				} catch (IllegalHeaderDataException ignored) {
				}
			}
		}
	}
}