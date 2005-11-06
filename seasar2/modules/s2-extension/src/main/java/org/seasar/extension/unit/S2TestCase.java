/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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
package org.seasar.extension.unit;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import org.seasar.extension.dataset.ColumnType;
import org.seasar.extension.dataset.DataReader;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.DataWriter;
import org.seasar.extension.dataset.impl.SqlDeleteTableWriter;
import org.seasar.extension.dataset.impl.SqlReloadReader;
import org.seasar.extension.dataset.impl.SqlReloadTableReader;
import org.seasar.extension.dataset.impl.SqlTableReader;
import org.seasar.extension.dataset.impl.SqlWriter;
import org.seasar.extension.dataset.impl.XlsReader;
import org.seasar.extension.dataset.impl.XlsWriter;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.extension.jdbc.UpdateHandler;
import org.seasar.extension.jdbc.impl.BasicUpdateHandler;
import org.seasar.framework.unit.S2FrameworkTestCase;
import org.seasar.framework.util.FileOutputStreamUtil;
import org.seasar.framework.util.ResourceUtil;

/** 
 * @author higa
 */
public abstract class S2TestCase extends S2FrameworkTestCase {

	public S2TestCase() {
	}

	public S2TestCase(String name) {
		super(name);
	}

	public DataSet readXls(String path) {
		DataReader reader = new XlsReader(convertPath(path));
		return reader.read();
	}

	public void writeXls(String path, DataSet dataSet) {
		File dir = ResourceUtil.getBuildDir(getClass());
		File file = new File(dir, convertPath(path));
		DataWriter writer = new XlsWriter(FileOutputStreamUtil.create(file));
		writer.write(dataSet);
	}

	public void writeDb(DataSet dataSet) {
		DataWriter writer = new SqlWriter(getDataSource());
		writer.write(dataSet);
	}

	public DataTable readDbByTable(String table) {
		return readDbByTable(table, null);
	}

	public DataTable readDbByTable(String table, String condition) {
		SqlTableReader reader = new SqlTableReader(getDataSource());
		reader.setTable(table, condition);
		return reader.read();
	}

	public DataTable readDbBySql(String sql, String tableName) {
		SqlTableReader reader = new SqlTableReader(getDataSource());
		reader.setSql(sql, tableName);
		return reader.read();
	}

	public void readXlsWriteDb(String path) {
		writeDb(readXls(path));
	}

	public void readXlsReplaceDb(String path) {
		DataSet dataSet = readXls(path);
		deleteDb(dataSet);
		writeDb(dataSet);
	}

	public void readXlsAllReplaceDb(String path) {
		DataSet dataSet = readXls(path);
		for (int i = dataSet.getTableSize() - 1; i >= 0; --i) {
			deleteTable(dataSet.getTable(i).getTableName());
		}
		writeDb(dataSet);
	}

	public DataSet reload(DataSet dataSet) {
		return new SqlReloadReader(getDataSource(), dataSet).read();
	}

	public DataTable reload(DataTable table) {
		return new SqlReloadTableReader(getDataSource(), table).read();
	}

	public void deleteDb(DataSet dataSet) {
		SqlDeleteTableWriter writer = new SqlDeleteTableWriter(getDataSource());
		for (int i = dataSet.getTableSize() - 1; i >= 0; --i) {
			writer.write(dataSet.getTable(i));
		}
	}

	public void deleteTable(String tableName) {
		UpdateHandler handler = new BasicUpdateHandler(getDataSource(),
				"DELETE FROM " + tableName);
		handler.execute(null);
	}

	public void assertEquals(DataSet expected, DataSet actual) {
		assertEquals(null, expected, actual);
	}

	public void assertEquals(String message, DataSet expected, DataSet actual) {
		message = message == null ? "" : message;
		assertEquals(message + ":TableSize", expected.getTableSize(), actual
				.getTableSize());
		for (int i = 0; i < expected.getTableSize(); ++i) {
			assertEquals(message, expected.getTable(i), actual.getTable(i));
		}
	}

	public void assertEquals(DataTable expected, DataTable actual) {
		assertEquals(null, expected, actual);
	}

	public void assertEquals(String message, DataTable expected,
			DataTable actual) {

		message = message == null ? "" : message;
		message = message + ":TableName=" + expected.getTableName();
		assertEquals(message + ":RowSize", expected.getRowSize(), actual
				.getRowSize());
		for (int i = 0; i < expected.getRowSize(); ++i) {
			DataRow expectedRow = expected.getRow(i);
			DataRow actualRow = actual.getRow(i);
			List errorMessages = new ArrayList();
			for (int j = 0; j < expected.getColumnSize(); ++j) {
				try {
					String columnName = expected.getColumnName(j);
					Object expectedValue = expectedRow.getValue(columnName);
					ColumnType ct = ColumnTypes.getColumnType(expectedValue);
					Object actualValue = actualRow.getValue(columnName);
					if (!ct.equals(expectedValue, actualValue)) {
						assertEquals(message + ":Row=" + i + ":columnName="
								+ columnName, expectedValue, actualValue);
					}
				} catch (AssertionFailedError e) {
					errorMessages.add(e.getMessage());
				}
			}
			if (!errorMessages.isEmpty()) {
				fail(message + errorMessages);
			}
		}
	}

	public void assertEquals(DataSet expected, Object actual) {
		assertEquals(null, expected, actual);
	}

	public void assertEquals(String message, DataSet expected, Object actual) {
		if (expected == null || actual == null) {
			Assert.assertEquals(message, expected, actual);
			return;
		}
		if (actual instanceof List) {
			List actualList = (List) actual;
			Assert.assertFalse(actualList.isEmpty());
			Object actualItem = actualList.get(0);
			if (actualItem instanceof Map) {
				assertMapListEquals(message, expected, actualList);
			} else {
				assertBeanListEquals(message, expected, actualList);
			}
		} else if (actual instanceof Object[]) {
			assertEquals(message, expected, Arrays.asList((Object[]) actual));
		} else {
			if (actual instanceof Map) {
				assertMapEquals(message, expected, (Map) actual);
			} else {
				assertBeanEquals(message, expected, actual);
			}
		}
	}

	protected void assertMapEquals(String message, DataSet expected, Map map) {

		MapReader reader = new MapReader(map);
		assertEquals(message, expected, reader.read());
	}

	protected void assertMapListEquals(String message, DataSet expected,
			List list) {

		MapListReader reader = new MapListReader(list);
		assertEquals(message, expected, reader.read());
	}

	protected void assertBeanEquals(String message, DataSet expected,
			Object bean) {

		BeanReader reader = new BeanReader(bean);
		assertEquals(message, expected, reader.read());
	}

	protected void assertBeanListEquals(String message, DataSet expected,
			List list) {

		BeanListReader reader = new BeanListReader(list);
		assertEquals(message, expected, reader.read());
	}
}