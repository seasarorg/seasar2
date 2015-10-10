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
 * テーブル用のメタデータです。
 * 
 * @author higa
 * 
 */
public class TableMeta {

    /**
     * 名前です。
     */
    protected String name;

    /**
     * カタログです。
     */
    protected String catalog;

    /**
     * スキーマです。
     */
    protected String schema;

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
     * カタログを返します。
     * 
     * @return カタログ
     */
    public String getCatalog() {
        return catalog;
    }

    /**
     * カタログを設定します。
     * 
     * @param catalog
     *            カタログ
     */
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    /**
     * スキーマを返します。
     * 
     * @return スキーマ
     */
    public String getSchema() {
        return schema;
    }

    /**
     * スキーマを設定します。
     * 
     * @param schema
     *            スキーマ
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * カタログやスキーマを含んだ完全な名前を返します。
     * 
     * @return カタログやスキーマを含んだ完全な名前
     */
    public String getFullName() {
        StringBuilder buf = new StringBuilder();
        if (catalog != null) {
            buf.append(catalog).append(".");
        }
        if (schema != null) {
            buf.append(schema).append(".");
        }
        return buf.append(name).toString();
    }
}