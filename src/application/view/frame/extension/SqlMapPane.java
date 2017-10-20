package application.view.frame.extension;

import application.extension.sqlmap.CommandLineListener;
import application.extension.sqlmap.SqlmapHandler;

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

		Runnable execRunnable = () -> {
			CommandLineListener.getInstance().setExitValue(SqlmapHandler.getController().exec());
			resultTextArea.append("\nexit: " + CommandLineListener.getInstance().getExitValue() + "\n");
			resultTextArea.append("> python sqlmap ");
		};

		JButton btnEnter = new JButton("Enter");
		btnEnter.addActionListener(e -> {
			String[] lines = resultTextArea.getText().split("\n");
			String lastLine = lines[lines.length -1];
			if (lastLine.equals("> python sqlmap ")) {
				if (!resultTextArea.getText().equals("> python sqlmap ")) {
					resultTextArea.append("> python sqlmap ");
				}
				SqlmapHandler.getController().setCommand(commandTextField.getText().split(" "));
				resultTextArea.append(commandTextField.getText());
				new Thread(execRunnable, "Sqlmap Main Thread").start();
			} else {
				CommandLineListener.getInstance().setCommand(commandTextField.getText());
				resultTextArea.append(CommandLineListener.getInstance().getCommand());
			}
			resultTextArea.append("\n");
			commandTextField.setText("");
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


		//Center panel
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		resultTextArea = new JTextArea();
		resultTextArea.setText("> python sqlmap ");
		resultTextArea.setEditable(false);
		resultTextArea.setLineWrap(true);
		SqlmapHandler.getController().setOutput(resultTextArea);
		scrollPane.setViewportView(resultTextArea);
	}
}
