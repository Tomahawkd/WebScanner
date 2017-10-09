/*  GNU LESSER GENERAL PUBLIC LICENSE
    Copyright (C) 2006 The Lobo Project

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

    Contact info: lobochief@users.sourceforge.net
*/

package org.lobobrowser.html.renderer;

import org.lobobrowser.html.HttpRequest;
import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.domimpl.HTMLDocumentImpl;
import org.lobobrowser.html.domimpl.HTMLElementImpl;
import org.lobobrowser.html.domimpl.ModelNode;
import org.lobobrowser.html.style.*;
import org.lobobrowser.util.Strings;
import org.lobobrowser.util.gui.GUITasks;
import org.w3c.dom.css.CSS2Properties;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

abstract class BaseElementRenderable extends BaseRCollection implements RElement, RenderableContainer, java.awt.image.ImageObserver {
	private static final Integer INVALID_SIZE = Integer.MIN_VALUE;

	/**
	 * A collection of all GUI components
	 * added by descendents.
	 */
	private Collection<Component> guiComponents = null;

	/**
	 * A list of absolute positioned or
	 * float parent-child pairs.
	 */
	Collection<DelayedPair> delayedPairs = null;

	//	protected boolean renderStyleCanBeInvalidated = true;

	/**
	 * Background color which may be different to
	 * that from RenderState in the case of a Document node.
	 */
	protected Color backgroundColor;
	volatile Image backgroundImage;
	private int zIndex;
	private Color borderTopColor;
	private Color borderLeftColor;
	private Color borderBottomColor;
	private Color borderRightColor;
	Insets borderInsets;
	Insets marginInsets;
	Insets paddingInsets;
	private BorderInfo borderInfo;
	java.net.URL lastBackgroundImageUri;
	private Insets defaultMarginInsets = null;
	private Insets defaultPaddingInsets = null;
	int overflowX;
	int overflowY;

	private final UserAgentContext userAgentContext;

	BaseElementRenderable(RenderableContainer container, ModelNode modelNode, UserAgentContext ucontext) {
		super(container, modelNode);
		this.userAgentContext = ucontext;
	}

	public void setDefaultMarginInsets(Insets insets) {
		this.defaultMarginInsets = insets;
	}

	boolean layoutDeepCanBeInvalidated = false;

	/**
	 * Invalidates this Renderable and all
	 * descendents. This is only used in special
	 * cases, such as when a new style sheet is
	 * added.
	 */
	public final void invalidateLayoutDeep() {
		if (this.layoutDeepCanBeInvalidated) {
			this.layoutDeepCanBeInvalidated = false;
			this.invalidateLayoutLocal();
			Iterator i = this.getRenderables();
			if (i != null) {
				while (i.hasNext()) {
					Object r = i.next();
					if (r instanceof RCollection) {
						((RCollection) r).invalidateLayoutDeep();
					}
				}
			}
		}
	}

	protected void invalidateLayoutLocal() {
		RenderState rs = this.modelNode.getRenderState();
		if (rs != null) {
			rs.invalidate();
		}
		this.overflowX = RenderState.OVERFLOW_NONE;
		this.overflowY = RenderState.OVERFLOW_NONE;
		this.declaredWidth = INVALID_SIZE;
		this.declaredHeight = INVALID_SIZE;
		this.lastAvailHeightForDeclared = -1;
		this.lastAvailWidthForDeclared = -1;
	}

	private Integer declaredWidth = INVALID_SIZE;
	private Integer declaredHeight = INVALID_SIZE;
	private int lastAvailWidthForDeclared = -1;
	private int lastAvailHeightForDeclared = -1;

	protected Integer getDeclaredWidth(RenderState renderState, int actualAvailWidth) {
		Integer dw = this.declaredWidth;
		if (dw == INVALID_SIZE || actualAvailWidth != this.lastAvailWidthForDeclared) {
			this.lastAvailWidthForDeclared = actualAvailWidth;
			int dwInt = this.getDeclaredWidthImpl(renderState, actualAvailWidth);
			dw = dwInt == -1 ? null : dwInt;
			this.declaredWidth = dw;
		}
		return dw;
	}

