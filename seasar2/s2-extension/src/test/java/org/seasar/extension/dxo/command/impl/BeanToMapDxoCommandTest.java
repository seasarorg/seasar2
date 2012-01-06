/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.extension.dxo.Hoge;
import org.seasar.extension.dxo.builder.impl.BeanToMapDxoCommandBuilder;
import org.seasar.extension.dxo.command.DxoCommand;
import org.seasar.framework.exception.SIllegalArgumentException;
import org.seasar.framework.unit.S2FrameworkTestCase;
import org.seasar.framework.util.ClassUtil;

/**
 * @author koichik
 * 
 */
public class BeanToMapDxoCommandTest extends S2FrameworkTestCase {

    private BeanToMapDxoCommandBuilder builder;

    protected void setUp() throws Exception {
        super.setUp();
        include("dxo.dicon");
    }

    /**
     * @throws Exception
     */
    public void testScalar1() throws Exception {
        DxoCommand command = builder.createDxoCommand(ScalarDxo.class,
                ClassUtil.getMethod(ScalarDxo.class, "convert",
                        new Class[] { Hoge.class }));
        Hoge src = new Hoge(100, "Hoge", new BigDecimal("1000"));
        Map dest = (Map) command.execute(new Object[] { src });
        assertNotNull(dest);
        assertEquals(3, dest.size());
        assertEquals(new Integer(100), dest.get("foo"));
        assertEquals("Hoge", dest.get("bar"));
        assertEquals(new BigDecimal("1000"), dest.get("baz"));

        src = new Hoge(0, null, null);
        dest = (Map) command.execute(new Object[] { src });
        assertNotNull(dest);
        assertEquals(1, dest.size());
        assertEquals(new Integer(0), dest.get("foo"));
    }

    /**
     * @throws Exception
     */
    public void testScalar2() throws Exception {
        DxoCommand command = builder.createDxoCommand(ScalarDxo.class,
                ClassUtil.getMethod(ScalarDxo.class, "convert", new Class[] {
                        Hoge.class, Map.class }));
        Hoge src = new Hoge(100, "Hoge", new BigDecimal("1000"));
        Map dest = new HashMap();
        command.execute(new Object[] { src, dest });
        assertNotNull(dest);
        assertEquals(3, dest.size());
        assertEquals(new Integer(100), dest.get("foo"));
        assertEquals("Hoge", dest.get("bar"));
        assertEquals(new BigDecimal("1000"), dest.get("baz"));

        src = new Hoge(0, null, null);
        dest = new HashMap();
        command.execute(new Object[] { src, dest });
        assertNotNull(dest);
        assertEquals(1, dest.size());
        assertEquals(new Integer(0), dest.get("foo"));
    }

    /**
     * @throws Exception
     */
    public void testScalar3() throws Exception {
        DxoCommand command = builder.createDxoCommand(ScalarDxo.class,
                ClassUtil.getMethod(ScalarDxo.class, "convert3", new Class[] {
                        Hoge.class, Map.class }));
        Hoge src = new Hoge(100, "Hoge", new BigDecimal("1000"));
        Map dest = new HashMap();
        command.execute(new Object[] { src, dest });
        assertNotNull(dest);
        assertEquals(4, dest.size());
        assertEquals(new Integer(100), dest.get("one"));
        assertEquals("Hoge", dest.get("two"));
        assertEquals(new BigDecimal("1000"), dest.get("three"));
        assertEquals("100Hoge1000", dest.get("four"));

        src = new Hoge(0, null, null);
        dest = new HashMap();
        command.execute(new Object[] { src, dest });
        assertNotNull(dest);
        assertEquals(4, dest.size());
        assertEquals(new Integer(0), dest.get("one"));
        assertNull(dest.get("two"));
        assertNull(dest.get("three"));
        assertEquals("0nullnull", dest.get("four"));
    }

    /**
     * @throws Exception
     */
    public void testScalar4() throws Exception {
        DxoCommand command = builder.createDxoCommand(ScalarDxo.class,
                ClassUtil.getMethod(ScalarDxo.class, "convert4", new Class[] {
                        Hoge.class, Map.class }));
        Hoge src = new Hoge(100, "Hoge", new BigDecimal("1000"));
        Map dest = new HashMap();
        command.execute(new Object[] { src, dest });
        assertNotNull(dest);
        assertEquals(1, dest.size());
        assertEquals("Hoge", dest.get("one"));
        assertNull(dest.get("two"));
        assertNull(dest.get("three"));
        assertNull(dest.get("four"));

        src = new Hoge(0, null, null);
        dest = new HashMap();
        command.execute(new Object[] { src, dest });
        assertNotNull(dest);
        assertEquals(1, dest.size());
        assertNull(dest.get("one"));
        assertNull(dest.get("two"));
        assertNull(dest.get("three"));
        assertNull(dest.get("four"));
    }

    /**
     * @throws Exception
     */
    public void testScalarNull1() throws Exception {
        DxoCommand command = builder.createDxoCommand(ScalarDxo.class,
                ClassUtil.getMethod(ScalarDxo.class, "convert",
                        new Class[] { Hoge.class }));
        assertNull(command.execute(new Object[] { null }));
    }

