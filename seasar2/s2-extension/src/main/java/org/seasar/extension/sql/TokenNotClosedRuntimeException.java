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
package org.seasar.extension.sql;

import org.seasar.framework.exception.SRuntimeException;

/**
 * トークンが閉じていない場合の例外クラスです。
 * 
 * @author higa
 * 
 */
public class TokenNotClosedRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    private String token;

    private String sql;

    /**
     * <code>TokenNotClosedRuntimeException</code>を返します。
     * 
     * @param token
     * @param sql
     */
    public TokenNotClosedRuntimeException(String token, String sql) {
        super("ESSR0087", new Object[] { token, sql });
        this.token = token;
        this.sql = sql;
    }

    /**
     * トークンを返します。
     * 
     * @return
     */
    public String getToken() {
        return token;
    }

    /**
     * SQLを返します。
     * 
     * @return
     */
    public String getSql() {
        return sql;
    }
}