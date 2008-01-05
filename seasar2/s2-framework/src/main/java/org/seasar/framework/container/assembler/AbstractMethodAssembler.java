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
package org.seasar.framework.container.assembler;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.IllegalMethodRuntimeException;
import org.seasar.framework.container.MethodAssembler;
import org.seasar.framework.container.MethodDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.util.BindingUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * メソッドアセンブラの抽象クラスです。
 * 
 * @author higa
 * 
 */
public abstract class AbstractMethodAssembler extends AbstractAssembler
        implements MethodAssembler {

    /**
     * @param componentDef
     */
    public AbstractMethodAssembler(ComponentDef componentDef) {
        super(componentDef);
    }

    /**
     * 定義されたメソッドを呼び出します。
     * 
     * @param beanDesc
     * @param component
     * @param methodDef
     * @throws IllegalMethodRuntimeException
     *             メソッド呼び出しで例外が発生した場合
     */
    protected void invoke(BeanDesc beanDesc, Object component,
            MethodDef methodDef) throws IllegalMethodRuntimeException {

        Method method = methodDef.getMethod();
        if (method != null) {
            if (!Modifier.isPublic(method.getModifiers())
                    && !method.isAccessible()) {
                method.setAccessible(true);
            }
            try {
                MethodUtil.invoke(method, component, methodDef.getArgs());
            } catch (Exception cause) {
                throw new IllegalMethodRuntimeException(
                        getComponentClass(component), method.getName(), cause);
            }
            return;
        }

        String methodName = methodDef.getMethodName();
        if (methodName != null) {
            Object[] args = null;
            try {
                if (methodDef.getArgDefSize() > 0) {
                    args = methodDef.getArgs();
                } else {
                    Method[] methods = beanDesc.getMethods(methodName);
                    method = getSuitableMethod(methods);
                    if (method != null) {
                        args = getArgs(method.getParameterTypes());
                    }
                }
            } catch (ComponentNotFoundRuntimeException cause) {
                throw new IllegalMethodRuntimeException(
                        getComponentClass(component), methodName, cause);
            }
            if (method != null) {
                MethodUtil.invoke(method, component, args);
            } else {
                invoke(beanDesc, component, methodName, args);
            }
            return;
        }

        invokeExpression(component, methodDef);
    }

    private void invokeExpression(Object component, MethodDef methodDef) {
        Map ctx = new HashMap();
        ctx.put("self", component);
        ctx.put("out", System.out);
        ctx.put("err", System.err);
        S2Container container = getComponentDef().getContainer();
        methodDef.getExpression().evaluate(container, ctx);
    }

    private Method getSuitableMethod(Method[] methods) {
        int argSize = -1;
        Method method = null;
        for (int i = 0; i < methods.length; ++i) {
            int tempArgSize = methods[i].getParameterTypes().length;
            if (tempArgSize > argSize
                    && BindingUtil.isAutoBindable(methods[i]
                            .getParameterTypes())) {
                method = methods[i];
                argSize = tempArgSize;
            }
        }
        return method;
    }

    private void invoke(BeanDesc beanDesc, Object component, String methodName,
            Object[] args) throws IllegalMethodRuntimeException {

        try {
            beanDesc.invoke(component, methodName, args);
        } catch (NumberFormatException ex) {
            throw new IllegalMethodRuntimeException(getComponentDef()
                    .getComponentClass(), methodName, ex);
        }
    }
}
