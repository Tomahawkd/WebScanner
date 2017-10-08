package application.decoder;

public class DataHandler {

	private static DecoderData data;

	public static DecoderData getInstance() {
		if (data == null) data = new DecoderData();
		return data;
	}
}
