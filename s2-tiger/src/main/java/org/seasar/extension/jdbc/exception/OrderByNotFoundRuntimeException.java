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
package org.seasar.extension.jdbc.exception;

import org.seasar.framework.exception.SRuntimeException;

/**
 * <code>order by</code>が見つからない場合の例外です。
 * 
 * @author higa
 * 
 */
public class OrderByNotFoundRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    private String sql;

    /**
     * {@link OrderByNotFoundRuntimeException}を作成します。
     * 
     * @param sql
     *            SQL
     */
    public OrderByNotFoundRuntimeException(String sql) {
        super("ESSR0702", new Object[] { sql });
        this.sql = sql;
    }

    /**
     * SQLを返します。
     * 
     * @return SQL
     */
    public String getSql() {
        return sql;
    }
}