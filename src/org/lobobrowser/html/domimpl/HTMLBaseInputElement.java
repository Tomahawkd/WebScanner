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
 * Created on Jan 15, 2006
 */
package org.lobobrowser.html.domimpl;

import org.lobobrowser.html.FormInput;
import org.w3c.dom.Node;
import org.w3c.dom.html2.HTMLFormElement;

import java.util.ArrayList;

public abstract class HTMLBaseInputElement extends HTMLAbstractUIElement {
	public HTMLBaseInputElement(String name) {
		super(name);
	}

	InputContext inputContext;
	private String deferredValue;

	public void setInputContext(InputContext ic) {
		String dv = null;
		synchronized (this) {
			this.inputContext = ic;
			if (ic != null) {
				dv = this.deferredValue;
			}
		}
		if (dv != null) {
			ic.setValue(dv);
		}
	}

	public HTMLFormElement getForm() {
		Node parent = this.getParentNode();
		while (parent != null && !(parent instanceof HTMLFormElement)) {
			parent = parent.getParentNode();
		}
		return (HTMLFormElement) parent;
	}

	public void submitForm(FormInput[] extraFormInputs) {
		HTMLFormElementImpl form = (HTMLFormElementImpl) this.getForm();
		if (form != null) {
			form.submit(extraFormInputs);
		}
	}

	public void resetForm() {
		HTMLFormElement form = this.getForm();
		if (form != null) {
			form.reset();
		}
	}

	public String getAccept() {
		return this.getAttribute("accept");
	}

	public void setAccept(String accept) {
		this.setAttribute("accept", accept);
	}

	public String getAlign() {
		return this.getAttribute("align");
	}

	public void setAlign(String align) {
		this.setAttribute("align", align);
	}

	public boolean getDisabled() {
		InputContext ic = this.inputContext;
		return ic != null && ic.getDisabled();
	}

	public void setDisabled(boolean disabled) {
		InputContext ic = this.inputContext;
		if (ic != null) {
			ic.setDisabled(disabled);
		}
	}

	public String getName() {
		return this.getAttribute("name");
	}

	public void setName(String name) {
		this.setAttribute("name", name);
	}

	public String getValue() {
		InputContext ic = this.inputContext;
		if (ic != null) {
			//Note: Per HTML Spec, setValue does not set attribute.
			return ic.getValue();
		} else {
			String dv = this.deferredValue;
			if (dv != null) {
				return dv;
			} else {
				String val = this.getAttribute("value");
				return val == null ? "" : val;
			}
		}
	}

	java.io.File getFileValue() {
		InputContext ic = this.inputContext;
		if (ic != null) {
			return ic.getFileValue();
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		InputContext ic;
		synchronized (this) {
			ic = this.inputContext;
			if (ic == null) {
				this.deferredValue = value;
			}
		}
		if (ic != null) {
			ic.setValue(value);
		}
	}

	public void blur() {
		InputContext ic = this.inputContext;
		if (ic != null) {
			ic.blur();
		}
	}

	public void focus() {
		InputContext ic = this.inputContext;
		if (ic != null) {
			ic.focus();
		}
	}

	public void select() {
		InputContext ic = this.inputContext;
		if (ic != null) {
			ic.select();
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.domimpl.HTMLElementImpl#assignAttributeField(java.lang.String, java.lang.String)
	 */
	protected void assignAttributeField(String normalName, String value) {
		if ("value".equals(normalName)) {
			InputContext ic = this.inputContext;
			if (ic != null) {
				ic.setValue(value);
			}
		} else if ("src".equals(normalName)) {
			this.loadImage(value);
		} else {
			super.assignAttributeField(normalName, value);
		}
	}

	private java.awt.Image image = null;
	private String imageSrc;

	private void loadImage(String src) {
		HTMLDocumentImpl document = (HTMLDocumentImpl) this.document;
		if (document != null) {
			synchronized (this.imageListeners) {
				this.imageSrc = src;
				this.image = null;
			}
			if (src != null) {
				document.loadImage(src, new LocalImageListener(src));
			}
		}
	}

	public final java.awt.Image getImage() {
		synchronized (this.imageListeners) {
			return this.image;
		}
	}

	private final ArrayList<ImageListener> imageListeners = new ArrayList<>(1);

	/**
	 * Adds a listener of image loading events.
	 * The listener gets called right away if there's already
	 * an image.
	 *
	 * @param listener listener
	 */
	public void addImageListener(ImageListener listener) {
		java.awt.Image currentImage;
		synchronized (this.imageListeners) {
			currentImage = this.image;
			this.imageListeners.add(listener);
		}
		if (currentImage != null) {
			listener.imageLoaded(new ImageEvent(this, currentImage));
		}
	}

	void resetInput() {
		InputContext ic = this.inputContext;
		if (ic != null) {
			ic.resetInput();
		}
	}

	private void dispatchEvent(String expectedImgSrc, ImageEvent event) {
		ImageListener[] listenerArray;
		synchronized (this.imageListeners) {
			if (!expectedImgSrc.equals(this.imageSrc)) {
				return;
			}
			this.image = event.image;
			listenerArray = this.imageListeners.toArray(ImageListener.EMPTY_ARRAY);
		}
		for (ImageListener aListenerArray : listenerArray) {
			aListenerArray.imageLoaded(event);
		}
	}

	private class LocalImageListener implements ImageListener {
		private final String expectedImgSrc;

		LocalImageListener(String imgSrc) {
			this.expectedImgSrc = imgSrc;
		}

		public void imageLoaded(ImageEvent event) {
			dispatchEvent(this.expectedImgSrc, event);
		}
	}

}
