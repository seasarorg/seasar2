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
package org.seasar.extension.dxo.command.impl;

import java.lang.reflect.Method;
import java.util.Map;

import ognl.OgnlException;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.PropertyNotFoundRuntimeException;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.util.OgnlUtil;

/**
 * @author koichik
 * 
 */
public class BeanToMapDxoCommand extends AbstractDxoCommand {

    protected static final String PREFIX = "#@java.util.LinkedHashMap@{";

    protected static final String SUFFIX = "}";

    protected String expression;

    protected Object parsedExpression;

    static {
        OgnlRuntime.setPropertyAccessor(BeanContext.class,
                new BeanPropertyAccessor());
    }

    public BeanToMapDxoCommand(final Method method,
            final String expression) {
        super(method);
        this.expression = expression;
        parsedExpression = OgnlUtil.parseExpression(PREFIX + expression
                + SUFFIX);
    }

    protected Object convertScalar(final Object source) {
        return OgnlUtil.getValue(parsedExpression, source);
    }

    protected void copy(final Object src, final Object dest) {
        final Map srcMap = (Map) src;
        final Map destMap = (Map) dest;
        destMap.clear();
        destMap.putAll(srcMap);
    }

    protected Class getDestElementType() {
        return Map.class;
    }

    public static class BeanContext {
        protected Object bean;

        protected BeanDesc beanDesc;

        public BeanContext(final Object bean) {
            this.bean = bean;
            beanDesc = BeanDescFactory.getBeanDesc(bean.getClass());
        }

        public Object getValue(final String propertyName) {
            final PropertyDesc propertyDesc = beanDesc
                    .getPropertyDesc(propertyName);
            if (!propertyDesc.hasReadMethod()) {
                throw new PropertyNotFoundRuntimeException(bean.getClass(),
                        propertyName);
            }
            return propertyDesc.getValue(bean);
        }
    }

    public static class BeanPropertyAccessor implements PropertyAccessor {
        public Object getProperty(final Map context, final Object target,
                final Object name) throws OgnlException {
            final BeanContext beanContext = (BeanContext) target;
            try {
                return beanContext.getValue(name.toString());
            } catch (final PropertyNotFoundRuntimeException e) {
                throw new OgnlException(name.toString(), e);
            }
        }

        public void setProperty(final Map context, final Object target,
                final Object name, final Object value) throws OgnlException {
            throw new OgnlException(name.toString());
        }
    }

}
