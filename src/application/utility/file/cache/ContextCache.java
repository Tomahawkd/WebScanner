package application.utility.file.cache;

import application.utility.net.Context;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ContextCache {

	private static ContextCache cache;

	private ArrayList<String> hosts;
	private Map<String, ContextList> cachedMap;
	private CachedFile cachedFile;
	private final int capacity = 3;

	public static ContextCache getInstance() {
		if (cache == null) cache = new ContextCache();
		return cache;
	}

	private ContextCache() {
		this.cachedMap = new LinkedHashMap<>(capacity);
		this.cachedFile = new CachedFile();
		this.hosts = new ArrayList<>();
	}

	public void add(String host, Context data) {
		ContextList list = get(host);
		if (list == null) {
			hosts.add(host);
			list = new ContextList(data);
		} else {
			list.add(data);
		}
		put(host, list);

	}

	public void put(String host, ContextList list) {
		cachedMap.put(host, list);
		int index = 0;
		for (Map.Entry<String, ContextList> mapping : cachedMap.entrySet()) {
			if (index > capacity - 1) {
				try {
					cachedFile.writeData(mapping.getKey(), mapping.getValue());
					cachedMap.remove(mapping.getKey(), mapping.getValue());
				} catch (IOException ignored) {
				}
			}
			index++;
		}
	}

	public ContextList get(String host) {
		try {
		return cachedMap.get(host) == null ? cachedFile.readData(host) : cachedMap.get(host);
		} catch (IOException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null,
					"Temporary file read failed",
					"Temporary File Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	public Map<String, ContextList> toFileData() {
		Map<String, ContextList> contextListMap = new HashMap<>();
		for (String host : hosts) contextListMap.put(host, get(host));
		return contextListMap;
	}
}