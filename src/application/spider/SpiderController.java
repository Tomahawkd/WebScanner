package application.spider;

import application.alertHandler.AlertHandler;
import application.repeater.RepeaterData;
import application.target.TargetTreeModel;
import application.utility.parser.html.HTMLParser;
import application.utility.thread.ThreadPool;
import application.view.frame.spider.SpiderPanelController;
import org.jsoup.nodes.Document;

import javax.swing.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SpiderController {

	//Excution control
	private boolean suspendFlag = false;
	private boolean firstExecution = true;

	//Thread control
	private int threadNum = 3;
	private ThreadPool threadPool;

	//Data control
	private final Cookie cookie = new Cookie();
	private final SpiderQueue queue = new SpiderQueue();
	private final TargetTreeModel dataModel = TargetTreeModel.getDefaultModel();

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
		SpiderPanelController.getInstance().updateQueueCounter(0);
	}

	public boolean isFirstExecution() {
		return firstExecution;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public TargetTreeModel getDataModel() {
		return dataModel;
	}

	private SpiderQueue crawlLinks(SpiderQueue urlMap) {

		if (!suspendFlag) {
			// New URLs queue map
			Map<String, Boolean> newURLMap = new LinkedHashMap<>();

			try {
//				threadPool = new ThreadPool(3);
				for (Map.Entry<String, Boolean> mapping : urlMap.entrySet()) {

					// Check if is already accessed
					if (!mapping.getValue()) {
						String url = mapping.getKey();

						// Set to already accessed
						urlMap.replace(url, false, true);

//						threadPool.execute(() -> {
							try {
								URL link = new URL(url);

								SpiderConnection conn;
								try {
									conn = new SpiderConnection(link);
								} catch (StringIndexOutOfBoundsException ignored) {
									continue;
								}
								synchronized (cookie) {
									conn.connectWithCookie(cookie);
								}

								SpiderQueue.decreaseCountBy(1);
								dataModel.add(link, conn.getContext());

								String host = link.getProtocol() + "://" + link.getHost();
								Document doc = HTMLParser.getParser().parse(conn.getContext().getData(), host);

								for (String urlStr : HTMLParser.getParser().getLink(doc)) {
									if (!urlStr.equals("") && !urlMap.containsKey(urlStr)) {
//										synchronized (newURLMap) {
											if (!newURLMap.containsKey(urlStr)) newURLMap.put(urlStr, false);
//										}
									}
								}
							} catch (MalformedURLException e) {
								AlertHandler.getInstance().addAlert("Spider",
										"MalformedURLException: URL " + url + " is not valid");
							} catch (IOException ignored) {
							}
//						});
					}
				}
//				threadPool.waitFor();
			} catch (ConcurrentModificationException ignored) {
//			} catch (InterruptedException e) {
//				threadPool.close();
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
}
