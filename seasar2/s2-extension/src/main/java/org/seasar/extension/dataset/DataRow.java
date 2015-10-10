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

import org.seasar.extension.jdbc.ColumnNotFoundRuntimeException;

/**
 * DataSetの業をあらわすインターフェースです。
 * 
 * @author higa
 * 
 */
public interface DataRow {

    /**
     * 値を返します。
     * 
     * @param index
     *            位置
     * @return 値
     */
    Object getValue(int index);

    /**
     * 値を返します。
     * 
     * @param columnName
     *            カラム名
     * @return 値
     * @throws ColumnNotFoundRuntimeException
     *             カラムが見つからなかった場合
     */
    Object getValue(String columnName) throws ColumnNotFoundRuntimeException;

    /**
     * 値を設定します。
     * 
     * @param index
     *            位置
     * @param value
     *            値
     */
    void setValue(int index, Object value);

    /**
     * 値を設定します。
     * 
     * @param columnName
     *            カラム名
     * @param value
     *            値
     * @throws ColumnNotFoundRuntimeException
     *             カラムが見つからなかった場合
     */
    void setValue(String columnName, Object value)
            throws ColumnNotFoundRuntimeException;

    /**
     * 削除します。
     */
    void remove();

    /**
     * テーブルを返します。
     * 
     * @return テーブル
     */
    DataTable getTable();

    /**
     * 行の状態を返します。
     * 
     * @return 行の状態
     */
    RowState getState();

    /**
     * 行の状態を設定します。
     * 
     * @param rowState
     *            行の状態
     */
    void setState(RowState rowState);

    /**
     * 他から値をコピーします。
     * 
     * @param source
     *            他のデータ
     */
    void copyFrom(Object source);
}
