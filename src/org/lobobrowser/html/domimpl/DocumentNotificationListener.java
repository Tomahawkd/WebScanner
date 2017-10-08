package org.lobobrowser.html.domimpl;

/**
 * A listener of document changes.
 */
public interface DocumentNotificationListener {
	/**
	 * Called if a property related to the node's
	 * size has changed.
	 *
	 * @param node node
	 */
	void sizeInvalidated(NodeImpl node);

	/**
	 * Called if something such as a color or
	 * decoration has changed. This would be
	 * something which does not affect the
	 * rendered size.
	 *
	 * @param node node
	 */
	void lookInvalidated(NodeImpl node);

	/**
	 * Changed if the position of the node in a
	 * parent has changed.
	 *
	 * @param node node
	 */
	void positionInvalidated(NodeImpl node);

	/**
	 * This is called when the node has changed, but
	 * it is unclear if it's a size change or a look
	 * change. Typically, a node attribute has changed,
	 * but the set of child nodes has not changed.
	 *
	 * @param node node
	 */
	void invalidated(NodeImpl node);

	/**
	 * Called when the node (with all its contents) is first
	 * created by the parser.
	 *
	 * @param node node
	 */
	void nodeLoaded(NodeImpl node);

	/**
	 * The children of the node might have changed.
	 *
	 * @param node node
	 */
	void structureInvalidated(NodeImpl node);

	/**
	 * Called when a external script (a SCRIPT tag with a src attribute)
	 * is about to start loading.
	 *
	 * @param node node
	 */
	void externalScriptLoading(NodeImpl node);


	/**
	 * This is called when the whole document
	 * is potentially invalid, e.g. when a new
	 * style sheet has been added.
	 */
	void allInvalidated();
}
