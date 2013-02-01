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
package org.seasar.extension.jdbc.name;

import java.util.Map;

import org.seasar.framework.util.tiger.CollectionsUtil;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class PropertyNameTest extends TestCase {

    /**
     * 
     * @throws Exception
     */
    public void testSimple() throws Exception {
        assertEquals("aaa1", new Aaa().aaa1().toString());
    }

    /**
     * 
     * @throws Exception
     */
    public void testRelationship() throws Exception {
        assertEquals("bbb", new Aaa().bbb().toString());
    }

    /**
     * 
     * @throws Exception
     */
    public void testRelationshipProperty() throws Exception {
        assertEquals("bbb.bbb1", new Aaa().bbb().bbb1().toString());
        assertEquals("ccc.ccc1", new Bbb().ccc().ccc1().toString());
    }

    /**
     * 
     * @throws Exception
     */
    public void testNestRelationship() throws Exception {
        assertEquals("bbb.ccc", new Aaa().bbb().ccc().toString());
        assertEquals("bbb.aaa.bbb", new Aaa().bbb().aaa().bbb().toString());
    }

    /**
     * 
     * @throws Exception
     */
    public void testNestRelationshipProperty() throws Exception {
        assertEquals("bbb.ccc.ccc1", new Aaa().bbb().ccc().ccc1().toString());
    }

    /**
     * @throws Exception
     */
    public void testEquals() throws Exception {
        assertTrue(new Aaa().aaa1().equals(new Aaa().aaa1()));
        assertTrue(new Aaa().aaa1().equals("aaa1"));
    }

    /**
     * @throws Exception
     */
    public void testHashcode() throws Exception {
        Map<CharSequence, String> map = CollectionsUtil.newHashMap();
        map.put(new Aaa().aaa1(), "hoge");
        assertEquals("hoge", map.get(new Aaa().aaa1()));
    }

    /**
     * 
     */
    public static class Aaa extends PropertyName<Void> {

        /**
         * 
         */
        public Aaa() {
        }

        /**
         * @param name
         */
        public Aaa(String name) {
            super(name);
        }

        /**
         * @param parent
         * @param name
         */
        public Aaa(PropertyName<?> parent, String name) {
            super(parent, name);
        }

        /**
         * @return
         */
        public PropertyName<Integer> aaa1() {
            return new PropertyName<Integer>(this, "aaa1");
        }

        /**
         * @return
         */
        public Bbb bbb() {
            return new Bbb(this, "bbb");
        }
    }

    /**
     * 
     */
    public static class Bbb extends PropertyName<Void> {

        /**
         * 
         */
        public Bbb() {
        }

        /**
         * @param name
         */
        public Bbb(String name) {
            super(name);
        }

        /**
         * @param parent
         * @param name
         */
        public Bbb(PropertyName<?> parent, String name) {
            super(parent, name);
        }

        /**
         * @return
         */
        public PropertyName<Integer> bbb1() {
            return new PropertyName<Integer>(this, "bbb1");
        }

        /**
         * @return
         */
        public Ccc ccc() {
            return new Ccc(this, "ccc");
        }

        /**
         * @return
         */
        public Aaa aaa() {
            return new Aaa(this, "aaa");
        }
    }

    /**
     * 
     */
    public static class Ccc extends PropertyName<Void> {

        /**
         * 
         */
        public Ccc() {
        }

        /**
         * @param name
         */
        public Ccc(String name) {
            super(name);
        }

        /**
         * @param parent
         * @param name
         */
        public Ccc(PropertyName<?> parent, String name) {
            super(parent, name);
        }

        /**
         * @return
         */
        public PropertyName<String> ccc1() {
            return new PropertyName<String>(this, "ccc1");
        }
    }
}
