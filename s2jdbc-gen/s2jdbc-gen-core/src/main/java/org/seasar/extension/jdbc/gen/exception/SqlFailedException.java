/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.exception;

import java.sql.SQLException;

import org.seasar.framework.exception.SRuntimeException;

/**
 * @author taedium
 * 
 */
public class SqlFailedException extends SRuntimeException {

    protected String fileName;

    protected String sql;

    /**
     * @param cause
     * @param fileName
     * @param sql
     */
    public SqlFailedException(SQLException cause, String fileName, String sql) {
        super("ES2JDBCGen0003", new Object[] { fileName, sql, cause }, cause);
        this.fileName = fileName;
        this.sql = sql;
    }

    /**
     * @return Returns the sqlFileName.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return Returns the sql.
     */
    public String getSql() {
        return sql;
    }

}
