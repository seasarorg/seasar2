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
package org.seasar.framework.container.assembler;

import java.util.Map;
import java.util.Set;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.PropertyAssembler;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author higa
 * 
 */
public abstract class AbstractPropertyAssembler extends AbstractAssembler
        implements PropertyAssembler {

    private static final String NULL = "null";

    public AbstractPropertyAssembler(ComponentDef componentDef) {
        super(componentDef);
    }

    protected void bindExternally(BeanDesc beanDesc, ComponentDef componentDef,
            Object component, Set names) {
        ExternalContext extCtx = componentDef.getContainer().getRoot()
                .getExternalContext();
        if (extCtx == null) {
            throw new EmptyRuntimeException("externalContext");
        }
        Map parameterMap = extCtx.getRequestParameterMap();
        Map requestMap = extCtx.getRequestMap();
        for (int i = 0; i < beanDesc.getPropertyDescSize(); ++i) {
            PropertyDesc pd = beanDesc.getPropertyDesc(i);
            if (!pd.hasWriteMethod()) {
                continue;
            }
            String varName = pd.getPropertyName();
            if (names.contains(varName)) {
                continue;
            }
            Object var = getValue(varName, parameterMap, requestMap);
            if (var == null) {
                continue;
            }
            pd.setValue(component, var);
            names.add(varName);
        }
    }

    protected Object getValue(String name, Map parameterMap, Map requestMap) {
        Object value = requestMap.get(name);
        if (value != null) {
            return value;
        }
        value = parameterMap.get(name);
        if (NULL.equals(value)) {
            value = "";
        }
        return value;
    }
}
