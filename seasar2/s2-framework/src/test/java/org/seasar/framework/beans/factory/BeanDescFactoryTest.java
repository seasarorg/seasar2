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
package org.seasar.framework.beans.factory;

import junit.framework.TestCase;

import org.seasar.framework.beans.BeanDesc;

/**
 * @author higa
 */
public class BeanDescFactoryTest extends TestCase {

    public void testGetBeanDesc() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(MyBean.class);
        assertSame("1", beanDesc, BeanDescFactory.getBeanDesc(MyBean.class));
    }

    public void testClear() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(MyBean.class);
        BeanDescFactory.clear();
        assertNotSame("1", beanDesc, BeanDescFactory.getBeanDesc(MyBean.class));
    }

    public static class MyBean {

        public String getAaa() {
            return null;
        }

        public String getBbb(Object a) {
            return null;
        }

        public boolean isCCC() {
            return true;
        }

        public Object isDdd() {
            return null;
        }

        public String getEee() {
            return null;
        }

        public void setEee(String eee) {
        }
    }

}
