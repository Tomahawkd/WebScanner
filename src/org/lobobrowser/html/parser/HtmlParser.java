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
 * Created on Aug 28, 2005
 */
package org.lobobrowser.html.parser;

import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.io.WritableLineReader;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.*;

/**
 * The <code>HtmlParser</code> class is an HTML DOM parser.
 * This parser provides the functionality for
 * the standard DOM parser implementation {@link org.lobobrowser.html.parser.DocumentBuilderImpl}.
 * This parser class may be used directly when a different DOM
 * implementation is preferred.
 */
public class HtmlParser {
	private final Document document;
	private final UserAgentContext ucontext;

	private static final Map<String, Character> ENTITIES = new HashMap<>(256);
	private static final Map<String, ElementInfo> ELEMENT_INFOS = new HashMap<>(35);

	/**
	 * A node <code>UserData</code> key used to tell
	 * nodes that their content may be about to be
	 * modified. Elements could use this to temporarily
	 * suspend notifications. The value set
	 * will be either <code>Boolean.TRUE</code> or
	 * <code>Boolean.FALSE</code>.
	 */
	public static final String MODIFYING_KEY = "cobra.suspend";

	static {
		Map<String, Character> entities = ENTITIES;
		entities.put("amp", '&');
		entities.put("lt", '<');
		entities.put("gt", '>');
		entities.put("quot", '"');
		entities.put("nbsp", (char) 160);

		entities.put("lsquo", '\u2018');
		entities.put("rsquo", '\u2019');

		entities.put("frasl", (char) 47);
		entities.put("ndash", (char) 8211);
		entities.put("mdash", (char) 8212);
		entities.put("iexcl", (char) 161);
		entities.put("cent", (char) 162);
		entities.put("pound", (char) 163);
		entities.put("curren", (char) 164);
		entities.put("yen", (char) 165);
		entities.put("brvbar", (char) 166);
		entities.put("brkbar", (char) 166);
		entities.put("sect", (char) 167);
		entities.put("uml", (char) 168);
		entities.put("die", (char) 168);
		entities.put("copy", (char) 169);
		entities.put("ordf", (char) 170);
		entities.put("laquo", (char) 171);
		entities.put("not", (char) 172);
		entities.put("shy", (char) 173);
		entities.put("reg", (char) 174);
		entities.put("macr", (char) 175);
		entities.put("hibar", (char) 175);
		entities.put("deg", (char) 176);
		entities.put("plusmn", (char) 177);
		entities.put("sup2", (char) 178);
		entities.put("sup3", (char) 179);
		entities.put("acute", (char) 180);
		entities.put("micro", (char) 181);
		entities.put("para", (char) 182);
		entities.put("middot", (char) 183);
		entities.put("cedil", (char) 184);
		entities.put("sup1", (char) 185);
		entities.put("ordm", (char) 186);
		entities.put("raquo", (char) 187);
		entities.put("frac14", (char) 188);
		entities.put("frac12", (char) 189);
		entities.put("frac34", (char) 190);
		entities.put("iquest", (char) 191);
		entities.put("Agrave", (char) 192);
		entities.put("Aacute", (char) 193);
		entities.put("Acirc", (char) 194);
		entities.put("Atilde", (char) 195);
		entities.put("Auml", (char) 196);
		entities.put("Aring", (char) 197);
		entities.put("AElig", (char) 198);
		entities.put("Ccedil", (char) 199);
		entities.put("Egrave", (char) 200);
		entities.put("Eacute", (char) 201);
		entities.put("Ecirc", (char) 202);
		entities.put("Euml", (char) 203);
		entities.put("Igrave", (char) 204);
		entities.put("Iacute", (char) 205);
		entities.put("Icirc", (char) 206);
		entities.put("Iuml", (char) 207);
		entities.put("ETH", (char) 208);
		entities.put("Ntilde", (char) 209);
		entities.put("Ograve", (char) 210);
		entities.put("Oacute", (char) 211);
		entities.put("Ocirc", (char) 212);
		entities.put("Otilde", (char) 213);
		entities.put("Ouml", (char) 214);
		entities.put("times", (char) 215);
		entities.put("Oslash", (char) 216);
		entities.put("Ugrave", (char) 217);
		entities.put("Uacute", (char) 218);
		entities.put("Ucirc", (char) 219);
		entities.put("Uuml", (char) 220);
		entities.put("Yacute", (char) 221);
		entities.put("THORN", (char) 222);
		entities.put("szlig", (char) 223);
		entities.put("agrave", (char) 224);
		entities.put("aacute", (char) 225);
		entities.put("acirc", (char) 226);
		entities.put("atilde", (char) 227);
		entities.put("auml", (char) 228);
		entities.put("aring", (char) 229);
		entities.put("aelig", (char) 230);
		entities.put("ccedil", (char) 231);
		entities.put("egrave", (char) 232);
		entities.put("eacute", (char) 233);
		entities.put("ecirc", (char) 234);
		entities.put("euml", (char) 235);
		entities.put("igrave", (char) 236);
		entities.put("iacute", (char) 237);
		entities.put("icirc", (char) 238);
		entities.put("iuml", (char) 239);
		entities.put("eth", (char) 240);
		entities.put("ntilde", (char) 241);
		entities.put("ograve", (char) 242);
		entities.put("oacute", (char) 243);
		entities.put("ocirc", (char) 244);
		entities.put("otilde", (char) 245);
		entities.put("ouml", (char) 246);
		entities.put("divide", (char) 247);
		entities.put("oslash", (char) 248);
		entities.put("ugrave", (char) 249);
		entities.put("uacute", (char) 250);
		entities.put("ucirc", (char) 251);
		entities.put("uuml", (char) 252);
		entities.put("yacute", (char) 253);
		entities.put("thorn", (char) 254);
		entities.put("yuml", (char) 255);

		//greek letters
		entities.put("Alpha", (char) 913);
		entities.put("Beta", (char) 914);
		entities.put("Gamma", (char) 915);
		entities.put("Delta", (char) 916);
		entities.put("Epsilon", (char) 917);
		entities.put("Zeta", (char) 918);
		entities.put("Eta", (char) 919);
		entities.put("Theta", (char) 920);
		entities.put("Iota", (char) 921);
		entities.put("Kappa", (char) 922);
		entities.put("Lambda", (char) 923);
		entities.put("Mu", (char) 924);
		entities.put("Nu", (char) 925);
		entities.put("Xi", (char) 926);
		entities.put("Omicron", (char) 927);
		entities.put("Pi", (char) 928);
		entities.put("Rho", (char) 929);
		entities.put("Sigma", (char) 930);
		entities.put("Sigmaf", (char) 931);
		entities.put("Tau", (char) 932);
		entities.put("Upsilon", (char) 933);
		entities.put("Phi", (char) 934);
		entities.put("Chi", (char) 935);
		entities.put("Psi", (char) 936);
		entities.put("Omega", (char) 937);

		entities.put("alpha", (char) 945);
		entities.put("beta", (char) 946);
		entities.put("gamma", (char) 947);
		entities.put("delta", (char) 948);
		entities.put("epsilon", (char) 949);
		entities.put("zeta", (char) 950);
		entities.put("eta", (char) 951);
		entities.put("theta", (char) 952);
		entities.put("iota", (char) 953);
		entities.put("kappa", (char) 954);
		entities.put("lambda", (char) 955);
		entities.put("mu", (char) 956);
		entities.put("nu", (char) 957);
		entities.put("xi", (char) 958);
		entities.put("omicron", (char) 959);
		entities.put("pi", (char) 960);
		entities.put("rho", (char) 961);
		entities.put("sigma", (char) 962);
		entities.put("sigmaf", (char) 963);
		entities.put("tau", (char) 964);
		entities.put("upsilon", (char) 965);
		entities.put("phi", (char) 966);
		entities.put("chi", (char) 967);
		entities.put("psi", (char) 968);
		entities.put("omega", (char) 969);
		entities.put("thetasym", (char) 977);
		entities.put("upsih", (char) 978);
		entities.put("piv", (char) 982);

		//math symbols
		entities.put("forall", (char) 8704);
		entities.put("part", (char) 8706);
		entities.put("exist", (char) 8707);
		entities.put("empty", (char) 8709);
		entities.put("nabla", (char) 8711);
		entities.put("isin", (char) 8712);
		entities.put("notin", (char) 8713);
		entities.put("ni", (char) 8715);
		entities.put("prod", (char) 8719);
		entities.put("sum", (char) 8721);
		entities.put("minus", (char) 8722);
		entities.put("lowast", (char) 8727);
		entities.put("radic", (char) 8730);
		entities.put("prop", (char) 8733);
		entities.put("infin", (char) 8734);
		entities.put("ang", (char) 8736);
		entities.put("and", (char) 8743);
		entities.put("or", (char) 8744);
		entities.put("cap", (char) 8745);
		entities.put("cup", (char) 8746);
		entities.put("int", (char) 8747);
		entities.put("there4", (char) 8756);
		entities.put("sim", (char) 8764);
		entities.put("cong", (char) 8773);
		entities.put("asymp", (char) 8776);
		entities.put("ne", (char) 8800);
		entities.put("equiv", (char) 8801);
		entities.put("le", (char) 8804);
		entities.put("ge", (char) 8805);
		entities.put("sub", (char) 8834);
		entities.put("sup", (char) 8835);
		entities.put("nsub", (char) 8836);
		entities.put("sube", (char) 8838);
		entities.put("supe", (char) 8839);
		entities.put("oplus", (char) 8853);
		entities.put("otimes", (char) 8855);
		entities.put("perp", (char) 8869);
		entities.put("sdot", (char) 8901);
		entities.put("loz", (char) 9674);

		//technical symbols
		entities.put("lceil", (char) 8968);
		entities.put("rceil", (char) 8969);
		entities.put("lfloor", (char) 8970);
		entities.put("rfloor", (char) 8971);
		entities.put("lang", (char) 9001);
		entities.put("rang", (char) 9002);

		//arrow symbols
		entities.put("larr", (char) 8592);
		entities.put("uarr", (char) 8593);
		entities.put("rarr", (char) 8594);
		entities.put("darr", (char) 8595);
		entities.put("harr", (char) 8596);
		entities.put("crarr", (char) 8629);
		entities.put("lArr", (char) 8656);
		entities.put("uArr", (char) 8657);
		entities.put("rArr", (char) 8658);
		entities.put("dArr", (char) 8659);
		entities.put("hArr", (char) 8960);

		//divers symbols
		entities.put("bull", (char) 8226);
		entities.put("prime", (char) 8242);
		entities.put("Prime", (char) 8243);
		entities.put("oline", (char) 8254);
		entities.put("weierp", (char) 8472);
		entities.put("image", (char) 8465);
		entities.put("real", (char) 8476);
		entities.put("trade", (char) 8482);
		entities.put("euro", (char) 8364);
		entities.put("alefsym", (char) 8501);
		entities.put("spades", (char) 9824);
		entities.put("clubs", (char) 9827);
		entities.put("hearts", (char) 9829);
		entities.put("diams", (char) 9830);

		//ext lat symbols
		entities.put("OElig", (char) 338);
		entities.put("oelig", (char) 339);
		entities.put("Scaron", (char) 352);
		entities.put("scaron", (char) 353);
		entities.put("fnof", (char) 402);

		//interpunction
		entities.put("ensp", (char) 8194);
		entities.put("emsp", (char) 8195);
		entities.put("thinsp", (char) 8201);
		entities.put("zwnj", (char) 8204);
		entities.put("zwj", (char) 8205);
		entities.put("lrm", (char) 8206);
		entities.put("rlm", (char) 8207);

		entities.put("sbquo", (char) 8218);
		entities.put("ldquo", (char) 8220);
		entities.put("rdquo", (char) 8221);
		entities.put("bdquo", (char) 8222);
		entities.put("dagger", (char) 8224);
		entities.put("Dagger", (char) 8225);
		entities.put("hellip", (char) 8230);
		entities.put("permil", (char) 8240);
		entities.put("lsaquo", (char) 8249);
		entities.put("rsaquo", (char) 8250);

		//diacrit symb
		entities.put("circ", (char) 710);
		entities.put("tilde", (char) 732);

		Map<String, ElementInfo> elementInfos = ELEMENT_INFOS;

		elementInfos.put("NOSCRIPT", new ElementInfo(true, ElementInfo.END_ELEMENT_REQUIRED, null, true));

		ElementInfo optionalEndElement = new ElementInfo(true, ElementInfo.END_ELEMENT_OPTIONAL);
		ElementInfo forbiddenEndElement = new ElementInfo(false, ElementInfo.END_ELEMENT_FORBIDDEN);
		ElementInfo onlyTextDE = new ElementInfo(false, ElementInfo.END_ELEMENT_REQUIRED, true);
		ElementInfo onlyText = new ElementInfo(false, ElementInfo.END_ELEMENT_REQUIRED, false);

		Set<String> tableCellStopElements = new HashSet<>();
		tableCellStopElements.add("TH");
		tableCellStopElements.add("TD");
		tableCellStopElements.add("TR");
		ElementInfo tableCellElement = new ElementInfo(true, ElementInfo.END_ELEMENT_OPTIONAL, tableCellStopElements);

		Set<String> headStopElements = new HashSet<>();
		headStopElements.add("BODY");
		headStopElements.add("DIV");
		headStopElements.add("SPAN");
		headStopElements.add("TABLE");
		ElementInfo headElement = new ElementInfo(true, ElementInfo.END_ELEMENT_OPTIONAL, headStopElements);

		Set<String> optionStopElements = new HashSet<>();
		optionStopElements.add("OPTION");
		optionStopElements.add("SELECT");
		ElementInfo optionElement = new ElementInfo(true, ElementInfo.END_ELEMENT_OPTIONAL, optionStopElements);

		Set<String> paragraphStopElements = new HashSet<>();
		paragraphStopElements.add("P");
		paragraphStopElements.add("DIV");
		paragraphStopElements.add("TABLE");
		paragraphStopElements.add("PRE");
		paragraphStopElements.add("UL");
		paragraphStopElements.add("OL");
		ElementInfo paragraphElement = new ElementInfo(true, ElementInfo.END_ELEMENT_OPTIONAL, paragraphStopElements);

		elementInfos.put("SCRIPT", onlyText);
		elementInfos.put("STYLE", onlyText);
		elementInfos.put("TEXTAREA", onlyTextDE);
		elementInfos.put("IMG", forbiddenEndElement);
		elementInfos.put("META", forbiddenEndElement);
		elementInfos.put("LINK", forbiddenEndElement);
		elementInfos.put("BASE", forbiddenEndElement);
		elementInfos.put("INPUT", forbiddenEndElement);
		elementInfos.put("FRAME", forbiddenEndElement);
		elementInfos.put("BR", forbiddenEndElement);
		elementInfos.put("HR", forbiddenEndElement);
		elementInfos.put("EMBED", forbiddenEndElement);
		elementInfos.put("SPACER", forbiddenEndElement);

		elementInfos.put("P", paragraphElement);
		elementInfos.put("LI", optionalEndElement);
		elementInfos.put("DT", optionalEndElement);
		elementInfos.put("DD", optionalEndElement);
		elementInfos.put("TR", optionalEndElement);
		elementInfos.put("TH", tableCellElement);
		elementInfos.put("TD", tableCellElement);
		elementInfos.put("HEAD", headElement);
		elementInfos.put("OPTION", optionElement);

		// Note: The specification states anchors have
		// a required end element, but browsers generally behave
		// as if it's optional.
		elementInfos.put("A", optionalEndElement);
		elementInfos.put("ANCHOR", optionalEndElement);
	}

