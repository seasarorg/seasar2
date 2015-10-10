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

/**
 * 関連のカラムを指定するためのメタデータです。
 * 
 * @author higa
 * 
 */
public class JoinColumnMeta {

	/**
	 * 外部キーのカラム名です。
	 */
	protected String name;

	/**
	 * 関連テーブルの主キーのカラム名
	 */
	protected String referencedColumnName;

	/**
	 * {@link JoinColumnMeta}を作成します。
	 * 
	 */
	public JoinColumnMeta() {
	}

	/**
	 * {@link JoinColumnMeta}を作成します。
	 * 
	 * @param name
	 *            外部キーのカラム名
	 * @param referencedColumnName
	 *            関連テーブルの主キーのカラム名
	 */
	public JoinColumnMeta(String name, String referencedColumnName) {
		if ("".equals(name)) {
			name = null;
		}
		if ("".equals(referencedColumnName)) {
			referencedColumnName = null;
		}
		this.name = name;
		this.referencedColumnName = referencedColumnName;
	}

	/**
	 * 外部キーのカラム名を返します。
	 * 
	 * @return 外部キーのカラム名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 外部キーのカラム名を設定します。
	 * 
	 * @param name
	 *            外部キーのカラム名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 関連テーブルの主キーのカラム名を返します。
	 * 
	 * @return 関連テーブルの主キーのカラム名
	 */
	public String getReferencedColumnName() {
		return referencedColumnName;
	}

	/**
	 * 関連テーブルの主キーのカラム名を設定します。
	 * 
	 * @param referencedColumnName
	 *            関連テーブルの主キーのカラム名
	 */
	public void setReferencedColumnName(String referencedColumnName) {
		this.referencedColumnName = referencedColumnName;
	}
}
