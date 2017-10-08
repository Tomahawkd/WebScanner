package application.view.frame.decoder;

public class DecoderPanelController {

	private static DecoderPanel panel;

	public static DecoderPanel getInstance() {
		if (panel == null) panel = new DecoderPanel();
		return panel;
	}
}
