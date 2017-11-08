package application.view.contentViewer;

import javax.swing.*;

class RawViewer extends JScrollPane {

	private JTextArea rawTextArea;

	RawViewer() {
		rawTextArea = new JTextArea();
		rawTextArea.setEditable(false);
		setViewportView(rawTextArea);
	}

	void setText(String text) {
		rawTextArea.setText(text);
	}

	String getText() {
		return rawTextArea.getText();
	}

	void setEditable(boolean editable) {
		rawTextArea.setEditable(editable);
	}
}
