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

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author koichik
 */
public abstract class S2EasyMockTestCase extends S2TestCase {

    // instance fields
    protected EasyMockSupport easyMockSupport = new EasyMockSupport();

    public S2EasyMockTestCase() {
    }

    public S2EasyMockTestCase(final String name) {
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

    protected abstract class Subsequence {
        public void doTest() throws Exception {
            record();
            S2EasyMockTestCase.this.easyMockSupport.replay();
            replay();
            S2EasyMockTestCase.this.easyMockSupport.verify();
            S2EasyMockTestCase.this.easyMockSupport.reset();
        }

        protected abstract void replay() throws Exception;

        protected void record() throws Exception {
        }
    }
}
