/*    GNU LESSER GENERAL PUBLIC LICENSE
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
 * Created on Sep 3, 2005
 */
package org.lobobrowser.html.domimpl;

import com.steadystate.css.parser.CSSOMParser;
import org.lobobrowser.html.FormInput;
import org.lobobrowser.html.style.*;
import org.lobobrowser.util.Strings;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.html2.HTMLElement;

import java.io.StringReader;
import java.util.*;

public class HTMLElementImpl extends ElementImpl implements HTMLElement, CSS2PropertiesContext {

	public HTMLElementImpl(String name) {
		super(name);
	}

	private volatile AbstractCSS2Properties currentStyleDeclarationState;
	private volatile AbstractCSS2Properties localStyleDeclarationState;

	private void forgetLocalStyle() {
		synchronized (this) {
			this.currentStyleDeclarationState = null;
			this.localStyleDeclarationState = null;
		}
	}

	final void forgetStyle(boolean deep) {
		synchronized (this) {
			this.currentStyleDeclarationState = null;
			this.isHoverStyle = null;
			this.hasHoverStyleByElement = null;
			if (deep) {
				java.util.ArrayList<Node> nl = this.nodeList;
				if (nl != null) {
					for (Node node : nl) {
						if (node instanceof HTMLElementImpl) {
							((HTMLElementImpl) node).forgetStyle(true);
						}
					}
				}
			}
		}
	}

	/**
	 * Gets the style object associated with the element.
	 * It may return null only if the type of element does not handle stylesheets.
	 */
	public AbstractCSS2Properties getCurrentStyle() {
		AbstractCSS2Properties sds;
		synchronized (this) {
			sds = this.currentStyleDeclarationState;
			if (sds != null) {
				return sds;
			}
		}
		// Can't do the following in synchronized block (reverse locking order with document).
		// First, add declarations from stylesheet
		sds = this.createDefaultStyleSheet();
		sds = this.addStyleSheetDeclarations(sds, this.getPseudoNames());
		// Now add local style if any.
		AbstractCSS2Properties localStyle = this.getStyle();
		if (sds == null) {
			sds = new ComputedCSS2Properties(this);
			sds.setLocalStyleProperties(localStyle);
		} else {
			sds.setLocalStyleProperties(localStyle);
		}
		synchronized (this) {
			// Check if style properties were set while outside
			// the synchronized block (can happen).
			AbstractCSS2Properties setProps = this.currentStyleDeclarationState;
			if (setProps != null) {
				return setProps;
			}
			this.currentStyleDeclarationState = sds;
			return sds;
		}
	}

	/**
	 * Gets the local style object associated with the element. The properties
	 * object returned only includes properties from the local style attribute.
	 * It may return null only if the type of element does not handle stylesheets.
	 */
	public AbstractCSS2Properties getStyle() {
		AbstractCSS2Properties sds;
		synchronized (this) {
			sds = this.localStyleDeclarationState;
			if (sds != null) {
				return sds;
			}
			sds = new LocalCSS2Properties(this);
			// Add any declarations in style attribute (last takes precedence).
			String style = this.getAttribute("style");
			if (style != null && style.length() != 0) {
				CSSOMParser parser = new CSSOMParser();
				InputSource inputSource = this.getCssInputSourceForDecl(style);
				try {
					CSSStyleDeclaration sd = parser.parseStyleDeclaration(inputSource);
					sds.addStyleDeclaration(sd);
				} catch (Exception ignored) {
				}
			}
			this.localStyleDeclarationState = sds;
		}
		// Synchronization note: Make sure getStyle() does not return multiple values.
		return sds;
	}

	protected AbstractCSS2Properties createDefaultStyleSheet() {
		// Override to provide element defaults.
		return null;
	}

	public String getClassName() {
		String className = this.getAttribute("class");
		// Blank required instead of null.
		return className == null ? "" : className;
	}

	public String getCharset() {
		return this.getAttribute("charset");
	}

	public void setCharset(String charset) {
		this.setAttribute("charset", charset);
	}

	public boolean getAttributeAsBoolean(String name) {
		return this.getAttribute(name) != null;
	}

	protected void assignAttributeField(String normalName, String value) {
		if (!this.notificationsSuspended) {
			this.informInvalidAttibute(normalName);
		} else {
			if ("style".equals(normalName)) {
				this.forgetLocalStyle();
			}
		}
		super.assignAttributeField(normalName, value);
	}

	private InputSource getCssInputSourceForDecl(String text) {
		java.io.Reader reader = new StringReader("{" + text + "}");
		return new InputSource(reader);
	}

