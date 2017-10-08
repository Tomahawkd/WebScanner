package application.view.frame.intruder;

import javax.swing.*;
import java.awt.*;

class TargetPane extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 7574324720405985180L;

	private JTextField hostTextField;
	private JTextField portTextField;

	TargetPane() {
		setLayout(null);

		JLabel lblAttackTarget = new JLabel("Attack Target");
		lblAttackTarget.setForeground(new Color(135, 206, 250));
		lblAttackTarget.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		lblAttackTarget.setBounds(23, 21, 105, 19);
		add(lblAttackTarget);

		JLabel lblHost = new JLabel("Host:");
		lblHost.setBounds(33, 55, 61, 16);
		add(lblHost);

		hostTextField = new JTextField();
		hostTextField.setText("127.0.0.1");
		hostTextField.setBounds(106, 52, 225, 26);
		add(hostTextField);
		hostTextField.setColumns(10);

		JLabel lblPort = new JLabel("Port:");
		lblPort.setBounds(33, 86, 61, 16);
		add(lblPort);

		portTextField = new JTextField();
		portTextField.setText("80");
		portTextField.setBounds(106, 81, 225, 26);
		add(portTextField);
		portTextField.setColumns(10);

		JCheckBox chckbxUseHttps = new JCheckBox("Use HTTPS");
		chckbxUseHttps.setBounds(23, 114, 128, 23);
		add(chckbxUseHttps);

		JButton btnStartAttack = new JButton("Start Attack");
		btnStartAttack.setBounds(23, 149, 117, 29);
		add(btnStartAttack);

	}
}
