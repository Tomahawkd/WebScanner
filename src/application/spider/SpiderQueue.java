package application.spider;

import application.view.frame.spider.SpiderPanelController;

import java.util.LinkedHashMap;
import java.util.Map;

public class SpiderQueue extends LinkedHashMap<String, Boolean> {

	private static int queueCount = 0;
	private static SpiderQueue queue;

	public static SpiderQueue getQueue() {
		if (queue == null) queue = new SpiderQueue();
		return queue;
	}

	private SpiderQueue() {}

	@Override
	public Boolean put(String key, Boolean value) {
		queueCount++;
		SpiderPanelController.getInstance().updateQueueCounter(queueCount);
		return super.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Boolean> m) {
		queueCount += m.size();
		SpiderPanelController.getInstance().updateQueueCounter(queueCount);
		super.putAll(m);
	}

	@Override
	public Boolean replace(String key, Boolean value) {
		queueCount--;
		SpiderPanelController.getInstance().updateQueueCounter(queueCount);
		return super.replace(key, value);
	}

	@Override
	public boolean replace(String key, Boolean oldValue, Boolean newValue) {
		queueCount--;
		SpiderPanelController.getInstance().updateQueueCounter(queueCount);
		return super.replace(key, oldValue, newValue);
	}

	@Override
	public void clear() {
		queueCount = 0;
		SpiderPanelController.getInstance().updateQueueCounter(0);
		super.clear();
	}
}
