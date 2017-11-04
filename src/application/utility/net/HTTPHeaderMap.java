package application.utility.net;

import application.utility.net.Exceptions.IllegalHeaderDataException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public interface HTTPHeaderMap extends Serializable {

	int REQUEST = 0;
	int RESPONSE = 1;

	int getType();

	String get(String type, String name) throws IllegalHeaderDataException;

	ArrayList<Map.Entry<HTTPHeader, String>> get(String type);

	Map.Entry<HTTPHeader, String> getAtIndex(int index);
}
