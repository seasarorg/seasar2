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

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

import junit.framework.TestCase;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.javassist.ClassPoolUtil;
import org.seasar.framework.aop.javassist.EnhancedClassGenerator;
import org.seasar.framework.aop.javassist.MethodInvocationClassGenerator;

/**
 * @author koichik
 */
public class EnhancedClassGeneratorTest extends TestCase {
    public EnhancedClassGeneratorTest() {
        super();
    }

    public EnhancedClassGeneratorTest(String name) {
        super(name);
    }

    public void testNormalizeExceptionTypes() throws Exception {
        assertTrue("1", Arrays.equals(new Class[] { Throwable.class }, EnhancedClassGenerator
                .normalizeExceptionTypes(new Class[] { Throwable.class })));
        assertTrue("2", Arrays.equals(new Class[] { Throwable.class }, EnhancedClassGenerator
                .normalizeExceptionTypes(new Class[] { Throwable.class, Exception.class })));
        assertTrue("3", Arrays.equals(new Class[] { Throwable.class }, EnhancedClassGenerator
                .normalizeExceptionTypes(new Class[] { Throwable.class, Exception.class,
                        Error.class })));
        assertTrue("4", Arrays.equals(new Class[] { Exception.class, Error.class },
                EnhancedClassGenerator.normalizeExceptionTypes(new Class[] { Exception.class,
                        Error.class })));
        assertTrue("5", Arrays.equals(new Class[] { Exception.class, Error.class },
                EnhancedClassGenerator.normalizeExceptionTypes(new Class[] {
                        RuntimeException.class, Exception.class, Error.class })));
        assertTrue("6", Arrays.equals(new Class[] { RuntimeException.class, IOException.class,
                Error.class }, EnhancedClassGenerator.normalizeExceptionTypes(new Class[] {
                RuntimeException.class, IOException.class, Error.class })));
    }

    public void testAroundTryCatchBlock() throws Exception {
        assertEquals(
                "1",
                "try {return;}"
                        + "catch (java.lang.Exception e) {throw e;}"
                        + "catch (java.lang.RuntimeException e) {throw e;}"
                        + "catch (java.lang.Error e) {throw e;}"
                        + "catch (java.lang.Throwable e) {throw new java.lang.reflect.UndeclaredThrowableException(e);}",
                EnhancedClassGenerator.aroundTryCatchBlock(new Class[] { Exception.class },
                        "return;"));

        assertEquals(
                "2",
                "try {return;}"
                        + "catch (java.lang.RuntimeException e) {throw e;}"
                        + "catch (java.io.IOException e) {throw e;}"
                        + "catch (java.lang.Error e) {throw e;}"
                        + "catch (java.lang.Throwable e) {throw new java.lang.reflect.UndeclaredThrowableException(e);}",
                EnhancedClassGenerator.aroundTryCatchBlock(new Class[] { RuntimeException.class,
                        IOException.class }, "return;"));
    }

    public void testCreateTargetMethodSource() throws Exception {
        Method method1 = Object.class.getMethod("hashCode", null);
        assertEquals(
                "1",
                "{"
                        + "try {"
                        + "Object result = new MethodInvocation(this, $args).proceed();"
                        + "return ($r) ((result == null) ? 0 : ((java.lang.Number) result).intValue());"
                        + "}"
                        + "catch (java.lang.RuntimeException e) {throw e;}"
                        + "catch (java.lang.Error e) {throw e;}"
                        + "catch (java.lang.Throwable e) {throw new java.lang.reflect.UndeclaredThrowableException(e);}"
                        + "}", EnhancedClassGenerator.createTargetMethodSource(method1,
                        "MethodInvocation"));

        Method method2 = Object.class.getMethod("wait", null);
        assertEquals(
                "2",
                "{"
                        + "try {"
                        + "Object result = new MethodInvocation(this, $args).proceed();"
                        + "return;"
                        + "}"
                        + "catch (java.lang.InterruptedException e) {throw e;}"
                        + "catch (java.lang.RuntimeException e) {throw e;}"
                        + "catch (java.lang.Error e) {throw e;}"
                        + "catch (java.lang.Throwable e) {throw new java.lang.reflect.UndeclaredThrowableException(e);}"
                        + "}", EnhancedClassGenerator.createTargetMethodSource(method2,
                        "MethodInvocation"));

        Method method3 = MethodInvocation.class.getMethod("proceed", null);
        assertEquals("3", "{" + "Object result = new MethodInvocation(this, $args).proceed();"
                + "return ($r) result;" + "}", EnhancedClassGenerator.createTargetMethodSource(
                method3, "MethodInvocation"));
    }

    public void testCreateInvokeSuperMethodSource() throws Exception {
        Method method = Object.class.getMethod("wait", null);
        assertEquals("1", "{" + "return ($r) super.wait($$);" + "}", EnhancedClassGenerator
                .createInvokeSuperMethodSource(method));
    }

