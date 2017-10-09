package org.lobobrowser.html.domimpl;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.html2.HTMLOptionElement;
import org.w3c.dom.html2.HTMLOptionsCollection;

public class HTMLOptionsCollectionImpl extends DescendentHTMLCollection implements HTMLOptionsCollection {
	private static final NodeFilter OPTION_FILTER = new OptionFilter();

	HTMLOptionsCollectionImpl(HTMLElementImpl selectElement) {
		super(selectElement, OPTION_FILTER, selectElement.treeLock, false);
	}

	public void setLength(int length) throws DOMException {
		throw new UnsupportedOperationException();
	}

	private static class OptionFilter implements NodeFilter {
		public boolean accept(Node node) {
			return node instanceof HTMLOptionElement;
		}
	}
}
