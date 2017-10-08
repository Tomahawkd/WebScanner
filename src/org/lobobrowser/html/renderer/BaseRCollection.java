package org.lobobrowser.html.renderer;

import org.lobobrowser.html.domimpl.ModelNode;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Iterator;

abstract class BaseRCollection extends BaseBoundableRenderable implements RCollection {

	BaseRCollection(RenderableContainer container, ModelNode modelNode) {
		super(container, modelNode);
	}

	public void focus() {
		this.container.focus();
	}

	public void blur() {
		RCollection parent = this.parent;
		if (parent != null) {
			parent.focus();
		}
	}

	/**
	 * Updates bounds of all descendent's GUI components,
	 * based on root bounds.
	 */
	public void updateWidgetBounds(int guiX, int guiY) {
		Iterator i = this.getRenderables();
		if (i != null) {
			while (i.hasNext()) {
				Object r = i.next();
				if (r instanceof RCollection) {
					// RUIControl is a RCollection too.
					RCollection rc = (RCollection) r;
					rc.updateWidgetBounds(guiX + rc.getX(), guiY + rc.getY());
				}
			}
		}
	}

	private boolean checkStartSelection(Rectangle bounds, Point selectionPoint) {
		return bounds.y > selectionPoint.y ||
				selectionPoint.y >= bounds.y &&
						selectionPoint.y < bounds.y + bounds.height &&
						bounds.x > selectionPoint.x;
	}

	private boolean checkEndSelection(Rectangle bounds, Point selectionPoint) {
		return bounds.y > selectionPoint.y ||
				selectionPoint.y >= bounds.y &&
						selectionPoint.y < bounds.y + bounds.height &&
						selectionPoint.x < bounds.x;
	}

	public boolean paintSelection(Graphics g, boolean inSelection, RenderableSpot startPoint, RenderableSpot endPoint) {
		Point checkPoint1 = null;
		Point checkPoint2 = null;
		if (!inSelection) {
			boolean isStart = startPoint.renderable == this;
			boolean isEnd = endPoint.renderable == this;
			if (isStart && isEnd) {
				checkPoint1 = startPoint.getPoint();
				checkPoint2 = endPoint.getPoint();
			} else if (isStart) {
				checkPoint1 = startPoint.getPoint();
			} else if (isEnd) {
				checkPoint1 = endPoint.getPoint();
			}
		} else {
			if (startPoint.renderable == this) {
				checkPoint1 = startPoint.getPoint();
			} else if (endPoint.renderable == this) {
				checkPoint1 = endPoint.getPoint();
			}
		}
		Iterator i = this.getRenderables();
		if (i != null) {
			while (i.hasNext()) {
				Object robj = i.next();
				if (robj instanceof BoundableRenderable) {
					BoundableRenderable renderable = (BoundableRenderable) robj;
					Rectangle bounds = renderable.getBounds();
					if (!inSelection) {
						if (checkPoint1 != null && this.checkStartSelection(bounds, checkPoint1)) {
							if (checkPoint2 != null) {
								checkPoint1 = checkPoint2;
								checkPoint2 = null;
							} else {
								checkPoint1 = null;
							}
							inSelection = true;
						} else if (checkPoint2 != null && this.checkStartSelection(bounds, checkPoint2)) {
							checkPoint1 = null;
							checkPoint2 = null;
							inSelection = true;
						}
					} else if (checkPoint1 != null && this.checkEndSelection(bounds, checkPoint1)) {
						return false;
					}
					int offsetX = bounds.x;
					int offsetY = bounds.y;
					g.translate(offsetX, offsetY);
					try {
						boolean newInSelection = renderable.paintSelection(g, inSelection, startPoint, endPoint);
						if (inSelection && !newInSelection) {
							return false;
						}
						inSelection = newInSelection;
					} finally {
						g.translate(-offsetX, -offsetY);
					}
				}
			}
		}
		if (inSelection && checkPoint1 != null) {
			return false;
		} else if (!inSelection && (checkPoint1 != null || checkPoint2 != null) && !(checkPoint1 != null && checkPoint2 != null)) {
			// Has to have started not being in selection,
			// but we must start selecting without having
			// selected anything in the block then.
			return true;
		}
		return inSelection;
	}

