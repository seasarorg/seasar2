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
 * 結合メタデータです。
 * 
 * @author higa
 * 
 */
public class JoinMeta {

	/**
	 * 結合名です。
	 */
	protected String name;

	/**
	 * 結合タイプです。
	 */
	protected JoinType joinType;

	/**
	 * フェッチするかどうかです。
	 */
	protected boolean fetch = true;

	/**
	 * {@link JoinMeta}を作成します。
	 */
	public JoinMeta() {
	}

	/**
	 * {@link JoinMeta}を作成します。
	 * 
	 * @param name
	 *            結合するプロパティ名
	 */
	public JoinMeta(String name) {
		this(name, JoinType.LEFT_OUTER);
	}

	/**
	 * {@link JoinMeta}を作成します。
	 * 
	 * @param name
	 *            結合名
	 * @param joinType
	 *            結合タイプ
	 */
	public JoinMeta(String name, JoinType joinType) {
		this(name, joinType, true);
	}

	/**
	 * {@link JoinMeta}を作成します。
	 * 
	 * @param name
	 *            結合名
	 * @param fetch
	 *            フェッチするかどうか
	 */
	public JoinMeta(String name, boolean fetch) {
		this(name, JoinType.LEFT_OUTER, fetch);
	}

	/**
	 * {@link JoinMeta}を作成します。
	 * 
	 * @param name
	 *            結合名
	 * @param joinType
	 *            結合タイプ
	 * @param fetch
	 *            フェッチするかどうか
	 */
	public JoinMeta(String name, JoinType joinType, boolean fetch) {
		setName(name);
		setJoinType(joinType);
		setFetch(fetch);
	}

	/**
	 * 結合名を返します。
	 * 
	 * @return 結合するプロパティ名
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>
	 * 結合名を設定します。
	 * </p>
	 * <p>
	 * ネストしている場合は、<code>aaa.bbb</code>のように.で区切ります。
	 * </p>
	 * 
	 * @param name
	 *            結合名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 結合タイプを返します。
	 * 
	 * @return 結合タイプ
	 */
	public JoinType getJoinType() {
		return joinType;
	}

	/**
	 * 結合タイプを設定します。
	 * 
	 * @param joinType
	 *            結合タイプ
	 */
	public void setJoinType(JoinType joinType) {
		this.joinType = joinType;
	}

	/**
	 * フェッチするかどうかを返します。
	 * 
	 * @return フェッチするかどうか
	 */
	public boolean isFetch() {
		return fetch;
	}

	/**
	 * フェッチするかどうかを設定します。
	 * 
	 * @param fetch
	 *            フェッチするかどうか
	 */
	public void setFetch(boolean fetch) {
		this.fetch = fetch;
	}
}
