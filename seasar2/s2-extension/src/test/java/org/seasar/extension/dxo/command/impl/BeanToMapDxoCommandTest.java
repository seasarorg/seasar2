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
package org.seasar.extension.dxo.command.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.extension.dxo.Hoge;
import org.seasar.framework.util.ClassUtil;

/**
 * @author koichik
 * 
 */
public class BeanToMapDxoCommandTest extends TestCase {

    private static final String MAP_CONVERSION = "'one' : foo, 'two' : bar, 'three' : baz, 'four' : foo+bar+baz";

    public void testScalar1() throws Exception {
        BeanToMapDxoCommand command = new BeanToMapDxoCommand(ClassUtil
                .getMethod(ScalarDxo.class, "convert",
                        new Class[] { Hoge.class }));
        Hoge src = new Hoge(100, "Hoge", new BigDecimal("1000"));
        Map dest = (Map) command.execute(new Object[] { src });
        assertNotNull(dest);
        assertEquals(3, dest.size());
        assertEquals(new Integer(100), dest.get("foo"));
        assertEquals("Hoge", dest.get("bar"));
        assertEquals(new BigDecimal("1000"), dest.get("baz"));
    }

    public void testScalar2() throws Exception {
        BeanToMapDxoCommand command = new BeanToMapDxoCommand(ClassUtil
                .getMethod(ScalarDxo.class, "convert", new Class[] {
                        Hoge.class, Map.class }), MAP_CONVERSION);
        Hoge src = new Hoge(100, "Hoge", new BigDecimal("1000"));
        Map dest = new HashMap();
        command.execute(new Object[] { src, dest });
        assertNotNull(dest);
        assertEquals(4, dest.size());
        assertEquals(new Integer(100), dest.get("one"));
        assertEquals("Hoge", dest.get("two"));
        assertEquals(new BigDecimal("1000"), dest.get("three"));
        assertEquals("100Hoge1000", dest.get("four"));
    }

    public void testScalar3() throws Exception {
        BeanToMapDxoCommand command = new BeanToMapDxoCommand(ClassUtil
                .getMethod(ScalarDxo.class, "convert", new Class[] {
                        Hoge.class, Map.class }), "one : foo");
        Hoge src = new Hoge(100, "Hoge", new BigDecimal("1000"));
        Map dest = new HashMap();
        command.execute(new Object[] { src, dest });
        assertNotNull(dest);
        assertEquals(1, dest.size());
        assertEquals(new Integer(100), dest.get("one"));
        assertNull(dest.get("two"));
        assertNull(dest.get("three"));
        assertNull(dest.get("four"));
    }

