package application.view.contentViewer;

import javax.swing.*;
import javax.swing.table.TableModel;

public abstract class Viewer extends JTabbedPane {

	private RawViewer rawViewer;
	private HeaderViewer headerViewer;

	Viewer() {
		rawViewer = new RawViewer();
		addTab("Raw", rawViewer);

		headerViewer = new HeaderViewer();
		addTab("Header", headerViewer);
	}

	public void setText(String text) {
		rawViewer.setText(text);
	}

	public String getText() {
		return rawViewer.getText();
	}

	public void setTableModel(TableModel model) {
		headerViewer.setModel(model);
	}

	public void updateViewerData() {
		headerViewer.updateTableUI();
	}
}
