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

import java.lang.reflect.Method;
import java.util.Collection;

import junit.framework.TestCase;

import org.seasar.framework.aop.javassist.ClassPoolUtil;
import org.seasar.framework.aop.javassist.MethodInvocationClassGenerator;

/**
 * @author koichik
 */
public class MethodInvocationClassGeneratorTest extends TestCase {
    public MethodInvocationClassGeneratorTest() {
    }

    public MethodInvocationClassGeneratorTest(String name) {
        super(name);
    }

    public void testCreateArgumentString() throws Exception {
        assertEquals("1",
                "((java.lang.Number) arguments[0]).intValue(), (java.lang.String) arguments[1]",
                MethodInvocationClassGenerator.createArgumentString(new Class[] { int.class,
                        String.class }));
    }

    public void testCreateProceedMethodSource() throws Exception {
        Method voidMethod = Object.class.getMethod("notify", null);
        assertEquals("1", "{" + "if (interceptorsIndex < interceptors.length) {"
                + "return interceptors[interceptorsIndex++].invoke(this);" + "}"
                + "((EnhancedClass) target).invokeSuper();" + "return null;" + "}",
                MethodInvocationClassGenerator.createProceedMethodSource(voidMethod,
                        "EnhancedClass", "invokeSuper"));

        Method intMethod = Object.class.getMethod("hashCode", null);
        assertEquals("2", "{" + "if (interceptorsIndex < interceptors.length) {"
                + "return interceptors[interceptorsIndex++].invoke(this);" + "}"
                + "return new java.lang.Integer(((EnhancedClass) target).invokeSuper());" + "}",
                MethodInvocationClassGenerator.createProceedMethodSource(intMethod,
                        "EnhancedClass", "invokeSuper"));

        Method stringMethod = Object.class.getMethod("toString", null);
        assertEquals("2", "{" + "if (interceptorsIndex < interceptors.length) {"
                + "return interceptors[interceptorsIndex++].invoke(this);" + "}"
                + "return ((EnhancedClass) target).invokeSuper();" + "}",
                MethodInvocationClassGenerator.createProceedMethodSource(stringMethod,
                        "EnhancedClass", "invokeSuper"));
    }

    public void testCreateProceedMethodSource2() throws Exception {
        Method voidMethod = Runnable.class.getMethod("run", null);
        assertEquals("1", "{" + "if (interceptorsIndex < interceptors.length) {"
                + "return interceptors[interceptorsIndex++].invoke(this);" + "}"
                + "throw new java.lang.NoSuchMethodError(\"EnhancedClass.run()\");" + "}",
                MethodInvocationClassGenerator.createProceedMethodSource(voidMethod,
                        "EnhancedClass", "invokeSuper"));

        Method intMethod = Collection.class.getMethod("size", null);
        assertEquals("2", "{" + "if (interceptorsIndex < interceptors.length) {"
                + "return interceptors[interceptorsIndex++].invoke(this);" + "}"
                + "throw new java.lang.NoSuchMethodError(\"EnhancedClass.size()\");" + "}",
                MethodInvocationClassGenerator.createProceedMethodSource(intMethod,
                        "EnhancedClass", "invokeSuper"));

        Method arrayMethod = Collection.class.getMethod("toArray", new Class[] { Object[].class });
        assertEquals(
                "2",
                "{"
                        + "if (interceptorsIndex < interceptors.length) {"
                        + "return interceptors[interceptorsIndex++].invoke(this);"
                        + "}"
                        + "throw new java.lang.NoSuchMethodError(\"EnhancedClass.toArray(java.lang.Object[])\");"
                        + "}", MethodInvocationClassGenerator.createProceedMethodSource(
                        arrayMethod, "EnhancedClass", "invokeSuper"));
    }

    public void testGenerateFromInterface() throws Exception {
        Method[] methods = TargetInterface.class.getDeclaredMethods();
        for (int i = 0; i < methods.length; ++i) {
            MethodInvocationClassGenerator generator = new MethodInvocationClassGenerator(
                    ClassPoolUtil.getClassPool(TargetInterface.class), TargetInterface.class.getName() + i,
                    TargetClass.class.getName());
            generator.createProceedMethod(methods[i], methods[i].getName());
            Class clazz = generator.toClass(getClass().getClassLoader());
            assertEquals("1", TargetInterface.class.getName() + i, clazz.getName());
            Method method = clazz.getDeclaredMethod("proceed", null);
            assertEquals("2", "proceed", method.getName());
        }
    }

    public void testGenerateFromClass() throws Exception {
        Method[] methods = TargetClass.class.getDeclaredMethods();
        for (int i = 0; i < methods.length; ++i) {
            MethodInvocationClassGenerator generator = new MethodInvocationClassGenerator(
                    ClassPoolUtil.getClassPool(TargetClass.class), TargetClass.class.getName() + i,
                    TargetClass.class.getName());
            generator.createProceedMethod(methods[i], methods[i].getName());
            Class clazz = generator.toClass(getClass().getClassLoader());
            assertEquals("1", TargetClass.class.getName() + i, clazz.getName());
            Method method = clazz.getDeclaredMethod("proceed", null);
            assertEquals("2", "proceed", method.getName());
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