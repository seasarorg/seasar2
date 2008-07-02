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
 * テーブル記述です。
 * 
 * @author taedium
 */
public class TableDesc {

    /** カタログ名 */
    protected String catalogName;

    /** スキーマ名 */
    protected String schemaName;

    /** 名前 */
    protected String name;

    /** カラム記述のリスト */
    protected List<ColumnDesc> columnDescList = new ArrayList<ColumnDesc>();

    /** 主キー記述のリスト */
    protected PrimaryKeyDesc primaryKeyDesc;

    /** 外部キー記述のリスト */
    protected List<ForeignKeyDesc> foreigneKeyDescList = new ArrayList<ForeignKeyDesc>();

    /** 一意記述のリスト */
    protected List<UniqueKeyDesc> uniqueKeyDescList = new ArrayList<UniqueKeyDesc>();

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
    public List<ForeignKeyDesc> getForeigneKeyDescList() {
        return Collections.unmodifiableList(foreigneKeyDescList);
    }

    /**
     * 外部キー記述を追加します。
     * 
     * @param foreigneKeyDesc
     *            外部キー記述
     */
    public void addForeigneKeyDesc(ForeignKeyDesc foreigneKeyDesc) {
        if (!foreigneKeyDescList.contains(foreigneKeyDesc)) {
            foreigneKeyDescList.add(foreigneKeyDesc);
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((catalogName == null) ? 0 : catalogName.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((schemaName == null) ? 0 : schemaName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final TableDesc other = (TableDesc) obj;
        if (catalogName == null) {
            if (other.catalogName != null)
                return false;
        } else if (!catalogName.equals(other.catalogName))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (schemaName == null) {
            if (other.schemaName != null)
                return false;
        } else if (!schemaName.equals(other.schemaName))
            return false;
        return true;
    }

}
