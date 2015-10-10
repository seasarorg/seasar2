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
package org.seasar.extension.jdbc.gen.dialect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;

import javax.persistence.GenerationType;
import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.gen.exception.UnsupportedSqlTypeRuntimeException;
import org.seasar.extension.jdbc.gen.provider.ValueTypeProvider;
import org.seasar.extension.jdbc.gen.sqltype.SqlType;

/**
 * RDBMSごとの方言を扱うインタフェースです。
 * 
 * @author taedium
 */
public interface GenDialect {

    /**
     * 名前を返します。
     * 
     * @return 名前
     */
    String getName();

    /**
     * デフォルトのスキーマ名を返します。
     * 
     * @param userName
     *            ユーザー名
     * @return スキーマ名
     */
    String getDefaultSchemaName(String userName);

    /**
     * SQL型を返します。
     * 
     * @param sqlType
     *            JDBCのSQL型
     * @return SQL型
     * @throws UnsupportedSqlTypeRuntimeException
     *             サポートされていないJDBCのSQL型が渡された場合
     */
    SqlType getSqlType(int sqlType) throws UnsupportedSqlTypeRuntimeException;

    /**
     * SQL型を返します。
     * 
     * @param valueTypeProvider
     *            {@link ValueType}の提供者
     * @param propertyMeta
     *            プロパティメタデータ
     * @return SQL型
     * @throws UnsupportedSqlTypeRuntimeException
     *             サポートされていないJDBCのSQL型が渡された場合
     */
    SqlType getSqlType(ValueTypeProvider valueTypeProvider,
            PropertyMeta propertyMeta);

    /**
     * カラム型を返します。
     * 
     * @param columnTypeName
     *            カラムの型名
     * @param sqlType
     *            JDBCのSQL型
     * @return カラム型、サポートされていないカラムの型名の場合{@code null}
     */
    ColumnType getColumnType(String columnTypeName, int sqlType);

    /**
     * デフォルトの{@link GenerationType}を返します。
     * 
     * @return デフォルトの{@link GenerationType}
     */
    GenerationType getDefaultGenerationType();

    /**
     * 開始を表すクォートを返します。
     * 
     * @return 開始を表すクォート
     */
    String getOpenQuote();

    /**
     * 終了を表すクォートを返します。
     * 
     * @return 終了を表すクォート
     */
    String getCloseQuote();

    /**
     * クォートで囲みます。
     * 
     * @param value
     *            値
     * @return クォートで囲まれた値
     */
    String quote(String value);

    /**
     * クォートを取り除きます。
     * 
     * @param value
     *            値
     * @return クォートが取り除かれた値
     */
    String unquote(String value);

    /**
     * シーケンスをサポートする場合{@code true}を返します。
     * 
     * @return シーケンスをサポートする場合{@code true}、しない場合{@code false}
     */
    boolean supportsSequence();

    /**
     * {@link DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)}
     * をサポートする場合{@code true}を返します。
     * 
     * @param catalogName
     *            カタログ名
     * @param schemaName
     *            スキーマ名
     * @param tableName
     *            テーブル名
     * @return {@link DatabaseMetaData#getIndexInfo(String, String, String, boolean, boolean)}
     *         をサポートする場合{@code true}
     */
    boolean supportsGetIndexInfo(String catalogName, String schemaName,
            String tableName);

    /**
     * シーケンス定義の断片を返します。
     * <p>
     * この断片は create sequence 以降に続きます。
     * </p>
     * 
     * @param dataType
     *            データタイプ
     * @param initialValue
     *            初期値
     * @param allocationSize
     *            割り当てサイズ
     * @return
     */
    String getSequenceDefinitionFragment(String dataType, long initialValue,
            int allocationSize);

    /**
     * SQLブロックの区切り文字を返します。
     * 
     * @return SQLブロックの区切り文字、SQLブロックの区切り文字蛾存在しない場合{@code null}
     */
    String getSqlBlockDelimiter();

    /**
     * IDENTITYカラムの定義を返します。
     * 
     * @return IDENTITYカラムの定義
     */
    String getIdentityColumnDefinition();

