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

import java.util.List;

import org.seasar.extension.jdbc.ConditionType;
import org.seasar.extension.jdbc.Where;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * 複数の構成要素からなる条件を組み立てるためのコンテキストです。
 * 
 * @author koichik
 */
public class ComposableWhereContext implements Where {

    /** 現在のクライテリアを保持する文字列バッファ */
    protected StringBuilder criteriaSb = new StringBuilder(256);

    /** バインド変数のリスト */
    protected List<Object> paramList = CollectionsUtil.newArrayList();

    /** バインド変数に対応するプロパティ名のリスト */
    protected List<String> propertyNameList = CollectionsUtil.newArrayList();

    public String getCriteria() {
        return new String(criteriaSb);
    }

    public Object[] getParams() {
        return paramList.toArray();
    }

    public String[] getPropertyNames() {
        return propertyNameList.toArray(new String[propertyNameList.size()]);
    }

    /**
     * クライテリア文字列の現在の長さを返します。
     * 
     * @return クライテリア文字列の現在の長さ
     */
    public int getCriteriaLength() {
        return criteriaSb.length();
    }

    /**
     * 条件を追加します。
     * 
     * @param where
     *            条件
     * @return このインスタンス自身
     */
    public ComposableWhereContext append(final Where where) {
        if (where instanceof ComposableWhere) {
            ComposableWhere.class.cast(where).visit(this);
            return this;
        }

        final String criteria = where.getCriteria();
        if (StringUtil.isEmpty(criteria)) {
            return this;
        }
        append(criteria);
        for (final Object param : where.getParams()) {
            addParam(param);
        }
        for (final String propertyName : where.getPropertyNames()) {
            addPropertyName(propertyName);
        }
        return this;
    }

    /**
     * クライテリア文字列に値を追加します。
     * 
     * @param b
     *            値
     * @return このインスタンス自身
     */
    public ComposableWhereContext append(final boolean b) {
        criteriaSb.append(b);
        return this;
    }

    /**
     * クライテリア文字列に値を追加します。
     * 
     * @param b
     *            値
     * @return このインスタンス自身
     */
    public ComposableWhereContext append(final byte b) {
        criteriaSb.append(b);
        return this;
    }

    /**
     * クライテリア文字列に値を追加します。
     * 
     * @param s
     *            値
     * @return このインスタンス自身
     */
    public ComposableWhereContext append(final short s) {
        criteriaSb.append(s);
        return this;
    }

    /**
     * クライテリア文字列に値を追加します。
     * 
     * @param i
     *            値
     * @return このインスタンス自身
     */
    public ComposableWhereContext append(final int i) {
        criteriaSb.append(i);
        return this;
    }

    /**
     * クライテリア文字列に値を追加します。
     * 
     * @param l
     *            値
     * @return このインスタンス自身
     */
    public ComposableWhereContext append(final long l) {
        criteriaSb.append(l);
        return this;
    }

    /**
     * クライテリア文字列に値を追加します。
     * 
     * @param f
     *            値
     * @return このインスタンス自身
     */
    public ComposableWhereContext append(final float f) {
        criteriaSb.append(f);
        return this;
    }

    /**
     * クライテリア文字列に値を追加します。
     * 
     * @param d
     *            値
     * @return このインスタンス自身
     */
    public ComposableWhereContext append(final double d) {
        criteriaSb.append(d);
        return this;
    }

    /**
     * クライテリア文字列に値を追加します。
     * 
     * @param ch
     *            値
     * @return このインスタンス自身
     */
    public ComposableWhereContext append(final char ch) {
        criteriaSb.append(ch);
        return this;
    }

    /**
     * クライテリア文字列に文字列を追加します。
     * 
     * @param s
     *            文字列
     * @return このインスタンス自身
     */
    public ComposableWhereContext append(final String s) {
        criteriaSb.append(s);
        return this;
    }

    /**
     * クライテリア文字列に列挙を追加します。
     * 
     * @param e
     *            列挙
     * @return このインスタンス自身
     */
    public ComposableWhereContext append(final Enum<?> e) {
        criteriaSb.append(e.getClass().getName()).append(".").append(e.name());
        return this;
    }

    /**
     * クライテリア文字列にオブジェクトを追加します。
     * 
     * @param o
     *            オブジェクト
     * @return このインスタンス自身
     */
    public ComposableWhereContext append(final Object o) {
        criteriaSb.append(o);
        return this;
    }

    /**
     * クライテリア文字列の長さを切り詰めます。
     * 
     * @param number
     *            切り詰める長さ
     * @return このインスタンス自身
     */
    public ComposableWhereContext cutBack(final int number) {
        criteriaSb.setLength(criteriaSb.length() - number);
        return this;
    }

    /**
     * 条件にパラメータを追加します。
     * 
     * @param param
     *            パラメータ
     */
    public void addParam(final Object param) {
        paramList.add(param);
    }

    /**
     * 条件にパラメータを追加します。
     * 
     * @param conditionType
     *            条件タイプ
     * @param param
     *            パラメータ
     * @return 追加されたパラメータの数
     */
    public int addParam(final ConditionType conditionType, final Object param) {
        return conditionType.addValue(paramList, param);
    }

    /**
     * 条件にプロパティ名を追加します。
     * 
     * @param propertyName
     *            プロパティ名
     */
    public void addPropertyName(final CharSequence propertyName) {
        propertyNameList.add(propertyName.toString());
    }

}