	public boolean extractSelectionText(StringBuffer buffer, boolean inSelection, RenderableSpot startPoint, RenderableSpot endPoint) {
		Point checkPoint1 = null;
		Point checkPoint2 = null;
		if (!inSelection) {
			boolean isStart = startPoint.renderable == this;
			boolean isEnd = endPoint.renderable == this;
			if (isStart && isEnd) {
				checkPoint1 = startPoint.getPoint();
				checkPoint2 = endPoint.getPoint();
			} else if (isStart) {
				checkPoint1 = startPoint.getPoint();
			} else if (isEnd) {
				checkPoint1 = endPoint.getPoint();
			}
		} else {
			if (startPoint.renderable == this) {
				checkPoint1 = startPoint.getPoint();
			} else if (endPoint.renderable == this) {
				checkPoint1 = endPoint.getPoint();
			}
		}
		Iterator i = this.getRenderables();
		if (i != null) {
			while (i.hasNext()) {
				Object robj = i.next();
				if (robj instanceof BoundableRenderable) {
					BoundableRenderable renderable = (BoundableRenderable) robj;
					if (!inSelection) {
						Rectangle bounds = renderable.getBounds();
						if (checkPoint1 != null && this.checkStartSelection(bounds, checkPoint1)) {
							if (checkPoint2 != null) {
								checkPoint1 = checkPoint2;
								checkPoint2 = null;
							} else {
								checkPoint1 = null;
							}
							inSelection = true;
						} else if (checkPoint2 != null && this.checkStartSelection(bounds, checkPoint2)) {
							checkPoint1 = null;
							checkPoint2 = null;
							inSelection = true;
						}
					} else if (checkPoint1 != null && this.checkEndSelection(renderable.getBounds(), checkPoint1)) {
						return false;
					}
					boolean newInSelection = renderable.extractSelectionText(buffer, inSelection, startPoint, endPoint);
					if (inSelection && !newInSelection) {
						return false;
					}
					inSelection = newInSelection;
				}
			}
		}
		if (inSelection && checkPoint1 != null) {
			return false;
		} else if (!inSelection && (checkPoint1 != null || checkPoint2 != null) && !(checkPoint1 != null && checkPoint2 != null)) {
			// Has to have started not being in selection,
			// but we must start selecting without having
			// selected anything in the block then.
			return true;
		}
		return inSelection;
	}

	public void invalidateLayoutDeep() {
		this.invalidateLayoutLocal();
		Iterator renderables = this.getRenderables();
		if (renderables != null) {
			while (renderables.hasNext()) {
				Object r = renderables.next();
				if (r instanceof RCollection) {
					((RCollection) r).invalidateLayoutDeep();
				}
			}
		}
	}

	private BoundableRenderable renderableWithMouse = null;

	public void onMouseMoved(MouseEvent event, int x, int y, boolean triggerEvent, ModelNode limit) {
		super.onMouseMoved(event, x, y, triggerEvent, limit);
		BoundableRenderable oldRenderable = this.renderableWithMouse;
		Renderable r = this.getRenderable(x, y);
		BoundableRenderable newRenderable = r != null ? (BoundableRenderable) r : null;
		ModelNode newLimit;
		if (this.isContainedByNode()) {
			newLimit = this.modelNode;
		} else {
			newLimit = limit;
		}
		boolean changed = oldRenderable != newRenderable;
		if (changed) {
			if (oldRenderable != null) {
				oldRenderable.onMouseOut(event, x - oldRenderable.getX(), y - oldRenderable.getY(), newLimit);
			}
			this.renderableWithMouse = newRenderable;
		}
		// Must recurse always 
		if (newRenderable != null) {
			newRenderable.onMouseMoved(event, x - newRenderable.getX(), y - newRenderable.getY(), changed, newLimit);
		}
	}

	public void onMouseOut(MouseEvent event, int x, int y, ModelNode limit) {
		super.onMouseOut(event, x, y, limit);
		BoundableRenderable oldRenderable = this.renderableWithMouse;
		if (oldRenderable != null) {
			this.renderableWithMouse = null;
			ModelNode newLimit;
			if (this.isContainedByNode()) {
				newLimit = this.modelNode;
			} else {
				newLimit = limit;
			}
			oldRenderable.onMouseOut(event, x - oldRenderable.getX(), y - oldRenderable.getY(), newLimit);
		}
	}

	public BoundableRenderable getRenderable(int x, int y) {
		Iterator i = this.getRenderables();
		if (i != null) {
			while (i.hasNext()) {
				Object r = i.next();
				if (r instanceof BoundableRenderable) {
					BoundableRenderable br = (BoundableRenderable) r;
					int bx = br.getX();
					int by = br.getY();
					if (y >= by && y < by + br.getHeight() && x >= bx && x < bx + br.getWidth()) {
						return br;
					}
				}
			}
		}
		return null;
	}

	public boolean onRightClick(MouseEvent event, int x, int y) {
		BoundableRenderable br = this.getRenderable(x, y);
		if (br == null) {
			return HtmlController.getInstance().onContextMenu(this.modelNode, event);
		} else {
			return br.onRightClick(event, x - br.getX(), y - br.getY());
		}
	}
}
