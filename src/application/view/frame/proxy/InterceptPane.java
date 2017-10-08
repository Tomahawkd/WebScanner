package application.view.frame.proxy;

import javax.swing.*;
import java.awt.*;

class InterceptPane extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 8347363825425530885L;
	private JTable paramTable;

	InterceptPane() {

		setLayout(new BorderLayout(0, 0));

		JPanel controlPanel = new JPanel();
		add(controlPanel, BorderLayout.NORTH);
		controlPanel.setLayout(new BorderLayout(0, 0));

		/*
		 * Label Pane
		 */

		{
			JPanel labelPanel = new JPanel();
			controlPanel.add(labelPanel, BorderLayout.NORTH);
			GridBagLayout gbl_labelPanel = new GridBagLayout();
			gbl_labelPanel.columnWidths = new int[]{100, 0};
			gbl_labelPanel.rowHeights = new int[]{30, 0};
			gbl_labelPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
			gbl_labelPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			labelPanel.setLayout(gbl_labelPanel);

			JLabel lblIntercepter = new JLabel("Intercepter");
			lblIntercepter.setForeground(new Color(135, 206, 235));
			lblIntercepter.setFont(new Font("Lucida Grande", Font.BOLD, 15));
			GridBagConstraints gbc_lblIntercepter = new GridBagConstraints();
			gbc_lblIntercepter.anchor = GridBagConstraints.SOUTHEAST;
			gbc_lblIntercepter.gridx = 0;
			gbc_lblIntercepter.gridy = 0;
			labelPanel.add(lblIntercepter, gbc_lblIntercepter);
		}

		/*
		 * Button Pane
		 */

		{
			JPanel buttonPanel = new JPanel();
			controlPanel.add(buttonPanel, BorderLayout.WEST);

			JButton btnForward = new JButton("Forward");
			buttonPanel.add(btnForward);

			JButton btnDrop = new JButton("Drop");
			buttonPanel.add(btnDrop);

			JToggleButton tglbtnIntercept = new JToggleButton("Intercept Off");
			tglbtnIntercept.addActionListener(e -> {
				if (tglbtnIntercept.getText().equals("Intercept Off")) {
					tglbtnIntercept.setText("Intercept On");
					//TODO action
				} else {
					tglbtnIntercept.setText("Intercept Off");
					//TODO action
				}

			});
			buttonPanel.add(tglbtnIntercept);

			JButton btnAction = new JButton("Action");
			buttonPanel.add(btnAction);
		}

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane);

		JTextArea interceptTextArea = new JTextArea();
		tabbedPane.addTab("Raw", null, interceptTextArea, null);


		paramTable = new JTable();
		tabbedPane.addTab("Params", null, paramTable, null);
	}
}
