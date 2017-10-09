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

	protected void appendInnerTextImpl(StringBuffer buffer) {
		int length = buffer.length();
		int lineBreaks;
		if (length == 0) {
			lineBreaks = 2;
		} else {
			int start = length - 4;
			if (start < 0) {
				start = 0;
			}
			lineBreaks = 0;
			for (int i = start; i < length; i++) {
				char ch = buffer.charAt(i);
				if (ch == '\n') {
					lineBreaks++;
				}
			}
		}
		for (int i = 0; i < 2 - lineBreaks; i++) {
			buffer.append("\r\n");
		}
		super.appendInnerTextImpl(buffer);
		buffer.append("\r\n\r\n");
	}

	public String getDir() {
		return "";
	}

	public void setDir(String dir) {
	}

	public void setClassName(String className) {
	}
}
