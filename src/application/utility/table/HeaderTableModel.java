package application.utility.table;

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
}