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

/**
 * クラスが見つからないときにスローされる例外です。
 * 
 * @author higa
 * 
 */
public class ClassNotFoundRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = -9022468864937761059L;

    private String className;

    /**
     * {@link ClassNotFoundRuntimeException}を作成します。
     * 
     * @param cause
     */
    public ClassNotFoundRuntimeException(ClassNotFoundException cause) {
        this(null, cause);
    }

    /**
     * {@link ClassNotFoundRuntimeException}を作成します。
     * 
     * @param className
     * @param cause
     */
    public ClassNotFoundRuntimeException(String className,
            ClassNotFoundException cause) {
        super("ESSR0044", new Object[] { cause }, cause);
        setClassName(className);
    }

    /**
     * クラス名を返します。
     * 
     * @return
     */
    public String getClassName() {
        return className;
    }

    /**
     * クラス名を設定します。
     * 
     * @param className
     *            クラス名
     */
    protected void setClassName(String className) {
        this.className = className;
    }
}
