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
import java.util.Map;

import org.seasar.extension.jdbc.gen.exception.ColumnDescNotFoundRuntimeException;
import org.seasar.framework.util.CaseInsensitiveMap;

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

    /** 一意性をもつ標準名 */
    protected String canonicalName;

    /** コメント */
    protected String comment;

    /** カラム記述のリスト */
    protected List<ColumnDesc> columnDescList = new ArrayList<ColumnDesc>();

    /** カラム名ーをキー、カラム記述を値とするマップ */
    @SuppressWarnings("unchecked")
    protected Map<String, ColumnDesc> columnDescMap = new CaseInsensitiveMap();

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
     * 完全な名前を返します。
     * 
     * @return 完全な名前
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

    /**
     * 標準名を返します。
     * 
     * @return 標準名
     */
    public String getCanonicalName() {
        return canonicalName;
    }

    /**
     * 標準名を設定します。
     * 
     * @param canonicalName
     *            標準名
     */
    public void setCanonicalName(String canonicalName) {
        this.canonicalName = canonicalName;
        key.setCanonicalName(canonicalName);
    }

    /**
     * コメントを返します。
     * 
     * @return コメント
     */
    public String getComment() {
        return comment;
    }

    /**
     * コメントを設定します。
     * 
     * @param comment
     *            コメント
     */
    public void setComment(String comment) {
        this.comment = comment;
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
     * カラム記述を取得します。
     * 
     * @param columnName
     *            カラム名
     * @return カラム記述
     * @throws ColumnDescNotFoundRuntimeException
     *             カラム記述が存在しない場合
     */
    public ColumnDesc getColumnDesc(String columnName)
            throws ColumnDescNotFoundRuntimeException {
        if (columnDescMap.containsKey(columnName)) {
            return columnDescMap.get(columnName);
        }
        throw new ColumnDescNotFoundRuntimeException(columnName, getFullName());
    }

    /**
     * IDENTITYカラムを持っている場合{@code true}
     * 
     * @return IDENTITYカラムを持っている場合{@code true}
     */
    public boolean hasIdentityColumn() {
        for (ColumnDesc columnDesc : columnDescList) {
            if (columnDesc.isIdentity()) {
                return true;
            }
        }
        return false;
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

        /** 標準名 */
        protected String canonicalName;

        /**
         * 標準名を設定します。
         * 
         * @param canonicalName
         *            標準名
         */
        public void setCanonicalName(String canonicalName) {
            this.canonicalName = canonicalName;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((canonicalName == null) ? 0 : canonicalName.hashCode());
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
            if (canonicalName == null) {
                if (other.canonicalName != null) {
                    return false;
                }
            } else if (!canonicalName.equals(other.canonicalName)) {
                return false;
            }
            return true;
        }
    }

}
