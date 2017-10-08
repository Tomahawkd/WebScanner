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
package org.lobobrowser.html.renderer;

import org.lobobrowser.html.domimpl.ModelNode;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * A renderer node with well-defined bounds. Most renderer nodes
 * implement this interface.
 */
public interface BoundableRenderable extends Renderable {
	ModelNode getModelNode();

	Rectangle getBounds();

	Dimension getSize();

	Point getOrigin();

	Point getOriginRelativeTo(RCollection ancestor);

	/**
	 * Gets the parent where the renderable is rendered.
	 */
	RCollection getParent();

	void setOriginalParent(RCollection origParent);

	RCollection getOriginalOrCurrentParent();

	void setBounds(int x, int y, int with, int height);

	void setOrigin(int x, int y);

	void setX(int x);

	void setY(int y);

	int getX();

	int getY();

	int getHeight();

	int getWidth();

	void setHeight(int height);

	void setWidth(int width);

	RenderableSpot getLowestRenderableSpot(int x, int y);

	Point getRenderablePoint(int guiX, int guiY);

	void repaint();

	/**
	 * Returns false if the event is consumed. True to propagate further.
	 */
	boolean onMousePressed(MouseEvent event, int x, int y);

	boolean onMouseReleased(MouseEvent event, int x, int y);

	boolean onMouseDisarmed(MouseEvent event);

	boolean onMouseClick(MouseEvent event, int x, int y);

	boolean onDoubleClick(MouseEvent event, int x, int y);

	boolean onRightClick(MouseEvent event, int x, int y);

	void onMouseMoved(MouseEvent event, int x, int y, boolean triggerEvent, ModelNode limit);

	void onMouseOut(MouseEvent event, int x, int y, ModelNode limit);

	/**
	 * Returns true if the renderable is fully contained by its modelNode, but
	 * said modelNode does not fully contain an ancestor renderable.
	 */
	boolean isContainedByNode();

	boolean paintSelection(Graphics g, boolean inSelection, RenderableSpot startPoint, RenderableSpot endPoint);

	/**
	 * Paints by either creating a new clipped graphics context corresponding
	 * to the bounds of the Renderable, or by translating the origin.
	 *
	 * @param g Parent's Graphics context.
	 */
	void paintTranslated(Graphics g);

	boolean extractSelectionText(StringBuffer buffer, boolean inSelection, RenderableSpot startPoint, RenderableSpot endPoint);

	void repaint(int x, int y, int width, int height);

	void setParent(RCollection parent);

	java.awt.Point getGUIPoint(int clientX, int clientY);

	int getZIndex();

	void invalidateLayoutUpTree();
}
