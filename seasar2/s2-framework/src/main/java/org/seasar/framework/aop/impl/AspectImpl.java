/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
package org.seasar.framework.aop.impl;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInterceptor;
import org.seasar.framework.aop.Aspect;
import org.seasar.framework.aop.Pointcut;

/**
 * {@link Aspect}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class AspectImpl implements Aspect, Serializable {

    static final long serialVersionUID = 0L;

    private MethodInterceptor methodInterceptor;

    private Pointcut pointcut;

    /**
     * {@link AspectImpl}を作成します。
     * 
     * @param methodInterceptor
     */
    public AspectImpl(MethodInterceptor methodInterceptor) {
        this(methodInterceptor, null);
    }

    /**
     * {@link AspectImpl}を作成します。
     * 
     * @param methodInterceptor
     * @param pointcut
     */
    public AspectImpl(MethodInterceptor methodInterceptor, Pointcut pointcut) {
        this.methodInterceptor = methodInterceptor;
        this.pointcut = pointcut;
    }

    /**
     * @see org.seasar.framework.aop.Aspect#getMethodInterceptor()
     */
    public MethodInterceptor getMethodInterceptor() {
        return methodInterceptor;
    }

    /**
     * @see org.seasar.framework.aop.Aspect#getPointcut()
     */
    public Pointcut getPointcut() {
        return pointcut;
    }

    /**
     * @see org.seasar.framework.aop.Aspect#setPointcut(org.seasar.framework.aop.Pointcut)
     */
    public void setPointcut(Pointcut pointcut) {
        this.pointcut = pointcut;
    }

}
