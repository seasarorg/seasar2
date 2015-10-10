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
package org.seasar.extension.jdbc.gen.desc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 主キー記述です。
 * 
 * @author taedium
 */
public class PrimaryKeyDesc {

    /** キー */
    protected final Key key = new Key();

    /** カラム名のリスト */
    protected List<String> columnNameList = new ArrayList<String>();

    /**
     * カラム名のリストを返します。
     * 
     * @return カラム名のリスト
     */
    public List<String> getColumnNameList() {
        return Collections.unmodifiableList(columnNameList);
    }

    /**
     * カラム名のリストを設定します。
     * 
     * @param columnName
     *            カラム名のリスト
     */
    public void addColumnName(String columnName) {
        columnNameList.add(columnName);
        key.addColumnName(columnName);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PrimaryKeyDesc other = (PrimaryKeyDesc) obj;
        return key.equals(other.key);
    }

    /**
     * キーです。
     * 
     * @author taedium
     */
    protected static class Key {

        /** カラム名のリスト */
        protected List<String> columnNameList = new ArrayList<String>();

        /**
         * カラム名を追加します。
         * 
         * @param columnName
         *            カラム名
         */
        protected void addColumnName(String columnName) {
            if (columnName != null) {
                columnNameList.add(columnName.toLowerCase());
            } else {
                columnNameList.add(null);
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime
                    * result
                    + ((columnNameList == null) ? 0 : columnNameList.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Key other = (Key) obj;
            if (columnNameList == null) {
                if (other.columnNameList != null) {
                    return false;
                }
            } else if (!columnNameList.equals(other.columnNameList)) {
                return false;
            }
            return true;
        }
    }
}
