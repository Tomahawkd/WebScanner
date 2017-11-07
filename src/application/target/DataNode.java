package application.target;

import application.utility.net.Context;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class DataNode extends DefaultMutableTreeNode {

	private String name;
	private String method;
	private Context data;

	DataNode(String name, String method, Context data) {
		super(name);
		this.name = name;
		this.method = method;
		this.data = data;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Context getContext() {
		return data;
	}

	void setData(Context data) {
		this.data = data;
	}

	public String getName() {
		return name;
	}

	int getIndex(String name) {

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
