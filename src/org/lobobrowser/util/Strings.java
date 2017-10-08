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
package org.lobobrowser.util;

import java.util.ArrayList;

public class Strings {
	public static final String[] EMPTY_ARRAY = new String[0];

	private Strings() {
	}

	public static boolean isBlank(String text) {
		return text == null || "".equals(text);
	}

	static int countChars(String text, char ch) {
		int len = text.length();
		int count = 0;
		for (int i = 0; i < len; i++) {
			if (ch == text.charAt(i)) {
				count++;
			}
		}
		return count;
	}

	static String unquote(String text) {
		if (text.startsWith("\"") && text.endsWith("\"")) {
			// substring works on indices
			return text.substring(1, text.length() - 1);
		}
		return text;
	}

	public static String[] split(String phrase) {
		int length = phrase.length();
		ArrayList<String> wordList = new ArrayList<>();
		StringBuffer word = null;
		for (int i = 0; i < length; i++) {
			char ch = phrase.charAt(i);
			switch (ch) {
				case ' ':
				case '\t':
				case '\r':
				case '\n':
					if (word != null) {
						wordList.add(word.toString());
						word = null;
					}
					break;
				default:
					if (word == null) {
						word = new StringBuffer();
					}
					word.append(ch);
			}
		}
		if (word != null) {
			wordList.add(word.toString());
		}
		return wordList.toArray(EMPTY_ARRAY);
	}

	public static String truncate(String text, int maxLength) {
		if (text == null) {
			return null;
		}
		if (text.length() <= maxLength) {
			return text;
		}
		return text.substring(0, Math.max(maxLength - 3, 0)) + "...";
	}

	public static String strictHtmlEncode(String rawText, boolean quotes) {
		StringBuilder output = new StringBuilder();
		int length = rawText.length();
		for (int i = 0; i < length; i++) {
			char ch = rawText.charAt(i);
			switch (ch) {
				case '&':
					output.append("&amp;");
					break;
				case '"':
					if (quotes) {
						output.append("&quot;");
					} else {
						output.append(ch);
					}
					break;
				case '<':
					output.append("&lt;");
					break;
				case '>':
					output.append("&gt;");
					break;
				default:
					output.append(ch);
			}
		}
		return output.toString();
	}

	public static String trimForAlphaNumDash(String rawText) {
		int length = rawText.length();
		for (int i = 0; i < length; i++) {
			char ch = rawText.charAt(i);
			if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9') || ch == '-') {
				continue;
			}
			return rawText.substring(0, i);
		}
		return rawText;
	}

	public static String getCRLFString(String original) {
		if (original == null) {
			return null;
		}
		int length = original.length();
		StringBuilder buffer = new StringBuilder();
		boolean lastSlashR = false;
		for (int i = 0; i < length; i++) {
			char ch = original.charAt(i);
			switch (ch) {
				case '\r':
					lastSlashR = true;
					break;
				case '\n':
					lastSlashR = false;
					buffer.append("\r\n");
					break;
				default:
					if (lastSlashR) {
						lastSlashR = false;
						buffer.append("\r\n");
					}
					buffer.append(ch);
					break;
			}
		}
		return buffer.toString();
	}
}
