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
 * ダンプファイルのモデルです。
 * 
 * @author taedium
 */
public class DumpModel {

    /** 名前 */
    protected String name;

    /** データの区切り文字 */
    protected char delimiter;

    /** カラム名のリスト */
    protected List<String> columnNameList = new ArrayList<String>();

    /** 行のリスト */
    protected List<List<String>> rowList = new ArrayList<List<String>>();

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
     * 区切り文字を返します。
     * 
     * @return 区切り文字
     */
    public char getDelimiter() {
        return delimiter;
    }

    /**
     * 区切り文字を設定します。
     * 
     * @param delimiter
     *            区切り文字
     */
    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
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
     * 行を追加します。
     * 
     * @param row
     *            行
     */
    public void addRow(List<String> row) {
        this.rowList.add(row);
    }

    /**
     * 行のリストを返します。
     * 
     * @return 行のリスト
     */
    public List<List<String>> getRowList() {
        return Collections.unmodifiableList(rowList);
    }

}
