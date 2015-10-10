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
package org.seasar.extension.jdbc.gen.extension.svn;

import org.seasar.framework.exception.SRuntimeException;
import org.tmatesoft.svn.core.SVNException;

/**
 * Subversionの捜査中に例外が発生した場合にスローされる例外です。
 * 
 * @author koichik
 */
public class SvnRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * インスタンスを構築します。
     * 
     * @param cause
     *            原因となった{@link SVNException}
     */
    public SvnRuntimeException(final SVNException cause) {
        super("ES2JDBCGen0029", new Object[] { cause.getMessage() }, cause);
    }

}
