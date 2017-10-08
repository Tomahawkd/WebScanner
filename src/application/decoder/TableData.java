package application.decoder;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;

class TableData implements TableModel {

	private ArrayList<Byte> tableDataSource;

	TableData(byte[] source) {
		tableDataSource = new ArrayList<>();
		for (byte a : source) tableDataSource.add(a);
	}

	void setData(byte[] source) {
		tableDataSource.clear();
		for (byte a : source) tableDataSource.add(a);
	}

	byte[] getData() {
		byte[] result = new byte[tableDataSource.size()];
		int index = 0;
		for (byte digit : tableDataSource) {
			result[index] = digit;
			index++;
		}
		return result;
	}

	@Override
	public int getRowCount() {
		return (tableDataSource.size() / 16 + 1);
	}

	@Override
	public int getColumnCount() {
		return 16;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return null;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		boolean hasByte = false;
		if (tableDataSource.get(rowIndex * 16 + columnIndex) != null) {
			hasByte = true;
		}
		return hasByte;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			int digit = tableDataSource.get(rowIndex * 16 + columnIndex);
			if (digit < 0) digit += 256;
			return digit < 16 ? "0" + Integer.toHexString(digit) : Integer.toHexString(digit);
		} catch (IndexOutOfBoundsException e) {
			return "--";
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		try {
			String value = (String) aValue;
			if (!value.equals("--")) {
				tableDataSource.set(rowIndex * 16 + columnIndex, Byte.parseByte(value, 16));
			}

		} catch (ClassCastException e) {
			JOptionPane.showMessageDialog(null,
					"Invalid Hex Code",
					"Error", JOptionPane.ERROR_MESSAGE);
		} catch (IndexOutOfBoundsException e) {
			if (rowIndex * 16 + columnIndex + 1 == tableDataSource.size()) {
				String value = (String) aValue;
				if (!value.equals("--")) {
					try {
						tableDataSource.add(Byte.parseByte(value, 16));
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(null,
								"Invalid Hex Code",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}

		}
	}

	@Override
	public void addTableModelListener(TableModelListener l) {

	}

	@Override
	public void removeTableModelListener(TableModelListener l) {

	}
}
