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

import org.seasar.extension.jdbc.ValueType;

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
}