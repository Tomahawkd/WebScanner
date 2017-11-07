package application.utility.net;

class HeaderData implements Header {
	private String data;

	HeaderData(String data) {
		this.data = data;
	}

	@Override
	public String toFormalHeader() {
		return data + CRLF;
	}

	@Override
	public String toString() {
		return data;
	}
}