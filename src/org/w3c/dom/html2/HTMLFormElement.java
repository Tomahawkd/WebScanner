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
 * The <code>FORM</code> element encompasses behavior similar to a collection 
 * and an element. It provides direct access to the contained form controls 
 * as well as the attributes of the form element. See the FORM element 
 * definition in HTML 4.01.
 * <p>See also the <a href='http://www.w3.org/TR/2003/REC-DOM-Level-2-HTML-20030109'>Document Object Model (DOM) Level 2 HTML Specification</a>.
 */
public interface HTMLFormElement extends HTMLElement {
    /**
     * Returns a collection of all form control elements in the form. 
     */
    HTMLCollection getElements();

    /**
     * The number of form controls in the form.
     */
    int getLength();

    /**
     * Names the form. 
     */
    String getName();
    /**
     * Names the form. 
     */
    void setName(String name);

    /**
     * Server-side form handler. See the action attribute definition in HTML 
     * 4.01.
     */
    String getAction();
    /**
     * Server-side form handler. See the action attribute definition in HTML 
     * 4.01.
     */
    void setAction(String action);

    /**
     * The content type of the submitted form, generally 
     * "application/x-www-form-urlencoded". See the enctype attribute 
     * definition in HTML 4.01. The onsubmit even handler is not guaranteed 
     * to be triggered when invoking this method. The behavior is 
     * inconsistent for historical reasons and authors should not rely on a 
     * particular one. 
     */
    String getEnctype();

    /**
     * HTTP method [<a href='http://www.ietf.org/rfc/rfc2616.txt'>IETF RFC 2616</a>] used to submit form. See the method attribute definition 
     * in HTML 4.01.
     */
    String getMethod();
    /**
     * HTTP method [<a href='http://www.ietf.org/rfc/rfc2616.txt'>IETF RFC 2616</a>] used to submit form. See the method attribute definition 
     * in HTML 4.01.
     */
    void setMethod(String method);

    /**
     * Frame to render the resource in. See the target attribute definition in 
     * HTML 4.01.
     */
    String getTarget();
    /**
     * Frame to render the resource in. See the target attribute definition in 
     * HTML 4.01.
     */
    void setTarget(String target);

    /**
     * Submits the form. It performs the same action as a submit button.
     */
    void submit();

    /**
     * Restores a form element's default values. It performs the same action 
     * as a reset button.
     */
    void reset();

}
