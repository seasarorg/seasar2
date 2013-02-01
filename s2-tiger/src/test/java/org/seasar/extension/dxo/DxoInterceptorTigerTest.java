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
package org.seasar.extension.dxo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.seasar.extension.dxo.annotation.DestPrefix;
import org.seasar.extension.dxo.annotation.ExcludeNull;
import org.seasar.extension.dxo.annotation.SourcePrefix;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author koichik
 */
public class DxoInterceptorTigerTest extends S2TestCase {

    BeanDxo beanDxo;

    FromMapDxo fromMapDxo;

    ToMapDxo toMapDxo;

    SearchDxo searchDxo;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(getClass().getName().replace('.', '/') + ".dicon");
    }

    /**
     * @throws Exception
     */
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

    /**
     * @throws Exception
     */
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

    /**
     * @throws Exception
     */
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

    /**
     * @throws Exception
     */
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

    /**
     * @throws Exception
     */
    public void testBean_GenericProperty() throws Exception {
        Aaa src = new Aaa();
        src.prop1 = new Integer[] { 1, 2, 3, null, 5 };
        src.setProp2(Arrays.asList(1, 2, 3, 4, null));

        Bbb dest = beanDxo.convert(src);

        assertNotNull(dest);
        assertEquals(5, dest.getProp1().size());
        assertEquals("1", dest.getProp1().get(0));
        assertEquals("2", dest.getProp1().get(1));
        assertEquals("3", dest.getProp1().get(2));
        assertNull(dest.getProp1().get(3));
        assertEquals("5", dest.getProp1().get(4));

        assertEquals(5, dest.prop2.size());
        Iterator<String> it = dest.prop2.iterator();
        assertEquals("1", it.next());
        assertEquals("2", it.next());
        assertEquals("3", it.next());
        assertEquals("4", it.next());
        assertNull(it.next());
    }

    /**
     * @throws Exception
     */
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

    /**
     * 
     */
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

    /**
     * 
     */
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

    /**
     * @throws Exception
     */
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

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
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

    /**
     * @throws Exception
     */
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

    /**
     * @throws Exception
     */
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

    /**
     * @throws Exception
     */
    public void testEnum() throws Exception {
        Foo foo = new Foo(Color.RED.ordinal(), Color.GREEN.name());
        Bar bar = beanDxo.convert(foo);
        assertEquals(Color.RED, bar.getOrdinal());
        assertEquals(Color.GREEN, bar.getName());

        foo = beanDxo.convert(bar);
        assertEquals(Color.RED.ordinal(), foo.getOrdinal());
        assertEquals(Color.GREEN.name(), foo.getName());

        // [CONTAINER-400]
        Bar src = new Bar(Color.RED, Color.GREEN);
        Bar dest = new Bar();
        beanDxo.convert(src, dest);
        assertEquals(Color.RED, dest.getOrdinal());
        assertEquals(Color.GREEN, dest.getName());
    }

    /**
     * @throws Exception
     */
    public void testSrcPrefix_BeanToBean() throws Exception {
        SearchPage src = new SearchPage();
        src.setSearch_name_LIKE("%hoge%");
        src.setSearch_age_GT(Integer.valueOf(25));
        src.setName("foo");
        src.setAge(Integer.valueOf(100));
        src.setHoge("hoge");

        SearchDto dest = searchDxo.convert(src);
        assertEquals("%hoge%", dest.getName_LIKE());
        assertEquals(Integer.valueOf(25), dest.getAge_GT());
        assertNull(dest.getName());
        assertNull(dest.getAge());
        assertNull(dest.getHoge());
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testSrcPrefix_MapToBean() throws Exception {
        Map src = new HashMap();
        src.put("search_name_LIKE", "%hoge%");
        src.put("search_age_GT", Integer.valueOf(25));
        src.put("name", "foo");
        src.put("age", Integer.valueOf(100));
        src.put("hoge", "hoge");

        SearchDto dest = searchDxo.convert(src);
        assertEquals("%hoge%", dest.getName_LIKE());
        assertEquals(Integer.valueOf(25), dest.getAge_GT());
        assertNull(dest.getName());
        assertNull(dest.getAge());
        assertNull(dest.getHoge());
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testSrcPrefix_BeanToMap() throws Exception {
        SearchPage src = new SearchPage();
        src.setSearch_name_LIKE("%hoge%");
        src.setSearch_age_GT(Integer.valueOf(25));
        src.setName("foo");
        src.setAge(Integer.valueOf(100));
        src.setHoge("hoge");

        Map dest = new HashMap();
        searchDxo.convert(src, dest);
        assertEquals("%hoge%", dest.get("name_LIKE"));
        assertEquals(Integer.valueOf(25), dest.get("age_GT"));
        assertNull(dest.get("name"));
        assertNull(dest.get("age"));
        assertNull(dest.get("hoge"));
    }

    /**
     * @throws Exception
     */
    public void testDestPrefix_BeanToBean() throws Exception {
        SearchDto src = new SearchDto();
        src.setName_LIKE("%hoge%");
        src.setAge_GT(Integer.valueOf(25));
        src.setName("foo");
        src.setAge(Integer.valueOf(100));
        src.setHoge("hoge");

        SearchPage dest = new SearchPage();
        searchDxo.convert(src, dest);
        assertEquals("%hoge%", dest.getSearch_name_LIKE());
        assertEquals(Integer.valueOf(25), dest.getSearch_age_GT());
        assertNull(dest.getName());
        assertNull(dest.getAge());
        assertNull(dest.getHoge());
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testDestPrefix_MapToBean() throws Exception {
        Map src = new HashMap();
        src.put("name_LIKE", "%hoge%");
        src.put("age_GT", Integer.valueOf(25));
        src.put("name", "foo");
        src.put("age", Integer.valueOf(100));
        src.put("hoge", "hoge");

        SearchPage dest = new SearchPage();
        searchDxo.convert(src, dest);
        assertEquals("%hoge%", dest.getSearch_name_LIKE());
        assertEquals(Integer.valueOf(25), dest.getSearch_age_GT());
        assertNull(dest.getName());
        assertNull(dest.getAge());
        assertNull(dest.getHoge());
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void testDestPrefix_BeanToMap() throws Exception {
        SearchDto src = new SearchDto();
        src.setName_LIKE("%hoge%");
        src.setAge_GT(Integer.valueOf(25));
        src.setName("foo");
        src.setAge(Integer.valueOf(100));
        src.setHoge("hoge");

        Map dest = new HashMap();
        searchDxo.convert(src, dest);
        assertEquals("%hoge%", dest.get("search_name_LIKE"));
        assertEquals(Integer.valueOf(25), dest.get("search_age_GT"));
        assertNull(dest.get("name"));
        assertNull(dest.get("age"));
        assertNull(dest.get("hoge"));
    }

    /**
     * 
     */
    public interface BeanDxo {

        /**
         * @param src
         * @return
         */
        Hoge[] convertArrayToArray(HogeHoge[] src);

        /**
         * @param src
         * @return
         */
        List<Hoge> convertArrayToList(HogeHoge[] src);

        /**
         * @param src
         * @return
         */
        Hoge[] convertListToArray(List<HogeHoge> src);

        /**
         * @param src
         * @return
         */
        List<Hoge> convertListToList(List<HogeHoge> src);

        /**
         * @param employee
         * @param empDto
         */
        @ExcludeNull
        void convertExcludeNull(Employee employee, EmpDto empDto);

        /**
         * @param bar
         * @return
         */
        Foo convert(Bar bar);

        /**
         * @param foo
         * @return
         */
        Bar convert(Foo foo);

        /**
         * @param aaa
         * @return
         */
        Bbb convert(Aaa aaa);

        /**
         * @param src
         * @param dest
         */
        void convert(Bar src, Bar dest);
    }

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    public interface FromMapDxo {

        /**
         * @param src
         * @return
         */
        Hoge convert(Map src);

        /**
         * @param src
         * @return
         */
        List<Hoge> convert(Map[] src);

        /**
         * @param src
         * @return
         */
        Hoge[] convert(List<Map> src);
    }

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    public interface ToMapDxo {

        /**
         * @param src
         * @return
         */
        Map convert(Hoge src);

        /**
         * @param src
         * @return
         */
        Map[] convert(Hoge[] src);

        /**
         * @param src
         * @return
         */
        List<Map> convert(List<Hoge> src);
    }

    /**
     * 
     */
    public interface SearchDxo {

        /**
         * @param src
         * @return
         */
        @SourcePrefix("search_")
        SearchDto convert(SearchPage src);

        /**
         * @param src
         * @return
         */
        @SourcePrefix("search_")
        @SuppressWarnings("unchecked")
        SearchDto convert(Map src);

        /**
         * @param src
         * @param dest
         * @return
         */
        @SourcePrefix("search_")
        @SuppressWarnings("unchecked")
        void convert(SearchPage src, Map dest);

        /**
         * @param src
         * @param dest
         */
        @DestPrefix("search_")
        void convert(SearchDto src, SearchPage dest);

        /**
         * @param src
         * @param dest
         */
        @SuppressWarnings("unchecked")
        @DestPrefix("search_")
        void convert(Map src, SearchPage dest);

        /**
         * @param src
         * @param dest
         */
        @SuppressWarnings("unchecked")
        @DestPrefix("search_")
        void convert(SearchDto src, Map dest);

    }

}
