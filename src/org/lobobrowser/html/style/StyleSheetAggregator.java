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

package org.lobobrowser.html.style;

import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.domimpl.HTMLDocumentImpl;
import org.lobobrowser.html.domimpl.HTMLElementImpl;
import org.w3c.dom.css.*;
import org.w3c.dom.stylesheets.MediaList;

import java.net.MalformedURLException;
import java.util.*;

/**
 * Aggregates all style sheets in a document.
 * Every time a new STYLE element is found, it is
 * added to the style sheet aggreagator by means
 * of the {@link #addStyleSheet(CSSStyleSheet)} method.
 */
public class StyleSheetAggregator {
	private final HTMLDocumentImpl document;
	private final Map<String, Map<String, Collection<StyleRuleInfo>>> classMapsByElement = new HashMap<>();
	private final Map<String, Map<String, Collection<StyleRuleInfo>>> idMapsByElement = new HashMap<>();
	private final Map<String, Collection<StyleRuleInfo>> rulesByElement = new HashMap<>();

	public StyleSheetAggregator(HTMLDocumentImpl document) {
		this.document = document;
	}

	public final void addStyleSheets(Collection<CSSStyleSheet> styleSheets) throws MalformedURLException {
		for (CSSStyleSheet styleSheet : styleSheets) {
			this.addStyleSheet(styleSheet);
		}
	}

	private void addStyleSheet(CSSStyleSheet styleSheet) throws MalformedURLException {
		CSSRuleList ruleList = styleSheet.getCssRules();
		int length = ruleList.getLength();
		for (int i = 0; i < length; i++) {
			CSSRule rule = ruleList.item(i);
			this.addRule(styleSheet, rule);
		}
	}

	private void addRule(CSSStyleSheet styleSheet, CSSRule rule) throws MalformedURLException {
		HTMLDocumentImpl document = this.document;
		if (rule instanceof CSSStyleRule) {
			CSSStyleRule sr = (CSSStyleRule) rule;
			String selectorList = sr.getSelectorText();
			StringTokenizer commaTok = new StringTokenizer(selectorList, ",");
			while (commaTok.hasMoreTokens()) {
				String selectorPart = commaTok.nextToken().toLowerCase();
				ArrayList<SimpleSelector> simpleSelectors = null;
				String lastSelectorText = null;
				StringTokenizer tok = new StringTokenizer(selectorPart, " \t\r\n");
				if (tok.hasMoreTokens()) {
					simpleSelectors = new ArrayList<>();
					SimpleSelector prevSelector = null;
					for (; ; ) {
						String token = tok.nextToken();
						if (">".equals(token)) {
							if (prevSelector != null) {
								prevSelector.setSelectorType(SimpleSelector.PARENT);
							}
							continue;
						} else if ("+".equals(token)) {
							if (prevSelector != null) {
								prevSelector.setSelectorType(SimpleSelector.PRECEEDING_SIBLING);
							}
							continue;
						}
						int colonIdx = token.indexOf(':');
						String simpleSelectorText = colonIdx == -1 ? token : token.substring(0, colonIdx);
						String pseudoElement = colonIdx == -1 ? null : token.substring(colonIdx + 1);
						prevSelector = new SimpleSelector(simpleSelectorText, pseudoElement);
						simpleSelectors.add(prevSelector);
						if (!tok.hasMoreTokens()) {
							lastSelectorText = simpleSelectorText;
							break;
						}
					}
				}
				if (lastSelectorText != null) {
					int dotIdx = lastSelectorText.indexOf('.');
					if (dotIdx != -1) {
						String elemtl = lastSelectorText.substring(0, dotIdx);
						String classtl = lastSelectorText.substring(dotIdx + 1);
						this.addClassRule(elemtl, classtl, sr, simpleSelectors);
					} else {
						int poundIdx = lastSelectorText.indexOf('#');
						if (poundIdx != -1) {
							String elemtl = lastSelectorText.substring(0, poundIdx);
							String idtl = lastSelectorText.substring(poundIdx + 1);
							this.addIdRule(elemtl, idtl, sr, simpleSelectors);
						} else {
							this.addElementRule(lastSelectorText, sr, simpleSelectors);
						}
					}
				}
			}
		} else if (rule instanceof CSSImportRule) {
			UserAgentContext uacontext = document.getUserAgentContext();
			if (uacontext.isExternalCSSEnabled()) {
				CSSImportRule importRule = (CSSImportRule) rule;
				if (CSSUtilities.matchesMedia(importRule.getMedia(), uacontext)) {
					String href = importRule.getHref();
					String styleHref = styleSheet.getHref();
					String baseHref = styleHref == null ? document.getBaseURI() : styleHref;
					CSSStyleSheet sheet = CSSUtilities.parse(styleSheet.getOwnerNode(), href, document, baseHref, false);
					if (sheet != null) {
						this.addStyleSheet(sheet);
					}
				}
			}
		} else if (rule instanceof CSSMediaRule) {
			CSSMediaRule mrule = (CSSMediaRule) rule;
			MediaList mediaList = mrule.getMedia();
			if (CSSUtilities.matchesMedia(mediaList, document.getUserAgentContext())) {
				CSSRuleList ruleList = mrule.getCssRules();
				int length = ruleList.getLength();
				for (int i = 0; i < length; i++) {
					CSSRule subRule = ruleList.item(i);
					this.addRule(styleSheet, subRule);
				}
			}
		}
	}

