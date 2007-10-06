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
package org.seasar.extension.jdbc.util;

import org.seasar.framework.util.StringUtil;

/**
 * where句の条件を組み立てるためのユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public final class ConditionUtil {

    private ConditionUtil() {
    }

    /**
     * =用の条件を返します。
     * 
     * @param columnName
     *            カラム名
     * @return =用の条件
     */
    public static String getEqCondition(String columnName) {
        return getEqCondition(null, columnName);
    }

    /**
     * =用の条件を返します。
     * 
     * @param tableAlias
     *            テーブル別名
     * @param columnName
     *            カラム名
     * @return =用の条件
     */
    public static String getEqCondition(String tableAlias, String columnName) {
        if (StringUtil.isEmpty(tableAlias)) {
            return columnName + " = ?";
        }
        return tableAlias + "." + columnName + " = ?";
    }

    /**
     * &lt;&gt;用の条件を返します。
     * 
     * @param tableAlias
     *            テーブル別名
     * @param columnName
     *            カラム名
     * @return &lt;&gt;用の条件
     */
    public static String getNeCondition(String tableAlias, String columnName) {
        return tableAlias + "." + columnName + " <> ?";
    }

    /**
     * &lt;用の条件を返します。
     * 
     * @param tableAlias
     *            テーブル別名
     * @param columnName
     *            カラム名
     * @return &lt;用の条件
     */
    public static String getLtCondition(String tableAlias, String columnName) {
        return tableAlias + "." + columnName + " < ?";
    }

    /**
     * &lt;=用の条件を返します。
     * 
     * @param tableAlias
     *            テーブル別名
     * @param columnName
     *            カラム名
     * @return &lt;=用の条件
     */
    public static String getLeCondition(String tableAlias, String columnName) {
        return tableAlias + "." + columnName + " <= ?";
    }

    /**
     * &gt;用の条件を返します。
     * 
     * @param tableAlias
     *            テーブル別名
     * @param columnName
     *            カラム名
     * @return &gt;用の条件
     */
    public static String getGtCondition(String tableAlias, String columnName) {
        return tableAlias + "." + columnName + " > ?";
    }

    /**
     * &gt;=用の条件を返します。
     * 
     * @param tableAlias
     *            テーブル別名
     * @param columnName
     *            カラム名
     * @return &gt;=用の条件
     */
    public static String getGeCondition(String tableAlias, String columnName) {
        return tableAlias + "." + columnName + " >= ?";
    }

    /**
     * in用の条件を返します。
     * 
     * @param tableAlias
     *            テーブル別名
     * @param columnName
     *            カラム名
     * @param size
     *            変数の数
     * @return in用の条件
     */
    public static String getInCondition(String tableAlias, String columnName,
            int size) {
        return getInternalInCondition(tableAlias, columnName, size, "in");
    }

    /**
     * not in用の条件を返します。
     * 
     * @param tableAlias
     *            テーブル別名
     * @param columnName
     *            カラム名
     * @param size
     *            変数の数
     * @return in用の条件
     */
    public static String getNotInCondition(String tableAlias,
            String columnName, int size) {
        return getInternalInCondition(tableAlias, columnName, size, "not in");
    }

    private static String getInternalInCondition(String tableAlias,
            String columnName, int size, String str) {
        StringBuilder buf = new StringBuilder(30);
        buf.append(tableAlias).append(".").append(columnName);
        buf.append(" ").append(str).append(" (");
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
     * likeの条件を返します。
     * 
     * @param tableAlias
     *            テーブル別名
     * @param columnName
     *            カラム名
     * @return like用の条件
     */
    public static String getLikeCondition(String tableAlias, String columnName) {
        return tableAlias + "." + columnName + " like ?";
    }

    /**
     * is nullの条件を返します。
     * 
     * @param tableAlias
     *            テーブル別名
     * @param columnName
     *            カラム名
     * @return is null用の条件
     */
    public static String getIsNullCondition(String tableAlias, String columnName) {
        return tableAlias + "." + columnName + " is null";
    }

    /**
     * is not nullの条件を返します。
     * 
     * @param tableAlias
     *            テーブル別名
     * @param columnName
     *            カラム名
     * @return is not null用の条件
     */
    public static String getIsNotNullCondition(String tableAlias,
            String columnName) {
        return tableAlias + "." + columnName + " is not null";
    }
}
