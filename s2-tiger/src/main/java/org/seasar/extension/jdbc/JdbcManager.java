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

/**
 * <code>JDBC</code>による<code>SQL</code>の実行を管理するインターフェースです。
 * 
 * @author higa
 * 
 */
public interface JdbcManager {

    /**
     * SQL検索を作成します。
     * 
     * @param <T>
     *            戻り値のベースの型です。
     * @param baseClass
     *            ベースクラス
     * @param sql
     *            SQL
     * @param params
     *            パラメータの配列
     * @return SQL検索
     * @see SqlSelect
     */
    <T> SqlSelect<T> selectBySql(Class<T> baseClass, String sql,
            Object... params);

    /**
     * SQL更新を返します。
     * 
     * @param sql
     *            SQL
     * @param paramClasses
     *            パラメータのクラスの配列
     * @return SQL更新
     */
    SqlUpdate updateBySql(String sql, Class<?>... paramClasses);

    /**
     * SQLファイル検索を作成します。
     * 
     * @param <T>
     *            戻り値のベースの型です。
     * @param baseClass
     *            ベースクラス
     * @param path
     *            SQLファイルのパス
     * @return SQLファイル検索
     * @see #selectBySqlFile(Class, String, Object)
     */
    <T> SqlFileSelect<T> selectBySqlFile(Class<T> baseClass, String path);

    /**
     * SQLファイル検索を作成します。
     * 
     * @param <T>
     *            戻り値のベースの型です。
     * @param baseClass
     *            ベースクラス
     * @param path
     *            SQLファイルのパス
     * @param parameter
     *            <p>
     *            パラメータ。
     *            </p>
     *            <p>
     *            パラメータが1つしかない場合は、値を直接指定します。 パラメータが複数ある場合は、JavaBeansを作って、
     *            プロパティ名をSQLファイルのバインド変数名とあわせます。
     *            JavaBeansはpublicフィールドで定義することもできます。
     *            </p>
     * @return SQLファイル検索
     * @see SqlFileSelect
     */
    <T> SqlFileSelect<T> selectBySqlFile(Class<T> baseClass, String path,
            Object parameter);

    /**
     * 自動検索を作成します。
     * 
     * @param <T>
     *            戻り値のベースの型です。
     * @param baseClass
     *            ベースクラス
     * @return 自動検索
     */
    <T> AutoSelect<T> from(Class<T> baseClass);

    /**
     * 自動更新を作成します。
     * 
     * @param <T>
     *            更新するエンティティの型です。
     * @param entity
     *            エンティティ
     * @return 自動更新
     */
    <T> AutoUpdate<T> update(T entity);

    /**
     * JDBCコンテキストを返します。
     * 
     * @return JDBCコンテキスト
     */
    JdbcContext getJdbcContext();

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
}