	final boolean hasDeclaredWidth() {
		Integer dw = this.declaredWidth;
		if (dw == INVALID_SIZE) {
			Object rootNode = this.modelNode;
			if (rootNode instanceof HTMLElementImpl) {
				HTMLElementImpl element = (HTMLElementImpl) rootNode;
				CSS2Properties props = element.getCurrentStyle();
				return props != null && !Strings.isBlank(props.getWidth());
			}
			return false;
		}
		return dw != null;
	}

	private int getDeclaredWidthImpl(RenderState renderState, int availWidth) {
		Object rootNode = this.modelNode;
		if (rootNode instanceof HTMLElementImpl) {
			HTMLElementImpl element = (HTMLElementImpl) rootNode;
			CSS2Properties props = element.getCurrentStyle();
			if (props == null) {
				return -1;
			}
			String widthText = props.getWidth();
			if (widthText == null || "".equals(widthText)) {
				return -1;
			}
			return HtmlValues.getPixelSize(widthText, renderState, -1, availWidth);
		} else {
			return -1;
		}
	}

	protected Integer getDeclaredHeight(RenderState renderState, int actualAvailHeight) {
		Integer dh = this.declaredHeight;
		if (dh == INVALID_SIZE || actualAvailHeight != this.lastAvailHeightForDeclared) {
			this.lastAvailHeightForDeclared = actualAvailHeight;
			int dhInt = this.getDeclaredHeightImpl(renderState, actualAvailHeight);
			dh = dhInt == -1 ? null : dhInt;
			this.declaredHeight = dh;
		}
		return dh;
	}

	private int getDeclaredHeightImpl(RenderState renderState, int availHeight) {
		Object rootNode = this.modelNode;
		if (rootNode instanceof HTMLElementImpl) {
			HTMLElementImpl element = (HTMLElementImpl) rootNode;
			CSS2Properties props = element.getCurrentStyle();
			if (props == null) {
				return -1;
			}
			String heightText = props.getHeight();
			if (heightText == null || "".equals(heightText)) {
				return -1;
			}
			return HtmlValues.getPixelSize(heightText, renderState, -1, availHeight);
		} else {
			return -1;
		}
	}

	/**
	 * All overriders should call super implementation.
	 */
	public void paint(Graphics g) {
	}

	/**
	 * Lays out children, and deals with "valid" state. Override doLayout method
	 * instead of this one.
	 */
	public final void layout(int availWidth, int availHeight, boolean sizeOnly) {
		// Must call doLayout regardless of validity state.
		try {
			this.doLayout(availWidth, availHeight, sizeOnly);
		} finally {
			this.layoutUpTreeCanBeInvalidated = true;
			this.layoutDeepCanBeInvalidated = true;
		}
	}

	protected abstract void doLayout(int availWidth, int availHeight, boolean sizeOnly);

	final void sendGUIComponentsToParent() {
		// Ensures that parent has all the components
		// below this renderer node. (Parent expected to have removed them).
		Collection gc = this.guiComponents;
		if (gc != null) {
			for (Object aGc : gc) {
				this.container.addComponent((Component) aGc);
			}
		}
	}

