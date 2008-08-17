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
package org.seasar.extension.jdbc.gen.meta;

/**
 * データベースのカラムメタデータです。
 * 
 * @author taedium
 */
public class DbColumnMeta {

    /** 名前 */
    protected String name;

    /** SQL型 */
    protected int sqlType;

    /** 型名 */
    protected String typeName;

    /** 長さ */
    protected int length;

    /** スケール */
    protected int scale;

    /** NULL可能の場合{@code true} */
    protected boolean nullable;

    /** 主キーの場合{@code true} */
    protected boolean primaryKey;

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
     * SQL型を返します。
     * 
     * @return SQL型
     */
    public int getSqlType() {
        return sqlType;
    }

    /**
     * SQL型をセットします。
     * 
     * @param sqlType
     *            SQL型
     */
    public void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }

    /**
     * 型名を返します。
     * 
     * @return 型名
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * SQL型の名前を設定します。
     * 
     * @param typeName
     *            SQL型の名前
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * 長さを返します。
     * 
     * @return 長さ
     */
    public int getLength() {
        return length;
    }

    /**
     * 長さを設定します。
     * 
     * @param length
     *            長さ
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * スケールを返します。
     * 
     * @return スケール
     */
    public int getScale() {
        return scale;
    }

    /**
     * スケールを設定します。
     * 
     * @param scale
     *            スケール
     */
    public void setScale(int scale) {
        this.scale = scale;
    }

    /**
     * NULL可能の場合{@code true}を返します。
     * 
     * @return NULL可能の場合{@code true}、そうでない場合{@code false}
     */
    public boolean isNullable() {
        return nullable;
    }

    /**
     * NULL可能の場合{@code true}を設定します。
     * 
     * @param nullable
     *            NULL可能の場合{@code true}
     */
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    /**
     * 主キーの場合{@code true}を返します。
     * 
     * @return 主キーの場合{@code true}、そうでない場合{@code false}
     */
    public boolean isPrimaryKey() {
        return primaryKey;
    }

    /**
     * 主キーの場合 {@code true}を設定します。
     * 
     * @param primaryKey
     *            主キーの場合 {@code true}
     * 
     */
    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

}
