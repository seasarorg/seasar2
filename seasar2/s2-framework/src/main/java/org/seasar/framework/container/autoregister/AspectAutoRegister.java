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
package org.seasar.framework.container.autoregister;

import org.aopalliance.intercept.MethodInterceptor;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.AspectDefFactory;

/**
 * @author higa
 * 
 */
public class AspectAutoRegister extends AbstractAutoRegister {

    private MethodInterceptor interceptor;

    private String pointcut;

    public void setInterceptor(MethodInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public void setPointcut(String pointcut) {
        this.pointcut = pointcut;
    }

    public void registerAll() {
        S2Container container = getContainer();
        for (int i = 0; i < container.getComponentDefSize(); ++i) {
            ComponentDef cd = container.getComponentDef(i);
            register(cd);
        }
    }

    protected void register(ComponentDef componentDef) {
        Class componentClass = componentDef.getComponentClass();
        if (componentClass == null) {
            return;
        }
        /*
         * if (componentDef.getAspectDefSize() > 0) { return; }
         */
        String className = componentClass.getName();
        int pos = className.lastIndexOf('.');
        String packageName = pos < 0 ? null : className.substring(0, pos);
        String shortClassName = pos < 0 ? className : className
                .substring(pos + 1);
        for (int i = 0; i < getClassPatternSize(); ++i) {
            ClassPattern cp = getClassPattern(i);
            if (isIgnore(packageName, shortClassName)) {
                continue;
            }
            if (cp.isAppliedPackageName(packageName)
                    && cp.isAppliedShortClassName(shortClassName)) {
                registerInterceptor(componentDef);
                return;
            }
        }
    }

    protected void registerInterceptor(ComponentDef componentDef) {
        AspectDef aspectDef = AspectDefFactory.createAspectDef(interceptor,
                pointcut);
        componentDef.addAspectDef(aspectDef);
    }
}