	/**
	 * Adds style sheet declarations applicable
	 * to this element.
	 * A properties object is created if necessary
	 * when the one passed is <code>null</code>.
	 *
	 */
	private AbstractCSS2Properties addStyleSheetDeclarations(AbstractCSS2Properties style, Set<String> pseudoNames) {
		Node pn = this.parentNode;
		if (pn == null) {
			// do later
			return style;
		}
		String classNames = this.getClassName();
		if (classNames != null && classNames.length() != 0) {
			String id = this.getId();
			String elementName = this.getTagName();
			String[] classNameArray = Strings.split(classNames);
			for (int i = classNameArray.length; --i >= 0; ) {
				String className = classNameArray[i];
				Collection<CSSStyleDeclaration> sds = this.findStyleDeclarations(elementName, id, className, pseudoNames);
				style = addDeclaration(style, sds);
			}
		} else {
			String id = this.getId();
			String elementName = this.getTagName();
			Collection<CSSStyleDeclaration> sds = this.findStyleDeclarations(elementName, id, null, pseudoNames);
			style = addDeclaration(style, sds);
		}
		return style;
	}

	private AbstractCSS2Properties addDeclaration(AbstractCSS2Properties style, Collection<CSSStyleDeclaration> sds) {
		if (sds != null) {
			for (CSSStyleDeclaration sd1 : sds) {
				if (style == null) {
					style = new ComputedCSS2Properties(this);
				}
				style.addStyleDeclaration(sd1);
			}
		}
		return style;
	}

	private boolean isMouseOver = false;

	public void setMouseOver(boolean mouseOver) {
		if (this.isMouseOver != mouseOver) {
			// Change isMouseOver field before checking to invalidate.
			this.isMouseOver = mouseOver;
			// Check if descendents are affected (e.g. div:hover a { ... } )
			this.invalidateDescendentsForHover();
			if (this.hasHoverStyle()) {
				this.informInvalid();
			}
		}
	}

	private void invalidateDescendentsForHover() {
		synchronized (this.treeLock) {
			this.invalidateDescendentsForHoverImpl(this);
		}
	}

	private void invalidateDescendentsForHoverImpl(HTMLElementImpl ancestor) {
		ArrayList<Node> nodeList = this.nodeList;
		if (nodeList != null) {
			for (Node aNodeList : nodeList) {
				if (aNodeList instanceof HTMLElementImpl) {
					HTMLElementImpl descendent = (HTMLElementImpl) aNodeList;
					if (descendent.hasHoverStyle(ancestor)) {
						descendent.informInvalid();
					}
					descendent.invalidateDescendentsForHoverImpl(ancestor);
				}
			}
		}
	}

	private Boolean isHoverStyle = null;
	private Map<HTMLElement, Boolean> hasHoverStyleByElement = null;

	private boolean hasHoverStyle() {
		Boolean ihs;
		synchronized (this) {
			ihs = this.isHoverStyle;
			if (ihs != null) {
				return ihs;
			}
		}
		HTMLDocumentImpl doc = (HTMLDocumentImpl) this.document;
		if (doc == null) {
			ihs = Boolean.FALSE;
		} else {
			StyleSheetAggregator ssa = doc.getStyleSheetAggregator();
			String id = this.getId();
			String elementName = this.getTagName();
			String classNames = this.getClassName();
			String[] classNameArray = null;
			if (classNames != null && classNames.length() != 0) {
				classNameArray = Strings.split(classNames);
			}
			ihs = ssa.affectedByPseudoNameInAncestor(this, this, elementName, id, classNameArray, "hover");
		}
		synchronized (this) {
			this.isHoverStyle = ihs;
		}
		return ihs;
	}

	private boolean hasHoverStyle(HTMLElementImpl ancestor) {
		Map<HTMLElement, Boolean> ihs;
		synchronized (this) {
			ihs = this.hasHoverStyleByElement;
			if (ihs != null) {
				Boolean f = ihs.get(ancestor);
				if (f != null) {
					return f;
				}
			}
		}
		Boolean hhs;
		HTMLDocumentImpl doc = (HTMLDocumentImpl) this.document;
		if (doc == null) {
			hhs = Boolean.FALSE;
		} else {
			StyleSheetAggregator ssa = doc.getStyleSheetAggregator();
			String id = this.getId();
			String elementName = this.getTagName();
			String classNames = this.getClassName();
			String[] classNameArray = null;
			if (classNames != null && classNames.length() != 0) {
				classNameArray = Strings.split(classNames);
			}
			hhs = ssa.affectedByPseudoNameInAncestor(this, ancestor, elementName, id, classNameArray, "hover");
		}
		synchronized (this) {
			ihs = this.hasHoverStyleByElement;
			if (ihs == null) {
				ihs = new HashMap<>(2);
				this.hasHoverStyleByElement = ihs;
			}
			ihs.put(ancestor, hhs);
		}
		return hhs;
	}

