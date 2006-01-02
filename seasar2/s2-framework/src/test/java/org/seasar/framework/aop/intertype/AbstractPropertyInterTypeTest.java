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

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * @author y-komori
 * 
 */
public abstract class AbstractPropertyInterTypeTest extends TestCase {

    protected PropertyInterTypeTarget target_;

    private Class targetClass_;

    abstract protected PropertyInterTypeTarget getTarget();

    protected void setUp() throws Exception {
        super.setUp();

        target_ = getTarget();
        targetClass_ = target_.getClass();
    }

    public void testSetterGetter() throws Exception {
        // getter test
        assertEquals(getIntField("getIntReadField"), 123);

        // setter test
        setIntField("setIntWriteField", 456);
        assertEquals(target_.getIntWriteField(), 456);

        // setter/getter test
        setIntField("setIntReadWriteField", 789);
        assertEquals(getIntField("getIntReadWriteField"), 789);
    }

    public void testFieldType() throws Exception {
        Object testValue1 = new Object();
        setField("setObjectField", testValue1);
        assertEquals(getField("getObjectField"), testValue1);

        String[] testValue2 = new String[] { "apple", "orange", "lemon" };
        setField("setStringArrayField", testValue2);
        assertEquals(getField("getStringArrayField"), testValue2);

        long[][] testValue3 = new long[][] { { 123, 456, 789 },
                { 123, 456, 789 } };
        setField("setLongArrayField", testValue3);
        assertEquals(getField("getLongArrayField"), testValue3);

    }

    public void testModifier() throws Exception {
        // Does public field's getter exist?
        assertMethodExists("getPublicField");

        // Does public field's setter exist?
        assertMethodExists("setPublicField", Integer.TYPE);

        // Does protected field's getter exist?
        assertMethodExists("getProtectedField");

        // Does protected field's setter exist?
        assertMethodExists("setProtectedField", Integer.TYPE);

        // Does private field's getter exist?
        assertMethodNotExists("getPrivateField");

        // Does private field's setter exist?
        assertMethodNotExists("setPrivateField", Integer.TYPE);

        // Does none annotated field's getter exist?
        assertMethodNotExists("getIntNoneField");

        // Does none annotated field's setter exist?
        assertMethodNotExists("setIntNoneField", Integer.TYPE);

        // Does none annotated field's getter exist?
        assertMethodNotExists("getNoneAnnotatedField");

        // Does none annotated field's setter exist?
        assertMethodNotExists("setNoneAnnotatedField", Integer.TYPE);
    }

    private void setIntField(String methodName, int param) {
        Method setter = ClassUtil.getMethod(targetClass_, methodName,
                new Class[] { Integer.TYPE });
        MethodUtil.invoke(setter, target_, new Object[] { new Integer(param) });
    }

    private int getIntField(String methodName) {
        Method getter = ClassUtil.getMethod(targetClass_, methodName, null);
        Integer value = (Integer) MethodUtil.invoke(getter, target_, null);
        return value.intValue();
    }

    private void setField(String methodName, Object param) {
        Method setter = ClassUtil.getMethod(targetClass_, methodName,
                new Class[] { param.getClass() });
        MethodUtil.invoke(setter, target_, new Object[] { param });
    }

    private Object getField(String methodName) {
        Method getter = ClassUtil.getMethod(targetClass_, methodName, null);
        Object value = MethodUtil.invoke(getter, target_, null);
        return value;
    }

    private void assertMethodExists(String methodName) {
        assertMethodExists(methodName, null);
    }

    private void assertMethodExists(String methodName, Class paramType) {
        Class[] param = null;
        if (paramType != null) {
            param = new Class[] { paramType };
        }
        ClassUtil.getMethod(targetClass_, methodName, param);
        assertNotNull(methodName);
    }

    private void assertMethodNotExists(String methodName) {
        assertMethodNotExists(methodName, null);
    }

    private void assertMethodNotExists(String methodName, Class paramType) {
        Class[] param = null;
        if (paramType != null) {
            param = new Class[] { paramType };
        }

        try {
            ClassUtil.getMethod(targetClass_, methodName, param);
            fail("The method " + methodName + "exsts.");
        } catch (NoSuchMethodRuntimeException e) {
        }
    }
}
