/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import org.aopalliance.intercept.MethodInterceptor;
import org.seasar.framework.aop.Aspect;
import org.seasar.framework.aop.Pointcut;
import org.seasar.framework.aop.impl.AspectImpl;
import org.seasar.framework.container.AspectDef;

/**
 * {@link AspectDef}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class AspectDefImpl extends ArgDefImpl implements AspectDef {

    private Pointcut pointcut;

    /**
     * {@link AspectDefImpl}を作成します。
     */
    public AspectDefImpl() {
    }

    /**
     * {@link AspectDefImpl}を作成します。
     * 
     * @param pointcut
     */
    public AspectDefImpl(Pointcut pointcut) {
        setPointcut(pointcut);
    }

    /**
     * {@link AspectDefImpl}を作成します。
     * 
     * @param interceptor
     */
    public AspectDefImpl(MethodInterceptor interceptor) {
        setValue(interceptor);
    }

    /**
     * {@link AspectDefImpl}を作成します。
     * 
     * @param interceptor
     * @param pointcut
     */
    public AspectDefImpl(MethodInterceptor interceptor, Pointcut pointcut) {
        setValue(interceptor);
        setPointcut(pointcut);
    }

    public Pointcut getPointcut() {
        return pointcut;
    }

    public void setPointcut(Pointcut pointcut) {
        this.pointcut = pointcut;
    }

    /**
     * @see org.seasar.framework.container.AspectDef#getAspect()
     */
    public Aspect getAspect() {
        MethodInterceptor interceptor = (MethodInterceptor) getValue();
        return new AspectImpl(interceptor, pointcut);
    }
}
