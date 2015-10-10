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

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import org.seasar.extension.jdbc.annotation.InOut;
import org.seasar.extension.jdbc.annotation.Out;
import org.seasar.extension.jdbc.parameter.Parameter;

/**
 * <code>JDBC</code>による<code>SQL</code>の実行を管理するインターフェースです。
 * 
 * @author higa
 * 
 */
public interface JdbcManager {

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
     *            <p>
     *            パラメータの配列の要素が{@link Date}、{@link Calendar}のいずれか場合、{@link Parameter}に定義されたメソッドによりパラメータの時制を指定できます。
     *            </p>
     *            <p>
     *            パラメータの配列の要素が{@link String}、<code>byte[]</code>、{@link Serializable}のいずれかの場合、{@link Parameter}に定義されたメソッドによりパラメータをラージオブジェクトとして扱えます。
     *            </p>
     * @return SQL検索
     * @see SqlSelect
     */
    <T> SqlSelect<T> selectBySql(Class<T> baseClass, String sql,
            Object... params);

    /**
     * {@link #selectBySql}で実行可能なSQLが返す結果セットの行数を返します。
     * <p>
     * このメソッドは通常、<code>select count(*) from (<var>SQL</var>)</code>を
     * 実行した結果を返します。
     * </p>
     * 
     * @param sql
     *            SQL
     * @param params
     *            パラメータの配列
     *            <p>
     *            パラメータの配列の要素が{@link Date}、{@link Calendar}のいずれか場合、{@link Parameter}に定義されたメソッドによりパラメータの時制を指定できます。
     *            </p>
     *            <p>
     *            パラメータの配列の要素が{@link String}、<code>byte[]</code>、{@link Serializable}のいずれかの場合、{@link Parameter}に定義されたメソッドによりパラメータをラージオブジェクトとして扱えます。
     *            </p>
     * @return SQLが返す結果セットの行数
     * @see DbmsDialect#convertGetCountSql(String)
     */
    long getCountBySql(String sql, Object... params);

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
     *            <p>
     *            パラメータが1つで型が{@link Date}、{@link Calendar}のいずれか場合、{@link Parameter}に定義されたメソッドによりパラメータの時制を指定できます。
     *            </p>
     *            <p>
     *            パラメータが1つで型が{@link String}、<code>byte[]</code>、{@link Serializable}のいずれかの場合、{@link Parameter}に定義されたメソッドによりパラメータをラージオブジェクトとして扱えます。
     *            </p>
     * @return SQLファイル検索
     * @see SqlFileSelect
     * @see Parameter
     */
    <T> SqlFileSelect<T> selectBySqlFile(Class<T> baseClass, String path,
            Object parameter);

    /**
     * {@link #selectBySql}で実行可能なSQLが返す結果セットの行数を返します。
     * <p>
     * このメソッドは通常、<code>select count(*) from (<var>SQL</var>)</code>を
     * 実行した結果を返します。
     * </p>
     * 
     * @param path
     *            SQLファイルのパス
     * @return SQLが返す結果セットの行数
     * @see DbmsDialect#convertGetCountSql(String)
     */
    long getCountBySqlFile(String path);

    /**
     * {@link #selectBySql}で実行可能なSQLが返す結果セットの行数を返します。
     * <p>
     * このメソッドは通常、<code>select count(*) from (<var>SQL</var>)</code>を
     * 実行した結果を返します。
     * </p>
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
     *            <p>
     *            パラメータが1つで型が{@link Date}、{@link Calendar}のいずれか場合、{@link Parameter}に定義されたメソッドによりパラメータの時制を指定できます。
     *            </p>
     *            <p>
     *            パラメータが1つで型が{@link String}、<code>byte[]</code>、{@link Serializable}のいずれかの場合、{@link Parameter}に定義されたメソッドによりパラメータをラージオブジェクトとして扱えます。
     *            </p>
     * @return SQLが返す結果セットの行数
     * @see DbmsDialect#convertGetCountSql(String)
     */
    long getCountBySqlFile(String path, Object parameter);

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
     *            エンティティの並び
     * @return 自動バッチ挿入
     */
    <T> AutoBatchInsert<T> insertBatch(T... entities);

