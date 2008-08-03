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
import java.util.Map;

import org.seasar.framework.util.ArrayMap;

/**
 * テーブル記述です。
 * 
 * @author taedium
 */
public class TableDesc {

    /** キー */
    protected final Key key = new Key();

    /** カタログ名 */
    protected String catalogName;

    /** スキーマ名 */
    protected String schemaName;

    /** 名前 */
    protected String name;

    /** カラム記述のリスト */
    protected List<ColumnDesc> columnDescList = new ArrayList<ColumnDesc>();

    protected Map<String, ColumnDesc> columnDescMap = new ArrayMap();

    /** 主キー記述のリスト */
    protected PrimaryKeyDesc primaryKeyDesc;

    /** 外部キー記述のリスト */
    protected List<ForeignKeyDesc> foreignKeyDescList = new ArrayList<ForeignKeyDesc>();

    /** 一意キー記述のリスト */
    protected List<UniqueKeyDesc> uniqueKeyDescList = new ArrayList<UniqueKeyDesc>();

    /** シーケンス記述のリスト */
    protected List<SequenceDesc> sequenceDesclist = new ArrayList<SequenceDesc>();

    /** 識別子を生成するテーブル記述のリスト */
    protected List<TableDesc> idTableDescList = new ArrayList<TableDesc>();

    /**
     * カタログ名を返す。
     * 
     * @return カタログ名
     */
    public String getCatalogName() {
        return catalogName;
    }