	/**
	 * Gets the pseudo-element lowercase names currently
	 * applicable to this element. Method must return
	 * <code>null</code> if there are no such
	 * pseudo-elements.
	 */
	public Set<String> getPseudoNames() {
		Set<String> pnset = null;
		if (this.isMouseOver) {
			pnset = new HashSet<>(1);
			pnset.add("hover");
		}
		return pnset;
	}

	private Collection<CSSStyleDeclaration> findStyleDeclarations(String elementName, String id, String className, Set<String> pseudoNames) {
		HTMLDocumentImpl doc = (HTMLDocumentImpl) this.document;
		if (doc == null) {
			return null;
		}
		StyleSheetAggregator ssa = doc.getStyleSheetAggregator();
		return ssa.getActiveStyleDeclarations(this, elementName, id, className, pseudoNames);
	}

	public void informInvalid() {
		// This is called when an attribute or child changes.
		this.forgetStyle(false);
		super.informInvalid();
	}

	private void informInvalidAttibute(String normalName) {
		// This is called when an attribute changes while
		// the element is allowing notifications.
		if ("style".equals(normalName)) {
			this.forgetLocalStyle();
		} else if ("id".equals(normalName) || "class".equals(normalName)) {
			this.forgetStyle(false);
		}
		// Call super implementation of informValid().
		super.informInvalid();
	}

	/**
	 * Gets form input due to the current element. It should
	 * return <code>null</code> except when the element is a form input element.
	 */
	protected FormInput[] getFormInputs() {
		// Override in input elements
		return null;
	}

