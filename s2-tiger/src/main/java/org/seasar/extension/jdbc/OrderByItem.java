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

/**
 * OrderByの項目を組み立てるインターフェースです。
 * 
 * @author koichik
 */
public class OrderByItem {

    /**
     * 昇順・降順を表す列挙です。
     */
    public enum OrderingSpec {

        /** 昇順です。 */
        ASC,

        /** 降順です。 */
        DESC;

        @Override
        public String toString() {
            return name().toLowerCase();
        }

    }

    /** SQLのフラグメントです。 */
    protected final String sql;

    /**
     * プロパティ名を指定して昇順のインスタンスを構築します。
     * 
     * @param propertyName
     *            プロパティ名
     */
    public OrderByItem(final CharSequence propertyName) {
        sql = propertyName.toString();
    }

    /**
     * プロパティ名と昇順・降順を指定してインスタンスを構築します。
     * 
     * @param propertyName
     *            プロパティ名
     * @param spec
     *            昇順・降順
     */
    public OrderByItem(final CharSequence propertyName, final OrderingSpec spec) {
        sql = propertyName + " " + spec;
    }

    /**
     * クライテリアを返します。
     * 
     * @return クライテリア
     */
    public String getCriteria() {
        return sql;
    }

}
