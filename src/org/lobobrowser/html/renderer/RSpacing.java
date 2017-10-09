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
 * Created on May 21, 2005
 */
package org.lobobrowser.html.renderer;

import org.lobobrowser.html.domimpl.ModelNode;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * This is used when there's padding or margins in inline elements.
 */
final class RSpacing extends BaseBoundableRenderable {
	RSpacing(ModelNode me, RenderableContainer container, int width, int height) {
		super(container, me);
		// Dimensions set when constructed.
		this.width = width;
		this.height = height;
	}

	protected void invalidateLayoutLocal() {
	}

	public boolean onMouseClick(java.awt.event.MouseEvent event, int x, int y) {
		ModelNode me = this.modelNode;
		return me == null || HtmlController.getInstance().onMouseClick(me, event);
	}

	public boolean onDoubleClick(java.awt.event.MouseEvent event, int x, int y) {
		ModelNode me = this.modelNode;
		return me == null || HtmlController.getInstance().onDoubleClick(me, event);
	}

	public boolean onMousePressed(java.awt.event.MouseEvent event, int x, int y) {
		ModelNode me = this.modelNode;
		return me == null || HtmlController.getInstance().onMouseDown(me);
	}

	public boolean onMouseReleased(java.awt.event.MouseEvent event, int x, int y) {
		ModelNode me = this.modelNode;
		return me == null || HtmlController.getInstance().onMouseUp(me);
	}

	public boolean onMouseDisarmed(java.awt.event.MouseEvent event) {
		ModelNode me = this.modelNode;
		return me == null || HtmlController.getInstance().onMouseDisarmed(me);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.xamj.domimpl.markup.Renderable#paint(java.awt.Graphics)
	 */
	public final void paint(Graphics g) {
		// Nothing to paint in spacing.
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.BoundableRenderable#getRenderable(int, int)
	 */
	public RenderableSpot getLowestRenderableSpot(int x, int y) {
		return new RenderableSpot(this, x, y);
	}

	public boolean isContainedByNode() {
		return true;
	}

	public boolean onRightClick(MouseEvent event, int x, int y) {
		ModelNode me = this.modelNode;
		return me == null || HtmlController.getInstance().onContextMenu(me, event);
	}
}