	/**
	 * Constructs a <code>HtmlParser</code>.
	 *
	 * @param document     A W3C Document instance.
	 * @deprecated UserAgentContext should be passed in constructor.
	 */
	public HtmlParser(Document document) {
		this.ucontext = null;
		this.document = document;
	}

	/**
	 * Constructs a <code>HtmlParser</code>.
	 *
	 * @param ucontext     The user agent context.
	 * @param document     An W3C Document instance.
	 */
	public HtmlParser(UserAgentContext ucontext, Document document) {
		this.ucontext = ucontext;
		this.document = document;
	}

	public static boolean isDecodeEntities(String elementName) {
		ElementInfo einfo = ELEMENT_INFOS.get(elementName.toUpperCase());
		return einfo == null || einfo.decodeEntities;
	}

	/**
	 * Parses HTML from an input stream, assuming
	 * the character set is ISO-8859-1.
	 *
	 * @param in The input stream.
	 * @throws IOException  Thrown when there are errors reading the stream.
	 * @throws SAXException Thrown when there are parse errors.
	 */
	public void parse(InputStream in) throws IOException, SAXException {
		this.parse(in, "ISO-8859-1");
	}

	/**
	 * Parses HTML from an input stream, using the given character set.
	 *
	 * @param in      The input stream.
	 * @param charset The character set.
	 * @throws IOException                  Thrown when there's an error reading from the stream.
	 * @throws SAXException                 Thrown when there is a parser error.
	 * @throws UnsupportedEncodingException Thrown if the character set is not supported.
	 */
	public void parse(InputStream in, String charset) throws IOException, SAXException {
		WritableLineReader reader = new WritableLineReader(new InputStreamReader(in, charset));
		this.parse(reader);
	}

