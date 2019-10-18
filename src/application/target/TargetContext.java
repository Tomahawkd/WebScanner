package application.target;

import application.alertHandler.AlertHandler;
import application.spider.SpiderHandler;
import application.utility.file.cache.ContextList;
import application.utility.file.cache.ContextCache;
import application.utility.net.Context;

public class TargetContext implements Target {

	private static TargetContext instance;

	private ContextCache contextCache;
	private TargetTreeModel treeModel;

	public static Target getInstance() {
		if (instance == null) instance = new TargetContext();
		return instance;
	}

	private TargetContext() {
		contextCache = ContextCache.getInstance();
		treeModel = new TargetTreeModel();
	}

	public void addTarget(Context context) {
		if(context.getURL() != null) {
			contextCache.add(context.getHost(), context);
			SpiderHandler.addToQueue(context.getURL().toExternalForm());
		} else {
			AlertHandler.getInstance().addAlert("Target", "URL is null");
		}
	}

	public DataNode toTree() {
		return treeModel.getRoot();
	}

	@Override
	public Context getContext(String host, String path) {
		ContextList list = contextCache.get(host);
		if (list != null) {
			for(Context context : list) {
				if (context.getPath().equals(path)) return context;
			}
		}
		return null;
	}
}
