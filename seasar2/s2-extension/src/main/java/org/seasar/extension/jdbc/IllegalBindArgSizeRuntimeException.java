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
package org.seasar.extension.jdbc;

import org.seasar.framework.exception.SRuntimeException;

/**
 * <p>
 * バインド変数に対応する引数の数が不正な場合の例外です。
 * </p>
 * 
 * @author taedium
 * 
 */
public class IllegalBindArgSizeRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    private String sql;

    private int argSize;

    /**
     * 
     * @param sql
     *            SQL
     * @param argSize
     *            バインド変数に対する引数の数
     */
    public IllegalBindArgSizeRuntimeException(String sql, int argSize) {
        super("ESSR0095", new Object[] { sql, String.valueOf(argSize) });
        this.sql = sql;
        this.argSize = argSize;
    }

    /**
     * SQLを返します。
     * 
     * @return SQL
     */
    public String getSql() {
        return sql;
    }

    /**
     * バインド変数に対する引数の数を返します。
     * 
     * @return バインド変数に対する引数の数
     */
    public int getArgSize() {
        return argSize;
    }

}