    /**
     * @throws Exception
     */
    public void testScalarNull2() throws Exception {
        DxoCommand command = builder.createDxoCommand(ScalarDxo.class,
                ClassUtil.getMethod(ScalarDxo.class, "convert", new Class[] {
                        Hoge.class, Map.class }));
        Map dest = new HashMap();
        try {
            command.execute(new Object[] { null, dest });
            fail();
        } catch (final SIllegalArgumentException e) {
            assertEquals("ESSR0601", e.getMessageCode());
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testScalarNull3() throws Exception {
        DxoCommand command = builder.createDxoCommand(ScalarDxo.class,
                ClassUtil.getMethod(ScalarDxo.class, "convert", new Class[] {
                        Hoge.class, Map.class }));
        Hoge src = new Hoge(100, "Hoge", new BigDecimal("1000"));
        try {
            command.execute(new Object[] { src, null });
            fail();
        } catch (final SIllegalArgumentException e) {
            assertEquals("ESSR0602", e.getMessageCode());
            System.out.println(e.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testScalarDestPrefix() throws Exception {
        DxoCommand command = builder.createDxoCommand(ScalarDxo.class,
                ClassUtil.getMethod(ScalarDxo.class, "convert5",
                        new Class[] { Hoge.class }));
        Hoge src = new Hoge(100, "Hoge", new BigDecimal("1000"));

        Map dest = (Map) command.execute(new Object[] { src });
        assertNotNull(dest);
        assertEquals(4, dest.size());
        assertEquals(new Integer(100), dest.get("deptFoo"));
        assertEquals("Hoge", dest.get("deptBar"));
        assertNull(dest.get("deptBarBar"));
        assertEquals(new BigDecimal("1000"), dest.get("deptBaz"));

        src = new Hoge(0, null, null);
        dest = (Map) command.execute(new Object[] { src });
        assertNotNull(dest);
        assertEquals(4, dest.size());
        assertEquals(new Integer(0), dest.get("deptFoo"));
        assertNull(dest.get("deptBar"));
        assertNull(dest.get("deptBarBar"));
        assertNull(dest.get("deptBaz"));
    }

    /**
     * @throws Exception
     */
    public void testScalarDestPrefix2() throws Exception {
        DxoCommand command = builder.createDxoCommand(ScalarDxo.class,
                ClassUtil.getMethod(ScalarDxo.class, "convert6",
                        new Class[] { Hoge.class }));
        Hoge src = new Hoge(100, "Hoge", new BigDecimal("1000"));

        Map dest = (Map) command.execute(new Object[] { src });
        assertNotNull(dest);
        assertEquals(4, dest.size());
        assertEquals(new Integer(100), dest.get("dept_foo"));
        assertEquals("Hoge", dest.get("dept_bar"));
        assertNull(dest.get("dept_barBar"));
        assertEquals(new BigDecimal("1000"), dest.get("dept_baz"));

        src = new Hoge(0, null, null);
        dest = (Map) command.execute(new Object[] { src });
        assertNotNull(dest);
        assertEquals(4, dest.size());
        assertEquals(new Integer(0), dest.get("dept_foo"));
        assertNull(dest.get("dept_bar"));
        assertNull(dest.get("dept_barBar"));
        assertNull(dest.get("dept_baz"));
    }

    /**
     * @throws Exception
     */
    public void testArrayToArray1() throws Exception {
        DxoCommand command = builder.createDxoCommand(ToArrayDxo.class,
                ClassUtil.getMethod(ToArrayDxo.class, "convert",
                        new Class[] { Hoge[].class }));
        Hoge[] src = new Hoge[2];
        src[0] = new Hoge(100, "Hoge", new BigDecimal("1000"));
        src[1] = new Hoge(200, "HogeHoge", new BigDecimal("2000"));
        Map[] dest = (Map[]) command.execute(new Object[] { src });
        assertNotNull(dest);
        assertEquals(2, dest.length);

        assertEquals(4, dest[0].size());
        assertEquals(new Integer(100), dest[0].get("one"));
        assertEquals("Hoge", dest[0].get("two"));
        assertEquals(new BigDecimal("1000"), dest[0].get("three"));
        assertEquals("100Hoge1000", dest[0].get("four"));

        assertEquals(4, dest[1].size());
        assertEquals(new Integer(200), dest[1].get("one"));
        assertEquals("HogeHoge", dest[1].get("two"));
        assertEquals(new BigDecimal("2000"), dest[1].get("three"));
        assertEquals("200HogeHoge2000", dest[1].get("four"));
    }

    /**
     * @throws Exception
     */
    public void testArrayToArray2() throws Exception {
        DxoCommand command = builder.createDxoCommand(ToArrayDxo.class,
                ClassUtil.getMethod(ToArrayDxo.class, "convert", new Class[] {
                        Hoge[].class, Map[].class }));
        Hoge[] src = new Hoge[2];
        src[0] = new Hoge(100, "Hoge", new BigDecimal("1000"));
        src[1] = new Hoge(200, "HogeHoge", new BigDecimal("2000"));
        Map[] dest = new HashMap[2];
        command.execute(new Object[] { src, dest });
        assertNotNull(dest);
        assertEquals(2, dest.length);

        assertEquals(4, dest[0].size());
        assertEquals(new Integer(100), dest[0].get("one"));
        assertEquals("Hoge", dest[0].get("two"));
        assertEquals(new BigDecimal("1000"), dest[0].get("three"));
        assertEquals("100Hoge1000", dest[0].get("four"));

        assertEquals(4, dest[1].size());
        assertEquals(new Integer(200), dest[1].get("one"));
        assertEquals("HogeHoge", dest[1].get("two"));
        assertEquals(new BigDecimal("2000"), dest[1].get("three"));
        assertEquals("200HogeHoge2000", dest[1].get("four"));
    }

    /**
     * @throws Exception
     */
    public void testListToArray1() throws Exception {
        DxoCommand command = builder.createDxoCommand(ToArrayDxo.class,
                ClassUtil.getMethod(ToArrayDxo.class, "convert",
                        new Class[] { List.class }));
        List src = new ArrayList();
        src.add(new Hoge(100, "Hoge", new BigDecimal("1000")));
        src.add(new Hoge(200, "HogeHoge", new BigDecimal("2000")));
        Map[] dest = (Map[]) command.execute(new Object[] { src });
        assertNotNull(dest);
        assertEquals(2, dest.length);

        assertEquals(4, dest[0].size());
        assertEquals(new Integer(100), dest[0].get("one"));
        assertEquals("Hoge", dest[0].get("two"));
        assertEquals(new BigDecimal("1000"), dest[0].get("three"));
        assertEquals("100Hoge1000", dest[0].get("four"));

        assertEquals(4, dest[1].size());
        assertEquals(new Integer(200), dest[1].get("one"));
        assertEquals("HogeHoge", dest[1].get("two"));
        assertEquals(new BigDecimal("2000"), dest[1].get("three"));
        assertEquals("200HogeHoge2000", dest[1].get("four"));
    }

    /**
     * @throws Exception
     */
    public void testListToArray2() throws Exception {
        DxoCommand command = builder.createDxoCommand(ToArrayDxo.class,
                ClassUtil.getMethod(ToArrayDxo.class, "convert", new Class[] {
                        List.class, Map[].class }));
        List src = new ArrayList();
        src.add(new Hoge(100, "Hoge", new BigDecimal("1000")));
        src.add(new Hoge(200, "HogeHoge", new BigDecimal("2000")));
        Map[] dest = new Map[2];
        command.execute(new Object[] { src, dest });
        assertNotNull(dest);
        assertEquals(2, dest.length);

        assertEquals(4, dest[0].size());
        assertEquals(new Integer(100), dest[0].get("one"));
        assertEquals("Hoge", dest[0].get("two"));
        assertEquals(new BigDecimal("1000"), dest[0].get("three"));
        assertEquals("100Hoge1000", dest[0].get("four"));

        assertEquals(4, dest[1].size());
        assertEquals(new Integer(200), dest[1].get("one"));
        assertEquals("HogeHoge", dest[1].get("two"));
        assertEquals(new BigDecimal("2000"), dest[1].get("three"));
        assertEquals("200HogeHoge2000", dest[1].get("four"));
    }

    /**
     * 
     */
    public interface ScalarDxo {
        /**
         * 
         */
        public static final String convert_EXCLUDE_NULL = "";

        /**
         * @param src
         * @return
         */
        Map convert(Hoge src);

        /**
         * @param src
         * @param dest
         */
        void convert(Hoge src, Map dest);

        /**
         * 
         */
        public static final String convert3_CONVERSION_RULE = "'one' : foo, 'two' : bar, 'three' : baz, 'four' : ''+foo+bar+baz";

        /**
         * @param src
         * @param dest
         */
        void convert3(Hoge src, Map dest);

        /**
         * 
         */
        public static final String convert4_CONVERSION_RULE = "one : bar";

        /**
         * @param src
         * @param dest
         */
        void convert4(Hoge src, Map dest);

        /**
         * 
         */
        public static final String convert5_DEST_PREFIX = "dept";

        /**
         * @param src
         * @return
         */
        Map convert5(Hoge src);

        /**
         * 
         */
        public static final String convert6_DEST_PREFIX = "dept_";

        /**
         * @param src
         * @return
         */
        Map convert6(Hoge src);
    }

    /**
     * 
     */
    public interface ToArrayDxo {
        /**
         * 
         */
        public static final String convert_CONVERSION_RULE = "'one' : foo, 'two' : bar, 'three' : baz, 'four' : foo+bar+baz";

        /**
         * @param src
         * @return
         */
        Map[] convert(Hoge[] src);

        /**
         * @param src
         * @param dest
         */
        void convert(Hoge[] src, Map[] dest);

        /**
         * @param src
         * @return
         */
        Map[] convert(List src);

        /**
         * @param src
         * @param dest
         */
        void convert(List src, Map[] dest);

        /**
         * @param src
         * @return
         */
        Map[] convert2(Hoge[] src);
    }

}