	/**
	 * Parses HTML given by a <code>Reader</code>. This method appends
	 * nodes to the document provided to the parser.
	 *
	 * @param reader An instance of <code>Reader</code>.
	 * @throws IOException  Thrown if there are errors reading the input stream.
	 * @throws SAXException Thrown if there are parse errors.
	 */
	public void parse(Reader reader) throws IOException, SAXException {
		this.parse(new LineNumberReader(reader));
	}

	public void parse(LineNumberReader reader) throws IOException, SAXException {
		this.parse(reader, this.document);
	}

	/**
	 * This method may be used when the DOM should be built under
	 * a given node, such as when <code>innerHTML</code> is used
	 * in Javascript.
	 *
	 * @param reader A document reader.
	 * @param parent The root node for the parsed DOM.
	 * @throws IOException io
	 * @throws SAXException xml
	 */
	public void parse(Reader reader, Node parent) throws IOException, SAXException {
		this.parse(new LineNumberReader(reader), parent);
	}

	/**
	 * This method may be used when the DOM should be built under
	 * a given node, such as when <code>innerHTML</code> is used
	 * in Javascript.
	 *
	 * @param reader A LineNumberReader for the document.
	 * @param parent The root node for the parsed DOM.
	 * @throws IOException io
	 * @throws SAXException xml
	 */
	public void parse(LineNumberReader reader, Node parent) throws IOException, SAXException {
		// Note: Parser does not clear document. It could be used incrementally.
		try {
			parent.setUserData(MODIFYING_KEY, Boolean.TRUE, null);
			try {
				while (this.parseToken(parent, reader, null, new LinkedList<>()) != TOKEN_EOD) {
				}
			} catch (StopException se) {
				throw new SAXException("Unexpected flow exception", se);
			}
		} finally {
			parent.setUserData(MODIFYING_KEY, Boolean.FALSE, null);
		}
	}

