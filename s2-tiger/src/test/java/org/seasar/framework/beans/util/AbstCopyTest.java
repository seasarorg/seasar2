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
package org.seasar.framework.beans.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.framework.beans.ConverterRuntimeException;
import org.seasar.framework.beans.converter.DateConverter;
import org.seasar.framework.beans.converter.NumberConverter;

/**
 * @author higa
 */
public class AbstCopyTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testIncludes() throws Exception {
        MyCopy copy = new MyCopy();
        assertSame(copy, copy.includes(BeanNames.hoge()));
        assertEquals(1, copy.includePropertyNames.length);
        assertEquals("hoge", copy.includePropertyNames[0]);
    }

    /**
     * @throws Exception
     */
    public void testExcludes() throws Exception {
        MyCopy copy = new MyCopy();
        assertSame(copy, copy.excludes(BeanNames.hoge()));
        assertEquals(1, copy.excludePropertyNames.length);
        assertEquals("hoge", copy.excludePropertyNames[0]);
    }

    /**
     * @throws Exception
     */
    public void testPrefix() throws Exception {
        MyCopy copy = new MyCopy();
        assertSame(copy, copy.prefix(BeanNames.search_()));
        assertEquals("search_", copy.prefix);
    }

    /**
     * @throws Exception
     */
    public void testBeanDelimiter() throws Exception {
        MyCopy copy = new MyCopy();
        assertSame(copy, copy.beanDelimiter('#'));
        assertEquals('#', copy.beanDelimiter);
    }

    /**
     * @throws Exception
     */
    public void testMapDelimiter() throws Exception {
        MyCopy copy = new MyCopy();
        assertSame(copy, copy.mapDelimiter('#'));
        assertEquals('#', copy.mapDelimiter);
    }

    /**
     * @throws Exception
     */
    public void testIsTargetProperty() throws Exception {
        MyCopy copy = new MyCopy();
        assertTrue(copy.isTargetProperty("hoge"));
    }

    /**
     * @throws Exception
     */
    public void testIsTargetProperty_includes() throws Exception {
        MyCopy copy = new MyCopy().includes(BeanNames.hoge());
        assertTrue(copy.isTargetProperty("hoge"));
        assertFalse(copy.isTargetProperty("hoge2"));
    }

    /**
     * @throws Exception
     */
    public void testIsTargetProperty_includes_prefix() throws Exception {
        MyCopy copy = new MyCopy().includes(BeanNames.search_aaa(),
                BeanNames.bbb()).prefix(BeanNames.search_());
        assertTrue(copy.isTargetProperty("search_aaa"));
        assertFalse(copy.isTargetProperty("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testIsTargetProperty_excludes() throws Exception {
        MyCopy copy = new MyCopy().excludes(BeanNames.hoge());
        assertFalse(copy.isTargetProperty("hoge"));
        assertTrue(copy.isTargetProperty("hoge2"));
    }

    /**
     * @throws Exception
     */
    public void testIsTargetProperty_excludes_prefix() throws Exception {
        MyCopy copy = new MyCopy().prefix(BeanNames.abc_()).excludes(
                BeanNames.abc_exclude());
        assertTrue(copy.isTargetProperty("abc_value"));
        assertFalse(copy.isTargetProperty("abc_exclude"));
        assertFalse(copy.isTargetProperty("ab"));
    }

    /**
     * @throws Exception
     */
    public void testIsTargetProperty_prefix() throws Exception {
        MyCopy copy = new MyCopy().prefix(BeanNames.search_());
        assertTrue(copy.isTargetProperty("search_aaa"));
        assertFalse(copy.isTargetProperty("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testIsTargetProperty_includes_excludes() throws Exception {
        MyCopy copy = new MyCopy();
        copy.includes(BeanNames.hoge(), BeanNames.hoge2());
        copy.excludes(BeanNames.hoge2(), BeanNames.hoge3());
        assertTrue(copy.isTargetProperty("hoge"));
        assertFalse(copy.isTargetProperty("hoge2"));
        assertFalse(copy.isTargetProperty("hoge3"));
        assertFalse(copy.isTargetProperty("hoge4"));
    }

    /**
     * @throws Exception
     */
    public void testTrimPrefix() throws Exception {
        MyCopy copy = new MyCopy();
        assertEquals("aaa", copy.trimPrefix("aaa"));
        copy.prefix(BeanNames.search_());
        assertEquals("aaa", copy.trimPrefix("search_aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToBean() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = "ccc";
        src.eee = "1";
        DestBean dest = new DestBean();
        new MyCopy().copyBeanToBean(src, dest);
        assertNull(dest.bbb);
        assertEquals("ccc", dest.ccc);
        assertNull(dest.ddd);
        assertEquals(1, dest.eee);
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToBean_includes() throws Exception {
        MyBean src = new MyBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        MyBean dest = new MyBean();
        new MyCopy().includes(BeanNames.aaa()).copyBeanToBean(src, dest);
        assertEquals("aaa", dest.aaa);
        assertNull(dest.bbb);
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToBean_excludes() throws Exception {
        MyBean src = new MyBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        MyBean dest = new MyBean();
        new MyCopy().excludes(BeanNames.bbb()).copyBeanToBean(src, dest);
        assertEquals("aaa", dest.aaa);
        assertNull(dest.bbb);
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToBean_null() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = null;
        DestBean dest = new DestBean();
        dest.ccc = "ccc";
        new MyCopy().copyBeanToBean(src, dest);
        assertNull(dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToBean_excludesNull() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = null;
        DestBean dest = new DestBean();
        dest.ccc = "ccc";
        new MyCopy().excludesNull().copyBeanToBean(src, dest);
        assertEquals("ccc", dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToBean_whitespace() throws Exception {
        SrcBean src = new SrcBean();
        src.ccc = " ";
        DestBean dest = new DestBean();
        new MyCopy().copyBeanToBean(src, dest);
        assertEquals(" ", dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToBean_excludesWhitespace() throws Exception {
        SrcBean src = new SrcBean();
        src.ccc = " ";
        DestBean dest = new DestBean();
        dest.ccc = "ccc";
        new MyCopy().excludesWhitespace().copyBeanToBean(src, dest);
        assertEquals("ccc", dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToBean_prefix() throws Exception {
        SrcBean src = new SrcBean();
        src.search_eee$fff = "hoge";
        DestBean dest = new DestBean();
        new MyCopy().prefix(BeanNames.search_()).copyBeanToBean(src, dest);
        assertEquals("hoge", dest.eee$fff);
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToBean_converter() throws Exception {
        Bean bean = new Bean();
        bean.aaa = "1,000";
        Bean2 bean2 = new Bean2();
        new MyCopy().converter(new NumberConverter("#,##0")).copyBeanToBean(
                bean, bean2);
        assertEquals(new Integer(1000), bean2.aaa);
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToMap() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = "ccc";
        Map<String, Object> dest = new HashMap<String, Object>();
        new MyCopy().copyBeanToMap(src, dest);
        assertEquals("aaa", dest.get("aaa"));
        assertNull(dest.get("bbb"));
        assertEquals("ccc", dest.get("ccc"));
        assertNull(dest.get("ddd"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToMap_includes() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = "ccc";
        Map<String, Object> dest = new HashMap<String, Object>();
        new MyCopy().includes(BeanNames.aaa()).copyBeanToMap(src, dest);
        assertEquals("aaa", dest.get("aaa"));
        assertNull(dest.get("ccc"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToMap_excludes() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = "ccc";
        Map<String, Object> dest = new HashMap<String, Object>();
        new MyCopy().excludes(BeanNames.ccc()).copyBeanToMap(src, dest);
        assertEquals("aaa", dest.get("aaa"));
        assertNull(dest.get("ccc"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToMap_null() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = null;
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("ccc", "ccc");
        new MyCopy().copyBeanToMap(src, dest);
        assertNull(dest.get("ccc"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToMap_excludesNull() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = null;
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("ccc", "ccc");
        new MyCopy().excludesNull().copyBeanToMap(src, dest);
        assertEquals("ccc", dest.get("ccc"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToMap_whitespace() throws Exception {
        SrcBean src = new SrcBean();
        src.ccc = " ";
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("ccc", "ccc");
        new MyCopy().copyBeanToMap(src, dest);
        assertEquals(" ", dest.get("ccc"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToMap_excludesWhitespace() throws Exception {
        SrcBean src = new SrcBean();
        src.ccc = " ";
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("ccc", "ccc");
        new MyCopy().excludesWhitespace().copyBeanToMap(src, dest);
        assertEquals("ccc", dest.get("ccc"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToMap_prefix() throws Exception {
        SrcBean src = new SrcBean();
        src.search_eee$fff = "hoge";
        Map<String, Object> dest = new HashMap<String, Object>();
        new MyCopy().prefix(BeanNames.search_()).copyBeanToMap(src, dest);
        assertEquals("hoge", dest.get("eee.fff"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToMap_converter() throws Exception {
        Bean bean = new Bean();
        bean.aaa = "1,000";
        Map<String, Object> map = new HashMap<String, Object>();
        new MyCopy().converter(new NumberConverter("#,##0")).copyBeanToMap(
                bean, map);
        assertEquals("1,000", map.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToMap_converter2() throws Exception {
        Bean bean = new Bean();
        bean.aaa = "1,000";
        Map<String, Object> map = new HashMap<String, Object>();
        new MyCopy().converter(new NumberConverter("#,##0"), BeanNames.aaa())
                .copyBeanToMap(bean, map);
        assertEquals(new Long(1000), map.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopyBeanToMap_converter3() throws Exception {
        Bean2 bean2 = new Bean2();
        bean2.aaa = new Integer(1000);
        Map<String, Object> map = new HashMap<String, Object>();
        new MyCopy().converter(new NumberConverter("#,##0")).copyBeanToMap(
                bean2, map);
        assertEquals("1,000", map.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToBean_converter() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("aaa", new Integer(1000));
        Bean bean = new Bean();
        new MyCopy().converter(new NumberConverter("#,##0")).copyMapToBean(map,
                bean);
        assertEquals("1,000", bean.aaa);
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToBean_converter2() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("aaa", "1,000");
        Bean2 bean2 = new Bean2();
        new MyCopy().converter(new NumberConverter("#,##0")).copyMapToBean(map,
                bean2);
        assertEquals(new Integer(1000), bean2.aaa);
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToBean() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        src.put("ccc", "ccc");
        DestBean dest = new DestBean();
        new MyCopy().copyMapToBean(src, dest);
        assertEquals("bbb", dest.bbb);
        assertEquals("ccc", dest.ccc);
        assertNull(dest.ddd);
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToBean_includes() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        src.put("ccc", "ccc");
        DestBean dest = new DestBean();
        new MyCopy().includes(BeanNames.bbb()).copyMapToBean(src, dest);
        assertEquals("bbb", dest.bbb);
        assertNull(dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToBean_excludes() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        src.put("ccc", "ccc");
        DestBean dest = new DestBean();
        new MyCopy().excludes(BeanNames.ccc()).copyMapToBean(src, dest);
        assertEquals("bbb", dest.bbb);
        assertNull(dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToBean_null() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        src.put("ccc", null);
        DestBean dest = new DestBean();
        dest.ccc = "ccc";
        new MyCopy().copyMapToBean(src, dest);
        assertNull(dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToBean_excludesNull() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        src.put("ccc", null);
        DestBean dest = new DestBean();
        dest.ccc = "ccc";
        new MyCopy().excludesNull().copyMapToBean(src, dest);
        assertEquals("ccc", dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToBean_whitespace() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("ccc", " ");
        DestBean dest = new DestBean();
        dest.ccc = "ccc";
        new MyCopy().copyMapToBean(src, dest);
        assertEquals(" ", dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToBean_excludesWhitespace() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("ccc", " ");
        DestBean dest = new DestBean();
        dest.ccc = "ccc";
        new MyCopy().excludesWhitespace().copyMapToBean(src, dest);
        assertEquals("ccc", dest.ccc);
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToBean_prefix() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("search_eee.fff", "hoge");
        DestBean dest = new DestBean();
        new MyCopy().prefix(BeanNames.search_()).copyMapToBean(src, dest);
        assertEquals("hoge", dest.eee$fff);
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToMap() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", new Date(0));
        Map<String, Object> dest = new HashMap<String, Object>();
        new MyCopy().copyMapToMap(src, dest);
        assertEquals("aaa", dest.get("aaa"));
        assertEquals(new Date(0), dest.get("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToMap_includes() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        Map<String, Object> dest = new HashMap<String, Object>();
        new MyCopy().includes(BeanNames.aaa()).copyMapToMap(src, dest);
        assertEquals("aaa", dest.get("aaa"));
        assertNull(dest.get("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToMap_excludes() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        Map<String, Object> dest = new HashMap<String, Object>();
        new MyCopy().excludes(BeanNames.bbb()).copyMapToMap(src, dest);
        assertEquals("aaa", dest.get("aaa"));
        assertNull(dest.get("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToMap_null() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", null);
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("bbb", "bbb");
        new MyCopy().copyMapToMap(src, dest);
        assertNull(dest.get("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToMap_excludesNull() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", null);
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("bbb", "bbb");
        new MyCopy().excludesNull().copyMapToMap(src, dest);
        assertEquals("bbb", dest.get("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToMap_whitespace() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("bbb", " ");
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("bbb", "bbb");
        new MyCopy().copyMapToMap(src, dest);
        assertEquals(" ", dest.get("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToMap_excludesWhitespace() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("bbb", " ");
        Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("bbb", "bbb");
        new MyCopy().excludesWhitespace().copyMapToMap(src, dest);
        assertEquals("bbb", dest.get("bbb"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToMap_prefix() throws Exception {
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("search_eee.fff", "hoge");
        Map<String, Object> dest = new HashMap<String, Object>();
        new MyCopy().prefix(BeanNames.search_()).copyMapToMap(src, dest);
        assertEquals("hoge", dest.get("eee.fff"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToMap_converter() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("aaa", new Integer(1000));
        Map<String, Object> map2 = new HashMap<String, Object>();
        new MyCopy().converter(new NumberConverter("#,##0")).copyMapToMap(map,
                map2);
        assertEquals("1,000", map2.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToMap_converter2() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("aaa", "1,000");
        Map<String, Object> map2 = new HashMap<String, Object>();
        new MyCopy().converter(new NumberConverter("#,##0")).copyMapToMap(map,
                map2);
        assertEquals("1,000", map2.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testCopyMapToMap_converter3() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("aaa", "1,000");
        Map<String, Object> map2 = new HashMap<String, Object>();
        new MyCopy().converter(new NumberConverter("#,##0"), "aaa")
                .copyMapToMap(map, map2);
        assertEquals(new Long(1000), map2.get("aaa"));
    }

    /**
     * @throws Exception
     */
    public void testConvertValue_zeroConverter() throws Exception {
        assertEquals(new Integer(1), new MyCopy().convertValue(new Integer(1),
                "aaa", null));
    }

    /**
     * @throws Exception
     */
    public void testConvertValue_propertyConverter_asString() throws Exception {
        assertEquals("1", new MyCopy().converter(new NumberConverter("##0"),
                "aaa").convertValue(new Integer(1), "aaa", null));
    }

    /**
     * @throws Exception
     */
    public void testConvertValue_propertyConverter_asObject() throws Exception {
        assertEquals(new Long(1), new MyCopy().converter(
                new NumberConverter("##0"), BeanNames.aaa()).convertValue("1",
                "aaa", null));
    }

    /**
     * @throws Exception
     */
    public void testConvertValue_typeConverter_asString() throws Exception {
        assertEquals("1", new MyCopy().converter(new NumberConverter("##0"))
                .convertValue(new Integer(1), "aaa", null));
    }

    /**
     * @throws Exception
     */
    public void testConvertValue_typeConverter_asObject() throws Exception {
        assertEquals(new Long(1), new MyCopy().converter(
                new NumberConverter("##0")).convertValue("1", "aaa",
                Integer.class));
        assertEquals("19700101", new MyCopy().converter(
                new DateConverter("yyyyMMdd")).convertValue(new Timestamp(0),
                "aaa", String.class));
    }

    /**
     * @throws Exception
     */
    public void testConvertValue_throwable() throws Exception {
        try {
            new MyCopy().converter(new NumberConverter("##0")).convertValue(
                    "a", "aaa", Integer.class);
        } catch (ConverterRuntimeException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     */
    public void testConvertValue_dateToDate() throws Exception {
        Date date = new Date(1);
        assertEquals(date, new MyCopy().convertValue(date, "aaa", Date.class));
    }

    /**
     * @throws Exception
     */
    public void testDateConverter() throws Exception {
        assertEquals("19700101", new MyCopy().dateConverter("yyyyMMdd")
                .convertValue(new java.util.Date(0), "aaa", String.class));
    }

    /**
     * @throws Exception
     */
    public void testSqlDateConverter() throws Exception {
        assertEquals("19700101", new MyCopy().sqlDateConverter("yyyyMMdd")
                .convertValue(new java.sql.Date(0), "aaa", String.class));
    }

    /**
     * @throws Exception
     */
    public void testTimeConverter() throws Exception {
        assertEquals("00", new MyCopy().timeConverter("ss").convertValue(
                new java.sql.Time(0), "aaa", String.class));
    }

    /**
     * @throws Exception
     */
    public void testTimestampConverter() throws Exception {
        assertEquals("19700101 00", new MyCopy().timestampConverter(
                "yyyyMMdd ss").convertValue(new java.sql.Timestamp(0), "aaa",
                String.class));
    }

    /**
     * @throws Exception
     */
    public void testFindDefaultConverter() throws Exception {
        assertEquals(AbstractCopy.DEFAULT_TIME_CONVERTER, new MyCopy()
                .findDefaultConverter(Time.class));
        assertEquals(AbstractCopy.DEFAULT_TIMESTAMP_CONVERTER, new MyCopy()
                .findDefaultConverter(Timestamp.class));
        assertEquals(AbstractCopy.DEFAULT_TIMESTAMP_CONVERTER, new MyCopy()
                .findDefaultConverter(java.util.Date.class));
        assertEquals(AbstractCopy.DEFAULT_DATE_CONVERTER, new MyCopy()
                .findDefaultConverter(java.sql.Date.class));
        assertNull(new MyCopy().findDefaultConverter(Integer.class));
    }

    /**
     * 
     */
    private static class MyCopy extends AbstractCopy<MyCopy> {

    }

    /**
     * 
     */
    @SuppressWarnings("unused")
    public static class SrcBean {

        private String aaa;

        private String bbb;

        private String ccc;

        /**
         * 
         */
        public String eee;

        /**
         * 
         */
        public String search_eee$fff;

        /**
         * @return
         */
        public String getAaa() {
            return aaa;
        }

        /**
         * @param aaa
         */
        public void setAaa(String aaa) {
            this.aaa = aaa;
        }

        /**
         * @param bbb
         */
        public void setBbb(String bbb) {
            this.bbb = bbb;
        }

        /**
         * @return
         */
        public String getCcc() {
            return ccc;
        }
    }

    /**
     * 
     */
    @SuppressWarnings("unused")
    public static class DestBean {

        private String bbb;

        private String ccc;

        private String ddd;

        /**
         * 
         */
        public int eee;

        /**
         * 
         */
        public String eee$fff;

        /**
         * @param bbb
         */
        public void setBbb(String bbb) {
            this.bbb = bbb;
        }

        /**
         * @param ccc
         */
        public void setCcc(String ccc) {
            this.ccc = ccc;
        }

        /**
         * @return
         */
        public String getDdd() {
            return ddd;
        }

        /**
         * @param ddd
         */
        public void setDdd(String ddd) {
            this.ddd = ddd;
        }
    }

    /**
     * 
     */
    public static class Bean {

        /**
         * 
         */
        public String aaa;
    }

    /**
     * 
     */
    public static class Bean2 {

        /**
         * 
         */
        public Integer aaa;
    }

    /**
     * 
     */
    public static class Bean3 {

        /**
         * 
         */
        public Date aaa;
    }

    /**
     * @author kato
     * 
     */
    public static class BeanNames {

        /**
         * CharSequenceを作成します。
         * 
         * @param name
         * @return
         */
        protected static CharSequence createCharSequence(final String name) {
            return new CharSequence() {

                @Override
                public String toString() {
                    return name;
                }

                public char charAt(int index) {
                    return name.charAt(index);
                }

                public int length() {
                    return name.length();
                }

                public CharSequence subSequence(int start, int end) {
                    return name.subSequence(start, end);
                }

            };
        }

        /**
         * @return
         */
        public static CharSequence aaa() {
            return createCharSequence("aaa");
        }

        /**
         * @return
         */
        public static CharSequence bbb() {
            return createCharSequence("bbb");
        }

        /**
         * @return
         */
        public static CharSequence ccc() {
            return createCharSequence("ccc");
        }

        /**
         * @return
         */
        public static CharSequence hoge() {
            return createCharSequence("hoge");
        }

        /**
         * @return
         */
        public static CharSequence hoge2() {
            return createCharSequence("hoge2");
        }

        /**
         * @return
         */
        public static CharSequence hoge3() {
            return createCharSequence("hoge3");
        }

        /**
         * @return
         */
        public static CharSequence search_aaa() {
            return createCharSequence("search_aaa");
        }

        /**
         * @return
         */
        public static CharSequence abc_exclude() {
            return createCharSequence("abc_exclude");
        }

        /**
         * @return
         */
        public static CharSequence search_() {
            return createCharSequence("search_");
        }

        /**
         * @return
         */
        public static CharSequence abc_() {
            return createCharSequence("abc_");
        }

    }

}
