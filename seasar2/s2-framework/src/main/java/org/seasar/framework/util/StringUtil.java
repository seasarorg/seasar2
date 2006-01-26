/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.framework.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author higa
 *  
 */
public final class StringUtil {

	public static final String[] EMPTY_STRINGS = new String[0];

	/**
	 *  
	 */
	private StringUtil() {
	}
    
	public static final boolean isEmpty(String text) {
		return text == null || text.length() == 0;
	}

	public static final String replace(String text, String fromText,
			String toText) {

		if (text == null || fromText == null || toText == null) {
			return null;
		}
		StringBuffer buf = new StringBuffer(100);
		int pos = 0;
		int pos2 = 0;
		while (true) {
			pos = text.indexOf(fromText, pos2);
			if (pos == 0) {
				buf.append(toText);
				pos2 = fromText.length();
			} else if (pos > 0) {
				buf.append(text.substring(pos2, pos));
				buf.append(toText);
				pos2 = pos + fromText.length();
			} else {
				buf.append(text.substring(pos2));
				break;
			}
		}
		return buf.toString();
	}

	public static String[] split(String str, String delim) {
		if (str == null) {
			return EMPTY_STRINGS;
		}
		List list = new ArrayList();
		StringTokenizer st = new StringTokenizer(str, delim);
		while (st.hasMoreElements()) {
			list.add(st.nextElement());
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	public static final String ltrim(final String text) {
		return ltrim(text, null);
	}

	public static final String ltrim(final String text, String trimText) {
		if (text == null) {
			return null;
		}
		if (trimText == null) {
			trimText = " ";
		}
		int pos = 0;
		for (; pos < text.length(); pos++) {
			if (trimText.indexOf(text.charAt(pos)) < 0) {
				break;
			}
		}
		return text.substring(pos);
	}

	public static final String rtrim(final String text) {
		return rtrim(text, null);
	}

	public static final String rtrim(final String text, String trimText) {
		if (text == null) {
			return null;
		}
		if (trimText == null) {
			trimText = " ";
		}
		int pos = text.length() - 1;
		for (; pos >= 0; pos--) {
			if (trimText.indexOf(text.charAt(pos)) < 0) {
				break;
			}
		}
		return text.substring(0, pos + 1);
	}

	public static String decapitalize(String name) {
		if (isEmpty(name)) {
			return name;
		}
		char chars[] = name.toCharArray();
		chars[0] = Character.toLowerCase(chars[0]);
		return new String(chars);
	}

    public static String capitalize(String name) {
        if (isEmpty(name)) {
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

	public static boolean startsWith(String text, String fragment) {
		if (text == null || fragment == null) {
			return false;
		}
		return text.length() > fragment.length()
				&& text.substring(0, fragment.length()).equalsIgnoreCase(
						fragment);
	}
}