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

import org.seasar.framework.aop.InterType;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.InterTypeDefImpl;

public class InterTypeAutoRegister extends AbstractAutoRegister {

    protected InterType interType;

    public void setInterType(final InterType interType) {
        this.interType = interType;
    }

    public void registerAll() {
        final S2Container container = getContainer();
        for (int i = 0; i < container.getComponentDefSize(); ++i) {
            register(container.getComponentDef(i));
        }
    }

    protected void register(final ComponentDef componentDef) {
        final Class componentClass = componentDef.getComponentClass();
        if (componentClass == null) {
            return;
        }

        final String className = componentClass.getName();
        final int pos = className.lastIndexOf('.');
        final String packageName = pos < 0 ? null : className.substring(0, pos);
        final String shortClassName = pos < 0 ? className : className
                .substring(pos + 1);
        for (int i = 0; i < getClassPatternSize(); ++i) {
            final ClassPattern cp = getClassPattern(i);
            if (isIgnore(packageName, shortClassName)) {
                continue;
            }
            if (cp.isAppliedPackageName(packageName)
                    && cp.isAppliedShortClassName(shortClassName)) {
                registerInterType(componentDef);
                return;
            }
        }
    }

    protected void registerInterType(final ComponentDef componentDef) {
        componentDef.addInterTypeDef(new InterTypeDefImpl(interType));
    }
}