    public void testGenerateFromInterface() throws Exception {
        Method[] methods = TargetInterface.class.getDeclaredMethods();
        for (int i = 0; i < methods.length; ++i) {
            EnhancedClassGenerator generator = new EnhancedClassGenerator(ClassPoolUtil
                    .getClassPool(TargetInterface.class), TargetInterface.class, TargetInterface.class.getName() + i);
            generator.createTargetMethod(methods[i],
                    MethodInvocationClassGenerator.MethodInvocationTemplate.class.getName());
            Class clazz = generator.toClass(getClass().getClassLoader());
            assertEquals("1", TargetInterface.class.getName() + i, clazz.getName());
            Method method = clazz.getDeclaredMethod(methods[i].getName(), methods[i]
                    .getParameterTypes());
            assertEquals("2", methods[i].getName(), method.getName());
        }
    }

    public void testGenerateFromClass() throws Exception {
        Method[] methods = TargetClass.class.getDeclaredMethods();
        for (int i = 0; i < methods.length; ++i) {
            EnhancedClassGenerator generator = new EnhancedClassGenerator(ClassPoolUtil
                    .getClassPool(TargetClass.class), TargetClass.class, TargetClass.class.getName() + i);
            generator.createInvokeSuperMethod(methods[i], methods[i].getName() + "__invokeSuper__");
            generator.createTargetMethod(methods[i],
                    MethodInvocationClassGenerator.MethodInvocationTemplate.class.getName());
            Class clazz = generator.toClass(getClass().getClassLoader());
            assertEquals("1", TargetClass.class.getName() + i, clazz.getName());
            Method method = clazz.getDeclaredMethod(methods[i].getName(), methods[i]
                    .getParameterTypes());
            assertEquals("2", methods[i].getName(), method.getName());
            Method invokeSuperMethod = clazz.getDeclaredMethod(methods[i].getName()
                    + "__invokeSuper__", methods[i].getParameterTypes());
            assertEquals("3", methods[i].getName() + "__invokeSuper__", invokeSuperMethod.getName());
        }
    }

    public static interface TargetInterface {
        public void fVoid();

        public boolean fBoolean();

        public char fChar();

        public byte fByte();

        public short fShort();

        public int fInt();

        public long fLong();

        public float fFloat();

        public double fDouble();

        public int[] fIntArray();

        public int[][] fInt2DArray();

        public Object fObject();

        public Object[] fObjectArray();

        public String fString();

        public String[] fStringArray();

        public boolean f(boolean arg0);

        public char f(char arg0);

        public byte f(byte arg0);

        public short f(short arg0);

        public int f(int arg0);

        public long f(long arg0);

        public float f(float arg0);

        public double f(double arg0);

        public int[] f(int[] arg0);

        public int[][] f(int[][] arg0);

        public Object f(Object arg0);

        public Object[] f(Object[] arg0);

        public String f(String arg0);

        public String[] f(String[] arg0);

        public void f(boolean arg0, char arg1, byte arg2, short arg3, int arg4, long arg5,
                float arg6, double arg7, int[] arg8, int[][] arg9, Object arg10, Object[] arg11,
                String arg12, String[] arg13);
    }

    public static class TargetClass {
        public void fVoid() {
        }

        public boolean fBoolean() {
            return false;
        }

        public char fChar() {
            return 0;
        }

        public byte fByte() {
            return 0;
        }

        public short fShort() {
            return 0;
        }

        public int fInt() {
            return 0;
        }

        public long fLong() {
            return 0;
        }

        public float fFloat() {
            return 0;
        }

        public double fDouble() {
            return 0;
        }

        public int[] fIntArray() {
            return new int[0];
        }

        public int[][] fInt2DArray() {
            return new int[0][0];
        }

        public Object fObject() {
            return null;
        }

        public Object[] fObjectArray() {
            return new Object[0];
        }

        public String fString() {
            return "";
        }

        public String[] fStringArray() {
            return new String[0];
        }

        public boolean f(boolean arg0) {
            return arg0;
        }

        public char f(char arg0) {
            return arg0;
        }

        public byte f(byte arg0) {
            return arg0;
        }

        public short f(short arg0) {
            return arg0;
        }

        public int f(int arg0) {
            return arg0;
        }

        public long f(long arg0) {
            return arg0;
        }

        public float f(float arg0) {
            return arg0;
        }

        public double f(double arg0) {
            return arg0;
        }

        public int[] f(int[] arg0) {
            return arg0;
        }

        public int[][] f(int[][] arg0) {
            return arg0;
        }

        public Object f(Object arg0) {
            return arg0;
        }

        public Object[] f(Object[] arg0) {
            return arg0;
        }

        public String f(String arg0) {
            return arg0;
        }

        public String[] f(String[] arg0) {
            return arg0;
        }

        public void f(boolean arg0, char arg1, byte arg2, short arg3, int arg4, long arg5,
                float arg6, double arg7, int[] arg8, int[][] arg9, Object arg10, Object[] arg11,
                String arg12, String[] arg13) {
        }
    }
}