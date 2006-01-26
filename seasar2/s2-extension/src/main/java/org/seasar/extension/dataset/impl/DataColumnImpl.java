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

import org.seasar.extension.dataset.ColumnType;
import org.seasar.extension.dataset.DataColumn;

/**
 * @author higa
 *
 */
public class DataColumnImpl implements DataColumn {

	private String columnName_;
	private ColumnType columnType_;
	private int columnIndex_;
	private boolean primaryKey_ = false;
	private boolean writable_ = true;
	private String formatPattern_;
	
	public DataColumnImpl(String columnName, ColumnType columnType,
			int columnIndex) {
		
		columnName_ = columnName;
		columnType_ = columnType;
		columnIndex_ = columnIndex;
	}

	/**
	 * @see org.seasar.extension.dataset.DataColumn#getColumnName()
	 */
	public String getColumnName() {
		return columnName_;
	}
	
	/**
	 * @see org.seasar.extension.dataset.DataColumn#getColumnType()
	 */
	public ColumnType getColumnType() {
		return columnType_;
	}
	
	/**
	 * @see org.seasar.extension.dataset.DataColumn#getColumnIndex()
	 */
	public int getColumnIndex() {
		return columnIndex_;
	}
	
	/**
	 * @see org.seasar.extension.dataset.DataColumn#isPrimaryKey()
	 */
	public boolean isPrimaryKey() {
		return primaryKey_;
	}
	
	/**
	 * @see org.seasar.extension.dataset.DataColumn#setPrimaryKey(boolean)
	 */
	public void setPrimaryKey(boolean primaryKey) {
		primaryKey_ = primaryKey;
	}
	
	/**
	 * @see org.seasar.extension.dataset.DataColumn#isWritable()
	 */
	public boolean isWritable() {
		return writable_;
	}
	
	/**
	 * @see org.seasar.extension.dataset.DataColumn#setWritable(boolean)
	 */
	public void setWritable(boolean writable) {
		writable_ = writable;
	}
	
	/**
	 * @see org.seasar.extension.dataset.DataColumn#getFormatPattern()
	 */
	public String getFormatPattern() {
		return formatPattern_;
	}
	
	/**
	 * @see org.seasar.extension.dataset.DataColumn#setFormatPattern(java.lang.String)
	 */
	public void setFormatPattern(String formatPattern) {
		formatPattern_ = formatPattern;
	}
	
	public Object convert(Object value) {
		return columnType_.convert(value, formatPattern_);
	}
}