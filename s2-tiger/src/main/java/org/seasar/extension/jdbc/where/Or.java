/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.where;

import java.util.Arrays;
import java.util.List;

import org.seasar.extension.jdbc.Where;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * 入力された項を<code>or</code>でつなげていくような検索条件を組み立てるクラスです。
 * 
 * @author koichik
 */
public class Or implements Where {

    /** 項のリスト */
    protected List<Where> terms = CollectionsUtil.newArrayList();

    /**
     * インスタンスを構築します。
     */
    public Or() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param term
     *            項
     */
    public Or(final Where term) {
        terms.add(term);
    }

    /**
     * orで連結される項を追加します。
     * 
     * @param term
     *            項
     * @return このインスタンス自身
     */
    public Or or(final Where term) {
        terms.add(term);
        return this;
    }

    public String getCriteria() {
        final StringBuilder buf = new StringBuilder(200);
        for (final Where term : terms) {
            final String criteria = term.getCriteria();
            if (StringUtil.isEmpty(criteria)) {
                continue;
            }
            if (buf.length() == 0) {
                buf.append("(").append(criteria).append(")");
            } else {
                buf.append(" or (").append(criteria).append(")");
            }
        }
        return new String(buf);
    }

    public Object[] getParams() {
        final List<Object> params = CollectionsUtil.newArrayList();
        for (final Where term : terms) {
            params.addAll(Arrays.asList(term.getParams()));
        }
        return params.toArray(new Object[params.size()]);
    }

}
