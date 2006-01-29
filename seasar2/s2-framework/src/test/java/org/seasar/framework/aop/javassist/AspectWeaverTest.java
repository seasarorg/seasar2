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
package org.seasar.framework.aop.javassist;

import java.math.BigDecimal;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.InterType;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.framework.aop.javassist.AspectWeaver;

/**
 * @author koichik
 */
public class AspectWeaverTest extends TestCase {
    public AspectWeaverTest() {
        super();
    }

    public AspectWeaverTest(String name) {
        super(name);
    }

    public void testGetEnhancedClassName() throws Exception {
        AspectWeaver weaver = new AspectWeaver(Object.class, null);
        String name1 = weaver.getEnhancedClassName();
        assertTrue("1", name1
                .startsWith("$$java.lang.Object$$EnhancedByS2AOP$$"));
        assertTrue("2", name1.endsWith("_0"));

        String name2 = weaver.getEnhancedClassName();
        assertTrue("3", name2
                .startsWith("$$java.lang.Object$$EnhancedByS2AOP$$"));
        assertTrue("4", name2.endsWith("_1"));

        weaver = new AspectWeaver(AspectWeaverTest.class, null);
        String name3 = weaver.getEnhancedClassName();
        assertTrue(
                "5",
                name3
                        .startsWith("org.seasar.framework.aop.javassist.AspectWeaverTest$$EnhancedByS2AOP$$"));
        assertTrue("6", name3.endsWith("_0"));
    }

    public void testGetMethodInvocationClassName() throws Exception {
        AspectWeaver weaver = new AspectWeaver(Object.class, null);
        String name1 = weaver.getMethodInvocationClassName(Object.class
                .getMethod("hashCode", null));
        assertTrue("1", name1
                .startsWith("$$java.lang.Object$$EnhancedByS2AOP$$"));
        assertTrue("2", name1.endsWith("hashCode0"));
    }

    public void testGenerateFromInterface() throws Exception {
        AspectWeaver weaver = new AspectWeaver(Runnable.class, null);
        weaver.setInterceptors(Runnable.class.getDeclaredMethod("run", null),
                new MethodInterceptor[0]);
        Class clazz = weaver.generateClass();

        clazz.getDeclaredMethod("run", null);
        try {
            clazz.getDeclaredMethod("run"
                    + AspectWeaver.SUFFIX_INVOKE_SUPER_METHOD, null);
            fail("3");
        } catch (NoSuchMethodException expected) {
        }
    }

    public void testGenerateFromConcreteClass() throws Exception {
        AspectWeaver weaver = new AspectWeaver(ArrayList.class, null);
        weaver.setInterceptors(ArrayList.class.getDeclaredMethod("size", null),
                new MethodInterceptor[0]);
        Class clazz = weaver.generateClass();

        clazz.getDeclaredMethod("size", null);
        clazz.getDeclaredMethod("size"
                + AspectWeaver.SUFFIX_INVOKE_SUPER_METHOD, null);
    }

    public void testGenerateFromAbstractClass() throws Exception {
        AspectWeaver weaver = new AspectWeaver(AbstractList.class, null);
        weaver.setInterceptors(AbstractList.class.getDeclaredMethod("clear",
                null), new MethodInterceptor[0]);
        weaver.setInterceptors(List.class.getDeclaredMethod("size", null),
                new MethodInterceptor[0]);
        Class clazz = weaver.generateClass();

        clazz.getDeclaredMethod("clear", null);
        clazz.getDeclaredMethod("clear"
                + AspectWeaver.SUFFIX_INVOKE_SUPER_METHOD, null);

        clazz.getDeclaredMethod("size", null);
        try {
            clazz.getDeclaredMethod("size"
                    + AspectWeaver.SUFFIX_INVOKE_SUPER_METHOD, null);
            fail("3");
        } catch (NoSuchMethodException expected) {
        }
    }

    public void testInterType() throws Exception {
        AspectWeaver weaver = new AspectWeaver(Foo.class, null);
        weaver.setInterTypes(new InterType[] { new FieldInterType() });
        Class clazz = weaver.generateClass();
        assertNotNull("1", clazz.getDeclaredField("test"));
    }

    public void testReturnNumber() throws Exception {
        AspectWeaver weaver = new AspectWeaver(Collection.class, null);
        weaver.setInterceptors(
                Collection.class.getDeclaredMethod("size", null),
                new MethodInterceptor[] { new ReturnBigDecimalInterceptor() });
        Class clazz = weaver.generateClass();
        Collection coll = (Collection) clazz.newInstance();
        assertEquals("1", 100, coll.size());
    }

    public void testReturnNumberNull() throws Exception {
        AspectWeaver weaver = new AspectWeaver(Collection.class, null);
        weaver.setInterceptors(
                Collection.class.getDeclaredMethod("size", null),
                new MethodInterceptor[] { new ReturnNullInterceptor() });
        Class clazz = weaver.generateClass();
        Collection coll = (Collection) clazz.newInstance();
        assertEquals("1", 0, coll.size());
    }

    public void testReturnBooleanNull() throws Exception {
        AspectWeaver weaver = new AspectWeaver(Collection.class, null);
        weaver.setInterceptors(Collection.class.getDeclaredMethod("isEmpty",
                null), new MethodInterceptor[] { new ReturnNullInterceptor() });
        Class clazz = weaver.generateClass();
        Collection coll = (Collection) clazz.newInstance();
        assertFalse("1", coll.isEmpty());
    }

    public static class Foo {
    }

    public static class ReturnBigDecimalInterceptor extends AbstractInterceptor {
        private static final long serialVersionUID = -3444509695408031219L;

        public Object invoke(MethodInvocation arg0) throws Throwable {
            return new BigDecimal("100");
        }
    }

    public static class ReturnNullInterceptor extends AbstractInterceptor {
        private static final long serialVersionUID = 4724885819473955866L;

        public Object invoke(MethodInvocation arg0) throws Throwable {
            return null;
        }
    }

    public static class FieldInterType implements InterType {

        public void introduce(Class targetClass, CtClass enhancedClass) {
            try {
                enhancedClass.addField(new CtField(CtClass.booleanType, "test",
                        enhancedClass));
            } catch (CannotCompileException e) {
                throw new RuntimeException(e);
            }
        }

    }
}