package application.spider;

import application.view.frame.spider.SpiderPanelController;

import java.util.LinkedHashMap;

class SpiderQueue extends LinkedHashMap<String, Boolean> {

	private static int queueCount = 0;

	@Override
	public Boolean put(String key, Boolean value) {
		queueCount++;
		SpiderPanelController.getInstance().updateQueueCounter(queueCount);
		return super.put(key, value);
	}

	static void decreaseCountBy(int num) {
		queueCount -= num;
		SpiderPanelController.getInstance().updateQueueCounter(queueCount);
	}
}
