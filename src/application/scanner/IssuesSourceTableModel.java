package application.scanner;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class IssuesSourceTableModel implements TableModel {

	private ArrayList<IssueInfoFile> files;

	IssuesSourceTableModel(ArrayList<IssueInfoFile> files) {
		this.files = files;
	}

	@Override
	public int getRowCount() {
		return files.size();
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
				return "Typical Severity";
			default:
				return null;
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return "".getClass();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		IssueInfoFile info = files.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return info.getIssueName();
			case 1:
				return info.getSeverity();
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
