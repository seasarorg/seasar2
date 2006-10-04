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
package org.seasar.extension.dxo.converter.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import org.seasar.extension.dxo.converter.Converter;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author Satsohi Kimura
 * @author koichik
 */
public class ConverterFactoryImplTest extends S2TestCase {
    private ConverterFactoryImpl factory;

    protected void setUp() throws Exception {
        include("dxo.dicon");
    }

    public void testBoolean() {
        Converter converter = factory.getConverter(Object.class, boolean.class);
        assertEquals(BooleanConverter.class.getName(), converter.getClass()
                .getName());
    }

    public void testNumber() {
        Converter converter = factory.getConverter(Object.class, byte.class);
        assertEquals(ByteConverter.class.getName(), converter.getClass()
                .getName());

        converter = factory.getConverter(Object.class, short.class);
        assertEquals(ShortConverter.class.getName(), converter.getClass()
                .getName());

        converter = factory.getConverter(Object.class, int.class);
        assertEquals(IntegerConverter.class.getName(), converter.getClass()
                .getName());

        converter = factory.getConverter(Object.class, long.class);
        assertEquals(LongConverter.class.getName(), converter.getClass()
                .getName());

        converter = factory.getConverter(Object.class, float.class);
        assertEquals(FloatConverter.class.getName(), converter.getClass()
                .getName());

        converter = factory.getConverter(Object.class, double.class);
        assertEquals(DoubleConverter.class.getName(), converter.getClass()
                .getName());

        converter = factory.getConverter(Object.class, BigInteger.class);
        assertEquals(BigIntegerConverter.class.getName(), converter.getClass()
                .getName());

        converter = factory.getConverter(Object.class, BigDecimal.class);
        assertEquals(BigDecimalConverter.class.getName(), converter.getClass()
                .getName());
    }

    public void testString() {
        Converter converter = factory.getConverter(Object.class, char.class);
        assertEquals(CharacterConverter.class.getName(), converter.getClass()
                .getName());

        converter = factory.getConverter(Object.class, String.class);
        assertEquals(StringConverter.class.getName(), converter.getClass()
                .getName());
    }

    public void testWrapper() {
        Converter converter = factory.getConverter(Object.class, Byte.class);
        assertEquals(ByteConverter.class.getName(), converter.getClass()
                .getName());

        converter = factory.getConverter(Object.class, Short.class);
        assertEquals(ShortConverter.class.getName(), converter.getClass()
                .getName());

        converter = factory.getConverter(Object.class, Integer.class);
        assertEquals(IntegerConverter.class.getName(), converter.getClass()
                .getName());

        converter = factory.getConverter(Object.class, Long.class);
        assertEquals(LongConverter.class.getName(), converter.getClass()
                .getName());

        converter = factory.getConverter(Object.class, Float.class);
        assertEquals(FloatConverter.class.getName(), converter.getClass()
                .getName());

        converter = factory.getConverter(Object.class, Double.class);
        assertEquals(DoubleConverter.class.getName(), converter.getClass()
                .getName());

        converter = factory.getConverter(Object.class, Boolean.class);
        assertEquals(BooleanConverter.class.getName(), converter.getClass()
                .getName());

        converter = factory.getConverter(Object.class, Character.class);
        assertEquals(CharacterConverter.class.getName(), converter.getClass()
                .getName());
    }

    public void testArray() {
        Converter converter = factory.getConverter(Object.class, char[].class);
        assertEquals(CharArrayConverter.class.getName(), converter.getClass()
                .getName());

        converter = factory.getConverter(Object.class, int[].class);
        assertEquals(ArrayConverter.class.getName(), converter.getClass()
                .getName());

        converter = factory.getConverter(Object.class, Object[].class);
        assertEquals(ArrayConverter.class.getName(), converter.getClass()
                .getName());
    }

    public void testCollection() {
        Converter converter = factory.getConverter(Object.class, List.class);
        assertEquals(ListConverter.class.getName(), converter.getClass()
                .getName());

        converter = factory.getConverter(Object.class, Set.class);
        assertEquals(SetConverter.class.getName(), converter.getClass()
                .getName());
    }

}
