package application.view.frame.target;

import javax.swing.*;

class TargetPanel extends JTabbedPane {


	/**
	 *
	 */
	private static final long serialVersionUID = 2045325259053709215L;


	/**
	 * Create the frame.
	 */
	TargetPanel() {

		SiteMapPane siteMapPane = new SiteMapPane();
		addTab("Site Map", null, siteMapPane, null);

		ScopePane scopePane = new ScopePane();
		addTab("Scope", null, scopePane, null);

	}
}
