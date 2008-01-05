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
package org.seasar.extension.dxo.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.seasar.framework.util.OgnlUtil;

/**
 * 変換ルールで指定されたOGNL式の評価可能な表現です。
 * 
 * @author koichik
 */
public class OgnlExpression implements Expression {

    /** OGNL式による{@link Map}リテラルの接頭辞です。 */
    protected static final String MAP_PREFIX = "#@java.util.LinkedHashMap@{";

    /** OGNL式による{@link Map}リテラルの接尾辞です。 */
    protected static final String MAP_SUFFIX = "}";

    /** 解析済みの式 */
    protected Object parsedExpression;

    /**
     * インスタンスを構築します。
     * 
     * @param source
     *            式のソース文字列
     */
    public OgnlExpression(final String source) {
        parsedExpression = OgnlUtil.parseExpression(MAP_PREFIX + source
                + MAP_SUFFIX);
    }

    public Map evaluate(final Object source) {
        final Map map = (Map) OgnlUtil.getValue(parsedExpression, source);
        final Map result = new LinkedHashMap(map.size());
        for (final Iterator it = map.entrySet().iterator(); it.hasNext();) {
            final Entry entry = (Entry) it.next();
            final Object key = entry.getKey();
            final Object value = entry.getValue();
            if (key instanceof Character) {
                result.put(new String(new char[] { ((Character) key)
                        .charValue() }), value);
            } else {
                result.put(key, value);
            }
        }
        return result;
    }

}
