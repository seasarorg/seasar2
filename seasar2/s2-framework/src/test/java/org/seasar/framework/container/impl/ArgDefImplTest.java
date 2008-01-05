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
package org.seasar.framework.container.impl;

import junit.framework.TestCase;

import org.seasar.framework.container.ArgDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.ognl.OgnlExpression;

/**
 * @author higa
 * 
 */
public class ArgDefImplTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testSetExpression() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A.class);
        ArgDef ad = new ArgDefImpl();
        ad.setExpression(new OgnlExpression("hoge"));
        cd.addArgDef(ad);
        container.register(cd);
        ComponentDefImpl cd2 = new ComponentDefImpl(B.class, "hoge");
        container.register(cd2);
        container.register(C.class);
        A a = (A) container.getComponent(A.class);
        assertEquals("1", "B", a.getHogeName());
    }

    /**
     * @throws Exception
     */
    public void testSetChildComponentDef() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A.class);
        ArgDef ad = new ArgDefImpl();
        ComponentDefImpl cd2 = new ComponentDefImpl(B.class);
        ad.setChildComponentDef(cd2);
        cd.addArgDef(ad);
        container.register(cd);
        container.register(C.class);
        A a = (A) container.getComponent(A.class);
        assertEquals("1", "B", a.getHogeName());
    }

    /**
     * @throws Exception
     */
    public void testPrototype() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(StrHolderImpl.class, "foo");
        cd.setInstanceDef(InstanceDefFactory.PROTOTYPE);
        ComponentDefImpl cd2 = new ComponentDefImpl(StrFacadeImpl.class);
        cd2.setInstanceDef(InstanceDefFactory.PROTOTYPE);
        ArgDef ad = new ArgDefImpl();
        ad.setExpression(new OgnlExpression("foo"));
        cd2.addArgDef(ad);
        container.register(cd);
        container.register(cd2);
        StrFacade facade1 = (StrFacade) container.getComponent(StrFacade.class);
        StrFacade facade2 = (StrFacade) container.getComponent(StrFacade.class);
        facade1.setStr("aaa");
        facade2.setStr("bbb");
        assertEquals("1", "aaa", facade1.getStr());
        assertEquals("2", "bbb", facade2.getStr());
    }

    /**
     *
     */
    public static class A {

        private Hoge hoge_;

        /**
         * @param hoge
         */
        public A(Hoge hoge) {
            hoge_ = hoge;
        }

        /**
         * @return
         */
        public String getHogeName() {
            return hoge_.getName();
        }
    }

    /**
     *
     */
    public static class A2 {

        private Hoge hoge_;

        /**
         * @param hoge
         */
        public void setHoge(Hoge hoge) {
            hoge_ = hoge;
        }

        /**
         * @return
         */
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

    /**
     *
     */
    public static class C implements Hoge {

        private A2 a2_;

        /**
         * @param a2
         */
        public void setA2(A2 a2) {
            a2_ = a2;
        }

        public String getName() {
            return "C";
        }

        /**
         * @return
         */
        public String getHogeName() {
            return a2_.getHogeName();
        }
    }

    /**
     *
     */
    public static interface StrHolder {
        /**
         * @param str
         */
        public void setStr(String str);

        /**
         * @return
         */
        public String getStr();
    }

    /**
     *
     */
    public static class StrHolderImpl implements StrHolder {
        private String str_;

        public void setStr(String str) {
            str_ = str;
        }

        public String getStr() {
            return str_;
        }
    }

    /**
     *
     */
    public static interface StrFacade {
        /**
         * @param str
         */
        public void setStr(String str);

        /**
         * @return
         */
        public String getStr();
    }

    /**
     *
     */
    public static class StrFacadeImpl implements StrFacade {
        private StrHolder strHolder_;

        /**
         * @param strHolder
         */
        public StrFacadeImpl(StrHolder strHolder) {
            strHolder_ = strHolder;
        }

        public void setStr(String str) {
            strHolder_.setStr(str);
        }

        public String getStr() {
            return strHolder_.getStr();
        }
    }
}