	private static final int TOKEN_EOD = 0;
	private static final int TOKEN_COMMENT = 1;
	private static final int TOKEN_TEXT = 2;
	private static final int TOKEN_BEGIN_ELEMENT = 3;
	private static final int TOKEN_END_ELEMENT = 4;
	private static final int TOKEN_FULL_ELEMENT = 5;
	private static final int TOKEN_BAD = 6;

	private String normalLastTag = null;
	private boolean justReadTagBegin = false;
	private boolean justReadTagEnd = false;

	/**
	 * Only set when readAttribute returns false.
	 */
	private boolean justReadEmptyElement = false;

	private int parseToken(Node parent, LineNumberReader reader, Set<String> stopTags, LinkedList<String> ancestors)
			throws IOException, StopException, SAXException {
		Document doc = this.document;
		StringBuffer textSb = this.readUpToTagBegin(reader);
		if (textSb == null) {
			return TOKEN_EOD;
		}
		if (textSb.length() != 0) {
			StringBuffer decText = this.entityDecode(textSb);
			Node textNode = doc.createTextNode(decText.toString());
			try {
				parent.appendChild(textNode);
			} catch (DOMException ignored) {
			}
		}
		if (this.justReadTagBegin) {
			String tag = this.readTag(parent, reader);
			String normalTag = tag.toUpperCase();
			try {
				if (tag.startsWith("!")) {
					if ("!--".equals(tag)) {
						StringBuffer comment = this.passEndOfComment(reader);
						StringBuffer decText = this.entityDecode(comment);
						parent.appendChild(doc.createComment(decText.toString()));
						return TOKEN_COMMENT;
					} else {
						this.passEndOfTag(reader);
						return TOKEN_BAD;
					}
				} else if (tag.startsWith("/")) {
					normalTag = normalTag.substring(1);
					this.passEndOfTag(reader);
					return TOKEN_END_ELEMENT;
				} else if (tag.startsWith("?")) {
					tag = tag.substring(1);
					StringBuffer data = readProcessingInstruction(reader);
					parent.appendChild(doc.createProcessingInstruction(tag, data.toString()));
					return TOKEN_FULL_ELEMENT;
				} else {
					Element element = doc.createElement(tag);
					element.setUserData(MODIFYING_KEY, Boolean.TRUE, null);
					try {
						if (!this.justReadTagEnd) {
							while (this.readAttribute(reader, element)) {
							}
						}
						if (stopTags != null && stopTags.contains(normalTag)) {
							// Throw before appending to parent.
							// After attributes are set.
							// After MODIFYING_KEY is set.
							throw new StopException(element);
						}
						// Add element to parent before children are added.
						// This is necessary for incremental rendering.
						parent.appendChild(element);
						if (!this.justReadEmptyElement) {
							ElementInfo einfo = ELEMENT_INFOS.get(normalTag);
							int endTagType = einfo == null ? ElementInfo.END_ELEMENT_REQUIRED : einfo.endElementType;
							if (endTagType != ElementInfo.END_ELEMENT_FORBIDDEN) {
								boolean childrenOk = einfo == null || einfo.childElementOk;
								Set<String> newStopSet = einfo == null ? null : einfo.stopTags;
								if (newStopSet == null) {
									if (endTagType == ElementInfo.END_ELEMENT_OPTIONAL) {
										newStopSet = Collections.singleton(normalTag);
									}
								}
								if (stopTags != null) {
									if (newStopSet != null) {
										Set<String> newStopSet2 = new HashSet<>();
										newStopSet2.addAll(stopTags);
										newStopSet2.addAll(newStopSet);
										newStopSet = newStopSet2;
									} else {
										newStopSet = endTagType == ElementInfo.END_ELEMENT_REQUIRED ? null : stopTags;
									}
								}
								ancestors.addFirst(normalTag);
								try {
									for (; ; ) {
										try {
											int token;
											if (einfo != null && einfo.noScriptElement) {
												UserAgentContext ucontext = this.ucontext;
												if (ucontext == null) {
													token = this.parseForEndTag(parent, reader, tag, false, einfo.decodeEntities);
												} else {
													token = this.parseToken(element, reader, newStopSet, ancestors);
												}
											} else {
												token = childrenOk ? this.parseToken(element, reader, newStopSet, ancestors) : this.parseForEndTag(element, reader, tag, true, einfo.decodeEntities);
											}
											if (token == TOKEN_END_ELEMENT) {
												String normalLastTag = this.normalLastTag;
												if (normalTag.equals(normalLastTag)) {
													return TOKEN_FULL_ELEMENT;
												} else {
													ElementInfo closeTagInfo = ELEMENT_INFOS.get(normalLastTag);
													if (closeTagInfo == null || closeTagInfo.endElementType != ElementInfo.END_ELEMENT_FORBIDDEN) {
														Iterator i = ancestors.iterator();
														if (i.hasNext()) {
															i.next();
															while (i.hasNext()) {
																String normalAncestorTag = (String) i.next();
																if (normalLastTag.equals(normalAncestorTag)) {
																	normalTag = normalLastTag;
																	return TOKEN_END_ELEMENT;
																}
															}
														}
													}
												}
											} else if (token == TOKEN_EOD) {
												return TOKEN_EOD;
											}
										} catch (StopException se) {
											// newElement does not have a parent.
											Element newElement = se.getElement();
											tag = newElement.getTagName();
											normalTag = tag.toUpperCase();
											// If a subelement throws StopException with
											// a tag matching the current stop tag, the exception
											// is rethrown (e.g. <TR><TD>blah<TR><TD>blah)
											if (stopTags != null && stopTags.contains(normalTag)) {
												throw se;
											}
											einfo = ELEMENT_INFOS.get(normalTag);
											endTagType = einfo == null ? ElementInfo.END_ELEMENT_REQUIRED : einfo.endElementType;
											childrenOk = einfo == null || einfo.childElementOk;
											newStopSet = einfo == null ? null : einfo.stopTags;
											if (newStopSet == null) {
												if (endTagType == ElementInfo.END_ELEMENT_OPTIONAL) {
													newStopSet = Collections.singleton(normalTag);
												}
											}
											if (stopTags != null && newStopSet != null) {
												Set<String> newStopSet2 = new HashSet<>();
												newStopSet2.addAll(stopTags);
												newStopSet2.addAll(newStopSet);
												newStopSet = newStopSet2;
											}
											ancestors.removeFirst();
											ancestors.addFirst(normalTag);
											//Switch element
											element.setUserData(MODIFYING_KEY, Boolean.FALSE, null);
											//newElement should have been suspended.
											element = newElement;
											// Add to parent
											parent.appendChild(element);
											if (this.justReadEmptyElement) {
												return TOKEN_BEGIN_ELEMENT;
											}
										}
									}
								} finally {
									ancestors.removeFirst();
								}
							}
						}
						return TOKEN_BEGIN_ELEMENT;
					} finally {
						// This can inform elements to continue with notifications.
						// It can also cause Javascript to get processed.
						element.setUserData(MODIFYING_KEY, Boolean.FALSE, null);
					}
				}
			} finally {
				this.normalLastTag = normalTag;
			}
		} else {
			this.normalLastTag = null;
			return TOKEN_TEXT;
		}
	}

