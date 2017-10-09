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
 * Created on Oct 8, 2005
 */
package org.lobobrowser.html.domimpl;

import org.lobobrowser.html.style.BodyRenderState;
import org.lobobrowser.html.style.RenderState;
import org.w3c.dom.Document;
import org.w3c.dom.html2.HTMLBodyElement;
import org.w3c.dom.html2.HTMLDocument;

public class HTMLBodyElementImpl extends HTMLAbstractUIElement implements HTMLBodyElement {
	HTMLBodyElementImpl(String name) {
		super(name);
	}

	void setOwnerDocument(Document value, boolean deep) {
		super.setOwnerDocument(value, deep);
		if (value instanceof HTMLDocument) {
			((HTMLDocument) value).setBody(this);
		}
	}

	void setOwnerDocument(Document value) {
		super.setOwnerDocument(value);
		if (value instanceof HTMLDocument) {
			((HTMLDocument) value).setBody(this);
		}
	}

	public String getBackground() {
		return this.getAttribute("background");
	}

	public void setBackground(String background) {
		this.setAttribute("background", background);
	}

	public String getLink() {
		return this.getAttribute("link");
	}

	public void setLink(String link) {
		this.setAttribute("link", link);
	}

	public String getText() {
		return this.getAttribute("text");
	}

	public void setText(String text) {
		this.setAttribute("text", text);
	}

	public String getVLink() {
		return this.getAttribute("vlink");
	}

	protected RenderState createRenderState(RenderState prevRenderState) {
		return new BodyRenderState(prevRenderState, this);
	}

	protected void assignAttributeField(String normalName, String value) {
		if (!"onload".equals(normalName)) {
			super.assignAttributeField(normalName, value);
		}
	}
}
