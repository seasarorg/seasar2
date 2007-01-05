/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import org.seasar.framework.exception.SRuntimeException;
import org.seasar.framework.util.MethodUtil;

/**
 * @author higa
 * 
 */
public final class MethodNotFoundRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = -3508955801981550317L;

    private Class targetClass_;

    private String methodName_;

    private Class[] methodArgClasses_;

    public MethodNotFoundRuntimeException(Class targetClass, String methodName,
            Object[] methodArgs) {

        super("ESSR0049", new Object[] { targetClass.getName(),
                MethodUtil.getSignature(methodName, methodArgs) });
        targetClass_ = targetClass;
        methodName_ = methodName;
        if (methodArgs != null) {
            methodArgClasses_ = new Class[methodArgs.length];
            for (int i = 0; i < methodArgs.length; ++i) {
                if (methodArgs[i] != null) {
                    methodArgClasses_[i] = methodArgs[i].getClass();
                }
            }
        }

    }

    public MethodNotFoundRuntimeException(Class targetClass, String methodName,
            Class[] methodArgClasses) {

        super("ESSR0049", new Object[] { targetClass.getName(),
                MethodUtil.getSignature(methodName, methodArgClasses) });
        targetClass_ = targetClass;
        methodName_ = methodName;
        methodArgClasses_ = methodArgClasses;
    }

    public Class getTargetClass() {
        return targetClass_;
    }

    public String getMethodName() {
        return methodName_;
    }

    public Class[] getMethodArgClasses() {
        return methodArgClasses_;
    }

}
