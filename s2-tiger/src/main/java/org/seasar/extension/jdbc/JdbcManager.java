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
	 * @param baseClass
	 *            ベースクラス
	 * @param sql
	 *            SQL
	 * @return SQL検索
	 * @see #selectBySql(Class, String, Object[])
	 */
	SqlSelect selectBySql(Class<?> baseClass, String sql);

	/**
	 * SQL検索を作成します。
	 * 
	 * @param baseClass
	 *            ベースクラス
	 * @param sql
	 *            SQL
	 * @param parameters
	 *            パラメータの配列
	 * @return SQL検索
	 */
	SqlSelect selectBySql(Class<?> baseClass, String sql, Object... parameters);

	/**
	 * 自動検索を作成します。
	 * 
	 * @param baseClass
	 *            ベースクラス
	 * @return 自動検索
	 */
	AutoSelect from(Class<?> baseClass);

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
