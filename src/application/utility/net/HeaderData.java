package application.utility.net;

class HeaderData implements EditableHeader {
	private String data;

	HeaderData(String data) {
		setValue(data);
	}

	@Override
	public void setValue(String value) {
		this.data = value;
	}

	@Override
	public String toString() {
		return data;
	}
}