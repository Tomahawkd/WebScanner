package org.lobobrowser.html.domimpl;

import org.lobobrowser.html.style.ParagraphRenderState;
import org.lobobrowser.html.style.RenderState;
import org.w3c.dom.html2.HTMLParagraphElement;

public class HTMLPElementImpl extends HTMLAbstractUIElement implements HTMLParagraphElement {
	HTMLPElementImpl(String name) {
		super(name);
	}

	public String getAlign() {
		return this.getAttribute("align");
	}

	public void setAlign(String align) {
		this.setAttribute("align", align);
	}

	protected RenderState createRenderState(RenderState prevRenderState) {
		return new ParagraphRenderState(prevRenderState, this);
	}
}
