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
package org.seasar.extension.dxo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.extension.dxo.annotation.ExcludeNull;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author koichik
 */
public class DxoInterceptorTigerTest extends S2TestCase {

    private BeanDxo beanDxo;

    private FromMapDxo fromMapDxo;

    private ToMapDxo toMapDxo;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(getClass().getName().replace('.', '/') + ".dicon");
    }

    public void testBeanArrayToArray() throws Exception {
        HogeHoge[] src = new HogeHoge[2];
        src[0] = new HogeHoge("100", "Hoge".toCharArray(), 1000);
        src[1] = new HogeHoge("200", "HogeHoge".toCharArray(), 2000);

        Hoge[] dest = beanDxo.convertArrayToArray(src);

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

    public void testBeanArrayToList() throws Exception {
        HogeHoge[] src = new HogeHoge[2];
        src[0] = new HogeHoge("100", "Hoge".toCharArray(), 1000);
        src[1] = new HogeHoge("200", "HogeHoge".toCharArray(), 2000);

        List<Hoge> dest = beanDxo.convertArrayToList(src);

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

    public void testBeanListToArray() throws Exception {
        List<HogeHoge> src = new ArrayList<HogeHoge>();
        src.add(new HogeHoge("100", "Hoge".toCharArray(), 1000));
        src.add(new HogeHoge("200", "HogeHoge".toCharArray(), 2000));

        Hoge[] dest = beanDxo.convertListToArray(src);

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

    public void testBeanListToList() throws Exception {
        List<HogeHoge> src = new ArrayList<HogeHoge>();
        src.add(new HogeHoge("100", "Hoge".toCharArray(), 1000));
        src.add(new HogeHoge("200", "HogeHoge".toCharArray(), 2000));

        List<Hoge> dest = beanDxo.convertListToList(src);

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

    @SuppressWarnings("unchecked")
    public void testFromMap_Scalar() throws Exception {
        Map src = new HashMap();
        src.put("foo", new Integer(100));
        src.put("bar", "Hoge");
        src.put("baz", new BigDecimal("1000"));

        Hoge dest = fromMapDxo.convert(src);

        assertNotNull(dest);
        assertEquals(100, dest.getFoo());
        assertEquals("Hoge", dest.getBar());
        assertEquals(new BigDecimal("1000"), dest.getBaz());
    }

    @SuppressWarnings("unchecked")
    public void testFromMap_ArrayToList() {
        Map[] src = new Map[2];
        src[0] = new HashMap();
        src[0].put("foo", new Integer(100));
        src[0].put("bar", "Hoge");
        src[0].put("baz", new BigDecimal("1000"));
        src[1] = new HashMap();
        src[1].put("foo", new Integer(200));
        src[1].put("bar", "HogeHoge");
        src[1].put("baz", new BigDecimal("2000"));

        List<Hoge> dest = fromMapDxo.convert(src);

        assertNotNull(dest);
        assertEquals(2, dest.size());

        Hoge hoge = dest.get(0);
        assertNotNull(hoge);
        assertEquals(100, hoge.getFoo());
        assertEquals("Hoge", hoge.getBar());
        assertEquals(new BigDecimal("1000"), hoge.getBaz());

        hoge = dest.get(1);
        assertNotNull(hoge);
        assertEquals(200, hoge.getFoo());
        assertEquals("HogeHoge", hoge.getBar());
        assertEquals(new BigDecimal("2000"), hoge.getBaz());
    }

    @SuppressWarnings("unchecked")
    public void testFromMap_ListToArray() {
        List<Map> src = new ArrayList<Map>();
        Map map = new HashMap();
        map.put("foo", new Integer(100));
        map.put("bar", "Hoge");
        map.put("baz", new BigDecimal("1000"));
        src.add(map);

        map = new HashMap();
        map.put("foo", new Integer(200));
        map.put("bar", "HogeHoge");
        map.put("baz", new BigDecimal("2000"));
        src.add(map);

        Hoge[] dest = fromMapDxo.convert(src);

        assertNotNull(dest);
        assertEquals(2, dest.length);

        assertNotNull(dest[0]);
        assertEquals(100, dest[0].getFoo());
        assertEquals("Hoge", dest[0].getBar());
        assertEquals(new BigDecimal("1000"), dest[0].getBaz());

        assertNotNull(dest[1]);
        assertEquals(200, dest[1].getFoo());
        assertEquals("HogeHoge", dest[1].getBar());
        assertEquals(new BigDecimal("2000"), dest[1].getBaz());
    }

    @SuppressWarnings("unchecked")
    public void testToMap_Scalar() throws Exception {
        Hoge src = new Hoge(100, "Hoge", new BigDecimal("1000"));

        Map dest = toMapDxo.convert(src);

        assertNotNull(dest);
        assertEquals(3, dest.size());
        assertEquals(new Integer(100), dest.get("foo"));
        assertEquals("Hoge", dest.get("bar"));
        assertEquals(new BigDecimal("1000"), dest.get("baz"));
    }

    public void testToMap_ArrayToArray() throws Exception {
        Hoge[] src = new Hoge[2];
        src[0] = new Hoge(100, "Hoge", new BigDecimal("1000"));
        src[1] = new Hoge(200, "HogeHoge", new BigDecimal("2000"));

        Map[] dest = toMapDxo.convert(src);

        assertNotNull(dest);
        assertEquals(2, dest.length);

        assertEquals(3, dest[0].size());
        assertEquals(new Integer(100), dest[0].get("foo"));
        assertEquals("Hoge", dest[0].get("bar"));
        assertEquals(new BigDecimal("1000"), dest[0].get("baz"));

        assertEquals(3, dest[1].size());
        assertEquals(new Integer(200), dest[1].get("foo"));
        assertEquals("HogeHoge", dest[1].get("bar"));
        assertEquals(new BigDecimal("2000"), dest[1].get("baz"));
    }

    @SuppressWarnings("unchecked")
    public void testToMap_ListToList() throws Exception {
        List<Hoge> src = new ArrayList<Hoge>();
        src.add(new Hoge(100, "Hoge", new BigDecimal("1000")));
        src.add(new Hoge(200, "HogeHoge", new BigDecimal("2000")));

        List<Map> dest = toMapDxo.convert(src);

        assertNotNull(dest);
        assertEquals(2, dest.size());

        Map map = dest.get(0);
        assertEquals(3, map.size());
        assertEquals(new Integer(100), map.get("foo"));
        assertEquals("Hoge", map.get("bar"));
        assertEquals(new BigDecimal("1000"), map.get("baz"));

        map = dest.get(1);
        assertEquals(3, map.size());
        assertEquals(new Integer(200), map.get("foo"));
        assertEquals("HogeHoge", map.get("bar"));
        assertEquals(new BigDecimal("2000"), map.get("baz"));
    }

    public void testExcludeNull() throws Exception {
        Employee emp = new Employee();
        Department dept = new Department();
        emp.setDepartment(dept);

        EmpDto dest = new EmpDto();
        dest.setEname("foo");
        dest.setDname("bar");
        beanDxo.convertExcludeNull(emp, dest);
        assertEquals("foo", dest.getEname());
        assertEquals("bar", dest.getDname());

        emp.setEname("hoge");
        beanDxo.convertExcludeNull(emp, dest);
        assertEquals("hoge", dest.getEname());
        assertEquals("bar", dest.getDname());

        dept.setDname("hogehoge");
        beanDxo.convertExcludeNull(emp, dest);
        assertEquals("hoge", dest.getEname());
        assertEquals("hogehoge", dest.getDname());
    }

    public interface BeanDxo {
        Hoge[] convertArrayToArray(HogeHoge[] src);

        List<Hoge> convertArrayToList(HogeHoge[] src);

        Hoge[] convertListToArray(List<HogeHoge> src);

        List<Hoge> convertListToList(List<HogeHoge> src);

        @ExcludeNull
        void convertExcludeNull(Employee employee, EmpDto empDto);
    }

    @SuppressWarnings("unchecked")
    public interface FromMapDxo {
        Hoge convert(Map src);

        List<Hoge> convert(Map[] src);

        Hoge[] convert(List<Map> src);
    }

    @SuppressWarnings("unchecked")
    public interface ToMapDxo {
        Map convert(Hoge src);

        Map[] convert(Hoge[] src);

        List<Map> convert(List<Hoge> src);
    }

}
