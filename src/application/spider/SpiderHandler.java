package application.spider;

public class SpiderHandler {

	private static SpiderController instance;

	public static SpiderController getInstance() {
		if (instance == null) instance = new SpiderController();
		return instance;
	}

	public void addToQueue(String url) {
		SpiderQueue.getQueue().put(url, false);
	}
}