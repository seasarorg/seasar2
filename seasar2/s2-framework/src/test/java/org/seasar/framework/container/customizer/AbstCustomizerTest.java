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

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.impl.ComponentDefImpl;

/**
 * @author koichik
 * 
 */
public class AbstCustomizerTest extends TestCase {

    private boolean matched;

    /**
     * @throws Exception
     */
    public void testNoPattern() throws Exception {
        TestCustomizer customizer = new TestCustomizer();
        matched = false;
        customizer.customize(new ComponentDefImpl(Foo.class));
        assertTrue(matched);
        matched = false;
        customizer.customize(new ComponentDefImpl(Bar.class));
        assertTrue(matched);
        matched = false;
        customizer.customize(new ComponentDefImpl(Baz.class));
        assertTrue(matched);
    }

    /**
     * @throws Exception
     */
    public void testApplyOnly() throws Exception {
        TestCustomizer customizer = new TestCustomizer();
        customizer.addClassPattern("org.seasar.framework.container.customizer",
                "AbstCustomizerTest\\$B.*");
        matched = false;
        customizer.customize(new ComponentDefImpl(Foo.class));
        assertFalse(matched);
        matched = false;
        customizer.customize(new ComponentDefImpl(Bar.class));
        assertTrue(matched);
        matched = false;
        customizer.customize(new ComponentDefImpl(Baz.class));
        assertTrue(matched);
    }

    /**
     * @throws Exception
     */
    public void testIgnoreOnly() throws Exception {
        TestCustomizer customizer = new TestCustomizer();
        customizer.addIgnoreClassPattern(
                "org.seasar.framework.container.customizer",
                "AbstCustomizerTest\\$B.*");
        matched = false;
        customizer.customize(new ComponentDefImpl(Foo.class));
        assertTrue(matched);
        matched = false;
        customizer.customize(new ComponentDefImpl(Bar.class));
        assertFalse(matched);
        matched = false;
        customizer.customize(new ComponentDefImpl(Baz.class));
        assertFalse(matched);
    }

    /**
     * @throws Exception
     */
    public void testApplyIgnore() throws Exception {
        TestCustomizer customizer = new TestCustomizer();
        customizer.addClassPattern("org.seasar.framework.container.customizer",
                "AbstCustomizerTest\\$B.*");
        customizer.addIgnoreClassPattern(
                "org.seasar.framework.container.customizer", ".*z");
        matched = false;
        customizer.customize(new ComponentDefImpl(Foo.class));
        assertFalse(matched);
        matched = false;
        customizer.customize(new ComponentDefImpl(Bar.class));
        assertTrue(matched);
        matched = false;
        customizer.customize(new ComponentDefImpl(Baz.class));
        assertFalse(matched);
    }

    /**
     * @throws Exception
     */
    public void testTargetInterface() throws Exception {
        TestCustomizer customizer = new TestCustomizer();
        customizer.setTargetInterface(Super.class);
        matched = false;
        customizer.customize(new ComponentDefImpl(Foo.class));
        assertTrue(matched);
        matched = false;
        customizer.customize(new ComponentDefImpl(Bar.class));
        assertTrue(matched);
        matched = false;
        customizer.customize(new ComponentDefImpl(Baz.class));
        assertFalse(matched);
    }

    /**
     * 
     */
    public class TestCustomizer extends AbstractCustomizer {
        protected void doCustomize(ComponentDef componentDef) {
            System.out.println(componentDef.getComponentClass().getName());
            matched = true;
        }
    }

    /**
     * 
     */
    public interface Super {
    }

    /**
     * 
     */
    public interface Sub extends Super {
    }

    /**
     * 
     */
    public static class Foo implements Super {
    }

    /**
     * 
     */
    public static class Bar implements Sub {
    }

    /**
     * 
     */
    public static class Baz {
    }
}
