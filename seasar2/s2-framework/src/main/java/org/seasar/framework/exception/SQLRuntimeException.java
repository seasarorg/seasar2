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
package org.seasar.framework.exception;

import java.sql.SQLException;

import org.seasar.framework.message.MessageFormatter;

/**
 * {@link SQLException}をラップする例外です。
 * 
 * @author higa
 * @author manhole
 */
public class SQLRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 2533513110369526191L;

    /**
     * {@link SQLRuntimeException}を作成します。
     * 
     * @param cause
     */
    public SQLRuntimeException(SQLException cause) {
        super("ESSR0072", new Object[] { getSql(cause), getRealMessage(cause),
                Integer.toString(cause.getErrorCode()), cause.getSQLState() },
                cause);
    }

    /**
     * <code>SQL</code>を返します。
     * 
     * @param cause
     *            原因
     * @return <code>SQL</code>
     */
    protected static String getSql(SQLException cause) {
        if (cause instanceof SSQLException) {
            return ((SSQLException) cause).getSql();
        }
        return "";
    }

    /**
     * 本当のメッセージを返します。
     * 
     * @param cause
     * @return 本当のメッセージ
     */
    protected static String getRealMessage(SQLException cause) {
        StringBuffer buf = new StringBuffer(256);
        buf.append(cause.getMessage()).append(" : [");
        SQLException next = cause.getNextException();
        while (next != null) {
            buf.append(
                    MessageFormatter.getSimpleMessage("ESSR0071", new Object[] {
                            next.getMessage(),
                            Integer.toString(next.getErrorCode()),
                            next.getSQLState() })).append("], [");
            next = next.getNextException();
        }
        Throwable t = cause.getCause();
        while (t != null) {
            buf.append(t.getMessage()).append("], [");
            t = t.getCause();
        }
        buf.setLength(buf.length() - 4);
        return new String(buf);
    }
}