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

import javax.persistence.GenerationType;
import javax.persistence.TemporalType;

/**
 * RDBMSごとの方言を扱うインタフェースです。
 * 
 * @author taedium
 */
public interface GenDialect {

    /**
     * デフォルトのスキーマ名を返します。
     * 
     * @param userName
     *            ユーザー名
     * @return スキーマ名
     */
    String getDefaultSchemaName(String userName);

    /**
     * ユーザーテーブルならば{@code true}を返します。
     * 
     * @param tableName
     *            テーブル名
     * @return ユーザーテーブルならば{@code true}
     */
    boolean isUserTable(String tableName);

    /**
     * {@code LOB}型であれば{@code true}を返します。
     * 
     * @param sqlType
     *            SQL型
     * @param typeName
     *            型名
     * @return LOB型であれば{@code true}
     */
    boolean isLobType(int sqlType, String typeName);

    /**
     * 時間型を返します。
     * 
     * @param sqlType
     *            SQL型
     * @param typeName
     *            型名
     * @return 時間型
     */
    TemporalType getTemporalType(int sqlType, String typeName);

    /**
     * Javaの型を返します。
     * 
     * @param sqlType
     *            SQL型
     * @param typeName
     *            型名
     * @param nullable
     *            NULLが可能であれば{@code true}
     * @return Javaの型
     */
    Class<?> getJavaType(int sqlType, String typeName, boolean nullable);

    /**
     * データ型を返します。
     * 
     * @param sqlType
     *            JDBCのSQL型
     * @return データ型
     */
    DataType getDataType(int sqlType);

    /**
     * デフォルトの{@link GenerationType}を返します。
     * 
     * @return デフォルトの{@link GenerationType}
     */
    GenerationType getDefaultGenerationType();

}
