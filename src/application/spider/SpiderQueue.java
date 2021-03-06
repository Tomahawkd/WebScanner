package application.spider;

import application.view.frame.spider.SpiderPanelController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpiderQueue extends ConcurrentHashMap<String, Boolean> {

	private int queueCount;
	private static SpiderQueue queue;

	static SpiderQueue getQueue() {
		if (queue == null) queue = new SpiderQueue();
		return queue;
	}

	private SpiderQueue() {
		queueCount = 0;
	}

	@Override
	public Boolean put(String key, Boolean value) {
		if (get(key) != null) {
			queueCount++;
			updateQueue();
			return super.put(key, value);
		} else return false;
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