    /**
     * 外部キーを削除する構文を返します。
     * 
     * @return 外部キーを削除する構文
     */
    String getDropForeignKeySyntax();

    /**
     * 一意キーを削除する構文を返します。
     * 
     * @return 外部キーを削除する構文
     */
    String getDropUniqueKeySyntax();

    /**
     * テーブルが存在しない例外を表す場合{@code true}を返します。
     * 
     * @param throwable
     *            何らかの例外
     * @return テーブルが存在しない例外を表す場合{@code true}
     */
    boolean isTableNotFound(Throwable throwable);

    /**
     * カラムが存在しない例外を表す場合{@code true}を返します。
     * 
     * @param throwable
     *            何らかの例外
     * @return カラムが存在しない例外を表す場合{@code true}
     */
    boolean isColumnNotFound(Throwable throwable);

    /**
     * シーケンスが存在しない例外を表す場合{@code true}を返します。
     * 
     * @param throwable
     *            何らかの例外
     * @return シーケンスが存在しない例外を表す場合{@code true}
     */
    boolean isSequenceNotFound(Throwable throwable);

    /**
     * SQLブロックのコンテキストを作成します。
     * 
     * @return SQLブロックのコンテキスト
     */
    SqlBlockContext createSqlBlockContext();

    /**
     * IDENTITYカラムに対するinsertをサポートしている場合{@code true}を返します。
     * 
     * @return IDENTITYカラムに対するinsertをサポートしている場合{@code true}
     */
    boolean supportsIdentityInsert();

    /**
     * IDENTITYカラムに対するinsertの有効/無効を制御するステートメントをサポートしている場合{@code true}を返します。
     * 
     * @return IDENTITYカラムに対するinsertをサポートしている場合{@code true}
     */
    boolean supportsIdentityInsertControlStatement();

    /**
     * IDENTITYカラムに対するinsertを有効化するステートメントを返します。
     * 
     * @param tableName
     *            テーブル名
     * @return IDENTITYカラムに対するinsertを有効化するステートメント
     */
    String getIdentityInsertEnableStatement(String tableName);

    /**
     * IDENTITYカラムに対するinsertを無効化するステートメントを返します。
     * 
     * @param tableName
     *            テーブル名
     * @return IDENTITYカラムに対するinsertを無効化するステートメント
     */
    String getIdentityInsertDisableStatement(String tableName);

    /**
     * NULLが可能な一意制約をサポートしている場合{@code true}を返します。
     * 
     * @return NULLが可能な一意制約をサポートしている場合{@code true}
     */
    boolean supportsNullableUnique();

    /**
     * IDENTITYカラムをサポートしている場合{@code true}を返します。
     * 
     * @return IDENTITYカラムをサポートしている場合{@code true}
     */
    boolean supportsIdentity();

    /**
     * シーケンスの値を取得するSQLを返します。
     * 
     * @param sequenceName
     *            シーケンス名
     * @param allocationSize
     *            割り当てサイズ
     * @return シーケンスの値を取得するSQL
     */
    String getSequenceNextValString(String sequenceName, int allocationSize);

    /**
     * CREATE TABLEでコメントをサポートする場合{@code true}を返します。
     * 
     * @return コメントをサポートする場合{@code true}
     */
    boolean supportsCommentInCreateTable();

    /**
     * COMMENT ONをサポートする場合{@code true}を返します。
     * 
     * @return COMMENT ONをサポートする場合{@code true}
     */
    boolean supportsCommentOn();

    /**
     * JDBCのコメント取得機能が利用できる場合{@code true}を返します。
     * <p>
     * JDBCのコメント取得機能が利用できるとは、次のメソッドでREMARKSカラムの値が取得できることを意味します。
     * </p>
     * <ul>
     * <li>{@link DatabaseMetaData#getTables(String, String, String, String[])}</li>
     * <li>{@link DatabaseMetaData#getColumns(String, String, String, String)}</li>
     * </ul>
     * 
     * @return JDBCのコメント取得機能が利用できる場合{@code true}
     */
    boolean isJdbcCommentAvailable();

