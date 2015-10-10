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
package org.seasar.extension.jdbc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * LIKE 述語用のユーティリティです。
 * 
 * @author koichik
 */
public class LikeUtil {

    /** LIKE 述語で指定される検索条件中のワイルドカードのパターン */
    protected static final Pattern WILDCARD_PATTERN = Pattern
            .compile("[%_％＿]");

    /** LIKE 述語で指定される検索条件中のワイルドカードを置換するためのパターン */
    protected static final Pattern WILDCARD_REPLACEMENT_PATTERN = Pattern
            .compile("[$%_％＿]");

    /** LIKE述語で指定される検索条件中のワイルドカード文字をエスケープするための文字 */
    protected static final char WILDCARD_ESCAPE_CHAR = '$';

    /**
     * LIKE 述語で指定される検索条件中のワイルドカードのパターン
     * 
     * @see #WILDCARD_PATTERN
     * */
    protected static Pattern wildcardPattern;

    /**
     * LIKE 述語で指定される検索条件中のワイルドカードを置換するためのパターン
     * 
     * @see #WILDCARD_REPLACEMENT_PATTERN
     * */
    protected static Pattern wildcardReplacementPattern;

    /**
     * LIKE 述語で指定される検索条件中のワイルドカードのパターンを返します。
     * 
     * @return LIKE 述語で指定される検索条件中のワイルドカードのパターン
     */
    public static Pattern getWildcardPattern() {
        if (wildcardPattern == null) {
            return WILDCARD_PATTERN;
        }
        return wildcardPattern;
    }

    /**
     * LIKE 述語で指定される検索条件中のワイルドカードのパターンを設定します。
     * 
     * @param pattern
     *            LIKE 述語で指定される検索条件中のワイルドカードのパターン
     */
    public static void setWildcardPattern(final Pattern pattern) {
        wildcardPattern = pattern;
    }

    /**
     * LIKE 述語で指定される検索条件中のワイルドカードのパターンを文字列で設定します。
     * 
     * @param pattern
     *            LIKE 述語で指定される検索条件中のワイルドカードのパターン文字列
     */
    public static void setWildcardPatternAsString(final String pattern) {
        setWildcardPattern(Pattern.compile(pattern));
    }

    /**
     * LIKE 述語で指定される検索条件中のワイルドカードを置換するためのパターンを返します。
     * 
     * @return LIKE 述語で指定される検索条件中のワイルドカードを置換するためのパターン
     */
    public static Pattern getWildcardReplacementPattern() {
        if (wildcardReplacementPattern == null) {
            return WILDCARD_REPLACEMENT_PATTERN;
        }
        return wildcardReplacementPattern;
    }

    /**
     * LIKE 述語で指定される検索条件中のワイルドカードを置換するためのパターンを設定します。
     * 
     * @param pattern
     *            LIKE 述語で指定される検索条件中のワイルドカードを置換するためのパターン
     */
    public static void setWildcardReplacementPattern(final Pattern pattern) {
        wildcardReplacementPattern = pattern;
    }

    /**
     * LIKE 述語で指定される検索条件中のワイルドカードを置換するためのパターンを文字列で設定します。
     * 
     * @param pattern
     *            LIKE 述語で指定される検索条件中のワイルドカードを置換するためのパターン文字列
     */
    public static void setWildcardReplacementPatternAsString(
            final String pattern) {
        setWildcardReplacementPattern(Pattern.compile(pattern));
    }

    /**
     * LIKE述語で使用される検索条件のワイルドカードが含まれていれば<code>true</code>を返します。
     * 
     * @param likeCondition
     *            LIKE述語で使用される検索条件の文字列
     * @return LIKE述語で使用される検索条件のワイルドカードが含まれていれば<code>true</code>
     */
    public static boolean containsWildcard(final String likeCondition) {
        final Matcher matcher = getWildcardPattern().matcher(likeCondition);
        return matcher.find();
    }

    /**
     * LIKE述語で使用される検索条件のワイルドカードを<code>'$'</code>でエスケープします．
     * 
     * @param likeCondition
     *            LIKE述語で使用される検索条件の文字列
     * @return ワイルドカードを<code>'$'</code>でエスケープした文字列
     */
    public static String escapeWildcard(final String likeCondition) {
        final Matcher matcher = getWildcardReplacementPattern()
                .matcher(likeCondition);
        return matcher.replaceAll("\\$$0");
    }

}
