package application.utility.parser.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

public class HTMLParser {

	private static HTMLParser parser;

	public static HTMLParser getParser() {
		if (parser == null) parser = new HTMLParser();
		return parser;
	}

	public Document parse(String html, String host) {
		return Jsoup.parse(html, host);
	}

	public DefaultMutableTreeNode getTreeNode(Document doc) {
		if (doc == null) return null;
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("");
		getNode(doc, treeNode);
		return treeNode;
	}

	private void getNode(Node node, DefaultMutableTreeNode treeNode) {
		if (node == null) return;
		for (Node childNode: node.childNodes()) {
			String text;
			if (childNode instanceof Element) {
				Element element = (Element) childNode;
				StringBuilder tag = new StringBuilder();
				tag.append("< ").append(element.tagName()).append(" ");
				for (Attribute attr : element.attributes()) {
					tag.append(attr.getKey()).append("=").append(attr.getValue()).append(" ");
				}
				text = tag.toString().trim() + " >";

			} else if (childNode instanceof TextNode) {
				text = ((TextNode) childNode).text().trim();

			} else if (childNode instanceof DataNode) {
				text = ((DataNode) childNode).getWholeData().trim();

			} else if (childNode instanceof Comment ||
					childNode instanceof XmlDeclaration ||
					childNode instanceof DocumentType) {
				text = childNode.toString().trim();

			} else {
				continue;
			}

			if (!text.equals("")) {
				DefaultMutableTreeNode childTreeNode = new DefaultMutableTreeNode(text);
				treeNode.add(childTreeNode);
				if (childNode.childNodeSize() > 0) {
					getNode(childNode, childTreeNode);
				}
			}
		}

	}

	public ArrayList<String> getLink(Document doc) {
		ArrayList<String> linkList = new ArrayList<>();

		// Page source like HTML etc.
		Elements imports = doc.select("*[href]");
		for (Element link : imports) {

			// Get the absolute URL
			String newUrl = link.attr("abs:href");

			// Filter empty URL
			if (!newUrl.equals("")) {
				linkList.add(newUrl);
			}
		}

		// Media source like image etc.
		Elements media = doc.select("[src]");
		for (Element src : media) {

			// Get the absolute URL
			String newUrl = src.attr("abs:src");

			// Filter empty URL
			if (!newUrl.equals("")) {
				linkList.add(newUrl);
			}
		}
		return linkList;
	}
}
