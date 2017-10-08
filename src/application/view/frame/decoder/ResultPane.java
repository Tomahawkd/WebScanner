package application.view.frame.decoder;

import javax.swing.*;
import java.awt.*;

class ResultPane extends JPanel {

	private JTable hexTable;
	private JTextArea rawTextArea;

	ResultPane() {

		setPreferredSize(new Dimension(1000, 200));
		setLayout(new GridLayout(0, 1, 0, 0));

		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel resultPanel = new JPanel();
		panel.add(resultPanel, BorderLayout.CENTER);
		resultPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		resultPanel.add(scrollPane);

		rawTextArea = new JTextArea();
		rawTextArea.setEditable(false);
		scrollPane.setViewportView(rawTextArea);

		hexTable = new JTable();

		JPanel blankPanelNorth = new JPanel();
		resultPanel.add(blankPanelNorth, BorderLayout.NORTH);
		GridBagLayout gbl_blankPanelNorth = new GridBagLayout();
		gbl_blankPanelNorth.columnWidths = new int[]{233, 1, 0};
		gbl_blankPanelNorth.rowHeights = new int[]{20, 0};
		gbl_blankPanelNorth.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_blankPanelNorth.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		blankPanelNorth.setLayout(gbl_blankPanelNorth);

		JLabel label = new JLabel("");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.NORTHWEST;
		gbc_label.gridx = 1;
		gbc_label.gridy = 0;
		blankPanelNorth.add(label, gbc_label);

		JPanel blankPanelSouth = new JPanel();
		resultPanel.add(blankPanelSouth, BorderLayout.SOUTH);
		GridBagLayout gbl_blankPanelSouth = new GridBagLayout();
		gbl_blankPanelSouth.columnWidths = new int[]{233, 1, 0};
		gbl_blankPanelSouth.rowHeights = new int[]{20, 0};
		gbl_blankPanelSouth.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_blankPanelSouth.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		blankPanelSouth.setLayout(gbl_blankPanelSouth);

		JLabel lblNewLabel = new JLabel("");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		blankPanelSouth.add(lblNewLabel, gbc_lblNewLabel);

		JPanel buttonPanel = new JPanel();
		panel.add(buttonPanel, BorderLayout.WEST);
		GridBagLayout gbl_buttonPanel = new GridBagLayout();
		gbl_buttonPanel.columnWidths = new int[]{98, 0};
		gbl_buttonPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_buttonPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_buttonPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		buttonPanel.setLayout(gbl_buttonPanel);

		JRadioButton rdbtnRaw = new JRadioButton("Raw");
		rdbtnRaw.addActionListener(e -> {

		});
		rdbtnRaw.setSelected(true);
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rdbtnRaw);
		GridBagConstraints gbc_rdbtnRaw = new GridBagConstraints();
		gbc_rdbtnRaw.fill = GridBagConstraints.VERTICAL;
		gbc_rdbtnRaw.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnRaw.gridx = 0;
		gbc_rdbtnRaw.gridy = 1;
		buttonPanel.add(rdbtnRaw, gbc_rdbtnRaw);

		JRadioButton rdbtnHex = new JRadioButton("Hex");
		rdbtnHex.addActionListener(e -> {
			hexTable = new JTable();
			scrollPane.setViewportView(hexTable);
		});
		buttonGroup.add(rdbtnHex);
		GridBagConstraints gbc_rdbtnHex = new GridBagConstraints();
		gbc_rdbtnHex.fill = GridBagConstraints.VERTICAL;
		gbc_rdbtnHex.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnHex.gridx = 0;
		gbc_rdbtnHex.gridy = 2;
		buttonPanel.add(rdbtnHex, gbc_rdbtnHex);
	}

	void setData() {

	}
}
