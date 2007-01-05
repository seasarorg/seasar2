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
package examples.aop;

import java.util.Date;

import org.seasar.framework.aop.Aspect;
import org.seasar.framework.aop.Pointcut;
import org.seasar.framework.aop.impl.AspectImpl;
import org.seasar.framework.aop.impl.PointcutImpl;
import org.seasar.framework.aop.interceptors.TraceInterceptor;
import org.seasar.framework.aop.proxy.AopProxy;

public class DateProxy {

    public static void main(String[] args) {
        Pointcut pointcut = new PointcutImpl(new String[] { "getTime" });
        Aspect aspect = new AspectImpl(new TraceInterceptor(), pointcut);
        AopProxy aopProxy = new AopProxy(Date.class, new Aspect[] { aspect });
        Date proxy = (Date) aopProxy.create();
        proxy.getTime();
        System.out.println(byte[].class.getName());
    }
}
