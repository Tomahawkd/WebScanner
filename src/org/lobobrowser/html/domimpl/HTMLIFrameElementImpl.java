package org.lobobrowser.html.domimpl;

import org.lobobrowser.html.BrowserFrame;
import org.lobobrowser.html.style.IFrameRenderState;
import org.lobobrowser.html.style.RenderState;
import org.w3c.dom.html2.HTMLIFrameElement;

public class HTMLIFrameElementImpl extends HTMLAbstractUIElement implements
		HTMLIFrameElement, FrameNode {
	private volatile BrowserFrame browserFrame;

	HTMLIFrameElementImpl(String name) {
		super(name);
	}

	public void setBrowserFrame(BrowserFrame frame) {
		this.browserFrame = frame;
		if (frame != null) {
			String src = this.getAttribute("src");
			if (src != null) {
				try {
					frame.loadURL(this.getFullURL(src));
				} catch (java.net.MalformedURLException ignored) {
				}
			}
		}
	}

	public BrowserFrame getBrowserFrame() {
		return this.browserFrame;
	}

	public String getAlign() {
		return this.getAttribute("align");
	}

	public String getHeight() {
		return this.getAttribute("height");
	}

	public String getName() {
		return this.getAttribute("name");
	}

	public String getSrc() {
		return this.getAttribute("src");
	}

	public String getWidth() {
		return this.getAttribute("width");
	}

	public void setAlign(String align) {
		this.setAttribute("align", align);
	}

	public void setHeight(String height) {
		this.setAttribute("height", height);
	}

	public void setName(String name) {
		this.setAttribute("name", name);
	}

	public void setSrc(String src) {
		this.setAttribute("src", src);
	}

	public void setWidth(String width) {
		this.setAttribute("width", width);
	}

	protected void assignAttributeField(String normalName, String value) {
		if ("src".equals(normalName)) {
			BrowserFrame frame = this.browserFrame;
			if (frame != null) {
				try {
					frame.loadURL(this.getFullURL(value));
				} catch (java.net.MalformedURLException ignored) {
				}
			}
		} else {
			super.assignAttributeField(normalName, value);
		}
	}

	protected RenderState createRenderState(RenderState prevRenderState) {
		return new IFrameRenderState(prevRenderState, this);
	}
}
