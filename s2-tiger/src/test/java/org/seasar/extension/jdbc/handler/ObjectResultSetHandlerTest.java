/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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

import junit.framework.TestCase;

import org.seasar.extension.jdbc.exception.SNonUniqueResultException;
import org.seasar.extension.jdbc.handler.ObjectResultSetHandler;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.mock.sql.MockColumnMetaData;
import org.seasar.framework.mock.sql.MockResultSet;
import org.seasar.framework.mock.sql.MockResultSetMetaData;
import org.seasar.framework.util.ArrayMap;

/**
 * @author higa
 * 
 */
public class ObjectResultSetHandlerTest extends TestCase {

	private static final String SQL = "select count(*) as cnt from aaa";

	/**
	 * @throws Exception
	 * 
	 */
	public void testHandle() throws Exception {
		ObjectResultSetHandler handler = new ObjectResultSetHandler(
				ValueTypes.INTEGER, SQL);
		MockResultSetMetaData rsMeta = new MockResultSetMetaData();
		MockColumnMetaData columnMeta = new MockColumnMetaData();
		columnMeta.setColumnLabel("CNT");
		rsMeta.addColumnMetaData(columnMeta);
		MockResultSet rs = new MockResultSet(rsMeta);
		ArrayMap data = new ArrayMap();
		data.put("CNT", new Integer(5));
		rs.addRowData(data);
		assertEquals(new Integer(5), handler.handle(rs));
	}

	/**
	 * @throws Exception
	 */
	public void testHandle_nonUniqueResult() throws Exception {
		ObjectResultSetHandler handler = new ObjectResultSetHandler(
				ValueTypes.INTEGER, SQL);
		MockResultSetMetaData rsMeta = new MockResultSetMetaData();
		MockColumnMetaData columnMeta = new MockColumnMetaData();
		columnMeta.setColumnLabel("CNT");
		rsMeta.addColumnMetaData(columnMeta);
		MockResultSet rs = new MockResultSet(rsMeta);
		ArrayMap data = new ArrayMap();
		data.put("CNT", new Integer(5));
		rs.addRowData(data);
		rs.addRowData(data);
		try {
			handler.handle(rs);
			fail();
		} catch (SNonUniqueResultException e) {
			System.out.println(e.getMessage());
			assertEquals(SQL, e.getSql());
		}
	}
}
