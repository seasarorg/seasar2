/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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
package org.seasar.framework.aop.intertype;

import java.util.ArrayList;

import javassist.CannotCompileException;
import javassist.NotFoundException;
import junit.framework.TestCase;

import org.seasar.framework.aop.Aspect;
import org.seasar.framework.aop.InterType;
import org.seasar.framework.aop.impl.AspectImpl;
import org.seasar.framework.aop.interceptors.TraceInterceptor;
import org.seasar.framework.aop.proxy.AopProxy;

public class AbstractInterTypeTest extends TestCase {

    public AbstractInterTypeTest() {
    }

    public AbstractInterTypeTest(String name) {
        super(name);
    }

    public void test() throws Exception {
        AopProxy aopProxy = new AopProxy(ArrayList.class,
                new Aspect[] { new AspectImpl(new TraceInterceptor()) },
                new InterType[] { new TestInterType() });
        Runnable o = (Runnable) aopProxy.create();
        o.run();
        assertEquals("1", "1", o.toString());
    }

    public static class TestInterType extends AbstractInterType {
        protected void introduce() {
            addInterface(Runnable.class);
            addField(String.class, "hoge", "\"hoge\"");
            addMethod("run", "add(hoge);");
            addMethod(String.class, "toString",
                    "return Integer.toString(size());");
        }
    }
}
