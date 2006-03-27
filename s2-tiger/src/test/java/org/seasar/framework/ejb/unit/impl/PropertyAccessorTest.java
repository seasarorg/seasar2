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

import junit.framework.TestCase;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;

/**
 * @author taedium
 * 
 */
public class PropertyAccessorTest extends TestCase {

    public void testGetValue() throws Exception {
        Hoge2 hoge2 = new Hoge2();
        hoge2.setBbb(10);
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(hoge2.getClass());
        PropertyDesc propDesc = beanDesc.getPropertyDesc("bbb");
        PropertyAccessor accessor = new PropertyAccessor(propDesc, propDesc
                .getReadMethod());
        assertEquals("1", 10, accessor.getValue(hoge2));
    }

    public void testSetValue() {
        Hoge2 hoge2 = new Hoge2();
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(hoge2.getClass());
        PropertyDesc propDesc = beanDesc.getPropertyDesc("bbb");
        PropertyAccessor accessor = new PropertyAccessor(propDesc, propDesc
                .getReadMethod());
        accessor.setValue(hoge2, 10);
        assertEquals("1", new Integer(10), hoge2.getBbb());
    }

    public void testSetValue2() {
        Hoge4 hoge4 = new Hoge4();
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(hoge4.getClass());
        PropertyDesc propDesc = beanDesc.getPropertyDesc("bbb");
        PropertyAccessor accessor = new PropertyAccessor(propDesc, propDesc
                .getReadMethod());
        try {
            accessor.setValue(hoge4, 10);
            fail();
        } catch (Exception e) {
        }
    }

}
