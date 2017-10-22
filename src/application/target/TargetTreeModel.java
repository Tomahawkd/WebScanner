package application.target;

import application.utility.net.Context;
import application.view.frame.target.TargetPanelController;

import java.net.URL;

public class TargetTreeModel {

	private DataNode root;
	private static TargetTreeModel treeModel;

	public static TargetTreeModel getDefaultModel() {
		if (treeModel == null) treeModel = new TargetTreeModel();
		return treeModel;
	}

	private TargetTreeModel() {
		root = new DataNode("", null);
	}

	public void setRoot(String name, Context data) {
		root = new DataNode(name, data);
	}

	public DataNode getRoot() {
		return root;
	}

	public synchronized void add(URL path, Context data) {

		DataNode currentNode = root;

		// Loop path array to locate the file
		for (String aPath : toPathArray(path)) {

			// Check if the node is already exist
			int childIndex = currentNode.getIndex(aPath);
			if (childIndex == -1) {

				// Create new node
				DataNode newChild = new DataNode(aPath, null);
				currentNode.add(newChild);
				currentNode = newChild;

			} else {

				// Get child node
				currentNode = (DataNode) currentNode.getChildAt(childIndex);
			}
		}

		// Add data
		currentNode.setData(data);
		TargetPanelController.getInstance().updateMapData();
	}

	private String[] toPathArray(URL url) {

		String[] path = url.getPath().substring(1).split("/");
		path[path.length - 1] += (url.getQuery() != null ? "?" + url.getQuery() : "");

		String[] pathArr = new String[path.length + 1];
		pathArr[0] = url.getProtocol() + "://" + url.getHost();

		System.arraycopy(path, 0, pathArr, 1, pathArr.length - 1);
		return pathArr;
	}
}
