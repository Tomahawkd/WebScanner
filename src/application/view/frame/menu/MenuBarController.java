package application.view.frame.menu;

/**
 * Created by Ghost on 18/07/2017.
 */
public class MenuBarController {
	private static MenuBar menuBar;

	public static MenuBar getInstance() {
		if (menuBar == null) menuBar = new MenuBar();
		return menuBar;
	}
}
