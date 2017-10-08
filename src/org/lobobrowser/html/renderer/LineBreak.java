/*
    GNU LESSER GENERAL PUBLIC LICENSE
    Copyright (C) 2006 The XAMJ Project

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

public class LineBreak {
	static final int NONE = 0;
	static final int LEFT = 1;
	static final int RIGHT = 2;
	private static final int ALL = 3;

	private final ModelNode newLineNode;

	LineBreak(ModelNode newLineNode) {
		super();
		this.newLineNode = newLineNode;
	}

	public ModelNode getModelNode() {
		return this.newLineNode;
	}

	static int getBreakType(String clearAttr) {
		if (clearAttr == null) {
			return NONE;
		} else if ("all".equalsIgnoreCase(clearAttr)) {
			return ALL;
		} else if ("left".equalsIgnoreCase(clearAttr)) {
			return LEFT;
		} else if ("right".equalsIgnoreCase(clearAttr)) {
			return RIGHT;
		} else {
			return NONE;
		}
	}
}
