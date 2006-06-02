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
package org.seasar.framework.exception;

import java.sql.SQLException;

import org.seasar.framework.message.MessageFormatter;

/**
 * @author higa
 * 
 */
public class SSQLException extends SQLException {

    private static final long serialVersionUID = 4098267431221202677L;

    private String messageCode;

    private String sql;

    private Object[] args;

    public SSQLException(String messageCode, Object[] args) {
        this(messageCode, args, null, 0, null);
    }

    public SSQLException(String messageCode, Object[] args, Throwable cause) {
        this(messageCode, args, null, 0, cause);
    }

    public SSQLException(String messageCode, Object[] args, String sqlState) {
        this(messageCode, args, sqlState, 0, null);
    }

    public SSQLException(String messageCode, Object[] args, String sqlState,
            Throwable cause) {
        this(messageCode, args, sqlState, 0, cause);
    }

    public SSQLException(String messageCode, Object[] args, String sqlState,
            int vendorCode) {
        this(messageCode, args, sqlState, vendorCode, null);
    }

    public SSQLException(String messageCode, Object[] args, String sqlState,
            int vendorCode, Throwable cause) {
        this(messageCode, args, sqlState, vendorCode, cause, null);
    }

    public SSQLException(String messageCode, Object[] args, String sqlState,
            int vendorCode, Throwable cause, String sql) {
        super(MessageFormatter.getMessage(messageCode, args), sqlState,
                vendorCode);
        this.messageCode = messageCode;
        this.args = args;
        this.sql = sql;
        initCause(cause);
        if (cause instanceof SQLException) {
            setNextException((SQLException) cause);
        }
    }

    public String getMessageCode() {
        return messageCode;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getSql() {
        return sql;
    }
}
