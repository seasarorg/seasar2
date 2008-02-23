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
package org.seasar.extension.jdbc.gen.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * データベースのテーブル記述です。
 * 
 * @author taedium
 */
public class DbTableDesc {

    /** 名前 */
    protected String name;

    /** スキーマ名 */
    protected String schema;

    /** カラム記述のリスト */
    protected List<DbColumnDesc> columnDescList = new ArrayList<DbColumnDesc>();

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
     * カラム記述を追加します。
     * 
     * @param columnDesc
     *            カラム記述
     */
    public void addColumnDesc(DbColumnDesc columnDesc) {
        columnDescList.add(columnDesc);
    }

    /**
     * カラム記述のリストを返します。
     * 
     * @return カラム記述のリスト
     */
    public List<DbColumnDesc> getColumnDescList() {
        return Collections.unmodifiableList(columnDescList);
    }
}
