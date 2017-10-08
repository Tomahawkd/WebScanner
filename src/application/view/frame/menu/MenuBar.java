package application.view.frame.menu;

import application.view.frame.main.ExitTipFrameController;

import javax.swing.*;

/**
 * Created by Ghost on 18/07/2017.
 */

class MenuBar extends JMenuBar {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	MenuBar() {

		/*
		 * Menu: Project
		 */

		JMenu mnProject = new JMenu("Project");
		add(mnProject);

		/*
		 * Items
		 */

		JMenuItem mntmNew = new JMenuItem("New...");

		//TODO New file operation
		mnProject.add(mntmNew);

		JMenuItem mntmLoad = new JMenuItem("Load");

		//TODO Load file operation
		mnProject.add(mntmLoad);

		JMenuItem mntmSave = new JMenuItem("Save");

		//TODO Save file operation
		mnProject.add(mntmSave);

		JMenuItem mntmSaveAs = new JMenuItem("Save as...");

		//TODO Save-as file operation
		mnProject.add(mntmSaveAs);

		// Add separator
		mnProject.addSeparator();

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(e -> ExitTipFrameController.getInstance());
		mnProject.add(mntmExit);

		/*
		 * Menu: About
		 */

		JMenu mnAbout = new JMenu("About");
		add(mnAbout);

		/*
		 * Items
		 */

		JMenuItem mntmAboutUs = new JMenuItem("About Us");
		//TODO about us
		mnAbout.add(mntmAboutUs);
	}

}
