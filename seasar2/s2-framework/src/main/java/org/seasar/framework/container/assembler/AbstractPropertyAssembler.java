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
package org.seasar.framework.container.assembler;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.IllegalPropertyRuntimeException;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.PropertyAssembler;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * プロパティアセンブラの抽象クラスです。
 * 
 * @author higa
 * 
 */
public abstract class AbstractPropertyAssembler extends AbstractAssembler
        implements PropertyAssembler {

    /**
     * @param componentDef
     */
    public AbstractPropertyAssembler(ComponentDef componentDef) {
        super(componentDef);
    }

    /**
     * {@link ExternalContext}のデータをプロパティに自動設定します。
     * 
     * @param beanDesc
     * @param componentDef
     * @param component
     * @param names
     * @throws EmptyRuntimeException
     *             {@link ExternalContext}がnullの場合
     */
    protected void bindExternally(final BeanDesc beanDesc,
            final ComponentDef componentDef, final Object component,
            final Set names) throws EmptyRuntimeException {
        final ExternalContext extCtx = componentDef.getContainer().getRoot()
                .getExternalContext();
        if (extCtx == null) {
            throw new EmptyRuntimeException("externalContext");
        }

        for (int i = 0; i < beanDesc.getPropertyDescSize(); ++i) {
            final PropertyDesc pd = beanDesc.getPropertyDesc(i);
            if (!pd.isWritable()) {
                continue;
            }
            final String name = pd.getPropertyName();
            if (names.contains(name)) {
                continue;
            }
            final Object value = getValue(name, pd.getPropertyType(), extCtx);
            if (value == null) {
                continue;
            }
            try {
                pd.setValue(component, value);
                names.add(name);
            } catch (final IllegalPropertyRuntimeException ignore) {
            }
        }
    }

    /**
     * {@link ExternalContext}から値を取り出します。
     * 
     * @param name
     * @param type
     * @param extCtx
     * @return 値
     */
    protected Object getValue(final String name, final Class type,
            final ExternalContext extCtx) {
        if (type.isArray()) {
            Object[] values = getValues(name, extCtx);
            if (values != null) {
                return values;
            }
        } else if (List.class.isAssignableFrom(type)) {
            final Object[] values = getValues(name, extCtx);
            if (values != null) {
                return Arrays.asList(values);
            }
        }
        return getValue(name, extCtx);
    }

    /**
     * {@link ExternalContext}から値を取り出します。
     * 
     * @param name
     * @param extCtx
     * @return 値
     */
    protected Object getValue(final String name, final ExternalContext extCtx) {
        Object value = extCtx.getRequestParameterMap().get(name);
        if (value != null) {
            return value;
        }
        value = extCtx.getRequestHeaderMap().get(name);
        if (value != null) {
            return value;
        }
        return extCtx.getRequestMap().get(name);
    }

    /**
     * {@link ExternalContext}から配列の値を取り出します。
     * 
     * @param name
     * @param extCtx
     * @return 配列の値
     */
    protected Object[] getValues(final String name, final ExternalContext extCtx) {
        Object[] values = (Object[]) extCtx.getRequestParameterValuesMap().get(
                name);
        if (values != null && values.length > 0) {
            return values;
        }
        values = (Object[]) extCtx.getRequestHeaderValuesMap().get(name);
        if (values != null && values.length > 0) {
            return values;
        }
        return null;
    }
}
