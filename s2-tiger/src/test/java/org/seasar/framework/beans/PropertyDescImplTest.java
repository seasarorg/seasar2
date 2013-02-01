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
package org.seasar.framework.beans;

import junit.framework.TestCase;

import org.seasar.framework.beans.factory.BeanDescFactory;

/**
 * @author higa
 * 
 */
public class PropertyDescImplTest extends TestCase {

    /**
     * 
     */
    public void testConvertWithString() {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(MyDto.class);
        PropertyDesc pd = beanDesc.getPropertyDesc("myEnum");
        MyDto dto = new MyDto();
        pd.setValue(dto, "ONE");
        assertEquals(MyEnum.ONE, dto.myEnum);

    }

    /**
     *
     */
    public enum MyEnum {
        /**
         * 
         */
        ONE,
        /**
         * 
         */
        TWO
    }

    private class MyDto {

        /**
         * 
         */
        public MyEnum myEnum;
    }
}
