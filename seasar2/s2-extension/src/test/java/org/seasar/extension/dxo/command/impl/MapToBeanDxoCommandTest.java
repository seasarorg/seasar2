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
package org.seasar.extension.dxo.command.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.seasar.extension.dxo.Hoge;
import org.seasar.extension.dxo.HogeHoge;
import org.seasar.extension.dxo.builder.impl.MapToBeanDxoCommandBuilder;
import org.seasar.extension.dxo.command.DxoCommand;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author koichik
 * 
 */
public class MapToBeanDxoCommandTest extends S2FrameworkTestCase {

    private MapToBeanDxoCommandBuilder builder;

    protected void setUp() throws Exception {
        super.setUp();
        include("dxo.dicon");
    }

    public void testScalar1() throws Exception {
        DxoCommand command = builder.createDxoCommand(ToScalarDxo.class,
                ToScalarDxo.class.getMethod("convert",
                        new Class[] { Map.class }));
        Map src = new HashMap();
        src.put("foo", new Integer(100));
        src.put("bar", "Hoge");
        src.put("baz", new BigDecimal("1000"));

        Hoge dest = (Hoge) command.execute(new Object[] { src });

        assertNotNull(dest);
        assertEquals(100, dest.getFoo());
        assertEquals("Hoge", dest.getBar());
        assertEquals(new BigDecimal("1000"), dest.getBaz());
    }

    public void testScalar2() throws Exception {
        DxoCommand command = builder.createDxoCommand(ToScalarDxo.class,
                ToScalarDxo.class.getMethod("convert", new Class[] { Map.class,
                        Hoge.class }));
        Map src = new HashMap();
        src.put("foo", new Integer(100));
        src.put("bar", "Hoge");
        src.put("baz", new BigDecimal("1000"));
        Hoge dest = new Hoge();

        command.execute(new Object[] { src, dest });

        assertNotNull(dest);
        assertEquals(100, dest.getFoo());
        assertEquals("Hoge", dest.getBar());
        assertEquals(new BigDecimal("1000"), dest.getBaz());
    }

    public void testWithRule() throws Exception {
        DxoCommand command = builder.createDxoCommand(ToScalarDxo.class,
                ToScalarDxo.class.getMethod("convertWithRule",
                        new Class[] { Map.class }));
        Map src = new HashMap();
        src.put("foo", new Integer(100));
        src.put("bar", "Hoge");
        src.put("baz", new BigDecimal("1000"));

        HogeHoge dest = (HogeHoge) command.execute(new Object[] { src });

        assertNotNull(dest);
        assertEquals("1000", dest.getFoo());
        assertEquals(4, dest.getBar().length);
        assertEquals('H', dest.getBar()[0]);
        assertEquals('o', dest.getBar()[1]);
        assertEquals('g', dest.getBar()[2]);
        assertEquals('e', dest.getBar()[3]);
        assertEquals(100, dest.getBaz());
    }

    public void testArrayToArray1() throws Exception {
        DxoCommand command = builder.createDxoCommand(ToArrayDxo.class,
                ToArrayDxo.class.getMethod("convert",
                        new Class[] { Map[].class }));
        Map[] src = new Map[2];
        src[0] = new HashMap();
        src[0].put("foo", new Integer(100));
        src[0].put("bar", "Hoge");
        src[0].put("baz", new BigDecimal("1000"));
        src[1] = new HashMap();
        src[1].put("foo", new Integer(200));
        src[1].put("bar", "HogeHoge");
        src[1].put("baz", new BigDecimal("2000"));

        Hoge[] dest = (Hoge[]) command.execute(new Object[] { src });

        assertEquals(100, dest[0].getFoo());
        assertEquals("Hoge", dest[0].getBar());
        assertEquals(new BigDecimal("1000"), dest[0].getBaz());

        assertEquals(200, dest[1].getFoo());
        assertEquals("HogeHoge", dest[1].getBar());
        assertEquals(new BigDecimal("2000"), dest[1].getBaz());
    }

    public void testArrayToArray2() throws Exception {
        DxoCommand command = builder.createDxoCommand(ToArrayDxo.class,
                ToArrayDxo.class.getMethod("convert", new Class[] {
                        Map[].class, Hoge[].class }));
        Map[] src = new Map[2];
        src[0] = new HashMap();
        src[0].put("foo", new Integer(100));
        src[0].put("bar", "Hoge");
        src[0].put("baz", new BigDecimal("1000"));
        src[1] = new HashMap();
        src[1].put("foo", new Integer(200));
        src[1].put("bar", "HogeHoge");
        src[1].put("baz", new BigDecimal("2000"));
        Hoge[] dest = new Hoge[2];

        command.execute(new Object[] { src, dest });

        assertEquals(100, dest[0].getFoo());
        assertEquals("Hoge", dest[0].getBar());
        assertEquals(new BigDecimal("1000"), dest[0].getBaz());

        assertEquals(200, dest[1].getFoo());
        assertEquals("HogeHoge", dest[1].getBar());
        assertEquals(new BigDecimal("2000"), dest[1].getBaz());
    }

    public interface ToScalarDxo {
        Hoge convert(Map src);

        void convert(Map src, Hoge dest);

        public static final String convertWithRule_CONVERSION_RULE = "'foo' : baz, 'baz' : foo";

        HogeHoge convertWithRule(Map src);
    }

    public interface ToArrayDxo {
        Hoge[] convert(Map[] src);

        void convert(Map[] src, Hoge[] dest);
    }

}
