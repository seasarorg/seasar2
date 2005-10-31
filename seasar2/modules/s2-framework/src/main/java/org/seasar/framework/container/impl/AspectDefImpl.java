/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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
 * @author higa
 *
 */
public class AspectDefImpl extends ArgDefImpl implements AspectDef {

	private Pointcut pointcut_;

	public AspectDefImpl() {
	}

	public AspectDefImpl(Pointcut pointcut) {
		pointcut_ = pointcut;
	}
	
	public AspectDefImpl(MethodInterceptor interceptor) {
		setValue(interceptor);
	}

	public AspectDefImpl(MethodInterceptor interceptor, Pointcut pointcut) {
		setValue(interceptor);
		pointcut_ = pointcut;
	}
    
    public Pointcut getPointcut() {
        return pointcut_;
    }

    public void setPointcut(Pointcut pointcut) {
        pointcut_ = pointcut;
    }

	/**
	 * @see org.seasar.framework.container.AspectDef#getAspect()
	 */
	public Aspect getAspect() {
		MethodInterceptor interceptor = (MethodInterceptor) getValue();
		return new AspectImpl(interceptor, pointcut_);
	}
}
