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
package examples.aop.mockinterceptor;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.aop.interceptors.MockInterceptor;

public class HelloTest extends S2TestCase {
    private static String PATH = "Hello.dicon";

    private Hello hello;

    private MockInterceptor mi;

    public void testHello() throws Exception {

        assertEquals("Hello", hello.greeting());
        assertEquals("Hoge", hello.echo("test"));

        hello.echo("Hello");
        assertEquals(true, mi.isInvoked("echo"));

        assertEquals("Hello", mi.getArgs("echo")[0]);

    }

    protected void setUp() throws Exception {
        include(PATH);
    }

    protected void tearDown() throws Exception {
    }

    public HelloTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(HelloTest.class);
    }

}
