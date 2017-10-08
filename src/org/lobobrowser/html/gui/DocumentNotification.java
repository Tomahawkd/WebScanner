package org.lobobrowser.html.gui;

import org.lobobrowser.html.domimpl.NodeImpl;

class DocumentNotification {
	static final int LOOK = 0;
	static final int POSITION = 1;
	static final int SIZE = 2;
	static final int GENERIC = 3;

	public final int type;
	public final NodeImpl node;

	DocumentNotification(int type, NodeImpl node) {
		this.type = type;
		this.node = node;
	}

	public String toString() {
		return "DocumentNotification[type=" + this.type + ",node=" + this.node + "]";
	}
}
