package application.view.contentViewer;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

class TreeViewer extends JScrollPane {

	private JTree contentTree;

	TreeViewer() {
		contentTree = new JTree();
		contentTree.setRootVisible(false);
		contentTree.setEditable(false);
		setViewportView(contentTree);
	}

	void setTreeNode(DefaultMutableTreeNode treeNode) {
		contentTree.setModel(new DefaultTreeModel(treeNode));
	}

	void updateTreeUI() {
		contentTree.updateUI();
	}
}
