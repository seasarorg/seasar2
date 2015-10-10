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
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.TemporalType;

import org.seasar.framework.util.tiger.Pair;

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
     * limit用SQLに変換します。
     * 
     * @param sql
     *            SQL
     * @param offset
     *            オフセット
     * @param limit
     *            リミット
     * @return limit用SQL
     */
    String convertLimitSql(String sql, int offset, int limit);

    /**
     * 件数取得用SQLに変換します。
     * 
     * @param sql
     *            SQL
     * @return 件数取得用SQL
     */
    String convertGetCountSql(String sql);

    /**
     * 件数取得用SQLのSELECTリストを返します。
     * 
     * @param idPropertyMeta
     *            IDプロパティのメタデータ
     * @return 件数取得用SQLのSELECTリスト
     */
    String getCountSqlSelectList(List<PropertyMeta> idPropertyMeta);

    /**
     * 値タイプを返します。
     * 
     * @param propertyMeta
     *            プロパティのメタデータ
     * @return 値タイプ
     */
    ValueType getValueType(PropertyMeta propertyMeta);

    /**
     * 値タイプを返します。
     * 
     * @param clazz
     *            クラス
     * @param lob
     *            <code>LOB</code>かどうか
     * @param temporalType
     *            時制の種別
     * @return 値タイプ
     */
    ValueType getValueType(Class<?> clazz, boolean lob,
            TemporalType temporalType);

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
     * @param lockHint
     *            ロックヒント
     * @param condition
     *            付加的な結合条件
     */
    void setupJoin(FromClause fromClause, WhereClause whereClause,
            JoinType joinType, String tableName, String tableAlias,
            String fkTableAlias, String pkTableAlias,
            List<JoinColumnMeta> joinColumnMetaList, String lockHint,
            String condition);

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
     * 識別子が{@link GenerationType#IDENTITY}で生成される場合に、 識別子をINSERT文に含める場合は
     * <code>true</code>を返します。
     * 
     * @return 識別子をINSERT文に含める場合は<code>true</code>
     */
    boolean isInsertIdentityColumn();

    /**
     * 識別子が{@link GenerationType#IDENTITY}で自動生成される場合に、 生成された主キーの値を
     * {@link Statement#getGeneratedKeys()}で取得できる場合は<code>true</code>を返します。
     * 
     * @return 生成された識別子の値を{@link Statement#getGeneratedKeys()}で取得できる場合は
     *         <code>true</code>
     */
    boolean supportsGetGeneratedKeys();

    /**
     * 識別子が{@link GenerationType#IDENTITY}で自動生成される場合に、 生成された主キーの値を取得するSQLを返します。
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
     * @param allocationSize
     *            アロケーションサイズ
     * @return シーケンスの値を取得するSQL
     */
    String getSequenceNextValString(String sequenceName, int allocationSize);

    /**
     * デフォルトのバッチサイズを返します。
     * 
     * @return デフォルトのバッチサイズ
     */
    int getDefaultBatchSize();

    /**
     * バッチ更新の結果として更新カウントの配列を正しく返す場合は<code>true</code>を返します。
     * 
     * @return バッチ更新の結果として更新カウントの配列を正しく返す場合は<code>true</code>
     */
    boolean supportsBatchUpdateResults();

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
     * @param type
     *            SELECT ～ FOR UPDATEのタイプ
     * @param withTarget
     *            ロック対象を指定する場合は<code>true</code>
     * @return SELECT文で<code>FOR UPDATE</code>をサポートしていれば<code>true</code>
     */
    boolean supportsForUpdate(SelectForUpdateType type, boolean withTarget);

    /**
     * SELECT文に付加する<code>FOR UPDATE NOWAIT</code>相当のSQLを返します。
     * 
     * @param type
     *            SELECT ～ FOR UPDATEのタイプ
     * @param waitSeconds
     *            <code>type</code>に{@link SelectForUpdateType#WAIT}
     *            が指定された場合の待機時間(秒単位)
     * @param aliases
     *            ロック対象となるカラムのエイリアス。対象が指定されなかった場合は空の配列
     * @return SELECT文に付加する<code>FOR UPDATE</code>句のSQL
     */
    String getForUpdateString(SelectForUpdateType type, int waitSeconds,
            Pair<String, String>... aliases);

    /**
     * SELECT文で排他制御のためのロックヒントをサポートしていれば<code>true</code>を返します。
     * 
     * @return SELECT文で排他制御のためのロックヒントをサポートしていれば<code>true</code>
     */
    boolean supportsLockHint();

    /**
     * SELECT文に付加するロックヒントを返します。
     * 
     * @param type
     *            SELECT ～ FOR UPDATEのタイプ
     * @param waitSeconds
     *            <code>type</code>に{@link SelectForUpdateType#WAIT}
     *            が指定された場合の待機時間(秒単位)
     * @return SELECT文に付加するロックヒント
     */
    String getLockHintString(SelectForUpdateType type, int waitSeconds);

    /**
     * 内部結合した場合に<code>FOR UPDATE</code>をサポートしていれば<code>true</code>を返します。
     * 
     * @return 内部結合した場合に<code>FOR UPDATE</code>をサポートしていれば<code>true</code>
     */
    boolean supportsInnerJoinForUpdate();

    /**
     * 外部結合した場合に<code>FOR UPDATE</code>をサポートしていれば<code>true</code>を返します。
     * 
     * @return 外部結合した場合に<code>FOR UPDATE</code>をサポートしていれば<code>true</code>
     */
    boolean supportsOuterJoinForUpdate();

    /**
     * ヒントコメントを返します。
     * 
     * @param hint
     *            ヒント
     * @return ヒントコメント
     */
    String getHintComment(String hint);
}
