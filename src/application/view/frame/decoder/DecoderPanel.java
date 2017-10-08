package application.view.frame.decoder;

import javax.swing.*;

class DecoderPanel extends JScrollPane {

	/**
	 *
	 */
	private static final long serialVersionUID = -2479243466821207299L;

	DecoderPanel() {

		JPanel mainPanel = new JPanel();
		setViewportView(mainPanel);
		mainPanel.setLayout(null);

		DecoderPane decoderPane = new DecoderPane();
		decoderPane.setBounds(20, 20, 1000, 200);
		mainPanel.add(decoderPane);

		ResultPane resultPane = new ResultPane();
		resultPane.setBounds(20, 240, 1000, 200);
		mainPanel.add(resultPane);
	}
}
