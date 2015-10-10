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

import org.seasar.framework.util.StringUtil;

/**
 * <code>OgnlException</code>をラップする例外です。
 * 
 * @author higa
 * @author manhole
 */
public class OgnlRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 2290558407298004909L;

    /**
     * {@link OgnlRuntimeException}を作成します。
     * 
     * @param cause
     */
    public OgnlRuntimeException(Throwable cause) {
        this(cause, null, 0);
    }

    /**
     * {@link OgnlRuntimeException}を作成します。
     * 
     * @param cause
     * @param path
     * @param lineNumber
     */
    public OgnlRuntimeException(Throwable cause, String path, int lineNumber) {
        super("ESSR0073",
                new Object[] { createMessage(cause, path, lineNumber) }, cause);
    }

    /**
     * メッセージを作成します。
     * 
     * @param cause
     *            原因
     * @param path
     *            パス
     * @param lineNumber
     *            行番号
     * @return メッセージ
     */
    protected static String createMessage(Throwable cause, String path,
            int lineNumber) {
        StringBuffer buf = new StringBuffer(100);
        buf.append(cause.getMessage());
        if (!StringUtil.isEmpty(path)) {
            buf.append(" at ").append(path).append("(").append(lineNumber)
                    .append(")");
        }
        return new String(buf);
    }
}
