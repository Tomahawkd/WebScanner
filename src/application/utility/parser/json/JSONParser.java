package application.utility.parser.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Iterator;

public class JSONParser {

	private static JSONParser parser;

	public static JSONParser getParser() {
		if (parser == null) parser = new JSONParser();
		return parser;
	}

	public JSONObject parse(String json) throws JSONException {
		return new JSONObject(json);
	}

	public DefaultMutableTreeNode getTreeNode(JSONObject json) {
		DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode("root");
		traverseJson(json, treeNode);
		return treeNode;
	}

	private void traverseJson(Object json, DefaultMutableTreeNode node) {
		if (json == null) return;
		try {
			if (json instanceof JSONObject) {
				JSONObject jsonObj = (JSONObject) json;
				Iterator it = jsonObj.keys();
				while (it.hasNext()) {
					String key = (String) it.next();
					DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(key);
					node.add(childNode);
					Object value = jsonObj.get(key);
					traverseJson(value, childNode);
				}

			} else if (json instanceof JSONArray) {
				JSONArray jsonArr = (JSONArray) json;
				for (Object aJsonArr : jsonArr) {
					traverseJson(aJsonArr, node);
				}

			} else {
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(json);
				node.add(childNode);
			}

		} catch (Exception ignored) {

		}
	}
}
