package application.decoder;

public class ResultDataHandler {

	private static DecoderData data;

	public static DecoderData getInstance() {
		if (data == null) data = new DecoderData();
		return data;
	}
}
