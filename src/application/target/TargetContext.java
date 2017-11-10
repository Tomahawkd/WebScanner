package application.target;

import application.alertHandler.AlertHandler;
import application.spider.SpiderHandler;
import application.utility.file.DataSerializable;
import application.utility.net.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class TargetContext implements Target, DataSerializable<TargetContext.ContextMap> {

	private static Target instance;

	private ContextMap contextMap;
	private TargetTreeModel treeModel;

	public static Target getInstance() {
		if (instance == null) instance = new TargetContext();
		return instance;
	}

	private TargetContext() {
		contextMap = new ContextMap();
		treeModel = new TargetTreeModel();
	}

	public void addTarget(Context context) {
		if(context.getURL() != null) {
			contextMap.add(context.getHost(), context.copy());
			SpiderHandler.addToQueue(context.getURL().toExternalForm());
		} else {
			AlertHandler.getInstance().addAlert("Target", "URL is null");
		}
	}

	/**
	 * <p>Get a list of the site which has been accessed.</p>
	 * <p>Invoke this function will get a reference.</p>
	 * <p>Invoke {@link ContextList#copy()} to get a copy.</p>
	 * @return context list
	 */
	public ContextList getContextList() {
		return contextMap.toList();
	}

	public DataNode toTree() {
		return treeModel.getRoot();
	}

	@Override
	public void setData(ContextMap data) {
		//TODO
	}

	@Override
	public ContextMap getData() {
		return null;
		//TODO
	}

	class ContextMap extends ConcurrentHashMap<String, ContextList> implements Serializable {

		ContextList toList() {
			ContextList list = new ContextList(size());
			for (ContextList value : this.values()) list.addAll(value);
			return list;
		}

		void add(String host, Context context) {
			treeModel.add(context.getURL(), context);
			if (get(host) == null) put(host, new ContextList(context));
			else {
				get(host).add(context);
			}
		}

		@Override
		public int size() {
			int size = 0;
			for (Collection<Context> contexts : this.values()) {
				size += contexts.size();
			}
			return size;
		}
	}

	public class ContextList extends ArrayList<Context> {

		ContextList(int initialSize) {
			super(initialSize);
		}

		ContextList(Context context) {
			super();
			add(context);
		}

		/**
		 * Deep copy of the array.
		 * @return A copy of current array.
		 */
		public ContextList copy() {
			ContextList list = new ContextList(this.size());
			for (Context context : this) {
				list.add(context.copy());
			}
			return list;
		}

		@Override
		public boolean add(Context context) {
			Context currentContext;
			//Initial list
			if (size() == 0) return super.add(context);
			for (int index = 0; index < size(); index++) {
				currentContext = get(index);
				if (currentContext != null) {
					if (currentContext.getURL() != null && context.getURL() != null) {
						if (currentContext.getURL().getPath().equals(context.getURL().getPath())) {
							//Param is different
							if (!currentContext.getParams().equals(context.getParams())) {
								return super.add(context);
							} else {
								//Update data
								remove(index);
								super.add(index, context);
								//Compare address of the data
								return get(index) == context;
							}
						}
					}
				}
			}
			return false;
		}
	}
}
