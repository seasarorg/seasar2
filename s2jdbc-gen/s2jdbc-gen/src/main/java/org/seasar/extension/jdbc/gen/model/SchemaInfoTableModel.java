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

/**
 * スキーマ情報テーブルのモデルです。
 * 
 * @author taedium
 */
public class SchemaInfoTableModel extends DdlModel {

    /** カラム名 */
    protected String columnName;

    /** カラム定義 */
    protected String columnDefinition;

    /** バージョン番号 */
    protected int versionNo;

    /**
     * カラム名を返します。
     * 
     * @return カラム名
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * カラム名を設定します。
     * 
     * @param columnName
     *            カラム名
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * カラム定義を返します。
     * 
     * @return カラム定義
     */
    public String getColumnDefinition() {
        return columnDefinition;
    }

    /**
     * カラム定義を設定します。
     * 
     * @param columnDefinition
     *            カラム定義
     */
    public void setColumnDefinition(String columnDefinition) {
        this.columnDefinition = columnDefinition;
    }

    /**
     * バージョン番号を返します。
     * 
     * @return バージョン番号
     */
    public int getVersionNo() {
        return versionNo;
    }

    /**
     * バージョン番号を設定します。
     * 
     * @param versionNo
     *            バージョン番号
     */
    public void setVersionNo(int versionNo) {
        this.versionNo = versionNo;
    }

}
