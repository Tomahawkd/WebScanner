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
 * Copyright (c) 2003 World Wide Web Consortium,
 * (Massachusetts Institute of Technology, Institut National de
 * Recherche en Informatique et en Automatique, Keio University). All
 * Rights Reserved. This program is distributed under the W3C's Software
 * Intellectual Property License. This program is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.
 * See W3C License http://www.w3.org/Consortium/Legal/ for more details.
 */

package org.w3c.dom.html2;

/**
 * A selectable choice. See the OPTION element definition in HTML 4.01.
 * <p>See also the <a href='http://www.w3.org/TR/2003/REC-DOM-Level-2-HTML-20030109'>Document Object Model (DOM) Level 2 HTML Specification</a>.
 */
public interface HTMLOptionElement extends HTMLElement {

    /**
     * Represents the value of the HTML selected attribute. The value of this 
     * attribute does not change if the state of the corresponding form 
     * control, in an interactive user agent, changes. See the selected 
     * attribute definition in HTML 4.01.
     */
    boolean getDefaultSelected();

    /**
     * The text contained within the option element. 
     */
    String getText();

    /**
     * The index of this <code>OPTION</code> in its parent <code>SELECT</code>
     * , starting from 0.
     */
    int getIndex();

    /**
     * The control is unavailable in this context. See the disabled attribute 
     * definition in HTML 4.01.
     */
    boolean getDisabled();
    /**
     * The control is unavailable in this context. See the disabled attribute 
     * definition in HTML 4.01.
     */
    void setDisabled(boolean disabled);

    /**
     * Option label for use in hierarchical menus. See the label attribute 
     * definition in HTML 4.01.
     */
    String getLabel();
    /**
     * Option label for use in hierarchical menus. See the label attribute 
     * definition in HTML 4.01.
     */
    void setLabel(String label);

    /**
     * Represents the current state of the corresponding form control, in an 
     * interactive user agent. Changing this attribute changes the state of 
     * the form control, but does not change the value of the HTML selected 
     * attribute of the element.
     */
    boolean getSelected();
    /**
     * Represents the current state of the corresponding form control, in an 
     * interactive user agent. Changing this attribute changes the state of 
     * the form control, but does not change the value of the HTML selected 
     * attribute of the element.
     */
    void setSelected(boolean selected);

    /**
     * The current form control value. See the value attribute definition in 
     * HTML 4.01.
     */
    String getValue();
    /**
     * The current form control value. See the value attribute definition in 
     * HTML 4.01.
     */
    void setValue(String value);

}
