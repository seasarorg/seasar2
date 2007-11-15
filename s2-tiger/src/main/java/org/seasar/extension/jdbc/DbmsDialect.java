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

import java.sql.Statement;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * データベースごとの方言をあつかうインターフェースです。
 * 
 * @author higa
 * 
 */
public interface DbmsDialect {

    /**
     * 名前を返します。
     * 
     * @return 名前
     */
    String getName();

    /**
     * リミットをサポートしているかどうかを返します。
     * 
     * @return リミットをサポートしているかどうか
     */
    boolean supportsLimit();

    /**
     * オフセットをサポートしているかどうかを返します。
     * 
     * @return オフセットをサポートしているかどうか
     */
    boolean supportsOffset();

    /**
     * リミットなしのオフセットをサポートしているかどうかを返します。
     * 
     * @return リミットなしのオフセットをサポートしているかどうか
     */
    boolean supportsOffsetWithoutLimit();

    /**
     * カーソルをサポートしているかどうか返します。
     * 
     * @return カーソルをサポートしているかどうか
     */
    boolean supportsCursor();

    /**
     * プロシージャの呼び出しで結果セットに対してパラメータが必要かどうかを返します。
     * 
     * @return プロシージャの呼び出しで結果セットに対してパラメータが必要かどうか
     */
    boolean needsParameterForResultSet();

    /**
     * limit用<code>SQL</code>に変換します。
     * 
     * @param sql
     *            SQL
     * @param offset
     *            オフセット
     * @param limit
     *            リミット
     * @return limit用<code>SQL</code>
     */
    String convertLimitSql(String sql, int offset, int limit);

    /**
     * 値タイプを返します。
     * 
     * @param clazz
     *            クラス
     * @return 値タイプ
     */
    ValueType getValueType(Class<?> clazz);

    /**
     * 結合用のSQLを組み立てます。
     * 
     * @param fromClause
     *            from句
     * @param whereClause
     *            where句
     * @param joinType
     *            結合タイプ
     * @param tableName
     *            テーブル名
     * @param tableAlias
     *            テーブル別名
     * @param fkTableAlias
     *            外部キーを持つテーブルの別名
     * @param pkTableAlias
     *            主キーを持つテーブルの別名
     * @param joinColumnMetaList
     *            結合カラムメタデータのリスト
     * 
     */
    void setupJoin(FromClause fromClause, WhereClause whereClause,
            JoinType joinType, String tableName, String tableAlias,
            String fkTableAlias, String pkTableAlias,
            List<JoinColumnMeta> joinColumnMetaList);

    /**
     * {@link GeneratedValue#strategy()}に{@link GenerationType#AUTO}が指定された場合の、
     * デフォルトの{@link GenerationType}を返します。
     * 
     * @return デフォルトの{@link GenerationType}
     */
    GenerationType getDefaultGenerationType();

    /**
     * {@link GenerationType#IDENTITY}をサポートしている場合は<code>true</code>を返します。
     * 
     * @return {@link GenerationType#IDENTITY}をサポートしている場合は<code>true</code>
     */
    boolean supportsIdentity();

    /**
     * 識別子が{@link GenerationType#IDENTITY}で生成される場合に、 識別子をINSERT文に含める場合は<code>true</code>を返します。
     * 
     * @return 識別子をINSERT文に含める場合は<code>true</code>
     */
    boolean isInsertIdentityColumn();

    /**
     * 識別子が{@link GenerationType#IDENTITY}で自動生成される場合に、 生成された主キーの値を{@link Statement#getGeneratedKeys()}で取得できる場合は<code>true</code>を返します。
     * 
     * @return 生成された識別子の値を{@link Statement#getGeneratedKeys()}で取得できる場合は<code>true</code>
     */
    boolean supportGetGeneratedKeys();

    /**
     * 識別子が{@link GenerationType#IDENTITY}で自動生成される場合に、
     * 生成された主キーの値を取得するSQLを返します。
     * 
     * @param tableName
     *            テーブル名
     * @param columnName
     *            識別子のカラム名
     * @return 生成された識別子の値を取得するSQL
     */
    String getIdentitySelectString(String tableName, String columnName);

    /**
     * {@link GenerationType#SEQUENCE}をサポートしている場合は<code>true</code>を返します。
     * 
     * @return {@link GenerationType#SEQUENCE}をサポートしている場合は<code>true</code>
     */
    boolean supportsSequence();

    /**
     * 識別子が{@link GenerationType#SEQUENCE}で自動生成される場合に、 シーケンスの値を取得するSQLを返します。
     * 
     * @param sequenceName
     *            シーケンス名
     * @return シーケンスの値を取得するSQL
     */
    String getSequenceNextValString(String sequenceName);

    /**
     * デフォルトのバッチサイズを返します。
     * 
     * @return デフォルトのバッチサイズ
     */
    int getDefaultBatchSize();

