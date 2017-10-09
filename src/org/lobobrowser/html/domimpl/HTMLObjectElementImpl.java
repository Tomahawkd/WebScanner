package org.lobobrowser.html.domimpl;

import org.w3c.dom.html2.HTMLFormElement;
import org.w3c.dom.html2.HTMLObjectElement;

public class HTMLObjectElementImpl extends HTMLAbstractUIElement implements
		HTMLObjectElement {
	HTMLObjectElementImpl(String name) {
		super(name);
	}

	public String getAlign() {
		return this.getAttribute("align");
	}

	public String getCode() {
		return this.getAttribute("code");
	}

	public String getHeight() {
		return this.getAttribute("height");
	}

	public String getName() {
		return this.getAttribute("name");
	}

	public String getObject() {
		return this.getAttribute("object");
	}

	public String getWidth() {
		return this.getAttribute("width");
	}

	public void setAlign(String align) {
		this.setAttribute("align", align);
	}

	public void setCode(String code) {
		this.setAttribute("code", code);
	}

	public void setHeight(String height) {
		this.setAttribute("height", height);
	}

	public void setName(String name) {
		this.setAttribute("name", name);
	}

	public void setObject(String object) {
		this.setAttribute("object", object);
	}

	public void setWidth(String width) {
		this.setAttribute("width", width);
	}

	public String getBorder() {
		return this.getAttribute("border");
	}

	public String getData() {
		return this.getAttribute("data");
	}

	public HTMLFormElement getForm() {
		return (HTMLFormElement) this.getAncestorForJavaClass(HTMLFormElement.class);
	}

	public String getType() {
		return this.getAttribute("type");
	}

	public void setBorder(String border) {
		this.setAttribute("border", border);
	}

	public void setData(String data) {
		this.setAttribute("data", data);
	}

	public void setType(String type) {
		this.setAttribute("type", type);
	}

}
