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
 * Created on Oct 29, 2005
 */
package org.lobobrowser.html.domimpl;

import org.lobobrowser.util.Objects;
import org.lobobrowser.util.Strings;
import org.w3c.dom.*;

import java.util.*;

public class ElementImpl extends NodeImpl implements Element {
	private final String name;

	ElementImpl(String name) {
		super();
		this.name = name;
	}

	protected Map<String, String> attributes;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xamjwg.html.domimpl.NodeImpl#getattributes()
	 */
	public NamedNodeMap getAttributes() {
		synchronized (this) {
			Map<String, String> attrs = this.attributes;
			if (attrs == null) {
				attrs = new HashMap<>();
				this.attributes = attrs;
			}
			return new NamedNodeMapImpl(this, this.attributes);
		}
	}

	public boolean hasAttributes() {
		synchronized (this) {
			Map attrs = this.attributes;
			return attrs != null && !attrs.isEmpty();
		}
	}

	public boolean equalAttributes(Node arg) {
		if (arg instanceof ElementImpl) {
			synchronized (this) {
				Map attrs1 = this.attributes;
				if (attrs1 == null) {
					attrs1 = Collections.EMPTY_MAP;
				}
				Map attrs2 = ((ElementImpl) arg).attributes;
				if (attrs2 == null) {
					attrs2 = Collections.EMPTY_MAP;
				}
				return Objects.equals(attrs1, attrs2);
			}
		} else {
			return false;
		}
	}

	private String id;

	public String getId() {
		String id = this.id;
		return id == null ? "" : id;
	}

	public void setId(String id) {
		this.setAttribute("id", id);
	}

	//private String title;

	public String getTitle() {
		return this.getAttribute("title");
	}

	public void setTitle(String title) {
		this.setAttribute("title", title);
	}

	public String getLang() {
		return this.getAttribute("lang");
	}

	public void setLang(String lang) {
		this.setAttribute("lang", lang);
	}

	public final String getAttribute(String name) {
		String normalName = this.normalizeAttributeName(name);
		synchronized (this) {
			Map<String, String> attributes = this.attributes;
			return attributes == null ? null : attributes.get(normalName);
		}
	}

	private Attr getAttr(String normalName, String value) {
		return new AttrImpl(normalName, value, true, this, "id"
				.equals(normalName));
	}

	public Attr getAttributeNode(String name) {
		String normalName = this.normalizeAttributeName(name);
		synchronized (this) {
			Map<String, String> attributes = this.attributes;
			String value = attributes == null ? null : attributes.get(normalName);
			return value == null ? null : this.getAttr(normalName, value);
		}
	}

