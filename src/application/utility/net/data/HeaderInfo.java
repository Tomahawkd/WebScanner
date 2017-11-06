package application.utility.net.data;

abstract class HeaderInfo implements Header {

	abstract String getVersion();

	public abstract String toFormalHeader();
}