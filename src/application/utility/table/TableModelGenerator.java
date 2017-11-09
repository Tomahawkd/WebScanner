package application.utility.table;

import application.target.DataNode;
import application.utility.net.HeaderMap;

import javax.swing.table.TableModel;

public class TableModelGenerator {

	private static TableModelGenerator tableModelGenerator;
	private ContentTableModel contentTableModel;

	public static TableModelGenerator getInstance() {
		if (tableModelGenerator == null) tableModelGenerator = new TableModelGenerator();
		return tableModelGenerator;
	}

	public TableModel generateHeaderModel(HeaderMap headerMap) {
		return new HeaderTableModel(headerMap);
	}

	public TableModel generateParamModel(String param) {
		return new ParamTableModel(param);
	}

	public ContentTableModel generateContentModel(DataNode rootNode) {
		if (contentTableModel == null) contentTableModel = new ContentTableModel(rootNode);
		else contentTableModel.updateContent(rootNode);
		return contentTableModel;
	}
}