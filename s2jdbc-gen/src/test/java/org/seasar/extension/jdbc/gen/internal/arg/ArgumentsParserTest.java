/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.internal.arg;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class ArgumentsParserTest {

    /**
     * 
     */
    @Test
    public void testParse() {
        MyBean bean = new MyBean();
        ArgumentsParser parser = new ArgumentsParser(bean);
        parser
                .parse(new String[] { "string='aaa'", "integer=1", "bool=true",
                        "ch=\\u0097", "myEnum=AAA", "list=['aaa','bbb']",
                        "file='aaa'" });
        assertEquals("aaa", bean.getString());
        assertEquals(new Integer(1), bean.getInteger());
        assertTrue(bean.isBool());
        assertEquals('a', bean.getCh());
        assertEquals(MyEnum.AAA, bean.getMyEnum());
        assertEquals(Arrays.asList("aaa", "bbb"), bean.getList());
        assertEquals(new File("aaa"), bean.getFile());
    }

    /**
     * 
     */
    @Test
    public void testParse_nullOrDefaultValue() {
        MyBean bean = new MyBean();
        ArgumentsParser parser = new ArgumentsParser(bean);
        parser.parse(new String[] { "string=", "integer=", "bool=",
                "ch=\\u0000", "myEnum=", "list=", "file=" });
        assertNull(bean.getString());
        assertNull(bean.getInteger());
        assertFalse(bean.isBool());
        assertEquals('\u0000', bean.getCh());
        assertNull(bean.getMyEnum());
        assertNull(bean.getList());
        assertNull(bean.getFile());
    }

    /**
     * 
     * @author taedium
     */
    public static class MyBean {

        private String string;

        private Integer integer;

        private boolean bool;

        private char ch;

        private MyEnum myEnum;

        private List<String> list;

        private File file;

        private Object object;

        /**
         * @return Returns the string.
         */
        public String getString() {
            return string;
        }

        /**
         * @param string
         *            The string to set.
         */
        public void setString(String string) {
            this.string = string;
        }

        /**
         * @return Returns the integer.
         */
        public Integer getInteger() {
            return integer;
        }

        /**
         * @param integer
         *            The integer to set.
         */
        public void setInteger(Integer integer) {
            this.integer = integer;
        }

        /**
         * @return Returns the bool.
         */
        public boolean isBool() {
            return bool;
        }

        /**
         * @param bool
         *            The bool to set.
         */
        public void setBool(boolean bool) {
            this.bool = bool;
        }

        /**
         * @return Returns the primitiveChar.
         */
        public char getCh() {
            return ch;
        }

        /**
         * @param primitiveChar
         *            The primitiveChar to set.
         */
        public void setCh(char primitiveChar) {
            this.ch = primitiveChar;
        }

        /**
         * @return Returns the myEnum.
         */
        public MyEnum getMyEnum() {
            return myEnum;
        }

        /**
         * @param myEnum
         *            The myEnum to set.
         */
        public void setMyEnum(MyEnum myEnum) {
            this.myEnum = myEnum;
        }

        /**
         * @return Returns the list.
         */
        public List<String> getList() {
            return list;
        }

        /**
         * @param list
         *            The list to set.
         */
        public void setList(List<String> list) {
            this.list = list;
        }

        /**
         * @return Returns the file.
         */
        public File getFile() {
            return file;
        }

        /**
         * @param file
         *            The file to set.
         */
        public void setFile(File file) {
            this.file = file;
        }

        /**
         * @return Returns the object.
         */
        public Object getObject() {
            return object;
        }

        /**
         * @param object
         *            The object to set.
         */
        public void setObject(Object object) {
            this.object = object;
        }

    }

    /** */
    public static enum MyEnum {
        /** */
        AAA,
        /** */
        BBB,
        /** */
        CCC;
    }
}
