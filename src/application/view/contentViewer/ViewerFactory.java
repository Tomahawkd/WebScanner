package application.view.contentViewer;

public class ViewerFactory {
	private static ViewerFactory factory;

	public static final int REQUEST = 0;
	public static final int DEFAULT_RESPONSE = 1;
	public static final int RESPONSE_HTML = 2;
	public static final int RESPONSE_XML = 3;
	public static final int RESPONSE_JSON = 4;


	public static ViewerFactory getInstance() {
		if (factory == null) factory = new ViewerFactory();
		return factory;
	}

	public Viewer createViewer() {
		return createViewer(DEFAULT_RESPONSE);
	}

	public Viewer createViewer(int type) {

		switch (type) {

			case REQUEST:
				return new DefaultViewer(true);

			case DEFAULT_RESPONSE:
				return new DefaultViewer(false);

			case RESPONSE_HTML:
				return new HTMLViewer();

			case RESPONSE_XML:
				return new ExtendedViewer("XML");

			case RESPONSE_JSON:
				return new ExtendedViewer("JSON");

			default:
				throw new IllegalArgumentException("Argument is not valid");
		}
	}
}
