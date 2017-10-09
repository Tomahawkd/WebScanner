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
 * The object used to represent the <code>TH</code> and <code>TD</code> 
 * elements. See the TD element definition in HTML 4.01.
 * <p>See also the <a href='http://www.w3.org/TR/2003/REC-DOM-Level-2-HTML-20030109'>Document Object Model (DOM) Level 2 HTML Specification</a>.
 */
public interface HTMLTableCellElement extends HTMLElement {

    /**
     * Horizontal alignment of data in cell. See the align attribute definition
     *  in HTML 4.01.
     */
    String getAlign();
    /**
     * Horizontal alignment of data in cell. See the align attribute definition
     *  in HTML 4.01.
     */
    void setAlign(String align);

    /**
     * Cell background color. See the bgcolor attribute definition in HTML 
     * 4.01. This attribute is deprecated in HTML 4.01.
     */
    String getBgColor();

    /**
     * Alignment character for cells in a column. See the char attribute 
     * definition in HTML 4.01.
     */
    String getCh();
    /**
     * Alignment character for cells in a column. See the char attribute 
     * definition in HTML 4.01.
     */
    void setCh(String ch);

    /**
     * Number of columns spanned by cell. See the colspan attribute definition 
     * in HTML 4.01.
     */
    int getColSpan();

    /**
     * List of <code>id</code> attribute values for header cells. See the 
     * headers attribute definition in HTML 4.01.
     */
    String getHeaders();
    /**
     * List of <code>id</code> attribute values for header cells. See the 
     * headers attribute definition in HTML 4.01.
     */
    void setHeaders(String headers);

    /**
     * Cell height. See the height attribute definition in HTML 4.01. This 
     * attribute is deprecated in HTML 4.01.
     */
    String getHeight();
    /**
     * Cell height. See the height attribute definition in HTML 4.01. This 
     * attribute is deprecated in HTML 4.01.
     */
    void setHeight(String height);

    /**
     * Number of rows spanned by cell. See the rowspan attribute definition in 
     * HTML 4.01.
     */
    int getRowSpan();

    /**
     * Scope covered by header cells. See the scope attribute definition in 
     * HTML 4.01.
     */
    String getScope();
    /**
     * Scope covered by header cells. See the scope attribute definition in 
     * HTML 4.01.
     */
    void setScope(String scope);

    /**
     * Vertical alignment of data in cell. See the valign attribute definition 
     * in HTML 4.01.
     */
    String getVAlign();

    /**
     * Cell width. See the width attribute definition in HTML 4.01. This 
     * attribute is deprecated in HTML 4.01.
     */
    String getWidth();
    /**
     * Cell width. See the width attribute definition in HTML 4.01. This 
     * attribute is deprecated in HTML 4.01.
     */
    void setWidth(String width);

}
