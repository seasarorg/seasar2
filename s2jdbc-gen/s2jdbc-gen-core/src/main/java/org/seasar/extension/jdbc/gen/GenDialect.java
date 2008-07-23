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

import java.util.List;

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
     * SQL型に対応する型を返します。
     * 
     * @param sqlType
     *            SQL型
     * @return SQL型に対応する型
     */
    Type getType(int sqlType);

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
     * @return SQLブロックの区切り文字
     */
    String getSqlBlockDelimiter();

    /**
     * SQLブロックの開始を示す単語の場合{@code true}を返します。
     * 
     * @param words
     *            単語のリスト
     * @return SQLブロックの開始を示す単語の場合{@code true}
     */
    boolean isSqlBlockStartWords(List<String> words);

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
     * Javaクラスとデータベースのカラム定義を表すインタフェースです。
     * 
     * @author taedium
     */
    public interface Type {

        /**
         * Javaのクラスを返します。
         * 
         * @param length
         *            長さ
         * @param precision
         *            精度
         * @param scale
         *            スケール
         * @param typeName
         *            カラムの型名
         * @return Javaのクラス
         */
        Class<?> getJavaClass(int length, int precision, int scale,
                String typeName);

        /**
         * カラム定義を返します。
         * 
         * @param length
         *            長さ
         * @param precision
         *            精度
         * @param scale
         *            スケール
         * @param typeName
         *            カラムの型名
         * @return カラム定義、サポートされるカラム定義が存在しない場合{@code null}
         */
        String getColumnDefinition(int length, int precision, int scale,
                String typeName);
    }
}
