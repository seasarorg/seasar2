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
package org.seasar.extension.jdbc.where;

import java.util.Collection;

import org.seasar.extension.jdbc.Where;

/**
 * ANDやORなど、複数の{@link Where 検索条件}を演算子で連結する検索条件です。
 * 
 * @author koichik
 */
public class CompositeWhere extends ComposableWhere {

    /** ANDやORといった演算子です。 */
    protected String operator;

    /**
     * インスタンスを構築します。
     * 
     * @param operator
     *            ANDやORといった演算子
     * @param children
     *            子供となる{@link Where 検索条件}
     */
    public CompositeWhere(final String operator, final Where... children) {
        super(children);
        this.operator = operator;
    }

    /**
     * インスタンスを構築します。
     * 
     * @param operator
     *            ANDやORといった演算子
     * @param children
     *            子供となる{@link Where 検索条件}
     */
    public CompositeWhere(final String operator,
            final Collection<Where> children) {
        super(children);
        this.operator = operator;
    }

    @Override
    protected void visit(final ComposableWhereContext context) {
        if (children.isEmpty()) {
            return;
        }
        context.append('(');
        int len = context.getCriteriaLength();
        int cutBack = 1;
        for (final Where child : children) {
            final int newLen = context.append(child).getCriteriaLength();
            if (len == newLen) {
                continue;
            }
            len = context.append(") ").append(operator).append(" (")
                    .getCriteriaLength();
            cutBack = operator.length() + 3;
        }
        context.cutBack(cutBack);
    }

}
