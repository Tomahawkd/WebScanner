package application.view.frame.main;

import application.utility.thread.TaskControl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Interface: Notify the user when exit the application.
 *
 * @author Tomahawkd
 */

class ExitTipFrame extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * Create the frame.
	 */

	ExitTipFrame() {

		/*
		 * Self configuration
		 */

		setResizable(false);
		setTitle("Confirm");
		setBounds(230, 250, 450, 150);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		/*
		 * Labels
		 */

		JLabel lblSaveYourProjects = new JLabel("Save your projects before you quit the application.");
		lblSaveYourProjects.setBounds(66, 22, 317, 16);
		contentPane.add(lblSaveYourProjects);

		JLabel lblDoYouWant = new JLabel("Do you want to quit?");
		lblDoYouWant.setBounds(66, 50, 317, 16);
		contentPane.add(lblDoYouWant);

		/*
		 * Buttons
		 */

		JButton btnOk = new JButton("OK");

		// ExitTipFrame the application
		btnOk.addActionListener(e -> {
			TaskControl.getInstance().shutDown();
			dispose();
		});
		btnOk.setBounds(76, 78, 117, 29);
		contentPane.add(btnOk);

		JButton btnCancel = new JButton("Cancel");

		// Cancel exiting
		btnCancel.addActionListener(e -> dispose());
		btnCancel.setBounds(266, 78, 117, 29);
		contentPane.add(btnCancel);
	}

}
