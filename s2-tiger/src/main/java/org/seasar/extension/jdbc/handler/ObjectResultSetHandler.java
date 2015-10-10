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
package org.seasar.extension.jdbc.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.exception.SNonUniqueResultException;

/**
 * 単独のオブジェクトを返す {@link ResultSetHandler}です。
 * 
 * @author higa
 * 
 */
public class ObjectResultSetHandler implements ResultSetHandler {

	/**
	 * 値タイプです。
	 */
	protected ValueType valueType;

	/**
	 * SQLです。
	 */
	protected String sql;

	/**
	 * {@link ObjectResultSetHandler}を作成します。
	 * 
	 * @param valueType
	 *            値タイプ
	 * @param sql
	 *            SQL
	 */
	public ObjectResultSetHandler(ValueType valueType, String sql) {
		this.valueType = valueType;
		this.sql = sql;
	}

	public Object handle(ResultSet rs) throws SQLException,
			SNonUniqueResultException {
		Object ret = null;
		if (rs.next()) {
			ret = valueType.getValue(rs, 1);
			if (rs.next()) {
				throw new SNonUniqueResultException(sql);
			}
		}
		return ret;
	}
}