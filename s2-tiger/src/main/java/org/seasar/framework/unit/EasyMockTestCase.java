/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import junit.framework.TestCase;

import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author koichik
 */
public abstract class EasyMockTestCase extends TestCase {
    // instance fields
    protected EasyMockSupport easyMockSupport = new EasyMockSupport();

    public EasyMockTestCase() {
    }

    public EasyMockTestCase(final String name) {
        super(name);
    }

    @Override
    public void runBare() throws Throwable {
        easyMockSupport.clear();
        setUp();
        try {
            bindFields();
            try {
                doRunTest();
            } finally {
                unbindFields();
            }
        } finally {
            tearDown();
        }
    }

    protected void bindFields() throws Throwable {
        easyMockSupport.bindMockFields(this, null);
    }

    protected void doRunTest() throws Throwable {
        final boolean recorded = doRecord();
        if (recorded) {
            easyMockSupport.replay();
        }
        runTest();
        if (recorded) {
            easyMockSupport.verify();
            easyMockSupport.reset();
        }
    }

    protected boolean doRecord() throws Throwable {
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

    protected String getTargetName() {
        return getName().substring(4);
    }

    protected void unbindFields() {
        easyMockSupport.unbindMockFields(this);
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
            EasyMockTestCase.this.easyMockSupport.replay();
            replay();
            EasyMockTestCase.this.easyMockSupport.verify();
            EasyMockTestCase.this.easyMockSupport.reset();
        }

        protected void record() throws Exception {
        }

        protected abstract void replay() throws Exception;
    }
}
