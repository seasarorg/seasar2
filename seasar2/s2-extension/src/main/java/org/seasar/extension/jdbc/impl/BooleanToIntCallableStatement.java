/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.impl;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author higa
 *  
 */
public class BooleanToIntCallableStatement extends CallableStatementWrapper {

	/**
	 * @param original
	 */
	public BooleanToIntCallableStatement(CallableStatement original) {
		super(original);
	}

	/**
	 * @see java.sql.PreparedStatement#setBoolean(int, boolean)
	 */
	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		setInt(parameterIndex, x ? 1 : 0);
	}

	/**
	 * @see java.sql.CallableStatement#setBoolean(java.lang.String, boolean)
	 */
	public void setBoolean(String parameterName, boolean x) throws SQLException {
		setInt(parameterName, x ? 1 : 0);
	}

	/**
	 * @see java.sql.PreparedStatement#setNull(int, int, java.lang.String)
	 */
	public void setNull(int paramIndex, int sqlType, String typeName)
			throws SQLException {

		super.setNull(paramIndex, changeSqlTypeIfBoolean(sqlType), typeName);
	}

	/**
	 * @see java.sql.PreparedStatement#setNull(int, int)
	 */
	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		super.setNull(parameterIndex, changeSqlTypeIfBoolean(sqlType));
	}

	/**
	 * @see java.sql.CallableStatement#setNull(java.lang.String, int,
	 *      java.lang.String)
	 */
	public void setNull(String parameterName, int sqlType, String typeName)
			throws SQLException {

		super.setNull(parameterName, changeSqlTypeIfBoolean(sqlType), typeName);
	}

	/** 
	 * @see java.sql.CallableStatement#setNull(java.lang.String, int)
	 */
	public void setNull(String parameterName, int sqlType) throws SQLException {
		super.setNull(parameterName, changeSqlTypeIfBoolean(sqlType));
	}
	
	protected int changeSqlTypeIfBoolean(int sqlType) {
		return sqlType == Types.BOOLEAN ? Types.INTEGER : sqlType;
	}
}