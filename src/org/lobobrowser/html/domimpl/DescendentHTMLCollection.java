/*
    GNU LESSER GENERAL PUBLIC LICENSE
    Copyright (C) 2006 The Lobo Project

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

    Contact info: lobochief@users.sourceforge.net
*/
/*
 * Created on Dec 3, 2005
 */
package org.lobobrowser.html.domimpl;

import org.lobobrowser.util.Nodes;
import org.w3c.dom.Node;
import org.w3c.dom.html2.HTMLCollection;

import java.lang.ref.WeakReference;
import java.util.*;

public class DescendentHTMLCollection implements HTMLCollection {
	private final NodeImpl rootNode;
	private final NodeFilter nodeFilter;
	private final Object treeLock;
	private final boolean nestIntoMatchingNodes;

	DescendentHTMLCollection(NodeImpl node, NodeFilter filter, Object treeLock) {
		this(node, filter, treeLock, true);
	}

	/**
	 * @param node node
	 * @param filter filter
	 */
	DescendentHTMLCollection(NodeImpl node, NodeFilter filter, Object treeLock, boolean nestMatchingNodes) {
		rootNode = node;
		nodeFilter = filter;
		this.treeLock = treeLock;
		this.nestIntoMatchingNodes = nestMatchingNodes;
		HTMLDocumentImpl document = (HTMLDocumentImpl) node.getOwnerDocument();
		document.addDocumentNotificationListener(new LocalNotificationListener(document, this));
	}

	private Map<String, ElementImpl> itemsByName = null;
	private List itemsByIndex = null;

	private void ensurePopulatedImpl() {
		if (this.itemsByName == null) {
			ArrayList descendents = this.rootNode.getDescendents(this.nodeFilter, this.nestIntoMatchingNodes);
			this.itemsByIndex = descendents == null ? Collections.EMPTY_LIST : descendents;
			int size = descendents == null ? 0 : descendents.size();
			Map<String, ElementImpl> itemsByName = new HashMap<>(size * 3 / 2);
			this.itemsByName = itemsByName;
			for (int i = 0; i < size; i++) {
				Object descNode = descendents.get(i);
				if (descNode instanceof ElementImpl) {
					ElementImpl element = (ElementImpl) descNode;
					String id = element.getId();
					if (id != null && id.length() != 0) {
						itemsByName.put(id, element);
					}
					String name = element.getAttribute("name");
					if (name != null && name.length() != 0 && !name.equals(id)) {
						itemsByName.put(name, element);
					}
				}
			}
		}
	}

	private void invalidate() {
		synchronized (this.treeLock) {
			this.itemsByName = null;
			this.itemsByIndex = null;
		}
	}

	private boolean isValid() {
		synchronized (this.treeLock) {
			return this.itemsByName != null && this.itemsByIndex != null;
		}
	}

	public int getLength() {
		synchronized (this.treeLock) {
			this.ensurePopulatedImpl();
			return this.itemsByIndex.size();
		}
	}

	public Node item(int index) {
		synchronized (this.treeLock) {
			this.ensurePopulatedImpl();
			try {
				return (Node) this.itemsByIndex.get(index);
			} catch (java.lang.IndexOutOfBoundsException iob) {
				return null;
			}
		}
	}

	public Node namedItem(String name) {
		synchronized (this.treeLock) {
			this.ensurePopulatedImpl();
			return this.itemsByName.get(name);
		}
	}

	public int indexOf(Node node) {
		synchronized (this.treeLock) {
			this.ensurePopulatedImpl();
			return this.itemsByIndex.indexOf(node);
		}
	}

	private static class LocalNotificationListener extends DocumentNotificationAdapter {
		// Needs to be a static class with a weak reference to
		// the collection object.
		private final HTMLDocumentImpl document;
		private final WeakReference<DescendentHTMLCollection> collectionRef;

		LocalNotificationListener(final HTMLDocumentImpl document, final DescendentHTMLCollection collection) {
			super();
			this.document = document;
			this.collectionRef = new WeakReference<>(collection);
		}

		public void structureInvalidated(NodeImpl node) {
			DescendentHTMLCollection collection = this.collectionRef.get();
			if (collection == null) {
				// Gone!
				this.document.removeDocumentNotificationListener(this);
				return;
			}
			if (collection.isValid()) {
				if (Nodes.isSameOrAncestorOf(collection.rootNode, node)) {
					collection.invalidate();
				}
			}
		}

		public void nodeLoaded(NodeImpl node) {
			this.structureInvalidated(node);
		}
	}

}
