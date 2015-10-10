/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
 * {@link String}用のユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public class StringUtil {

    /**
     * 空の文字列の配列です。
     */
    public static final String[] EMPTY_STRINGS = new String[0];

    /**
     * 
     */
    protected StringUtil() {
    }

    /**
     * 文字列が<code>null</code>または空文字列なら<code>true</code>を返します。
     * 
     * @param text
     *            文字列
     * @return 文字列が<code>null</code>または空文字列なら<code>true</code>
     */
    public static final boolean isEmpty(final String text) {
        return text == null || text.length() == 0;
    }

    /**
     * 文字列が<code>null</code>でも空文字列でもなければ<code>true</code>を返します。
     * 
     * @param text
     *            文字列
     * @return 文字列が<code>null</code>でも空文字列でもなければ<code>true</code>
     * @since 2.4.33
     */
    public static final boolean isNotEmpty(final String text) {
        return !isEmpty(text);
    }

    /**
     * 文字列を置き換えます。
     * 
     * @param text
     *            テキスト
     * @param fromText
     *            置き換え対象のテキスト
     * @param toText
     *            置き換えるテキスト
     * @return 結果
     */
    public static final String replace(final String text,
            final String fromText, final String toText) {

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

    /**
     * 文字列を分割します。
     * 
     * @param str
     *            文字列
     * @param delim
     *            分割するためのデリミタ
     * @return 分割された文字列の配列
     */
    public static String[] split(final String str, final String delim) {
        if (isEmpty(str)) {
            return EMPTY_STRINGS;
        }
        List list = new ArrayList();
        StringTokenizer st = new StringTokenizer(str, delim);
        while (st.hasMoreElements()) {
            list.add(st.nextElement());
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    /**
     * 左側の空白を削ります。
     * 
     * @param text
     *            テキスト
     * @return 結果の文字列
     */
    public static final String ltrim(final String text) {
        return ltrim(text, null);
    }

    /**
     * 左側の指定した文字列を削ります。
     * 
     * @param text
     *            テキスト
     * @param trimText
     *            削るテキスト
     * @return 結果の文字列
     */
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

    /**
     * 右側の空白を削ります。
     * 
     * @param text
     *            テキスト
     * @return 結果の文字列
     */
    public static final String rtrim(final String text) {
        return rtrim(text, null);
    }

    /**
     * 右側の指定した文字列を削ります。
     * 
     * @param text
     *            テキスト
     * @param trimText
     *            削る文字列
     * @return 結果の文字列
     */
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

    /**
     * サフィックスを削ります。
     * 
     * @param text
     *            テキスト
     * @param suffix
     *            サフィックス
     * @return 結果の文字列
     */
    public static final String trimSuffix(final String text, final String suffix) {
        if (text == null) {
            return null;
        }
        if (suffix == null) {
            return text;
        }
        if (text.endsWith(suffix)) {
            return text.substring(0, text.length() - suffix.length());
        }
        return text;
    }

    /**
     * プレフィックスを削ります。
     * 
     * @param text
     *            テキスト
     * @param prefix
     *            プレフィックス
     * @return 結果の文字列
     */
    public static final String trimPrefix(final String text, final String prefix) {
        if (text == null) {
            return null;
        }
        if (prefix == null) {
            return text;
        }
        if (text.startsWith(prefix)) {
            return text.substring(prefix.length());
        }
        return text;
    }

    /**
     * JavaBeansの仕様にしたがってデキャピタライズを行ないます。大文字が2つ以上続く場合は、小文字にならないので注意してください。
     * 
     * @param name
     *            名前
     * @return 結果の文字列
     */
    public static String decapitalize(final String name) {
        if (isEmpty(name)) {
            return name;
        }
        char chars[] = name.toCharArray();
        if (chars.length >= 2 && Character.isUpperCase(chars[0])
                && Character.isUpperCase(chars[1])) {
            return name;
        }
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    /**
     * JavaBeansの仕様にしたがってキャピタライズを行ないます。大文字が2つ以上続く場合は、大文字にならないので注意してください。
     * 
     * @param name
     *            名前
     * @return 結果の文字列
     */
    public static String capitalize(final String name) {
        if (isEmpty(name)) {
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    /**
     * ケースインセンシティブで特定の文字列で開始されているかどうかを返します。
     * 
     * @param text
     *            テキスト
     * @param fragment
     *            特定の文字列
     * @return ケースインセンシティブで特定の文字列で開始されているかどうか
     * @see #startsWithIgnoreCase(String, String)
     * @deprecated
     */
    public static boolean startsWith(final String text, final String fragment) {
        return startsWithIgnoreCase(text, fragment);
    }

    /**
     * ブランクかどうか返します。
     * 
     * @param str
     *            文字列
     * @return ブランクかどうか
     */
    public static boolean isBlank(final String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * ブランクではないかどうか返します。
     * 
     * @param str
     *            文字列
     * @return ブランクではないかどうか
     * @see #isBlank(String)
     */
    public static boolean isNotBlank(final String str) {
        return !isBlank(str);
    }

    /**
     * charを含んでいるかどうか返します。
     * 
     * @param str
     *            文字列
     * @param ch
     *            char
     * @return charを含んでいるかどうか
     */
    public static boolean contains(final String str, final char ch) {
        if (isEmpty(str)) {
            return false;
        }
        return str.indexOf(ch) >= 0;
    }

    /**
     * 文字列を含んでいるかどうか返します。
     * 
     * @param s1
     *            文字列
     * @param s2
     *            比較する対象となる文字列
     * @return 文字列を含んでいるかどうか
     */
    public static boolean contains(final String s1, final String s2) {
        if (isEmpty(s1)) {
            return false;
        }
        return s1.indexOf(s2) >= 0;
    }

    /**
     * 文字列同士が等しいかどうか返します。どちらもnullの場合は、<code>true</code>を返します。
     * 
     * @param target1
     *            文字列1
     * @param target2
     *            文字列2
     * @return 文字列同士が等しいかどうか
     */
    public static boolean equals(final String target1, final String target2) {
        return (target1 == null) ? (target2 == null) : target1.equals(target2);
    }

    /**
     * ケースインセンシティブで文字列同士が等しいかどうか返します。どちらもnullの場合は、<code>true</code>を返します。
     * 
     * @param target1
     *            文字列1
     * @param target2
     *            文字列2
     * @return ケースインセンシティブで文字列同士が等しいか
     */
    public static boolean equalsIgnoreCase(final String target1,
            final String target2) {
        return (target1 == null) ? (target2 == null) : target1
                .equalsIgnoreCase(target2);
    }

    /**
     * ケースインセンシティブで特定の文字で終わっているのかどうかを返します。
     * 
     * @param target1
     *            テキスト
     * @param target2
     *            比較する文字列
     * @return ケースインセンシティブで特定の文字で終わっているのかどうか
     */
    public static boolean endsWithIgnoreCase(final String target1,
            final String target2) {
        if (target1 == null || target2 == null) {
            return false;
        }
        int length1 = target1.length();
        int length2 = target2.length();
        if (length1 < length2) {
            return false;
        }
        String s1 = target1.substring(length1 - length2);
        return s1.equalsIgnoreCase(target2);
    }

    /**
     * ケースインセンシティブで特定の文字ではじまっているのかどうかを返します。
     * 
     * @param target1
     *            テキスト
     * @param target2
     *            比較する文字列
     * @return ケースインセンシティブで特定の文字ではじまっているのかどうか
     */
    public static boolean startsWithIgnoreCase(final String target1,
            final String target2) {
        if (target1 == null || target2 == null) {
            return false;
        }
        int length1 = target1.length();
        int length2 = target2.length();
        if (length1 < length2) {
            return false;
        }
        String s1 = target1.substring(0, target2.length());
        return s1.equalsIgnoreCase(target2);
    }

    /**
     * 文字列の最後から指定した文字列で始まっている部分より手前を返します。
     * 
     * @param str
     *            文字列
     * @param separator
     *            セパレータ
     * @return 結果の文字列
     */
    public static String substringFromLast(final String str,
            final String separator) {
        if (isEmpty(str) || isEmpty(separator)) {
            return str;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * 文字列の最後から指定した文字列で始まっている部分より後ろを返します。
     * 
     * @param str
     *            文字列
     * @param separator
     *            セパレータ
     * @return 結果の文字列
     */
    public static String substringToLast(final String str,
            final String separator) {
        if (isEmpty(str) || isEmpty(separator)) {
            return str;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(pos + 1, str.length());
    }

    /**
     * 16進数の文字列に変換します。
     * 
     * @param bytes
     *            バイトの配列
     * @return 16進数の文字列
     */
    public static String toHex(final byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; ++i) {
            appendHex(sb, bytes[i]);
        }
        return sb.toString();
    }

    /**
     * 16進数の文字列に変換します。
     * 
     * @param i
     *            int
     * @return 16進数の文字列
     */
    public static String toHex(final int i) {
        StringBuffer buf = new StringBuffer();
        appendHex(buf, i);
        return buf.toString();
    }

    /**
     * 文字列に、数値を16進数に変換した文字列を追加します。
     * 
     * @param buf
     *            追加先の文字列
     * @param i
     *            数値
     */
    public static void appendHex(final StringBuffer buf, final byte i) {
        buf.append(Character.forDigit((i & 0xf0) >> 4, 16));
        buf.append(Character.forDigit((i & 0x0f), 16));
    }

    /**
     * 文字列に、数値を16進数に変換した文字列を追加します。
     * 
     * @param buf
     *            追加先の文字列
     * @param i
     *            数値
     */
    public static void appendHex(final StringBuffer buf, final int i) {
        buf.append(Integer.toHexString((i >> 24) & 0xff));
        buf.append(Integer.toHexString((i >> 16) & 0xff));
        buf.append(Integer.toHexString((i >> 8) & 0xff));
        buf.append(Integer.toHexString(i & 0xff));
    }

    /**
     * _記法をキャメル記法に変換します。
     * 
     * @param s
     *            テキスト
     * @return 結果の文字列
     */
    public static String camelize(String s) {
        if (s == null) {
            return null;
        }
        s = s.toLowerCase();
        String[] array = StringUtil.split(s, "_");
        if (array.length == 1) {
            return StringUtil.capitalize(s);
        }
        StringBuffer buf = new StringBuffer(40);
        for (int i = 0; i < array.length; ++i) {
            buf.append(StringUtil.capitalize(array[i]));
        }
        return buf.toString();
    }

    /**
     * キャメル記法を_記法に変換します。
     * 
     * @param s
     *            テキスト
     * @return 結果の文字列
     */
    public static String decamelize(final String s) {
        if (s == null) {
            return null;
        }
        if (s.length() == 1) {
            return s.toUpperCase();
        }
        StringBuffer buf = new StringBuffer(40);
        int pos = 0;
        for (int i = 1; i < s.length(); ++i) {
            if (Character.isUpperCase(s.charAt(i))) {
                if (buf.length() != 0) {
                    buf.append('_');
                }
                buf.append(s.substring(pos, i).toUpperCase());
                pos = i;
            }
        }
        if (buf.length() != 0) {
            buf.append('_');
        }
        buf.append(s.substring(pos, s.length()).toUpperCase());
        return buf.toString();
    }

    /**
     * 文字列が数値のみで構成されているかどうかを返します。
     * 
     * @param s
     *            文字列
     * @return 数値のみで構成されている場合、<code>true</code>
     */
    public static boolean isNumber(final String s) {
        if (s == null || s.length() == 0) {
            return false;
        }

        int size = s.length();
        for (int i = 0; i < size; i++) {
            char chr = s.charAt(i);
            if (chr < '0' || '9' < chr) {
                return false;
            }
        }

        return true;
    }
}