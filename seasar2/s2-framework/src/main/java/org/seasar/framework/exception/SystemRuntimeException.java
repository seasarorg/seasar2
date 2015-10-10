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

import javax.transaction.SystemException;

/**
 * {@link SystemException}をラップする例外です。
 * 
 * @author higa
 * 
 */
public class SystemRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 7215695745074415461L;

    /**
     * {@link SystemRuntimeException}を作成します。
     * 
     * @param cause
     */
    public SystemRuntimeException(SystemException cause) {
        super("ESSR0061", new Object[] { cause }, cause);
    }
}
