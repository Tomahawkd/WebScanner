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

import org.w3c.dom.html2.HTMLScriptElement;

public class HTMLScriptElementImpl extends HTMLElementImpl implements
		HTMLScriptElement {

	HTMLScriptElementImpl(String name) {
		super(name);
	}

	private String text;

	public String getText() {
		String t = this.text;
		if (t == null) {
			return this.getRawInnerText(true);
		} else {
			return t;
		}
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getHtmlFor() {
		return this.getAttribute("htmlFor");
	}

	public void setHtmlFor(String htmlFor) {
		this.setAttribute("htmlFor", htmlFor);
	}

	public String getEvent() {
		return this.getAttribute("event");
	}

	public void setEvent(String event) {
		this.setAttribute("event", event);
	}

	private boolean defer;

	public boolean getDefer() {
		return this.defer;
	}

	public void setDefer(boolean defer) {
		this.defer = defer;
	}

	public String getSrc() {
		return this.getAttribute("src");
	}

	public void setSrc(String src) {
		this.setAttribute("src", src);
	}

	public String getType() {
		return this.getAttribute("type");
	}

	public void setType(String type) {
		this.setAttribute("type", type);
	}

	protected void appendInnerTextImpl(StringBuffer buffer) {
	}
}
