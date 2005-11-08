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
package org.seasar.extension.dataset;

import java.sql.DatabaseMetaData;

import org.seasar.extension.jdbc.ColumnNotFoundRuntimeException;

/**
 * @author higa
 *
 */
public interface DataTable {

	public String getTableName();
	
	public void setTableName(String tableName);
	
	public int getRowSize();
	
	public DataRow getRow(int index);
	
	public DataRow addRow();
	
	public int getRemovedRowSize();
	
	public DataRow getRemovedRow(int index);

	public DataRow[] removeRows();
	
	public int getColumnSize();
	
	public DataColumn getColumn(int index);
	
	public DataColumn getColumn(String columnName)
		throws ColumnNotFoundRuntimeException;
		
	public boolean hasColumn(String columnName);
	
	public String getColumnName(int index);
	
	public ColumnType getColumnType(int index);
	
	public ColumnType getColumnType(String columnName);
	
	public DataColumn addColumn(String columnName);

	public DataColumn addColumn(String columnName, ColumnType columnType);
	
	public boolean hasMetaData();
	
	public void setupMetaData(DatabaseMetaData dbMetaData);
	
	public void setupColumns(Class beanClass);
	
	public void copyFrom(Object source);
}
