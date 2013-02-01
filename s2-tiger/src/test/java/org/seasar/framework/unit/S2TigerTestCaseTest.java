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
package org.seasar.framework.unit;

import java.util.Date;
import java.util.Map;

import javax.sql.DataSource;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.framework.unit.annotation.EasyMock;
import org.seasar.framework.unit.annotation.EasyMockType;
import org.seasar.framework.unit.annotation.Mock;
import org.seasar.framework.unit.annotation.Mocks;

import static org.easymock.EasyMock.*;

/**
 * @author koichik
 * 
 */
public class S2TigerTestCaseTest extends S2TigerTestCase {

    Hello hello;

    Hello2 hello2;

    @EasyMock
    Runnable runnable;

    @EasyMock(EasyMockType.STRICT)
    Map<String, String> map;

    @EasyMock
    Date date;

    @EasyMock(register = true)
    DataSource dataSource;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(getClass().getName().replace('.', '/') + ".dicon");
    }

    /**
     * 
     */
    @Mock(target = Hello.class, returnValue = "'aa'")
    public void testTargetIsInterface() {
        assertEquals("aa", hello.greeting());
    }

    /**
     * 
     */
    @Mock(target = HelloImpl.class, returnValue = "'bb'")
    public void testTargetIsClass() {
        assertEquals("bb", hello.greeting());
    }

    /**
     * 
     */
    @Mock(target = Hello.class, targetName = "hoge", returnValue = "'cc'")
    public void testUsesTargetName() {
        assertEquals("cc", hello.greeting());
    }

    /**
     * 
     */
    @Mock(target = Hello.class, pointcut = "e.*", returnValue = "'dd'")
    public void testUsesPointcut() {
        assertEquals("hello", hello.greeting());
        assertEquals("dd", hello.echo("hoge"));
    }

    /**
     * 
     */
    @Mock(target = Hello2.class, targetName = "foo", returnValue = "'ee'")
    public void testOverridesOtherInterceptor() {
        assertEquals("ee", hello2.greeting());
    }

    /**
     * 
     */
    @Mocks( {
            @Mock(target = Hello.class, pointcut = "greeting", returnValue = "'ff'"),
            @Mock(target = Hello.class, pointcut = "echo", returnValue = "'gg'"),
            @Mock(target = Hello2.class, returnValue = "'hh'") })
    public void testUsesMultiMocks() {
        assertEquals("ff", hello.greeting());
        assertEquals("gg", hello.echo("hoge"));
        assertEquals("hh", hello2.greeting());
    }

    /**
     * 
     */
    @Mock(target = Hello.class, returnValue = "getValue()")
    public void testInvokesMethod() {
        assertEquals("ii", hello.greeting());
    }

    /**
     * @return
     */
    public String getValue() {
        return "ii";
    }

    /**
     * 
     */
    @Mock(target = Hello.class, throwable = "new IllegalArgumentException()")
    public void testUsesThrowable() {
        try {
            hello.echo("hoge");
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    /**
     * 
     */
    public void testRunnable() {
        runnable.run();
    }

    /**
     * 
     */
    public void recordRunnable() {
        runnable.run();
    }

    /**
     * @throws Exception
     */
    public void testMap() throws Exception {
        map.put("a", "A");
        map.put("b", "B");
        assertEquals(2, map.size());
    }

    /**
     * @throws Exception
     */
    public void recordMap() throws Exception {
        expect(map.put("a", "A")).andReturn(null);
        expect(map.put("b", "B")).andReturn(null);
        expect(map.size()).andReturn(2);
    }

    /**
     * @throws Exception
     */
    public void testClass() throws Exception {
        assertEquals(100L, date.getTime());
    }

    /**
     * @throws Exception
     */
    public void recordClass() throws Exception {
        expect(date.getTime()).andReturn(100L);
    }

    /**
     * @throws Exception
     */
    public void testRegister() throws Exception {
        assertSame(dataSource, getComponent("dataSource"));
        assertSame(dataSource, getComponent(DataSource.class));
        assertFalse(getContainer().hasComponentDef(Runnable.class));
        assertFalse(getContainer().hasComponentDef(Map.class));
    }

    /**
     * @throws Exception
     */
    public void testOldStyle() throws Exception {
        new Subsequence() {

            @Override
            protected void replay() throws Exception {
                map.put("a", "A");
                map.put("b", "B");
                assertEquals(2, map.size());
            }

            @Override
            protected void record() throws Exception {
                expect(map.put("a", "A")).andReturn(null);
                expect(map.put("b", "B")).andReturn(null);
                expect(map.size()).andReturn(2);
            }

        }.doTest();
    }

    /**
     * 
     */
    public interface Hello {

        /**
         * @return
         */
        public String greeting();

        /**
         * @param str
         * @return
         */
        public String echo(String str);
    }

    /**
     * 
     */
    public static class HelloImpl implements Hello {

        public String greeting() {
            return "hello";
        }

        public String echo(String str) {
            return str;
        }
    }

    /**
     * 
     */
    public interface Hello2 {

        /**
         * @return
         */
        public String greeting();
    }

    /**
     * 
     */
    public static class Hello2Impl implements Hello2 {

        public String greeting() {
            return "hello";
        }
    }

    /**
     * 
     */
    public static class DummyInteceptor extends AbstractInterceptor {

        static final long serialVersionUID = 0L;

        public Object invoke(MethodInvocation arg0) throws Throwable {
            throw new RuntimeException();
        }
    }

}
