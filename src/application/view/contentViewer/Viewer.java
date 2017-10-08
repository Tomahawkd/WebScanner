package application.view.contentViewer;

import javax.swing.*;
import javax.swing.table.TableModel;

public abstract class Viewer extends JTabbedPane {

	private RawViewer rawViewer;
	private ParamViewer paramViewer;

	Viewer() {
		rawViewer = new RawViewer();
		addTab("Raw", rawViewer);

		paramViewer = new ParamViewer();
		addTab("Param", paramViewer);
	}

	public void setText(String text) {
		rawViewer.setText(text);
	}

	public String getText() {
		return rawViewer.getText();
	}

	public void setTableModel(TableModel model) {
		paramViewer.setModel(model);
	}

	public void updateViewerData() {
		paramViewer.updateTableUI();
	}
}
