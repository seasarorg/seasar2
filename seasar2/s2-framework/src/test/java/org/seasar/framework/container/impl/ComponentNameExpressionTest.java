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
package org.seasar.framework.container.impl;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.Expression;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.ognl.OgnlExpression;

/**
 * @author koichik
 * 
 */
public class ComponentNameExpressionTest extends TestCase {

    /**
     * 
     */
    public static final int COUNT = 1000000;

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDef cd = new ComponentDefImpl(Object.class, "foo");
        container.register(cd);
        Expression expr = new ComponentNameExpression("foo");
        assertNotNull(expr.evaluate(container, null));
    }

    /**
     * @throws Exception
     */
    public void testPerf() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDef cd = new ComponentDefImpl(Object.class, "foo");
        container.register(cd);
        Expression expr = new ComponentNameExpression("foo");
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < COUNT; ++i) {
            expr.evaluate(container, null);
        }
        long t2 = System.currentTimeMillis();
        System.out.println("name : " + (t2 - t1) + "ms");

        expr = new OgnlExpression("foo");
        t1 = System.currentTimeMillis();
        for (int i = 0; i < COUNT; ++i) {
            expr.evaluate(container, null);
        }
        t2 = System.currentTimeMillis();
        System.out.println("ognl : " + (t2 - t1) + "ms");
    }

}
