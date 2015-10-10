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
public class ArgumentsBuilderTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testBuild() throws Exception {
        MyBean bean = new MyBean();
        bean.setString("aaa");
        bean.setInteger(1);
        bean.setBool(true);
        bean.setCh('a');
        bean.setMyEnum(MyEnum.AAA);
        bean.setList(Arrays.asList("aaa", "bbb"));
        bean.setFile(new File("aaa"));

        ArgumentsBuilder builder = new ArgumentsBuilder(bean);
        List<String> args = builder.build();
        assertEquals(7, args.size());
        assertTrue(args.contains("string='aaa'"));
        assertTrue(args.contains("integer=1"));
        assertTrue(args.contains("bool=true"));
        assertTrue(args.contains("ch=\\u0097"));
        assertTrue(args.contains("myEnum=AAA"));
        assertTrue(args.contains("list=['aaa','bbb']"));
        assertTrue(args.contains("file='" + new File("aaa").getCanonicalPath()
                + "'"));
    }

    /**
     * 
     */
    @Test
    public void testBuild_nullOrDefaultValue() {
        MyBean bean = new MyBean();
        ArgumentsBuilder builder = new ArgumentsBuilder(bean);
        List<String> args = builder.build();
        assertEquals(7, args.size());
        assertTrue(args.contains("string="));
        assertTrue(args.contains("integer="));
        assertTrue(args.contains("bool=false"));
        assertTrue(args.contains("ch=\\u0000"));
        assertTrue(args.contains("myEnum="));
        assertTrue(args.contains("list="));
        assertTrue(args.contains("file="));
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
