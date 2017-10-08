package application.view.frame.proxy;

import javax.swing.*;
import java.awt.*;

class HistoryPane extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = -1720662412037641704L;

	private JTable historyTable;

	HistoryPane() {
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

			JLabel lblFilter = new JLabel("Filter:");
			lblFilter.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
			GridBagConstraints gbc_lblFilter = new GridBagConstraints();
			gbc_lblFilter.fill = GridBagConstraints.BOTH;
			gbc_lblFilter.gridx = 0;
			gbc_lblFilter.gridy = 0;
			filterPanel.add(lblFilter, gbc_lblFilter);
		}


		historyTable = new JTable();
		add(historyTable, BorderLayout.CENTER);
	}
}
