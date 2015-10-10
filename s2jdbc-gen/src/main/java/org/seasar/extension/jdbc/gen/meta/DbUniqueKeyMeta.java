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
package org.seasar.extension.jdbc.gen.meta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * データベースの一意キーメタデータです。
 * 
 * @author taedium
 */
public class DbUniqueKeyMeta {

    /** 名前 */
    protected String name;

    /** 主キーの場合{@code true} */
    protected boolean primaryKey;

    /** カラム名 */
    protected List<String> columnNameList = new ArrayList<String>();

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
     * 主キーの場合{@code true}、そうでない場合{@code false}を返します。
     * 
     * @return 主キーの場合{@code true}、そうでない場合{@code false}
     */
    public boolean isPrimaryKey() {
        return primaryKey;
    }

    /**
     * 主キーの場合{@code true}、そうでない場合{@code false}を設定します。
     * 
     * @param primaryKey
     *            主キーの場合{@code true}、そうでない場合{@code false}
     */
    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

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
    }

    /**
     * 複合一意キーの場合{@code true}、そうでない場合{@code false}を返します。
     * 
     * @return 複合一意キーの場合{@code true}、そうでない場合{@code false}
     */
    public boolean isComposite() {
        return columnNameList.size() > 1;
    }

}
