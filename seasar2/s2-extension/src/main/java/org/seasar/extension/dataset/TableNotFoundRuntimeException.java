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
package org.seasar.extension.dataset;

import org.seasar.framework.exception.SRuntimeException;

/**
 * @author higa
 *
 */
public final class TableNotFoundRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = -8455516906109819288L;

	private String tableName_;
	
	/**
	 * @param componentKey
	 */
	public TableNotFoundRuntimeException(String tableName) {
		super("ESSR0067", new Object[] { tableName });
		tableName_ = tableName;
	}
	
	public String getTableName() {
		return tableName_;
	}
}