package application.utility.net;

import application.utility.net.Exceptions.IllegalHeaderDataException;

import java.net.URL;

public interface EditableContext extends Context {

	void setUrl(URL url);

	void setRequestForm(String form) throws IllegalHeaderDataException;

	void addResponse(String line) throws IllegalHeaderDataException;

	void setResponseData(byte[] data);

	void clearRequest();
	void clearResponse();
}