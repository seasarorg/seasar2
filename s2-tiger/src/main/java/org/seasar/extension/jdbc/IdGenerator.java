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
package org.seasar.extension.jdbc;

import java.sql.Statement;

import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;


/**
 * 識別子を自動生成するIDジェネレータのインターフェースです。
 * 
 * @author koichik
 */
public interface IdGenerator {

    /**
     * このIDジェネレータがバッチ更新に対応している場合は<code>true</code>を返します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @return このIDジェネレータがバッチ更新に対応している場合は<code>true</code>
     */
    boolean supportBatch(JdbcManagerImplementor jdbcManager);

    /**
     * 生成された識別子の値を{@link Statement#getGeneratedKeys()}で取得する場合は<code>true</code>を返します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @return 生成された識別子の値を{@link Statement#getGeneratedKeys()}で取得する場合は<code>true</code>
     */
    boolean useGetGeneratedKeys(JdbcManagerImplementor jdbcManager);

    /**
     * 自動生成される識別子をINSERT文に含める場合は<code>true</code>を返します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @return 自動生成される識別子をINSERT文に含める場合は<code>true</code>
     */
    boolean isInsertInto(JdbcManagerImplementor jdbcManager);

    /**
     * INSERTの実行前処理を行います。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param entity
     *            INSERT対象のエンティティ
     * @param sqlLogger
     *            SQLのロガー
     * @return INSERT文に渡すバインド変数の値
     */
    Object preInsert(JdbcManagerImplementor jdbcManager, Object entity,
            SqlLogger sqlLogger);

    /**
     * INSERTの実行後処理を行います。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param entity
     *            INSERT対象のエンティティ
     * @param statement
     *            INSERT文を実行した{@link Statement}
     * @param sqlLogger
     *            SQLのロガー
     */
    void postInsert(JdbcManagerImplementor jdbcManager, Object entity,
            Statement statement, SqlLogger sqlLogger);

}
