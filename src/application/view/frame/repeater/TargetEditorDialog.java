package application.view.frame.repeater;

import application.repeater.RepeaterData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

class TargetEditorDialog extends JDialog {

	/**
	 *
	 */
	private static final long serialVersionUID = -2765193025540071063L;


	TargetEditorDialog() {
		setBounds(450, 200, 320, 200);
		getContentPane().setLayout(new BorderLayout());
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblHost = new JLabel("Host:");
		lblHost.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblHost.setBounds(40, 33, 44, 19);
		contentPanel.add(lblHost);

		JTextField hostTextField = new JTextField();
		hostTextField.setBounds(96, 30, 175, 26);
		hostTextField.setColumns(10);
		contentPanel.add(hostTextField);

		JTextField portTextField = new JTextField();
		portTextField.setColumns(10);
		portTextField.setBounds(96, 68, 175, 26);
		contentPanel.add(portTextField);

		JLabel lblPort = new JLabel("Port:");
		lblPort.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
		lblPort.setBounds(40, 73, 44, 19);
		contentPanel.add(lblPort);

		JCheckBox chckbxUseHttps = new JCheckBox("Use HTTPS");
		chckbxUseHttps.setBounds(40, 104, 128, 23);
		contentPanel.add(chckbxUseHttps);

		if (RepeaterData.getInstance().getURL() != null) {
			hostTextField.setText(RepeaterData.getInstance().getURL().getHost());
			portTextField.setText("" + RepeaterData.getInstance().getURL().getPort());
			chckbxUseHttps.setSelected(RepeaterData.getInstance().getURL().getProtocol().equals("https"));
		}

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(e -> {

					int port;
					try {
						port = Integer.parseInt(portTextField.getText());
						if (port < 0 || port > 0xFFFF)
							throw new IllegalArgumentException("port out of range:" + port);


						String urlStr = (chckbxUseHttps.isSelected() ? "https://" : "http://") +
								hostTextField.getText() + ":" + port + "/";

						URL url = new URL(urlStr);
						RepeaterPanelController.getInstance().setURL(url);
						String urlLabel = url.getProtocol() + "://" + url.getHost() + "/";
						RepeaterPanelController.getInstance().updateTargetLabel(urlLabel);

						dispose();

					} catch (IllegalArgumentException e1) {

						portTextField.setText("");

						JOptionPane.showMessageDialog(
								null,
								"Port invalid",
								"URL Error",
								JOptionPane.ERROR_MESSAGE);

					} catch (MalformedURLException e1) {

						hostTextField.setText("");

						JOptionPane.showMessageDialog(
								null,
								"Target URL invalid",
								"URL Error",
								JOptionPane.ERROR_MESSAGE);
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}

			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(e -> dispose());
				buttonPane.add(cancelButton);
			}
		}
	}
}