	/**
	 * Reads text until the beginning of the next tag.
	 * Leaves the reader offset past the opening angle bracket.
	 * Returns null only on EOF.
	 */
	private StringBuffer readUpToTagBegin(LineNumberReader reader) throws IOException, SAXException {
		StringBuffer sb = null;
		int intCh;
		while ((intCh = reader.read()) != -1) {
			char ch = (char) intCh;
			if (ch == '<') {
				this.justReadTagBegin = true;
				this.justReadTagEnd = false;
				this.justReadEmptyElement = false;
				if (sb == null) {
					sb = new StringBuffer(0);
				}
				return sb;
			}
			if (sb == null) {
				sb = new StringBuffer();
			}
			sb.append(ch);
		}
		this.justReadTagBegin = false;
		this.justReadTagEnd = false;
		this.justReadEmptyElement = false;
		return sb;
	}

	private int parseForEndTag(Node parent, LineNumberReader reader, String tagName, boolean addTextNode, boolean decodeEntities) throws IOException, SAXException {
		Document doc = this.document;
		int intCh;
		StringBuffer sb = new StringBuffer();
		while ((intCh = reader.read()) != -1) {
			char ch = (char) intCh;
			if (ch == '<') {
				intCh = reader.read();
				if (intCh != -1) {
					ch = (char) intCh;
					if (ch == '/') {
						StringBuilder tempBuffer = new StringBuilder();
						while ((intCh = reader.read()) != -1) {
							ch = (char) intCh;
							if (ch == '>') {
								String thisTag = tempBuffer.toString().trim();
								if (thisTag.equalsIgnoreCase(tagName)) {
									this.justReadTagBegin = false;
									this.justReadTagEnd = true;
									this.justReadEmptyElement = false;
									this.normalLastTag = thisTag.toUpperCase();
									if (addTextNode) {
										if (decodeEntities) {
											sb = this.entityDecode(sb);
										}
										String text = sb.toString();
										if (text.length() != 0) {
											Node textNode = doc.createTextNode(text);
											parent.appendChild(textNode);
										}
									}
									return HtmlParser.TOKEN_END_ELEMENT;
								} else {
									break;
								}
							} else {
								tempBuffer.append(ch);
							}
						}
						sb.append("</");
						sb.append(tempBuffer);
					} else {
						sb.append('<');
					}
				}
			}
			sb.append(ch);
		}
		this.justReadTagBegin = false;
		this.justReadTagEnd = false;
		this.justReadEmptyElement = false;
		if (addTextNode) {
			if (decodeEntities) {
				sb = this.entityDecode(sb);
			}
			String text = sb.toString();
			if (text.length() != 0) {
				Node textNode = doc.createTextNode(text);
				parent.appendChild(textNode);
			}
		}
		return HtmlParser.TOKEN_EOD;
	}

