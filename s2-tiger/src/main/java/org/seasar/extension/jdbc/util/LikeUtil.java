/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
     * LIKE述語で使用される検索条件のワイルドカードが含まれていれば<code>true</code>を返します。
     * 
     * @param likeCondition
     *            LIKE述語で使用される検索条件の文字列
     * @return LIKE述語で使用される検索条件のワイルドカードが含まれていれば<code>true</code>
     */
    public static boolean containsWildcard(final String likeCondition) {
        final Matcher matcher = WILDCARD_PATTERN.matcher(likeCondition);
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
        final Matcher matcher = WILDCARD_REPLACEMENT_PATTERN
                .matcher(likeCondition);
        return matcher.replaceAll("\\$$0");
    }

}
