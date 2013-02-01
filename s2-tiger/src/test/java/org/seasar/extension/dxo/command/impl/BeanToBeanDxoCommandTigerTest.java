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
package org.seasar.extension.dxo.command.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.dxo.Hoge;
import org.seasar.extension.dxo.HogeHoge;
import org.seasar.extension.dxo.builder.impl.BeanToBeanDxoCommandBuilder;
import org.seasar.extension.dxo.command.DxoCommand;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author koichik
 */
public class BeanToBeanDxoCommandTigerTest extends S2FrameworkTestCase {

    private BeanToBeanDxoCommandBuilder builder;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include("dxo.dicon");
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testListToArray1() throws Exception {
        DxoCommand command = builder.createDxoCommand(ToArrayDxo.class,
                ToArrayDxo.class.getMethod("convert",
                        new Class[] { List.class }));
        List<HogeHoge> src = new ArrayList<HogeHoge>();
        src.add(new HogeHoge("100", "Hoge".toCharArray(), 1000));
        src.add(new HogeHoge("200", "HogeHoge".toCharArray(), 2000));

        Hoge[] dest = (Hoge[]) command.execute(new Object[] { src });

        assertNotNull(dest);
        assertEquals(2, dest.length);

        Hoge hoge = dest[0];
        assertEquals(100, hoge.getFoo());
        assertEquals("Hoge", hoge.getBar());
        assertEquals(new BigDecimal("1000"), hoge.getBaz());

        hoge = dest[1];
        assertEquals(200, hoge.getFoo());
        assertEquals("HogeHoge", hoge.getBar());
        assertEquals(new BigDecimal("2000"), hoge.getBaz());
    }

