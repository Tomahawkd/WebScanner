package application.view.frame.scanner;

import javax.swing.*;
import java.awt.*;

class LiveScanPane extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 4610963227362883597L;

	private final ButtonGroup buttonGroup = new ButtonGroup();


	LiveScanPane() {

		setLayout(null);

		JLabel lblPassiveLiveScanning = new JLabel("Passive Live Scanning");
		lblPassiveLiveScanning.setForeground(new Color(135, 206, 250));
		lblPassiveLiveScanning.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		lblPassiveLiveScanning.setBounds(16, 16, 170, 16);
		add(lblPassiveLiveScanning);

		JRadioButton rdbtnDoNotScan = new JRadioButton("Do not scan");
		buttonGroup.add(rdbtnDoNotScan);
		rdbtnDoNotScan.setBounds(26, 44, 141, 23);
		add(rdbtnDoNotScan);

		JRadioButton rdbtnScanEverything = new JRadioButton("Scan everything");
		buttonGroup.add(rdbtnScanEverything);
		rdbtnScanEverything.setSelected(true);
		rdbtnScanEverything.setBounds(26, 79, 141, 23);
		add(rdbtnScanEverything);

		JRadioButton rdbtnScanTargetScope = new JRadioButton("Scan target scope");
		buttonGroup.add(rdbtnScanTargetScope);
		rdbtnScanTargetScope.setBounds(26, 114, 177, 23);
		add(rdbtnScanTargetScope);

	}
}
