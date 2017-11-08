package application.utility.net;

import application.utility.net.Exceptions.IllegalHeaderDataException;

public interface EditableHeader extends Header {

	void setValue(String value) throws IllegalHeaderDataException;

}