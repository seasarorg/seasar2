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
package org.seasar.extension.persistence;

import javax.persistence.UniqueConstraint;

/**
 * Tableのメタデータです。
 * 
 * @author higa
 * 
 */
public class TableMeta {

    private String name;

    private String catalog;

    private String schema;

    private UniqueConstraint[] uniqueConstraints;

    /**
     * 名前を返します。
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 名前を設定します。
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * カタログを返します。
     * 
     * @return catalog.
     */
    public String getCatalog() {
        return catalog;
    }

    /**
     * カタログを設定します。
     * 
     * @param catalog
     */
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    /**
     * スキーマを返します。
     * 
     * @return schema.
     */
    public String getSchema() {
        return schema;
    }

    /**
     * スキーマを設定します。
     * 
     * @param schema
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * <code>UniqueConstraint</code>を返します。
     * 
     * @return uniqueConstraints.
     */
    public UniqueConstraint[] getUniqueConstraints() {
        return uniqueConstraints;
    }

    /**
     * <code>UniqueConstraint</code>を設定します。
     * 
     * @param uniqueConstraints
     */
    public void setUniqueConstraints(UniqueConstraint[] uniqueConstraints) {
        this.uniqueConstraints = uniqueConstraints;
    }
}