/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
        assertEquals("aaa1", new Aaa().aaa1().toPropertyName());
    }

    /**
     * 
     * @throws Exception
     */
    public void testRelationship() throws Exception {
        assertEquals("bbb", new Aaa().bbb().toPropertyName());
    }

    /**
     * 
     * @throws Exception
     */
    public void testRelationshipProperty() throws Exception {
        assertEquals("bbb.bbb1", new Aaa().bbb().bbb1().toPropertyName());
        assertEquals("ccc.ccc1", new Bbb().ccc().ccc1().toPropertyName());
    }

    /**
     * 
     * @throws Exception
     */
    public void testNestRelationship() throws Exception {
        assertEquals("bbb.ccc", new Aaa().bbb().ccc().toPropertyName());
        assertEquals("bbb.aaa.bbb", new Aaa().bbb().aaa().bbb()
                .toPropertyName());
    }

    /**
     * 
     * @throws Exception
     */
    public void testNestRelationshipProperty() throws Exception {
        assertEquals("bbb.ccc.ccc1", new Aaa().bbb().ccc().ccc1()
                .toPropertyName());
    }

    /**
     * 
     */
    public static class Aaa extends PropertyName {

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
        public Aaa(PropertyName parent, String name) {
            super(parent, name);
        }

        /**
         * @return
         */
        public PropertyName aaa1() {
            return new PropertyName(this, "aaa1");
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
    public static class Bbb extends PropertyName {

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
        public Bbb(PropertyName parent, String name) {
            super(parent, name);
        }

        /**
         * @return
         */
        public PropertyName bbb1() {
            return new PropertyName(this, "bbb1");
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
    public static class Ccc extends PropertyName {

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
        public Ccc(PropertyName parent, String name) {
            super(parent, name);
        }

        /**
         * @return
         */
        public PropertyName ccc1() {
            return new PropertyName(this, "ccc1");
        }
    }
}
