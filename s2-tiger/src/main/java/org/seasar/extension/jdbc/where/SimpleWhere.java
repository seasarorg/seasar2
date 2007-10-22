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

import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.ConditionType;
import org.seasar.extension.jdbc.Where;

/**
 * 入力された項目をandでつなげていくような検索条件を組み立てるクラスです。
 * 
 * @author higa
 * 
 */
public class SimpleWhere implements Where {

    private StringBuilder criteriaSb = new StringBuilder(100);

    private List<Object> paramList = new ArrayList<Object>();

    /**
     * {@link SimpleWhere}を作成します。
     */
    public SimpleWhere() {
    }

    /**
     * 条件を追加します。
     * 
     * @param conditionType
     *            条件タイプ
     * @param propertyName
     *            プロパティ名
     * @param value
     *            値
     */
    protected void addCondition(ConditionType conditionType,
            String propertyName, Object value) {
        if (criteriaSb.length() > 0) {
            criteriaSb.append(" and ");
        }
        criteriaSb.append(conditionType.getCondition(propertyName, value));
        conditionType.addValue(paramList, value);
    }

    /**
     * <code>=</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public SimpleWhere eq(String propertyName, Object value) {
        if (value != null) {
            addCondition(ConditionType.EQ, propertyName, value);
        }
        return this;
    }

    /**
     * <code>&lt;&gt;</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public SimpleWhere ne(String propertyName, Object value) {
        if (value != null) {
            addCondition(ConditionType.NE, propertyName, value);
        }
        return this;
    }

    /**
     * <code>&lt;</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public SimpleWhere lt(String propertyName, Object value) {
        if (value != null) {
            addCondition(ConditionType.LT, propertyName, value);
        }
        return this;
    }

    /**
     * <code>&lt;=</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public SimpleWhere le(String propertyName, Object value) {
        if (value != null) {
            addCondition(ConditionType.LE, propertyName, value);
        }
        return this;
    }

    /**
     * <code>&gt;</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public SimpleWhere gt(String propertyName, Object value) {
        if (value != null) {
            addCondition(ConditionType.GT, propertyName, value);
        }
        return this;
    }

    /**
     * <code>&gt;=</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public SimpleWhere ge(String propertyName, Object value) {
        if (value != null) {
            addCondition(ConditionType.GE, propertyName, value);
        }
        return this;
    }

    /**
     * <code>in</code>の条件を追加します。
     * 
     * @param propertyName
     * @param values
     * @return このインスタンス自身
     */
    public SimpleWhere in(String propertyName, Object... values) {
        if (values != null && values.length > 0) {
            addCondition(ConditionType.IN, propertyName, values);
        }
        return this;
    }

    /**
     * <code>not in</code>の条件を追加します。
     * 
     * @param propertyName
     * @param values
     * @return このインスタンス自身
     */
    public SimpleWhere notIn(String propertyName, Object... values) {
        if (values != null && values.length > 0) {
            addCondition(ConditionType.NOT_IN, propertyName, values);
        }
        return this;
    }

    /**
     * <code>like</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public SimpleWhere like(String propertyName, Object value) {
        if (value != null) {
            addCondition(ConditionType.LIKE, propertyName, value);
        }
        return this;
    }

    /**
     * <code>like '?%'</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public SimpleWhere starts(String propertyName, Object value) {
        if (value != null) {
            addCondition(ConditionType.STARTS, propertyName, value);
        }
        return this;
    }

    /**
     * <code>like '%?'</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public SimpleWhere ends(String propertyName, Object value) {
        if (value != null) {
            addCondition(ConditionType.ENDS, propertyName, value);
        }
        return this;
    }

    /**
     * <code>like '%?%'</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public SimpleWhere contains(String propertyName, Object value) {
        if (value != null) {
            addCondition(ConditionType.CONTAINS, propertyName, value);
        }
        return this;
    }

    /**
     * <code>is null</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public SimpleWhere isNull(String propertyName, Object value) {
        if (value != null && Boolean.TRUE.equals(value)) {
            addCondition(ConditionType.IS_NULL, propertyName, value);
        }
        return this;
    }

    /**
     * <code>is not null</code>の条件を追加します。
     * 
     * @param propertyName
     * @param value
     * @return このインスタンス自身
     */
    public SimpleWhere isNotNull(String propertyName, Object value) {
        if (value != null && Boolean.TRUE.equals(value)) {
            addCondition(ConditionType.IS_NOT_NULL, propertyName, value);
        }
        return this;
    }

    public String getCriteria() {
        return criteriaSb.toString();
    }

    public Object[] getParams() {
        return paramList.toArray();
    }

}