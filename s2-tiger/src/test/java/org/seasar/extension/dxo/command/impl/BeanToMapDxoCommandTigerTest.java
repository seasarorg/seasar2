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
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.extension.dxo.Hoge;
import org.seasar.framework.util.ClassUtil;

/**
 * @author koichik
 */
public class BeanToMapDxoCommandTigerTest extends TestCase {

    private static final String MAP_CONVERSION = "'one' : foo, 'two' : bar, 'three' : baz, 'four' : foo+bar+baz";

    public void testListToArray1() {
        BeanToMapDxoCommand command = new BeanToMapDxoCommand(ClassUtil
                .getMethod(ToArrayDxo.class, "convert",
                        new Class[] { List.class }), MAP_CONVERSION);
        List<Hoge> src = new ArrayList<Hoge>();
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

    public void testListToArray2() {
        BeanToMapDxoCommand command = new BeanToMapDxoCommand(ClassUtil
                .getMethod(ToArrayDxo.class, "convert", new Class[] {
                        List.class, Map[].class }), MAP_CONVERSION);
        List<Hoge> src = new ArrayList<Hoge>();
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

    @SuppressWarnings("unchecked")
    public void testArrayToList1() {
        BeanToMapDxoCommand command = new BeanToMapDxoCommand(ClassUtil
                .getMethod(ToListDxo.class, "convert",
                        new Class[] { Hoge[].class }), MAP_CONVERSION);
        Hoge[] src = new Hoge[2];
        src[0] = new Hoge(100, "Hoge", new BigDecimal("1000"));
        src[1] = new Hoge(200, "HogeHoge", new BigDecimal("2000"));

        List<Map<String, ?>> dest = (List<Map<String, ?>>) command
                .execute(new Object[] { src });

        assertNotNull(dest);
        assertEquals(2, dest.size());

        Map<String, ?> map = dest.get(0);
        assertEquals(4, map.size());
        assertEquals(new Integer(100), map.get("one"));
        assertEquals("Hoge", map.get("two"));
        assertEquals(new BigDecimal("1000"), map.get("three"));
        assertEquals("100Hoge1000", map.get("four"));

        map = dest.get(1);
        assertEquals(4, map.size());
        assertEquals(new Integer(200), map.get("one"));
        assertEquals("HogeHoge", map.get("two"));
        assertEquals(new BigDecimal("2000"), map.get("three"));
        assertEquals("200HogeHoge2000", map.get("four"));
    }

    public void testArrayToList2() {
        BeanToMapDxoCommand command = new BeanToMapDxoCommand(ClassUtil
                .getMethod(ToListDxo.class, "convert", new Class[] {
                        Hoge[].class, List.class }), MAP_CONVERSION);
        Hoge[] src = new Hoge[2];
        src[0] = new Hoge(100, "Hoge", new BigDecimal("1000"));
        src[1] = new Hoge(200, "HogeHoge", new BigDecimal("2000"));
        List<Map<String, ?>> dest = new ArrayList<Map<String, ?>>();

        command.execute(new Object[] { src, dest });

        assertNotNull(dest);
        assertEquals(2, dest.size());

        Map<String, ?> map = dest.get(0);
        assertEquals(4, map.size());
        assertEquals(new Integer(100), map.get("one"));
        assertEquals("Hoge", map.get("two"));
        assertEquals(new BigDecimal("1000"), map.get("three"));
        assertEquals("100Hoge1000", map.get("four"));

        map = dest.get(1);
        assertEquals(4, map.size());
        assertEquals(new Integer(200), map.get("one"));
        assertEquals("HogeHoge", map.get("two"));
        assertEquals(new BigDecimal("2000"), map.get("three"));
        assertEquals("200HogeHoge2000", map.get("four"));
    }

    @SuppressWarnings("unchecked")
    public void testListToList1() {
        BeanToMapDxoCommand command = new BeanToMapDxoCommand(ClassUtil
                .getMethod(ToListDxo.class, "convert",
                        new Class[] { List.class }), MAP_CONVERSION);
        List<Hoge> src = new ArrayList<Hoge>();
        src.add(new Hoge(100, "Hoge", new BigDecimal("1000")));
        src.add(new Hoge(200, "HogeHoge", new BigDecimal("2000")));

        List<Map<String, ?>> dest = (List<Map<String, ?>>) command
                .execute(new Object[] { src });

        assertNotNull(dest);
        assertEquals(2, dest.size());

        Map<String, ?> map = dest.get(0);
        assertEquals(4, map.size());
        assertEquals(new Integer(100), map.get("one"));
        assertEquals("Hoge", map.get("two"));
        assertEquals(new BigDecimal("1000"), map.get("three"));
        assertEquals("100Hoge1000", map.get("four"));

        map = dest.get(1);
        assertEquals(4, map.size());
        assertEquals(new Integer(200), map.get("one"));
        assertEquals("HogeHoge", map.get("two"));
        assertEquals(new BigDecimal("2000"), map.get("three"));
        assertEquals("200HogeHoge2000", map.get("four"));
    }

    public void testListToList2() {
        BeanToMapDxoCommand command = new BeanToMapDxoCommand(ClassUtil
                .getMethod(ToListDxo.class, "convert", new Class[] {
                        List.class, List.class }), MAP_CONVERSION);
        List<Hoge> src = new ArrayList<Hoge>();
        src.add(new Hoge(100, "Hoge", new BigDecimal("1000")));
        src.add(new Hoge(200, "HogeHoge", new BigDecimal("2000")));
        List<Map<String, ?>> dest = new ArrayList<Map<String, ?>>();

        command.execute(new Object[] { src, dest });

        assertNotNull(dest);
        assertEquals(2, dest.size());

        Map<String, ?> map = dest.get(0);
        assertEquals(4, map.size());
        assertEquals(new Integer(100), map.get("one"));
        assertEquals("Hoge", map.get("two"));
        assertEquals(new BigDecimal("1000"), map.get("three"));
        assertEquals("100Hoge1000", map.get("four"));

        map = dest.get(1);
        assertEquals(4, map.size());
        assertEquals(new Integer(200), map.get("one"));
        assertEquals("HogeHoge", map.get("two"));
        assertEquals(new BigDecimal("2000"), map.get("three"));
        assertEquals("200HogeHoge2000", map.get("four"));
    }

    public interface ToArrayDxo {
        Map[] convert(List<?> src);

        void convert(List<?> src, Map[] dest);
    }

    public interface ToListDxo {
        List<Map<String, ?>> convert(Hoge[] src);

        List<Map<String, ?>> convert(List<?> src);

        void convert(Hoge[] src, List<Map<String, ?>> dest);

        void convert(List<?> src, List<Map<String, ?>> dest);
    }

}
