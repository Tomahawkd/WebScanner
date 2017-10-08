package application.view.contentViewer;


import javax.swing.tree.DefaultMutableTreeNode;

public class ExtendedViewer extends Viewer {

	private TreeViewer treeViewer;

	ExtendedViewer(String title) {
		super();

		treeViewer = new TreeViewer();
		addTab(title, treeViewer);
	}

	public void setTreeNode(DefaultMutableTreeNode treeNode) {
		treeViewer.setTreeNode(treeNode);
	}

	@Override
	public void updateViewerData() {
		super.updateViewerData();
		treeViewer.updateTreeUI();
	}
}