	final void clearGUIComponents() {
		Collection<Component> gc = this.guiComponents;
		if (gc != null) {
			gc.clear();
		}
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RenderableContainer#add(java.awt.Component)
	 */
	public void addComponent(Component component) {
		// Expected to be called in GUI thread.
		// Adds only in local collection.
		// Does not remove from parent.
		// Sending components to parent is done
		// by sendGUIComponentsToParent().
		Collection<Component> gc = this.guiComponents;
		if (gc == null) {
			gc = new HashSet<>(1);
			this.guiComponents = gc;
		}
		gc.add(component);
	}

	public void updateAllWidgetBounds() {
		this.container.updateAllWidgetBounds();
	}

	/**
	 * Updates widget bounds below this node only.
	 * Should not be called during general rendering.
	 */
	void updateWidgetBounds() {
		java.awt.Point guiPoint = this.getGUIPoint(0, 0);
		this.updateWidgetBounds(guiPoint.x, guiPoint.y);
	}

	protected void clearStyle(boolean isRootBlock) {
		this.borderInfo = null;
		this.borderInsets = null;
		this.borderTopColor = null;
		this.borderLeftColor = null;
		this.borderBottomColor = null;
		this.borderRightColor = null;
		this.zIndex = 0;
		this.backgroundColor = null;
		this.backgroundImage = null;
		this.lastBackgroundImageUri = null;
		this.overflowX = RenderState.OVERFLOW_VISIBLE;
		this.overflowY = RenderState.OVERFLOW_VISIBLE;
		if (isRootBlock) {
			// The margin of the root block behaves like extra padding.
			Insets insets1 = this.defaultMarginInsets;
			Insets insets2 = this.defaultPaddingInsets;
			Insets finalInsets = insets1 == null ? null : new Insets(insets1.top, insets1.left, insets1.bottom, insets1.right);
			if (insets2 != null) {
				if (finalInsets == null) {
					finalInsets = new Insets(insets2.top, insets2.left, insets2.bottom, insets2.right);
				} else {
					finalInsets.top += insets2.top;
					finalInsets.bottom += insets2.bottom;
					finalInsets.left += insets2.left;
					finalInsets.right += insets2.right;
				}
			}
			this.marginInsets = null;
			this.paddingInsets = finalInsets;
		} else {
			this.marginInsets = this.defaultMarginInsets;
			this.paddingInsets = this.defaultPaddingInsets;
		}
	}

	protected void applyStyle(int availWidth, int availHeight) {
		Object rootNode = this.modelNode;
		HTMLElementImpl rootElement;
		boolean isRootBlock;
		if (rootNode instanceof HTMLDocumentImpl) {
			isRootBlock = true;
			HTMLDocumentImpl doc = (HTMLDocumentImpl) rootNode;
			// Need to get BODY tag, for bgcolor, etc.
			rootElement = (HTMLElementImpl) doc.getBody();
		} else {
			isRootBlock = false;
			rootElement = (HTMLElementImpl) rootNode;
		}
		if (rootElement == null) {
			this.clearStyle(isRootBlock);
			return;
		}
		RenderState rs = rootElement.getRenderState();
		if (rs == null) {
			throw new IllegalStateException("Element without render state: " + rootElement + "; parent=" + rootElement.getParentNode());
		}
		BackgroundInfo binfo = rs.getBackgroundInfo();
		this.backgroundColor = binfo == null ? null : binfo.backgroundColor;
		java.net.URL backgroundImageUri = binfo == null ? null
				: binfo.backgroundImage;
		if (backgroundImageUri == null) {
			this.backgroundImage = null;
			this.lastBackgroundImageUri = null;
		} else if (!backgroundImageUri.equals(this.lastBackgroundImageUri)) {
			this.lastBackgroundImageUri = backgroundImageUri;
			this.loadBackgroundImage(backgroundImageUri);
		}
		AbstractCSS2Properties props = rootElement.getCurrentStyle();
		if (props == null) {
			this.clearStyle(isRootBlock);
		} else {
			BorderInfo borderInfo = rs.getBorderInfo();
			this.borderInfo = borderInfo;
			HtmlInsets binsets = borderInfo == null ? null : borderInfo.insets;
			HtmlInsets minsets = rs.getMarginInsets();
			HtmlInsets pinsets = rs.getPaddingInsets();
			Insets defaultMarginInsets = this.defaultMarginInsets;
			int dmleft = 0, dmright = 0, dmtop = 0, dmbottom = 0;
			if (defaultMarginInsets != null) {
				dmleft = defaultMarginInsets.left;
				dmright = defaultMarginInsets.right;
				dmtop = defaultMarginInsets.top;
				dmbottom = defaultMarginInsets.bottom;
			}
			Insets defaultPaddingInsets = this.defaultPaddingInsets;
			int dpleft = 0, dpright = 0, dptop = 0, dpbottom = 0;
			if (defaultPaddingInsets != null) {
				dpleft = defaultPaddingInsets.left;
				dpright = defaultPaddingInsets.right;
				dptop = defaultPaddingInsets.top;
				dpbottom = defaultPaddingInsets.bottom;
			}
			Insets borderInsets = binsets == null ? null : binsets.getAWTInsets(0, 0, 0, 0, availWidth, availHeight, 0, 0);
			if (borderInsets == null) {
				borderInsets = RBlockViewport.ZERO_INSETS;
			}
			Insets paddingInsets = pinsets == null ? defaultPaddingInsets : pinsets.getAWTInsets(dptop, dpleft, dpbottom, dpright, availWidth, availHeight, 0, 0);
			if (paddingInsets == null) {
				paddingInsets = RBlockViewport.ZERO_INSETS;
			}
			Insets tentativeMarginInsets = minsets == null ? defaultMarginInsets : minsets.getAWTInsets(dmtop, dmleft, dmbottom, dmright, availWidth, availHeight, 0, 0);
			if (tentativeMarginInsets == null) {
				tentativeMarginInsets = RBlockViewport.ZERO_INSETS;
			}
			int actualAvailWidth = availWidth - paddingInsets.left - paddingInsets.right - borderInsets.left - borderInsets.right - tentativeMarginInsets.left - tentativeMarginInsets.right;
			int actualAvailHeight = availHeight - paddingInsets.top - paddingInsets.bottom - borderInsets.top - borderInsets.bottom - tentativeMarginInsets.top - tentativeMarginInsets.bottom;
			Integer declaredWidth = this.getDeclaredWidth(rs, actualAvailWidth);
			Integer declaredHeight = this.getDeclaredHeight(rs, actualAvailHeight);
			int autoMarginX = 0, autoMarginY = 0;
			if (declaredWidth != null) {
				autoMarginX = (availWidth - declaredWidth - (borderInsets.left - borderInsets.right) - (paddingInsets.left - paddingInsets.right)) / 2;
			}
			if (declaredHeight != null) {
				autoMarginY = (availHeight - declaredHeight - (borderInsets.top - borderInsets.bottom) - (paddingInsets.top - paddingInsets.bottom)) / 2;
			}
			this.borderInsets = borderInsets;
			if (isRootBlock) {
				// In the root block, the margin behaves like an extra padding.
				Insets regularMarginInsets = (autoMarginX == 0 && autoMarginY == 0) ? tentativeMarginInsets : (minsets == null ? defaultMarginInsets : minsets.getAWTInsets(dmtop, dmleft, dmbottom, dmright, availWidth, availHeight, autoMarginX, autoMarginY));
				if (regularMarginInsets == null) {
					regularMarginInsets = RBlockViewport.ZERO_INSETS;
				}
				this.marginInsets = null;
				this.paddingInsets = new Insets(
						paddingInsets.top + regularMarginInsets.top,
						paddingInsets.left + regularMarginInsets.left,
						paddingInsets.bottom + regularMarginInsets.bottom,
						paddingInsets.right + regularMarginInsets.right);
			} else {
				this.paddingInsets = paddingInsets;
				this.marginInsets = (autoMarginX == 0 && autoMarginY == 0) ? tentativeMarginInsets : (minsets == null ? defaultMarginInsets : minsets.getAWTInsets(dmtop, dmleft, dmbottom, dmright, availWidth, availHeight, autoMarginX, autoMarginY));
			}
			if (borderInfo != null) {
				this.borderTopColor = borderInfo.topColor;
				this.borderLeftColor = borderInfo.leftColor;
				this.borderBottomColor = borderInfo.bottomColor;
				this.borderRightColor = borderInfo.rightColor;
			} else {
				this.borderTopColor = null;
				this.borderLeftColor = null;
				this.borderBottomColor = null;
				this.borderRightColor = null;
			}
			String zIndex = props.getZIndex();
			if (zIndex != null) {
				try {
					this.zIndex = Integer.parseInt(zIndex);
				} catch (NumberFormatException err) {
					this.zIndex = 0;
				}
			} else {
				this.zIndex = 0;
			}
			this.overflowX = rs.getOverflowX();
			this.overflowY = rs.getOverflowY();
		}

		// Check if background image needs to be loaded
	}

	private void loadBackgroundImage(final java.net.URL imageURL) {
		UserAgentContext ctx = this.userAgentContext;
		if (ctx != null) {
			final HttpRequest request = ctx.createHttpRequest();
			request.addReadyStateChangeListener(() -> {
				int readyState = request.getReadyState();
				if (readyState == HttpRequest.STATE_COMPLETE) {
					int status = request.getStatus();
					if (status == 200 || status == 0) {
						Image img = request.getResponseImage();
						BaseElementRenderable.this.backgroundImage = img;
						// Cause observer to be called
						int w = img.getWidth(BaseElementRenderable.this);
						int h = img.getHeight(BaseElementRenderable.this);
						// Maybe image already done...
						if (w != -1 && h != -1) {
							BaseElementRenderable.this.repaint();
						}
					}
				}
			});
			SecurityManager sm = System.getSecurityManager();
			if (sm == null) {
				try {
					request.open("GET", imageURL);
					request.send(null);
				} catch (java.io.IOException ignored) {
				}
			} else {
				AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
					try {
						request.open("GET", imageURL);
						request.send(null);
					} catch (IOException ignored) {
					}
					return null;
				});
			}
		}
	}

	public int getZIndex() {
		return this.zIndex;
	}

	private Color getBorderTopColor() {
		Color c = this.borderTopColor;
		return c == null ? Color.black : c;
	}

	private Color getBorderLeftColor() {
		Color c = this.borderLeftColor;
		return c == null ? Color.black : c;
	}

	private Color getBorderBottomColor() {
		Color c = this.borderBottomColor;
		return c == null ? Color.black : c;
	}

	private Color getBorderRightColor() {
		Color c = this.borderRightColor;
		return c == null ? Color.black : c;
	}

	void prePaint(java.awt.Graphics g) {
		int startWidth = this.width;
		int startHeight = this.height;
		int totalWidth = startWidth;
		int totalHeight = startHeight;
		int startX = 0;
		int startY = 0;
		RenderState rs = this.modelNode.getRenderState();
		Insets marginInsets = this.marginInsets;
		if (marginInsets != null) {
			totalWidth -= (marginInsets.left + marginInsets.right);
			totalHeight -= (marginInsets.top + marginInsets.bottom);
			startX += marginInsets.left;
			startY += marginInsets.top;
		}
		Insets borderInsets = this.borderInsets;
		if (borderInsets != null) {
			int btop = borderInsets.top;
			int bleft = borderInsets.left;
			int bright = borderInsets.right;
			int bbottom = borderInsets.bottom;

			int newTotalWidth = totalWidth - (bleft + bright);
			int newTotalHeight = totalHeight - (btop + bbottom);
			int newStartX = startX + bleft;
			int newStartY = startY + btop;
			Rectangle clientRegion = new Rectangle(newStartX, newStartY, newTotalWidth, newTotalHeight);

			// Paint borders if the clip bounds are not contained
			// by the content area.
			Rectangle clipBounds = g.getClipBounds();
			if (!clientRegion.contains(clipBounds)) {
				BorderInfo borderInfo = this.borderInfo;
				if (btop > 0) {
					g.setColor(this.getBorderTopColor());
					int borderStyle = borderInfo == null ? HtmlValues.BORDER_STYLE_SOLID : borderInfo.topStyle;
					for (int i = 0; i < btop; i++) {
						int leftOffset = (i * bleft) / btop;
						int rightOffset = (i * bright) / btop;
						if (borderStyle == HtmlValues.BORDER_STYLE_DASHED) {
							GUITasks.drawDashed(g, startX + leftOffset, startY + i, startX + totalWidth - rightOffset - 1, startY + i, 10 + btop, 6);
						} else {
							g.drawLine(startX + leftOffset, startY + i, startX + totalWidth - rightOffset - 1, startY + i);
						}
					}
				}
				if (bright > 0) {
					int borderStyle = borderInfo == null ? HtmlValues.BORDER_STYLE_SOLID : borderInfo.rightStyle;
					g.setColor(this.getBorderRightColor());
					int lastX = startX + totalWidth - 1;
					for (int i = 0; i < bright; i++) {
						int topOffset = (i * btop) / bright;
						int bottomOffset = (i * bbottom) / bright;
						if (borderStyle == HtmlValues.BORDER_STYLE_DASHED) {
							GUITasks.drawDashed(g, lastX - i, startY + topOffset, lastX - i, startY + totalHeight - bottomOffset - 1, 10 + bright, 6);
						} else {
							g.drawLine(lastX - i, startY + topOffset, lastX - i, startY + totalHeight - bottomOffset - 1);
						}
					}
				}
				if (bbottom > 0) {
					int borderStyle = borderInfo == null ? HtmlValues.BORDER_STYLE_SOLID : borderInfo.bottomStyle;
					g.setColor(this.getBorderBottomColor());
					int lastY = startY + totalHeight - 1;
					for (int i = 0; i < bbottom; i++) {
						int leftOffset = (i * bleft) / bbottom;
						int rightOffset = (i * bright) / bbottom;
						if (borderStyle == HtmlValues.BORDER_STYLE_DASHED) {
							GUITasks.drawDashed(g, startX + leftOffset, lastY - i, startX + totalWidth - rightOffset - 1, lastY - i, 10 + bbottom, 6);
						} else {
							g.drawLine(startX + leftOffset, lastY - i, startX + totalWidth - rightOffset - 1, lastY - i);
						}
					}
				}
				if (bleft > 0) {
					int borderStyle = borderInfo == null ? HtmlValues.BORDER_STYLE_SOLID : borderInfo.leftStyle;
					g.setColor(this.getBorderLeftColor());
					for (int i = 0; i < bleft; i++) {
						int topOffset = (i * btop) / bleft;
						int bottomOffset = (i * bbottom) / bleft;
						if (borderStyle == HtmlValues.BORDER_STYLE_DASHED) {
							GUITasks.drawDashed(g, startX + i, startY + topOffset, startX + i, startY + totalHeight - bottomOffset - 1, 10 + bleft, 6);
						} else {
							g.drawLine(startX + i, startY + topOffset, startX + i, startY + totalHeight - bottomOffset - 1);
						}
					}
				}
			}

			// Adjust client area border
			totalWidth = newTotalWidth;
			totalHeight = newTotalHeight;
			startX = newStartX;
			startY = newStartY;

		}
		// Using clientG (clipped below) beyond this point.
		Graphics clientG = g.create(startX, startY, totalWidth, totalHeight);
		try {
			Rectangle bkgBounds = null;
			Color bkg = this.backgroundColor;
			if (bkg != null && bkg.getAlpha() > 0) {
				clientG.setColor(bkg);
				bkgBounds = clientG.getClipBounds();
				clientG.fillRect(bkgBounds.x, bkgBounds.y, bkgBounds.width, bkgBounds.height);
			}
			BackgroundInfo binfo = rs == null ? null : rs.getBackgroundInfo();
			Image image = this.backgroundImage;
			if (image != null) {
				if (bkgBounds == null) {
					bkgBounds = clientG.getClipBounds();
				}
				int w = image.getWidth(this);
				int h = image.getHeight(this);
				if (w != -1 && h != -1) {
					switch (binfo == null ? BackgroundInfo.BR_REPEAT : binfo.backgroundRepeat) {
						case BackgroundInfo.BR_NO_REPEAT: {
							int imageX;
							if (binfo.backgroundXPositionAbsolute) {
								imageX = binfo.backgroundXPosition;
							} else {
								imageX = (binfo.backgroundXPosition * (totalWidth - w)) / 100;
							}
							int imageY;
							if (binfo.backgroundYPositionAbsolute) {
								imageY = binfo.backgroundYPosition;
							} else {
								imageY = (binfo.backgroundYPosition * (totalHeight - h)) / 100;
							}
							clientG.drawImage(image, imageX, imageY, w, h, this);
							break;
						}
						case BackgroundInfo.BR_REPEAT_X: {
							int imageY;
							if (binfo.backgroundYPositionAbsolute) {
								imageY = binfo.backgroundYPosition;
							} else {
								imageY = (binfo.backgroundYPosition * (totalHeight - h)) / 100;
							}
							// Modulate starting x.
							int x = (bkgBounds.x / w) * w;
							int topX = bkgBounds.x + bkgBounds.width;
							for (; x < topX; x += w) {
								clientG.drawImage(image, x, imageY, w, h, this);
							}
							break;
						}
						case BackgroundInfo.BR_REPEAT_Y: {
							int imageX;
							if (binfo.backgroundXPositionAbsolute) {
								imageX = binfo.backgroundXPosition;
							} else {
								imageX = (binfo.backgroundXPosition * (totalWidth - w)) / 100;
							}
							// Modulate starting y.
							int y = (bkgBounds.y / h) * h;
							int topY = bkgBounds.y + bkgBounds.height;
							for (; y < topY; y += h) {
								clientG.drawImage(image, imageX, y, w, h, this);
							}
							break;
						}
						default: {
							// Modulate starting x and y.
							int baseX = (bkgBounds.x / w) * w;
							int baseY = (bkgBounds.y / h) * h;
							int topX = bkgBounds.x + bkgBounds.width;
							int topY = bkgBounds.y + bkgBounds.height;
							// Replacing this:
							for (int x = baseX; x < topX; x += w) {
								for (int y = baseY; y < topY; y += h) {
									clientG.drawImage(image, x, y, w, h, this);
								}
							}
							break;
						}
					}
				}
			}
		} finally {
			clientG.dispose();
		}
	}

	public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
		// This is so that a loading image doesn't cause
		// too many repaint events.
		if ((infoflags & ImageObserver.ALLBITS) != 0 || (infoflags & ImageObserver.FRAMEBITS) != 0) {
			this.repaint();
		}
		return true;
	}

	static final int SCROLL_BAR_THICKNESS = 16;

	/**
	 * Gets insets of content area. It includes margin, borders
	 * and scrollbars, but not padding.
	 */
	public Insets getInsets(boolean hscroll, boolean vscroll) {
		this.modelNode.getRenderState();
		Insets mi = this.marginInsets;
		Insets bi = this.borderInsets;
		int top = 0;
		int bottom = 0;
		int left = 0;
		int right = 0;
		if (mi != null) {
			top += mi.top;
			left += mi.left;
			bottom += mi.bottom;
			right += mi.right;
		}
		if (bi != null) {
			top += bi.top;
			left += bi.left;
			bottom += bi.bottom;
			right += bi.right;
		}
		if (hscroll) {
			bottom += SCROLL_BAR_THICKNESS;
		}
		if (vscroll) {
			right += SCROLL_BAR_THICKNESS;
		}
		return new Insets(top, left, bottom, right);
	}

	final void sendDelayedPairsToParent() {
		// Ensures that parent has all the components
		// below this renderer node. (Parent expected to have removed them).
		Collection<DelayedPair> gc = this.delayedPairs;
		if (gc != null) {
			for (DelayedPair aGc : gc) {
				if (aGc.targetParent != this) {
					this.container.addDelayedPair(aGc);
				}
			}
		}
	}

	public final void clearDelayedPairs() {
		Collection<DelayedPair> gc = this.delayedPairs;
		if (gc != null) {
			gc.clear();
		}
	}

	public final Collection<DelayedPair> getDelayedPairs() {
		return this.delayedPairs;
	}

	/* (non-Javadoc)
	 * @see org.xamjwg.html.renderer.RenderableContainer#add(java.awt.Component)
	 */
	public void addDelayedPair(DelayedPair pair) {
		Collection<DelayedPair> gc = this.delayedPairs;
		if (gc == null) {
			gc = new java.util.LinkedList<>();
			this.delayedPairs = gc;
		}
		gc.add(pair);
	}

	public RenderableContainer getParentContainer() {
		return this.container;
	}

	public boolean isContainedByNode() {
		return true;
	}

	public int getCollapsibleMarginBottom() {
		int cm;
		Insets paddingInsets = this.paddingInsets;
		if (paddingInsets != null && paddingInsets.bottom > 0) {
			cm = 0;
		} else {
			Insets borderInsets = this.borderInsets;
			if (borderInsets != null && borderInsets.bottom > 0) {
				cm = 0;
			} else {
				cm = this.getMarginBottom();
			}
		}
		if (this.isMarginBoundary()) {
			RenderState rs = this.modelNode.getRenderState();
			if (rs != null) {
				FontMetrics fm = rs.getFontMetrics();
				int fontHeight = fm.getHeight();
				if (fontHeight > cm) {
					cm = fontHeight;
				}
			}
		}
		return cm;
	}

	protected boolean isMarginBoundary() {
		return (this.overflowY != RenderState.OVERFLOW_VISIBLE && this.overflowX != RenderState.OVERFLOW_NONE) || this.modelNode instanceof HTMLDocumentImpl;
	}

	public int getCollapsibleMarginTop() {
		int cm;
		Insets paddingInsets = this.paddingInsets;
		if (paddingInsets != null && paddingInsets.top > 0) {
			cm = 0;
		} else {
			Insets borderInsets = this.borderInsets;
			if (borderInsets != null && borderInsets.top > 0) {
				cm = 0;
			} else {
				cm = this.getMarginTop();
			}
		}
		if (this.isMarginBoundary()) {
			RenderState rs = this.modelNode.getRenderState();
			if (rs != null) {
				FontMetrics fm = rs.getFontMetrics();
				int fontHeight = fm.getHeight();
				if (fontHeight > cm) {
					cm = fontHeight;
				}
			}
		}
		return cm;
	}

	public int getMarginBottom() {
		Insets marginInsets = this.marginInsets;
		return marginInsets == null ? 0 : marginInsets.bottom;
	}

	public int getMarginTop() {
		Insets marginInsets = this.marginInsets;
		return marginInsets == null ? 0 : marginInsets.top;
	}
}
