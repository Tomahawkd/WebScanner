package application.spider;

import application.view.frame.spider.SpiderPanelController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpiderQueue extends ConcurrentHashMap<String, Boolean> {

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
		updateQueue();
		return super.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Boolean> m) {
		queueCount += m.size();
		updateQueue();
		super.putAll(m);
	}

	@Override
	public Boolean replace(String key, Boolean value) {
		queueCount--;
		updateQueue();
		return super.replace(key, value);
	}

	@Override
	public boolean replace(String key, Boolean oldValue, Boolean newValue) {
		queueCount--;
		updateQueue();
		return super.replace(key, oldValue, newValue);
	}

	@Override
	public void clear() {
		queueCount = 0;
		updateQueue();
		super.clear();
	}

	private void updateQueue() {
		SpiderPanelController.getInstance().updateQueueCounter(queueCount);
	}
}