    /**
     * カタログ名を設定します。
     * 
     * @param catalogName
     *            カタログ名
     */
    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
        key.setCatalogName(catalogName);
    }

    /**
     * スキーマ名を返します。
     * 
     * @return スキーマ名
     */
    public String getSchemaName() {
        return schemaName;
    }

    /**
     * スキーマ名を設定します。
     * 
     * @param schemaName
     *            スキーマ名
     */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
        key.setSchemaName(schemaName);
    }

    /**
     * 名前を返します。
     * 
     * @return 名前
     */
    public String getName() {
        return name;
    }

    /**
     * 名前を設定します。
     * 
     * @param name
     *            名前
     */
    public void setName(String name) {
        this.name = name;
        key.setName(name);
    }

    /**
     * カラム記述のリストを返します。
     * 
     * @return カラム記述のリスト
     */
    public List<ColumnDesc> getColumnDescList() {
        return Collections.unmodifiableList(columnDescList);
    }

    /**
     * カラム記述を追加します。
     * 
     * @param columnDesc
     *            カラム記述
     */
    public void addColumnDesc(ColumnDesc columnDesc) {
        if (!columnDescList.contains(columnDesc)) {
            columnDescList.add(columnDesc);
            columnDescMap.put(columnDesc.getName(), columnDesc);
        }
    }

    /**
     * 主キー記述を返します。
     * 
     * @return 主キー記述
     */
    public PrimaryKeyDesc getPrimaryKeyDesc() {
        return primaryKeyDesc;
    }

    /**
     * 主キー記述を設定します。
     * 
     * @param primaryKeyDesc
     *            主キー記述
     */
    public void setPrimaryKeyDesc(PrimaryKeyDesc primaryKeyDesc) {
        this.primaryKeyDesc = primaryKeyDesc;
    }

    /**
     * 外部キー記述のリストを返します。
     * 
     * @return 外部キー記述
     */
    public List<ForeignKeyDesc> getForeignKeyDescList() {
        return Collections.unmodifiableList(foreignKeyDescList);
    }

    /**
     * 外部キー記述を追加します。
     * 
     * @param foreignKeyDesc
     *            外部キー記述
     */
    public void addForeignKeyDesc(ForeignKeyDesc foreignKeyDesc) {
        if (!foreignKeyDescList.contains(foreignKeyDesc)) {
            foreignKeyDescList.add(foreignKeyDesc);
        }
    }

    /**
     * 一意キー記述のリストを返します。
     * 
     * @return 一意キー記述のリスト
     */
    public List<UniqueKeyDesc> getUniqueKeyDescList() {
        return Collections.unmodifiableList(uniqueKeyDescList);
    }

    /**
     * 一意キー記述を追加します。
     * 
     * @param uniqueKeyDesc
     *            一意キー記述
     */
    public void addUniqueKeyDesc(UniqueKeyDesc uniqueKeyDesc) {
        if (!uniqueKeyDescList.contains(uniqueKeyDesc)) {
            uniqueKeyDescList.add(uniqueKeyDesc);
        }
    }

    /**
     * シーケンス記述のリストを返します。
     * 
     * @return シーケンス記述のリスト
     */
    public List<SequenceDesc> getSequenceDescList() {
        return Collections.unmodifiableList(sequenceDesclist);
    }

    /**
     * シーケンス記述を追加します。
     * 
     * @param sequenceDesc
     *            シーケンス記述
     */
    public void addSequenceDesc(SequenceDesc sequenceDesc) {
        if (!sequenceDesclist.contains(sequenceDesc)) {
            sequenceDesclist.add(sequenceDesc);
        }
    }

    /**
     * 識別子を生成するテーブル記述のリストを返します。
     * 
     * @return 識別子を生成するテーブル記述のリスト
     */
    public List<TableDesc> getIdTableDescList() {
        return Collections.unmodifiableList(idTableDescList);
    }

    /**
     * 識別子を生成するテーブル記述を追加します。
     * 
     * @param idTableDesc
     *            識別子を生成するテーブル記述
     */
    public void addIdTableDesc(TableDesc idTableDesc) {
        idTableDescList.add(idTableDesc);
    }

    /**
     * テーブルの完全な名前を返します。
     * 
     * @return テーブルの完全な名前
     */
    public String getFullName() {
        StringBuilder buf = new StringBuilder();
        if (catalogName != null) {
            buf.append(catalogName).append(".");
        }
        if (schemaName != null) {
            buf.append(schemaName).append(".");
        }
        return buf.append(name).toString();
    }

    public ColumnDesc getColumnDesc(String columnName) {
        return columnDescMap.get(columnName);
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
        final TableDesc other = (TableDesc) obj;
        return key.equals(other.key);
    }

    /**
     * キーです。
     * 
     * @author taedium
     */
    protected static class Key {

        /** カタログ名 */
        protected String catalogName;

        /** スキーマ名 */
        protected String schemaName;

        /** 名前 */
        protected String name;

        /**
         * カタログ名を設定します。
         * 
         * @param catalogName
         *            カタログ名
         */
        public void setCatalogName(String catalogName) {
            if (catalogName != null) {
                this.catalogName = catalogName.toLowerCase();
            }
        }

        /**
         * スキーマ名を設定します。
         * 
         * @param schemaName
         *            スキーマ名
         */
        public void setSchemaName(String schemaName) {
            if (schemaName != null) {
                this.schemaName = schemaName.toLowerCase();
            }
        }

        /**
         * 名前を設定します。
         * 
         * @param name
         *            名前
         */
        public void setName(String name) {
            if (name != null) {
                this.name = name.toLowerCase();
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((catalogName == null) ? 0 : catalogName.hashCode());
            result = prime * result
                    + ((schemaName == null) ? 0 : schemaName.hashCode());
            result = prime * result + ((name == null) ? 0 : name.hashCode());
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
            if (catalogName == null) {
                if (other.catalogName != null) {
                    return false;
                }
            } else if (!catalogName.equals(other.catalogName)) {
                return false;
            }
            if (name == null) {
                if (other.name != null) {
                    return false;
                }
            } else if (!name.equals(other.name)) {
                return false;
            }
            if (schemaName == null) {
                if (other.schemaName != null) {
                    return false;
                }
            } else if (!schemaName.equals(other.schemaName)) {
                return false;
            }
            return true;
        }
    }

}
