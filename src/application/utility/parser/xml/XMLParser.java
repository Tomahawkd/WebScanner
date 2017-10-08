package application.utility.parser.xml;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class XMLParser {
	private static XMLParser parser;

	public static XMLParser getParser() {
		if (parser == null) parser = new XMLParser();
		return parser;
	}

	public JSONObject parse(String xml) throws JSONException {
		return XML.toJSONObject(xml);
	}
}
