package application.utility.table;

import application.target.DataNode;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.tree.TreeNode;

public class ContentTableModel implements TableModel {

	private DataNode treeNode;

	ContentTableModel(DataNode rootNode) {
		this.treeNode = rootNode;
	}

	public void updateContent(DataNode treeNode) {
		this.treeNode = treeNode;
	}

	@Override
	public int getRowCount() {
		return treeNode.getLeafCount();
	}

	@Override
	public int getColumnCount() {
		return 6;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
			case 0:
				return "Host";
			case 1:
				return "Method";
			case 2:
				return "Path";
			case 3:
				return "Status";
			case 4:
				return "MIME type";
			case 5:
				return "Length";
			default:
				return null;
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		for (int i = 0; i < rowIndex; i++) {
			TreeNode next;
			if (treeNode != null && (next = treeNode.getNextLeaf()) instanceof DataNode) {
				treeNode = (DataNode) next;
			} else return "";
		}
		if (treeNode != null) {
			try {
				switch (columnIndex) {
					case 0:
						return treeNode.getContext().getHost();
					case 1:
						return treeNode.getContext().getMethod();
					case 2:
						return treeNode.getContext().getPath();
					case 3:
						return treeNode.getContext().getStatusCode();
					case 4:
						return treeNode.getContext().getMINEType();
					case 5:
						return treeNode.getContext().getResponseData().length();
					default:
						return null;
				}
			} catch (NullPointerException e) {
				return "";
			}
		} else {
			return "";
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
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
