package org.lobobrowser.html.domimpl;

import org.w3c.dom.html2.HTMLHRElement;

public class HTMLHRElementImpl extends HTMLAbstractUIElement implements HTMLHRElement {
	HTMLHRElementImpl(String name) {
		super(name);
	}

	public String getAlign() {
		return this.getAttribute("align");
	}

	public String getSize() {
		return this.getAttribute("size");
	}

	public String getWidth() {
		return this.getAttribute("width");
	}

	public void setAlign(String align) {
		this.setAttribute("align", align);
	}

	public void setSize(String size) {
		this.setAttribute("size", size);
	}

	public void setWidth(String width) {
		this.setAttribute("width", width);
	}
}
