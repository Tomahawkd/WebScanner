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
 * Embedded image. See the IMG element definition in HTML 4.01.
 * <p>See also the <a href='http://www.w3.org/TR/2003/REC-DOM-Level-2-HTML-20030109'>Document Object Model (DOM) Level 2 HTML Specification</a>.
 */
public interface HTMLImageElement extends HTMLElement {
    /**
     * The name of the element (for backwards compatibility). 
     */
    String getName();
    /**
     * The name of the element (for backwards compatibility). 
     */
    void setName(String name);

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
     * Width of border around image. See the  border attribute definition in 
     * HTML 4.01. This attribute is deprecated in HTML 4.01. Note that the 
     * type of this attribute was <code>DOMString</code> in DOM Level 1 HTML [<a href='http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001'>DOM Level 1</a>]
     * .
     */
    String getBorder();
    /**
     * Width of border around image. See the  border attribute definition in 
     * HTML 4.01. This attribute is deprecated in HTML 4.01. Note that the 
     * type of this attribute was <code>DOMString</code> in DOM Level 1 HTML [<a href='http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001'>DOM Level 1</a>]
     * .
     */
    void setBorder(String border);

    /**
     * Height of the image in pixels. See the height attribute definition in 
     * HTML 4.01. Note that the type of this attribute was 
     * <code>DOMString</code> in DOM Level 1 HTML [<a href='http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001'>DOM Level 1</a>].
     */
    int getHeight();
    /**
     * Height of the image in pixels. See the height attribute definition in 
     * HTML 4.01. Note that the type of this attribute was 
     * <code>DOMString</code> in DOM Level 1 HTML [<a href='http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001'>DOM Level 1</a>].
     */
    void setHeight(int height);

    /**
     * URI [<a href='http://www.ietf.org/rfc/rfc2396.txt'>IETF RFC 2396</a>] designating the source of this image. See the src attribute 
     * definition in HTML 4.01.
     */
    String getSrc();
    /**
     * URI [<a href='http://www.ietf.org/rfc/rfc2396.txt'>IETF RFC 2396</a>] designating the source of this image. See the src attribute 
     * definition in HTML 4.01.
     */
    void setSrc(String src);

    /**
     * The width of the image in pixels. See the width attribute definition in 
     * HTML 4.01. Note that the type of this attribute was 
     * <code>DOMString</code> in DOM Level 1 HTML [<a href='http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001'>DOM Level 1</a>].
     */
    int getWidth();
    /**
     * The width of the image in pixels. See the width attribute definition in 
     * HTML 4.01. Note that the type of this attribute was 
     * <code>DOMString</code> in DOM Level 1 HTML [<a href='http://www.w3.org/TR/1998/REC-DOM-Level-1-19981001'>DOM Level 1</a>].
     */
    void setWidth(int width);

}
