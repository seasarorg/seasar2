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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ognl.OgnlException;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;

import org.seasar.extension.dxo.IllegalSignatureRuntimeException;
import org.seasar.extension.dxo.command.DxoCommand;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.PropertyNotFoundRuntimeException;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.util.OgnlUtil;

/**
 * @author koichik
 * 
 */
public class MapConversionDxoCommand implements DxoCommand {

    protected static final String PREFIX = "#@java.util.LinkedHashMap@{";

    protected static final String SUFFIX = "}";

    protected String expression;

    protected Object parsedExpression;

    protected Converter converter;

    static {
        OgnlRuntime.setPropertyAccessor(BeanContext.class,
                new BeanPropertyAccessor());
    }

    public MapConversionDxoCommand(final String expression, final Method method) {
        this.expression = expression;
        this.parsedExpression = OgnlUtil.parseExpression(PREFIX + expression
                + SUFFIX);
        this.converter = getConverter(method);
    }

    public Object execute(final Object[] args) {
        return converter.convert(args);
    }

    protected Converter getConverter(final Method method) {
        final Class[] parameterTypes = method.getParameterTypes();
        final int parameterSize = parameterTypes.length;
        if (parameterSize != 1 && parameterSize != 2) {
            throw new IllegalSignatureRuntimeException(method
                    .getDeclaringClass(), method);
        }
        final Class sourceType = parameterTypes[0];
        final Class destType = parameterSize == 1 ? method.getReturnType()
                : parameterTypes[1];

        if (sourceType.isArray()) {
            if (destType.isArray()) {
                return new ArrayToArrayConvertor();
            } else if (List.class.isAssignableFrom(destType)) {
                return new ArrayToListConvertor();
            }
        } else if (List.class.isAssignableFrom(sourceType)) {
            if (destType.isArray()) {
                return new ListToArrayConvertor();
            } else if (List.class.isAssignableFrom(destType)) {
                return new ListToListConvertor();
            }
        } else {
            return new ScalarConvertor();
        }

        throw new IllegalSignatureRuntimeException(method.getDeclaringClass(),
                method);
    }

    public interface Converter {
        Object convert(Object[] args);
    }

    public class ScalarConvertor implements Converter {
        public Object convert(final Object[] args) {
            final Map map = (Map) OgnlUtil.getValue(parsedExpression, args[0]);
            if (args.length == 1) {
                return map;
            }
            final Map dest = (Map) args[1];
            dest.putAll(map);
            return null;
        }
    }

    public class ArrayToArrayConvertor implements Converter {
        public Object convert(final Object[] args) {
            final Object[] src = (Object[]) args[0];
            final Map[] dest = args.length == 1 ? new LinkedHashMap[src.length]
                    : (Map[]) args[1];
            for (int i = 0; i < src.length && i < dest.length; ++i) {
                dest[i] = (Map) OgnlUtil.getValue(parsedExpression, src[i]);
            }
            return dest;
        }
    }

    public class ArrayToListConvertor implements Converter {
        public Object convert(final Object[] args) {
            final Object[] src = (Object[]) args[0];
            final List dest = args.length == 1 ? new ArrayList()
                    : (List) args[1];
            for (int i = 0; i < src.length; ++i) {
                dest.add(OgnlUtil.getValue(parsedExpression, src[i]));
            }
            return dest;
        }
    }

    public class ListToArrayConvertor implements Converter {
        public Object convert(final Object[] args) {
            final List src = (List) args[0];
            final Map[] dest = args.length == 1 ? new LinkedHashMap[src.size()]
                    : (Map[]) args[1];
            int i = 0;
            for (final Iterator it = src.iterator(); it.hasNext()
                    && i < dest.length; ++i) {
                dest[i] = (Map) OgnlUtil.getValue(parsedExpression, it.next());
            }
            return dest;
        }
    }

    public class ListToListConvertor implements Converter {
        public Object convert(final Object[] args) {
            final List src = (List) args[0];
            final List dest = args.length == 1 ? new ArrayList()
                    : (List) args[1];
            for (final Iterator it = src.iterator(); it.hasNext();) {
                dest.add(OgnlUtil.getValue(parsedExpression, it.next()));
            }
            return dest;
        }
    }

    public static class BeanContext {
        protected Object bean;

        protected BeanDesc beanDesc;

        public BeanContext(final Object bean) {
            this.bean = bean;
            this.beanDesc = BeanDescFactory.getBeanDesc(bean.getClass());
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
