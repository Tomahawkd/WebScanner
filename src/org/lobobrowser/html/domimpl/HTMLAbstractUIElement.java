package org.lobobrowser.html.domimpl;

import java.util.Map;

/**
 * Implements common functionality of most elements.
 */
public class HTMLAbstractUIElement extends HTMLElementImpl {

	public HTMLAbstractUIElement(String name) {
		super(name);
	}

	public void focus() {
		UINode node = this.getUINode();
		if (node != null) {
			node.focus();
		}
	}

	public void blur() {
		UINode node = this.getUINode();
		if (node != null) {
			node.blur();
		}
	}

	private Map functionByAttribute = null;

	protected void assignAttributeField(String normalName, String value) {
		super.assignAttributeField(normalName, value);
		if (normalName.startsWith("on")) {
			synchronized (this) {
				Map fba = this.functionByAttribute;
				if (fba != null) {
					fba.remove(normalName);
				}
			}
		}
	}


	public String getDir() {
		return "";
	}

	public void setDir(String dir) {
	}

	public void setClassName(String className) {
	}
}
