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
import java.util.List;
import java.util.Map;

import org.seasar.extension.dxo.annotation.MapConversion;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author koichik
 */
public class DxoInterceptorTigerTest extends S2TestCase {

    private BeanDxo beanDxo;

    private MapDxo mapDxo;

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

    public void testMapScalar() throws Exception {
        Hoge src = new Hoge(100, "Hoge", new BigDecimal("1000"));

        Map dest = mapDxo.convert(src);

        assertNotNull(dest);
        assertEquals(4, dest.size());
        assertEquals(new Integer(100), dest.get("one"));
        assertEquals("Hoge", dest.get("two"));
        assertEquals(new BigDecimal("1000"), dest.get("three"));
        assertEquals("100Hoge1000", dest.get("four"));
    }

    public void testMapArrayToArray() throws Exception {
        Hoge[] src = new Hoge[2];
        src[0] = new Hoge(100, "Hoge", new BigDecimal("1000"));
        src[1] = new Hoge(200, "HogeHoge", new BigDecimal("2000"));

        Map[] dest = mapDxo.convert(src);

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

    public void testMapListToList() throws Exception {
        List<Hoge> src = new ArrayList<Hoge>();
        src.add(new Hoge(100, "Hoge", new BigDecimal("1000")));
        src.add(new Hoge(200, "HogeHoge", new BigDecimal("2000")));

        List<Map> dest = mapDxo.convert(src);

        assertNotNull(dest);
        assertEquals(2, dest.size());

        Map map = dest.get(0);
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

    public interface BeanDxo {
        Hoge[] convertArrayToArray(HogeHoge[] src);

        List<Hoge> convertArrayToList(HogeHoge[] src);

        Hoge[] convertListToArray(List<HogeHoge> src);

        List<Hoge> convertListToList(List<HogeHoge> src);
    }

    public interface MapDxo {
        @MapConversion("'one' : foo, 'two' : bar, 'three' : baz, 'four' : foo+bar+baz")
        Map convert(Hoge src);

        @MapConversion("'one' : foo, 'two' : bar, 'three' : baz, 'four' : foo+bar+baz")
        Map[] convert(Hoge[] src);

        @MapConversion("'one' : foo, 'two' : bar, 'three' : baz, 'four' : foo+bar+baz")
        List<Map> convert(List<Hoge> src);
    }

}
