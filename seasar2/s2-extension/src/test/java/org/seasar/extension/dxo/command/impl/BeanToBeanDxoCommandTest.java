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

import org.seasar.extension.dxo.Hoge;
import org.seasar.extension.dxo.HogeHoge;
import org.seasar.extension.dxo.builder.impl.BeanToBeanDxoCommandBuilder;
import org.seasar.extension.dxo.command.DxoCommand;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author koichik
 * 
 */
public class BeanToBeanDxoCommandTest extends S2FrameworkTestCase {

    private BeanToBeanDxoCommandBuilder builder;

    protected void setUp() throws Exception {
        super.setUp();
        include("dxo.dicon");
    }

    public void testScalar1() throws Exception {
        DxoCommand command = builder.createDxoCommand(ToScalarDxo.class,
                ToScalarDxo.class.getMethod("convert",
                        new Class[] { Hoge.class }));
        Hoge src = new Hoge(100, "Hoge", new BigDecimal("1000"));

        HogeHoge dest = (HogeHoge) command.execute(new Object[] { src });

        assertNotNull(dest);
        assertEquals("100", dest.getFoo());
        assertEquals(4, dest.getBar().length);
        assertEquals('H', dest.getBar()[0]);
        assertEquals('o', dest.getBar()[1]);
        assertEquals('g', dest.getBar()[2]);
        assertEquals('e', dest.getBar()[3]);
        assertEquals(1000, dest.getBaz());
        assertEquals("hoge", dest.getHoge());
    }

    public void testScalar2() throws Exception {
        DxoCommand command = builder.createDxoCommand(ToScalarDxo.class,
                ToScalarDxo.class.getMethod("convert", new Class[] {
                        Hoge.class, HogeHoge.class }));
        Hoge src = new Hoge(100, "Hoge", new BigDecimal("1000"));
        HogeHoge dest = new HogeHoge();
        dest.setHoge("HogeHoge");

        command.execute(new Object[] { src, dest });

        assertEquals("100", dest.getFoo());
        assertEquals(4, dest.getBar().length);
        assertEquals('H', dest.getBar()[0]);
        assertEquals('o', dest.getBar()[1]);
        assertEquals('g', dest.getBar()[2]);
        assertEquals('e', dest.getBar()[3]);
        assertEquals(1000, dest.getBaz());
        assertEquals("HogeHoge", dest.getHoge());
    }

    public void testWithRule() throws Exception {
        DxoCommand command = builder.createDxoCommand(ToScalarDxo.class,
                ToScalarDxo.class.getMethod("convertWithRule",
                        new Class[] { Hoge.class }));
        Hoge src = new Hoge(100, "Hoge", new BigDecimal("1000"));

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
        DxoCommand command = builder.createDxoCommand(ToScalarDxo.class,
                ToArrayDxo.class.getMethod("convert",
                        new Class[] { Hoge[].class }));
        Hoge[] src = new Hoge[2];
        src[0] = new Hoge(100, "Hoge", new BigDecimal("1000"));
        src[1] = new Hoge(200, "HogeHoge", new BigDecimal("2000"));

        HogeHoge[] dest = (HogeHoge[]) command.execute(new Object[] { src });

        assertNotNull(dest);
        assertEquals(2, dest.length);

        HogeHoge hogeHoge = dest[0];
        assertNotNull(hogeHoge);
        assertEquals("100", hogeHoge.getFoo());
        assertEquals(4, hogeHoge.getBar().length);
        assertEquals('H', hogeHoge.getBar()[0]);
        assertEquals('o', hogeHoge.getBar()[1]);
        assertEquals('g', hogeHoge.getBar()[2]);
        assertEquals('e', hogeHoge.getBar()[3]);
        assertEquals(1000, hogeHoge.getBaz());

        hogeHoge = dest[1];
        assertNotNull(hogeHoge);
        assertEquals("200", hogeHoge.getFoo());
        assertEquals(8, hogeHoge.getBar().length);
        assertEquals('H', hogeHoge.getBar()[0]);
        assertEquals('o', hogeHoge.getBar()[1]);
        assertEquals('g', hogeHoge.getBar()[2]);
        assertEquals('e', hogeHoge.getBar()[3]);
        assertEquals('H', hogeHoge.getBar()[4]);
        assertEquals('o', hogeHoge.getBar()[5]);
        assertEquals('g', hogeHoge.getBar()[6]);
        assertEquals('e', hogeHoge.getBar()[7]);
        assertEquals(2000, hogeHoge.getBaz());
    }

    public void testArrayToArray2() throws Exception {
        DxoCommand command = builder.createDxoCommand(ToScalarDxo.class,
                ToArrayDxo.class.getMethod("convert", new Class[] {
                        Hoge[].class, HogeHoge[].class }));
        Hoge[] src = new Hoge[2];
        src[0] = new Hoge(100, "Hoge", new BigDecimal("1000"));
        src[1] = new Hoge(200, "HogeHoge", new BigDecimal("2000"));
        HogeHoge[] dest = new HogeHoge[2];

        command.execute(new Object[] { src, dest });

        assertNotNull(dest);
        assertEquals(2, dest.length);

        HogeHoge hogeHoge = dest[0];
        assertNotNull(hogeHoge);
        assertEquals("100", hogeHoge.getFoo());
        assertEquals(4, hogeHoge.getBar().length);
        assertEquals('H', hogeHoge.getBar()[0]);
        assertEquals('o', hogeHoge.getBar()[1]);
        assertEquals('g', hogeHoge.getBar()[2]);
        assertEquals('e', hogeHoge.getBar()[3]);
        assertEquals(1000, hogeHoge.getBaz());

        hogeHoge = dest[1];
        assertNotNull(hogeHoge);
        assertEquals("200", hogeHoge.getFoo());
        assertEquals(8, hogeHoge.getBar().length);
        assertEquals('H', hogeHoge.getBar()[0]);
        assertEquals('o', hogeHoge.getBar()[1]);
        assertEquals('g', hogeHoge.getBar()[2]);
        assertEquals('e', hogeHoge.getBar()[3]);
        assertEquals('H', hogeHoge.getBar()[4]);
        assertEquals('o', hogeHoge.getBar()[5]);
        assertEquals('g', hogeHoge.getBar()[6]);
        assertEquals('e', hogeHoge.getBar()[7]);
        assertEquals(2000, hogeHoge.getBaz());
    }

    public interface ToScalarDxo {
        HogeHoge convert(Hoge src);

        void convert(Hoge src, HogeHoge dest);

        public static final String convertWithRule_CONVERSION_RULE = "'foo' : baz, 'baz' : foo";

        HogeHoge convertWithRule(Hoge src);
    }

    public interface ToArrayDxo {
        HogeHoge[] convert(Hoge[] src);

        void convert(Hoge[] src, HogeHoge[] dest);
    }
}
