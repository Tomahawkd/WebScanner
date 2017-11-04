package application.spider;

import application.alertHandler.AlertHandler;
import application.repeater.RepeaterData;
import application.target.TargetTreeModel;
import application.utility.parser.html.HTMLParser;
import org.jsoup.nodes.Document;

import javax.swing.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.*;

public class SpiderController {

	//Execution control
	private boolean suspendFlag = false;
	private boolean firstExecution = true;

	//Thread control
	private int threadNum = 10;

	//Data control
	private final Cookie cookie = new Cookie();
	private final SpiderQueue queue = SpiderQueue.getQueue();
	private final TargetTreeModel dataModel = TargetTreeModel.getDefaultModel();

	//Scope control
	//Regex

	public void start() {
		URL baseURL = RepeaterData.getInstance().getURL();
		if (baseURL != null) {
			String baseURLStr = baseURL.getProtocol() + "//:" + baseURL.getHost();
			dataModel.setRoot(baseURLStr, null);
			queue.put(baseURL.toExternalForm(), false);
			firstExecution = false;
			suspendFlag = false;
			new Thread(() -> crawlLinks(queue), "Spider Main Thread").start();
		} else {
			JOptionPane.showMessageDialog(
					null,
					"Please set target URL in Repeater",
					"Target",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public void suspend() {
		suspendFlag = true;
	}

	public void resume() {
		suspendFlag = false;
		new Thread(() -> crawlLinks(queue), "Spider Main Thread").start();
	}

	public void clearQueue() {
		queue.clear();
	}

	public boolean isFirstExecution() {
		return firstExecution;
	}


	public void setThreadNum(int threadNum) throws IllegalArgumentException {
		if (threadNum < 0) throw new IllegalArgumentException();
		this.threadNum = threadNum;
	}

	public int getThreadNum() {
		return threadNum;
	}

	private SpiderQueue crawlLinks(SpiderQueue urlMap) {

		if (!suspendFlag) {
			// New URLs queue map
			Map<String, Boolean> newURLMap = new ConcurrentHashMap<>();

			ExecutorService executor = Executors.newScheduledThreadPool(threadNum);
			for (Map.Entry<String, Boolean> mapping : urlMap.entrySet()) {

				// Check if is already accessed
				if (!mapping.getValue()) {
					String url = mapping.getKey();

					// Set to already accessed
					urlMap.replace(url, false, true);

					try {
						executor.execute(getTask(url, newURLMap, urlMap));
					} catch (RejectedExecutionException e) {
						new Thread(getTask(url, newURLMap, urlMap)).start();
					}
				}
			}
			executor.shutdown();
			try {
				boolean suspendFlag;
				do suspendFlag = !executor.awaitTermination(60, TimeUnit.SECONDS); while (suspendFlag);
			} catch (InterruptedException ignored) {
			}

			// if the new queue map is not empty then rescue
			if (!newURLMap.isEmpty()) {
				// Save URLs to queue map
				urlMap.putAll(newURLMap);

				Map<String, Boolean> childMap = crawlLinks(urlMap);

				urlMap.putAll(childMap);
			}
		}
		return urlMap;
	}

	private Runnable getTask(String url, Map<String, Boolean> newURLMap, SpiderQueue urlMap) {
		return () -> {
			try {
				URL link = new URL(url);

				try {
					SpiderConnection conn = new SpiderConnection(link);

					synchronized (cookie) {
						conn.connectWithCookie(cookie);
					}

					dataModel.add(link, "GET", conn.getContext());

					String host = link.getProtocol() + "://" + link.getHost();
					Document doc = HTMLParser.getParser().parse(conn.getContext().getData(), host);

					Map<String, Boolean> newCrawledURLMap = new ConcurrentHashMap<>();
					for (String urlStr : HTMLParser.getParser().getLink(doc)) {
						if (!urlStr.equals("") && !urlMap.containsKey(urlStr)) {
							if (!newCrawledURLMap.containsKey(urlStr)) newCrawledURLMap.put(urlStr, false);
						}
					}
					newURLMap.putAll(newCrawledURLMap);

				} catch (StringIndexOutOfBoundsException ignored) {
				}
			} catch (MalformedURLException e) {
				AlertHandler.getInstance().addAlert("Spider",
						"MalformedURLException: URL " + url + " is not valid");
			} catch (IOException e) {
				AlertHandler.getInstance().addAlert("Spider",
						e.getClass().getName() + ": " + (e.getMessage() == null ? "" : e.getMessage()));
			}
		};
	}
}
