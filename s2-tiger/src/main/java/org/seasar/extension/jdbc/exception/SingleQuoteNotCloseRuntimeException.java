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
 * プロパティが見つからない場合の例外です。
 * 
 * @author higa
 * 
 */
public class SingleQuoteNotCloseRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    private String target;

    /**
     * {@link SingleQuoteNotCloseRuntimeException}を作成します。
     * 
     * @param target
     *            対象の文字列
     */
    public SingleQuoteNotCloseRuntimeException(String target) {
        super("ESSR0733", new Object[] { target });
        this.target = target;
    }

    /**
     * 対象の文字列を返します。
     * 
     * @return 対象の文字列
     */
    public String getTarget() {
        return target;
    }
}