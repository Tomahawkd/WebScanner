/*
    GNU LESSER GENERAL PUBLIC LICENSE
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
/*
 * Created on Nov 20, 2005
 */
package org.lobobrowser.html.style;

import org.lobobrowser.util.Urls;
import org.lobobrowser.util.gui.ColorFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSS2Properties;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleSheet;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class AbstractCSS2Properties implements CSS2Properties {

	private static final String FLOAT = "float";
	private static final String AZIMUTH = "azimuth";
	static final String BACKGROUND = "background";
	private static final String BACKGROUND_ATTACHMENT = "background-attachment";
	static final String BACKGROUND_COLOR = "background-color";
	static final String BACKGROUND_IMAGE = "background-image";
	static final String BACKGROUND_POSITION = "background-position";
	static final String BACKGROUND_REPEAT = "background-repeat";
	static final String BORDER = "border";
	static final String BORDER_BOTTOM = "border-bottom";
	private static final String BORDER_BOTTOM_COLOR = "border-bottom-color";
	private static final String BORDER_BOTTOM_STYLE = "border-bottom-style";
	private static final String BORDER_BOTTOM_WIDTH = "border-bottom-width";
	private static final String BORDER_COLLAPSE = "border-collapse";
	private static final String BORDER_COLOR = "border-color";
	static final String BORDER_LEFT = "border-left";
	private static final String BORDER_LEFT_COLOR = "border-left-color";
	private static final String BORDER_LEFT_STYLE = "border-left-style";
	private static final String BORDER_LEFT_WIDTH = "border-left-width";
	static final String BORDER_RIGHT = "border-right";
	private static final String BORDER_RIGHT_COLOR = "border-right-color";
	private static final String BORDER_RIGHT_STYLE = "border-right-style";
	private static final String BORDER_RIGHT_WIDTH = "border-right-width";
	private static final String BORDER_SPACING = "border-spacing";
	private static final String BORDER_STYLE = "border-style";
	static final String BORDER_TOP = "border-top";
	private static final String BORDER_TOP_COLOR = "border-top-color";
	private static final String BORDER_TOP_STYLE = "border-top-style";
	private static final String BORDER_TOP_WIDTH = "border-top-width";
	private static final String BORDER_WIDTH = "border-width";
	private static final String BOTTOM = "bottom";
	private static final String CAPTION_SIDE = "caption-side";
	private static final String CLEAR = "clear";
	private static final String CLIP = "clip";
	private static final String COLOR = "color";
	private static final String CONTENT = "content";
	private static final String COUNTER_INCREMENT = "counter-increment";
	private static final String COUNTER_RESET = "counter-reset";
	private static final String CSS_FLOAT = "css-float";
	private static final String CUE = "cue";
	private static final String CUE_AFTER = "cue-after";
	private static final String CUE_BEFORE = "cue-before";
	private static final String CURSOR = "cursor";
	private static final String DIRECTION = "direction";
	private static final String DISPLAY = "display";
	private static final String ELEVATION = "elevation";
	private static final String EMPTY_CELLS = "empty-cells";
	static final String FONT = "font";
	static final String FONT_FAMILY = "font-family";
	static final String FONT_SIZE = "font-size";
	private static final String FONT_SIZE_ADJUST = "font-size-adjust";
	private static final String FONT_STRETCH = "font-stretch";
	static final String FONT_STYLE = "font-style";
	static final String FONT_VARIANT = "font-variant";
	static final String FONT_WEIGHT = "font-weight";
	private static final String HEIGHT = "height";
	private static final String LEFT = "left";
	private static final String LETTER_SPACING = "letter-spacing";
	static final String LINE_HEIGHT = "line-height";
	private static final String LIST_STYLE = "list-style";
	private static final String LIST_STYLE_IMAGE = "list-style-image";
	private static final String LIST_STYLE_POSITION = "list-style-position";
	private static final String LIST_STYLE_TYPE = "list-style-type";
	private static final String MARGIN = "margin";
	private static final String MARGIN_BOTTOM = "margin-bottom";
	private static final String MARGIN_LEFT = "margin-left";
	private static final String MARGIN_RIGHT = "margin-right";
	private static final String MARGIN_TOP = "margin-top";
	private static final String MARKER_OFFSET = "marker-offset";
	private static final String MARKS = "marks";
	private static final String MAX_HEIGHT = "max-height";
	private static final String MAX_WIDTH = "max-width";
	private static final String MIN_HEIGHT = "min-height";
	private static final String MIN_WIDTH = "min-width";
	private static final String ORPHANS = "orphans";
	private static final String OUTLINE = "outline";
	private static final String OUTLINE_COLOR = "outline-color";
	private static final String OUTLINE_STYLE = "outline-style";
	private static final String OUTLINE_WIDTH = "outline-width";
	private static final String OVERFLOW = "overflow";
	private static final String PADDING = "padding";
	private static final String PADDING_BOTTOM = "padding-bottom";
	private static final String PADDING_LEFT = "padding-left";
	private static final String PADDING_RIGHT = "padding-right";
	private static final String PADDING_TOP = "padding-top";
	private static final String PAGE = "page";
	private static final String PAGE_BREAK_AFTER = "page-break-after";
	private static final String PAGE_BREAK_BEFORE = "page-break-before";
	private static final String PAGE_BREAK_INSIDE = "page-break-inside";
	private static final String PAUSE = "pause";
	private static final String PAUSE_AFTER = "pause-after";
	private static final String PAUSE_BEFORE = "pause-before";
	private static final String PITCH = "pitch";
	private static final String PITCH_RANGE = "pitch-range";
	private static final String PLAY_DURING = "play-during";
	private static final String POSITION = "position";
	private static final String QUOTES = "quotes";
	private static final String RICHNESS = "richness";
	private static final String RIGHT = "right";
	private static final String SIZE = "size";
	private static final String SPEAK = "speak";
	private static final String SPEAK_HEADER = "speak-header";
	private static final String SPEAK_NUMERAL = "speak-numeral";
	private static final String SPEAK_PUNCTUATION = "speak-puctuation";
	private static final String SPEECH_RATE = "speech-rate";
	private static final String STRESS = "stress";
	private static final String TABLE_LAYOUT = "table-layout";
	private static final String TEXT_ALIGN = "text-align";
	private static final String TEXT_DECORATION = "text-decoration";
	private static final String TEXT_INDENT = "text-indent";
	private static final String TEXT_SHADOW = "text-shadow";
	private static final String TEXT_TRANSFORM = "text-transform";
	private static final String TOP = "top";
	private static final String UNICODE_BIDI = "unicode-bidi";
	private static final String VERTICAL_ALIGN = "vertical-align";
	private static final String VISIBILITY = "visibility";
	private static final String VOICE_FAMILY = "voice-family";
	private static final String VOLUME = "volume";
	private static final String WHITE_SPACE = "white-space";
	private static final String WIDOWS = "widows";
	private static final String WIDTH = "width";
	private static final String WORD_SPACING = "word_spacing";
	private static final String Z_INDEX = "z-index";

	private static final Map<String, SubPropertySetter> SUB_SETTERS = new HashMap<>(20);

	private final CSS2PropertiesContext context;
	private AbstractCSS2Properties localStyleProperties;
	private Collection<CSSStyleDeclaration> styleDeclarations;
	private Map<String, Property> valueMap = null;

	static {
		Map<String, SubPropertySetter> subSetters = SUB_SETTERS;
		subSetters.put(MARGIN, new FourCornersSetter(MARGIN, "margin-", ""));
		subSetters.put(PADDING, new FourCornersSetter(PADDING, "padding-", ""));
		subSetters.put(BORDER, new BorderSetter1());
		subSetters.put(BORDER_TOP, new BorderSetter2(BORDER_TOP));
		subSetters.put(BORDER_LEFT, new BorderSetter2(BORDER_LEFT));
		subSetters.put(BORDER_BOTTOM, new BorderSetter2(BORDER_BOTTOM));
		subSetters.put(BORDER_RIGHT, new BorderSetter2(BORDER_RIGHT));
		subSetters.put(BORDER_COLOR, new FourCornersSetter(BORDER_COLOR, "border-", "-color"));
		subSetters.put(BORDER_STYLE, new FourCornersSetter(BORDER_STYLE, "border-", "-style"));
		subSetters.put(BORDER_WIDTH, new FourCornersSetter(BORDER_WIDTH, "border-", "-width"));
		subSetters.put(BACKGROUND, new BackgroundSetter());
		subSetters.put(BACKGROUND_IMAGE, new BackgroundImageSetter());
		subSetters.put(FONT, new FontSetter());
	}

	public AbstractCSS2Properties(CSS2PropertiesContext context) {
		this.context = context;
	}

	public void addStyleDeclaration(CSSStyleDeclaration styleDeclaration) {
		synchronized (this) {
			Collection<CSSStyleDeclaration> sd = this.styleDeclarations;
			if (sd == null) {
				sd = new LinkedList<>();
				this.styleDeclarations = sd;
			}
			sd.add(styleDeclaration);
			int length = styleDeclaration.getLength();
			for (int i = 0; i < length; i++) {
				String propertyName = styleDeclaration.item(i);
				String propertyValue = styleDeclaration.getPropertyValue(propertyName);
				String priority = styleDeclaration.getPropertyPriority(propertyName);
				boolean important = priority != null && priority.length() != 0 && "important".equals(priority);
				this.setPropertyValueProcessed(propertyName.toLowerCase(), propertyValue, styleDeclaration, important);
			}
		}
	}

	public void setLocalStyleProperties(AbstractCSS2Properties properties) {
		if (properties == this) {
			throw new IllegalStateException("setting same");
		}
		synchronized (this) {
			this.localStyleProperties = properties;
		}
	}

	final String getPropertyValue(String name) {
		return this.getPropertyValueLC(name.toLowerCase());
	}

	private String getPropertyValueLC(String lowerCaseName) {
		Map<String, Property> vm = this.valueMap;
		synchronized (this) {
			// Local properties have precedence
			AbstractCSS2Properties localProps = this.localStyleProperties;
			if (localProps != null) {
				String value = localProps.getPropertyValueLC(lowerCaseName);
				if (value != null) {
					return value;
				}
			}
			if (vm != null) {
				Property p = vm.get(lowerCaseName);
				return p == null ? null : p.value;
			}
		}
		return null;
	}

	/**
	 * Method called by property setters to set property values.
	 *
	 * @param lowerCaseName The name of the property in lowercase.
	 * @param value         The property value.
	 */
	protected void setPropertyValueLC(String lowerCaseName, String value) {
		Map<String, Property> vm = this.valueMap;
		synchronized (this) {
			if (vm == null) {
				vm = new HashMap<>(1);
				this.valueMap = vm;
			}
			vm.put(lowerCaseName, new Property(value, true));
		}
	}

	/**
	 * Alternate method called to set property values from
	 * CSS declarations.
	 * <p>
	 * This method checks importance of the value.
	 *
	 * @param lowerCaseName The name of the property in lowercase.
	 * @param value         The property value.
	 */
	final void setPropertyValueLCAlt(String lowerCaseName, String value, boolean important) {
		Map<String, Property> vm = this.valueMap;
		synchronized (this) {
			if (vm == null) {
				vm = new HashMap<>(1);
				this.valueMap = vm;
			} else {
				if (!important) {
					Property oldProperty = vm.get(lowerCaseName);
					if (oldProperty != null && oldProperty.important) {
						return;
					}
				}
			}
			vm.put(lowerCaseName, new Property(value, important));
		}
	}

	final void setPropertyValueProcessed(String lowerCaseName, String value, CSSStyleDeclaration declaration, boolean important) {
		SubPropertySetter setter = SUB_SETTERS.get(lowerCaseName);
		if (setter != null) {
			setter.changeValue(this, value, declaration, important);
		} else {
			this.setPropertyValueLCAlt(lowerCaseName, value, important);
		}
	}

	//---------- NonStandard properties

	private String overlayColor;

	String getOverlayColor() {
		return this.overlayColor;
	}

	public void setOverlayColor(String value) {
		this.overlayColor = value;
		this.context.informLookInvalid();
	}


	public String getFloat() {
		return this.getPropertyValueLC(FLOAT);
	}

	public void setFloat(String value) {
		this.setPropertyValueLC(FLOAT, value);
	}


	//---------- Implemented properties

	public String getAzimuth() {
		return this.getPropertyValueLC(AZIMUTH);
	}

	public String getBackground() {
		return this.getPropertyValueLC(BACKGROUND);
	}

	public String getBackgroundAttachment() {
		return this.getPropertyValueLC(BACKGROUND_ATTACHMENT);
	}

	public String getBackgroundColor() {
		return this.getPropertyValueLC(BACKGROUND_COLOR);
	}

	public String getBackgroundImage() {
		return this.getPropertyValueLC(BACKGROUND_IMAGE);
	}

	public String getBackgroundPosition() {
		return this.getPropertyValueLC(BACKGROUND_POSITION);
	}

	public String getBackgroundRepeat() {
		return this.getPropertyValueLC(BACKGROUND_REPEAT);
	}

	public String getBorder() {
		return this.getPropertyValueLC(BORDER);
	}

	public String getBorderBottom() {
		return this.getPropertyValueLC(BORDER_BOTTOM);
	}

	public String getBorderBottomColor() {
		return this.getPropertyValueLC(BORDER_BOTTOM_COLOR);
	}

	public String getBorderBottomStyle() {
		return this.getPropertyValueLC(BORDER_BOTTOM_STYLE);
	}

	public String getBorderBottomWidth() {
		return this.getPropertyValueLC(BORDER_BOTTOM_WIDTH);
	}

	public String getBorderCollapse() {
		return this.getPropertyValueLC(BORDER_COLLAPSE);
	}

	public String getBorderColor() {
		return this.getPropertyValueLC(BORDER_COLOR);
	}

	public String getBorderLeft() {
		return this.getPropertyValueLC(BORDER_LEFT);
	}

	public String getBorderLeftColor() {
		return this.getPropertyValueLC(BORDER_LEFT_COLOR);
	}

	public String getBorderLeftStyle() {
		return this.getPropertyValueLC(BORDER_LEFT_STYLE);
	}

	public String getBorderLeftWidth() {
		return this.getPropertyValueLC(BORDER_LEFT_WIDTH);
	}

	public String getBorderRight() {
		return this.getPropertyValueLC(BORDER_RIGHT);
	}

	public String getBorderRightColor() {
		return this.getPropertyValueLC(BORDER_RIGHT_COLOR);
	}

	public String getBorderRightStyle() {
		return this.getPropertyValueLC(BORDER_RIGHT_STYLE);
	}

	public String getBorderRightWidth() {
		return this.getPropertyValueLC(BORDER_RIGHT_WIDTH);
	}

	public String getBorderSpacing() {
		return this.getPropertyValueLC(BORDER_SPACING);
	}

	public String getBorderStyle() {
		return this.getPropertyValueLC(BORDER_STYLE);
	}

	public String getBorderTop() {
		return this.getPropertyValueLC(BORDER_TOP);
	}

	public String getBorderTopColor() {
		return this.getPropertyValueLC(BORDER_TOP_COLOR);
	}

	public String getBorderTopStyle() {
		return this.getPropertyValueLC(BORDER_TOP_STYLE);
	}

	public String getBorderTopWidth() {
		return this.getPropertyValueLC(BORDER_TOP_WIDTH);
	}

	public String getBorderWidth() {
		return this.getPropertyValueLC(BORDER_WIDTH);
	}

	public String getBottom() {
		return this.getPropertyValueLC(BOTTOM);
	}

	public String getCaptionSide() {
		return this.getPropertyValueLC(CAPTION_SIDE);
	}

	public String getClear() {
		return this.getPropertyValueLC(CLEAR);
	}

	public String getClip() {
		return this.getPropertyValueLC(CLIP);
	}

	public String getColor() {

		return this.getPropertyValueLC(COLOR);
	}

	public String getContent() {

		return this.getPropertyValueLC(CONTENT);
	}

	public String getCounterIncrement() {

		return this.getPropertyValueLC(COUNTER_INCREMENT);
	}

	public String getCounterReset() {

		return this.getPropertyValueLC(COUNTER_RESET);
	}

	public String getCssFloat() {

		return this.getPropertyValueLC(CSS_FLOAT);
	}

	public String getCue() {

		return this.getPropertyValueLC(CUE);
	}

	public String getCueAfter() {

		return this.getPropertyValueLC(CUE_AFTER);
	}

	public String getCueBefore() {

		return this.getPropertyValueLC(CUE_BEFORE);
	}

	public String getCursor() {

		return this.getPropertyValueLC(CURSOR);
	}

	public String getDirection() {

		return this.getPropertyValueLC(DIRECTION);
	}

	public String getDisplay() {

		return this.getPropertyValueLC(DISPLAY);
	}

	public String getElevation() {

		return this.getPropertyValueLC(ELEVATION);
	}

	public String getEmptyCells() {

		return this.getPropertyValueLC(EMPTY_CELLS);
	}

	public String getFont() {

		return this.getPropertyValueLC(FONT);
	}

	public String getFontFamily() {
		return this.getPropertyValueLC(FONT_FAMILY);
	}

	public String getFontSize() {
		return this.getPropertyValueLC(FONT_SIZE);
	}

	public String getFontSizeAdjust() {

		return this.getPropertyValueLC(FONT_SIZE_ADJUST);
	}

	public String getFontStretch() {

		return this.getPropertyValueLC(FONT_STRETCH);
	}

	public String getFontStyle() {

		return this.getPropertyValueLC(FONT_STYLE);
	}

	public String getFontVariant() {

		return this.getPropertyValueLC(FONT_VARIANT);
	}

	public String getFontWeight() {

		return this.getPropertyValueLC(FONT_WEIGHT);
	}

	public String getHeight() {

		return this.getPropertyValueLC(HEIGHT);
	}

	public String getLeft() {

		return this.getPropertyValueLC(LEFT);
	}

	public String getLetterSpacing() {

		return this.getPropertyValueLC(LETTER_SPACING);
	}

	public String getLineHeight() {

		return this.getPropertyValueLC(LINE_HEIGHT);
	}

	public String getListStyle() {
		return this.getPropertyValueLC(LIST_STYLE);
	}

	public String getListStyleImage() {
		return this.getPropertyValueLC(LIST_STYLE_IMAGE);
	}

	public String getListStylePosition() {
		return this.getPropertyValueLC(LIST_STYLE_POSITION);
	}

	public String getListStyleType() {
		return this.getPropertyValueLC(LIST_STYLE_TYPE);
	}

	public String getMargin() {

		return this.getPropertyValueLC(MARGIN);
	}

	public String getMarginBottom() {

		return this.getPropertyValueLC(MARGIN_BOTTOM);
	}

	public String getMarginLeft() {

		return this.getPropertyValueLC(MARGIN_LEFT);
	}

	public String getMarginRight() {

		return this.getPropertyValueLC(MARGIN_RIGHT);
	}

	public String getMarginTop() {

		return this.getPropertyValueLC(MARGIN_TOP);
	}

	public String getMarkerOffset() {

		return this.getPropertyValueLC(MARKER_OFFSET);
	}

	public String getMarks() {

		return this.getPropertyValueLC(MARKS);
	}

	public String getMaxHeight() {

		return this.getPropertyValueLC(MAX_HEIGHT);
	}

	public String getMaxWidth() {

		return this.getPropertyValueLC(MAX_WIDTH);
	}

	public String getMinHeight() {

		return this.getPropertyValueLC(MIN_HEIGHT);
	}

	public String getMinWidth() {

		return this.getPropertyValueLC(MIN_WIDTH);
	}

	public String getOrphans() {

		return this.getPropertyValueLC(ORPHANS);
	}

	public String getOutline() {

		return this.getPropertyValueLC(OUTLINE);
	}

	public String getOutlineColor() {

		return this.getPropertyValueLC(OUTLINE_COLOR);
	}

	public String getOutlineStyle() {

		return this.getPropertyValueLC(OUTLINE_STYLE);
	}

	public String getOutlineWidth() {

		return this.getPropertyValueLC(OUTLINE_WIDTH);
	}

	public String getOverflow() {
		return this.getPropertyValueLC(OVERFLOW);
	}

	public String getPadding() {
		return this.getPropertyValueLC(PADDING);
	}

	public String getPaddingBottom() {
		return this.getPropertyValueLC(PADDING_BOTTOM);
	}

	public String getPaddingLeft() {
		return this.getPropertyValueLC(PADDING_LEFT);
	}

	public String getPaddingRight() {

		return this.getPropertyValueLC(PADDING_RIGHT);
	}

	public String getPaddingTop() {

		return this.getPropertyValueLC(PADDING_TOP);
	}

	public String getPage() {

		return this.getPropertyValueLC(PAGE);
	}

	public String getPageBreakAfter() {

		return this.getPropertyValueLC(PAGE_BREAK_AFTER);
	}

	public String getPageBreakBefore() {

		return this.getPropertyValueLC(PAGE_BREAK_BEFORE);
	}

	public String getPageBreakInside() {

		return this.getPropertyValueLC(PAGE_BREAK_INSIDE);
	}

	public String getPause() {

		return this.getPropertyValueLC(PAUSE);
	}

	public String getPauseAfter() {

		return this.getPropertyValueLC(PAUSE_AFTER);
	}

	public String getPauseBefore() {

		return this.getPropertyValueLC(PAUSE_BEFORE);
	}

	public String getPitch() {

		return this.getPropertyValueLC(PITCH);
	}

	public String getPitchRange() {

		return this.getPropertyValueLC(PITCH_RANGE);
	}

	public String getPlayDuring() {

		return this.getPropertyValueLC(PLAY_DURING);
	}

	public String getPosition() {

		return this.getPropertyValueLC(POSITION);
	}

	public String getQuotes() {

		return this.getPropertyValueLC(QUOTES);
	}

	public String getRichness() {

		return this.getPropertyValueLC(RICHNESS);
	}

	public String getRight() {

		return this.getPropertyValueLC(RIGHT);
	}

	public String getSize() {

		return this.getPropertyValueLC(SIZE);
	}

	public String getSpeak() {

		return this.getPropertyValueLC(SPEAK);
	}

	public String getSpeakHeader() {

		return this.getPropertyValueLC(SPEAK_HEADER);
	}

	public String getSpeakNumeral() {

		return this.getPropertyValueLC(SPEAK_NUMERAL);
	}

	public String getSpeakPunctuation() {

		return this.getPropertyValueLC(SPEAK_PUNCTUATION);
	}

	public String getSpeechRate() {

		return this.getPropertyValueLC(SPEECH_RATE);
	}

	public String getStress() {

		return this.getPropertyValueLC(STRESS);
	}

	public String getTableLayout() {

		return this.getPropertyValueLC(TABLE_LAYOUT);
	}

	public String getTextAlign() {
		return this.getPropertyValueLC(TEXT_ALIGN);
	}

	public String getTextDecoration() {

		return this.getPropertyValueLC(TEXT_DECORATION);
	}

	public String getTextIndent() {

		return this.getPropertyValueLC(TEXT_INDENT);
	}

	public String getTextShadow() {

		return this.getPropertyValueLC(TEXT_SHADOW);
	}

	public String getTextTransform() {

		return this.getPropertyValueLC(TEXT_TRANSFORM);
	}

	public String getTop() {

		return this.getPropertyValueLC(TOP);
	}

	public String getUnicodeBidi() {

		return this.getPropertyValueLC(UNICODE_BIDI);
	}

	public String getVerticalAlign() {

		return this.getPropertyValueLC(VERTICAL_ALIGN);
	}

	public String getVisibility() {

		return this.getPropertyValueLC(VISIBILITY);
	}

	public String getVoiceFamily() {

		return this.getPropertyValueLC(VOICE_FAMILY);
	}

	public String getVolume() {

		return this.getPropertyValueLC(VOLUME);
	}

	public String getWhiteSpace() {

		return this.getPropertyValueLC(WHITE_SPACE);
	}

	public String getWidows() {

		return this.getPropertyValueLC(WIDOWS);
	}

	public String getWidth() {

		return this.getPropertyValueLC(WIDTH);
	}

	public String getWordSpacing() {

		return this.getPropertyValueLC(WORD_SPACING);
	}

	public String getZIndex() {

		return this.getPropertyValueLC(Z_INDEX);
	}

	public void setAzimuth(String azimuth) throws DOMException {
		this.setPropertyValueLC(AZIMUTH, azimuth);
	}

	public void setBackground(String background) throws DOMException {
		this.checkSetProperty();
		new BackgroundSetter().changeValue(this, background, null);
		this.context.informLookInvalid();
	}

	public void setBackgroundAttachment(String backgroundAttachment) throws DOMException {
		this.setPropertyValueLC(BACKGROUND_ATTACHMENT, backgroundAttachment);
		this.context.informLookInvalid();
	}

	public void setBackgroundColor(String backgroundColor) throws DOMException {
		this.setPropertyValueLC(BACKGROUND_COLOR, backgroundColor);
		this.context.informLookInvalid();
	}

	public void setBackgroundImage(String backgroundImage) throws DOMException {
		this.checkSetProperty();
		new BackgroundImageSetter().changeValue(this, backgroundImage, null);
		this.context.informLookInvalid();
	}

	public void setBackgroundPosition(String backgroundPosition) throws DOMException {
		this.setPropertyValueLC(BACKGROUND_POSITION, backgroundPosition);
		this.context.informLookInvalid();
	}

	public void setBackgroundRepeat(String backgroundRepeat) throws DOMException {
		this.setPropertyValueLC(BACKGROUND_REPEAT, backgroundRepeat);
		this.context.informLookInvalid();
	}

	public void setBorder(String border) throws DOMException {
		this.checkSetProperty();
		new BorderSetter1().changeValue(this, border, null);
		this.context.informInvalid();
	}

	public void setBorderBottom(String borderBottom) throws DOMException {
		this.checkSetProperty();
		new BorderSetter2(BORDER_BOTTOM).changeValue(this, borderBottom, null);
		this.context.informInvalid();
	}

	public void setBorderBottomColor(String borderBottomColor) throws DOMException {
		this.setPropertyValueLC(BORDER_BOTTOM_COLOR, borderBottomColor);
		this.context.informLookInvalid();
	}

	public void setBorderBottomStyle(String borderBottomStyle) throws DOMException {
		this.setPropertyValueLC(BORDER_BOTTOM_STYLE, borderBottomStyle);
		this.context.informLookInvalid();
	}

	public void setBorderBottomWidth(String borderBottomWidth) throws DOMException {
		this.setPropertyValueLC(BORDER_BOTTOM_WIDTH, borderBottomWidth);
		this.context.informInvalid();
	}

	public void setBorderCollapse(String borderCollapse) throws DOMException {
		this.setPropertyValueLC(BORDER_COLLAPSE, borderCollapse);
		this.context.informInvalid();
	}

	public void setBorderColor(String borderColor) throws DOMException {
		this.checkSetProperty();
		new FourCornersSetter(BORDER_COLOR, "border-", "-color").changeValue(this, borderColor, null);
		this.context.informLookInvalid();
	}

	public void setBorderLeft(String borderLeft) throws DOMException {
		this.checkSetProperty();
		new BorderSetter2(BORDER_LEFT).changeValue(this, borderLeft, null);
		this.context.informInvalid();
	}

	public void setBorderLeftColor(String borderLeftColor) throws DOMException {
		this.setPropertyValueLC(BORDER_LEFT_COLOR, borderLeftColor);
		this.context.informLookInvalid();
	}

	public void setBorderLeftStyle(String borderLeftStyle) throws DOMException {
		this.setPropertyValueLC(BORDER_LEFT_STYLE, borderLeftStyle);
		this.context.informLookInvalid();
	}

	public void setBorderLeftWidth(String borderLeftWidth) throws DOMException {
		this.setPropertyValueLC(BORDER_LEFT_WIDTH, borderLeftWidth);
		this.context.informInvalid();
	}

	public void setBorderRight(String borderRight) throws DOMException {
		this.checkSetProperty();
		new BorderSetter2(BORDER_RIGHT).changeValue(this, borderRight, null);
		this.context.informInvalid();
	}

	public void setBorderRightColor(String borderRightColor) throws DOMException {
		this.setPropertyValueLC(BORDER_RIGHT_COLOR, borderRightColor);
		this.context.informLookInvalid();
	}

	public void setBorderRightStyle(String borderRightStyle) throws DOMException {
		this.setPropertyValueLC(BORDER_RIGHT_STYLE, borderRightStyle);
		this.context.informLookInvalid();
	}

	public void setBorderRightWidth(String borderRightWidth) throws DOMException {
		this.setPropertyValueLC(BORDER_RIGHT_WIDTH, borderRightWidth);
		this.context.informInvalid();
	}

	public void setBorderSpacing(String borderSpacing) throws DOMException {
		this.setPropertyValueLC(BORDER_SPACING, borderSpacing);
		this.context.informInvalid();
	}

	public void setBorderStyle(String borderStyle) throws DOMException {
		this.checkSetProperty();
		new FourCornersSetter(BORDER_STYLE, "border-", "-style").changeValue(this, borderStyle, null);
		this.context.informLookInvalid();
	}

	public void setBorderTop(String borderTop) throws DOMException {
		this.checkSetProperty();
		new BorderSetter2(BORDER_TOP).changeValue(this, borderTop, null);
		this.context.informInvalid();
	}

	public void setBorderTopColor(String borderTopColor) throws DOMException {
		this.setPropertyValueLC(BORDER_TOP_COLOR, borderTopColor);
		this.context.informLookInvalid();
	}

	public void setBorderTopStyle(String borderTopStyle) throws DOMException {
		this.setPropertyValueLC(BORDER_TOP_STYLE, borderTopStyle);
		this.context.informLookInvalid();
	}

	public void setBorderTopWidth(String borderTopWidth) throws DOMException {
		this.setPropertyValueLC(BORDER_TOP_WIDTH, borderTopWidth);
		this.context.informInvalid();
	}

	public void setBorderWidth(String borderWidth) throws DOMException {
		this.checkSetProperty();
		new FourCornersSetter(BORDER_WIDTH, "border-", "-width").changeValue(this, borderWidth, null);
		this.context.informInvalid();
	}

	public void setBottom(String bottom) throws DOMException {
		this.setPropertyValueLC(BOTTOM, bottom);
		this.context.informPositionInvalid();
	}

	public void setCaptionSide(String captionSide) throws DOMException {
		this.setPropertyValueLC(CAPTION_SIDE, captionSide);
	}

	public void setClear(String clear) throws DOMException {
		this.setPropertyValueLC(CLEAR, clear);
		this.context.informInvalid();
	}

	public void setClip(String clip) throws DOMException {
		this.setPropertyValueLC(CLIP, clip);
	}

	public void setColor(String color) throws DOMException {
		this.setPropertyValueLC(COLOR, color);
		this.context.informLookInvalid();
	}

	public void setContent(String content) throws DOMException {
		this.setPropertyValueLC(CONTENT, content);
		this.context.informInvalid();
	}

	public void setCounterIncrement(String counterIncrement) throws DOMException {
		this.setPropertyValueLC(COUNTER_INCREMENT, counterIncrement);
		this.context.informLookInvalid();
	}

	public void setCounterReset(String counterReset) throws DOMException {
		this.setPropertyValueLC(COUNTER_RESET, counterReset);
		this.context.informLookInvalid();
	}

	public void setCssFloat(String cssFloat) throws DOMException {
		this.setPropertyValueLC(CSS_FLOAT, cssFloat);
		this.context.informInvalid();
	}

	public void setCue(String cue) throws DOMException {
		this.setPropertyValueLC(CUE, cue);
	}

	public void setCueAfter(String cueAfter) throws DOMException {
		this.setPropertyValueLC(CUE_AFTER, cueAfter);
	}

	public void setCueBefore(String cueBefore) throws DOMException {
		this.setPropertyValueLC(CUE_BEFORE, cueBefore);
	}

	public void setCursor(String cursor) throws DOMException {
		this.setPropertyValueLC(CURSOR, cursor);
		this.context.informLookInvalid();
	}

	public void setDirection(String direction) throws DOMException {
		this.setPropertyValueLC(DIRECTION, direction);
		this.context.informInvalid();
	}

	public void setDisplay(String display) throws DOMException {
		this.setPropertyValueLC(DISPLAY, display);
		this.context.informInvalid();
	}

	public void setElevation(String elevation) throws DOMException {
		this.setPropertyValueLC(ELEVATION, elevation);
		this.context.informInvalid();
	}

	public void setEmptyCells(String emptyCells) throws DOMException {
		this.setPropertyValueLC(EMPTY_CELLS, emptyCells);
	}

	public void setFont(String font) throws DOMException {
		this.checkSetProperty();
		new FontSetter().changeValue(this, font, null);
		this.context.informInvalid();
	}

	public void setFontFamily(String fontFamily) throws DOMException {
		this.setPropertyValueLC(FONT_FAMILY, fontFamily);
		this.context.informInvalid();
	}

	public void setFontSize(String fontSize) throws DOMException {
		this.setPropertyValueLC(FONT_SIZE, fontSize);
		this.context.informInvalid();
	}

	public void setFontSizeAdjust(String fontSizeAdjust) throws DOMException {
		this.setPropertyValueLC(FONT_SIZE_ADJUST, fontSizeAdjust);
		this.context.informInvalid();
	}

	public void setFontStretch(String fontStretch) throws DOMException {
		this.setPropertyValueLC(FONT_STRETCH, fontStretch);
		this.context.informInvalid();
	}

	public void setFontStyle(String fontStyle) throws DOMException {
		this.setPropertyValueLC(FONT_STYLE, fontStyle);
		this.context.informInvalid();
	}

	public void setFontVariant(String fontVariant) throws DOMException {
		this.setPropertyValueLC(FONT_VARIANT, fontVariant);
		this.context.informInvalid();
	}

	public void setFontWeight(String fontWeight) throws DOMException {
		this.setPropertyValueLC(FONT_WEIGHT, fontWeight);
		this.context.informInvalid();
	}

	public void setHeight(String height) throws DOMException {
		this.setPropertyValueLC(HEIGHT, height);
		this.context.informSizeInvalid();
	}

	public void setLeft(String left) throws DOMException {
		this.setPropertyValueLC(LEFT, left);
		this.context.informPositionInvalid();
	}

	public void setLetterSpacing(String letterSpacing) throws DOMException {
		this.setPropertyValueLC(LETTER_SPACING, letterSpacing);
		this.context.informInvalid();
	}

	public void setLineHeight(String lineHeight) throws DOMException {
		this.setPropertyValueLC(LINE_HEIGHT, lineHeight);
		this.context.informInvalid();
	}

	public void setListStyle(String listStyle) throws DOMException {
		this.setPropertyValueLC(LIST_STYLE, listStyle);
		this.context.informInvalid();
	}

	public void setListStyleImage(String listStyleImage) throws DOMException {
		this.setPropertyValueLC(LIST_STYLE_IMAGE, listStyleImage);
		this.context.informLookInvalid();
	}

	public void setListStylePosition(String listStylePosition) throws DOMException {
		this.setPropertyValueLC(LIST_STYLE_POSITION, listStylePosition);
		this.context.informInvalid();
	}

	public void setListStyleType(String listStyleType) throws DOMException {
		this.setPropertyValueLC(LIST_STYLE_TYPE, listStyleType);
		this.context.informLookInvalid();
	}

	public void setMargin(String margin) throws DOMException {
		this.checkSetProperty();
		new AbstractCSS2Properties.FourCornersSetter(MARGIN, "margin-", "").changeValue(this, margin, null);
		this.context.informInvalid();
	}

	public void setMarginBottom(String marginBottom) throws DOMException {
		this.setPropertyValueLC(MARGIN_BOTTOM, marginBottom);
		this.context.informInvalid();
	}

	public void setMarginLeft(String marginLeft) throws DOMException {
		this.setPropertyValueLC(MARGIN_LEFT, marginLeft);
		this.context.informInvalid();
	}

	public void setMarginRight(String marginRight) throws DOMException {
		this.setPropertyValueLC(MARGIN_RIGHT, marginRight);
		this.context.informInvalid();
	}

	public void setMarginTop(String marginTop) throws DOMException {
		this.setPropertyValueLC(MARGIN_TOP, marginTop);
		this.context.informInvalid();
	}

	public void setMarkerOffset(String markerOffset) throws DOMException {
		this.setPropertyValueLC(MARKER_OFFSET, markerOffset);
	}

	public void setMarks(String marks) throws DOMException {
		this.setPropertyValueLC(MARKS, marks);
	}

	public void setMaxHeight(String maxHeight) throws DOMException {
		this.setPropertyValueLC(MAX_HEIGHT, maxHeight);
		this.context.informSizeInvalid();
	}

	public void setMaxWidth(String maxWidth) throws DOMException {
		this.setPropertyValueLC(MAX_WIDTH, maxWidth);
		this.context.informSizeInvalid();
	}

	public void setMinHeight(String minHeight) throws DOMException {
		this.setPropertyValueLC(MIN_HEIGHT, minHeight);
		this.context.informSizeInvalid();
	}

	public void setMinWidth(String minWidth) throws DOMException {
		this.setPropertyValueLC(MIN_WIDTH, minWidth);
		this.context.informSizeInvalid();
	}

	public void setOrphans(String orphans) throws DOMException {
		this.setPropertyValueLC(ORPHANS, orphans);
	}

	public void setOutline(String outline) throws DOMException {
		this.setPropertyValueLC(OUTLINE, outline);
		this.context.informInvalid();
	}

	public void setOutlineColor(String outlineColor) throws DOMException {
		this.setPropertyValueLC(OUTLINE_COLOR, outlineColor);
		this.context.informLookInvalid();
	}

	public void setOutlineStyle(String outlineStyle) throws DOMException {
		this.setPropertyValueLC(OUTLINE_STYLE, outlineStyle);
		this.context.informLookInvalid();
	}

	public void setOutlineWidth(String outlineWidth) throws DOMException {
		this.setPropertyValueLC(OUTLINE_WIDTH, outlineWidth);
		this.context.informInvalid();
	}

	public void setOverflow(String overflow) throws DOMException {
		this.setPropertyValueLC(OVERFLOW, overflow);
		this.context.informInvalid();
	}

	public void setPadding(String padding) throws DOMException {
		this.checkSetProperty();
		new FourCornersSetter(PADDING, "padding-", "").changeValue(this, padding, null);
		this.context.informInvalid();
	}

	public void setPaddingBottom(String paddingBottom) throws DOMException {
		this.setPropertyValueLC(PADDING_BOTTOM, paddingBottom);
		this.context.informInvalid();
	}

	public void setPaddingLeft(String paddingLeft) throws DOMException {
		this.setPropertyValueLC(PADDING_LEFT, paddingLeft);
		this.context.informInvalid();
	}

	public void setPaddingRight(String paddingRight) throws DOMException {
		this.setPropertyValueLC(PADDING_RIGHT, paddingRight);
		this.context.informInvalid();
	}

	public void setPaddingTop(String paddingTop) throws DOMException {
		this.setPropertyValueLC(PADDING_TOP, paddingTop);
		this.context.informInvalid();
	}

	public void setPage(String page) throws DOMException {
		this.setPropertyValueLC(PAGE, page);
	}

	public void setPageBreakAfter(String pageBreakAfter) throws DOMException {
		this.setPropertyValueLC(PAGE_BREAK_AFTER, pageBreakAfter);
		this.context.informInvalid();
	}

	public void setPageBreakBefore(String pageBreakBefore) throws DOMException {
		this.setPropertyValueLC(PAGE_BREAK_BEFORE, pageBreakBefore);
		this.context.informInvalid();
	}

	public void setPageBreakInside(String pageBreakInside) throws DOMException {
		this.setPropertyValueLC(PAGE_BREAK_INSIDE, pageBreakInside);
		this.context.informInvalid();
	}

	public void setPause(String pause) throws DOMException {
		this.setPropertyValueLC(PAUSE, pause);
	}

	public void setPauseAfter(String pauseAfter) throws DOMException {
		this.setPropertyValueLC(PAUSE_AFTER, pauseAfter);
	}

	public void setPauseBefore(String pauseBefore) throws DOMException {
		this.setPropertyValueLC(PAUSE_BEFORE, pauseBefore);
	}

	public void setPitch(String pitch) throws DOMException {
		this.setPropertyValueLC(PITCH, pitch);
	}

	public void setPitchRange(String pitchRange) throws DOMException {
		this.setPropertyValueLC(PITCH_RANGE, pitchRange);
	}

	public void setPlayDuring(String playDuring) throws DOMException {
		this.setPropertyValueLC(PLAY_DURING, playDuring);
	}

	public void setPosition(String position) throws DOMException {
		this.setPropertyValueLC(POSITION, position);
		this.context.informPositionInvalid();
	}

	public void setQuotes(String quotes) throws DOMException {
		this.setPropertyValueLC(QUOTES, quotes);
	}

	public void setRichness(String richness) throws DOMException {
		this.setPropertyValueLC(RICHNESS, richness);
	}

	public void setRight(String right) throws DOMException {
		this.setPropertyValueLC(RIGHT, right);
		this.context.informPositionInvalid();
	}

	public void setSize(String size) throws DOMException {
		this.setPropertyValueLC(SIZE, size);
		this.context.informInvalid();
	}

	public void setSpeak(String speak) throws DOMException {
		this.setPropertyValueLC(SPEAK, speak);
	}

	public void setSpeakHeader(String speakHeader) throws DOMException {
		this.setPropertyValueLC(SPEAK_HEADER, speakHeader);
	}

	public void setSpeakNumeral(String speakNumeral) throws DOMException {
		this.setPropertyValueLC(SPEAK_NUMERAL, speakNumeral);
	}

	public void setSpeakPunctuation(String speakPunctuation) throws DOMException {
		this.setPropertyValueLC(SPEAK_PUNCTUATION, speakPunctuation);
	}

	public void setSpeechRate(String speechRate) throws DOMException {
		this.setPropertyValueLC(SPEECH_RATE, speechRate);
	}

	public void setStress(String stress) throws DOMException {
		this.setPropertyValueLC(STRESS, stress);
	}

	public void setTableLayout(String tableLayout) throws DOMException {
		this.setPropertyValueLC(TABLE_LAYOUT, tableLayout);
		this.context.informInvalid();
	}

	public void setTextAlign(String textAlign) throws DOMException {
		this.setPropertyValueLC(TEXT_ALIGN, textAlign);
		this.context.informLayoutInvalid();
	}

	public void setTextDecoration(String textDecoration) throws DOMException {
		this.setPropertyValueLC(TEXT_DECORATION, textDecoration);
		this.context.informLookInvalid();
	}

	public void setTextIndent(String textIndent) throws DOMException {
		this.setPropertyValueLC(TEXT_INDENT, textIndent);
		this.context.informLayoutInvalid();
	}

	public void setTextShadow(String textShadow) throws DOMException {
		this.setPropertyValueLC(TEXT_SHADOW, textShadow);
		this.context.informLookInvalid();
	}

	public void setTextTransform(String textTransform) throws DOMException {
		this.setPropertyValueLC(TEXT_TRANSFORM, textTransform);
		this.context.informInvalid();
	}

	public void setTop(String top) throws DOMException {
		this.setPropertyValueLC(TOP, top);
		this.context.informPositionInvalid();
	}

	public void setUnicodeBidi(String unicodeBidi) throws DOMException {
		this.setPropertyValueLC(UNICODE_BIDI, unicodeBidi);
		this.context.informInvalid();
	}

	public void setVerticalAlign(String verticalAlign) throws DOMException {
		this.setPropertyValueLC(VERTICAL_ALIGN, verticalAlign);
		this.context.informInvalid();
	}

	public void setVisibility(String visibility) throws DOMException {
		this.setPropertyValueLC(VISIBILITY, visibility);
		this.context.informLookInvalid();
	}

	public void setVoiceFamily(String voiceFamily) throws DOMException {
		this.setPropertyValueLC(VOICE_FAMILY, voiceFamily);
	}

	public void setVolume(String volume) throws DOMException {
		this.setPropertyValueLC(VOLUME, volume);
	}

	public void setWhiteSpace(String whiteSpace) throws DOMException {
		this.setPropertyValueLC(WHITE_SPACE, whiteSpace);
		this.context.informInvalid();
	}

	public void setWidows(String widows) throws DOMException {
		this.setPropertyValueLC(WIDOWS, widows);
	}

	public void setWidth(String width) throws DOMException {
		this.setPropertyValueLC(WIDTH, width);
		this.context.informSizeInvalid();
	}

	public void setWordSpacing(String wordSpacing) throws DOMException {
		this.setPropertyValueLC(WORD_SPACING, wordSpacing);
		this.context.informInvalid();
	}

	public void setZIndex(String zIndex) throws DOMException {
		this.setPropertyValueLC(Z_INDEX, zIndex);
		this.context.informPositionInvalid();
	}

	/**
	 * Does nothing but may be overridden. This method
	 * is called by some property setters.
	 */
	protected void checkSetProperty() {
	}

	public String toString() {
		int size;
		synchronized (this) {
			Map map = this.valueMap;
			size = map == null ? 0 : map.size();
		}
		return this.getClass().getSimpleName() + "[size=" + size + "]";
	}

	private interface SubPropertySetter {
		void changeValue(AbstractCSS2Properties properties, String newValue, CSSStyleDeclaration declaration, boolean important);
	}

	private static class BorderSetter1 implements SubPropertySetter {
		void changeValue(AbstractCSS2Properties properties, String newValue, CSSStyleDeclaration declaration) {
			this.changeValue(properties, newValue, declaration, true);
		}

		public void changeValue(AbstractCSS2Properties properties, String newValue, CSSStyleDeclaration declaration, boolean important) {
			properties.setPropertyValueLCAlt(BORDER, newValue, important);
			properties.setPropertyValueProcessed(BORDER_TOP, newValue, declaration, important);
			properties.setPropertyValueProcessed(BORDER_LEFT, newValue, declaration, important);
			properties.setPropertyValueProcessed(BORDER_BOTTOM, newValue, declaration, important);
			properties.setPropertyValueProcessed(BORDER_RIGHT, newValue, declaration, important);
		}
	}

	private static class BorderSetter2 implements SubPropertySetter {
		private final String name;

		BorderSetter2(String baseName) {
			this.name = baseName;
		}

		void changeValue(AbstractCSS2Properties properties, String value, CSSStyleDeclaration declaration) {
			this.changeValue(properties, value, declaration, true);
		}

		public void changeValue(AbstractCSS2Properties properties, String value, CSSStyleDeclaration declaration, boolean important) {
			properties.setPropertyValueLCAlt(this.name, value, important);
			if (value != null && value.length() > 0) {
				String[] array = HtmlValues.splitCssValue(value);
				String color = null;
				String style = null;
				String width = null;
				for (String token : array) {
					if (HtmlValues.isBorderStyle(token)) {
						style = token;
					} else if (ColorFactory.getInstance().isColor(token)) {
						color = token;
					} else {
						width = token;
					}
				}
				String name = this.name;
				if (color != null) {
					properties.setPropertyValueLCAlt(name + "-color", color, important);
				}
				if (width != null) {
					properties.setPropertyValueLCAlt(name + "-width", width, important);
				}
				if (style != null) {
					properties.setPropertyValueLCAlt(name + "-style", style, important);
				}
			}
		}
	}

	private static class FourCornersSetter implements SubPropertySetter {
		private final String prefix;
		private final String suffix;
		private final String property;

		FourCornersSetter(String property, String prefix, String suffix) {
			this.prefix = prefix;
			this.suffix = suffix;
			this.property = property;
		}

		void changeValue(AbstractCSS2Properties properties, String newValue, CSSStyleDeclaration declaration) {
			this.changeValue(properties, newValue, declaration, true);
		}

		public void changeValue(AbstractCSS2Properties properties, String newValue, CSSStyleDeclaration declaration, boolean important) {
			properties.setPropertyValueLCAlt(this.property, newValue, important);
			if (newValue != null && newValue.length() > 0) {
				String[] array = HtmlValues.splitCssValue(newValue);
				int size = array.length;
				if (size == 1) {
					String prefix = this.prefix;
					String suffix = this.suffix;
					String value = array[0];
					properties.setPropertyValueLCAlt(prefix + "top" + suffix, value, important);
					properties.setPropertyValueLCAlt(prefix + "right" + suffix, value, important);
					properties.setPropertyValueLCAlt(prefix + "bottom" + suffix, value, important);
					properties.setPropertyValueLCAlt(prefix + "left" + suffix, value, important);
				} else if (size >= 4) {
					String prefix = this.prefix;
					String suffix = this.suffix;
					properties.setPropertyValueLCAlt(prefix + "top" + suffix, array[0], important);
					properties.setPropertyValueLCAlt(prefix + "right" + suffix, array[1], important);
					properties.setPropertyValueLCAlt(prefix + "bottom" + suffix, array[2], important);
					properties.setPropertyValueLCAlt(prefix + "left" + suffix, array[3], important);
				} else if (size == 2) {
					String prefix = this.prefix;
					String suffix = this.suffix;
					properties.setPropertyValueLCAlt(prefix + "top" + suffix, array[0], important);
					properties.setPropertyValueLCAlt(prefix + "right" + suffix, array[1], important);
					properties.setPropertyValueLCAlt(prefix + "bottom" + suffix, array[0], important);
					properties.setPropertyValueLCAlt(prefix + "left" + suffix, array[1], important);
				} else if (size == 3) {
					String prefix = this.prefix;
					String suffix = this.suffix;
					properties.setPropertyValueLCAlt(prefix + "top" + suffix, array[0], important);
					properties.setPropertyValueLCAlt(prefix + "right" + suffix, array[1], important);
					properties.setPropertyValueLCAlt(prefix + "bottom" + suffix, array[2], important);
					properties.setPropertyValueLCAlt(prefix + "left" + suffix, array[1], important);
				}
			}
		}
	}

	private static class BackgroundImageSetter implements SubPropertySetter {
		void changeValue(AbstractCSS2Properties properties, String newValue, CSSStyleDeclaration declaration) {
			this.changeValue(properties, newValue, declaration, true);
		}

		public void changeValue(AbstractCSS2Properties properties, String newValue, CSSStyleDeclaration declaration, boolean important) {
			String baseHref = null;
			String finalValue;
			if (declaration != null) {
				CSSRule rule = declaration.getParentRule();
				if (rule != null) {
					CSSStyleSheet sheet = rule.getParentStyleSheet();
					if (sheet instanceof com.steadystate.css.dom.CSSStyleSheetImpl) {
						com.steadystate.css.dom.CSSStyleSheetImpl ssheet = (com.steadystate.css.dom.CSSStyleSheetImpl) sheet;
						baseHref = ssheet.getHref();
					}
				}
			}
			if (baseHref == null) {
				baseHref = properties.context.getDocumentBaseURI();
			}
			String start = "url(";
			if (newValue == null || !newValue.toLowerCase().startsWith(start)) {
				finalValue = newValue;
			} else {
				int startIdx = start.length();
				int closingIdx = newValue.lastIndexOf(')');
				if (closingIdx == -1) {
					finalValue = newValue;
				} else {
					String quotedUri = newValue.substring(startIdx, closingIdx);
					String tentativeUri = HtmlValues.unquoteAndUnescape(quotedUri);
					if (baseHref == null) {
						finalValue = newValue;
					} else {
						try {
							java.net.URL styleUrl = Urls.createURL(null, baseHref);
							finalValue = "url(" + HtmlValues.quoteAndEscape(Urls.createURL(styleUrl, tentativeUri).toExternalForm()) + ")";
						} catch (java.net.MalformedURLException mfu) {
							finalValue = newValue;
						}
					}
				}
			}
			properties.setPropertyValueLCAlt(BACKGROUND_IMAGE, finalValue, important);
		}
	}

	private static class BackgroundSetter implements SubPropertySetter {
		void changeValue(AbstractCSS2Properties properties, String newValue, CSSStyleDeclaration declaration) {
			this.changeValue(properties, newValue, declaration, true);
		}

		public void changeValue(AbstractCSS2Properties properties, String newValue, CSSStyleDeclaration declaration, boolean important) {
			properties.setPropertyValueLCAlt(BACKGROUND, newValue, important);
			if (newValue != null && newValue.length() > 0) {
				String[] tokens = HtmlValues.splitCssValue(newValue);
				boolean hasXPosition = false;
				boolean hasYPosition = false;
				String color = null;
				String image = null;
				String backgroundRepeat = null;
				String position = null;
				for (String token : tokens) {
					if (ColorFactory.getInstance().isColor(token)) {
						color = token;
					} else if (HtmlValues.isUrl(token)) {
						image = token;
					} else if (HtmlValues.isBackgroundRepeat(token)) {
						backgroundRepeat = token;
					} else if (HtmlValues.isBackgroundPosition(token)) {
						if (hasXPosition && !hasYPosition) {
							position += (" " + token);
							hasYPosition = true;
						} else {
							hasXPosition = true;
							position = token;
						}
					}
				}
				if (color != null) {
					properties.setPropertyValueLCAlt(BACKGROUND_COLOR, color, important);
				}
				if (image != null) {
					properties.setPropertyValueProcessed(BACKGROUND_IMAGE, image, declaration, important);
				}
				if (backgroundRepeat != null) {
					properties.setPropertyValueLCAlt(BACKGROUND_REPEAT, backgroundRepeat, important);
				}
				if (position != null) {
					properties.setPropertyValueLCAlt(BACKGROUND_POSITION, position, important);
				}
			}
		}

	}

	private static class FontSetter implements SubPropertySetter {
		void changeValue(AbstractCSS2Properties properties, String newValue, CSSStyleDeclaration declaration) {
			this.changeValue(properties, newValue, declaration, true);
		}

		public void changeValue(AbstractCSS2Properties properties, String newValue, CSSStyleDeclaration declaration, boolean important) {
			properties.setPropertyValueLCAlt(FONT, newValue, important);
			if (newValue != null && newValue.length() > 0) {
				String fontSpecTL = newValue.toLowerCase();
				FontInfo fontInfo = (FontInfo) HtmlValues.SYSTEM_FONTS.get(fontSpecTL);
				if (fontInfo != null) {
					if (fontInfo.fontFamily != null) {
						properties.setPropertyValueLCAlt(FONT_FAMILY, fontInfo.fontFamily, important);
					}
					if (fontInfo.fontSize != null) {
						properties.setPropertyValueLCAlt(FONT_SIZE, fontInfo.fontSize, important);
					}
					if (fontInfo.fontStyle != null) {
						properties.setPropertyValueLCAlt(FONT_STYLE, fontInfo.fontStyle, important);
					}
					if (fontInfo.fontVariant != null) {
						properties.setPropertyValueLCAlt(FONT_VARIANT, fontInfo.fontVariant, important);
					}
					if (fontInfo.fontWeight != null) {
						properties.setPropertyValueLCAlt(FONT_WEIGHT, fontInfo.fontWeight, important);
					}
					return;
				}
				String[] tokens = HtmlValues.splitCssValue(fontSpecTL);
				String token = null;
				int length = tokens.length;
				int i;
				for (i = 0; i < length; i++) {
					token = tokens[i];
					if (HtmlValues.isFontStyle(token)) {
						properties.setPropertyValueLCAlt(FONT_STYLE, token, important);
						continue;
					}
					if (HtmlValues.isFontVariant(token)) {
						properties.setPropertyValueLCAlt(FONT_VARIANT, token, important);
						continue;
					}
					if (HtmlValues.isFontWeight(token)) {
						properties.setPropertyValueLCAlt(FONT_WEIGHT, token, important);
						continue;
					}
					// Otherwise exit loop
					break;
				}
				if (token != null) {
					int slashIdx = token.indexOf('/');
					String fontSizeText = slashIdx == -1 ? token : token.substring(0, slashIdx);
					properties.setPropertyValueLCAlt(FONT_SIZE, fontSizeText, important);
					String lineHeightText = slashIdx == -1 ? null : token.substring(slashIdx + 1);
					if (lineHeightText != null) {
						properties.setPropertyValueLCAlt(LINE_HEIGHT, lineHeightText, important);
					}
					if (++i < length) {
						StringBuilder fontFamilyBuff = new StringBuilder();
						do {
							token = tokens[i];
							fontFamilyBuff.append(token);
							fontFamilyBuff.append(' ');
						} while (++i < length);
						properties.setPropertyValueLCAlt(FONT_FAMILY, fontFamilyBuff.toString(), important);
					}
				}
			}
		}
	}

	private static class Property {
		public final String value;
		public final boolean important;

		public Property(final String value, final boolean important) {
			this.value = value;
			this.important = important;
		}
	}
}
