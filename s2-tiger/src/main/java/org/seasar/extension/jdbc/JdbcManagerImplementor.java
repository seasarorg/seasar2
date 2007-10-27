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
package org.seasar.extension.jdbc;

import javax.sql.DataSource;

import org.seasar.framework.convention.PersistenceConvention;

/**
 * JDBCマネージャの内部用のインターフェースです。
 * 
 * @author higa
 * 
 */
public interface JdbcManagerImplementor {

    /**
     * JDBCコンテキストを返します。
     * 
     * @return JDBCコンテキスト
     */
    JdbcContext getJdbcContext();

    /**
     * データソースを返します。
     * 
     * @return データソース
     */
    DataSource getDataSource();

    /**
     * データベースの方言を返します。
     * 
     * @return データベースの方言
     */
    DbmsDialect getDialect();

    /**
     * エンティティメタデータファクトリを返します。
     * 
     * @return エンティティメタデータファクトリ
     */
    EntityMetaFactory getEntityMetaFactory();

    /**
     * 永続化層の規約を返します。
     * 
     * @return 永続化層の規約
     */
    PersistenceConvention getPersistenceConvention();
}
