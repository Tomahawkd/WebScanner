package application.utility.net;

import application.utility.net.Exceptions.IllegalHeaderDataException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

class HTTPHeaderMapImpl
		extends LinkedHashMap<HTTPHeader, String> implements HTTPHeaderMap {

	private int type;

	public int getType() {
		return type;
	}

	void setType(int type) {
		if (type == REQUEST || type == RESPONSE) this.type = type;
		else throw new IllegalArgumentException("Unsupported type");
	}

	HTTPHeaderMapImpl(int type) {
		super();
		setType(type);
	}

	public String get(String type, String name) throws IllegalHeaderDataException {
		for (Map.Entry<HTTPHeader, String> mapping : this.entrySet()) {
			if (mapping.getKey().equals(type, name)) return mapping.getValue();
		}

		throw new IllegalHeaderDataException("Data( " + type + ", " + name + " ) not found");
	}

	public ArrayList<Map.Entry<HTTPHeader, String>> get(String type) {
		ArrayList<Map.Entry<HTTPHeader, String>> list = new ArrayList<>();
		for (Map.Entry<HTTPHeader, String> mapping : this.entrySet()) {
			if (mapping.getKey().equals(type)) list.add(mapping);
		}
		return list;
	}

	private String update(HTTPHeader key, String value) throws IllegalHeaderDataException {
		for (Map.Entry<HTTPHeader, String> mapping : this.entrySet()) {
			if (mapping.getKey().equals(key.getType(), key.getName())) {
				String oldValue = mapping.getValue();
				mapping.setValue(value);
				return oldValue;
			}
		}

		throw new IllegalHeaderDataException("Data( " + key + " ) not found");
	}

	public String getMethod() throws IllegalHeaderDataException {
		try {
			return get("Method", "MethodType");
		} catch (IllegalHeaderDataException e) {
			throw new IllegalHeaderDataException("Request method is missing");
		}
	}

	public Map.Entry<HTTPHeader, String> getAtIndex(int index) throws IndexOutOfBoundsException {

		int count = 0;
		for (Map.Entry<HTTPHeader, String> mapping : this.entrySet()) {
			if (index == count) return mapping;
			count++;
		}

		throw new IndexOutOfBoundsException();
	}

	@Override
	public String put(HTTPHeader key, String value) {
		try {
			return update(key, value);
		} catch (IllegalHeaderDataException e) {
			return super.put(key, value);
		}
	}

	void put(String type, String name, String value) {
		put(new HTTPHeader(type, name), value);
	}
}
