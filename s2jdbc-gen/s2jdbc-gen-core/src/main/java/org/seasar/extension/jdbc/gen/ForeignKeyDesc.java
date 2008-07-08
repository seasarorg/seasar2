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
package org.seasar.extension.jdbc.gen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 外部キー記述です。
 * 
 * @author taedium
 */
public class ForeignKeyDesc {

    protected final Key key = new Key();

    /** カラム名のリスト */
    protected List<String> columnNameList = new ArrayList<String>();

    /** 参照されるテーブルのカタログ名 */
    protected String referencedCatalogName;

    /** 参照されるテーブルのスキーマ名 */
    protected String referencedSchemaName;

    /** 参照されるテーブル名 */
    protected String referencedTableName;

    /** 参照されるカラム名のリスト */
    protected List<String> referencedColumnNameList = new ArrayList<String>();

    /**
     * カラム名のリストを返します。
     * 
     * @return カラム名のリスト
     */
    public List<String> getColumnNameList() {
        return Collections.unmodifiableList(columnNameList);
    }

    /**
     * カラム名を追加します。
     * 
     * @param columnName
     *            カラム名
     */
    public void addColumnName(String columnName) {
        columnNameList.add(columnName);
        key.addColumnName(columnName);
    }

    /**
     * 参照されるテーブルのカタログ名を返します。
     * 
     * @return 参照されるテーブルのカタログ名
     */
    public String getReferencedCatalogName() {
        return referencedCatalogName;
    }

    /**
     * 参照されるテーブルのカタログ名を設定します。
     * 
     * @param referencedCatalogName
     *            参照されるテーブルのカタログ名
     */
    public void setReferencedCatalogName(String referencedCatalogName) {
        this.referencedCatalogName = referencedCatalogName;
        key.setReferencedCatalogName(referencedCatalogName);
    }

    /**
     * 参照されるテーブルのスキーマ名を返します。
     * 
     * @return 参照されるテーブルのスキーマ名
     */
    public String getReferencedSchemaName() {
        return referencedSchemaName;
    }

    /**
     * 参照されるテーブルのスキーマ名を設定します。
     * 
     * @param referencedSchemaName
     *            参照されるテーブルのスキーマ名
     */
    public void setReferencedSchemaName(String referencedSchemaName) {
        this.referencedSchemaName = referencedSchemaName;
        key.setReferencedSchemaName(referencedSchemaName);
    }

    /**
     * 参照されるテーブル名を返します。
     * 
     * @return 参照されるテーブル名
     */
    public String getReferencedTableName() {
        return referencedTableName;
    }

    /**
     * 参照されるテーブル名を設定します。
     * 
     * @param referencedTableName
     *            参照されるテーブル名
     */
    public void setReferencedTableName(String referencedTableName) {
        this.referencedTableName = referencedTableName;
        key.setReferencedTableName(referencedTableName);
    }

    /**
     * 参照されるカラム名のリストを返します。
     * 
     * @return 参照されるカラム名のリスト
     */
    public List<String> getReferencedColumnNameList() {
        return Collections.unmodifiableList(referencedColumnNameList);
    }

    /**
     * 参照されるカラム名を追加します。
     * 
     * @param referencedColumnName
     *            参照されるカラム名
     */
    public void addReferencedColumnName(String referencedColumnName) {
        referencedColumnNameList.add(referencedColumnName);
        key.addtReferencedColumnName(referencedColumnName);
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
        ForeignKeyDesc other = (ForeignKeyDesc) obj;
        return key.equals(other.key);
    }

    protected static class Key {

        /** カラム名のリスト */
        protected List<String> columnNameList = new ArrayList<String>();

        /** 参照されるテーブルのカタログ名 */
        protected String referencedCatalogName;

        /** 参照されるテーブルのスキーマ名 */
        protected String referencedSchemaName;

        /** 参照されるテーブル名 */
        protected String referencedTableName;

        /** 参照されるカラム名のリスト */
        protected List<String> referencedColumnNameList = new ArrayList<String>();

        protected void addColumnName(String columnName) {
            this.columnNameList.add(columnName.toLowerCase());
        }

        protected void setReferencedCatalogName(String referencedCatalogName) {
            this.referencedCatalogName = referencedCatalogName.toLowerCase();
        }

        protected void setReferencedSchemaName(String referencedSchemaName) {
            this.referencedSchemaName = referencedSchemaName.toLowerCase();
        }

        protected void setReferencedTableName(String referencedTableName) {
            this.referencedTableName = referencedTableName.toLowerCase();
        }

        protected void addtReferencedColumnName(String referencedColumnName) {
            this.referencedColumnNameList.add(referencedColumnName
                    .toLowerCase());
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime
                    * result
                    + ((columnNameList == null) ? 0 : columnNameList.hashCode());
            result = prime
                    * result
                    + ((referencedCatalogName == null) ? 0
                            : referencedCatalogName.hashCode());
            result = prime
                    * result
                    + ((referencedSchemaName == null) ? 0
                            : referencedSchemaName.hashCode());
            result = prime
                    * result
                    + ((referencedTableName == null) ? 0 : referencedTableName
                            .hashCode());
            result = prime
                    * result
                    + ((referencedColumnNameList == null) ? 0
                            : referencedColumnNameList.hashCode());
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
            if (referencedCatalogName == null) {
                if (other.referencedCatalogName != null) {
                    return false;
                }
            } else if (!referencedCatalogName
                    .equals(other.referencedCatalogName)) {
                return false;
            }
            if (referencedSchemaName == null) {
                if (other.referencedSchemaName != null) {
                    return false;
                }
            } else if (!referencedSchemaName.equals(other.referencedSchemaName)) {
                return false;
            }
            if (referencedTableName == null) {
                if (other.referencedTableName != null) {
                    return false;
                }
            } else if (!referencedTableName.equals(other.referencedTableName)) {
                return false;
            }
            if (referencedColumnNameList == null) {
                if (other.referencedColumnNameList != null) {
                    return false;
                }
            } else if (!referencedColumnNameList
                    .equals(other.referencedColumnNameList)) {
                return false;
            }
            return true;
        }
    }

}
