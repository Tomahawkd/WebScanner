package application.spider;

import application.utility.net.Context;

import java.net.URL;

public class SpiderDataModel {

	private SpiderDataNode root;

	public SpiderDataModel() {
		root = new SpiderDataNode("", null);
	}

	public SpiderDataModel(String name, Context data) {
		setRoot(name, data);
	}

	void setRoot(String name, Context data) {
		root = new SpiderDataNode(name, data);
	}

	public SpiderDataNode getRoot() {
		return root;
	}

	public synchronized void add(URL path, Context data) {

		SpiderDataNode currentNode = root;

		// Loop path array to locate the file
		for (String aPath : toPathArray(path)) {

			// Check if the node is already exist
			int childIndex = currentNode.getIndex(aPath);
			if (childIndex == -1) {

				// Create new node
				SpiderDataNode newChild = new SpiderDataNode(aPath, null);
				currentNode.add(newChild);
				currentNode = newChild;

			} else {

				// Get child node
				currentNode = (SpiderDataNode) currentNode.getChildAt(childIndex);
			}
		}

		// Add data
		currentNode.setData(data);
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
