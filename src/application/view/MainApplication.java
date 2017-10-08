package application.view;

/*
 * Created by Ghost on 17/07/2017.
 */

import application.view.frame.main.MainFrameController;

import java.awt.*;

public class MainApplication {

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				MainFrameController.getInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}
