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
 * Created on Apr 17, 2005
 */
package org.lobobrowser.util.gui;

import org.lobobrowser.util.Objects;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.*;

/**
 * @author J. H. S.
 */
public class FontFactory {
	private static final FontFactory instance = new FontFactory();
	private final Set<String> fontFamilies = new HashSet<>(40);
	private final Map<FontKey, Font> fontMap = new HashMap<>(50);

	/**
	 *
	 */
	private FontFactory() {
		String[] ffns = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		Set<String> fontFamilies = this.fontFamilies;
		synchronized (this) {
			for (String ffn : ffns) {
				fontFamilies.add(ffn.toLowerCase());
			}
		}
	}

	public static FontFactory getInstance() {
		return instance;
	}

	private final Map<String, Font> registeredFonts = new HashMap<>(0);

	public Font getFont(String fontFamily, String fontStyle, String fontVariant, String fontWeight, float fontSize, Set locales, Integer superscript) {
		FontKey key = new FontKey(fontFamily, fontStyle, fontVariant, fontWeight, fontSize, locales, superscript);
		synchronized (this) {
			Font font = this.fontMap.get(key);
			if (font == null) {
				font = this.createFont(key);
				this.fontMap.put(key, font);
			}
			return font;
		}
	}

	private Font createFont(FontKey key) {
		Font font = createFont_Impl(key);
		return superscriptFont(font, key.superscript);
	}

	public static Font superscriptFont(Font baseFont, Integer newSuperscript) {
		if (newSuperscript == null) {
			return baseFont;
		}
		Integer fontSuperScript = (Integer) baseFont.getAttributes().get(TextAttribute.SUPERSCRIPT);
		if (fontSuperScript == null) {
			fontSuperScript = 0;
		}
		if (fontSuperScript.equals(newSuperscript)) {
			return baseFont;
		} else {
			Map<TextAttribute, Integer> additionalAttributes = new HashMap<>();
			additionalAttributes.put(TextAttribute.SUPERSCRIPT, newSuperscript);
			return baseFont.deriveFont(additionalAttributes);
		}
	}

	private Font createFont_Impl(FontKey key) {
		String fontNames = key.fontFamily;
		String matchingFace = null;
		Set fontFamilies = this.fontFamilies;
		Map<String, Font> registeredFonts = this.registeredFonts;
		Font baseFont = null;
		if (fontNames != null) {
			StringTokenizer tok = new StringTokenizer(fontNames, ",");
			while (tok.hasMoreTokens()) {
				String face = tok.nextToken().trim();
				String faceTL = face.toLowerCase();
				if (registeredFonts.containsKey(faceTL)) {
					baseFont = registeredFonts.get(faceTL);
					break;
				} else if (fontFamilies.contains(faceTL)) {
					matchingFace = faceTL;
					break;
				}
			}
		}
		int fontStyle = Font.PLAIN;
		if ("italic".equalsIgnoreCase(key.fontStyle)) {
			fontStyle |= Font.ITALIC;
		}
		if ("bold".equalsIgnoreCase(key.fontWeight) || "bolder".equalsIgnoreCase(key.fontWeight)) {
			fontStyle |= Font.BOLD;
		}
		if (baseFont != null) {
			return baseFont.deriveFont(fontStyle, key.fontSize);
		} else if (matchingFace != null) {
			Font font = createFont(matchingFace, fontStyle, Math.round(key.fontSize));
			Set locales = key.locales;
			if (locales == null) {
				Locale locale = Locale.getDefault();
				if (font.canDisplayUpTo(locale.getDisplayLanguage(locale)) == -1) {
					return font;
				}
			} else {
				Iterator i = locales.iterator();
				boolean allMatch = true;
				while (i.hasNext()) {
					Locale locale = (Locale) i.next();
					if (font.canDisplayUpTo(locale.getDisplayLanguage(locale)) != -1) {
						allMatch = false;
						break;
					}
				}
				if (allMatch) {
					return font;
				}
			}
			// Otherwise, fall through.
		}
		// Last resort:
		String defaultFontName = "SansSerif";
		return createFont(defaultFontName, fontStyle, Math.round(key.fontSize));
	}

	private Font createFont(String name, int style, int size) {
		// Proprietary Sun API. Maybe shouldn't use it. Works well for Chinese.
		return new Font(name, style, size);
	}

	private static class FontKey {
		final String fontFamily;
		final String fontStyle;
		final String fontVariant;
		final String fontWeight;
		public final float fontSize;
		final Set locales;
		final Integer superscript;


		/**
		 * @param fontFamily font family
		 * @param fontStyle font style
		 * @param fontVariant font variant
		 * @param fontWeight font weight
		 * @param fontSize font size
		 */
		FontKey(final String fontFamily, final String fontStyle,
		        final String fontVariant, final String fontWeight,
		        final float fontSize, final Set locales, final Integer superscript) {
			this.fontFamily = fontFamily == null ? null : fontFamily.intern();
			this.fontStyle = fontStyle == null ? null : fontStyle.intern();
			this.fontVariant = fontVariant == null ? null : fontVariant.intern();
			this.fontWeight = fontWeight == null ? null : fontWeight.intern();
			this.fontSize = fontSize;
			this.locales = locales;
			this.superscript = superscript;
		}

		public boolean equals(Object other) {
			if (other == this) {
				return true;
			}
			if (other instanceof FontKey) {
				FontKey ors = (FontKey) other;
				return this.fontSize == ors.fontSize &&
						this.fontFamily == ors.fontFamily &&
						this.fontStyle == ors.fontStyle &&
						this.fontWeight == ors.fontWeight &&
						this.fontVariant == ors.fontVariant &&
						this.superscript == ors.superscript &&
						Objects.equals(this.locales, ors.locales);
			} else return false;
		}

		private int cachedHash = -1;

		public int hashCode() {
			int ch = this.cachedHash;
			if (ch != -1) {
				// Object is immutable - caching is ok.
				return ch;
			}
			String ff = this.fontFamily;
			if (ff == null) {
				ff = "";
			}
			String fw = this.fontWeight;
			if (fw == null) {
				fw = "";
			}
			String fs = this.fontStyle;
			if (fs == null) {
				fs = "";
			}
			Integer ss = this.superscript;
			ch = ff.hashCode() ^
					fw.hashCode() ^
					fs.hashCode() ^
					(int) this.fontSize ^
					(ss == null ? 0 : ss);
			this.cachedHash = ch;
			return ch;
		}

		public String toString() {
			return "FontKey[family=" + this.fontFamily + ",size=" + this.fontSize + ",style=" + this.fontStyle + ",weight=" + this.fontWeight + ",variant=" + this.fontVariant + ",superscript=" + this.superscript + "]";
		}
	}
}