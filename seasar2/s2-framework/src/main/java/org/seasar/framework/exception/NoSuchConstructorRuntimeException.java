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
package org.seasar.framework.exception;

import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * @author higa
 * 
 */
public class NoSuchConstructorRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 8688818589925114466L;

    private Class targetClass;

    private Class[] argTypes;

    public NoSuchConstructorRuntimeException(Class targetClass,
            Class[] argTypes, NoSuchMethodException cause) {

        super("ESSR0064", new Object[] {
                targetClass.getName(),
                MethodUtil.getSignature(ClassUtil
                        .getShortClassName(targetClass), argTypes), cause },
                cause);
        this.targetClass = targetClass;
        this.argTypes = argTypes;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public Class[] getArgTypes() {
        return argTypes;
    }
}
