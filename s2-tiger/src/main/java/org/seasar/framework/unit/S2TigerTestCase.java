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
package org.seasar.framework.unit;

import java.lang.reflect.Method;
import java.util.Map;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.aop.Pointcut;
import org.seasar.framework.aop.interceptors.MockInterceptor;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.factory.AspectDefFactory;
import org.seasar.framework.env.Env;
import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.framework.unit.annotation.Mock;
import org.seasar.framework.unit.annotation.Mocks;
import org.seasar.framework.unit.impl.OgnlExpression;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * @author koichik
 */
public abstract class S2TigerTestCase extends S2TestCase {

    // instance fields
    protected EasyMockSupport easyMockSupport = new EasyMockSupport();

    public S2TigerTestCase() {
    }

    public S2TigerTestCase(final String name) {
        super(name);
    }

    @Override
    public void runBare() throws Throwable {
        easyMockSupport.clear();
        super.runBare();
    }

    @Override
    protected void setUpForEachTestMethod() throws Throwable {
        super.setUpForEachTestMethod();
        applyMockInterceptor(getTargetMethod());
        easyMockSupport.bindMockFields(this, getContainer());
    }

    @Override
    protected void doRunTest() throws Throwable {
        final boolean recorded = doRecord();
        if (recorded) {
            easyMockSupport.replay();
        }
        super.doRunTest();
        if (recorded) {
            easyMockSupport.verify();
            easyMockSupport.reset();
        }
    }

    protected boolean doRecord() {
        final String targetName = getTargetName();
        if (!StringUtil.isEmpty(targetName)) {
            try {
                final Method method = ClassUtil.getMethod(getClass(), "record"
                        + targetName, null);
                MethodUtil.invoke(method, this, null);
                return true;
            } catch (final NoSuchMethodRuntimeException ignore) {
            }
        }
        return false;
    }

    @Override
    protected void tearDownForEachTestMethod() throws Throwable {
        easyMockSupport.unbindMockFields(this);
        super.tearDownForEachTestMethod();
    }

    protected <T> T createMock(final Class<T> clazz) {
        return easyMockSupport.createMock(clazz);
    }

    protected <T> T createNiceMock(final Class<T> clazz) {
        return easyMockSupport.createNiceMock(clazz);
    }

    protected <T> T createStrictMock(final Class<T> clazz) {
        return easyMockSupport.createStrictMock(clazz);
    }

    protected void applyMockInterceptor(final Method method) {
        final Mock mock = method.getAnnotation(Mock.class);
        if (mock != null) {
            applyMockInterceptor(mock, method);
        } else {
            final Mocks mocks = method.getAnnotation(Mocks.class);
            if (mocks != null) {
                for (final Mock each : mocks.value()) {
                    applyMockInterceptor(each, method);
                }
            }
        }
    }

    protected void applyMockInterceptor(final Mock mock, final Method method) {
        final MockInterceptor mi = new MockInterceptor();
        if (!StringUtil.isEmpty(mock.returnValue())) {
            final Expression exp = createExpression(mock.returnValue(), method);
            mi.setReturnValue(exp.evaluate());
        }
        if (!StringUtil.isEmpty(mock.throwable())) {
            final Expression exp = createExpression(mock.throwable(), method);
            final Object result = exp.evaluate();
            mi.setThrowable(Throwable.class.cast(result));
        }
        Pointcut pc = null;
        if (StringUtil.isEmpty(mock.pointcut())) {
            pc = AspectDefFactory.createPointcut(mock.target());
        } else {
            pc = AspectDefFactory.createPointcut(mock.pointcut());
        }
        final Object componentKey = StringUtil.isEmpty(mock.targetName()) ? mock
                .target()
                : mock.targetName();
        final AspectDef aspectDef = AspectDefFactory.createAspectDef(mi, pc);
        addAspecDef(componentKey, aspectDef);
    }

    protected Expression createExpression(final String source,
            final Method method) {
        final Map<String, Object> ctx = CollectionsUtil.newHashMap();
        ctx.put("ENV", Env.getValue());
        ctx.put("method", method);
        return new OgnlExpression(source, this, ctx);
    }

    protected void addAspecDef(final Object componentKey,
            final AspectDef aspectDef) {
        getContainer().getComponentDef(componentKey).addAspectDef(0, aspectDef);
    }

    protected abstract class Subsequence {
        public void doTest() throws Exception {
            record();
            easyMockSupport.replay();
            replay();
            easyMockSupport.verify();
            easyMockSupport.reset();
        }

        protected abstract void replay() throws Exception;

        protected void record() throws Exception {
        }
    }

}
