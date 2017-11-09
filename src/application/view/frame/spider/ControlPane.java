package application.view.frame.spider;

import application.spider.SpiderHandler;

import javax.swing.*;
import java.awt.*;

class ControlPane extends JScrollPane {

	/**
	 *
	 */
	private static final long serialVersionUID = 2533591031210694796L;
	private JLabel requestQueueCounter;
	private JLabel requestMadeCounter;

	ControlPane() {


		JPanel contentPane = new JPanel();
		contentPane.setPreferredSize(new Dimension(350, 200));
		contentPane.setLayout(null);
		setViewportView(contentPane);

		JLabel lblSpiderStatus = new JLabel("Spider Status");
		lblSpiderStatus.setBounds(10, 6, 111, 30);
		lblSpiderStatus.setForeground(new Color(135, 206, 250));
		lblSpiderStatus.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		contentPane.add(lblSpiderStatus);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(10, 32, 275, 39);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		contentPane.add(buttonPanel);

		JToggleButton tglbtnSpiderControl = new JToggleButton("Spider Paused");
		tglbtnSpiderControl.addActionListener(e -> {
			if (tglbtnSpiderControl.getText().equals("Spider Paused")) {
				tglbtnSpiderControl.setText("Spider Running");

				new Thread(() -> {
					if (SpiderHandler.getInstance().isFirstExecution()) {
						SpiderHandler.getInstance().start();

					} else {
						SpiderHandler.getInstance().resume();
					}
				}, "Spider Main Control Thread").start();
			} else {
				tglbtnSpiderControl.setText("Spider Paused");
				SpiderHandler.getInstance().suspend();
			}

		});
		buttonPanel.add(tglbtnSpiderControl);

		JButton btnClear = new JButton("Clear Queue");
		btnClear.addActionListener(e -> SpiderHandler.getInstance().clearQueue());
		buttonPanel.add(btnClear);

		JLabel lblRequestMade = new JLabel("Requests made:");
		lblRequestMade.setBounds(24, 83, 121, 16);
		contentPane.add(lblRequestMade);

		JLabel lblRequestsQueued = new JLabel("Requests queued:");
		lblRequestsQueued.setBounds(24, 111, 121, 16);
		contentPane.add(lblRequestsQueued);

		requestMadeCounter = new JLabel("0");
		requestMadeCounter.setBounds(147, 83, 72, 16);
		contentPane.add(requestMadeCounter);

		requestQueueCounter = new JLabel("0");
		requestQueueCounter.setBounds(147, 111, 72, 16);
		contentPane.add(requestQueueCounter);
	}

	void updateRequestCounter(int requestCount) {
		requestMadeCounter.setText(String.valueOf(requestCount));
	}

	void updateQueueCounter(int queueCount) {
		requestQueueCounter.setText(String.valueOf(queueCount));
	}
}
