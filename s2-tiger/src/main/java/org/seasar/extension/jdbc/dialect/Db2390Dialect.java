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
package org.seasar.extension.jdbc.dialect;

/**
 * DB2/390用の方言をあつかうクラスです。
 * 
 * @author higa
 * 
 */
public class Db2390Dialect extends Db2Dialect {

	@Override
	public boolean supportsOffset() {
		return false;
	}

	@Override
	public String convertLimitSql(String sql, int offset, int limit) {
		return convertLimitOnlySql(sql, offset + limit);

	}
}