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

import java.util.Arrays;
import java.util.LinkedList;

import org.seasar.extension.jdbc.Where;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * 入力された項目をANDやORでつなげていくような検索条件を組み立てるクラスです。
 * 
 * @author koichik
 */
public class ComplexWhere extends AbstractWhere<ComplexWhere> implements Where {

    /** クライテリアを保持する文字列バッファのリスト */
    protected LinkedList<StringBuilder> criteriaList = CollectionsUtil
            .newLinkedList();

    /**
     * インスタンスを構築します。
     */
    public ComplexWhere() {
        criteriaList.add(criteriaSb);
    }

    /**
     * これまでに追加された条件とこれから追加される条件をORで結合します。
     * 
     * @return このインスタンス自身
     */
    public ComplexWhere or() {
        if (criteriaSb.length() > 0) {
            criteriaSb = new StringBuilder(100);
            criteriaList.addLast(criteriaSb);
        }
        return this;
    }

    /**
     * これまでに追加された条件と、引数で渡された条件全体をANDで結合します。
     * 
     * @param factor
     *            ANDで結合される条件
     * @return このインスタンス自身
     */
    public ComplexWhere and(final Where factor) {
        final String factorCriteria = factor.getCriteria();
        if (StringUtil.isEmpty(factorCriteria)) {
            return this;
        }
        if (criteriaSb.length() > 0) {
            criteriaSb.append(" and");
        }
        criteriaSb.append(" (").append(factorCriteria).append(")");
        paramList.addAll(Arrays.asList(factor.getParams()));
        propertyNameList.addAll(Arrays.asList(factor.getPropertyNames()));
        return this;
    }

    @Override
    public String getCriteria() {
        if (criteriaSb.length() == 0) {
            criteriaList.removeLast();
        }
        if (criteriaList.isEmpty()) {
            return "";
        }
        if (criteriaList.size() == 1) {
            return new String(criteriaList.getFirst());
        }

        int size = 0;
        for (final StringBuilder buf : criteriaList) {
            size += buf.length();
        }
        final StringBuilder buf = new StringBuilder(size + 50);
        for (final StringBuilder criteria : criteriaList) {
            buf.append("(").append(criteria).append(") or ");
        }
        buf.setLength(buf.length() - 4);
        return new String(buf);
    }

}
