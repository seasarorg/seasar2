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
package org.seasar.extension.jdbc.gen.internal.argtype;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class ArgumentTypeRegistryTest {

    private BeanDesc beanDesc;

    /**
     * 
     */
    @Before
    public void setUp() {
        beanDesc = BeanDescFactory.getBeanDesc(MyBean.class);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetArgumentType_String() throws Exception {
        ArgumentType<String> argumentType = ArgumentTypeRegistry
                .getArgumentType(beanDesc.getPropertyDesc("string"));
        assertNotNull(argumentType);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetArgumentType_Integer() throws Exception {
        ArgumentType<String> argumentType = ArgumentTypeRegistry
                .getArgumentType(beanDesc.getPropertyDesc("integer"));
        assertNotNull(argumentType);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetArgumentType_MyEnum() throws Exception {
        ArgumentType<String> argumentType = ArgumentTypeRegistry
                .getArgumentType(beanDesc.getPropertyDesc("myEnum"));
        assertNotNull(argumentType);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetArgumentType_List() throws Exception {
        ArgumentType<List<File>> argumentType = ArgumentTypeRegistry
                .getArgumentType(beanDesc.getPropertyDesc("list"));
        assertNotNull(argumentType);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testRegister() throws Exception {
        ArgumentTypeRegistry.register(MyArgumentType.class);
        try {
            ArgumentType<String> argumentType = ArgumentTypeRegistry
                    .getArgumentType(MyArgumentType.class);
            assertNotNull(argumentType);
        } finally {
            ArgumentTypeRegistry.deregister(MyArgumentType.class);
        }
    }

    /**
     * 
     * @author taedium
     * 
     */
    public static class MyBean {

        private String string;

        private Integer integer;

        private MyEnum myEnum;

        private List<File> list;

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
        public List<File> getList() {
            return list;
        }

        /**
         * @param list
         *            The list to set.
         */
        public void setList(List<File> list) {
            this.list = list;
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

    /**
     * 
     * @author taedium
     * 
     */
    public static class MyArgumentType implements ArgumentType<String> {

        public String toObject(String value) {
            return null;
        }

        public String toText(String value) {
            return null;
        }
    }

}
