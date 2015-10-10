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
 * カラム用のメタデータです。
 * 
 * @author higa
 * 
 */
public class ColumnMeta {

	/**
	 * 名前です。
	 */
	protected String name;

	/**
	 * 挿入可能かどうかです。
	 */
	protected boolean insertable = true;

	/**
	 * 更新可能かどうかです。
	 */
	protected boolean updatable = true;

	/**
	 * 名前を返します。
	 * 
	 * @return 名前
	 */
	public String getName() {
		return name;
	}

	/**
	 * 名前を設定します。
	 * 
	 * @param name
	 *            名前
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 挿入可能かどうかを返します。
	 * 
	 * @return 挿入可能かどうか
	 */
	public boolean isInsertable() {
		return insertable;
	}

	/**
	 * 挿入可能かどうかを設定します。
	 * 
	 * @param insertable
	 *            挿入可能かどうか
	 */
	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}

	/**
	 * 更新可能かどうかを返します。
	 * 
	 * @return 更新可能かどうか
	 */
	public boolean isUpdatable() {
		return updatable;
	}

	/**
	 * 更新可能かどうかを設定します。
	 * 
	 * @param updatable
	 *            更新可能かどうか
	 */
	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}
}