	private void addClassRule(String elemtl, String classtl, CSSStyleRule styleRule, ArrayList ancestorSelectors) {
		Map<String, Collection<StyleRuleInfo>> classMap = this.classMapsByElement.computeIfAbsent(elemtl, k -> new HashMap<>());
		Collection<StyleRuleInfo> rules = classMap.computeIfAbsent(classtl, k -> new LinkedList<>());
		rules.add(new StyleRuleInfo(ancestorSelectors, styleRule));
	}

	private void addIdRule(String elemtl, String idtl, CSSStyleRule styleRule, ArrayList ancestorSelectors) {
		Map<String, Collection<StyleRuleInfo>> idsMap = this.idMapsByElement.computeIfAbsent(elemtl, k -> new HashMap<>());
		Collection<StyleRuleInfo> rules = idsMap.computeIfAbsent(idtl, k -> new LinkedList<>());
		rules.add(new StyleRuleInfo(ancestorSelectors, styleRule));
	}

	private void addElementRule(String elemtl, CSSStyleRule styleRule, ArrayList ancestorSelectors) {
		Collection<StyleRuleInfo> rules = this.rulesByElement.computeIfAbsent(elemtl, k -> new LinkedList<>());
		rules.add(new StyleRuleInfo(ancestorSelectors, styleRule));
	}

