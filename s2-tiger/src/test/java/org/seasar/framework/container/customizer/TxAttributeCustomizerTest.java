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
package org.seasar.framework.container.customizer;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.seasar.extension.tx.MandatoryInterceptor;
import org.seasar.extension.tx.RequiredInterceptor;
import org.seasar.extension.tx.RequiresNewInterceptor;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.ComponentDef;

/**
 * @author koichik
 */
public class TxAttributeCustomizerTest extends S2TestCase {

    /**
     * @throws Exception
     */
    @Override
    public void setUp() throws Exception {
        include("TxAttributeCustomizerTest.dicon");
    }

    /**
     */
    public void testCustomize1() {
        ComponentDef cd = getComponentDef(Comp1.class);
        assertEquals(1, cd.getAspectDefSize());

        AspectDef ad = cd.getAspectDef(0);
        assertEquals(RequiredInterceptor.class, ad.getAspect()
                .getMethodInterceptor().getClass());
    }

    /**
     * @throws Exception
     */
    public void testCustomize2() throws Exception {
        ComponentDef cd = getComponentDef(Comp2.class);
        assertEquals(2, cd.getAspectDefSize());

        AspectDef ad = cd.getAspectDef(0);
        assertEquals(RequiredInterceptor.class, ad.getAspect()
                .getMethodInterceptor().getClass());
        assertTrue(ad.getPointcut().isApplied(
                Comp2.class.getMethod("foo", null)));

        ad = cd.getAspectDef(1);
        assertEquals(RequiresNewInterceptor.class, ad.getAspect()
                .getMethodInterceptor().getClass());
        assertTrue(ad.getPointcut().isApplied(
                Comp2.class.getMethod("bar", null)));
    }

    /**
     */
    public void testCustomize3() {
        ComponentDef cd = getComponentDef(Comp3.class);
        assertEquals(1, cd.getAspectDefSize());

        AspectDef ad = cd.getAspectDef(0);
        assertEquals(MandatoryInterceptor.class, ad.getAspect()
                .getMethodInterceptor().getClass());
    }

    /**
     * @throws Exception
     */
    public void testCustomize4() throws Exception {
        ComponentDef cd = getComponentDef(Comp4.class);
        assertEquals(2, cd.getAspectDefSize());

        AspectDef ad = cd.getAspectDef(0);
        assertEquals(MandatoryInterceptor.class, ad.getAspect()
                .getMethodInterceptor().getClass());
        assertTrue(ad.getPointcut().isApplied(
                Comp4.class.getMethod("foo", null)));

        ad = cd.getAspectDef(1);
        assertEquals(RequiresNewInterceptor.class, ad.getAspect()
                .getMethodInterceptor().getClass());
        assertTrue(ad.getPointcut().isApplied(
                Comp4.class.getMethod("bar", null)));
    }

    /**
     * @author koichik
     */
    public static class Comp1 {

        /**
         * 
         */
        public void foo() {
        }

    }

    /**
     * @author koichik
     */
    public static class Comp2 {

        /**
         * 
         */
        public void foo() {
        }

        /**
         * 
         */
        @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
        public void bar() {
        }

    }

    /**
     * @author koichik
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public static class Comp3 {

        /**
         * 
         */
        public void foo() {
        }

    }

    /**
     * @author koichik
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public static class Comp4 {

        /**
         * 
         */
        public void foo() {
        }

        /**
         * 
         */
        @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
        public void bar() {
        }

    }

}