	private String readTag(Node parent, LineNumberReader reader) throws IOException {
		StringBuilder sb = new StringBuilder();
		int chInt;
		chInt = reader.read();
		if (chInt != -1) {
			boolean cont = true;
			char ch;
			for (; ; ) {
				ch = (char) chInt;
				if (Character.isLetter(ch)) {
					break;
				} else if (ch == '!') {
					sb.append('!');
					chInt = reader.read();
					if (chInt != -1) {
						ch = (char) chInt;
						if (ch == '-') {
							sb.append('-');
							chInt = reader.read();
							if (chInt != -1) {
								ch = (char) chInt;
								if (ch == '-') {
									sb.append('-');
									cont = false;
								}
							} else {
								cont = false;
							}
						}
					} else {
						cont = false;
					}
				} else if (ch == '/') {
					sb.append(ch);
					chInt = reader.read();
					if (chInt != -1) {
						ch = (char) chInt;
					} else {
						cont = false;
					}
				} else if (ch == '<') {
					StringBuilder ltText = new StringBuilder(3);
					ltText.append('<');
					while ((chInt = reader.read()) == '<') {
						ltText.append('<');
					}
					Node textNode = this.document.createTextNode(ltText.toString());
					try {
						parent.appendChild(textNode);
					} catch (DOMException ignored) {
					}
					if (chInt == -1) {
						cont = false;
					} else {
						continue;
					}
				} else if (Character.isWhitespace(ch)) {
					StringBuilder ltText = new StringBuilder();
					ltText.append('<');
					ltText.append(ch);
					while ((chInt = reader.read()) != -1) {
						ch = (char) chInt;
						if (ch == '<') {
							chInt = reader.read();
							break;
						}
						ltText.append(ch);
					}
					Node textNode = this.document.createTextNode(ltText.toString());
					try {
						parent.appendChild(textNode);
					} catch (DOMException ignored) {
					}
					if (chInt == -1) {
						cont = false;
					} else {
						continue;
					}
				}
				break;
			}
			if (cont) {
				boolean lastCharSlash = false;
				for (; ; ) {
					if (Character.isWhitespace(ch)) {
						break;
					} else if (ch == '>') {
						this.justReadTagEnd = true;
						this.justReadTagBegin = false;
						this.justReadEmptyElement = lastCharSlash;
						return sb.toString();
					} else if (ch == '/') {
						lastCharSlash = true;
					} else {
						if (lastCharSlash) {
							sb.append('/');
						}
						lastCharSlash = false;
						sb.append(ch);
					}
					chInt = reader.read();
					if (chInt == -1) {
						break;
					}
					ch = (char) chInt;
				}
			}
		}
		if (sb.length() > 0) {
			this.justReadTagEnd = false;
			this.justReadTagBegin = false;
			this.justReadEmptyElement = false;
		}
		return sb.toString();
	}

