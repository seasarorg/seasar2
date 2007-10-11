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

import java.util.List;

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
     * SQLバッチ更新を返します。
     * 
     * @param sql
     *            SQL
     * @param paramClasses
     *            パラメータのクラスの配列
     * @return SQLバッチ更新
     */
    SqlBatchUpdate updateBatchBySql(String sql, Class<?>... paramClasses);

    /**
     * SQLプロシージャ呼び出しを返します。
     * 
     * @param sql
     *            SQL
     * @return SQLプロシージャ呼び出し
     * @see #callBySql(String, Object)
     */
    SqlProcedureCall callBySql(String sql);

    /**
     * SQLプロシージャ呼び出しを返します。
     * 
     * @param sql
     *            SQL
     * @param parameter
     *            <p>
     *            パラメータです。
     *            </p>
     *            <p>
     *            INパラメータが1つしかない場合は、数値や文字列などを直接指定します。 それ以外は、JavaBeansを指定します。
     *            </p>
     *            <p>
     *            プロシージャを呼び出すバインド変数の順番にJavaBeansのフィールドを定義します。
     *            フィールド名が_OUTで終わっている場合<code>OUT</code>パラメータになります。
     *            フィールド名が_IN_OUTで終わっている場合<code>IN OUT</code>パラメータになります。
     *            フィールド名が_INで終わっている場合<code>IN</code>パラメータになります。
     *            フィールド名が_OUT、_IN_OUT、_INで終わっていない場合は、<code>IN</code>パラメータになります。
     *            </p>
     *            <p>
     *            プロシージャが結果セットを返す場合、フィールドの型は<code>List<レコードの型></code>にします。
     *            </p>
     *            <p>
     *            JavaBeansの場合、継承もとのクラスのフィールドは認識しません。
     *            </p>
     * @return SQLプロシージャ呼び出し
     */
    SqlProcedureCall callBySql(String sql, Object parameter);

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
     * SQLファイル更新を作成します。
     * 
     * @param path
     *            SQLファイルのパス
     * @return SQLファイル更新
     * @see #updateBySqlFile(String, Object)
     */
    SqlFileUpdate updateBySqlFile(String path);

    /**
     * SQLファイル更新を作成します。
     * 
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
     * @return SQLファイル更新
     * @see SqlFileUpdate
     */
    SqlFileUpdate updateBySqlFile(String path, Object parameter);

    /**
     * SQLバッチファイル更新を作成します。
     * 
     * @param <T>
     *            パラメータの型です。
     * @param path
     *            SQLファイルのパス
     * @param params
     *            <p>
     *            パラメータのリスト。
     *            </p>
     *            <p>
     *            パラメータが1つしかない場合は、値のリストを指定します。 パラメータが複数ある場合は、JavaBeansを作って、
     *            プロパティ名をSQLファイルのバインド変数名とあわせ、JavaBeansのリストを指定します。
     *            JavaBeansはpublicフィールドで定義することもできます。
     *            </p>
     * @return SQLバッチファイル更新
     * @see SqlFileBatchUpdate
     */
    <T> SqlFileBatchUpdate<T> updateBatchBySqlFile(String path, List<T> params);

    /**
     * SQLバッチファイル更新を作成します。
     * 
     * @param <T>
     *            パラメータの型です。
     * @param path
     *            SQLファイルのパス
     * @param params
     *            <p>
     *            パラメータの配列。
     *            </p>
     *            <p>
     *            パラメータが1つしかない場合は、値の配列を指定します。 パラメータが複数ある場合は、JavaBeansを作って、
     *            プロパティ名をSQLファイルのバインド変数名とあわせ、JavaBeansの配列を指定します。
     *            JavaBeansはpublicフィールドで定義することもできます。
     *            </p>
     * @return SQLバッチファイル更新
     * @see SqlFileBatchUpdate
     */
    <T> SqlFileBatchUpdate<T> updateBatchBySqlFile(String path, T... params);

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
     * 自動挿入を作成します。
     * 
     * @param <T>
     *            挿入するエンティティの型です。
     * @param entity
     *            エンティティ
     * @return 自動挿入
     */
    <T> AutoInsert<T> insert(T entity);

    /**
     * 自動バッチ挿入を作成します。
     * 
     * @param <T>
     *            挿入するエンティティの型です。
     * @param entities
     *            エンティティのリスト
     * @return 自動バッチ挿入
     */
    <T> AutoBatchInsert<T> insert(List<T> entities);

    /**
     * 自動バッチ挿入を作成します。
     * 
     * @param <T>
     *            挿入するエンティティの型です。
     * @param entities
     *            エンティティの並び
     * @return 自動バッチ挿入
     */
    <T> AutoBatchInsert<T> insert(T... entities);

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
     * 自動バッチ更新を作成します。
     * 
     * @param <T>
     *            更新するエンティティの型です。
     * @param entities
     *            エンティティのリスト
     * @return 自動バッチ更新
     */
    <T> AutoBatchUpdate<T> update(List<T> entities);

    /**
     * 自動バッチ更新を作成します。
     * 
     * @param <T>
     *            更新するエンティティの型です。
     * @param entities
     *            エンティティの並び
     * @return 自動バッチ更新
     */
    <T> AutoBatchUpdate<T> update(T... entities);

    /**
     * 自動削除を作成します。
     * 
     * @param <T>
     *            削除するエンティティの型です。
     * @param entity
     *            エンティティ
     * @return 自動更新
     */
    <T> AutoDelete<T> delete(T entity);

    /**
     * 自動バッチ削除を作成します。
     * 
     * @param <T>
     *            削除するエンティティの型です。
     * @param entities
     *            エンティティのリスト
     * @return 自動バッチ更新
     */
    <T> AutoBatchDelete<T> delete(List<T> entities);

    /**
     * 自動バッチ削除を作成します。
     * 
     * @param <T>
     *            削除するエンティティの型です。
     * @param entities
     *            エンティティの並び
     * @return 自動バッチ削除
     */
    <T> AutoBatchDelete<T> delete(T... entities);

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
