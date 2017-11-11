package application.target;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

public interface DataNode extends MutableTreeNode {

	String getName();

	String getHost();

	String getMethod();

	String getURLPath();

	String getStatusCode();

	String getMIMEType();

	String getLength();

	int getLeafCount();

	DefaultMutableTreeNode getNextLeaf();
}
