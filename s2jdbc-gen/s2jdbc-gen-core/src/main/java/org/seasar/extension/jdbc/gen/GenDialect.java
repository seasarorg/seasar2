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
     * SQL型を返します。
     * 
     * @param sqlType
     *            JDBCのSQL型
     * @return SQL型
     */
    SqlType getSqlType(int sqlType);

    /**
     * カラム型を返します。
     * 
     * @param columnTypeName
     *            カラムの型名
     * @return カラム型、サポートされていないカラムの型名の場合{@code null}
     */
    ColumnType getColumnType(String columnTypeName);

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

    String quote(String value);

    String unquote(String value);

    /**
     * シーケンスをサポートする場合{@code true}、しない場合{@code false}を返します。
     * 
     * @return シーケンスをサポートする場合{@code true}、しない場合{@code false}
     */
    boolean supportsSequence();

    /**
     * シーケンス定義の断片を返します。
     * <p>
     * この断片は create sequence 以降に続きます。
     * </p>
     * 
     * @param dataType
     *            データタイプ
     * @param initValue
     *            初期値
     * @param allocationSize
     *            割り当てサイズ
     * @return
     */
    String getSequenceDefinitionFragment(String dataType, int initValue,
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
     * SQLブロックのコンテキストを作成します。
     * 
     * @return SQLブロックのコンテキスト
     */
    SqlBlockContext createSqlBlockContext();

    /**
     * IDENTITYカラムに対するinsertをサポートしている場合{@code true}
     * 
     * @return IDENTITYカラムに対するinsertをサポートしている場合{@code true}
     */
    boolean supportsIdentityInsert();

    /**
     * IDENTITYカラムに対するinsertの有効/無効を制御するステートメントをサポートしている場合{@code true}
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
         * @return カラム定義
         */
        String getColumnDefinition(int length, int precision, int scale);

        /**
         * LOBの場合{@code true}
         * 
         * @return LOBの場合{@code true}
         */
        boolean isLob();

        /**
         * 時制型を返します。
         * 
         * @return 時制型、ただしこの型が時制を表さない場合{@code null}
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
