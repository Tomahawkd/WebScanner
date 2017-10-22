package application.spider;

import application.alertHandler.AlertHandler;
import application.utility.parser.html.HTMLParser;
import application.utility.thread.ThreadPool;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SpiderController {


	private boolean suspendFlag = false;

	//Thread control
	private int threadNum = 3;
	private ThreadPool threadPool;

	//Data control
	private final Cookie cookie = new Cookie();

	public void start() {
		//TODO
	}

	public void suspend() {
		suspendFlag = true;
	}

	public void stop() {
		//TODO
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public int getThreadNum() {
		return threadNum;
	}

	private Map<String, Boolean> crawlLinks(Map<String, Boolean> urlMap) {

		if (!suspendFlag) {
			// New URLs queue map
			Map<String, Boolean> newURLMap = new LinkedHashMap<>();

			try {
				threadPool = new ThreadPool(3);
				for (Map.Entry<String, Boolean> mapping : urlMap.entrySet()) {

					// Check if is already accessed
					if (!mapping.getValue()) {
						String url = mapping.getKey();

						// Set to already accessed
						urlMap.replace(url, false, true);

						threadPool.execute(getConnnectExecution(url, newURLMap, urlMap));
					}
				}
				threadPool.waitFor();
			} catch (ConcurrentModificationException ignored) {
			} catch (InterruptedException e) {
				threadPool.close();
			}

			// if the new queue map is not empty then rescue
			if (!newURLMap.isEmpty()) {

				// Save URLs to queue map
				urlMap.putAll(newURLMap);

				Map<String, Boolean> childMap = crawlLinks(urlMap);

				//TODO Save URLs to queue map
				urlMap.putAll(childMap);
			}
		}
		return urlMap;

	}

	private Runnable getConnnectExecution(String url,
	                                      Map<String, Boolean> newURLMap,
	                                      Map<String, Boolean> urlMap) {
		return () -> {
			try {
				URL link = new URL(url);
				SpiderConnection conn = new SpiderConnection(link);
				synchronized (cookie) {
					conn.connectWithCookie(cookie);
				}
				String host = link.getProtocol() + "://" + link.getHost();
				Document doc = HTMLParser.getParser().parse(conn.getContext().getData(), host);

				for (String urlStr : HTMLParser.getParser().getLink(doc)) {
					synchronized (newURLMap) {
						if (!urlStr.equals("") && !urlMap.containsKey(urlStr)
								&& !newURLMap.containsKey(urlStr)) {
							newURLMap.put(urlStr, false);
						}
					}
				}

			} catch (MalformedURLException e) {
				AlertHandler.getInstance().addAlert("Spider",
						"MalformedURLException: URL " + url + " is not valid");
			} catch (IOException ignored) {
			}
		};
	}
}
