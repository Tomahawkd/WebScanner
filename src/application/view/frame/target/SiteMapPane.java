package application.view.frame.target;

import application.target.DataNode;
import application.target.TargetContext;
import application.utility.table.TableModelGenerator;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class SiteMapPane extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = -3354363114279415492L;

	private JTree siteMapTree;
	private JTable contentTable;
	private JTextPane issuePane;

	SiteMapPane() {

		setLayout(new BorderLayout(0, 0));

		/*
		 * Filter
		 */

		{
			JPanel filterPanel = new JPanel();
			add(filterPanel, BorderLayout.NORTH);
			GridBagLayout gbl_filterPanel = new GridBagLayout();
			gbl_filterPanel.columnWidths = new int[]{1280, 0};
			gbl_filterPanel.rowHeights = new int[]{30, 0};
			gbl_filterPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
			gbl_filterPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			filterPanel.setLayout(gbl_filterPanel);

			//TODO Display Filter when clicked
			JLabel lblFilter = new JLabel("Filter:");
			lblFilter.setBackground(Color.WHITE);
			lblFilter.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
			lblFilter.setOpaque(true);
			GridBagConstraints gbc_lblFilter = new GridBagConstraints();
			gbc_lblFilter.fill = GridBagConstraints.BOTH;
			gbc_lblFilter.gridx = 0;
			gbc_lblFilter.gridy = 0;
			filterPanel.add(lblFilter, gbc_lblFilter);
		}

		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(200);
		add(splitPane, BorderLayout.CENTER);

		{
			siteMapTree = new JTree(new DefaultTreeModel(TargetContext.getInstance().toTree()));
			siteMapTree.setEditable(false);
			siteMapTree.setRootVisible(false);
			siteMapTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			siteMapTree.addTreeSelectionListener(tsl -> {
				Object node = siteMapTree.getLastSelectedPathComponent();
				if (node != null && node instanceof DataNode) {
					TableModelGenerator.getInstance()
							.generateContentModel(TargetContext.getInstance().toTree());
					contentTable.updateUI();
				}
			});

			JScrollPane treeScrollPane = new JScrollPane();
			treeScrollPane.setViewportView(siteMapTree);
			splitPane.setLeftComponent(treeScrollPane);

			JSplitPane chileSplitPane = new JSplitPane();
			chileSplitPane.setDividerLocation(500);
			splitPane.setRightComponent(chileSplitPane);

			{
				JPanel contentPanel = new JPanel();
				chileSplitPane.setLeftComponent(contentPanel);
				contentPanel.setLayout(new BorderLayout(0, 0));

				/*
				 * Content
				 */

				{
					JPanel labelPanel = new JPanel();
					GridBagLayout gbl_labelPanel = new GridBagLayout();
					gbl_labelPanel.columnWidths = new int[]{72, 0};
					gbl_labelPanel.rowHeights = new int[]{29, 0};
					gbl_labelPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
					gbl_labelPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
					labelPanel.setLayout(gbl_labelPanel);
					contentPanel.add(labelPanel, BorderLayout.NORTH);

					JLabel lblContents = new JLabel("Contents");
					lblContents.setForeground(new Color(135, 206, 250));
					lblContents.setFont(new Font("Lucida Grande", Font.BOLD, 15));
					GridBagConstraints gbc_lblContents = new GridBagConstraints();
					gbc_lblContents.fill = GridBagConstraints.HORIZONTAL;
					gbc_lblContents.gridx = 0;
					gbc_lblContents.gridy = 0;
					labelPanel.add(lblContents, gbc_lblContents);

					contentTable = new JTable(TableModelGenerator.getInstance()
							.generateContentModel(TargetContext.getInstance().toTree()));
					contentTable.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							//TODo
						}
					});

					JScrollPane tableScrollPane = new JScrollPane();
					tableScrollPane.setViewportView(contentTable);
					contentPanel.add(tableScrollPane, BorderLayout.CENTER);
				}

				JPanel IssuePanel = new JPanel();
				chileSplitPane.setRightComponent(IssuePanel);
				IssuePanel.setLayout(new BorderLayout(0, 0));

				/*
				 * Issue
				 */

				{
					JPanel labelPanel = new JPanel();
					GridBagLayout gbl_labelPanel = new GridBagLayout();
					gbl_labelPanel.columnWidths = new int[]{48, 0};
					gbl_labelPanel.rowHeights = new int[]{29, 0};
					gbl_labelPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
					gbl_labelPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
					labelPanel.setLayout(gbl_labelPanel);
					IssuePanel.add(labelPanel, BorderLayout.NORTH);

					JLabel lblIssue = new JLabel("Issues");
					lblIssue.setFont(new Font("Lucida Grande", Font.BOLD, 15));
					lblIssue.setForeground(new Color(135, 206, 250));
					GridBagConstraints gbc_lblIssue = new GridBagConstraints();
					gbc_lblIssue.fill = GridBagConstraints.HORIZONTAL;
					gbc_lblIssue.gridx = 0;
					gbc_lblIssue.gridy = 0;
					labelPanel.add(lblIssue, gbc_lblIssue);

					issuePane = new JTextPane();
					IssuePanel.add(issuePane, BorderLayout.CENTER);
				}
			}
		}
	}

	void updateMapData() {
		siteMapTree.setModel(new DefaultTreeModel(TargetContext.getInstance().toTree()));
	}

}
