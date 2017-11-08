package application.view.contentViewer;

import javax.swing.*;
import javax.swing.table.TableModel;

public class Viewer extends JTabbedPane {

	private RawViewer rawViewer;
	private TableViewer headerViewer;
	private TableViewer paramViewer;

	Viewer(boolean editable) {
		rawViewer = new RawViewer();
		rawViewer.setEditable(editable);
		addTab("Raw", rawViewer);

		headerViewer = new TableViewer();
		addTab("Header", headerViewer);

		paramViewer = new TableViewer();
		addTab("Param", paramViewer);
	}

	public void setText(String text) {
		rawViewer.setText(text);
	}

	public String getText() {
		return rawViewer.getText();
	}

	public void setHeaderTableModel(TableModel model) {
		headerViewer.setModel(model);
	}

	public void setParamTableModel(TableModel model) {
		paramViewer.setModel(model);
	}

	public void updateViewerData() {
		headerViewer.updateTableUI();
		paramViewer.updateTableUI();
	}
}