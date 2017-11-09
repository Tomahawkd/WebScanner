package application.utility.net;

public interface Header {

	String CRLF = "\r\n";

	default String toFormalHeader() {
		return toString() + CRLF;
	}

	Header copy();
}