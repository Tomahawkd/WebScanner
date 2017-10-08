package org.lobobrowser.html.domimpl;

import org.lobobrowser.html.UserAgentContext;
import org.mozilla.javascript.Function;

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

	Function getEventFunction(Function varValue, String attributeName) {
		if (varValue != null) {
			return varValue;
		}
		String normalAttributeName = this.normalizeAttributeName(attributeName);
		synchronized (this) {
			Map fba = this.functionByAttribute;
			Function f = fba == null ? null : (Function) fba.get(normalAttributeName);
			if (f != null) {
				return f;
			}
			UserAgentContext uac = this.getUserAgentContext();
			if (uac == null) {
				throw new IllegalStateException("No user agent context.");
			}
			return null;
		}
	}

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
}
