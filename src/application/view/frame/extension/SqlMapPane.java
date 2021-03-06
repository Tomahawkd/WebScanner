package application.view.frame.extension;

import application.extension.sqlmap.CommandLineListener;
import application.extension.sqlmap.SqlmapLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class SqlMapPane extends JPanel {

	private JTextArea resultTextArea;

	SqlMapPane() {
		setLayout(new BorderLayout(0, 0));

		//North panel
		JPanel labelPanel = new JPanel();
		FlowLayout fl_labelPanel = (FlowLayout) labelPanel.getLayout();
		fl_labelPanel.setHgap(20);
		fl_labelPanel.setVgap(10);
		fl_labelPanel.setAlignment(FlowLayout.LEFT);
		add(labelPanel, BorderLayout.NORTH);

		JLabel lblSqlMap = new JLabel("SQL Map");
		lblSqlMap.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		lblSqlMap.setForeground(new Color(135, 206, 235));
		labelPanel.add(lblSqlMap);

		//Two sides blank panel
		JPanel blankPanel1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) blankPanel1.getLayout();
		flowLayout_1.setHgap(15);
		add(blankPanel1, BorderLayout.WEST);

		JPanel blankPanel2 = new JPanel();
		FlowLayout fl_blankPanel2 = (FlowLayout) blankPanel2.getLayout();
		fl_blankPanel2.setHgap(15);
		add(blankPanel2, BorderLayout.EAST);

		//South panel
		JPanel commandPanel = new JPanel();
		FlowLayout fl_commandPanel = (FlowLayout) commandPanel.getLayout();
		fl_commandPanel.setHgap(20);
		fl_commandPanel.setAlignment(FlowLayout.LEFT);
		add(commandPanel, BorderLayout.SOUTH);

		JTextField commandTextField = new JTextField();
		commandTextField.setColumns(75);

		JButton btnEnter = new JButton("Enter");
		btnEnter.addActionListener(e -> {
			if (SqlmapLoader.isLoaded()) {
				String[] lines = resultTextArea.getText().split("\n");
				String lastLine = lines[lines.length - 1];
				if (lastLine.equals("> python sqlmap ")) {
					if (!resultTextArea.getText().equals("> python sqlmap ")) {
						resultTextArea.append("> python sqlmap ");
					}
					SqlmapLoader.getController().setCommand(commandTextField.getText().split(" "));
					resultTextArea.append(commandTextField.getText());
					new Thread(() -> {
						CommandLineListener.getInstance().setExitValue(SqlmapLoader.getController().exec());
						resultTextArea.append("\nexit: " + CommandLineListener.getInstance().getExitValue() + "\n");
						resultTextArea.append("> python sqlmap ");
					}, "Sqlmap Main Thread").start();
				} else {
					CommandLineListener.getInstance().setCommand(commandTextField.getText());
					resultTextArea.append(CommandLineListener.getInstance().getCommand());
				}
				resultTextArea.append("\n");
				commandTextField.setText("");
			}
		});
		commandTextField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getExtendedKeyCode() == KeyEvent.VK_ENTER) {
					btnEnter.doClick();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
		commandPanel.add(commandTextField);

		// add it after text field
		commandPanel.add(btnEnter);

		JButton btnsetPos = new JButton("Set Executor");
		btnsetPos.addActionListener(e -> {
			String path =
					JOptionPane.showInputDialog(null,
							"Please enter sqlmap directory",
							"Set Executor", JOptionPane.PLAIN_MESSAGE);
			if (path != null) {
				try {
					SqlmapLoader.loadExecutor(path);
					resultTextArea.setText("> python sqlmap ");
					SqlmapLoader.getController().setOutput(resultTextArea);
				} catch (IllegalArgumentException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(),
							"Executor path mismatch", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		commandPanel.add(btnsetPos);

		//Center panel
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		resultTextArea = new JTextArea();
		if (SqlmapLoader.isLoaded()) {
			resultTextArea.setText("> python sqlmap ");
			SqlmapLoader.getController().setOutput(resultTextArea);
		}
		resultTextArea.setEditable(false);
		resultTextArea.setLineWrap(true);
		scrollPane.setViewportView(resultTextArea);
	}
}