	public final Collection<CSSStyleDeclaration> getActiveStyleDeclarations(HTMLElementImpl element, String elementName, String elementId, String className, Set pseudoNames) {
		Collection<CSSStyleDeclaration> styleDeclarations = null;
		String elementTL = elementName.toLowerCase();
		Collection<StyleRuleInfo> elementRules = this.rulesByElement.get(elementTL);
		if (elementRules != null) {
			for (StyleRuleInfo elementRule : elementRules) {
				if (elementRule.isSelectorMatch(element, pseudoNames)) {
					CSSStyleRule styleRule = elementRule.styleRule;
					CSSStyleSheet styleSheet = styleRule.getParentStyleSheet();
					if (styleSheet != null && styleSheet.getDisabled()) {
						continue;
					}
					if (styleDeclarations == null) {
						styleDeclarations = new LinkedList<>();
					}
					styleDeclarations.add(styleRule.getStyle());
				}
			}
		}
		elementRules = this.rulesByElement.get("*");
		if (elementRules != null) {
			for (StyleRuleInfo styleRuleInfo : elementRules) {
				if (styleRuleInfo.isSelectorMatch(element, pseudoNames)) {
					CSSStyleRule styleRule = styleRuleInfo.styleRule;
					CSSStyleSheet styleSheet = styleRule.getParentStyleSheet();
					if (styleSheet != null && styleSheet.getDisabled()) {
						continue;
					}
					if (styleDeclarations == null) {
						styleDeclarations = new LinkedList<>();
					}
					styleDeclarations.add(styleRule.getStyle());
				}
			}
		}
		if (className != null) {
			String classNameTL = className.toLowerCase();
			Map<String, Collection<StyleRuleInfo>> classMaps = this.classMapsByElement.get(elementTL);
			if (classMaps != null) {
				Collection<StyleRuleInfo> classRules = classMaps.get(classNameTL);
				if (classRules != null) {
					for (StyleRuleInfo styleRuleInfo : classRules) {
						if (styleRuleInfo.isSelectorMatch(element, pseudoNames)) {
							CSSStyleRule styleRule = styleRuleInfo.styleRule;
							CSSStyleSheet styleSheet = styleRule.getParentStyleSheet();
							if (styleSheet != null && styleSheet.getDisabled()) {
								continue;
							}
							if (styleDeclarations == null) {
								styleDeclarations = new LinkedList<>();
							}
							styleDeclarations.add(styleRule.getStyle());
						}
					}
				}
			}
			classMaps = this.classMapsByElement.get("*");
			if (classMaps != null) {
				Collection<StyleRuleInfo> classRules = classMaps.get(classNameTL);
				if (classRules != null) {
					for (StyleRuleInfo classRule : classRules) {
						if (classRule.isSelectorMatch(element, pseudoNames)) {
							CSSStyleRule styleRule = classRule.styleRule;
							CSSStyleSheet styleSheet = styleRule.getParentStyleSheet();
							if (styleSheet != null && styleSheet.getDisabled()) {
								continue;
							}
							if (styleDeclarations == null) {
								styleDeclarations = new LinkedList<>();
							}
							styleDeclarations.add(styleRule.getStyle());
						}
					}
				}
			}
		}
		if (elementId != null) {
			Map<String, Collection<StyleRuleInfo>> idMaps = this.idMapsByElement.get(elementTL);
			if (idMaps != null) {
				String elementIdTL = elementId.toLowerCase();
				Collection<StyleRuleInfo> idRules = idMaps.get(elementIdTL);
				if (idRules != null) {
					for (StyleRuleInfo idRule : idRules) {
						if (idRule.isSelectorMatch(element, pseudoNames)) {
							CSSStyleRule styleRule = idRule.styleRule;
							CSSStyleSheet styleSheet = styleRule.getParentStyleSheet();
							if (styleSheet != null && styleSheet.getDisabled()) {
								continue;
							}
							if (styleDeclarations == null) {
								styleDeclarations = new LinkedList<>();
							}
							styleDeclarations.add(styleRule.getStyle());
						}
					}
				}
			}
			idMaps = this.idMapsByElement.get("*");
			if (idMaps != null) {
				String elementIdTL = elementId.toLowerCase();
				Collection<StyleRuleInfo> idRules = idMaps.get(elementIdTL);
				if (idRules != null) {
					for (StyleRuleInfo styleRuleInfo : idRules) {
						if (styleRuleInfo.isSelectorMatch(element, pseudoNames)) {
							CSSStyleRule styleRule = styleRuleInfo.styleRule;
							CSSStyleSheet styleSheet = styleRule.getParentStyleSheet();
							if (styleSheet != null && styleSheet.getDisabled()) {
								continue;
							}
							if (styleDeclarations == null) {
								styleDeclarations = new LinkedList<>();
							}
							styleDeclarations.add(styleRule.getStyle());
						}
					}
				}
			}
		}
		return styleDeclarations;
	}