    /**
     * 自動バッチ挿入を作成します。
     * 
     * @param <T>
     *            挿入するエンティティの型です。
     * @param entities
     *            エンティティのリスト
     * @return 自動バッチ挿入
     */
    <T> AutoBatchInsert<T> insertBatch(List<T> entities);

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
     *            エンティティの並び
     * @return 自動バッチ更新
     */
    <T> AutoBatchUpdate<T> updateBatch(T... entities);

    /**
     * 自動バッチ更新を作成します。
     * 
     * @param <T>
     *            更新するエンティティの型です。
     * @param entities
     *            エンティティのリスト
     * @return 自動バッチ更新
     */
    <T> AutoBatchUpdate<T> updateBatch(List<T> entities);

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
     *            パラメータが1つで値が<code>null</code>ではない場合は、値を直接指定します。
     *            それ以外の場合は、JavaBeansを作って、 プロパティ名をSQLファイルのバインド変数名とあわせます。
     *            JavaBeansはpublicフィールドで定義することもできます。
     *            </p>
     *            <p>
     *            パラメータが1つで型が{@link Date}、{@link Calendar}のいずれか場合、{@link Parameter}に定義されたメソッドによりパラメータの時制を指定できます。
     *            </p>
     *            <p>
     *            パラメータが1つで型が{@link String}、<code>byte[]</code>、{@link Serializable}のいずれかの場合、{@link Parameter}に定義されたメソッドによりパラメータをラージオブジェクトとして扱えます。
     *            </p>
     * @return SQLファイル更新
     * @see SqlFileUpdate
     * @see
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
     *            パラメータの配列。
     *            </p>
     *            <p>
     *            パラメータが1つで値が<code>null</code>にならない場合は、 値の可変長引数を指定します。
     *            それ以外の場合は、JavaBeansを作って、
     *            プロパティ名をSQLファイルのバインド変数名とあわせ、JavaBeansの可変長引数を指定します。
     *            JavaBeansはpublicフィールドで定義することもできます。
     *            </p>
     * @return SQLバッチファイル更新
     * @see SqlFileBatchUpdate
     */
    <T> SqlFileBatchUpdate<T> updateBatchBySqlFile(String path, T... params);

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
     *            パラメータが1つで値が<code>null</code>にならない場合は、 値のリストを指定します。
     *            それ以外の場合は、JavaBeansを作って、
     *            プロパティ名をSQLファイルのバインド変数名とあわせ、JavaBeansのリストを指定します。
     *            JavaBeansはpublicフィールドで定義することもできます。
     *            </p>
     * @return SQLバッチファイル更新
     * @see SqlFileBatchUpdate
     */
    <T> SqlFileBatchUpdate<T> updateBatchBySqlFile(String path, List<T> params);

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
     *            エンティティの並び
     * @return 自動バッチ削除
     */
    <T> AutoBatchDelete<T> deleteBatch(T... entities);

    /**
     * 自動バッチ削除を作成します。
     * 
     * @param <T>
     *            削除するエンティティの型です。
     * @param entities
     *            エンティティのリスト
     * @return 自動バッチ更新
     */
    <T> AutoBatchDelete<T> deleteBatch(List<T> entities);

    /**
     * 自動プロシージャ呼び出しを返します。
     * 
     * @param procedureName
     *            呼び出すストアドプロシージャの名前
     * @return 自動プロシージャ呼び出し
     * @see #call(String, Object)
     */
    AutoProcedureCall call(String procedureName);

