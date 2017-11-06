package application.utility.net.data;

import java.io.Serializable;
import java.util.Map;

public interface HeaderMap extends Serializable {

	int REQUEST = 0;
	int RESPONSE = 1;

	int getType();

	Header get(String key);

	Map.Entry<String, Header> getAtIndex(int index);

	String toFormalHeader();

	int size();
}