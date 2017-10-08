package application.view.frame.main;

public class ExitTipFrameController {
	private static ExitTipFrame frame;

	public static ExitTipFrame getInstance() {
		if (frame == null) frame = new ExitTipFrame();
		frame.setVisible(true);
		return frame;
	}
}