    /**
     * 一意制約違反を原因とする例外であれば<code>true</code>を返します。
     * 
     * @param t
     *            例外
     * @return 一意制約違反を原因とする例外であれば<code>true</code>
     */
    boolean isUniqueConstraintViolation(Throwable t);

    /**
     * SELECT文で<code>FOR UPDATE</code>をサポートしていれば<code>true</code>を返します。
     * 
     * @return SELECT文で<code>FOR UPDATE</code>をサポートしていれば<code>true</code>
     */
    boolean supportsForUpdate();

    /**
     * SELECT文で<code>FOR UPDATE OF <var>column</var></code>をサポートしていれば<code>true</code>を返します。
     * 
     * @return SELECT文で<code>FOR UPDATE OF <var>column</var></code>をサポートしていれば<code>true</code>
     */
    boolean supportsForUpdateWithColumn();

    /**
     * SELECT文で<code>FOR UPDATE NOWAIT</code>をサポートしていれば<code>true</code>を返します。
     * 
     * @return SELECT文で<code>FOR UPDATE NOWAIT</code>をサポートしていれば<code>true</code>
     */
    boolean supportsForUpdateNowait();

    /**
     * SELECT文で<code>FOR UPDATE OF <var>column</var> NOWAIT</code>をサポートしていれば<code>true</code>を返します。
     * 
     * @return SELECT文で<code>FOR UPDATE OF <var>column</var> NOWAIT</code>をサポートしていれば<code>true</code>
     */
    boolean supportsForUpdateNowaitWithColumn();

    /**
     * SELECT文で<code>FOR UPDATE WAIT <var>sec</var></code>をサポートしていれば<code>true</code>を返します。
     * 
     * @return SELECT文で<code>FOR UPDATE WAIT <var>sec</var></code>をサポートしていれば<code>true</code>
     */
    boolean supportsForUpdateWait();

    /**
     * SELECT文で<code>FOR UPDATE OF <var>column</var> WAIT <var>sec</var></code>をサポートしていれば<code>true</code>を返します。
     * 
     * @return SELECT文で<code>FOR UPDATE OF <var>column</var> WAIT <var>sec</var></code>をサポートしていれば<code>true</code>
     */
    boolean supportsForUpdateWaitWithColumn();

    /**
     * SELECT文に付加する<code>FOR UPDATE</code>相当のSQLを返します。
     * 
     * @return SELECT文に付加する<code>FOR UPDATE</code>相当のSQL
     */
    String getForUpdateString();

    /**
     * SELECT文に付加する<code>FOR UPDATE OF <var>column</var></code>相当のSQLを返します。
     * 
     * @param columnName
     *            ロック対象のカラム名
     * @return SELECT文に付加する<code>FOR UPDATE OF <var>column</var></code>相当のSQL
     */
    String getForUpdateString(String columnName);

    /**
     * SELECT文に付加する<code>FOR UPDATE NOWAIT</code>相当のSQLを返します。
     * 
     * @return SELECT文に付加する<code>FOR UPDATE NOWAIT</code>相当のSQL
     */
    String getForUpdateNowaitString();

    /**
     * SELECT文に付加する<code>FOR UPDATE OF <var>column</var> NOWAIT</code>相当のSQLを返します。
     * 
     * @param columnName
     *            ロック対象のカラム名
     * @return SELECT文に付加する<code>FOR UPDATE OF <var>column</var> NOWAIT</code>相当のSQL
     */
    String getForUpdateNowaitString(String columnName);

    /**
     * SELECT文に付加する<code>FOR UPDATE WAIT <var>sec</var></code>相当のSQLを返します。
     * 
     * @param seconds
     *            ロックを獲得できるまでの最大待機時間(秒単位)
     * @return SELECT文に付加する<code>FOR UPDATE WAIT <var>sec</var></code>相当のSQL
     */
    String getForUpdateWaitString(int seconds);

    /**
     * SELECT文に付加する<code>FOR UPDATE OF <var>column</var> WAIT <var>sec</var></code>相当のSQLを返します。
     * 
     * @param columnName
     *            ロック対象のカラム名
     * @param seconds
     *            ロックを獲得できるまでの最大待機時間(秒単位)
     * @return SELECT文に付加する<code>FOR UPDATE OF <var>column</var> WAIT <var>sec</var></code>相当のSQL
     */
    String getForUpdateWaitString(String columnName, int seconds);

    /**
     * SELECT文で排他制御のためのロックヒントをサポートしていれば<code>true</code>を返します。
     * 
     * @return SELECT文で排他制御のためのロックヒントをサポートしていれば<code>true</code>
     */
    boolean supportsLockHint();

    /**
     * SELECT文に付加するロックヒントを返します。
     * 
     * @return SELECT文に付加するロックヒント
     */
    String getLockHintString();

}
