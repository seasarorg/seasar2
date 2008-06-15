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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * データベースのテーブルメタデータです。
 * 
 * @author taedium
 */
public class DbTableMeta {

    /** 名前 */
    protected String name;

    /** スキーマ名 */
    protected String schema;

    /** カラムのメタデータのリスト */
    protected List<DbColumnMeta> columnMetaList = new ArrayList<DbColumnMeta>();

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
     * スキーマ名を返します
     * 
     * @return スキーマ名
     */
    public String getSchema() {
        return schema;
    }

    /**
     * スキーマ名を設定します。
     * 
     * @param schema
     *            スキーマ名
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * カラムのメタデータを追加します。
     * 
     * @param columnDesc
     *            カラム記述
     */
    public void addColumnMeta(DbColumnMeta columnDesc) {
        columnMetaList.add(columnDesc);
    }

    /**
     * カラムのメタデータのリストを返します。
     * 
     * @return カラムのメタデータのリスト
     */
    public List<DbColumnMeta> getColumnMetaList() {
        return Collections.unmodifiableList(columnMetaList);
    }
}
