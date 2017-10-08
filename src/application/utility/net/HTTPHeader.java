package application.utility.net;

final class HTTPHeader {

	private String type;
	private String name;

	HTTPHeader(String type, String name) {
		this.type = type;
		this.name = name;
	}

	String getType() {
		return type;
	}

	String getName() {
		return name;
	}

	boolean equals(String type, String name) {
		return this.type.equals(type) && this.name.equals(name);
	}

	boolean equals(String type) {
		return this.type.equals(type);
	}

	@Override
	public String toString() {
		return "( " + type + ", " + name + " )";
	}
}
