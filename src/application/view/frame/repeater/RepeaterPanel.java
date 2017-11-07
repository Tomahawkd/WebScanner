package application.view.frame.repeater;

import application.alertHandler.AlertHandler;
import application.repeater.RepeaterData;
import application.utility.net.CoreData;
import application.utility.net.Exceptions.IllegalHeaderDataException;
import application.utility.parser.html.HTMLParser;
import application.utility.parser.json.JSONParser;
import application.utility.parser.xml.XMLParser;
import application.utility.table.TableModelGenerator;
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
	private JTable requestParamTable;
	private JTextArea requestTextArea;
	private JPanel responsePanel;
	private Viewer responseViewer;
	private Thread connectionThread;

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

		JTabbedPane requestTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		requestTabbedPane.addChangeListener(e -> {

			if (requestTextArea != null) {
				JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
				int selectedIndex = tabbedPane.getSelectedIndex();
				switch (selectedIndex) {
					case 0:
						try {
							requestTextArea.setText(RepeaterData.getInstance().getContext()
									.getRequestForm().replace("\r\n", "\n"));
						} catch (IllegalArgumentException ignored) {
						} catch (IllegalHeaderDataException e1) {
							JOptionPane.showMessageDialog(null,
									e1.getMessage() == null ?
											e1.getMessage() : "The request data is invalid",
									"Data Invalid",
									JOptionPane.WARNING_MESSAGE);
						}
						break;

					case 1:
						try {
							RepeaterData.getInstance().setRequest(requestTextArea.getText());
							requestParamTable.updateUI();
						} catch (IndexOutOfBoundsException | IllegalHeaderDataException e1) {
							JOptionPane.showMessageDialog(null,
									e1.getMessage() == null ?
											e1.getMessage() : "The request data is invalid",
									"Data Invalid",
									JOptionPane.WARNING_MESSAGE);
							tabbedPane.setSelectedIndex(0);
						}
						break;

					default:
						break;
				}
			}
		});
		requestPanel.add(requestTabbedPane, BorderLayout.CENTER);

		JScrollPane requestRawScrollPane = new JScrollPane();
		requestTabbedPane.addTab("Raw", requestRawScrollPane);

		requestTextArea = new JTextArea();
		requestRawScrollPane.setViewportView(requestTextArea);

		JScrollPane requestParamScrollPane = new JScrollPane();
		requestTabbedPane.addTab("Param", null, requestParamScrollPane, null);

		requestParamTable = new JTable();
		requestParamTable.setModel(TableModelGenerator.getInstance()
				.generateHeaderModel(RepeaterData.getInstance().getContext().getRequestHeader()));
		requestParamTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		requestParamScrollPane.setViewportView(requestParamTable);

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
		responseViewer.setTableModel(TableModelGenerator.getInstance()
				.generateHeaderModel(RepeaterData.getInstance().getContext().getResponseHeader()));
		responseViewer.addChangeListener(e -> {

			JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
			int selectedIndex = tabbedPane.getSelectedIndex();
			switch (selectedIndex) {
				case 1:
					responseViewer.updateViewerData();
					break;

				default:
					break;
			}

		});
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

			if (RepeaterData.getInstance().getContext().getURL() == null) {

				btnSend.setEnabled(true);
				btnCancel.setEnabled(false);

				JOptionPane.showMessageDialog(
						null,
						"Please set target URL",
						"Target",
						JOptionPane.INFORMATION_MESSAGE);

			} else {
				connectionThread = new Thread(getTarget());
				connectionThread.start();
			}
		});
		targetPanel.add(btnSend);

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(e -> {
			connectionThread.interrupt();

			btnSend.setEnabled(true);
			btnCancel.setEnabled(false);
		});
		btnCancel.setEnabled(false);
		targetPanel.add(btnCancel);

		JButton btnGenerate = new JButton("Generate");
		btnGenerate.addActionListener(e -> {
			if (RepeaterData.getInstance().getContext().getURL() == null) {
				JOptionPane.showMessageDialog(
						null,
						"Please set target URL",
						"Target",
						JOptionPane.INFORMATION_MESSAGE);
			} else {

				String httpData = "GET / HTTP/1.1\n" +
						"Host: " + RepeaterData.getInstance().getContext().getURL().getHost() + "\n" +
						"Connection: close\n" +
						"\n";

				requestTextArea.setText(httpData);
			}
		});
		targetPanel.add(btnGenerate);
	}

	void updateTargetLabel(String urlStr) {
		lblTarget.setText(urlStr);
	}

	void setURL(URL url) {
		RepeaterData.getInstance().setURL(url);
	}

	private Runnable getTarget() {
		return () -> {
			try {
				RepeaterData.getInstance().setRequest(requestTextArea.getText());
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
					CoreData handler = RepeaterData.getInstance();
					String type = handler.getContext().getMINEType();
					if (type.contains("text/html")) {
						Document doc = HTMLParser.getParser()
								.parse(handler.getContext().getResponseData(), "");
						responseViewer = ViewerFactory.getInstance().createViewer(ViewerFactory.RESPONSE_HTML);
						((HTMLViewer) responseViewer)
								.setHTML(handler.getContext().getResponseData(),
										RepeaterData.getInstance().getContext().getURL());
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
					responseViewer.setTableModel(TableModelGenerator.getInstance()
							.generateHeaderModel(handler.getContext().getResponseHeader()));
					responseViewer.setText(handler.getContext().getResponseForm()
							.replace("\r\n", "\n"));
					responsePanel.remove(oldViewer);
					responsePanel.add(responseViewer, BorderLayout.CENTER);
					updateUI();
				} catch (JSONException e) {
					responseViewer = ViewerFactory.getInstance().createViewer(ViewerFactory.DEFAULT_RESPONSE);
					responseViewer.setTableModel(TableModelGenerator.getInstance()
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
			} catch (IllegalHeaderDataException | IndexOutOfBoundsException e) {
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
