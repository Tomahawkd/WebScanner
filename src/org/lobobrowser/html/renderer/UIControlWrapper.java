package org.lobobrowser.html.renderer;

import org.lobobrowser.html.HtmlObject;

import java.awt.*;

class UIControlWrapper implements UIControl {
	private final Component component;
	private final HtmlObject htmlObject;

	UIControlWrapper(HtmlObject ho) {
		this.htmlObject = ho;
		Component c;
		if (ho == null) {
			c = new BrokenComponent();
		} else {
			c = ho.getComponent();
		}
		this.component = c;
	}

	public void reset(int availWidth, int availHeight) {
		this.htmlObject.reset(availWidth, availHeight);
	}

	public Component getComponent() {
		return this.component;
	}

	public int getVAlign() {
		return RElement.VALIGN_BASELINE;
	}

	public Color getBackgroundColor() {
		return this.component.getBackground();
	}

	public Dimension getPreferredSize() {
		return this.component.getPreferredSize();
	}

	public void invalidate() {
		// Calls its AWT parent's invalidate, but I guess that's OK.
		this.component.invalidate();
	}

	public void setBounds(int x, int y, int width, int height) {
		this.component.setBounds(x, y, width, height);
	}

	public void setRUIControl(RUIControl ruicontrol) {
		// Not doing anything with this.
	}

	public void paint(Graphics g) {
		this.component.paint(g);
	}
}
