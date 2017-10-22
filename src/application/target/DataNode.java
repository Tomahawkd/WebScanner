package application.target;

import application.utility.net.Context;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class DataNode extends DefaultMutableTreeNode {


	private Context data;

	DataNode(String name, Context data) {
		super(name);
		this.data = data;
	}

	public Context getData() {
		return data;
	}

	void setData(Context data) {
		this.data = data;
	}

	public String getName() {
		return this.userObject.toString();
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
