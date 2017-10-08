package application.view.frame.target;

import javax.swing.*;
import java.awt.*;

class ScopePane extends JScrollPane {

	/**
	 *
	 */
	private static final long serialVersionUID = -5037202105380371557L;

	private JTable tableInclude;
	private JTable tableExclude;

	ScopePane() {

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(1000, 500));
		panel.setLayout(null);
		setViewportView(panel);

		/*
		 * Include
		 */

		{
			JLabel lblTargetScope = new JLabel("Target Scope");
			lblTargetScope.setBounds(24, 21, 102, 19);
			panel.add(lblTargetScope);
			lblTargetScope.setForeground(new Color(135, 206, 250));
			lblTargetScope.setFont(new Font("Lucida Grande", Font.BOLD, 15));

			JLabel lblInclude = new JLabel("Include");
			lblInclude.setBounds(24, 66, 46, 16);
			panel.add(lblInclude);

			JButton btnIncludeAdd = new JButton("Add");
			btnIncludeAdd.setBounds(24, 106, 93, 34);
			panel.add(btnIncludeAdd);

			JButton btnIncludeEdit = new JButton("Edit");
			btnIncludeEdit.setBounds(24, 162, 93, 34);
			panel.add(btnIncludeEdit);

			JButton btnIncludeRemove = new JButton("Remove");
			btnIncludeRemove.setBounds(24, 217, 93, 34);
			panel.add(btnIncludeRemove);

			tableInclude = new JTable();
			tableInclude.setBounds(129, 102, 828, 149);
			panel.add(tableInclude);
		}


		/*
		 * Exclude
		 */

		{
			JLabel lblExclude = new JLabel("Exclude");
			lblExclude.setBounds(24, 290, 49, 16);
			panel.add(lblExclude);

			JButton btnExcludeAdd = new JButton("Add");
			btnExcludeAdd.setBounds(24, 324, 93, 34);
			panel.add(btnExcludeAdd);

			JButton btnExcludeEdit = new JButton("Edit");
			btnExcludeEdit.setBounds(24, 382, 93, 34);
			panel.add(btnExcludeEdit);

			JButton btnExcludeRemove = new JButton("Remove");
			btnExcludeRemove.setBounds(24, 439, 93, 34);
			panel.add(btnExcludeRemove);

			tableExclude = new JTable();
			tableExclude.setBounds(129, 324, 828, 149);
			panel.add(tableExclude);
		}
	}

}
