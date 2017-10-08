package org.lobobrowser.html.domimpl;

import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.js.Executor;
import org.lobobrowser.js.JavaScript;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Implements common functionality of most elements.
 */
public class HTMLAbstractUIElement extends HTMLElementImpl {
	private Function onfocus, onblur, onclick, ondblclick, onmousedown, onmouseup, onmouseover, onmousemove, onmouseout, onkeypress, onkeydown, onkeyup, oncontextmenu;

	public HTMLAbstractUIElement(String name) {
		super(name);
	}

	public Function getOnblur() {
		return this.getEventFunction(onblur, "onblur");
	}

	public void setOnblur(Function onblur) {
		this.onblur = onblur;
	}

	public Function getOnclick() {
		return this.getEventFunction(onclick, "onclick");
	}

	public void setOnclick(Function onclick) {
		this.onclick = onclick;
	}

	public Function getOndblclick() {
		return this.getEventFunction(ondblclick, "ondblclick");
	}

	public void setOndblclick(Function ondblclick) {
		this.ondblclick = ondblclick;
	}

	public Function getOnfocus() {
		return this.getEventFunction(onfocus, "onfocus");
	}

	public void setOnfocus(Function onfocus) {
		this.onfocus = onfocus;
	}

	public Function getOnkeydown() {
		return this.getEventFunction(onkeydown, "onkeydown");
	}

	public void setOnkeydown(Function onkeydown) {
		this.onkeydown = onkeydown;
	}

	public Function getOnkeypress() {
		return this.getEventFunction(onkeypress, "onkeypress");
	}

	public void setOnkeypress(Function onkeypress) {
		this.onkeypress = onkeypress;
	}

	public Function getOnkeyup() {
		return this.getEventFunction(onkeyup, "onkeyup");
	}

	public void setOnkeyup(Function onkeyup) {
		this.onkeyup = onkeyup;
	}

	public Function getOnmousedown() {
		return this.getEventFunction(onmousedown, "onmousedown");
	}

	public void setOnmousedown(Function onmousedown) {
		this.onmousedown = onmousedown;
	}

	public Function getOnmousemove() {
		return this.getEventFunction(onmousemove, "onmousemove");
	}

	public void setOnmousemove(Function onmousemove) {
		this.onmousemove = onmousemove;
	}

	public Function getOnmouseout() {
		return this.getEventFunction(onmouseout, "onmouseout");
	}

	public void setOnmouseout(Function onmouseout) {
		this.onmouseout = onmouseout;
	}

	public Function getOnmouseover() {
		return this.getEventFunction(onmouseover, "onmouseover");
	}

	public void setOnmouseover(Function onmouseover) {
		this.onmouseover = onmouseover;
	}

	public Function getOnmouseup() {
		return this.getEventFunction(onmouseup, "onmouseup");
	}

	public void setOnmouseup(Function onmouseup) {
		this.onmouseup = onmouseup;
	}

	public Function getOncontextmenu() {
		return this.getEventFunction(oncontextmenu, "oncontextmenu");
	}

	public void setOncontextmenu(Function oncontextmenu) {
		this.oncontextmenu = oncontextmenu;
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

	protected Function getEventFunction(Function varValue, String attributeName) {
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
