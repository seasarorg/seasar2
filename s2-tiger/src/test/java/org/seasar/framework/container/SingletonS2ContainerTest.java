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
package org.seasar.framework.container;

import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * 
 */
public class SingletonS2ContainerTest extends S2FrameworkTestCase {

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        register(Bar.class);
        register(Hoge.class, "hoge");
        Foo foo = SingletonS2Container.getComponent(Foo.class);
        assertNotNull(foo);
        Foo foo2 = SingletonS2Container.getComponent(Bar.class);
        assertNotNull(foo2);
        Bar bar = SingletonS2Container.getComponent(Bar.class);
        assertNotNull(bar);
        Hoge hoge = SingletonS2Container.getComponent(Hoge.class);
        assertNotNull(hoge);
        Hoge hoge2 = SingletonS2Container.getComponent("hoge");
        assertNotNull(hoge2);
    }

    /**
     * 
     */
    public static class Foo {
    }

    /**
     * 
     */
    public static class Bar extends Foo {
    }

    /**
     * 
     */
    public static class Hoge {

    }
}
