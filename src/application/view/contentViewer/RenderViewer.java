package application.view.contentViewer;

import org.lobobrowser.html.gui.HtmlPanel;
import org.lobobrowser.html.test.SimpleHtmlRendererContext;
import org.lobobrowser.html.test.SimpleUserAgentContext;

import javax.swing.*;
import java.net.URL;

class RenderViewer extends JScrollPane {

	private HtmlPanel htmlPane;

	RenderViewer() {
		htmlPane = new HtmlPanel();
		setViewportView(htmlPane);
	}

	void setText(String html, URL url) {
		SimpleUserAgentContext userAgentContext = new SimpleUserAgentContext();
		SimpleHtmlRendererContext rendererContext = new SimpleHtmlRendererContext(htmlPane, userAgentContext);
		htmlPane.setHtml(html, url.toExternalForm(), rendererContext);
	}
}