    /**
     * 自動プロシージャ呼び出しを返します。
     * 
     * @param procedureName
     *            呼び出すストアドプロシージャの名前
     * @param parameter
     *            <p>
     *            パラメータです。
     *            </p>
     *            <p>
     *            INパラメータが1つで値が<code>null</code>にならない場合は、数値や文字列などを直接指定します。
     *            それ以外は、JavaBeansを指定します。
     *            </p>
     *            <p>
     *            プロシージャを呼び出すバインド変数の順番にJavaBeansのフィールドを定義します。 <code>OUT</code>パラメータのフィールドには{@link Out}アノテーションを指定します。
     *            <code>IN OUT</code>パラメータのフィールドには{@link InOut}アノテーションを指定します。
     *            いずれのアノテーションも付けられていない場合は、<code>IN</code>パラメータになります。
     *            </p>
     *            <p>
     *            プロシージャが結果セットを返す場合、フィールドの型は<code>List&lt;レコードの型&gt;</code>にします。
     *            </p>
     *            <p>
     *            JavaBeansの場合、継承もとのクラスのフィールドは認識しません。
     *            </p>
     *            <p>
     *            パラメータが1つで型が{@link Date}、{@link Calendar}のいずれか場合、{@link Parameter}に定義されたメソッドによりパラメータの時制を指定できます。
     *            </p>
     *            <p>
     *            パラメータが1つで型が{@link String}、<code>byte[]</code>、{@link Serializable}のいずれかの場合、{@link Parameter}に定義されたメソッドによりパラメータをラージオブジェクトとして扱えます。
     *            </p>
     * @return 自動プロシージャ呼び出し
     * @see Parameter
     */
    AutoProcedureCall call(String procedureName, Object parameter);

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
     *            INパラメータが1つで値が<code>null</code>にならない場合は、数値や文字列などを直接指定します。
     *            それ以外は、JavaBeansを指定します。
     *            </p>
     *            <p>
     *            プロシージャを呼び出すバインド変数の順番にJavaBeansのフィールドを定義します。 <code>OUT</code>パラメータのフィールドには{@link Out}アノテーションを指定します。
     *            <code>IN OUT</code>パラメータのフィールドには{@link InOut}アノテーションを指定します。
     *            いずれのアノテーションも付けられていない場合は、<code>IN</code>パラメータになります。
     *            </p>
     *            <p>
     *            プロシージャが結果セットを返す場合、フィールドの型は<code>List&lt;レコードの型&gt;</code>にします。
     *            </p>
     *            <p>
     *            JavaBeansの場合、継承もとのクラスのフィールドは認識しません。
     *            </p>
     *            <p>
     *            パラメータが1つで型が{@link Date}、{@link Calendar}のいずれか場合、{@link Parameter}に定義されたメソッドによりパラメータの時制を指定できます。
     *            </p>
     *            <p>
     *            パラメータが1つで型が{@link String}、<code>byte[]</code>、{@link Serializable}のいずれかの場合、{@link Parameter}に定義されたメソッドによりパラメータをラージオブジェクトとして扱えます。
     *            </p>
     * @return SQLプロシージャ呼び出し
     */
    SqlProcedureCall callBySql(String sql, Object parameter);

    /**
     * SQLファイルプロシージャ呼び出しを返します。
     * 
     * @param path
     *            SQLファイルのパス
     * @return SQLファイルプロシージャ呼び出し
     * @see #callBySqlFile(String, Object)
     */
    SqlFileProcedureCall callBySqlFile(String path);

    /**
     * SQLファイルプロシージャ呼び出しを返します。
     * 
     * @param path
     *            SQLファイルのパス
     * @param parameter
     *            <p>
     *            パラメータです。
     *            </p>
     *            <p>
     *            INパラメータが1つで値が<code>null</code>にならない場合は、数値や文字列などを直接指定します。
     *            それ以外は、JavaBeansを指定します。
     *            </p>
     *            <p>
     *            プロシージャを呼び出すパラメータの順番にJavaBeansのフィールドを定義します。<br />
     *            <ul>
     *            <li>フィールド名が_OUTで終わっている場合<code>OUT</code>パラメータになります。</li>
     *            <li>フィールド名が_IN_OUTで終わっている場合<code>IN OUT</code>パラメータになります。</li>
     *            <li>フィールド名が_INで終わっている場合<code>IN</code>パラメータになります。</li>
     *            <li>フィールド名が_OUT、_IN_OUT、_INで終わっていない場合は、<code>IN</code>パラメータになります。</li>
     *            </ul>
     *            </p>
     *            <p>
     *            プロシージャが結果セットを返す場合、フィールドの型は<code>List<レコードの型></code>にします。
     *            </p>
     *            <p>
     *            JavaBeansの場合、継承もとのクラスのフィールドは認識しません。
     *            </p>
     *            <p>
     *            パラメータが1つで型が{@link Date}、{@link Calendar}のいずれか場合、{@link Parameter}に定義されたメソッドによりパラメータの時制を指定できます。
     *            </p>
     *            <p>
     *            パラメータが1つで型が{@link String}、<code>byte[]</code>、{@link Serializable}のいずれかの場合、{@link Parameter}に定義されたメソッドによりパラメータをラージオブジェクトとして扱えます。
     *            </p>
     * @return SQLファイルプロシージャ呼び出し
     * @see Parameter
     */
    SqlFileProcedureCall callBySqlFile(String path, Object parameter);

