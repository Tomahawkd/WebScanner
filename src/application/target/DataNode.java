package application.target;

import application.utility.net.Context;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

public interface DataNode extends MutableTreeNode {

	String getName();

	Context getContext();

	int getLeafCount();

	DefaultMutableTreeNode getNextLeaf();
}
