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
package org.seasar.framework.aop.javassist;

import junit.framework.TestCase;

/**
 * @author koichik
 */
public class AbstGeneratorTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testFromObject() throws Exception {
        assertEquals("1", "var", AbstractGenerator
                .fromObject(void.class, "var"));
        assertEquals("2", "((java.lang.Boolean) var).booleanValue()",
                AbstractGenerator.fromObject(boolean.class, "var"));
        assertEquals("3", "((java.lang.Character) var).charValue()",
                AbstractGenerator.fromObject(char.class, "var"));
        assertEquals("4", "((java.lang.Number) var).byteValue()",
                AbstractGenerator.fromObject(byte.class, "var"));
        assertEquals("5", "((java.lang.Number) var).shortValue()",
                AbstractGenerator.fromObject(short.class, "var"));
        assertEquals("6", "((java.lang.Number) var).intValue()",
                AbstractGenerator.fromObject(int.class, "var"));
        assertEquals("7", "((java.lang.Number) var).longValue()",
                AbstractGenerator.fromObject(long.class, "var"));
        assertEquals("8", "((java.lang.Number) var).floatValue()",
                AbstractGenerator.fromObject(float.class, "var"));
        assertEquals("9", "((java.lang.Number) var).doubleValue()",
                AbstractGenerator.fromObject(double.class, "var"));
        assertEquals("10", "(int[]) var", AbstractGenerator.fromObject(
                int[].class, "var"));
        assertEquals("11", "var", AbstractGenerator.fromObject(Object.class,
                "var"));
        assertEquals("12", "(java.lang.Object[]) var", AbstractGenerator
                .fromObject(Object[].class, "var"));
        assertEquals("13", "(java.lang.String) var", AbstractGenerator
                .fromObject(String.class, "var"));
        assertEquals("14", "(java.lang.String[]) var", AbstractGenerator
                .fromObject(String[].class, "var"));
    }

    /**
     * @throws Exception
     */
    public void testToObject() throws Exception {
        assertEquals("1", "new java.lang.Boolean(var)", AbstractGenerator
                .toObject(boolean.class, "var"));
        assertEquals("1", "new java.lang.Character(var)", AbstractGenerator
                .toObject(char.class, "var"));
        assertEquals("1", "new java.lang.Byte(var)", AbstractGenerator
                .toObject(byte.class, "var"));
        assertEquals("1", "new java.lang.Short(var)", AbstractGenerator
                .toObject(short.class, "var"));
        assertEquals("1", "new java.lang.Integer(var)", AbstractGenerator
                .toObject(int.class, "var"));
        assertEquals("1", "new java.lang.Long(var)", AbstractGenerator
                .toObject(long.class, "var"));
        assertEquals("1", "new java.lang.Float(var)", AbstractGenerator
                .toObject(float.class, "var"));
        assertEquals("1", "new java.lang.Double(var)", AbstractGenerator
                .toObject(double.class, "var"));
        assertEquals("2", "var", AbstractGenerator.toObject(int[].class, "var"));
        assertEquals("3", "var", AbstractGenerator
                .toObject(Object.class, "var"));
        assertEquals("4", "var", AbstractGenerator.toObject(Object[].class,
                "var"));
        assertEquals("5", "var", AbstractGenerator
                .toObject(String.class, "var"));
        assertEquals("6", "var", AbstractGenerator.toObject(String[].class,
                "var"));
    }
}