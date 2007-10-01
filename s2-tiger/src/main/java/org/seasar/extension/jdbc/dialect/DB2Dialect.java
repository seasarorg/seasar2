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
package org.seasar.extension.jdbc.dialect;

import org.seasar.extension.jdbc.exception.OrderByNotFoundRuntimeException;
import org.seasar.framework.util.StringUtil;

/**
 * DB2用の方言をあつかうクラスです。
 * 
 * @author higa
 * 
 */
public class DB2Dialect extends StandardDialect {

	@Override
	public String getName() {
		return "db2";
	}

	@Override
	public boolean supportsLimit() {
		return true;
	}

	@Override
	public String convertLimitSql(String sql, int offset, int limit) {
		if (offset > 0) {
			return convertOffsetLimitSql(sql, offset, limit);
		}
		return convertLimitOnlySql(sql, limit);

	}

	/**
	 * offset、limitつきのSQLに変換します。
	 * 
	 * @param sql
	 *            SQL
	 * @param offset
	 *            オフセット
	 * @param limit
	 *            リミット
	 * @return offset、limitつきのSQL
	 * @throws OrderByNotFoundRuntimeException
	 *             <code>ordr by</code>が見つからない場合。
	 */
	protected String convertOffsetLimitSql(String sql, int offset, int limit)
			throws OrderByNotFoundRuntimeException {
		StringBuilder buf = new StringBuilder(sql.length() + 150);
		String lowerSql = sql.toLowerCase();
		int startOfSelect = lowerSql.indexOf("select");
		buf.append(sql.substring(0, startOfSelect));
		buf.append("select * from ( select ");
		buf.append("rownumber() over(");
		int orderByIndex = lowerSql.lastIndexOf("order by");
		if (orderByIndex > 0) {
			buf.append(sql.substring(orderByIndex));
			sql = StringUtil.rtrim(sql.substring(0, orderByIndex));
		} else {
			throw new OrderByNotFoundRuntimeException(sql);
		}
		buf.append(") as rownumber_, temp_.* from ( ");
		buf.append(sql.substring(startOfSelect));
		buf.append(" ) as temp_");
		buf.append(" ) as temp2_ where rownumber_ >= ");
		buf.append(offset + 1);
		if (limit > 0) {
			buf.append(" and rownumber_ <= ");
			buf.append(offset + limit);
		}
		return buf.toString();
	}

	/**
	 * limitのみがついたのSQLに変換します。
	 * 
	 * @param sql
	 *            SQL
	 * @param limit
	 *            リミット
	 * @return limitのみがついたのSQL
	 */
	protected String convertLimitOnlySql(String sql, int limit) {
		StringBuilder buf = new StringBuilder(sql.length() + 30);
		buf.append(sql);
		buf.append(" fetch first ");
		buf.append(limit);
		buf.append(" rows only");
		return buf.toString();
	}
}