    /**
     * 自動ファンクション呼び出しを返します。
     * 
     * @param resultClass
     *            ファンクションの戻り値の型。ファンクションの戻り値が結果セットの場合はリストの要素のクラス
     * @param functionName
     *            呼び出すストアドファンクションの名前
     * @param <T>
     *            ファンクションの戻り値の型。ファンクションの戻り値が結果セットの場合はリストの要素のクラス
     * @return 自動ファンクション呼び出し
     * @see #call(Class, String, Object)
     */
    <T> AutoFunctionCall<T> call(Class<T> resultClass, String functionName);

    /**
     * 自動ファンクション呼び出しを返します。
     * 
     * @param resultClass
     *            ファンクションの戻り値の型。ファンクションの戻り値が結果セットの場合はリストの要素のクラス
     * @param functionName
     *            呼び出すストアドファンクションの名前
     * @param parameter
     *            <p>
     *            パラメータです。
     *            </p>
     *            <p>
     *            INパラメータが1つで値が<code>null</code>にならない場合は、数値や文字列などを直接指定します。
     *            それ以外は、JavaBeansを指定します。
     *            </p>
     *            <p>
     *            ファンクションを呼び出すバインド変数の順番にJavaBeansのフィールドを定義します。 <code>OUT</code>パラメータのフィールドには{@link Out}アノテーションを指定します。
     *            <code>IN OUT</code>パラメータのフィールドには{@link InOut}アノテーションを指定します。
     *            いずれのアノテーションも付けられていない場合は、<code>IN</code>パラメータになります。
     *            </p>
     *            <p>
     *            ファンクションが結果セットを返す場合、フィールドの型は<code>List&lt;レコードの型&gt;</code>にします。
     *            </p>
     *            <p>
     *            JavaBeansの場合、継承もとのクラスのフィールドは認識しません。
     *            </p>
     *            <p>
     *            パラメータが1つで型が{@link Date}、{@link Calendar}のいずれか場合、{@link Parameter}に定義されたメソッドによりパラメータの時制を指定できます。
     *            </p>
     *            <p>
     *            パラメータが1つで型が{@link String}、<code>byte[]</code>、{@link Serializable}のいずれかの場合、{@link Parameter}に定義されたメソッドによりパラメータをラージオブジェクトとして扱えます。
     *            </p>
     * @param <T>
     *            ファンクションの戻り値の型。ファンクションの戻り値が結果セットの場合はリストの要素のクラス
     * @return 自動ファンクション呼び出し
     * @see Parameter
     */
    <T> AutoFunctionCall<T> call(Class<T> resultClass, String functionName,
            Object parameter);

    /**
     * SQLファンクション呼び出しを返します。
     * 
     * @param resultClass
     *            ファンクションの戻り値の型。ファンクションの戻り値が結果セットの場合はリストの要素のクラス
     * @param sql
     *            SQL
     * @param <T>
     *            ファンクションの戻り値の型。ファンクションの戻り値が結果セットの場合はリストの要素のクラス
     * @return SQLファンクション呼び出し
     * @see #callBySql(Class, String, Object)
     */
    <T> SqlFunctionCall<T> callBySql(Class<T> resultClass, String sql);

