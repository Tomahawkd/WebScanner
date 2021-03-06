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
import org.lobobrowser.html.style.RenderState;

import java.awt.*;
import java.awt.event.MouseEvent;

final class RBlank extends BaseBoundableRenderable {
	final int ascentPlusLeading;
	private final FontMetrics fontMetrics;

	RBlank(ModelNode me, FontMetrics fm, RenderableContainer container, int ascentPlusLeading, int width, int height) {
		super(container, me);
		this.fontMetrics = fm;
		this.ascentPlusLeading = ascentPlusLeading;
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
		RenderState rs = this.modelNode.getRenderState();
		Color bkg = rs.getTextBackgroundColor();
		if (bkg != null) {
			Color oldColor = g.getColor();
			try {
				g.setColor(bkg);
				g.fillRect(0, 0, this.width, this.height);
			} finally {
				g.setColor(oldColor);
			}
		}
		int td = rs.getTextDecorationMask();
		if (td != 0) {
			if ((td & RenderState.MASK_TEXTDECORATION_UNDERLINE) != 0) {
				int lineOffset = this.ascentPlusLeading + 2;
				g.drawLine(0, lineOffset, this.width, lineOffset);
			}
			if ((td & RenderState.MASK_TEXTDECORATION_LINE_THROUGH) != 0) {
				FontMetrics fm = this.fontMetrics;
				int lineOffset = fm.getLeading() + (fm.getAscent() + fm.getDescent()) / 2;
				g.drawLine(0, lineOffset, this.width, lineOffset);
			}
			if ((td & RenderState.MASK_TEXTDECORATION_OVERLINE) != 0) {
				int lineOffset = this.fontMetrics.getLeading();
				g.drawLine(0, lineOffset, this.width, lineOffset);
			}
		}
		Color over = rs.getOverlayColor();
		if (over != null) {
			Color oldColor = g.getColor();
			try {
				g.setColor(over);
				g.fillRect(0, 0, width, height);
			} finally {
				g.setColor(oldColor);
			}
		}
	}

	public boolean extractSelectionText(StringBuffer buffer, boolean inSelection, RenderableSpot startPoint, RenderableSpot endPoint) {
		if (this == startPoint.renderable || this == endPoint.renderable) {
			if (inSelection) {
				return false;
			}
		} else if (!inSelection) {
			return false;
		}
		buffer.append(' ');
		return true;
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
