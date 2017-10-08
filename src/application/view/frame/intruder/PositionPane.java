package application.view.frame.intruder;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;

class PositionPane extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 5025556023617929907L;

	private JTextArea requestPositionMarkerTextArea;
	private JLabel lblMarkCount;

	private final String mark = "¶";
	private int positionCounter = 0;

	PositionPane() {
		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		requestPositionMarkerTextArea = new JTextArea();
		requestPositionMarkerTextArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateHighLight();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateHighLight();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateHighLight();
			}
		});
		scrollPane.setViewportView(requestPositionMarkerTextArea);

		JPanel typePanel = new JPanel();
		add(typePanel, BorderLayout.NORTH);
		typePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblAttackType = new JLabel("Attack Type:");
		typePanel.add(lblAttackType);

		JComboBox<String> comboBox = new JComboBox<>();
		//TODO Need to switch to enumeration
		comboBox.setModel(new DefaultComboBoxModel<>(
				new String[]{"Single Position & Single Dictionary",
						"Muti Position & Single Dictionary",
						"Muti Position & Muti Dictionary with correspond order",
						"Muti Position & Muti Dictionary with all possible combination"}));
		comboBox.addActionListener(e -> {

		});
		typePanel.add(comboBox);

		JPanel buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.WEST);
		GridBagLayout gbl_buttonPanel = new GridBagLayout();
		gbl_buttonPanel.columnWidths = new int[]{104, 0};
		gbl_buttonPanel.rowHeights = new int[]{30, 30, 30, 30, 30, 30, 30, 30, 30, 0};
		gbl_buttonPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_buttonPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		buttonPanel.setLayout(gbl_buttonPanel);

		GridBagConstraints gbc_btnAddMark = new GridBagConstraints();
		gbc_btnAddMark.fill = GridBagConstraints.BOTH;
		gbc_btnAddMark.insets = new Insets(0, 0, 5, 0);
		gbc_btnAddMark.gridx = 0;
		gbc_btnAddMark.gridy = 0;

		JButton btnAddMark = new JButton("Add Mark");
		btnAddMark.addActionListener(e -> {
			int position = requestPositionMarkerTextArea.getCaretPosition();
			try {
				requestPositionMarkerTextArea.getDocument().insertString(position, mark, null);
				requestPositionMarkerTextArea.requestFocus();
			} catch (BadLocationException ignored) {
			}
		});
		buttonPanel.add(btnAddMark, gbc_btnAddMark);

		GridBagConstraints gbc_btnClearMark = new GridBagConstraints();
		gbc_btnClearMark.insets = new Insets(0, 0, 5, 0);
		gbc_btnClearMark.fill = GridBagConstraints.BOTH;
		gbc_btnClearMark.gridx = 0;
		gbc_btnClearMark.gridy = 1;

		JButton btnClearMark = new JButton("Clear Mark");
		btnClearMark.addActionListener(e -> {
			String content = requestPositionMarkerTextArea.getText();
			int position = 0;
			while ((position = content.indexOf(mark, position)) >= 0) {
				try {
					requestPositionMarkerTextArea.getDocument().remove(position, mark.length());
					content = requestPositionMarkerTextArea.getText();
				} catch (BadLocationException ignored) {
				}
			}
		});
		buttonPanel.add(btnClearMark, gbc_btnClearMark);

		GridBagConstraints gbc_lblMarkCount = new GridBagConstraints();
		gbc_lblMarkCount.fill = GridBagConstraints.VERTICAL;
		gbc_lblMarkCount.insets = new Insets(0, 0, 5, 0);
		gbc_lblMarkCount.gridx = 0;
		gbc_lblMarkCount.gridy = 2;

		lblMarkCount = new JLabel("Payload Position Count: " + positionCounter);
		buttonPanel.add(lblMarkCount, gbc_lblMarkCount);

		GridBagConstraints gbc_btnClear = new GridBagConstraints();
		gbc_btnClear.fill = GridBagConstraints.BOTH;
		gbc_btnClear.insets = new Insets(0, 0, 5, 0);
		gbc_btnClear.gridx = 0;
		gbc_btnClear.gridy = 7;

		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(e -> requestPositionMarkerTextArea.setText(""));
		buttonPanel.add(btnClear, gbc_btnClear);

		GridBagConstraints gbc_btnStartAttack = new GridBagConstraints();
		gbc_btnStartAttack.fill = GridBagConstraints.BOTH;
		gbc_btnStartAttack.gridx = 0;
		gbc_btnStartAttack.gridy = 8;

		JButton btnStartAttack = new JButton("Start Attack");
		//TODO
		buttonPanel.add(btnStartAttack, gbc_btnStartAttack);

	}

	int getPositionCount() {
		return positionCounter;
	}

	private void updateHighLight() {
		Highlighter highlighter = requestPositionMarkerTextArea.getHighlighter();
		highlighter.removeAllHighlights();
		DefaultHighlighter.DefaultHighlightPainter painter
				= new DefaultHighlighter.DefaultHighlightPainter(Color.CYAN);
		String content = requestPositionMarkerTextArea.getText();

		int start, end = 0;
		positionCounter = 0;
		while ((start = content.indexOf(mark, end)) >= 0) {
			try {
				positionCounter++;
				int searchEndPosition = content.indexOf(mark, start + 1);

				if (searchEndPosition == -1) {
					end = content.length();
				} else {
					end = searchEndPosition + mark.length();
				}

				highlighter.addHighlight(start, end, painter);

			} catch (BadLocationException ignored) {
			}
		}
		lblMarkCount.setText("Payload Position Count: " + positionCounter);
	}
}
