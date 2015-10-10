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

import org.seasar.framework.util.MethodUtil;

/**
 * {@link NoSuchMethodException}をラップする例外です。
 * 
 * @author higa
 * 
 */
public class NoSuchMethodRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = -5673845060079098617L;

    private Class targetClass;

    private String methodName;

    private Class[] argTypes;

    /**
     * {@link NoSuchMethodRuntimeException}を作成します。
     * 
     * @param targetClass
     * @param methodName
     * @param argTypes
     * @param cause
     */
    public NoSuchMethodRuntimeException(Class targetClass, String methodName,
            Class[] argTypes, NoSuchMethodException cause) {

        super("ESSR0057", new Object[] { targetClass.getName(),
                MethodUtil.getSignature(methodName, argTypes), cause }, cause);
        this.targetClass = targetClass;
        this.methodName = methodName;
        this.argTypes = argTypes;
    }

    /**
     * ターゲットのクラスを返します。
     * 
     * @return ターゲットのクラス
     */
    public Class getTargetClass() {
        return targetClass;
    }

    /**
     * メソッド名を返します。
     * 
     * @return メソッド名
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * 引数の型の配列を返します。
     * 
     * @return 引数の型の配列
     */
    public Class[] getArgTypes() {
        return argTypes;
    }
}