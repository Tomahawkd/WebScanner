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
 * Created on Apr 16, 2005
 */
package org.lobobrowser.html.style;

import java.awt.*;

/**
 * @author J. H. S.
 */
public interface RenderState {
	int MASK_TEXTDECORATION_UNDERLINE = 1;
	int MASK_TEXTDECORATION_OVERLINE = 2;
	int MASK_TEXTDECORATION_LINE_THROUGH = 4;
	int MASK_TEXTDECORATION_BLINK = 8;

	int TEXTTRANSFORM_NONE = 0;
	int TEXTTRANSFORM_CAPITALIZE = 1;
	int TEXTTRANSFORM_UPPERCASE = 2;
	int TEXTTRANSFORM_LOWERCASE = 4;

	int DISPLAY_NONE = 0;
	int DISPLAY_INLINE = 1;
	int DISPLAY_BLOCK = 2;
	int DISPLAY_LIST_ITEM = 3;
	int DISPLAY_TABLE_ROW = 4;
	int DISPLAY_TABLE_CELL = 5;
	int DISPLAY_TABLE = 6;

	int WS_NORMAL = 0;
	int WS_PRE = 1;
	int WS_NOWRAP = 2;

	int VISIBILITY_VISIBLE = 0;
	int VISIBILITY_HIDDEN = 1;
	int VISIBILITY_COLLAPSE = 2;

	int POSITION_STATIC = 0;
	int POSITION_ABSOLUTE = 1;
	int POSITION_RELATIVE = 2;
	int POSITION_FIXED = 3;

	int FLOAT_NONE = 0;
	int FLOAT_LEFT = 1;
	int FLOAT_RIGHT = 2;

	int OVERFLOW_NONE = 0;
	int OVERFLOW_SCROLL = 1;
	int OVERFLOW_AUTO = 2;
	int OVERFLOW_HIDDEN = 3;
	int OVERFLOW_VISIBLE = 4;

	int getPosition();

	int getFloat();

	int getVisibility();

	Font getFont();

	int getFontBase();

	WordInfo getWordInfo(String word);

	Color getColor();

	Color getBackgroundColor();

	Color getTextBackgroundColor();

	BackgroundInfo getBackgroundInfo();

	Color getOverlayColor();

	int getTextTransform();

	int getTextDecorationMask();

	FontMetrics getFontMetrics();

	int getBlankWidth();

	boolean isHighlight();

	void setHighlight(boolean highlight);

	RenderState getPreviousRenderState();

	int getAlignXPercent();

	int getAlignYPercent();

	int getCount(String counter, int nesting);

	int getDisplay();

	void resetCount(String counter, int nesting, int value);

	int incrementCount(String counter, int nesting);

	int getTextIndent(int availWidth);

	String getTextIndentText();

	int getWhiteSpace();

	HtmlInsets getMarginInsets();

	HtmlInsets getPaddingInsets();

	int getOverflowX();

	int getOverflowY();

	void invalidate();

	BorderInfo getBorderInfo();
}
