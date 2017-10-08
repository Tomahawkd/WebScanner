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

public interface InputContext {
	boolean getChecked();

	void setChecked(boolean checked);

	boolean getDisabled();

	void setDisabled(boolean disabled);

	int getMaxLength();

	void setMaxLength(int maxLength);

	String getName();

	void setName(String name);

	boolean getReadOnly();

	void setReadOnly(boolean readOnly);

	int getControlSize();

	void setControlSize(int size);

	int getTabIndex();

	void setTabIndex(int tabIndex);

	String getValue();

	String[] getValues();

	void setValue(String value);

	void blur();

	void focus();

	void select();

	void click();

	int getRows();

	int getCols();

	void setRows(int rows);

	void setCols(int cols);

	int getSelectedIndex();

	void setSelectedIndex(int value);

	int getVisibleSize();

	void setVisibleSize(int value);

	java.io.File getFileValue();

	void resetInput();
}
