package application.utility.net.data;

import application.utility.net.Exceptions.IllegalHeaderDataException;

import java.net.URL;

public interface EditableContext extends Context {

	void setUrl(URL url);

	void setForm(String form) throws IllegalHeaderDataException;

	void addHeaderLine(String line) throws IllegalHeaderDataException;

	void setHeader(String header) throws IllegalHeaderDataException;

	void setData(byte[] data);

	void clear();
}