	public Attr getAttributeNodeNS(String namespaceURI, String localName)
			throws DOMException {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR,
				"Namespaces not supported");
	}

	public String getAttributeNS(String namespaceURI, String localName)
			throws DOMException {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR,
				"Namespaces not supported");
	}

	private static boolean isTagName(Node node, String name) {
		return node.getNodeName().equalsIgnoreCase(name);
	}

	public NodeList getElementsByTagName(String name) {
		boolean matchesAll = "*".equals(name);
		List<Node> descendents = new LinkedList<>();
		synchronized (this.treeLock) {
			ArrayList<Node> nl = this.nodeList;
			if (nl != null) {
				for (Node child : nl) {
					if (child instanceof Element) {
						Element childElement = (Element) child;
						if (matchesAll || isTagName(childElement, name)) {
							descendents.add(child);
						}
						NodeList sublist = childElement.getElementsByTagName(name);
						int length = sublist.getLength();
						for (int idx = 0; idx < length; idx++) {
							descendents.add(sublist.item(idx));
						}
					}
				}
			}
		}
		return new NodeListImpl(descendents);
	}

	public NodeList getElementsByTagNameNS(String namespaceURI, String localName)
			throws DOMException {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR,
				"Namespaces not supported");
	}

	public TypeInfo getSchemaTypeInfo() {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR,
				"Namespaces not supported");
	}

	public String getTagName() {
		return this.getNodeName();
	}

	public boolean hasAttribute(String name) {
		String normalName = this.normalizeAttributeName(name);
		synchronized (this) {
			Map<String, String> attributes = this.attributes;
			return attributes != null && attributes.containsKey(normalName);
		}
	}

	public boolean hasAttributeNS(String namespaceURI, String localName)
			throws DOMException {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR,
				"Namespaces not supported");
	}

	public void removeAttribute(String name) throws DOMException {
		String normalName = this.normalizeAttributeName(name);
		synchronized (this) {
			Map<String, String> attributes = this.attributes;
			if (attributes == null) {
				return;
			}
			attributes.remove(normalName);
		}
	}

	public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
		String normalName = this.normalizeAttributeName(oldAttr.getName());
		synchronized (this) {
			Map<String, String> attributes = this.attributes;
			if (attributes == null) {
				return null;
			}
			String oldValue = attributes.remove(normalName);
			return oldValue == null ? null : this.getAttr(normalName, oldValue);
		}
	}

	public void removeAttributeNS(String namespaceURI, String localName)
			throws DOMException {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR,
				"Namespaces not supported");
	}

	protected void assignAttributeField(String normalName, String value) {
		// Note: overriders assume that processing here is only done after
		// checking attribute names, i.e. they may not call the super
		// implementation if an attribute is already taken care of.
		boolean isName = false;
		if ("id".equals(normalName) || (isName = "name".equals(normalName))) {
			// Note that the value of name is used
			// as an ID, but the value of ID is not
			// used as a name.
			if (!isName) {
				this.id = value;
			}
			HTMLDocumentImpl document = (HTMLDocumentImpl) this.document;
			if (document != null) {
				document.setElementById(value, this);
			}
		}
	}

	final String normalizeAttributeName(String name) {
		return name.toLowerCase();
	}

	public void setAttribute(String name, String value) throws DOMException {
		String normalName = this.normalizeAttributeName(name);
		synchronized (this) {
			Map<String, String> attribs = this.attributes;
			if (attribs == null) {
				attribs = new HashMap<>(2);
				this.attributes = attribs;
			}
			attribs.put(normalName, value);
		}
		this.assignAttributeField(normalName, value);
	}

	public Attr setAttributeNode(Attr newAttr) throws DOMException {
		String normalName = this.normalizeAttributeName(newAttr.getName());
		String value = newAttr.getValue();
		synchronized (this) {
			if (this.attributes == null) {
				this.attributes = new HashMap<>();
			}
			this.attributes.put(normalName, value);
		}
		this.assignAttributeField(normalName, value);
		return newAttr;
	}

	public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR,
				"Namespaces not supported");
	}

	public void setAttributeNS(String namespaceURI, String qualifiedName,
	                           String value) throws DOMException {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR,
				"Namespaces not supported");
	}

	public void setIdAttribute(String name, boolean isId) throws DOMException {
		String normalName = this.normalizeAttributeName(name);
		if (!"id".equals(normalName)) {
			throw new DOMException(DOMException.NOT_SUPPORTED_ERR,
					"IdAttribute can't be anything other than ID");
		}
	}

	public void setIdAttributeNode(Attr idAttr, boolean isId)
			throws DOMException {
		String normalName = this.normalizeAttributeName(idAttr.getName());
		if (!"id".equals(normalName)) {
			throw new DOMException(DOMException.NOT_SUPPORTED_ERR,
					"IdAttribute can't be anything other than ID");
		}
	}

	public void setIdAttributeNS(String namespaceURI, String localName,
	                             boolean isId) throws DOMException {
		throw new DOMException(DOMException.NOT_SUPPORTED_ERR,
				"Namespaces not supported");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xamjwg.html.domimpl.NodeImpl#getLocalName()
	 */
	public String getLocalName() {
		return this.getNodeName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xamjwg.html.domimpl.NodeImpl#getNodeName()
	 */
	public String getNodeName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xamjwg.html.domimpl.NodeImpl#getNodeType()
	 */
	public short getNodeType() {
		return Node.ELEMENT_NODE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xamjwg.html.domimpl.NodeImpl#getNodeValue()
	 */
	public String getNodeValue() throws DOMException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xamjwg.html.domimpl.NodeImpl#setNodeValue(java.lang.String)
	 */
	public void setNodeValue(String nodeValue) throws DOMException {
		// nop
	}

	/**
	 * Gets inner text of the element, possibly including text in comments.
	 * This can be used to get Javascript code out of a SCRIPT element.
	 *
	 * @param includeComment comment
	 */
	String getRawInnerText(boolean includeComment) {
		synchronized (this.treeLock) {
			ArrayList nl = this.nodeList;
			if (nl != null) {
				Iterator i = nl.iterator();
				StringBuffer sb = null;
				while (i.hasNext()) {
					Object node = i.next();
					if (node instanceof Text) {
						Text tn = (Text) node;
						String txt = tn.getNodeValue();
						if (!"".equals(txt)) {
							if (sb == null) {
								sb = new StringBuffer();
							}
							sb.append(txt);
						}
					} else if (node instanceof ElementImpl) {
						ElementImpl en = (ElementImpl) node;
						String txt = en.getRawInnerText(includeComment);
						if (!"".equals(txt)) {
							if (sb == null) {
								sb = new StringBuffer();
							}
							sb.append(txt);
						}
					} else if (includeComment && node instanceof Comment) {
						Comment cn = (Comment) node;
						String txt = cn.getNodeValue();
						if (!"".equals(txt)) {
							if (sb == null) {
								sb = new StringBuffer();
							}
							sb.append(txt);
						}
					}
				}
				return sb == null ? "" : sb.toString();
			} else {
				return "";
			}
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getNodeName());
		sb.append(" [");
		NamedNodeMap attribs = this.getAttributes();
		int length = attribs.getLength();
		for (int i = 0; i < length; i++) {
			Attr attr = (Attr) attribs.item(i);
			sb.append(attr.getNodeName());
			sb.append('=');
			sb.append(attr.getNodeValue());
			if (i + 1 < length) {
				sb.append(',');
			}
		}
		sb.append("]");
		return sb.toString();
	}

	protected Node createSimilarNode() {
		HTMLDocumentImpl doc = (HTMLDocumentImpl) this.document;
		return doc == null ? null : doc.createElement(this.getTagName());
	}

	protected String htmlEncodeChildText(String text) {
		if (org.lobobrowser.html.parser.HtmlParser.isDecodeEntities(this.name)) {
			return Strings.strictHtmlEncode(text, false);
		} else {
			return text;
		}
	}


}
