package application.view.frame.scanner;

import application.scanner.IssueInfoFile;
import application.scanner.IssuesLoader;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;

class IssueDefinitionPane extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = -1685042169132379280L;

	private JTable definitionTable;
	private JTextPane definitionTextPane;
	private IssuesLoader loader = IssuesLoader.getInstance();

	IssueDefinitionPane() {

		setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.NORTH);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{233, 0};
		gbl_panel_1.rowHeights = new int[]{54, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);

		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 0;
		panel_1.add(panel_2, gbc_panel_2);

		JLabel lblIssueDefinitions = new JLabel("Issue Definitions");
		lblIssueDefinitions.setBounds(17, 16, 160, 19);
		panel_2.add(lblIssueDefinitions);
		lblIssueDefinitions.setForeground(new Color(135, 206, 250));
		lblIssueDefinitions.setFont(new Font("Lucida Grande", Font.BOLD, 15));

		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(600);
		add(splitPane, BorderLayout.CENTER);

		JScrollPane issueScrollPane = new JScrollPane();
		splitPane.setLeftComponent(issueScrollPane);

		definitionTable = new JTable();
		if (loader.getModel() != null) {
			definitionTable.setModel(loader.getModel());
		}
		definitionTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		definitionTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int currentRow = definitionTable.getSelectedRow();
				try {
					IssueInfoFile file = loader.getAtIndex(currentRow);
					definitionTextPane.setText(file.getContent());
				} catch (IndexOutOfBoundsException ignored) {
				} catch (IOException e1) {
					definitionTextPane.setText("File not found");
				}
			}
		});
		issueScrollPane.setViewportView(definitionTable);

		JScrollPane scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);

		definitionTextPane = new JTextPane();
		definitionTextPane.setEditable(false);
		definitionTextPane.setEditorKit(new HTMLEditorKit());
		definitionTextPane.setContentType("text/html");
		definitionTextPane.addHyperlinkListener(e -> {
			if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				JEditorPane pane = (JEditorPane) e.getSource();
				if (e instanceof HTMLFrameHyperlinkEvent) {
					HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent) e;
					HTMLDocument doc = (HTMLDocument) pane.getDocument();
					doc.processHTMLFrameHyperlinkEvent(evt);
				} else {
					try {
						if (Desktop.isDesktopSupported()) {
							try {
								URI uri = e.getURL().toURI();
								Desktop dp = Desktop.getDesktop();
								if (dp.isSupported(Desktop.Action.BROWSE)) {
									dp.browse(uri);
								}
							} catch (Exception ignored) {
							}
						}
					} catch (Throwable ignored) {
					}
				}
			}
		});
		scrollPane.setViewportView(definitionTextPane);

	}

}