    /**
     * @throws Exception
     */
    public void testListToArray2() throws Exception {
        DxoCommand command = builder.createDxoCommand(ToArrayDxo.class,
                ToArrayDxo.class.getMethod("convert", new Class[] { List.class,
                        Hoge[].class }));
        List<HogeHoge> src = new ArrayList<HogeHoge>();
        src.add(new HogeHoge("100", "Hoge".toCharArray(), 1000));
        src.add(new HogeHoge("200", "HogeHoge".toCharArray(), 2000));
        Hoge[] dest = new Hoge[2];

        command.execute(new Object[] { src, dest });

        assertNotNull(dest);
        assertEquals(2, dest.length);

        Hoge hoge = dest[0];
        assertEquals(100, hoge.getFoo());
        assertEquals("Hoge", hoge.getBar());
        assertEquals(new BigDecimal("1000"), hoge.getBaz());

        hoge = dest[1];
        assertEquals(200, hoge.getFoo());
        assertEquals("HogeHoge", hoge.getBar());
        assertEquals(new BigDecimal("2000"), hoge.getBaz());
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testArrayToList1() throws Exception {
        DxoCommand command = builder.createDxoCommand(ToListDxo.class,
                ToListDxo.class.getMethod("convert",
                        new Class[] { HogeHoge[].class }));
        HogeHoge[] src = new HogeHoge[2];
        src[0] = new HogeHoge("100", "Hoge".toCharArray(), 1000);
        src[1] = new HogeHoge("200", "HogeHoge".toCharArray(), 2000);

        List<Hoge> dest = (List<Hoge>) command.execute(new Object[] { src });

        assertNotNull(dest);
        assertEquals(2, dest.size());

        Hoge hoge = dest.get(0);
        assertEquals(100, hoge.getFoo());
        assertEquals("Hoge", hoge.getBar());
        assertEquals(new BigDecimal("1000"), hoge.getBaz());

        hoge = dest.get(1);
        assertEquals(200, hoge.getFoo());
        assertEquals("HogeHoge", hoge.getBar());
        assertEquals(new BigDecimal("2000"), hoge.getBaz());
    }

    /**
     * @throws Exception
     */
    public void testArrayToList2() throws Exception {
        DxoCommand command = builder.createDxoCommand(ToListDxo.class,
                ToListDxo.class.getMethod("convert", new Class[] {
                        HogeHoge[].class, List.class }));
        HogeHoge[] src = new HogeHoge[2];
        src[0] = new HogeHoge("100", "Hoge".toCharArray(), 1000);
        src[1] = new HogeHoge("200", "HogeHoge".toCharArray(), 2000);
        List<Hoge> dest = new ArrayList<Hoge>();

        command.execute(new Object[] { src, dest });

        assertNotNull(dest);
        assertEquals(2, dest.size());

        Hoge hoge = dest.get(0);
        assertEquals(100, hoge.getFoo());
        assertEquals("Hoge", hoge.getBar());
        assertEquals(new BigDecimal("1000"), hoge.getBaz());

        hoge = dest.get(1);
        assertEquals(200, hoge.getFoo());
        assertEquals("HogeHoge", hoge.getBar());
        assertEquals(new BigDecimal("2000"), hoge.getBaz());
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testListToList1() throws Exception {
        DxoCommand command = builder.createDxoCommand(ToListDxo.class,
                ToListDxo.class
                        .getMethod("convert", new Class[] { List.class }));
        List<HogeHoge> src = new ArrayList<HogeHoge>();
        src.add(new HogeHoge("100", "Hoge".toCharArray(), 1000));
        src.add(new HogeHoge("200", "HogeHoge".toCharArray(), 2000));

        List<Hoge> dest = (List<Hoge>) command.execute(new Object[] { src });

        assertNotNull(dest);
        assertEquals(2, dest.size());

        Hoge hoge = dest.get(0);
        assertEquals(100, hoge.getFoo());
        assertEquals("Hoge", hoge.getBar());
        assertEquals(new BigDecimal("1000"), hoge.getBaz());

        hoge = dest.get(1);
        assertEquals(200, hoge.getFoo());
        assertEquals("HogeHoge", hoge.getBar());
        assertEquals(new BigDecimal("2000"), hoge.getBaz());
    }

    /**
     * @throws Exception
     */
    public void testLIstToList2() throws Exception {
        DxoCommand command = builder.createDxoCommand(ToListDxo.class,
                ToListDxo.class.getMethod("convert", new Class[] { List.class,
                        List.class }));
        List<HogeHoge> src = new ArrayList<HogeHoge>();
        src.add(new HogeHoge("100", "Hoge".toCharArray(), 1000));
        src.add(new HogeHoge("200", "HogeHoge".toCharArray(), 2000));
        List<Hoge> dest = new ArrayList<Hoge>();

        command.execute(new Object[] { src, dest });

        assertNotNull(dest);
        assertEquals(2, dest.size());

        Hoge hoge = dest.get(0);
        assertEquals(100, hoge.getFoo());
        assertEquals("Hoge", hoge.getBar());
        assertEquals(new BigDecimal("1000"), hoge.getBaz());

        hoge = dest.get(1);
        assertEquals(200, hoge.getFoo());
        assertEquals("HogeHoge", hoge.getBar());
        assertEquals(new BigDecimal("2000"), hoge.getBaz());
    }

    /**
     *
     */
    public interface ToArrayDxo {
        /**
         * @param src
         * @return
         */
        Hoge[] convert(List<HogeHoge> src);

        /**
         * @param src
         * @param dest
         */
        void convert(List<HogeHoge> src, Hoge[] dest);
    }

    /**
     *
     */
    public interface ToListDxo {
        /**
         * @param src
         * @return
         */
        List<Hoge> convert(HogeHoge[] src);

        /**
         * @param src
         * @param dest
         */
        void convert(HogeHoge[] src, List<Hoge> dest);

        /**
         * @param src
         * @return
         */
        List<Hoge> convert(List<HogeHoge> src);

        /**
         * @param src
         * @param dest
         */
        void convert(List<HogeHoge> src, List<Hoge> dest);
    }
}
