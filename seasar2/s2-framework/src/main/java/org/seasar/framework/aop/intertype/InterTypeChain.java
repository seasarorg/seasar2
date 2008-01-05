/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.framework.aop.intertype;

import javassist.CtClass;

import org.seasar.framework.aop.InterType;
import org.seasar.framework.util.ArrayUtil;

/**
 * 複数の{@link InterType}をチェイン上につなぐ{@link InterType}です。
 * 
 */
public class InterTypeChain implements InterType {
    /**
     * インタータイプの配列です。
     */
    protected InterType[] interTypes = new InterType[0];

    /**
     * {@link InterTypeChain}を作成します。
     */
    public InterTypeChain() {
    }

    /**
     * {@link InterType}を追加します。
     * 
     * @param interType
     */
    public void add(final InterType interType) {
        interTypes = (InterType[]) ArrayUtil.add(interTypes, interType);
    }

    public void introduce(final Class targetClass, final CtClass enhancedClass) {
        for (int i = 0; i < interTypes.length; ++i) {
            interTypes[i].introduce(targetClass, enhancedClass);
        }
    }
}