	private boolean classMatch(String classTL) {
		String classNames = this.getClassName();
		if (classNames == null || classNames.length() == 0) {
			return classTL == null;
		}
		StringTokenizer tok = new StringTokenizer(classNames, " \t\r\n");
		while (tok.hasMoreTokens()) {
			String token = tok.nextToken();
			if (token.toLowerCase().equals(classTL)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get an ancestor that matches the element tag name given and the
	 * style class given.
	 *
	 * @param elementTL An tag name in lowercase or an asterisk (*).
	 * @param classTL   A class name in lowercase.
	 */
	public HTMLElementImpl getAncestorWithClass(String elementTL, String classTL) {
		Object nodeObj = this.getParentNode();
		if (nodeObj instanceof HTMLElementImpl) {
			HTMLElementImpl parentElement = (HTMLElementImpl) nodeObj;
			String pelementTL = parentElement.getTagName().toLowerCase();
			if (("*".equals(elementTL) || elementTL.equals(pelementTL)) && parentElement.classMatch(classTL)) {
				return parentElement;
			}
			return parentElement.getAncestorWithClass(elementTL, classTL);
		} else {
			return null;
		}
	}

	public HTMLElementImpl getParentWithClass(String elementTL, String classTL) {
		Object nodeObj = this.getParentNode();
		if (nodeObj instanceof HTMLElementImpl) {
			HTMLElementImpl parentElement = (HTMLElementImpl) nodeObj;
			String pelementTL = parentElement.getTagName().toLowerCase();
			if (("*".equals(elementTL) || elementTL.equals(pelementTL)) && parentElement.classMatch(classTL)) {
				return parentElement;
			}
		}
		return null;
	}

	private HTMLElementImpl getPreceedingSiblingElement() {
		Node parentNode = this.getParentNode();
		if (parentNode == null) {
			return null;
		}
		NodeList childNodes = parentNode.getChildNodes();
		if (childNodes == null) {
			return null;
		}
		int length = childNodes.getLength();
		HTMLElementImpl priorElement = null;
		for (int i = 0; i < length; i++) {
			Node child = childNodes.item(i);
			if (child == this) {
				return priorElement;
			}
			if (child instanceof HTMLElementImpl) {
				priorElement = (HTMLElementImpl) child;
			}
		}
		return null;
	}

	public HTMLElementImpl getPreceedingSiblingWithClass(String elementTL, String classTL) {
		HTMLElementImpl psibling = this.getPreceedingSiblingElement();
		if (psibling != null) {
			String pelementTL = psibling.getTagName().toLowerCase();
			if (("*".equals(elementTL) || elementTL.equals(pelementTL)) && psibling.classMatch(classTL)) {
				return psibling;
			}
		}
		return null;
	}

	public HTMLElementImpl getAncestorWithId(String elementTL, String idTL) {
		Object nodeObj = this.getParentNode();
		if (nodeObj instanceof HTMLElementImpl) {
			HTMLElementImpl parentElement = (HTMLElementImpl) nodeObj;
			String pelementTL = parentElement.getTagName().toLowerCase();
			String pid = parentElement.getId();
			String pidTL = pid == null ? null : pid.toLowerCase();
			if (("*".equals(elementTL) || elementTL.equals(pelementTL)) && idTL.equals(pidTL)) {
				return parentElement;
			}
			return parentElement.getAncestorWithId(elementTL, idTL);
		} else {
			return null;
		}
	}

	public HTMLElementImpl getParentWithId(String elementTL, String idTL) {
		Object nodeObj = this.getParentNode();
		if (nodeObj instanceof HTMLElementImpl) {
			HTMLElementImpl parentElement = (HTMLElementImpl) nodeObj;
			String pelementTL = parentElement.getTagName().toLowerCase();
			String pid = parentElement.getId();
			String pidTL = pid == null ? null : pid.toLowerCase();
			if (("*".equals(elementTL) || elementTL.equals(pelementTL)) && idTL.equals(pidTL)) {
				return parentElement;
			}
		}
		return null;
	}

	public HTMLElementImpl getPreceedingSiblingWithId(String elementTL, String idTL) {
		HTMLElementImpl psibling = this.getPreceedingSiblingElement();
		if (psibling != null) {
			String pelementTL = psibling.getTagName().toLowerCase();
			String pid = psibling.getId();
			String pidTL = pid == null ? null : pid.toLowerCase();
			if (("*".equals(elementTL) || elementTL.equals(pelementTL)) && idTL.equals(pidTL)) {
				return psibling;
			}
		}
		return null;
	}

	public HTMLElementImpl getAncestor(String elementTL) {
		Object nodeObj = this.getParentNode();
		if (nodeObj instanceof HTMLElementImpl) {
			HTMLElementImpl parentElement = (HTMLElementImpl) nodeObj;
			if ("*".equals(elementTL)) {
				return parentElement;
			}
			String pelementTL = parentElement.getTagName().toLowerCase();
			if (elementTL.equals(pelementTL)) {
				return parentElement;
			}
			return parentElement.getAncestor(elementTL);
		} else {
			return null;
		}
	}

	public HTMLElementImpl getParent(String elementTL) {
		Object nodeObj = this.getParentNode();
		if (nodeObj instanceof HTMLElementImpl) {
			HTMLElementImpl parentElement = (HTMLElementImpl) nodeObj;
			if ("*".equals(elementTL)) {
				return parentElement;
			}
			String pelementTL = parentElement.getTagName().toLowerCase();
			if (elementTL.equals(pelementTL)) {
				return parentElement;
			}
		}
		return null;
	}

	public HTMLElementImpl getPreceedingSibling(String elementTL) {
		HTMLElementImpl psibling = this.getPreceedingSiblingElement();
		if (psibling != null) {
			if ("*".equals(elementTL)) {
				return psibling;
			}
			String pelementTL = psibling.getTagName().toLowerCase();
			if (elementTL.equals(pelementTL)) {
				return psibling;
			}
		}
		return null;
	}

	Object getAncestorForJavaClass(Class javaClass) {
		Object nodeObj = this.getParentNode();
		if (nodeObj == null || javaClass.isInstance(nodeObj)) {
			return nodeObj;
		} else if (nodeObj instanceof HTMLElementImpl) {
			return ((HTMLElementImpl) nodeObj).getAncestorForJavaClass(javaClass);
		} else {
			return null;
		}
	}

	void appendOuterHTMLImpl(StringBuffer buffer) {
		String tagName = this.getTagName();
		buffer.append('<');
		buffer.append(tagName);
		Map<String, String> attributes = this.attributes;
		if (attributes != null) {
			for (Map.Entry<String, String> o : attributes.entrySet()) {
				String value = o.getValue();
				if (value != null) {
					buffer.append(' ');
					buffer.append(o.getKey());
					buffer.append("=\"");
					buffer.append(Strings.strictHtmlEncode(value, true));
					buffer.append("\"");
				}
			}
		}
		ArrayList nl = this.nodeList;
		if (nl == null || nl.size() == 0) {
			buffer.append("/>");
			return;
		}
		buffer.append('>');
		this.appendInnerHTMLImpl(buffer);
		buffer.append("</");
		buffer.append(tagName);
		buffer.append('>');
	}

	protected RenderState createRenderState(RenderState prevRenderState) {
		// Overrides NodeImpl method
		// Called in synchronized block already
		return new StyleSheetRenderState(prevRenderState, this);
	}

	public String getDocumentBaseURI() {
		HTMLDocumentImpl doc = (HTMLDocumentImpl) this.document;
		if (doc != null) {
			return doc.getBaseURI();
		} else {
			return null;
		}
	}

	public String toString() {
		return super.toString() + "[currentStyle=" + this.getCurrentStyle() + "]";
	}
}
