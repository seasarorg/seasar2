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

import java.lang.reflect.InvocationTargetException;

/**
 * {@link InvocationTargetException}をラップする例外です。
 * 
 * @author higa
 * 
 */
public class InvocationTargetRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 7760491787158046906L;

    private Class targetClass;

    /**
     * {@link InvocationTargetRuntimeException}を作成します。
     * 
     * @param targetClass
     * @param cause
     */
    public InvocationTargetRuntimeException(Class targetClass,
            InvocationTargetException cause) {

        super("ESSR0043", new Object[] { targetClass.getName(),
                cause.getTargetException() }, cause.getTargetException());
        this.targetClass = targetClass;
    }

    /**
     * ターゲットのクラスを返します。
     * 
     * @return
     */
    public Class getTargetClass() {
        return targetClass;
    }
}