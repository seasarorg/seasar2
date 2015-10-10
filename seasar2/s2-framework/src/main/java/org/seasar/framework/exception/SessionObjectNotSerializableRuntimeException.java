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

import java.io.NotSerializableException;
import java.io.Serializable;

/**
 * {@link NotSerializableException}をラップする例外です。
 * 
 * @author koichik
 */
public class SessionObjectNotSerializableRuntimeException extends
        SRuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * {@link SessionObjectNotSerializableRuntimeException}を作成します。
     * 
     * @param clazz
     *            {@link Serializable}でないクラス
     */
    public SessionObjectNotSerializableRuntimeException(final Class clazz) {
        super("ESSR0099", new Object[] { clazz });
    }

    /**
     * {@link SessionObjectNotSerializableRuntimeException}を作成します。
     * 
     * @param cause
     *            原因となった例外
     */
    public SessionObjectNotSerializableRuntimeException(
            final NotSerializableException cause) {
        super("ESSR0100", new Object[] { cause }, cause);
    }
}
