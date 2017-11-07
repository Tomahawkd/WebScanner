package application.utility.net;

public interface EditableHeaderMap extends HeaderMap {

	Header put(String key, Header value);
}
