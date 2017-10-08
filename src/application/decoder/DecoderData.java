package application.decoder;

public class DecoderData {

	private byte[] data;
	private TableData hexData;

	DecoderData() {
		data = "".getBytes();
		hexData = new TableData(this.data);
	}

	public void setData(String data) {
		this.data = data.getBytes();
		hexData.setData(this.data);
	}

	public String getData() {
		this.data = hexData.getData();
		return new String(this.data);
	}

	public TableData getHexData() {
		return hexData;
	}
}
