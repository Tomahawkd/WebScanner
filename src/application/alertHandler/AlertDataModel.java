package application.alertHandler;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class AlertDataModel implements TableModel {

	private ArrayList<AlertData> alertDataList;

	AlertDataModel() {
		alertDataList = new ArrayList<>();
	}

	void addAlert(String time, String tool, String message) {
		alertDataList.add(new AlertData(time, tool, message));
	}

	@Override
	public int getRowCount() {
		return alertDataList.size();
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
			case 0:
				return "Time";
			case 1:
				return "Tool";
			case 2:
				return "Message";
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
		int index = 0;
		for (AlertData data : alertDataList) {
			if (rowIndex == index) {
				return data.getAtIndex(columnIndex);
			}
			index++;
		}
		throw new IndexOutOfBoundsException();
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
