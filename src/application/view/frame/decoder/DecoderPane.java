package application.view.frame.decoder;

import application.decoder.DataHandler;
import application.utility.coding.HashType;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Objects;

class DecoderPane extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = -4724973810412405433L;
	private JTextArea rawTextArea;
	private JTable hexTable;

	DecoderPane() {

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
		rawTextArea.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				DataHandler.getInstance().setData(rawTextArea.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				DataHandler.getInstance().setData(rawTextArea.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				DataHandler.getInstance().setData(rawTextArea.getText());
			}
		});

		hexTable = new JTable();
		hexTable.setModel(DataHandler.getInstance().getHexData());

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
			rawTextArea.setText(DataHandler.getInstance().getData());
			scrollPane.setViewportView(rawTextArea);
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
		rdbtnHex.addActionListener(e -> scrollPane.setViewportView(hexTable));
		buttonGroup.add(rdbtnHex);
		GridBagConstraints gbc_rdbtnHex = new GridBagConstraints();
		gbc_rdbtnHex.fill = GridBagConstraints.VERTICAL;
		gbc_rdbtnHex.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnHex.gridx = 0;
		gbc_rdbtnHex.gridy = 2;
		buttonPanel.add(rdbtnHex, gbc_rdbtnHex);

		JComboBox<String> decodeComboBox = new JComboBox<>();
		decodeComboBox.setModel(new DefaultComboBoxModel<>(
				new String[]{"Decode as…",
						"Plain",
						"URL",
						"BASE64",
						"Gzip"}));
		decodeComboBox.addActionListener(e -> {
			try {
				String algorithm = (String) decodeComboBox.getSelectedItem();
				if (!Objects.equals(algorithm, "Decode as…")) {

				}
			}catch (ClassCastException ignored){}
		});
		GridBagConstraints gbc_decodeComboBox = new GridBagConstraints();
		gbc_decodeComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_decodeComboBox.fill = GridBagConstraints.BOTH;
		gbc_decodeComboBox.gridx = 0;
		gbc_decodeComboBox.gridy = 3;
		buttonPanel.add(decodeComboBox, gbc_decodeComboBox);

		JComboBox<String> encodeComboBox = new JComboBox<>();
		encodeComboBox.setModel(new DefaultComboBoxModel<>(
				new String[]{"Encode as…",
						"Plain",
						"URL",
						"BASE64",
						"Gzip"}));
		encodeComboBox.addActionListener(e -> {
			try {
				String algorithm = (String) encodeComboBox.getSelectedItem();
				if (!Objects.equals(algorithm, "Encode as…")) {

				}
			}catch (ClassCastException ignored){}
		});
		GridBagConstraints gbc_encodeComboBox = new GridBagConstraints();
		gbc_encodeComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_encodeComboBox.fill = GridBagConstraints.BOTH;
		gbc_encodeComboBox.gridx = 0;
		gbc_encodeComboBox.gridy = 4;
		buttonPanel.add(encodeComboBox, gbc_encodeComboBox);

		JComboBox<HashType> hashComboBox = new JComboBox<>();
		hashComboBox.setModel(new DefaultComboBoxModel<>(HashType.values()));
		hashComboBox.addActionListener(e -> {
			try {
				HashType algorithm = (HashType) hashComboBox.getSelectedItem();
				if (algorithm != null && !Objects.equals(algorithm.toString(), "Hash…")) {

				}
			}catch (ClassCastException ignored){}
		});
		GridBagConstraints gbc_hashComboBox = new GridBagConstraints();
		gbc_hashComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_hashComboBox.fill = GridBagConstraints.BOTH;
		gbc_hashComboBox.gridx = 0;
		gbc_hashComboBox.gridy = 5;
		buttonPanel.add(hashComboBox, gbc_hashComboBox);

	}
}
