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

import org.seasar.extension.unit.S2TestCase;

/**
 * @author koichik
 * 
 */
public class DxoInterceptorTest extends S2TestCase {

    protected void setUp() throws Exception {
        super.setUp();
        include(getClass().getName().replace('.', '/') + ".dicon");
    }

    public void testMapScalar() throws Exception {
        MapDxo dxo = (MapDxo) getComponent(MapDxo.class);
        Hoge src = new Hoge(100, "Hoge", new BigDecimal("1000"));

        Map dest = dxo.convert(src);

        assertNotNull(dest);
        assertEquals(4, dest.size());
        assertEquals(new Integer(100), dest.get("one"));
        assertEquals("Hoge", dest.get("two"));
        assertEquals(new BigDecimal("1000"), dest.get("three"));
        assertEquals("100Hoge1000", dest.get("four"));
    }

    public void testMapArrayToArray() {
        MapDxo dxo = (MapDxo) getComponent(MapDxo.class);
        Hoge[] src = new Hoge[2];
        src[0] = new Hoge(100, "Hoge", new BigDecimal("1000"));
        src[1] = new Hoge(200, "HogeHoge", new BigDecimal("2000"));

        Map[] dest = dxo.convert(src);

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

    public void testMapListToList() {
        MapDxo dxo = (MapDxo) getComponent(MapDxo.class);
        List src = new ArrayList();
        src.add(new Hoge(100, "Hoge", new BigDecimal("1000")));
        src.add(new Hoge(200, "HogeHoge", new BigDecimal("2000")));

        List dest = dxo.convert(src);

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

    public interface MapDxo {
        String convert_Hoge_MAP_CONVERSION = "'one' : foo, 'two' : bar, 'three' : baz, 'four' : foo+bar+baz";

        String convert_Hoge$_MAP_CONVERSION = "'one' : foo, 'two' : bar, 'three' : baz, 'four' : foo+bar+baz";

        String convert_List_MAP_CONVERSION = "'one' : foo, 'two' : bar, 'three' : baz, 'four' : foo+bar+baz";

        Map convert(Hoge src);

        Map[] convert(Hoge[] src);

        List convert(List src);
    }

}