    public void testArrayToArray1() throws Exception {
        BeanToMapDxoCommand command = new BeanToMapDxoCommand(ClassUtil
                .getMethod(ToArrayDxo.class, "convert",
                        new Class[] { Hoge[].class }), MAP_CONVERSION);
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

    public void testArrayToArray2() throws Exception {
        BeanToMapDxoCommand command = new BeanToMapDxoCommand(ClassUtil
                .getMethod(ToArrayDxo.class, "convert", new Class[] {
                        Hoge[].class, Map[].class }), MAP_CONVERSION);
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

    public void testListToArray1() throws Exception {
        BeanToMapDxoCommand command = new BeanToMapDxoCommand(ClassUtil
                .getMethod(ToArrayDxo.class, "convert",
                        new Class[] { List.class }), MAP_CONVERSION);
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

    public void testListToArray2() throws Exception {
        BeanToMapDxoCommand command = new BeanToMapDxoCommand(ClassUtil
                .getMethod(ToArrayDxo.class, "convert", new Class[] {
                        List.class, Map[].class }), MAP_CONVERSION);
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

    public void testArrayToList1() throws Exception {
        BeanToMapDxoCommand command = new BeanToMapDxoCommand(ClassUtil
                .getMethod(ToListDxo.class, "convert",
                        new Class[] { Hoge[].class }), MAP_CONVERSION);
        Hoge[] src = new Hoge[2];
        src[0] = new Hoge(100, "Hoge", new BigDecimal("1000"));
        src[1] = new Hoge(200, "HogeHoge", new BigDecimal("2000"));
        List dest = (List) command.execute(new Object[] { src });
        assertNotNull(dest);
        assertEquals(2, dest.size());

        Map map = (Map) dest.get(0);
        assertEquals(4, map.size());
        assertEquals(new Integer(100), map.get("one"));
        assertEquals("Hoge", map.get("two"));
        assertEquals(new BigDecimal("1000"), map.get("three"));
        assertEquals("100Hoge1000", map.get("four"));

        map = (Map) dest.get(1);
        assertEquals(4, map.size());
        assertEquals(new Integer(200), map.get("one"));
        assertEquals("HogeHoge", map.get("two"));
        assertEquals(new BigDecimal("2000"), map.get("three"));
        assertEquals("200HogeHoge2000", map.get("four"));
    }

    public void testArrayToList2() throws Exception {
        BeanToMapDxoCommand command = new BeanToMapDxoCommand(ClassUtil
                .getMethod(ToListDxo.class, "convert", new Class[] {
                        Hoge[].class, List.class }), MAP_CONVERSION);
        Hoge[] src = new Hoge[2];
        src[0] = new Hoge(100, "Hoge", new BigDecimal("1000"));
        src[1] = new Hoge(200, "HogeHoge", new BigDecimal("2000"));
        List dest = new ArrayList();
        command.execute(new Object[] { src, dest });
        assertNotNull(dest);
        assertEquals(2, dest.size());

        Map map = (Map) dest.get(0);
        assertEquals(4, map.size());
        assertEquals(new Integer(100), map.get("one"));
        assertEquals("Hoge", map.get("two"));
        assertEquals(new BigDecimal("1000"), map.get("three"));
        assertEquals("100Hoge1000", map.get("four"));

        map = (Map) dest.get(1);
        assertEquals(4, map.size());
        assertEquals(new Integer(200), map.get("one"));
        assertEquals("HogeHoge", map.get("two"));
        assertEquals(new BigDecimal("2000"), map.get("three"));
        assertEquals("200HogeHoge2000", map.get("four"));
    }

    public void testListToList1() throws Exception {
        BeanToMapDxoCommand command = new BeanToMapDxoCommand(ClassUtil
                .getMethod(ToListDxo.class, "convert",
                        new Class[] { List.class }), MAP_CONVERSION);
        List src = new ArrayList();
        src.add(new Hoge(100, "Hoge", new BigDecimal("1000")));
        src.add(new Hoge(200, "HogeHoge", new BigDecimal("2000")));
        List dest = (List) command.execute(new Object[] { src });
        assertNotNull(dest);
        assertEquals(2, dest.size());

        Map map = (Map) dest.get(0);
        assertEquals(4, map.size());
        assertEquals(new Integer(100), map.get("one"));
        assertEquals("Hoge", map.get("two"));
        assertEquals(new BigDecimal("1000"), map.get("three"));
        assertEquals("100Hoge1000", map.get("four"));

        map = (Map) dest.get(1);
        assertEquals(4, map.size());
        assertEquals(new Integer(200), map.get("one"));
        assertEquals("HogeHoge", map.get("two"));
        assertEquals(new BigDecimal("2000"), map.get("three"));
        assertEquals("200HogeHoge2000", map.get("four"));
    }

    public void testListToList2() throws Exception {
        BeanToMapDxoCommand command = new BeanToMapDxoCommand(ClassUtil
                .getMethod(ToListDxo.class, "convert", new Class[] {
                        List.class, List.class }), MAP_CONVERSION);
        List src = new ArrayList();
        src.add(new Hoge(100, "Hoge", new BigDecimal("1000")));
        src.add(new Hoge(200, "HogeHoge", new BigDecimal("2000")));
        List dest = new ArrayList();
        command.execute(new Object[] { src, dest });
        assertNotNull(dest);
        assertEquals(2, dest.size());

        Map map = (Map) dest.get(0);
        assertEquals(4, map.size());
        assertEquals(new Integer(100), map.get("one"));
        assertEquals("Hoge", map.get("two"));
        assertEquals(new BigDecimal("1000"), map.get("three"));
        assertEquals("100Hoge1000", map.get("four"));

        map = (Map) dest.get(1);
        assertEquals(4, map.size());
        assertEquals(new Integer(200), map.get("one"));
        assertEquals("HogeHoge", map.get("two"));
        assertEquals(new BigDecimal("2000"), map.get("three"));
        assertEquals("200HogeHoge2000", map.get("four"));
    }

    public interface ScalarDxo {
        Map convert(Hoge src);

        void convert(Hoge src, Map dest);
    }

    public interface ToArrayDxo {
        Map[] convert(Hoge[] src);

        Map[] convert(List src);

        void convert(Hoge[] src, Map[] dest);

        void convert(List src, Map[] dest);
    }

    public interface ToListDxo {
        List convert(Hoge[] src);

        List convert(List src);

        void convert(Hoge[] src, List dest);

        void convert(List src, List dest);
    }
}
