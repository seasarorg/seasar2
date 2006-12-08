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
import java.util.HashMap;
import java.util.Map;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.util.CaseInsensitiveMap;

/**
 * @author koichik
 * 
 */
public class DxoInterceptorTest extends S2TestCase {

    private BeanDxo beanDxo;

    private FromMapDxo fromMapDxo;

    private ToMapDxo toMapDxo;

    protected void setUp() throws Exception {
        super.setUp();
        include(getClass().getName().replace('.', '/') + ".dicon");
    }

    public void testBeanScalar1() throws Exception {
        Hoge src = new Hoge(100, "Hoge", new BigDecimal("1000"));

        Hoge dest = beanDxo.convert1(src);

        assertNotNull(dest);
        assertSame(src, dest);
    }

    public void testBeanScalar2() throws Exception {
        Hoge src = new Hoge(100, "Hoge", new BigDecimal("1000"));

        HogeHoge dest = beanDxo.convert2(src);

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

    public void testBeanScalar3() throws Exception {
        HogeHoge src = new HogeHoge("100", "Hoge".toCharArray(), 1000);

        Hoge dest = beanDxo.convert3(src);

        assertNotNull(dest);
        assertEquals(100, dest.getFoo());
        assertEquals("Hoge", dest.getBar());
        assertEquals(new BigDecimal("1000"), dest.getBaz());
    }

    public void testBeanScalar4() throws Exception {
        HogeHoge src = new HogeHoge("100", "Hoge".toCharArray(), 1000);

        HogeHoge dest = beanDxo.convert4(src);

        assertNotNull(dest);
        assertSame(src, dest);
    }

    public void testBeanArrayToArray() {
        Hoge[] src = new Hoge[2];
        src[0] = new Hoge(100, "Hoge", new BigDecimal("1000"));
        src[1] = new Hoge(200, "HogeHoge", new BigDecimal("2000"));

        HogeHoge[] dest = beanDxo.convert(src);

        assertNotNull(dest);
        assertEquals(2, dest.length);

        assertEquals("100", dest[0].getFoo());
        assertEquals(4, dest[0].getBar().length);
        assertEquals('H', dest[0].getBar()[0]);
        assertEquals('o', dest[0].getBar()[1]);
        assertEquals('g', dest[0].getBar()[2]);
        assertEquals('e', dest[0].getBar()[3]);
        assertEquals(1000, dest[0].getBaz());

        assertEquals("200", dest[1].getFoo());
        assertEquals(8, dest[1].getBar().length);
        assertEquals('H', dest[1].getBar()[0]);
        assertEquals('o', dest[1].getBar()[1]);
        assertEquals('g', dest[1].getBar()[2]);
        assertEquals('e', dest[1].getBar()[3]);
        assertEquals('H', dest[1].getBar()[4]);
        assertEquals('o', dest[1].getBar()[5]);
        assertEquals('g', dest[1].getBar()[6]);
        assertEquals('e', dest[1].getBar()[7]);
        assertEquals(2000, dest[1].getBaz());
    }

    public void testNestedProperty() throws Exception {
        Employee emp = new Employee();
        emp.setEname("Foo");
        Department dept = new Department();
        dept.setDname("Bar");
        emp.setDepartment(dept);

        EmpDto dest = beanDxo.convert(emp);

        assertNotNull(dest);
        assertEquals("Foo", dest.getEname());
        assertEquals("Bar", dest.getDname());
        assertNull(dest.getMname());

        Employee mgr = new Employee();
        mgr.setEname("Baz");
        emp.setMgr(mgr);

        dest = beanDxo.convert(emp);

        assertNotNull(dest);
        assertEquals("Foo", dest.getEname());
        assertEquals("Bar", dest.getDname());
        assertEquals("Baz", dest.getMname());
    }

    public void testEmptyString() throws Exception {
        EmpDto dto = new EmpDto();
        dto.setSal(null);
        dto.setComm("");

        Employee dest = beanDxo.convert(dto);

        assertNotNull(dest);
        assertNull(dest.getSal());
        assertNull(dest.getComm());
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

    public void testFromMap_Scalar2() throws Exception {
        Map src = new CaseInsensitiveMap();
        src.put("barbar", "Hoge");

        Hoge dest = fromMapDxo.convert(src);

        assertNotNull(dest);
        assertEquals("Hoge", dest.getBarBar());
    }

    public void testFromMap_ArrayToArray() {
        Map[] src = new Map[2];
        src[0] = new HashMap();
        src[0].put("foo", new Integer(100));
        src[0].put("bar", "Hoge");
        src[0].put("baz", new BigDecimal("1000"));
        src[1] = new HashMap();
        src[1].put("foo", new Integer(200));
        src[1].put("bar", "HogeHoge");
        src[1].put("baz", new BigDecimal("2000"));

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

    public void testToMap_Scalar() throws Exception {
        Hoge src = new Hoge(100, "Hoge", new BigDecimal("1000"));

        Map dest = toMapDxo.convert(src);

        assertNotNull(dest);
        assertEquals(4, dest.size());
        assertEquals(new Integer(100), dest.get("one"));
        assertEquals("Hoge", dest.get("two"));
        assertEquals(new BigDecimal("1000"), dest.get("three"));
        assertEquals("100Hoge1000", dest.get("four"));
    }

    public void testToMap_ArrayToArray() {
        Hoge[] src = new Hoge[2];
        src[0] = new Hoge(100, "Hoge", new BigDecimal("1000"));
        src[1] = new Hoge(200, "HogeHoge", new BigDecimal("2000"));

        Map[] dest = toMapDxo.convert(src);

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

    public interface BeanDxo {
        Hoge convert1(Hoge src);

        HogeHoge convert2(Hoge src);

        Hoge convert3(HogeHoge src);

        HogeHoge convert4(HogeHoge src);

        HogeHoge[] convert(Hoge[] src);

        String convert_Employee_CONVERSION_RULE = "'mname' : mgr != null ? mgr.ename : null";

        EmpDto convert(Employee emp);

        Employee convert(EmpDto empDto);

        String convertExcludeNull_EXCLUDE_NULL = null;

        void convertExcludeNull(Employee employee, EmpDto empDto);
    }

    public interface FromMapDxo {
        Hoge convert(Map src);

        Hoge[] convert(Map[] src);
    }

    public interface ToMapDxo {
        String convert_Hoge_CONVERSION_RULE = "'one' : foo, 'two' : bar, 'three' : baz, 'four' : foo+bar+baz";

        String convert_Hoge$_CONVERSION_RULE = "'one' : foo, 'two' : bar, 'three' : baz, 'four' : foo+bar+baz";

        Map convert(Hoge src);

        Map[] convert(Hoge[] src);
    }

}
