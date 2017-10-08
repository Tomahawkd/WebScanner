package org.lobobrowser.html.renderer;

import org.lobobrowser.html.FormInput;
import org.lobobrowser.html.HtmlRendererContext;
import org.lobobrowser.html.domimpl.*;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

class HtmlController {
	private static final HtmlController instance = new HtmlController();

	static HtmlController getInstance() {
		return instance;
	}

	/**
	 * @return True to propagate further and false if the event was consumed.
	 */
	public boolean onEnterPressed(ModelNode node, InputEvent event) {
		if (node instanceof HTMLInputElementImpl) {
			HTMLInputElementImpl hie = (HTMLInputElementImpl) node;
			if (hie.isSubmittableWithEnterKey()) {
				hie.submitForm(null);
				return false;
			}
		}
		// No propagation
		return false;
	}

	/**
	 * @return True to propagate further and false if the event was consumed.
	 */
	public boolean onMouseClick(ModelNode node, MouseEvent event, int x, int y) {
		if (node instanceof HTMLAbstractUIElement) {
			HTMLAbstractUIElement uiElement = (HTMLAbstractUIElement) node;
			HtmlRendererContext rcontext = uiElement.getHtmlRendererContext();
			if (rcontext != null) {
				if (!rcontext.onMouseClick(uiElement, event)) {
					return false;
				}
			}
		}
		if (node instanceof HTMLLinkElementImpl) {
			((HTMLLinkElementImpl) node).navigate();
			return false;
		} else if (node instanceof HTMLButtonElementImpl) {
			HTMLButtonElementImpl button = (HTMLButtonElementImpl) node;
			String rawType = button.getAttribute("type");
			String type;
			if (rawType == null) {
				type = "submit";
			} else {
				type = rawType.trim().toLowerCase();
			}
			if ("submit".equals(type)) {
				FormInput[] formInputs;
				String name = button.getName();
				if (name == null) {
					formInputs = null;
				} else {
					formInputs = new FormInput[]{new FormInput(name, button.getValue())};
				}
				button.submitForm(formInputs);
			} else if ("reset".equals(type)) {
				button.resetForm();
			} else {
				// NOP for "button"!
			}
			return false;
		}
		ModelNode parent = node.getParentModelNode();
		if (parent == null) {
			return true;
		}
		return this.onMouseClick(parent, event, x, y);
	}

	public boolean onContextMenu(ModelNode node, MouseEvent event, int x, int y) {
		if (node instanceof HTMLAbstractUIElement) {
			HTMLAbstractUIElement uiElement = (HTMLAbstractUIElement) node;
			HtmlRendererContext rcontext = uiElement.getHtmlRendererContext();
			if (rcontext != null) {
				// Needs to be done after Javascript, so the script
				// is able to prevent it.
				if (!rcontext.onContextMenu(uiElement, event)) {
					return false;
				}
			}
		}
		ModelNode parent = node.getParentModelNode();
		if (parent == null) {
			return true;
		}
		return this.onContextMenu(parent, event, x, y);
	}

	public void onMouseOver(ModelNode node, MouseEvent event, int x, int y, ModelNode limit) {
		while (node != null) {
			if (node == limit) {
				break;
			}
			if (node instanceof HTMLAbstractUIElement) {
				HTMLAbstractUIElement uiElement = (HTMLAbstractUIElement) node;
				uiElement.setMouseOver(true);
				HtmlRendererContext rcontext = uiElement.getHtmlRendererContext();
				if (rcontext != null) {
					rcontext.onMouseOver(uiElement, event);
				}
			}
			node = node.getParentModelNode();
		}
	}

	public void onMouseOut(ModelNode node, MouseEvent event, int x, int y, ModelNode limit) {
		while (node != null) {
			if (node == limit) {
				break;
			}
			if (node instanceof HTMLAbstractUIElement) {
				HTMLAbstractUIElement uiElement = (HTMLAbstractUIElement) node;
				uiElement.setMouseOver(false);
				HtmlRendererContext rcontext = uiElement.getHtmlRendererContext();
				if (rcontext != null) {
					rcontext.onMouseOut(uiElement, event);
				}
			}
			node = node.getParentModelNode();
		}
	}

	/**
	 * @return True to propagate further, false if consumed.
	 */
	public boolean onDoubleClick(ModelNode node, MouseEvent event, int x, int y) {
		if (node instanceof HTMLAbstractUIElement) {
			HTMLAbstractUIElement uiElement = (HTMLAbstractUIElement) node;
			HtmlRendererContext rcontext = uiElement.getHtmlRendererContext();
			if (rcontext != null) {
				if (!rcontext.onDoubleClick(uiElement, event)) {
					return false;
				}
			}
		}
		ModelNode parent = node.getParentModelNode();
		if (parent == null) {
			return true;
		}
		return this.onDoubleClick(parent, event, x, y);
	}

	/**
	 * @return True to propagate further, false if consumed.
	 */
	public boolean onMouseDisarmed(ModelNode node, MouseEvent event) {
		if (node instanceof HTMLLinkElementImpl) {
			((HTMLLinkElementImpl) node).getCurrentStyle().setOverlayColor(null);
			return false;
		}
		ModelNode parent = node.getParentModelNode();
		if (parent == null) {
			return true;
		}
		return this.onMouseDisarmed(parent, event);
	}

	/**
	 * @return True to propagate further, false if consumed.
	 */
	public boolean onMouseDown(ModelNode node, MouseEvent event, int x, int y) {
		if (node instanceof HTMLLinkElementImpl) {
			((HTMLLinkElementImpl) node).getCurrentStyle().setOverlayColor("#9090FF80");
			return false;
		}
		ModelNode parent = node.getParentModelNode();
		if (parent == null) {
			return true;
		}
		return this.onMouseDown(parent, event, x, y);
	}

	/**
	 * @return True to propagate further, false if consumed.
	 */
	public boolean onMouseUp(ModelNode node, MouseEvent event, int x, int y) {
		if (node instanceof HTMLLinkElementImpl) {
			((HTMLLinkElementImpl) node).getCurrentStyle().setOverlayColor(null);
			return false;
		}
		ModelNode parent = node.getParentModelNode();
		if (parent == null) {
			return true;
		}
		return this.onMouseUp(parent, event, x, y);
	}

	/**
	 * @param node The node generating the event.
	 * @param x    For images only, x coordinate of mouse click.
	 * @param y    For images only, y coordinate of mouse click.
	 * @return True to propagate further, false if consumed.
	 */
	public boolean onPressed(ModelNode node, InputEvent event, int x, int y) {
		if (node instanceof HTMLInputElementImpl) {
			HTMLInputElementImpl hie = (HTMLInputElementImpl) node;
			if (hie.isSubmitInput()) {
				FormInput[] formInputs;
				String name = hie.getName();
				if (name == null) {
					formInputs = null;
				} else {
					formInputs = new FormInput[]{new FormInput(name, hie.getValue())};
				}
				hie.submitForm(formInputs);
			} else if (hie.isImageInput()) {
				String name = hie.getName();
				String prefix = name == null ? "" : name + ".";
				FormInput[] extraFormInputs = new FormInput[]{
						new FormInput(prefix + "x", String.valueOf(x)),
						new FormInput(prefix + "y", String.valueOf(y))
				};
				hie.submitForm(extraFormInputs);
			} else if (hie.isResetInput()) {
				hie.resetForm();
			}
		}
		// No propagate
		return false;
	}

	public boolean onChange(ModelNode node) {
		return false;
	}
}