	private StringBuffer passEndOfComment(LineNumberReader reader) throws IOException {
		if (this.justReadTagEnd) {
			return new StringBuffer(0);
		}
		StringBuffer sb = new StringBuffer();
		OUTER:
		for (; ; ) {
			int chInt = reader.read();
			if (chInt == -1) {
				break;
			}
			char ch = (char) chInt;
			if (ch == '-') {
				chInt = reader.read();
				if (chInt == -1) {
					sb.append(ch);
					break;
				}
				ch = (char) chInt;
				if (ch == '-') {
					StringBuffer extra = null;
					for (; ; ) {
						chInt = reader.read();
						if (chInt == -1) {
							if (extra != null) {
								sb.append(extra.toString());
							}
							break OUTER;
						}
						ch = (char) chInt;
						if (ch == '>') {
							this.justReadTagBegin = false;
							this.justReadTagEnd = true;
							return sb;
						} else if (ch == '-') {
							// Allow any number of dashes at the end
							if (extra == null) {
								extra = new StringBuffer();
								extra.append("--");
							}
							extra.append("-");
						} else if (Character.isWhitespace(ch)) {
							if (extra == null) {
								extra = new StringBuffer();
								extra.append("--");
							}
							extra.append(ch);
						} else {
							if (extra != null) {
								sb.append(extra.toString());
							}
							sb.append(ch);
							break;
						}
					}
				} else {
					sb.append('-');
					sb.append(ch);
				}
			} else {
				sb.append(ch);
			}
		}
		if (sb.length() > 0) {
			this.justReadTagBegin = false;
			this.justReadTagEnd = false;
		}
		return sb;
	}

	private void passEndOfTag(Reader reader) throws IOException {
		if (this.justReadTagEnd) {
			return;
		}
		boolean readSomething = false;
		for (; ; ) {
			int chInt = reader.read();
			if (chInt == -1) {
				break;
			}
			readSomething = true;
			char ch = (char) chInt;
			if (ch == '>') {
				this.justReadTagEnd = true;
				this.justReadTagBegin = false;
				return;
			}
		}
		if (readSomething) {
			this.justReadTagBegin = false;
			this.justReadTagEnd = false;
		}
	}

	private StringBuffer readProcessingInstruction(LineNumberReader reader) throws IOException {
		StringBuffer pidata = new StringBuffer();
		if (this.justReadTagEnd) {
			return pidata;
		}
		int ch;
		for (ch = reader.read(); ch != -1 && ch != '>'; ch = reader.read()) {
			pidata.append((char) ch);
		}
		this.justReadTagBegin = false;
		this.justReadTagEnd = ch != -1;
		return pidata;
	}

