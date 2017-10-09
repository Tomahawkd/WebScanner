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
 * Generic embedded object.In principle, all properties on the object element 
 * are read-write but in some environments some properties may be read-only 
 * once the underlying object is instantiated. See the OBJECT element 
 * definition in [<a href='http://www.w3.org/TR/1999/REC-html401-19991224'>HTML 4.01</a>].
 * <p>See also the <a href='http://www.w3.org/TR/2003/REC-DOM-Level-2-HTML-20030109'>Document Object Model (DOM) Level 2 HTML Specification</a>.
 */
public interface HTMLObjectElement extends HTMLElement {
    /**
     * Returns the <code>FORM</code> element containing this control. Returns 
     * <code>null</code> if this control is not within the context of a 
     * form. 
     */
    HTMLFormElement getForm();

    /**
     * Applet class file. See the <code>code</code> attribute for 
     * HTMLAppletElement. 
     */
    String getCode();
    /**
     * Applet class file. See the <code>code</code> attribute for 
     * HTMLAppletElement. 
     */
    void setCode(String code);

    /**
     * Aligns this object (vertically or horizontally) with respect to its 
     * surrounding text. See the align attribute definition in HTML 4.01. 
     * This attribute is deprecated in HTML 4.01.
     */
    String getAlign();
    /**
     * Aligns this object (vertically or horizontally) with respect to its 
     * surrounding text. See the align attribute definition in HTML 4.01. 
     * This attribute is deprecated in HTML 4.01.
     */
    void setAlign(String align);

    /**
     * Width of border around the object. See the border attribute definition 
     * in HTML 4.01. This attribute is deprecated in HTML 4.01.
     */
    String getBorder();
    /**
     * Width of border around the object. See the border attribute definition 
     * in HTML 4.01. This attribute is deprecated in HTML 4.01.
     */
    void setBorder(String border);

    /**
     * A URI [<a href='http://www.ietf.org/rfc/rfc2396.txt'>IETF RFC 2396</a>] specifying the location of the object's data. See the data 
     * attribute definition in HTML 4.01.
     */
    String getData();
    /**
     * A URI [<a href='http://www.ietf.org/rfc/rfc2396.txt'>IETF RFC 2396</a>] specifying the location of the object's data. See the data 
     * attribute definition in HTML 4.01.
     */
    void setData(String data);

    /**
     * Override height. See the height attribute definition in HTML 4.01.
     */
    String getHeight();
    /**
     * Override height. See the height attribute definition in HTML 4.01.
     */
    void setHeight(String height);

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
     * Content type for data downloaded via <code>data</code> attribute. See 
     * the type attribute definition in HTML 4.01.
     */
    String getType();
    /**
     * Content type for data downloaded via <code>data</code> attribute. See 
     * the type attribute definition in HTML 4.01.
     */
    void setType(String type);

    /**
     * Override width. See the width attribute definition in HTML 4.01.
     */
    String getWidth();
    /**
     * Override width. See the width attribute definition in HTML 4.01.
     */
    void setWidth(String width);

}
