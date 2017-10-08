package application.view.frame.main;

/**
 * Created by Ghost on 18/07/2017.
 */
public class MainFrameController {
	private static MainFrame mainFrame;

	public static MainFrame getInstance() {
		if (mainFrame == null) mainFrame = new MainFrame();
		return mainFrame;
	}
}
