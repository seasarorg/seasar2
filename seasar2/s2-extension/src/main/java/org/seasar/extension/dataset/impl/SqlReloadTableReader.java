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
package org.seasar.extension.dataset.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.seasar.extension.dataset.DataColumn;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.TableReader;
import org.seasar.extension.jdbc.SelectHandler;
import org.seasar.extension.jdbc.impl.BasicSelectHandler;
import org.seasar.framework.util.ConnectionUtil;
import org.seasar.framework.util.DataSourceUtil;

/**
 * @author higa
 *
 */
public class SqlReloadTableReader implements TableReader {

	private DataSource dataSource_;
	private DataTable table_;
	private String sql_;
	private String[] primaryKeys_;
	
	public SqlReloadTableReader(DataSource dataSource,
			DataTable table) {

		dataSource_ = dataSource;
		table_ = table;
		Connection con = DataSourceUtil.getConnection(dataSource);
		try {
			DatabaseMetaData dbMetaData = ConnectionUtil.getMetaData(con);
			table_.setupMetaData(dbMetaData);
		} finally {
			ConnectionUtil.close(con);
		}
		setup();
	}
	
	private void setup() {
		StringBuffer buf = new StringBuffer(100);
		buf.append("SELECT ");
		StringBuffer whereBuf = new StringBuffer(100);
		whereBuf.append(" WHERE");
		List primaryKeyList = new ArrayList();
		for (int i = 0; i < table_.getColumnSize(); ++i) {
			DataColumn column = table_.getColumn(i);
			buf.append(column.getColumnName());
			buf.append(", ");
			if (column.isPrimaryKey()) {
				whereBuf.append(" ");
				whereBuf.append(column.getColumnName());
				whereBuf.append(" = ? AND");
				primaryKeyList.add(column.getColumnName());
			}
		}
		buf.setLength(buf.length() - 2);
		whereBuf.setLength(whereBuf.length() - 4);
		buf.append(" FROM ");
		buf.append(table_.getTableName());
		buf.append(whereBuf);
		sql_ = buf.toString();
		primaryKeys_ = (String[]) primaryKeyList.toArray(new String[primaryKeyList.size()]);
	}
	
	public DataSource getDataSource() {
		return dataSource_;
	}
	
	public DataTable getTable() {
		return table_;
	}

	/**
	 * @see org.seasar.extension.dataset.TableReader#read()
	 */
	public DataTable read() {
		DataTable newTable = new DataTableImpl(table_.getTableName());
		for (int i = 0; i < table_.getColumnSize(); ++i) {
			DataColumn column = table_.getColumn(i);
			newTable.addColumn(column.getColumnName(), column.getColumnType());
		}
		for (int i = 0; i < table_.getRowSize(); ++i) {
			DataRow row = table_.getRow(i);
			DataRow newRow = newTable.addRow();
			reload(row, newRow);
		}
		return newTable;
	}
	
	protected void reload(DataRow row, DataRow newRow) {
		SelectHandler selectHandler = new BasicSelectHandler(
				dataSource_, sql_, new DataRowReloadResultSetHandler(row, newRow));
		Object[] args = new Object[primaryKeys_.length];
		for (int i = 0; i < primaryKeys_.length; ++i) {
			args[i] = row.getValue(primaryKeys_[i]);
		}
		selectHandler.execute(args);
	}

}
