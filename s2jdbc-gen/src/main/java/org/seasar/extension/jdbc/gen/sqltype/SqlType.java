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
package org.seasar.extension.jdbc.gen.sqltype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * SQL型です。
 * <p>
 * JDBCのSQL型、つまり{@link Types}の定数に対応します。
 * </p>
 * 
 * @author taedium
 */
public interface SqlType {

    /**
     * データ型を返します。
     * 
     * @param length
     *            長さ
     * @param precision
     *            精度
     * @param scale
     *            スケール
     * @param identity
     *            IDENTITYカラムの場合{@code true}
     * @return データ型
     */
    String getDataType(int length, int precision, int scale, boolean identity);

    /**
     * {@link ResultSet}から値を文字列として取得します。
     * 
     * @param resultSet
     *            結果セット
     * @param index
     *            順序
     * @return 値の文字列
     * @throws SQLException
     *             結果セットから取得できない場合
     */
    String getValue(ResultSet resultSet, int index) throws SQLException;

    /**
     * {@link PreparedStatement}に値をバインドします。
     * 
     * @param ps
     *            準備されたステートメント
     * @param index
     *            順序
     * @param value
     *            文字列としての値
     * @throws SQLException
     *             バインドできない場合
     */
    void bindValue(PreparedStatement ps, int index, String value)
            throws SQLException;
}
