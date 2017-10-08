package application.utility.net;

import application.utility.net.Exceptions.IllegalHeaderDataException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

class HTTPHeaderMap<K extends HTTPHeader, V extends String> extends LinkedHashMap<K, V> {

	private int type;

	static final int REQUEST = 0;
	static final int RESPONSE = 1;

	int getType() {
		return type;
	}

	void setType(int type) {
		if (type == REQUEST || type == RESPONSE) this.type = type;
		else throw new IllegalArgumentException("Unsupported type");
	}

	HTTPHeaderMap(int type) {
		super();
		setType(type);
	}

	V get(String type, String name) throws IllegalHeaderDataException {
		for (Map.Entry<K, V> mapping : this.entrySet()) {
			if (mapping.getKey().equals(type, name)) return mapping.getValue();
		}

		throw new IllegalHeaderDataException("Data( " + type + ", " + name + " ) not found");
	}

	ArrayList<Map.Entry<K, V>> get(String type) {
		ArrayList<Map.Entry<K, V>> list = new ArrayList<>();
		for (Map.Entry<K, V> mapping : this.entrySet()) {
			if (mapping.getKey().equals(type)) list.add(mapping);
		}
		return list;
	}

	private V update(K key, V value) throws IllegalHeaderDataException {
		for (Map.Entry<K, V> mapping : this.entrySet()) {
			if (mapping.getKey().equals(key.getType(), key.getName())) {
				V oldValue = mapping.getValue();
				mapping.setValue(value);
				return oldValue;
			}
		}

		throw new IllegalHeaderDataException("Data( " + key + " ) not found");
	}

	V getMethod() throws IllegalHeaderDataException {
		try {
			return get("Method", "MethodType");
		} catch (IllegalHeaderDataException e) {
			throw new IllegalHeaderDataException("Request method is missing");
		}
	}

	Map.Entry<K, V> getAtIndex(int index) throws IndexOutOfBoundsException {

		int count = 0;
		for (Map.Entry<K, V> mapping : this.entrySet()) {
			if (index == count) return mapping;
			count++;
		}

		throw new IndexOutOfBoundsException();
	}

	@Override
	public V put(K key, V value) {
		try {
			return update(key, value);
		} catch (IllegalHeaderDataException e) {
			return super.put(key, value);
		}
	}

	@SuppressWarnings("unchecked")
	void put(String type, String name, V value) {
		put((K) new HTTPHeader(type, name), value);
	}
}