	private boolean readAttribute(LineNumberReader reader, Element element)
			throws IOException, SAXException {
		if (this.justReadTagEnd) {
			return false;
		}

		// Read attribute name up to '=' character.
		// May read several attribute names without explicit values.

		StringBuffer attributeName = null;
		boolean blankFound = false;
		boolean lastCharSlash = false;
		for (; ; ) {
			int chInt = reader.read();
			if (chInt == -1) {
				if (attributeName != null && attributeName.length() != 0) {
					String attributeNameStr = attributeName.toString();
					element.setAttribute(attributeNameStr, attributeNameStr);
					attributeName.setLength(0);
				}
				this.justReadTagBegin = false;
				this.justReadTagEnd = false;
				this.justReadEmptyElement = false;
				return false;
			}
			char ch = (char) chInt;
			if (ch == '=') {
				lastCharSlash = false;
				break;
			} else if (ch == '>') {
				if (attributeName != null && attributeName.length() != 0) {
					String attributeNameStr = attributeName.toString();
					element.setAttribute(attributeNameStr, attributeNameStr);
				}
				this.justReadTagBegin = false;
				this.justReadTagEnd = true;
				this.justReadEmptyElement = lastCharSlash;
				return false;
			} else if (ch == '/') {
				blankFound = true;
				lastCharSlash = true;
			} else if (Character.isWhitespace(ch)) {
				lastCharSlash = false;
				blankFound = true;
			} else {
				lastCharSlash = false;
				if (blankFound) {
					blankFound = false;
					if (attributeName != null && attributeName.length() != 0) {
						String attributeNameStr = attributeName.toString();
						element.setAttribute(attributeNameStr, attributeNameStr);
						attributeName.setLength(0);
					}
				}
				if (attributeName == null) {
					attributeName = new StringBuffer(6);
				}
				attributeName.append(ch);
			}
		}
		// Read blanks up to open quote or first non-blank.
		StringBuffer attributeValue = null;
		int openQuote = -1;
		for (; ; ) {
			int chInt = reader.read();
			if (chInt == -1) {
				break;
			}
			char ch = (char) chInt;
			if (ch == '>') {
				if (attributeName != null && attributeName.length() != 0) {
					String attributeNameStr = attributeName.toString();
					element.setAttribute(attributeNameStr, attributeNameStr);
				}
				this.justReadTagBegin = false;
				this.justReadTagEnd = true;
				this.justReadEmptyElement = lastCharSlash;
				return false;
			} else if (ch == '/') {
				lastCharSlash = true;
			} else if (Character.isWhitespace(ch)) {
				lastCharSlash = false;
			} else {
				lastCharSlash = false;
				if (ch == '"') {
					openQuote = '"';
				} else if (ch == '\'') {
					openQuote = '\'';
				} else {
					openQuote = -1;
					attributeValue = new StringBuffer(6);
					attributeValue.append(ch);
				}
				break;
			}
		}

		// Read attribute value

		for (; ; ) {
			int chInt = reader.read();
			if (chInt == -1) {
				break;
			}
			char ch = (char) chInt;
			if (openQuote != -1 && ch == openQuote) {
				if (attributeName != null) {
					String attributeNameStr = attributeName.toString();
					if (attributeValue == null) {
						// Quotes are closed. There's a distinction
						// between blank values and null in HTML, as
						// processed by major browsers.
						element.setAttribute(attributeNameStr, "");
					} else {
						StringBuffer actualAttributeValue = this.entityDecode(attributeValue);
						element.setAttribute(attributeNameStr, actualAttributeValue.toString());
					}
				}
				this.justReadTagBegin = false;
				this.justReadTagEnd = false;
				return true;
			} else if (openQuote == -1 && ch == '>') {
				if (attributeName != null) {
					String attributeNameStr = attributeName.toString();
					if (attributeValue == null) {
						element.setAttribute(attributeNameStr, null);
					} else {
						StringBuffer actualAttributeValue = this.entityDecode(attributeValue);
						element.setAttribute(attributeNameStr, actualAttributeValue.toString());
					}
				}
				this.justReadTagBegin = false;
				this.justReadTagEnd = true;
				this.justReadEmptyElement = lastCharSlash;
				return false;
			} else if (openQuote == -1 && Character.isWhitespace(ch)) {
				if (attributeName != null) {
					String attributeNameStr = attributeName.toString();
					if (attributeValue == null) {
						element.setAttribute(attributeNameStr, null);
					} else {
						StringBuffer actualAttributeValue = this.entityDecode(attributeValue);
						element.setAttribute(attributeNameStr, actualAttributeValue.toString());
					}
				}
				this.justReadTagBegin = false;
				this.justReadTagEnd = false;
				return true;
			} else {
				if (attributeValue == null) {
					attributeValue = new StringBuffer(6);
				}
				if (lastCharSlash) {
					attributeValue.append('/');
				}
				lastCharSlash = false;
				attributeValue.append(ch);
			}
		}
		this.justReadTagBegin = false;
		this.justReadTagEnd = false;
		if (attributeName != null) {
			String attributeNameStr = attributeName.toString();
			if (attributeValue == null) {
				element.setAttribute(attributeNameStr, null);
			} else {
				StringBuffer actualAttributeValue = this.entityDecode(attributeValue);
				element.setAttribute(attributeNameStr, actualAttributeValue.toString());
			}
		}
		return false;
	}

	private StringBuffer entityDecode(StringBuffer rawText) throws org.xml.sax.SAXException {
		int startIdx = 0;
		StringBuffer sb = null;
		for (; ; ) {
			int ampIdx = rawText.indexOf("&", startIdx);
			if (ampIdx == -1) {
				if (sb == null) {
					return rawText;
				} else {
					sb.append(rawText.substring(startIdx));
					return sb;
				}
			}
			if (sb == null) {
				sb = new StringBuffer();
			}
			sb.append(rawText.substring(startIdx, ampIdx));
			int colonIdx = rawText.indexOf(";", ampIdx);
			if (colonIdx == -1) {
				sb.append('&');
				startIdx = ampIdx + 1;
				continue;
			}
			String spec = rawText.substring(ampIdx + 1, colonIdx);
			if (spec.startsWith("#")) {
				String number = spec.substring(1).toLowerCase();
				int decimal;
				try {
					if (number.startsWith("x")) {
						decimal = Integer.parseInt(number.substring(1), 16);
					} else {
						decimal = Integer.parseInt(number);
					}
				} catch (NumberFormatException nfe) {
					decimal = 0;
				}
				sb.append((char) decimal);
			} else {
				int chInt = this.getEntityChar(spec);
				if (chInt == -1) {
					sb.append('&');
					sb.append(spec);
					sb.append(';');
				} else {
					sb.append((char) chInt);
				}
			}
			startIdx = colonIdx + 1;
		}
	}

	private int getEntityChar(String spec) {
		Character c = ENTITIES.get(spec);
		if (c == null) {
			String specTL = spec.toLowerCase();
			c = ENTITIES.get(specTL);
			if (c == null) {
				return -1;
			}
		}
		return (int) c;
	}
}
