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
 * The create* and delete* methods on the table allow authors to construct and 
 * modify tables. [<a href='http://www.w3.org/TR/1999/REC-html401-19991224'>HTML 4.01</a>] specifies that only one of each of the 
 * <code>CAPTION</code>, <code>THEAD</code>, and <code>TFOOT</code> elements 
 * may exist in a table. Therefore, if one exists, and the createTHead() or 
 * createTFoot() method is called, the method returns the existing THead or 
 * TFoot element. See the TABLE element definition in HTML 4.01.
 * <p>See also the <a href='http://www.w3.org/TR/2003/REC-DOM-Level-2-HTML-20030109'>Document Object Model (DOM) Level 2 HTML Specification</a>.
 */
public interface HTMLTableElement extends HTMLElement {

    /**
     * Returns a collection of all the rows in the table, including all in 
     * <code>THEAD</code>, <code>TFOOT</code>, all <code>TBODY</code> 
     * elements. 
     */
    HTMLCollection getRows();

    /**
     * Specifies the table's position with respect to the rest of the 
     * document. See the align attribute definition in HTML 4.01. This 
     * attribute is deprecated in HTML 4.01.
     */
    String getAlign();
    /**
     * Specifies the table's position with respect to the rest of the 
     * document. See the align attribute definition in HTML 4.01. This 
     * attribute is deprecated in HTML 4.01.
     */
    void setAlign(String align);

    /**
     * Cell background color. See the bgcolor attribute definition in HTML 
     * 4.01. This attribute is deprecated in HTML 4.01.
     */
    String getBgColor();

    /**
     * The width of the border around the table. See the border attribute 
     * definition in HTML 4.01.
     */
    String getBorder();
    /**
     * The width of the border around the table. See the border attribute 
     * definition in HTML 4.01.
     */
    void setBorder(String border);

    /**
     * Specifies which external table borders to render. See the frame 
     * attribute definition in HTML 4.01.
     */
    String getFrame();
    /**
     * Specifies which external table borders to render. See the frame 
     * attribute definition in HTML 4.01.
     */
    void setFrame(String frame);

    /**
     * Specifies which internal table borders to render. See the rules 
     * attribute definition in HTML 4.01.
     */
    String getRules();
    /**
     * Specifies which internal table borders to render. See the rules 
     * attribute definition in HTML 4.01.
     */
    void setRules(String rules);

    /**
     * Specifies the desired table width. See the width attribute definition 
     * in HTML 4.01.
     */
    String getWidth();
    /**
     * Specifies the desired table width. See the width attribute definition 
     * in HTML 4.01.
     */
    void setWidth(String width);

}
