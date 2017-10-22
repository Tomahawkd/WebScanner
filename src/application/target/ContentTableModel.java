package application.target;

import application.utility.net.Exceptions.IllegalHeaderDataException;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;

public class ContentTableModel implements TableModel {

	private static ContentTableModel tableModel;
	private DataNode treeNode = TargetTreeModel.getDefaultModel().getRoot();

	public static ContentTableModel getDefaultModel() {
		if (tableModel == null) tableModel = new ContentTableModel();
		return tableModel;
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
		return "".getClass();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		DefaultMutableTreeNode leafNode = treeNode;
		for (int i = 0; i < rowIndex ; i++) {
			leafNode = leafNode.getNextLeaf();
		}
		if (leafNode != null && leafNode instanceof DataNode) {
			try {
				switch (columnIndex) {
					case 0:
						return TargetTreeModel.getDefaultModel().getRoot().getName();
					case 1:
						return ((DataNode) leafNode).getData().getHeader().getMethod();
					case 2:
						//TODO
						return ((DataNode) leafNode).getName()
								.equals(TargetTreeModel.getDefaultModel().getRoot().getName()) ?
								"/" : ((DataNode) leafNode).getName();
					case 3:
						return ((DataNode) leafNode).getData().getHeader().get("Status", "Code");
					case 4:
						return ((DataNode) leafNode).getData().getHeader().get("Content", "Content-Type");
					case 5:
						return ((DataNode) leafNode).getData().getData().length();
					default:
						return null;
				}
			} catch (IllegalHeaderDataException | NullPointerException e) {
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
