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
package org.seasar.framework.aop.interceptors;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.log.Logger;

/**
 * @author eriguchi
 * 
 */
public class ToStringInterceptor extends AbstractInterceptor {
    private static final long serialVersionUID = 8867582188275644844L;

    private static final String FIELD_SEPARATOR = ",";

    private static final String VALUE_SEPARATOR = "=";

    private static final String DOUBLE_QUOTE = "\"";

    private static final String SINGLE_QUOTE = "\'";

    private static final String FIELDS_BEGIN = "[";

    private static final String FIELDS_END = "]";

    private static final String ARRAY_BEGIN = "{";

    private static final String ARRAY_END = "}";

    private static final Logger logger = Logger
            .getLogger(ToStringInterceptor.class);

    /**
     * includeConstantのBindingアノテーションです。
     */
    public static final String includeConstant_BINDING = "bindingType=none";

    private boolean includeConstant;

    /**
     * includeStaticのBindingアノテーションです。
     */
    public static final String includeStatic_BINDING = "bindingType=none";

    private boolean includeStatic;

    private DateFormat dateFormat = new SimpleDateFormat();

    /**
     * 定数を出力するかどうかを設定します。
     * 
     * @param includeConstant
     */
    public void setIncludeConstant(boolean includeConstant) {
        this.includeConstant = includeConstant;
    }

    /**
     * staticなfieldを出力するかどうかを設定します。
     * 
     * @param includeStatic
     */
    public void setIncludeStatic(boolean includeStatic) {
        this.includeStatic = includeStatic;
    }

    /**
     * 日付のフォーマットを設定します。
     * 
     * @param format
     */
    public void setDateFormat(String format) {
        dateFormat = new SimpleDateFormat(format);
    }

    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class targetClass = getTargetClass(invocation);

        if (logger.isDebugEnabled()) {
            String methodName = invocation.getMethod().getName();
            logger.debug("[ToStringInterceptor] modifying "
                    + targetClass.getName() + "#" + methodName);
        }

        try {
            return toString(targetClass, invocation.getThis());
        } catch (Exception e) {
            logger.error("[ToStringInterceptor] modify failed", e);
            return invocation.proceed();
        }
    }

    private String toString(Class targetClass, Object target)
            throws IllegalAccessException {
        StringBuffer buf = new StringBuffer(100);

        buf.append(targetClass.getName());
        buf.append("@");
        buf.append(Integer.toHexString(System.identityHashCode(target)));

        buf.append(FIELDS_BEGIN);
        boolean hasFields = false;
        while (targetClass != null) {
            Field[] fields = targetClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);

                int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)
                        && !includeConstant) {
                    continue;
                }
                if (Modifier.isStatic(modifiers)
                        && !Modifier.isFinal(modifiers) && !includeStatic) {
                    continue;
                }

                buf.append(field.getName());
                buf.append(VALUE_SEPARATOR);
                append(buf, field.get(target));
                buf.append(FIELD_SEPARATOR);
                hasFields = true;
            }
            targetClass = targetClass.getSuperclass();
        }
        if (hasFields) {
            buf.setLength(buf.length() - FIELD_SEPARATOR.length());
        }
        buf.append(FIELDS_END);

        return new String(buf);
    }

    private void append(StringBuffer buf, Object value) {
        if (value == null) {
            buf.append((String) null);
            return;
        }
        if (value.getClass().isArray()) {
            appendArray(buf, value);
            return;
        }
        if (value instanceof String) {
            buf.append(DOUBLE_QUOTE).append(value).append(DOUBLE_QUOTE);
            return;
        }
        if (value instanceof Character) {
            buf.append(SINGLE_QUOTE).append(value).append(SINGLE_QUOTE);
            return;
        }
        if (value instanceof Date) {
            buf.append(dateFormat.format((Date) value));
            return;
        }
        buf.append(value);
    }

    private void appendArray(StringBuffer buf, Object array) {
        buf.append(ARRAY_BEGIN);
        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            append(buf, Array.get(array, i));
            buf.append(FIELD_SEPARATOR);
        }
        if (length > 0) {
            buf.setLength(buf.length() - FIELD_SEPARATOR.length());
        }
        buf.append(ARRAY_END);
    }
}