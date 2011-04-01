/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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

import java.lang.instrument.IllegalClassFormatException;

/**
 * {@link IllegalClassFormatException}をラップする実行時例外クラスです。
 * 
 * @author taedium
 */
public class IllegalClassFormatRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 2731688670094086529L;

    private String className;

    /**
     * インスタンスを構築します。
     * 
     * @param cause
     *            原因
     */
    public IllegalClassFormatRuntimeException(
            final IllegalClassFormatException cause) {
        this(null, cause);
    }

    /**
     * インスタンスを構築します。
     * 
     * @param className
     *            クラス名
     * @param cause
     *            原因
     */
    public IllegalClassFormatRuntimeException(final String className,
            final IllegalClassFormatException cause) {
        super("ESSR0092", new Object[] { cause }, cause);
        setClassName(className);
    }

    /**
     * クラス名を設定します。
     * 
     * @return クラス名
     */
    public String getClassName() {
        return className;
    }

    /**
     * クラス名を取得します。
     * 
     * @param className
     *            クラス名
     */
    public void setClassName(String className) {
        this.className = className;
    }
}
