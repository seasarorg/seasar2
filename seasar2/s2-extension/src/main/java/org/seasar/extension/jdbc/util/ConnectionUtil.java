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
package org.seasar.extension.jdbc.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.seasar.framework.exception.SQLRuntimeException;

/**
 * @author higa
 *
 */
public final class ConnectionUtil {

	private ConnectionUtil() {
	}

	public static void close(Connection connection) {
		if (connection == null) {
			return;
		}
		try {
			connection.close();
		} catch (SQLException ex) {
			throw new SQLRuntimeException(ex);
		}
	}
	
	public static PreparedStatement prepareStatement(
		Connection connection,
		String sql) {

		try {
			return connection.prepareStatement(sql);
		} catch (SQLException ex) {
			throw new SQLRuntimeException(ex);
		}
	}
	
	public static CallableStatement prepareCall(
			Connection connection,
			String sql) {

			try {
				return connection.prepareCall(sql);
			} catch (SQLException ex) {
				throw new SQLRuntimeException(ex);
			}
		}
	
	public static DatabaseMetaData getMetaData(Connection connection) {
		try {
			return connection.getMetaData();
		} catch (SQLException ex) {
			throw new SQLRuntimeException(ex);
		}
	}
}
