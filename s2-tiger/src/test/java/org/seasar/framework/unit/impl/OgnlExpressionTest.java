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
package org.seasar.framework.unit.impl;

import java.util.Map;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * @author nakamura
 * 
 */
public class OgnlExpressionTest extends S2TestCase {

    /**
     * @throws Exception
     */
    public void testContext() throws Exception {
        Map<String, Object> context = CollectionsUtil.newHashMap();
        context.put("method", ReflectionUtil.getMethod(this.getClass(),
                getName()));
        OgnlExpression expression = new OgnlExpression("#method.name", this,
                context);
        assertEquals("testContext", expression.evaluate());
    }

    /**
     * @throws Exception
     */
    public void testEvaluate() throws Exception {
        Map<String, Object> context = CollectionsUtil.newHashMap();
        context.put("method", ReflectionUtil.getMethod(this.getClass(),
                getName()));
        OgnlExpression expression = new OgnlExpression("hoge()", this, context);
        expression.throwExceptionIfNecessary();
        assertEquals("hoge", expression.evaluate());
    }

    /**
     * @throws Exception
     */
    public void testEvaluateNoException() throws Exception {
        Map<String, Object> context = CollectionsUtil.newHashMap();
        context.put("method", ReflectionUtil.getMethod(this.getClass(),
                getName()));
        OgnlExpression expression = new OgnlExpression("absentMethod()", this,
                context);
        expression.evaluateNoException();
        try {
            expression.throwExceptionIfNecessary();
            fail();
        } catch (Exception e) {
        }
    }

    /**
     * @throws Exception
     */
    public void testIsMethodFailed() throws Exception {
        Map<String, Object> context = CollectionsUtil.newHashMap();
        context.put("method", ReflectionUtil.getMethod(this.getClass(),
                getName()));
        OgnlExpression expression = new OgnlExpression("foo()", this, context);
        expression.evaluateNoException();
        assertTrue(expression.isMethodFailed());
    }

    /**
     * @return
     */
    public String hoge() {
        return "hoge";
    }

    /**
     * @return
     */
    public String foo() {
        throw new IllegalStateException();
    }

}