    /**
     * SQLファンクション呼び出しを返します。
     * 
     * @param resultClass
     *            ファンクションの戻り値の型。ファンクションの戻り値が結果セットの場合はリストの要素のクラス
     * @param sql
     *            SQL
     * @param parameter
     *            <p>
     *            パラメータです。
     *            </p>
     *            <p>
     *            INパラメータが1つで値が<code>null</code>にならない場合は、数値や文字列などを直接指定します。
     *            それ以外は、JavaBeansを指定します。
     *            </p>
     *            <p>
     *            ファンクションを呼び出すバインド変数の順番にJavaBeansのフィールドを定義します。 <code>OUT</code>パラメータのフィールドには{@link Out}アノテーションを指定します。
     *            <code>IN OUT</code>パラメータのフィールドには{@link InOut}アノテーションを指定します。
     *            いずれのアノテーションも付けられていない場合は、<code>IN</code>パラメータになります。
     *            </p>
     *            <p>
     *            ファンクションが結果セットを返す場合、フィールドの型は<code>List&lt;レコードの型&gt;</code>にします。
     *            </p>
     *            <p>
     *            JavaBeansの場合、継承もとのクラスのフィールドは認識しません。
     *            </p>
     *            <p>
     *            パラメータが1つで型が{@link Date}、{@link Calendar}のいずれか場合、{@link Parameter}に定義されたメソッドによりパラメータの時制を指定できます。
     *            </p>
     *            <p>
     *            パラメータが1つで型が{@link String}、<code>byte[]</code>、{@link Serializable}のいずれかの場合、{@link Parameter}に定義されたメソッドによりパラメータをラージオブジェクトとして扱えます。
     *            </p>
     * @param <T>
     *            ファンクションの戻り値の型。ファンクションの戻り値が結果セットの場合はリストの要素のクラス
     * @return SQLファンクション呼び出し
     * @see Parameter
     */
    <T> SqlFunctionCall<T> callBySql(Class<T> resultClass, String sql,
            Object parameter);

    /**
     * SQLファイルファンクション呼び出しを返します。
     * 
     * @param resultClass
     *            ファンクションの戻り値の型。ファンクションの戻り値が結果セットの場合はリストの要素のクラス
     * @param path
     *            SQLファイルのパス
     * @param <T>
     *            ファンクションの戻り値の型。ファンクションの戻り値が結果セットの場合はリストの要素のクラス
     * @return SQLファイルファンクション呼び出し
     * @see #call(Class, String, Object)
     */
    <T> SqlFileFunctionCall<T> callBySqlFile(Class<T> resultClass, String path);

    /**
     * SQLファイルファンクション呼び出しを返します。
     * 
     * @param resultClass
     *            ファンクションの戻り値の型。ファンクションの戻り値が結果セットの場合はリストの要素のクラス
     * @param path
     *            SQLファイルのパス
     * @param parameter
     *            <p>
     *            パラメータです。
     *            </p>
     *            <p>
     *            INパラメータが1つで値が<code>null</code>にならない場合は、数値や文字列などを直接指定します。
     *            それ以外は、JavaBeansを指定します。
     *            </p>
     *            <p>
     *            ファンクションを呼び出すバインド変数の順番にJavaBeansのフィールドを定義します。 <code>OUT</code>パラメータのフィールドには{@link Out}アノテーションを指定します。
     *            <code>IN OUT</code>パラメータのフィールドには{@link InOut}アノテーションを指定します。
     *            いずれのアノテーションも付けられていない場合は、<code>IN</code>パラメータになります。
     *            </p>
     *            <p>
     *            ファンクションが結果セットを返す場合、フィールドの型は<code>List&lt;レコードの型&gt;</code>にします。
     *            </p>
     *            <p>
     *            JavaBeansの場合、継承もとのクラスのフィールドは認識しません。
     *            </p>
     *            <p>
     *            パラメータが1つで型が{@link Date}、{@link Calendar}のいずれか場合、{@link Parameter}に定義されたメソッドによりパラメータの時制を指定できます。
     *            </p>
     *            <p>
     *            パラメータが1つで型が{@link String}、<code>byte[]</code>、{@link Serializable}のいずれかの場合、{@link Parameter}に定義されたメソッドによりパラメータをラージオブジェクトとして扱えます。
     *            </p>
     * @param <T>
     *            ファンクションの戻り値の型。ファンクションの戻り値が結果セットの場合はリストの要素のクラス
     * @return SQLファイルファンクション呼び出し
     * @see Parameter
     */
    <T> SqlFileFunctionCall<T> callBySqlFile(Class<T> resultClass, String path,
            Object parameter);

}
