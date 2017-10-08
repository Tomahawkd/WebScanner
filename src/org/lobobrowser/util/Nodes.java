package org.lobobrowser.util;

import org.w3c.dom.Node;

public class Nodes {

	public static boolean isSameOrAncestorOf(Node node, Node child) {
		if (child.isSameNode(node)) {
			return true;
		}
		Node parent = child.getParentNode();
		return parent != null && isSameOrAncestorOf(node, parent);
	}
}
