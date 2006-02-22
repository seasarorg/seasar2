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
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.seasar.framework.util.ArrayUtil;
import org.seasar.framework.util.MethodUtil;

public class HotswapProxy extends Hotswap implements InvocationHandler,
        Serializable {

    final static long serialVersionUID = 0L;

    private transient HotswapTargetFactory hotswapTargetFactory;

    public HotswapProxy() {
    }

    protected HotswapProxy(Class targetClass,
            HotswapTargetFactory hotswapTargetFactory) {
        super(targetClass);
        this.hotswapTargetFactory = hotswapTargetFactory;
    }

    public static Object create(Class targetClass,
            HotswapTargetFactory hotswapTargetFactory) {
        return create(targetClass, hotswapTargetFactory, Thread.currentThread()
                .getContextClassLoader());
    }

    public static Object create(Class targetClass,
            HotswapTargetFactory hotswapTargetFactory, ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader, getInterfaces(targetClass),
                new HotswapProxy(targetClass, hotswapTargetFactory));
    }
    
    static Class[] getInterfaces(Class targetClass) {
        Class[] intfs = targetClass.getInterfaces();
        if (targetClass.isInterface()) {
            Class[] intfs2 = new Class[intfs.length + 1];
            intfs2[0] = targetClass;
            System.arraycopy(intfs, 0, intfs2, 1, intfs.length);
            return intfs2;
        } else if (!targetClass.equals(Object.class)) {
            intfs = (Class[]) ArrayUtil.add(intfs, getInterfaces(targetClass
                    .getSuperclass()));
        }
        return intfs;
    }

    public static HotswapProxy getProxy(Object o) {
        if (Proxy.isProxyClass(o.getClass())) {
            Object ih = Proxy.getInvocationHandler(o);
            if (ih instanceof HotswapProxy) {
                return (HotswapProxy) ih;
            }
        }
        return null;
    }

    public HotswapTargetFactory getHotswapTargetFactory() {
        return hotswapTargetFactory;
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {

        Object target = hotswapTargetFactory.updateTarget();
        if (MethodUtil.isEqualsMethod(method)) {
            return equals(args[0]) ? Boolean.TRUE : Boolean.FALSE;
        }
        if (MethodUtil.isHashCodeMethod(method)) {
            return new Integer(target.hashCode());
        }
        if (MethodUtil.isToStringMethod(method)) {
            return target.toString();
        }
        return method.invoke(target, args);
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (Proxy.isProxyClass(o.getClass())) {
            o = Proxy.getInvocationHandler(o);
        }
        return this == o;
    }
}