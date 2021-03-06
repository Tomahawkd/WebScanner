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
package org.lobobrowser.html.domimpl;

import org.lobobrowser.html.style.AbstractCSS2Properties;
import org.lobobrowser.html.style.ComputedCSS2Properties;
import org.lobobrowser.html.style.HeadingRenderState;
import org.lobobrowser.html.style.RenderState;
import org.w3c.dom.html2.HTMLHeadingElement;

public class HTMLHeadingElementImpl extends HTMLAbstractUIElement implements HTMLHeadingElement {

	HTMLHeadingElementImpl(String name) {
		super(name);
	}

	public String getAlign() {
		return this.getAttribute("align");
	}

	public void setAlign(String align) {
		this.setAttribute("align", align);
	}

	private String getHeadingFontSizeText() {
		String tagName = this.getTagName();
		try {
			int lastCharValue = tagName.charAt(1) - '0';
			switch (lastCharValue) {
				case 1:
					return "24pt";
				case 2:
					return "18pt";
				case 3:
					return "13.55pt";
				case 4:
					return "12pt";
				case 5:
					return "10pt";
				case 6:
					return "7.55pt";
			}
		} catch (Exception ignored) {
		}
		return "14px";
	}

	protected RenderState createRenderState(RenderState prevRenderState) {
		return new HeadingRenderState(prevRenderState, this);
	}

	protected AbstractCSS2Properties createDefaultStyleSheet() {
		ComputedCSS2Properties css = new ComputedCSS2Properties(this);
		css.internalSetLC("font-size", this.getHeadingFontSizeText());
		css.internalSetLC("font-weight", "bolder");
		return css;
	}
}
