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
 * Created on Dec 3, 2005
 */
package org.lobobrowser.html.domimpl;

import org.lobobrowser.html.style.*;
import org.w3c.dom.html2.*;

public class HTMLTableElementImpl extends HTMLAbstractUIElement implements
		HTMLTableElement {

	HTMLTableElementImpl(String name) {
		super(name);
	}

	public HTMLCollection getRows() {
		return new DescendentHTMLCollection(this, new ElementFilter("TR"), this.treeLock, false);
	}

	public String getAlign() {
		return this.getAttribute("align");
	}

	public void setAlign(String align) {
		this.setAttribute("align", align);
	}

	public String getBgColor() {
		return this.getAttribute("bgcolor");
	}

	public String getBorder() {
		return this.getAttribute("border");
	}

	public void setBorder(String border) {
		this.setAttribute("border", border);
	}

	public String getFrame() {
		return this.getAttribute("frame");
	}

	public void setFrame(String frame) {
		this.setAttribute("frame", frame);
	}

	public String getRules() {
		return this.getAttribute("rules");
	}

	public void setRules(String rules) {
		this.setAttribute("rules", rules);
	}

	public String getWidth() {
		return this.getAttribute("width");
	}

	public void setWidth(String width) {
		this.setAttribute("width", width);
	}

	protected RenderState createRenderState(RenderState prevRenderState) {
		return new TableRenderState(prevRenderState, this);
	}
}
