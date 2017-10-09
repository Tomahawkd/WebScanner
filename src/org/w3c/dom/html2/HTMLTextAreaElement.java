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
 * Multi-line text field. See the TEXTAREA element definition in HTML 4.01.
 * <p>See also the <a href='http://www.w3.org/TR/2003/REC-DOM-Level-2-HTML-20030109'>Document Object Model (DOM) Level 2 HTML Specification</a>.
 */
public interface HTMLTextAreaElement extends HTMLElement {

    /**
     * Returns the <code>FORM</code> element containing this control. Returns 
     * <code>null</code> if this control is not within the context of a 
     * form. 
     */
    HTMLFormElement getForm();

    /**
     * Width of control (in characters). See the cols attribute definition in 
     * HTML 4.01.
     */
    int getCols();
    /**
     * Width of control (in characters). See the cols attribute definition in 
     * HTML 4.01.
     */
    void setCols(int cols);

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
     * Form control or object name when submitted with a form. See the name 
     * attribute definition in HTML 4.01.
     */
    String getName();
    /**
     * Form control or object name when submitted with a form. See the name 
     * attribute definition in HTML 4.01.
     */
    void setName(String name);

    /**
     * Number of text rows. See the rows attribute definition in HTML 4.01.
     */
    int getRows();
    /**
     * Number of text rows. See the rows attribute definition in HTML 4.01.
     */
    void setRows(int rows);

    /**
     * The type of this form control. This the string "textarea".
     */
    String getType();

    /**
     * Represents the current contents of the corresponding form control, in 
     * an interactive user agent. Changing this attribute changes the 
     * contents of the form control, but does not change the contents of the 
     * element. If the entirety of the data can not fit into a single 
     * <code>DOMString</code>, the implementation may truncate the data.
     */
    String getValue();
    /**
     * Represents the current contents of the corresponding form control, in 
     * an interactive user agent. Changing this attribute changes the 
     * contents of the form control, but does not change the contents of the 
     * element. If the entirety of the data can not fit into a single 
     * <code>DOMString</code>, the implementation may truncate the data.
     */
    void setValue(String value);

    /**
     * Removes keyboard focus from this element.
     */
    void blur();

    /**
     * Gives keyboard focus to this element.
     */
    void focus();

    /**
     * Select the contents of the <code>TEXTAREA</code>.
     */
    void select();

}
