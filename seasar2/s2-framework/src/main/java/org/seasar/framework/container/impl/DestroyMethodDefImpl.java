/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.impl;

import java.lang.reflect.Method;

import org.seasar.framework.container.DestroyMethodDef;
import org.seasar.framework.container.MethodDef;

/**
 * {@link DestroyMethodDef}の実装クラスです。
 * 
 * @author higa
 * @see MethodDef
 * 
 */
public class DestroyMethodDefImpl extends MethodDefImpl implements
        DestroyMethodDef {

    /**
     * {@link DestroyMethodDefImpl}を作成します。
     */
    public DestroyMethodDefImpl() {
    }

    /**
     * {@link DestroyMethodDefImpl}を作成します。
     * 
     * @param method
     */
    public DestroyMethodDefImpl(Method method) {
        super(method);
    }

    /**
     * {@link DestroyMethodDefImpl}を作成します。
     * 
     * @param methodName
     */
    public DestroyMethodDefImpl(String methodName) {
        super(methodName);
    }

}
