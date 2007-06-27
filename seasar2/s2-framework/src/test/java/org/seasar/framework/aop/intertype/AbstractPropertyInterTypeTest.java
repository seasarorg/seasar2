/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * @author y-komori
 * 
 */
public abstract class AbstractPropertyInterTypeTest extends TestCase {

    private PropertyInterTypeTarget target;

    private Class targetClass;

    private Object target2;

    private Class targetClass2;

    private Object testObject;

    /**
     * @return
     */
    abstract protected String getPath();

    /**
     * 
     */
    public AbstractPropertyInterTypeTest() {
    }

    /**
     * @param name
     */
    public AbstractPropertyInterTypeTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();

        S2Container container = S2ContainerFactory.create(getPath());

        target = (PropertyInterTypeTarget) container.getComponent("target");
        targetClass = target.getClass();

        target2 = container.getComponent("target2");
        targetClass2 = target2.getClass();

        testObject = container.getComponent("testObject");
    }

    /**
     * @throws Exception
     */
    public void testSetterGetter() throws Exception {
        // getter test
        assertEquals(getIntField("getIntReadField"), 123);

        // setter test
        setIntField("setIntWriteField", 456);
        assertEquals(target.getIntWriteField(), 456);

        // setter/getter test
        setIntField("setIntReadWriteField", 789);
        assertEquals(getIntField("getIntReadWriteField"), 789);
    }

    /**
     * @throws Exception
     */
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

    /**
     * @throws Exception
     */
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
        assertMethodNotExists("getNonAnnotatedField");

        // Does none annotated field's setter exist?
        assertMethodNotExists("setNonAnnotatedField", Integer.TYPE);
    }

    /**
     * @throws Exception
     */
    public void testClassAnnotated() throws Exception {
        // Does default (not annotated) field's getter exist?
        assertMethodExists(targetClass2, "getDefaultField");

        // Does default (not annotated) field's setter exist?
        assertMethodExists(targetClass2, "setDefaultField", Integer.TYPE);

        // Does read only (override) field's getter exist?
        assertMethodExists(targetClass2, "getReadField");

        // Does read only (override) field's setter exist?
        assertMethodNotExists(targetClass2, "setReadField", Integer.TYPE);

        // Does write only (override) field's getter exist?
        assertMethodNotExists(targetClass2, "getWriteField");

        // Does read only (override) field's setter exist?
        assertMethodExists(targetClass2, "setWriteField", Integer.TYPE);

        // Does read-write (override) field's getter exist?
        assertMethodExists(targetClass2, "getReadWriteField");

        // Does read-write (override) field's setter exist?
        assertMethodExists(targetClass2, "setReadWriteField", Integer.TYPE);

        // Does none (override) field's getter exist?
        assertMethodNotExists(targetClass2, "getNoneField");

        // Does none (override) field's setter exist?
        assertMethodNotExists(targetClass2, "setNoneField", Integer.TYPE);
    }

    /**
     * @throws Exception
     */
    public void testHasMethod() throws Exception {
        assertMethodNotExists(targetClass2, "getHasGetter");
        assertMethodExists(targetClass2, "setHasGetter", Integer.TYPE);

        assertMethodExists(targetClass2, "getHasSetter");
        assertMethodNotExists(targetClass2, "setHasSetter", Integer.TYPE);

        assertMethodNotExists(targetClass2, "getHasGetterSetter");
        assertMethodNotExists(targetClass2, "setHasGetterSetter", Integer.TYPE);
    }

    /**
     * 
     */
    public void testInjection() {
        BeanDesc desc = BeanDescFactory.getBeanDesc(targetClass);
        PropertyDesc pd = desc.getPropertyDesc("testObject");
        assertEquals(testObject, pd.getValue(target));
    }

    private void setIntField(String methodName, int param) {
        Method setter = ClassUtil.getMethod(targetClass, methodName,
                new Class[] { Integer.TYPE });
        MethodUtil.invoke(setter, target, new Object[] { new Integer(param) });
    }

    private int getIntField(String methodName) {
        Method getter = ClassUtil.getMethod(targetClass, methodName, null);
        Integer value = (Integer) MethodUtil.invoke(getter, target, null);
        return value.intValue();
    }

    private void setField(String methodName, Object param) {
        Method setter = ClassUtil.getMethod(targetClass, methodName,
                new Class[] { param.getClass() });
        MethodUtil.invoke(setter, target, new Object[] { param });
    }

    private Object getField(String methodName) {
        Method getter = ClassUtil.getMethod(targetClass, methodName, null);
        Object value = MethodUtil.invoke(getter, target, null);
        return value;
    }

    private void assertMethodExists(String methodName) {
        assertMethodExists(methodName, null);
    }

    private void assertMethodExists(String methodName, Class paramType) {
        assertMethodExists(targetClass, methodName, paramType);
    }

    private void assertMethodExists(Class targetClass, String methodName) {
        assertMethodExists(targetClass, methodName, null);
    }

    private void assertMethodExists(Class targetClass, String methodName,
            Class paramType) {
        Class[] param = null;
        if (paramType != null) {
            param = new Class[] { paramType };
        }
        ClassUtil.getDeclaredMethod(targetClass, methodName, param);
        assertNotNull(methodName);
    }

    private void assertMethodNotExists(String methodName) {
        assertMethodNotExists(methodName, null);
    }

    private void assertMethodNotExists(String methodName, Class paramType) {
        assertMethodNotExists(targetClass, methodName, paramType);
    }

    private void assertMethodNotExists(Class targetClass, String methodName) {
        assertMethodNotExists(targetClass, methodName, null);
    }

    private void assertMethodNotExists(Class targetClass, String methodName,
            Class paramType) {
        Class[] param = null;
        if (paramType != null) {
            param = new Class[] { paramType };
        }

        try {
            ClassUtil.getDeclaredMethod(targetClass, methodName, param);
            fail("The method " + methodName + " exsts.");
        } catch (NoSuchMethodRuntimeException e) {
        }
    }
}
