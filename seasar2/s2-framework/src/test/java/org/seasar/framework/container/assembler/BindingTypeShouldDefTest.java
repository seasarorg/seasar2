/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.assembler;

import junit.framework.TestCase;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;

/**
 * @author higa
 * 
 */
public class BindingTypeShouldDefTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testBindWarning() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(A.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("hoge");
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A.class);
        PropertyDef propDef = new PropertyDefImpl("hoge");
        cd.addPropertyDef(propDef);
        container.register(cd);
        A a = new A();
        BindingTypeDefFactory.SHOULD.bind(cd, propDef, propDesc, a);
    }

    /**
     * 
     */
    public interface Foo {
        /**
         * @return
         */
        public String getHogeName();
    }

    /**
     * 
     */
    public static class A implements Foo {

        private Hoge hoge_;

        private String message_;

        /**
         * 
         */
        public A() {
        }

        /**
         * @return
         */
        public Hoge getHoge() {
            return hoge_;
        }

        /**
         * @param hoge
         */
        public void setHoge(Hoge hoge) {
            hoge_ = hoge;
        }

        /**
         * @return
         */
        public String getMessage() {
            return message_;
        }

        /**
         * @param message
         */
        public void setMessage(String message) {
            message_ = message;
        }

        public String getHogeName() {
            return hoge_.getName();
        }
    }

    /**
     * 
     */
    public static class A2 implements Foo {

        private Hoge hoge_ = new B();

        /**
         * @return
         */
        public Hoge getHoge() {
            return hoge_;
        }

        /**
         * @param hoge
         */
        public void setHoge(Hoge hoge) {
            hoge_ = hoge;
        }

        public String getHogeName() {
            return hoge_.getName();
        }
    }

    /**
     * 
     */
    public interface Hoge {

        /**
         * @return
         */
        public String getName();
    }

    /**
     * 
     */
    public static class B implements Hoge {

        public String getName() {
            return "B";
        }
    }
}