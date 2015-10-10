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
package org.seasar.extension.dataset;

/**
 * DataSetのカラムをあらわすインターフェースです。
 * 
 * @author higa
 * 
 */
public interface DataColumn {

    /**
     * カラム名を返します。
     * 
     * @return カラム名
     */
    String getColumnName();

    /**
     * カラムの位置を返します。
     * 
     * @return カラムの位置
     */
    int getColumnIndex();

    /**
     * カラムの型を返します。
     * 
     * @return カラムの型
     */
    ColumnType getColumnType();

    /**
     * カラムの型を設定します。
     * 
     * @param columnType
     *            カラムの型
     */
    void setColumnType(ColumnType columnType);

    /**
     * プライマリキーかどうかを返します。
     * 
     * @return プライマリキーかどうか
     */
    boolean isPrimaryKey();

    /**
     * プライマリーキーかどうかを設定します。
     * 
     * @param primaryKey
     *            プライマリキーかどうか
     */
    void setPrimaryKey(boolean primaryKey);

    /**
     * 更新可能かどうかを返します。
     * 
     * @return 更新可能かどうか
     */
    boolean isWritable();

    /**
     * 更新可能かどうかを設定します。
     * 
     * @param writable
     *            更新可能かどうか
     */
    void setWritable(boolean writable);

    /**
     * フォーマットパターンを返します。
     * 
     * @return フォーマットパターン
     */
    String getFormatPattern();

    /**
     * フォーマットパターンを設定します。
     * 
     * @param formatPattern
     *            フォーマットパターン
     */
    void setFormatPattern(String formatPattern);

    /**
     * 値を適切な型に変換します。
     * 
     * @param value
     *            値
     * @return 変換後の値
     */
    Object convert(Object value);
}