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
package org.seasar.extension.jdbc.gen.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class BeanUtilTest {

    /**
     * 
     */
    @Test
    public void testCopy() {
        Src src = new Src();
        src.setAaa("hoge");
        src.setCcc("foo");
        Dest dest = new Dest();
        BeanUtil.copy(src, dest);
        assertEquals("hoge", dest.getAaa());
        assertNull(dest.getBbb());
        assertEquals("foo", dest.getCcc());
    }

    /**
     * 
     * 
     */
    public static class Src {

        private String aaa;

        private String bbb;

        private String ccc;

        /**
         * 
         * @return
         */
        public String getAaa() {
            return aaa;
        }

        /**
         * 
         * @param aaa
         */
        public void setAaa(String aaa) {
            this.aaa = aaa;
        }

        /**
         * 
         * @return
         */
        public String getBbb() {
            return bbb;
        }

        /**
         * 
         * @param bbb
         */
        public void setBbb(String bbb) {
            this.bbb = bbb;
        }

        /**
         * 
         * @return
         */
        public String getCcc() {
            return ccc;
        }

        /**
         * 
         * @param ccc
         */
        public void setCcc(String ccc) {
            this.ccc = ccc;
        }

    }

    /**
     * 
     * @author taedium
     * 
     */
    public static class Dest {

        private String aaa;

        private String bbb;

        private String ccc;

        /**
         * 
         * @return
         */
        public String getAaa() {
            return aaa;
        }

        /**
         * 
         * @param aaa
         */
        public void setAaa(String aaa) {
            this.aaa = aaa;
        }

        /**
         * 
         * @return
         */
        public String getBbb() {
            return bbb;
        }

        /**
         * 
         * @param bbb
         */
        public void setBbb(String bbb) {
            this.bbb = bbb;
        }

        /**
         * 
         * @return
         */
        public String getCcc() {
            return ccc;
        }

        /**
         * 
         * @param ccc
         */
        public void setCcc(String ccc) {
            this.ccc = ccc;
        }
    }

}
