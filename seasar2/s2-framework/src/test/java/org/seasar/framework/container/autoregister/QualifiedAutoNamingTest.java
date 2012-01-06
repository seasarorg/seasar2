/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.autoregister;

import junit.framework.TestCase;

/**
 * 
 * @author koichik
 */
public class QualifiedAutoNamingTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testDefineName() throws Exception {
        QualifiedAutoNaming naming = new QualifiedAutoNaming();
        naming.addIgnorePackagePrefix("foo.bar");
        naming.addIgnorePackagePrefix("hoge");
        naming.addIgnoreClassSuffix("Action");
        assertEquals("1", "hoge", naming.defineName(null, "Hoge"));
        assertEquals("2", "fooHoge", naming.defineName("foo", "Hoge"));
        assertEquals("3", "hoge", naming.defineName("foo.bar", "Hoge"));
        assertEquals("4", "bazHoge", naming.defineName("foo.bar.baz", "Hoge"));
        assertEquals("5", "hogeHoge", naming.defineName("hoge.hoge", "Hoge"));
        assertEquals("6", "bazHoge", naming.defineName("foo.bar.baz",
                "HogeActionImpl"));
    }

    /**
     * @throws Exception
     */
    public void testAddReplaceRule() throws Exception {
        QualifiedAutoNaming naming = new QualifiedAutoNaming();
        naming.addReplaceRule("(Ba[rz])+", "Hoge");
        assertEquals("1", "fooHoge", naming.defineName(null, "FooBarBaz"));
    }

    /**
     * @throws Exception
     */
    public void testClearRule() throws Exception {
        QualifiedAutoNaming naming = new QualifiedAutoNaming();
        naming.clearReplaceRule();
        naming.addIgnorePackagePrefix("foo.bar");
        assertEquals("1", "bazHogeFoo4Impl", naming.defineName(
                "foo.bar.baz.hoge", "Foo4Impl"));
    }

    /**
     * @throws Exception
     */
    public void testNotDecapitalize() throws Exception {
        QualifiedAutoNaming naming = new QualifiedAutoNaming();
        naming.setDecapitalize(false);
        naming.addIgnorePackagePrefix("foo");
        assertEquals("1", "Hoge", naming.defineName(null, "Hoge"));
        assertEquals("2", "Hoge", naming.defineName("foo", "Hoge"));
        assertEquals("3", "BarHoge", naming.defineName("foo.bar", "Hoge"));
    }
}
