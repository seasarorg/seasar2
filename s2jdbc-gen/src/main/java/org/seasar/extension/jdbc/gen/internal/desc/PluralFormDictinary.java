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
package org.seasar.extension.jdbc.gen.internal.desc;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 単語を複数形に変換するクラスです。
 * 
 * @author taedium
 */
public class PluralFormDictinary {

    /** {@link Pattern}をキー、置換文字列を値とするマップ */
    protected LinkedHashMap<Pattern, String> patternMap = new LinkedHashMap<Pattern, String>();

    /**
     * インスタンスを構築します。
     */
    public PluralFormDictinary() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param dictinary
     *            正規表現の文字列をキー、置換文字列を値とするマップ
     */
    public PluralFormDictinary(LinkedHashMap<String, String> dictinary) {
        for (Map.Entry<String, String> entry : dictinary.entrySet()) {
            Pattern pattern = Pattern.compile(entry.getKey());
            patternMap.put(pattern, entry.getValue());
        }
    }

    /**
     * 単語の複数形を検索します。
     * 
     * @param word
     *            単語
     * @return 見つかれば単語の複数形、見つからなければ{@code null}
     */
    public String lookup(String word) {
        for (Map.Entry<Pattern, String> entry : patternMap.entrySet()) {
            Pattern pattern = entry.getKey();
            Matcher matcher = pattern.matcher(word);
            if (!matcher.matches()) {
                continue;
            }
            matcher.reset();
            StringBuffer buf = new StringBuffer();
            String replacement = entry.getValue();
            for (; matcher.find();) {
                matcher.appendReplacement(buf, replacement);
                if (matcher.hitEnd()) {
                    break;
                }
            }
            matcher.appendTail(buf);
            return buf.toString();
        }
        return null;
    }
}
