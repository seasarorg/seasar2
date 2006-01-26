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
package org.seasar.extension.dataset.impl;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.seasar.extension.dataset.DataReader;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.TableReader;

/**
 * @author higa
 *
 */
public class SqlReader implements DataReader {

	private DataSource dataSource_;
	private List tableReaders_ = new ArrayList();
	
	public SqlReader(DataSource dataSource) {
		dataSource_ = dataSource;
	}
	
	public DataSource getDataSource() {
		return dataSource_;
	}
	
	public void addTable(String tableName) {
		addTable(tableName, null);
	}

	public void addTable(String tableName, String condition) {
		SqlTableReader reader = new SqlTableReader(dataSource_);
		reader.setTable(tableName, condition);
		tableReaders_.add(reader);
	}
	
	public void addSql(String sql, String tableName) {
		SqlTableReader reader = new SqlTableReader(dataSource_);
		reader.setSql(sql, tableName);
		tableReaders_.add(reader);
	}

	/**
	 * @see org.seasar.extension.dataset.DataReader#read()
	 */
	public DataSet read() {
		DataSet dataSet = new DataSetImpl();
		for (int i = 0; i < tableReaders_.size(); ++i) {
			TableReader reader = (TableReader) tableReaders_.get(i);
			dataSet.addTable(reader.read());
		}
		return dataSet;
	}

}
