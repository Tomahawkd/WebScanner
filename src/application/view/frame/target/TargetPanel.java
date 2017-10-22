package application.view.frame.target;

import javax.swing.*;

public class TargetPanel extends JTabbedPane {


	/**
	 *
	 */
	private static final long serialVersionUID = 2045325259053709215L;
	private SiteMapPane siteMapPane;

	/**
	 * Create the frame.
	 */
	TargetPanel() {

		siteMapPane = new SiteMapPane();
		addTab("Site Map", null, siteMapPane, null);

		ScopePane scopePane = new ScopePane();
		addTab("Scope", null, scopePane, null);

	}

	public void updateMapData() {
		siteMapPane.updateMapData();
	}
}