    /**
     * テーブルのコメントをデータベースから直接取得します。
     * <p>
     * {@link #isJdbcCommentAvailable()}が{@code true}を返す場合に利用できます。
     * </p>
     * 
     * @param connection
     *            コネクション
     * @param catalogName
     *            カタログ名
     * @param schemaName
     *            スキーマ名
     * @param tableName
     *            テーブル名
     * @return テーブルのコメント、存在しない場合{@code null}
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    String getTableComment(Connection connection, String catalogName,
            String schemaName, String tableName) throws SQLException;

    /**
     * カラムのコメントをデータベースから直接取得しマップに詰めて返します。
     * <p>
     * {@link #isJdbcCommentAvailable()}が{@code true}を返す場合に利用できます。
     * </p>
     * <p>
     * 戻り値のマップのキーは大文字小文字を気にしません。 カラム名に対応するコメントが存在しない、値は{@code null}になります。
     * </p>
     * 
     * @param connection
     *            コネクション
     * @param catalogName
     *            カタログ名
     * @param schemaName
     *            スキーマ名
     * @param tableName
     *            テーブル名
     * @return 大文字小文字を気にしないカラム名をキー、カラムのコメントを値とするマップ
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    Map<String, String> getColumnCommentMap(Connection connection,
            String catalogName, String schemaName, String tableName)
            throws SQLException;

    /**
     * 参照整合制約の削除規則をサポートする場合{@code true}を返します。
     * 
     * @return 参照整合制約の削除規則をサポートする場合{@code true}
     */
    boolean supportsReferentialDeleteRule();

    /**
     * 参照整合制約の更新規則をサポートする場合{@code true}を返します。
     * 
     * @return 参照整合制約の更新規則をサポートする場合{@code true}
     */
    boolean supportsReferentialUpdateRule();

    /**
     * 列の値が自動的に増分される場合{@code true}を返します。
     * 
     * @param connection
     *            コネクション
     * @param catalogName
     *            カタログ名
     * @param schemaName
     *            スキーマ名
     * @param tableName
     *            テーブル名
     * @param columnName
     *            カラム名
     * @return 列が自動的に増分される場合{@code true}、そうでない場合{@code false}
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    boolean isAutoIncrement(Connection connection, String catalogName,
            String schemaName, String tableName, String columnName)
            throws SQLException;

    /**
     * カラム型です。
     * <p>
     * データベースのメタデータが返すカラムの型名に対応します。
     * <p>
     * <p>
     * データベースのメタデータからJavaコードを生成する場合に使用できます。
     * </p>
     * 
     * @author taedium
     */
    interface ColumnType {

        /**
         * 属性のクラスを返します。
         * 
         * @param length
         *            長さ
         * @param precision
         *            精度
         * @param scale
         *            スケール
         * @return 属性のクラス
         */
        Class<?> getAttributeClass(int length, int precision, int scale);

        /**
         * カラム定義を返します。
         * 
         * @param length
         *            長さ
         * @param precision
         *            精度
         * @param scale
         *            スケール
         * @param defaultValue
         *            デフォルト値、存在しない場合は{@code null}
         * @return カラム定義
         */
        String getColumnDefinition(int length, int precision, int scale,
                String defaultValue);

        /**
         * LOBの場合{@code true}
         * 
         * @return LOBの場合{@code true}
         */
        boolean isLob();

        /**
         * 時制の種別を返します。
         * 
         * @return 時制の種別、存在しない場合は{@code null}
         */
        TemporalType getTemporalType();

    }

    /**
     * SQLブロックのコンテキストです。
     * 
     * @author taedium
     */
    interface SqlBlockContext {

        /**
         * SQLのキーワードを追加します。
         * 
         * @param keyword
         *            SQLのキーワード
         */
        void addKeyword(String keyword);

        /**
         * SQLブロックの内側と判定できる場合{@code true}
         * 
         * @return SQLブロックの内側と判定できる場合{@code true}
         */
        boolean isInSqlBlock();
    }
}
