/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.hotswap;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.Aspect;
import org.seasar.framework.aop.impl.AspectImpl;
import org.seasar.framework.aop.proxy.AopProxy;

public class Greeting2HotswapTargetFactory implements HotswapTargetFactory,
        MethodInterceptor, Serializable {

    final static long serialVersionUID = 0L;
    
    private Hotswap hotswap;
    
    private Object target;
    
    public Greeting2HotswapTargetFactory() {
        this(new Hotswap(Greeting2.class));
    }
    
    public Greeting2HotswapTargetFactory(Hotswap hotswap) {
        this.hotswap = hotswap;
        AopProxy proxy = new AopProxy(Greeting2.class,
                new Aspect[]{new AspectImpl(this)});
        target = proxy.create();
    }
    
    public Hotswap getHotswap() {
        return hotswap;
    }
    
    public Object getTarget() {
        return target;
    }

    public Object updateTarget() {
        return target;
    }

    public Object invoke(MethodInvocation arg0) throws Throwable {
        return "Hello";
    }
}
