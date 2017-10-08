package application.view.contentViewer;

import java.net.URL;

public class HTMLViewer extends ExtendedViewer {

	private RenderViewer renderViewer;

	HTMLViewer() {
		super("HTML");

		renderViewer = new RenderViewer();
		addTab("Render", renderViewer);
	}

	public void setHTML(String html, URL url) {
		renderViewer.setText(html, url);
	}
}
