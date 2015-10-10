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
package org.seasar.extension.jdbc;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.seasar.extension.jdbc.exception.NonArrayInConditionRuntimeException;
import org.seasar.extension.jdbc.exception.NonBooleanIsNullConditionRuntimeException;
import org.seasar.framework.util.StringUtil;

/**
 * where句の条件タイプです。
 * 
 * @author higa
 * 
 */
public enum ConditionType {

    /**
     * =です。
     */
    EQ {

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeCondition(tableAlias, columnName, "=");
        }
    },

    /**
     * &lt;&gt;です。
     */
    NE {

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeCondition(tableAlias, columnName, "<>");
        }
    },

    /**
     * &lt;です。
     */
    LT {

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeCondition(tableAlias, columnName, "<");
        }
    },
    /**
     * &lt;=です。
     */
    LE {

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeCondition(tableAlias, columnName, "<=");
        }
    },

    /**
     * &gt;です。
     */
    GT {

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeCondition(tableAlias, columnName, ">");
        }
    },

    /**
     * &gt;=です。
     */
    GE {

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeCondition(tableAlias, columnName, ">=");
        }
    },

    /**
     * inです。
     */
    IN {

        @Override
        public boolean isTarget(Object value) {
            return isTargetForIn("in", value);
        }

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeConditionForIn(tableAlias, columnName, "in", value);
        }

        @Override
        public int addValue(List<Object> paramList, Object value) {
            return addValueForIn(paramList, value);
        }
    },

    /**
     * not inです。
     */
    NOT_IN {

        @Override
        public boolean isTarget(Object value) {
            return isTargetForIn("not in", value);
        }

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeConditionForIn(tableAlias, columnName, "not in", value);
        }

        @Override
        public int addValue(List<Object> paramList, Object value) {
            return addValueForIn(paramList, value);
        }
    },

    /**
     * like ?です。
     */
    LIKE {

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeConditionForLike(tableAlias, columnName, "like", null);
        }
    },

    /**
     * like ? escape ?です。
     */
    LIKE_ESCAPE {

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeConditionForLike(tableAlias, columnName, "like", "?");
        }

        @Override
        public int addValue(List<Object> valueList, Object value) {
            final Object[] values = Object[].class.cast(value);
            super.addValue(valueList, values[0]);
            super.addValue(valueList, values[1]);
            return 2;
        }
    },

    /**
     * not like ?です。
     */
    NOT_LIKE {

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeConditionForLike(tableAlias, columnName, "not like",
                    null);
        }
    },

    /**
     * not like ? escape ?です。
     */
    NOT_LIKE_ESCAPE {

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeConditionForLike(tableAlias, columnName, "not like", "?");
        }

        @Override
        public int addValue(List<Object> valueList, Object value) {
            final Object[] values = Object[].class.cast(value);
            super.addValue(valueList, values[0]);
            super.addValue(valueList, values[1]);
            return 2;
        }
    },

    /**
     * like '?%'です。
     */
    STARTS {

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeConditionForLike(tableAlias, columnName, "like", null);
        }

        @Override
        public int addValue(List<Object> valueList, Object value) {
            return super.addValue(valueList, value + "%");
        }
    },

    /**
     * like '?%' escape '$'です。
     */
    STARTS_ESCAPE {

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeConditionForLike(tableAlias, columnName, "like", "'$'");
        }

        @Override
        public int addValue(List<Object> valueList, Object value) {
            return super.addValue(valueList, value + "%");
        }
    },

    /**
     * not like '?%'です。
     */
    NOT_STARTS {

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeConditionForLike(tableAlias, columnName, "not like",
                    null);
        }

        @Override
        public int addValue(List<Object> valueList, Object value) {
            return super.addValue(valueList, value + "%");
        }
    },

    /**
     * not like '?%' escape '$'です。
     */
    NOT_STARTS_ESCAPE {

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeConditionForLike(tableAlias, columnName, "not like",
                    "'$'");
        }

        @Override
        public int addValue(List<Object> valueList, Object value) {
            return super.addValue(valueList, value + "%");
        }
    },

    /**
     * like '%?'です。
     */
    ENDS {

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeConditionForLike(tableAlias, columnName, "like", null);
        }

        @Override
        public int addValue(List<Object> valueList, Object value) {
            return super.addValue(valueList, "%" + value);
        }
    },

    /**
     * like '%?' escape '$'です。
     */
    ENDS_ESCAPE {

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeConditionForLike(tableAlias, columnName, "like", "'$'");
        }

        @Override
        public int addValue(List<Object> valueList, Object value) {
            return super.addValue(valueList, "%" + value);
        }
    },

    /**
     * not like '%?'です。
     */
    NOT_ENDS {

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeConditionForLike(tableAlias, columnName, "not like",
                    null);
        }

        @Override
        public int addValue(List<Object> valueList, Object value) {
            return super.addValue(valueList, "%" + value);
        }
    },

    /**
     * not like '%?' escape '$'です。
     */
    NOT_ENDS_ESCAPE {

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeConditionForLike(tableAlias, columnName, "not like",
                    "'$'");
        }

        @Override
        public int addValue(List<Object> valueList, Object value) {
            return super.addValue(valueList, "%" + value);
        }
    },

    /**
     * like '%?%'です。
     */
    CONTAINS {

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeConditionForLike(tableAlias, columnName, "like", null);
        }

        @Override
        public int addValue(List<Object> valueList, Object value) {
            return super.addValue(valueList, "%" + value + "%");
        }
    },

    /**
     * like '%?%' escape '$'です。
     */
    CONTAINS_ESCAPE {

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeConditionForLike(tableAlias, columnName, "like", "'$'");
        }

        @Override
        public int addValue(List<Object> valueList, Object value) {
            return super.addValue(valueList, "%" + value + "%");
        }
    },

    /**
     * not like '%?%'です。
     */
    NOT_CONTAINS {

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeConditionForLike(tableAlias, columnName, "not like",
                    null);
        }

        @Override
        public int addValue(List<Object> valueList, Object value) {
            return super.addValue(valueList, "%" + value + "%");
        }
    },

    /**
     * not like '%?%' escape '$'です。
     */
    NOT_CONTAINS_ESCAPE {

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeConditionForLike(tableAlias, columnName, "not like",
                    "'$'");
        }

        @Override
        public int addValue(List<Object> valueList, Object value) {
            return super.addValue(valueList, "%" + value + "%");
        }
    },

    /**
     * is nullです。
     */
    IS_NULL {

        @Override
        public boolean isTarget(Object value) {
            return isTargetForIsNull("is null", value);
        }

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeConditionForIsNull(tableAlias, columnName, "is null");
        }

        @Override
        public int addValue(List<Object> paramList, Object value) {
            return 0;
        }
    },

    /**
     * is not nullです。
     */
    IS_NOT_NULL {

        @Override
        public boolean isTarget(Object value) {
            return isTargetForIsNull("is not null", value);
        }

        @Override
        public String getCondition(String tableAlias, String columnName,
                Object value) {
            return makeConditionForIsNull(tableAlias, columnName, "is not null");
        }

        @Override
        public int addValue(List<Object> paramList, Object value) {
            return 0;
        }
    };

    /**
     * 名前に応じた条件タイプを返します。
     * 
     * @param name
     *            名前
     * @return 条件タイプ
     */
    public static ConditionType getConditionType(String name) {
        if (NE.hasSuffix(name)) {
            return NE;
        } else if (LT.hasSuffix(name)) {
            return LT;
        } else if (LE.hasSuffix(name)) {
            return LE;
        } else if (GT.hasSuffix(name)) {
            return GT;
        } else if (GE.hasSuffix(name)) {
            return GE;
        } else if (NOT_IN.hasSuffix(name)) {
            return NOT_IN;
        } else if (IN.hasSuffix(name)) {
            return IN;
        } else if (LIKE.hasSuffix(name)) {
            return LIKE;
        } else if (STARTS.hasSuffix(name)) {
            return STARTS;
        } else if (ENDS.hasSuffix(name)) {
            return ENDS;
        } else if (CONTAINS.hasSuffix(name)) {
            return CONTAINS;
        } else if (IS_NULL.hasSuffix(name)) {
            return IS_NULL;
        } else if (IS_NOT_NULL.hasSuffix(name)) {
            return IS_NOT_NULL;
        }
        return EQ;
    }

    /**
     * サフィックスを返します。
     * 
     * @return サフィックス
     */
    public String getSuffix() {
        return "_" + name();
    }

    /**
     * サフィックスを持っているかどうかを返します。
     * 
     * @param name
     *            名前
     * @return サフィックスを持っているかどうか
     */
    public boolean hasSuffix(String name) {
        return name.endsWith(getSuffix());
    }

    /**
     * サフィックスを削除します。
     * 
     * @param s
     *            文字列
     * @return サフィックスが削除された文字列
     */
    public String removeSuffix(String s) {
        String suffix = getSuffix();
        if (s.endsWith(suffix)) {
            return s.substring(0, s.length() - getSuffix().length());
        }
        return s;
    }

    /**
     * 条件を追加します。
     * 
     * @param name
     *            名前
     * @param value
     *            値
     * @param whereClause
     *            where句
     * @param valueList
     *            値のリスト
     * @return 追加した値の数
     */
    public int addCondition(String name, Object value, WhereClause whereClause,
            List<Object> valueList) {
        return addCondition(null, name, value, whereClause, valueList);
    }

    /**
     * 条件を追加します。
     * 
     * @param tableAlias
     *            テーブルエイリアス
     * @param columnName
     *            カラム名
     * @param value
     *            値
     * @param whereClause
     *            where句
     * @param valueList
     *            値のリスト
     * @return 追加した値の数
     */
    public int addCondition(String tableAlias, String columnName, Object value,
            WhereClause whereClause, List<Object> valueList) {
        if (isTarget(value)) {
            whereClause.addAndSql(getCondition(tableAlias, columnName, value));
            return addValue(valueList, value);
        }
        return 0;
    }

    /**
     * 条件に追加する対象かどうかを返します。
     * 
     * @param value
     *            値
     * @return 条件に追加する対象かどうか
     */
    public boolean isTarget(Object value) {
        return value != null;
    }

    /**
     * 条件を返します。
     * 
     * @param name
     *            名前
     * @param value
     *            値
     * @return 条件
     */
    public String getCondition(String name, Object value) {
        return getCondition(null, name, value);
    }

    /**
     * 条件を返します。
     * 
     * @param tableAlias
     *            テーブルエイリアス
     * @param columnName
     *            カラム名
     * @param value
     *            値
     * @return 条件
     */
    public abstract String getCondition(String tableAlias, String columnName,
            Object value);

    /**
     * 値を追加します。
     * 
     * @param valueList
     *            値のリスト
     * @param value
     *            値
     * @return 追加した値の数
     */
    public int addValue(List<Object> valueList, Object value) {
        valueList.add(value);
        return 1;
    }

    /**
     * 条件を組み立てます。
     * 
     * @param tableAlias
     *            テーブルエイリアス
     * @param columnName
     *            カラム名
     * @param operator
     *            演算子
     * @return 条件
     */
    protected String makeCondition(String tableAlias, String columnName,
            String operator) {
        final StringBuilder buf = new StringBuilder(32);
        if (!StringUtil.isEmpty(tableAlias)) {
            buf.append(tableAlias).append('.');
        }
        buf.append(columnName).append(' ').append(operator).append(" ?");
        return new String(buf);
    }

    /**
     * <code>in, not in</code>の条件に追加する対象かどうかを返します。
     * 
     * @param conditionName
     *            条件名
     * @param value
     *            値
     * @return 条件に追加する対象かどうか
     */
    protected boolean isTargetForIn(String conditionName, Object value) {
        Collection<?> values = null;
        if (value == null) {
            return false;
        } else if (value.getClass().isArray()) {
            values = Arrays.asList((Object[]) value);
        } else if (value instanceof Collection) {
            values = (Collection<?>) value;
        } else {
            throw new NonArrayInConditionRuntimeException(conditionName, value
                    .getClass());
        }
        if (values.isEmpty()) {
            return false;
        }
        for (Object element : values) {
            if (element != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * <code>in, not in</code>用の条件を組み立てます。
     * 
     * @param tableAlias
     *            テーブルエイリアス
     * @param columnName
     *            カラム名
     * @param conditionName
     *            条件名
     * @param value
     *            値
     * @return 条件
     */
    protected String makeConditionForIn(String tableAlias, String columnName,
            String conditionName, Object value) {
        int size = value instanceof Collection ? ((Collection<?>) value).size()
                : Array.getLength(value);
        StringBuilder buf = new StringBuilder(30);
        if (!StringUtil.isEmpty(tableAlias)) {
            buf.append(tableAlias).append('.');
        }
        buf.append(columnName).append(' ').append(conditionName).append(" (");
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                buf.append(", ");
            }
            buf.append("?");
        }
        buf.append(")");
        return buf.toString();
    }

    /**
     * <code>in, not in</code>用に値を追加します。
     * 
     * @param valueList
     *            値のリスト
     * @param value
     *            値
     * @return 追加した値の数
     */
    protected int addValueForIn(List<Object> valueList, Object value) {
        final Collection<?> list = value instanceof Collection ? (Collection<?>) value
                : Arrays.asList((Object[]) value);
        valueList.addAll(list);
        return list.size();
    }

    /**
     * <code>like</code>の条件を組み立てます。
     * 
     * @param tableAlias
     *            テーブルエイリアス
     * @param columnName
     *            カラム名
     * @param operator
     *            演算子
     * @param escape
     *            エスケープ
     * @return 条件
     */
    protected String makeConditionForLike(String tableAlias, String columnName,
            String operator, String escape) {
        final StringBuilder buf = new StringBuilder(32);
        if (!StringUtil.isEmpty(tableAlias)) {
            buf.append(tableAlias).append('.');
        }
        buf.append(columnName).append(' ').append(operator).append(" ?");
        if (escape != null) {
            buf.append(" escape ").append(escape);
        }
        return new String(buf);
    }

    /**
     * <code>in, not in</code>の条件に追加する対象かどうかを返します。
     * 
     * @param conditionName
     *            条件名
     * @param value
     *            値
     * @return 条件に追加する対象かどうか
     */
    protected boolean isTargetForIsNull(String conditionName, Object value) {
        if (value != null && value.getClass() != Boolean.class) {
            throw new NonBooleanIsNullConditionRuntimeException(conditionName,
                    value.getClass());
        }
        return value != null && Boolean.TRUE.equals(value);
    }

    /**
     * <code>is null, is not null</code>用の条件を組み立てます。
     * 
     * @param tableAlias
     *            テーブルエイリアス
     * @param columnName
     *            カラム名
     * @param operator
     *            演算子
     * @param escape
     *            エスケープ
     * @return 条件
     */
    protected String makeConditionForIsNull(String tableAlias,
            String columnName, String operator) {
        final StringBuilder buf = new StringBuilder(32);
        if (!StringUtil.isEmpty(tableAlias)) {
            buf.append(tableAlias).append('.');
        }
        buf.append(columnName).append(' ').append(operator);
        return new String(buf);
    }

}