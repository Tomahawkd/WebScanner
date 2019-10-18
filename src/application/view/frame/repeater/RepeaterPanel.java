package application.view.frame.repeater;

import application.alertHandler.AlertHandler;
import application.repeater.RepeaterData;
import application.utility.net.Exceptions.IllegalHeaderDataException;
import application.utility.net.Form;
import application.utility.parser.html.HTMLParser;
import application.utility.parser.json.JSONParser;
import application.utility.parser.xml.XMLParser;
import application.utility.table.TableModelGenerator;
import application.utility.thread.TaskControl;
import application.view.contentViewer.ExtendedViewer;
import application.view.contentViewer.HTMLViewer;
import application.view.contentViewer.Viewer;
import application.view.contentViewer.ViewerFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

class RepeaterPanel extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 2040228521102491819L;
	private JButton btnSend;
	private JButton btnCancel;
	private JLabel lblTarget;
	private Viewer requestViewer;
	private JPanel responsePanel;
	private Viewer responseViewer;
	private String connectionThreadToken;

	RepeaterPanel() {
		setLayout(new BorderLayout(0, 0));

		JSplitPane mainSplitPane = new JSplitPane();
		add(mainSplitPane);

		JPanel requestPanel = new JPanel();
		mainSplitPane.setLeftComponent(requestPanel);
		mainSplitPane.setDividerLocation(600);
		requestPanel.setLayout(new BorderLayout(0, 0));

		JPanel requestLabelPanel = new JPanel();
		requestPanel.add(requestLabelPanel, BorderLayout.NORTH);

		JLabel lblRequest = new JLabel("Request");
		lblRequest.setForeground(new Color(135, 206, 250));
		lblRequest.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		requestLabelPanel.add(lblRequest);

		requestViewer = ViewerFactory.getInstance().createViewer(ViewerFactory.REQUEST);
		requestViewer.setHeaderTableModel(TableModelGenerator.getInstance()
				.generateHeaderModel(RepeaterData.getInstance().getContext().getRequestHeader()));
		requestViewer.setParamTableModel(TableModelGenerator.getInstance()
				.generateParamModel(RepeaterData.getInstance().getContext().getParams()));
		requestViewer.addChangeListener(e -> {
			try {
				if (requestViewer.getSelectedIndex() != 0) {
					RepeaterData.getInstance().setRequestForm(requestViewer.getText());
				}
				requestViewer.setText(RepeaterData.getInstance().getContext()
						.getRequestForm().replace("\r\n", "\n"));
				requestViewer.updateViewerData();
			} catch (IllegalHeaderDataException e1) {
				JOptionPane.showMessageDialog(null,
						e1.getMessage() == null ?
								e1.getMessage() : "The request data is invalid",
						"Data Invalid",
						JOptionPane.WARNING_MESSAGE);
				((JTabbedPane) e.getSource()).setSelectedIndex(0);
			}
		});
		requestPanel.add(requestViewer, BorderLayout.CENTER);

		responsePanel = new JPanel();
		mainSplitPane.setRightComponent(responsePanel);
		responsePanel.setLayout(new BorderLayout(0, 0));

		JPanel responseLabelPanel = new JPanel();
		responsePanel.add(responseLabelPanel, BorderLayout.NORTH);

		JLabel lblResponse = new JLabel("Response");
		lblResponse.setForeground(new Color(135, 206, 250));
		lblResponse.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		responseLabelPanel.add(lblResponse);

		responseViewer = ViewerFactory.getInstance().createViewer();
		responseViewer.setHeaderTableModel(TableModelGenerator.getInstance()
				.generateHeaderModel(RepeaterData.getInstance().getContext().getResponseHeader()));
		responseViewer.addChangeListener(e -> responseViewer.updateViewerData());
		responsePanel.add(responseViewer, BorderLayout.CENTER);

		JPanel targetPanel = new JPanel();
		add(targetPanel, BorderLayout.NORTH);

		JLabel label = new JLabel("Target:");
		targetPanel.add(label);

		lblTarget = new JLabel("Not set");
		targetPanel.add(lblTarget);

		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(e -> {
			TargetEditorDialog dialog = new TargetEditorDialog();
			dialog.setVisible(true);
		});
		targetPanel.add(btnEdit);

		btnSend = new JButton("Send");
		btnSend.addActionListener(e -> {

			btnCancel.setEnabled(true);
			btnSend.setEnabled(false);

			if (RepeaterData.getInstance().getContext().getHostURL() == null) {

				btnSend.setEnabled(true);
				btnCancel.setEnabled(false);

				JOptionPane.showMessageDialog(
						null,
						"Please set target URL",
						"Target",
						JOptionPane.INFORMATION_MESSAGE);

			} else {
				connectionThreadToken =
						TaskControl.getInstance().getController().addControllableTask(getTarget());
			}
		});
		targetPanel.add(btnSend);

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(e -> {
			TaskControl.getInstance().getController().stopControllableTask(connectionThreadToken);

			btnSend.setEnabled(true);
			btnCancel.setEnabled(false);
		});
		btnCancel.setEnabled(false);
		targetPanel.add(btnCancel);

		JButton btnGenerate = new JButton("Generate");
		btnGenerate.addActionListener(e -> {
			if (RepeaterData.getInstance().getContext().getHostURL() == null) {
				JOptionPane.showMessageDialog(
						null,
						"Please set target URL",
						"Target",
						JOptionPane.INFORMATION_MESSAGE);
			} else {

				String httpData = "GET / HTTP/1.1\n" +
						"Host: " + RepeaterData.getInstance().getContext().getHostURL().getHost() + "\n" +
						"Connection: close\n" +
						"\n";

				requestViewer.setText(httpData);
			}
		});
		targetPanel.add(btnGenerate);
	}

	void updateTargetLabel(String urlStr) {
		lblTarget.setText(urlStr);
	}

	void setURL(URL url) {
		RepeaterData.getInstance().setHostURL(url);
	}

	private Runnable getTarget() {
		return () -> {
			try {
				RepeaterData.getInstance().setRequestForm(requestViewer.getText());
				Viewer oldViewer = responseViewer;
				try {
					try {
						RepeaterData.getInstance().connect();
					} catch (IllegalHeaderDataException e) {
						AlertHandler.getInstance()
								.addAlert("Repeater",
										e.getClass().getName() + ": "
												+ (e.getMessage() == null ? "" : e.getMessage()));
					}
					Form handler = RepeaterData.getInstance();
					String type = handler.getContext().getMIMEType();
					if (type.contains("text/html")) {
						Document doc = HTMLParser.getParser()
								.parse(handler.getContext().getResponseData(), "");
						responseViewer = ViewerFactory.getInstance().createViewer(ViewerFactory.RESPONSE_HTML);
						((HTMLViewer) responseViewer)
								.setHTML(handler.getContext().getResponseData(),
										RepeaterData.getInstance().getContext().getHostURL());
						((HTMLViewer) responseViewer).setTreeNode(HTMLParser.getParser().getTreeNode(doc));
					} else if (type.contains("xml")) {
						JSONObject xml = XMLParser.getParser().parse(handler.getContext().getResponseData());
						responseViewer = ViewerFactory.getInstance().createViewer(ViewerFactory.RESPONSE_XML);
						((ExtendedViewer) responseViewer).setTreeNode(JSONParser.getParser().getTreeNode(xml));
					} else if (type.contains("json")) {
						JSONObject json = JSONParser.getParser().parse(handler.getContext().getResponseData());
						responseViewer = ViewerFactory.getInstance().createViewer(ViewerFactory.RESPONSE_JSON);
						((ExtendedViewer) responseViewer).setTreeNode(JSONParser.getParser().getTreeNode(json));
					} else {
						responseViewer = ViewerFactory.getInstance().createViewer(ViewerFactory.DEFAULT_RESPONSE);
					}
					responseViewer.setHeaderTableModel(TableModelGenerator.getInstance()
							.generateHeaderModel(handler.getContext().getResponseHeader()));
					responseViewer.setText(handler.getContext().getResponseForm()
							.replace("\r\n", "\n"));
					responsePanel.remove(oldViewer);
					responsePanel.add(responseViewer, BorderLayout.CENTER);
					updateUI();
				} catch (JSONException e) {
					responseViewer = ViewerFactory.getInstance().createViewer(ViewerFactory.DEFAULT_RESPONSE);
					responseViewer.setHeaderTableModel(TableModelGenerator.getInstance()
							.generateHeaderModel(RepeaterData.getInstance().getContext().getResponseHeader()));
					responseViewer.setText(RepeaterData.getInstance().getContext()
							.getResponseForm().replace("\r\n", "\n"));
					responsePanel.remove(oldViewer);
					responsePanel.add(responseViewer, BorderLayout.CENTER);
					updateUI();
				} catch (IOException | IndexOutOfBoundsException e) {
					AlertHandler.getInstance()
							.addAlert("Repeater",
									e.getClass().getName() + ": "
											+ (e.getMessage() == null ? "" : e.getMessage()));
				}
			} catch (IllegalHeaderDataException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,
						e.getMessage() == null ?
								e.getMessage() : "The request data is invalid",
						"Data Invalid",
						JOptionPane.WARNING_MESSAGE);
			} finally {
				btnSend.setEnabled(true);
				btnCancel.setEnabled(false);
			}
		};
	}
}
