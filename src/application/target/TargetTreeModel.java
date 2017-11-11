package application.target;

import application.utility.net.Context;
import application.view.frame.target.TargetPanelController;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.net.URL;

public class TargetTreeModel {

	private DataNodeImpl root;

	TargetTreeModel() {
		root = new DataNodeImpl("", "", "", "", "", "", "");
	}

	public DataNode getRoot() {
		return root;
	}

	public synchronized void add(URL path,
	                             String host,
	                             String method,
	                             String urlPath,
	                             String status,
	                             String type,
	                             String length) {
		DataNodeImpl currentNode = root;

		// Loop path array to locate the file
		for (String aPath : toPathArray(path)) {

			// Check if the node is already exist
			int childIndex = currentNode.getIndex(aPath);
			if (childIndex == -1) {

				// Create new node
				DataNodeImpl newChild = new DataNodeImpl(aPath,
						"", "", "", "", "", "");
				currentNode.add(newChild);
				currentNode = newChild;

			} else {

				// Get child node
				currentNode = (DataNodeImpl) currentNode.getChildAt(childIndex);
			}
		}

		// Add data
		currentNode.setData(host, method, urlPath, status, type, length);
		TargetPanelController.getInstance().updateMapData();
	}

	private String[] toPathArray(URL url) {

		String[] path = url.getPath().substring(1).split("/");
		path[path.length - 1] += (url.getQuery() != null ? "?" + url.getQuery() : "");

		String[] pathArr = new String[path.length + 1];
		pathArr[0] = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();

		System.arraycopy(path, 0, pathArr, 1, pathArr.length - 1);
		return pathArr;
	}

	class DataNodeImpl extends DefaultMutableTreeNode implements DataNode {

		private String name;
		private Context data;
		private String host;
		private String method;
		private String path;
		private String status;
		private String type;
		private String length;

		private DataNodeImpl(String name,
		                     String host,
		                     String method,
		                     String path,
		                     String status,
		                     String type,
		                     String length) {
			super(name);
			this.name = name;
			this.host = host;
			this.method = method;
			this.path = path;
			this.status = status;
			this.type = type;
			this.length = length;
		}

		private void setData(String host,
		                     String method,
		                     String path,
		                     String status,
		                     String type,
		                     String length) {
			this.host = host;
			this.method = method;
			this.path = path;
			this.status = status;
			this.type = type;
			this.length = length;
		}

		public String getName() {
			return name;
		}


		public String getHost() {
			return host;
		}

		public String getMethod() {
			return method;
		}

		public String getURLPath() {
			return path;
		}

		public String getStatusCode() {
			return status;
		}

		public String getMIMEType() {
			return type;
		}

		public String getLength() {
			return length;
		}

		private int getIndex(String name) {

			if (this.children != null) {

				// Point to the first child
				int index = 0;
				for (TreeNode child : this.children) {
					try {

						DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) child;

						// Check the child name
						if (childNode.getUserObject().toString().equals(name)) {
							return index;
						}

						// Point to next child
						index++;

					} catch (ClassCastException ignored) {
					}
				}
			}
			return -1;
		}
	}
}