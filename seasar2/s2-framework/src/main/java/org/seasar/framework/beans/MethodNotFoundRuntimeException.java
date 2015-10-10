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
package org.seasar.framework.beans;

import java.lang.reflect.Method;

import org.seasar.framework.exception.SRuntimeException;
import org.seasar.framework.util.MethodUtil;

/**
 * {@link Method}が見つからなかったときにスローされる例外です。
 * 
 * @author higa
 * 
 */
public class MethodNotFoundRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = -3508955801981550317L;

    private Class targetClass;

    private String methodName;

    private Class[] methodArgClasses;

    /**
     * {@link MethodNotFoundRuntimeException}を作成します。
     * 
     * @param targetClass
     * @param methodName
     * @param methodArgs
     */
    public MethodNotFoundRuntimeException(Class targetClass, String methodName,
            Object[] methodArgs) {

        super("ESSR0049", new Object[] { targetClass.getName(),
                MethodUtil.getSignature(methodName, methodArgs) });
        this.targetClass = targetClass;
        this.methodName = methodName;
        if (methodArgs != null) {
            methodArgClasses = new Class[methodArgs.length];
            for (int i = 0; i < methodArgs.length; ++i) {
                if (methodArgs[i] != null) {
                    methodArgClasses[i] = methodArgs[i].getClass();
                }
            }
        }

    }

    /**
     * {@link MethodNotFoundRuntimeException}を作成します。
     * 
     * @param targetClass
     * @param methodName
     * @param methodArgClasses
     */
    public MethodNotFoundRuntimeException(Class targetClass, String methodName,
            Class[] methodArgClasses) {

        super("ESSR0049", new Object[] { targetClass.getName(),
                MethodUtil.getSignature(methodName, methodArgClasses) });
        this.targetClass = targetClass;
        this.methodName = methodName;
        this.methodArgClasses = methodArgClasses;
    }

    /**
     * ターゲットの{@link Class}を返します。
     * 
     * @return ターゲットの{@link Class}
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
     * メソッドの引数の{@link Class}の配列を返します。
     * 
     * @return メソッドの引数の{@link Class}の配列
     */
    public Class[] getMethodArgClasses() {
        return methodArgClasses;
    }

}
