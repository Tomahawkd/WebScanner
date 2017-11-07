package application.utility.net;

abstract class HeaderInfo implements Header {

	abstract String getVersion();

	public abstract String toFormalHeader();
}