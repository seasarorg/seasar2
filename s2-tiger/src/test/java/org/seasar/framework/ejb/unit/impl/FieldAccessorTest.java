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
package org.seasar.framework.ejb.unit.impl;

import java.lang.reflect.Field;

import junit.framework.TestCase;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;

public class FieldAccessorTest extends TestCase {

    public void testGetValue() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setBbb(10);
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(hoge.getClass());
        Field field = beanDesc.getField("bbb");
        FieldAccessor accessor = new FieldAccessor(field);
        assertEquals("1", 10, accessor.getValue(hoge));
    }

    public void testSetValue() {
        Hoge hoge = new Hoge();
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(hoge.getClass());
        Field field = beanDesc.getField("bbb");
        FieldAccessor accessor = new FieldAccessor(field);
        accessor.setValue(hoge, 10);
        assertEquals("1", new Integer(10), hoge.getBbb());
    }

}
