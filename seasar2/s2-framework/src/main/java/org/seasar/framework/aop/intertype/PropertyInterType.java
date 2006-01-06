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
package org.seasar.framework.aop.intertype;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.seasar.framework.exception.SRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author y-komori
 * 
 */
public class PropertyInterType extends AbstractInterType {

    private static final String SETTER_PREFIX = "set";

    private static final String GETTER_PREFIX = "get";

    protected static final int UNSPECIFIED = 0;

    protected static final int READ = 1;

    protected static final int WRITE = 2;

    protected static final int READWRITE = 3;

    protected static final int NONE = 4;

    private static final String TIGER_ANNOTATION_HANDLER = "org.seasar.framework.aop.intertype.TigerPropertyAnnotationHandler";

    private static final String BACKPORT175_ANNOTATION_HANDLER = "org.seasar.framework.aop.intertype.Backport175PropertyAnnotationHandler";

    private static Logger logger = Logger.getLogger(PropertyInterType.class);

    private static PropertyAnnotationHandler annotationHandler;

    private boolean trace;

    static {
        setupAnnotationHandler();
    }

    private static void setupAnnotationHandler() {
        Class clazz = null;
        try {
            clazz = Class.forName(TIGER_ANNOTATION_HANDLER, true,
                    PropertyInterType.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            try {
                clazz = Class.forName(BACKPORT175_ANNOTATION_HANDLER, true,
                        PropertyInterType.class.getClassLoader());
            } catch (ClassNotFoundException e2) {
                return;
            }
        }
        annotationHandler = (PropertyAnnotationHandler) ClassUtil
                .newInstance(clazz);
    }

    protected void introduce() {
        if (annotationHandler == null) {
            throw new SRuntimeException("ESSR0001",
                    new Object[] { "PropertyAnnotationHandler implementation" });
        }
        if (logger.isDebugEnabled()) {
            logger.debug("[PropertyInterType] Introducing... "
                    + targetClass.getName());
        }

        int defaultValue = annotationHandler.getPropertyType(getTargetClass());
        List targetFields = getTargetFields(targetClass);

        for (Iterator iter = targetFields.iterator(); iter.hasNext();) {
            Field field = (Field) iter.next();
            int property = annotationHandler.getPropertyType(field);
            if (property == UNSPECIFIED) {
                property = defaultValue;
            }
            switch (property) {
            case READ:
                createGetter(targetClass, field);
                break;

            case WRITE:
                createSetter(targetClass, field);
                break;

            case READWRITE:
                createGetter(targetClass, field);
                createSetter(targetClass, field);
                break;

            default:
                break;
            }
        }
    }

    public void setTrace(boolean trace) {
        this.trace = trace;
    }

    private void createGetter(Class targetClass, Field targetField) {
        String targetFieldName = targetField.getName();
        String methodName = GETTER_PREFIX + createMethodName(targetFieldName);

        if (logger.isDebugEnabled()) {
            logger.debug("[PropertyInterType] Creating getter "
                    + targetClass.getName() + "#" + methodName);
        }

        StringBuffer src = new StringBuffer(512);
        src.append("{");
        if (trace) {
            src.append("org.seasar.framework.log.Logger logger =");
            src.append("org.seasar.framework.log.Logger.getLogger($class);");
            src.append("if(logger.isDebugEnabled()){");
            src
                    .append("logger.debug(\"CALL \" + $class.getSuperclass().getName() + \"#");
            src.append(methodName);
            src.append("() : \" + this.");
            src.append(targetFieldName);
            src.append(");}");
        }
        src.append("return this.");
        src.append(targetFieldName);
        src.append(";}");

        addMethod(targetField.getType(), methodName, src.toString());
    }

    private void createSetter(Class targetClass, Field targetField) {
        String targetFieldName = targetField.getName();
        String methodName = SETTER_PREFIX + createMethodName(targetFieldName);

        if (logger.isDebugEnabled()) {
            logger.debug("[PropertyInterType] Creating setter "
                    + targetClass.getName() + "#" + methodName);
        }

        StringBuffer src = new StringBuffer(512);
        src.append("{");
        if (trace) {
            src.append("org.seasar.framework.log.Logger logger =");
            src.append("org.seasar.framework.log.Logger.getLogger($class);");
            src.append("if(logger.isDebugEnabled()){");
            src
                    .append("logger.debug(\"CALL \" + $class.getSuperclass().getName() + \"#");
            src.append(methodName);
            src.append("(\" + $1 + \")\");}");
        }
        src.append("this.");
        src.append(targetFieldName);
        src.append(" = $1;}");

        addMethod(methodName, new Class[] { targetField.getType() }, src
                .toString());
    }

    private List getTargetFields(Class targetClass) {
        List targetFields = new ArrayList();

        Field[] nominationFields = getFields(targetClass);
        for (int i = 0; i < nominationFields.length; i++) {
            Field field = nominationFields[i];
            int modifier = field.getModifiers();
            if (!Modifier.isPrivate(modifier)) {
                targetFields.add(field);
            }
        }

        return targetFields;
    }

    private Field[] getFields(Class targetClass) {
        Class superClass = targetClass.getSuperclass();
        Field[] superFields = new Field[0];
        if (superClass != null) {
            superFields = getFields(superClass);
        }

        Field[] currentFields = targetClass.getDeclaredFields();

        Field[] fields = new Field[superFields.length + currentFields.length];
        System.arraycopy(superFields, 0, fields, 0, superFields.length);
        System.arraycopy(currentFields, 0, fields, superFields.length,
                currentFields.length);

        return fields;
    }

    private String createMethodName(String fieldName) {
        String methodName = StringUtil.capitalize(fieldName);
        if (methodName.endsWith("_")) {
            methodName = methodName.substring(0, methodName.length() - 1);
        }

        return methodName;
    }

    public interface PropertyAnnotationHandler {
        int getPropertyType(Class clazz);

        int getPropertyType(Field field);
    }
}