	public final boolean affectedByPseudoNameInAncestor(HTMLElementImpl element, HTMLElementImpl ancestor, String elementName, String elementId, String[] classArray, String pseudoName) {
		String elementTL = elementName.toLowerCase();
		Collection<StyleRuleInfo> elementRules = this.rulesByElement.get(elementTL);
		if (elementRules != null) {
			for (StyleRuleInfo styleRuleInfo : elementRules) {
				CSSStyleSheet styleSheet = styleRuleInfo.styleRule.getParentStyleSheet();
				if (styleSheet != null && styleSheet.getDisabled()) {
					continue;
				}
				if (styleRuleInfo.affectedByPseudoNameInAncestor(element, ancestor, pseudoName)) {
					return true;
				}
			}
		}
		elementRules = this.rulesByElement.get("*");
		if (elementRules != null) {
			for (StyleRuleInfo styleRuleInfo : elementRules) {
				CSSStyleSheet styleSheet = styleRuleInfo.styleRule.getParentStyleSheet();
				if (styleSheet != null && styleSheet.getDisabled()) {
					continue;
				}
				if (styleRuleInfo.affectedByPseudoNameInAncestor(element, ancestor, pseudoName)) {
					return true;
				}
			}
		}
		if (classArray != null) {
			for (String className : classArray) {
				String classNameTL = className.toLowerCase();
				Map<String, Collection<StyleRuleInfo>> classMaps = this.classMapsByElement.get(elementTL);
				if (classMaps != null) {
					Collection<StyleRuleInfo> classRules = classMaps.get(classNameTL);
					if (classRules != null) {
						for (StyleRuleInfo styleRuleInfo : classRules) {
							CSSStyleSheet styleSheet = styleRuleInfo.styleRule.getParentStyleSheet();
							if (styleSheet != null && styleSheet.getDisabled()) {
								continue;
							}
							if (styleRuleInfo.affectedByPseudoNameInAncestor(element, ancestor, pseudoName)) {
								return true;
							}
						}
					}
				}
				classMaps = this.classMapsByElement.get("*");
				if (classMaps != null) {
					Collection<StyleRuleInfo> classRules = classMaps.get(classNameTL);
					if (classRules != null) {
						for (StyleRuleInfo styleRuleInfo : classRules) {
							CSSStyleSheet styleSheet = styleRuleInfo.styleRule.getParentStyleSheet();
							if (styleSheet != null && styleSheet.getDisabled()) {
								continue;
							}
							if (styleRuleInfo.affectedByPseudoNameInAncestor(element, ancestor, pseudoName)) {
								return true;
							}
						}
					}
				}
			}
		}
		if (elementId != null) {
			Map<String, Collection<StyleRuleInfo>> idMaps = this.idMapsByElement.get(elementTL);
			if (idMaps != null) {
				String elementIdTL = elementId.toLowerCase();
				Collection<StyleRuleInfo> idRules = idMaps.get(elementIdTL);
				if (idRules != null) {
					for (StyleRuleInfo idRule : idRules) {
						CSSStyleSheet styleSheet = idRule.styleRule.getParentStyleSheet();
						if (styleSheet != null && styleSheet.getDisabled()) {
							continue;
						}
						if (idRule.affectedByPseudoNameInAncestor(element, ancestor, pseudoName)) {
							return true;
						}
					}
				}
			}
			idMaps = this.idMapsByElement.get("*");
			if (idMaps != null) {
				String elementIdTL = elementId.toLowerCase();
				Collection<StyleRuleInfo> idRules = idMaps.get(elementIdTL);
				if (idRules != null) {
					for (StyleRuleInfo styleRuleInfo : idRules) {
						CSSStyleSheet styleSheet = styleRuleInfo.styleRule.getParentStyleSheet();
						if (styleSheet != null && styleSheet.getDisabled()) {
							continue;
						}
						if (styleRuleInfo.affectedByPseudoNameInAncestor(element, ancestor, pseudoName)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private static class StyleRuleInfo {
		private final CSSStyleRule styleRule;
		private final ArrayList ancestorSelectors;

		StyleRuleInfo(ArrayList simpleSelectors, CSSStyleRule rule) {
			super();
			ancestorSelectors = simpleSelectors;
			styleRule = rule;
		}

		final boolean affectedByPseudoNameInAncestor(HTMLElementImpl element, HTMLElementImpl ancestor, String pseudoName) {
			ArrayList as = this.ancestorSelectors;
			HTMLElementImpl currentElement = element;
			int size = as.size();
			boolean first = true;
			for (int i = size; --i >= 0; ) {
				SimpleSelector simpleSelector = (SimpleSelector) as.get(i);
				if (first) {
					if (ancestor == element) {
						return simpleSelector.hasPseudoName(pseudoName);
					}
					first = false;
					continue;
				}
				String selectorText = simpleSelector.simpleSelectorText;
				int dotIdx = selectorText.indexOf('.');
				HTMLElementImpl newElement;
				if (dotIdx != -1) {
					String elemtl = selectorText.substring(0, dotIdx);
					String classtl = selectorText.substring(dotIdx + 1);
					newElement = currentElement.getAncestorWithClass(elemtl, classtl);
				} else {
					int poundIdx = selectorText.indexOf('#');
					if (poundIdx != -1) {
						String elemtl = selectorText.substring(0, poundIdx);
						String idtl = selectorText.substring(poundIdx + 1);
						newElement = currentElement.getAncestorWithId(elemtl, idtl);
					} else {
						newElement = currentElement.getAncestor(selectorText);
					}
				}
				if (newElement == null) {
					return false;
				}
				currentElement = newElement;
				if (currentElement == ancestor) {
					return simpleSelector.hasPseudoName(pseudoName);
				}
			}
			return false;
		}

		/**
		 * @param element     The element to test for a match.
		 * @param pseudoNames A set of pseudo-names in lowercase.
		 */
		private boolean isSelectorMatch(HTMLElementImpl element, Set pseudoNames) {
			ArrayList as = this.ancestorSelectors;
			HTMLElementImpl currentElement = element;
			int size = as.size();
			boolean first = true;
			for (int i = size; --i >= 0; ) {
				SimpleSelector simpleSelector = (SimpleSelector) as.get(i);
				if (first) {
					if (!simpleSelector.matches(pseudoNames)) {
						return false;
					}
					first = false;
					continue;
				}
				String selectorText = simpleSelector.simpleSelectorText;
				int dotIdx = selectorText.indexOf('.');
				int selectorType = simpleSelector.selectorType;
				HTMLElementImpl priorElement;
				if (dotIdx != -1) {
					String elemtl = selectorText.substring(0, dotIdx);
					String classtl = selectorText.substring(dotIdx + 1);
					if (selectorType == SimpleSelector.ANCESTOR) {
						priorElement = currentElement.getAncestorWithClass(elemtl, classtl);
					} else if (selectorType == SimpleSelector.PARENT) {
						priorElement = currentElement.getParentWithClass(elemtl, classtl);
					} else if (selectorType == SimpleSelector.PRECEEDING_SIBLING) {
						priorElement = currentElement.getPreceedingSiblingWithClass(elemtl, classtl);
					} else {
						throw new IllegalStateException("selectorType=" + selectorType);
					}
				} else {
					int poundIdx = selectorText.indexOf('#');
					if (poundIdx != -1) {
						String elemtl = selectorText.substring(0, poundIdx);
						String idtl = selectorText.substring(poundIdx + 1);
						if (selectorType == SimpleSelector.ANCESTOR) {
							priorElement = currentElement.getAncestorWithId(elemtl, idtl);
						} else if (selectorType == SimpleSelector.PARENT) {
							priorElement = currentElement.getParentWithId(elemtl, idtl);
						} else if (selectorType == SimpleSelector.PRECEEDING_SIBLING) {
							priorElement = currentElement.getPreceedingSiblingWithId(elemtl, idtl);
						} else {
							throw new IllegalStateException("selectorType=" + selectorType);
						}
					} else {
						if (selectorType == SimpleSelector.ANCESTOR) {
							priorElement = currentElement.getAncestor(selectorText);
						} else if (selectorType == SimpleSelector.PARENT) {
							priorElement = currentElement.getParent(selectorText);
						} else if (selectorType == SimpleSelector.PRECEEDING_SIBLING) {
							priorElement = currentElement.getPreceedingSibling(selectorText);
						} else {
							throw new IllegalStateException("selectorType=" + selectorType);
						}
					}
				}
				if (priorElement == null) {
					return false;
				}
				if (!simpleSelector.matches(priorElement)) {
					return false;
				}
				currentElement = priorElement;
			}
			return true;
		}
	}

	static class SimpleSelector {
		static final int ANCESTOR = 0;
		static final int PARENT = 1;
		static final int PRECEEDING_SIBLING = 2;

		final String simpleSelectorText;
		final String pseudoElement;
		int selectorType;

		/**
		 * @param simpleSelectorText Simple selector text in lower case.
		 * @param pseudoElement      The pseudo-element if any.
		 */
		public SimpleSelector(String simpleSelectorText, String pseudoElement) {
			super();
			this.simpleSelectorText = simpleSelectorText;
			this.pseudoElement = pseudoElement;
			this.selectorType = ANCESTOR;
		}

		final boolean matches(HTMLElementImpl element) {
			Set names = element.getPseudoNames();
			if (names == null) {
				return this.pseudoElement == null;
			} else {
				String pe = this.pseudoElement;
				return pe == null || names.contains(pe);
			}
		}

		final boolean matches(Set names) {
			if (names == null) {
				return this.pseudoElement == null;
			} else {
				String pe = this.pseudoElement;
				return pe == null || names.contains(pe);
			}
		}

		final boolean hasPseudoName(String pseudoName) {
			return pseudoName.equals(this.pseudoElement);
		}

		void setSelectorType(int selectorType) {
			this.selectorType = selectorType;
		}
	}
}
