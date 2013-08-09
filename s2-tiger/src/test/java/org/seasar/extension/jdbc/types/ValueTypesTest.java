/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.types;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.ValueType;

/**
 * @author higa
 * 
 */
public class ValueTypesTest extends TestCase {

    @Override
    protected void tearDown() throws Exception {
        ValueTypes.setEnumDefaultValueType(EnumOrdinalType.class);
        ValueTypes.clear();
        super.tearDown();
    }

    /**
     * @throws Exception
     */
    public void testEnum() throws Exception {
        ValueType valueType = ValueTypes.getValueType(MyEnum.class);
        assertNotNull(valueType);
        assertTrue(valueType instanceof EnumOrdinalType);
        EnumOrdinalType enumType = (EnumOrdinalType) valueType;
        assertEquals(MyEnum.ONE, enumType.toEnum(0));
        assertSame(valueType, ValueTypes.getValueType(MyEnum.class));
    }

    /**
     * @throws Exception
     */
    public void testEnumString() throws Exception {
        ValueType valueType = ValueTypes.getEnumStringValueType(MyEnum.class);
        assertNotNull(valueType);
        assertTrue(valueType instanceof EnumType);
        EnumType enumType = (EnumType) valueType;
        assertEquals(MyEnum.ONE, enumType.toEnum("ONE"));
    }

    /**
     * @throws Exception
     */
    public void testEnumOrdinal() throws Exception {
        ValueType valueType = ValueTypes.getEnumOrdinalValueType(MyEnum.class);
        assertNotNull(valueType);
        assertTrue(valueType instanceof EnumOrdinalType);
        EnumOrdinalType enumType = (EnumOrdinalType) valueType;
        assertEquals(MyEnum.ONE, enumType.toEnum(0));
    }

    /**
     * @throws Exception
     */
    public void testInheritedEnum() throws Exception {
        ValueType valueType = ValueTypes.getValueType(MyEnum2.ONE.getClass());
        assertNotNull(valueType);
        assertTrue(valueType instanceof EnumOrdinalType);
        EnumOrdinalType enumType = (EnumOrdinalType) valueType;
        assertEquals(MyEnum2.ONE, enumType.toEnum(0));
        assertSame(valueType, ValueTypes.getValueType(MyEnum2.class));
    }

    /**
     * @throws Exception
     */
    public void testEnum_CustomValueType() throws Exception {
        ValueTypes.setEnumDefaultValueType(EnumType.class);
        ValueType valueType = ValueTypes.getValueType(MyEnum.class);
        assertNotNull(valueType);
        assertTrue(valueType instanceof EnumType);
        EnumType enumType = (EnumType) valueType;
        assertEquals(MyEnum.ONE, enumType.toEnum("ONE"));
        assertSame(valueType, ValueTypes.getValueType(MyEnum.class));
    }

    /**
     * @throws Exception
     */
    public void testIsSimpleType() throws Exception {
        assertTrue(ValueTypes.isSimpleType(String.class));
        assertTrue(ValueTypes.isSimpleType(int.class));
        assertTrue(ValueTypes.isSimpleType(MyEnum.class));
    }

    private static enum MyEnum {
        /**
         * 
         */
        ONE,
        /**
         * 
         */
        TWO
    }

    /**
     * @author koichik
     */
    private static enum MyEnum2 {
        /**
         * 
         */
        ONE {

            @Override
            public void f() {
            }

        },
        /**
         * 
         */
        TWO {

            @Override
            public void f() {
            }
        };

        abstract void f();
    }

}
