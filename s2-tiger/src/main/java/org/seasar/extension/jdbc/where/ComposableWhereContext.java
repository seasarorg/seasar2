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
package org.seasar.extension.jdbc.where;

import java.util.List;

import org.seasar.extension.jdbc.ConditionType;
import org.seasar.extension.jdbc.Where;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * @author koichik
 */
public class ComposableWhereContext {

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

    public ComposableWhereContext append(Where where) {
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

    public ComposableWhereContext append(final boolean b) {
        criteriaSb.append(b);
        return this;
    }

    public ComposableWhereContext append(final byte b) {
        criteriaSb.append(b);
        return this;
    }

    public ComposableWhereContext append(final short s) {
        criteriaSb.append(s);
        return this;
    }

    public ComposableWhereContext append(final int i) {
        criteriaSb.append(i);
        return this;
    }

    public ComposableWhereContext append(final long l) {
        criteriaSb.append(l);
        return this;
    }

    public ComposableWhereContext append(final float f) {
        criteriaSb.append(f);
        return this;
    }

    public ComposableWhereContext append(final double d) {
        criteriaSb.append(d);
        return this;
    }

    public ComposableWhereContext append(final char ch) {
        criteriaSb.append(ch);
        return this;
    }

    public ComposableWhereContext append(final String s) {
        criteriaSb.append(s);
        return this;
    }

    public ComposableWhereContext append(final Enum<?> e) {
        criteriaSb.append(e.getClass().getName()).append(".").append(e.name());
        return this;
    }

    public ComposableWhereContext append(final Object o) {
        criteriaSb.append(o);
        return this;
    }

    public ComposableWhereContext cutBack(final int number) {
        criteriaSb.setLength(criteriaSb.length() - number);
        return this;
    }

    public void addParam(Object param) {
        paramList.add(param);
    }

    public int addParam(ConditionType conditionType, Object param) {
        return conditionType.addValue(paramList, param);
    }

    public void addPropertyName(CharSequence propertyName) {
        propertyNameList.add(propertyName.toString());
    }

}
