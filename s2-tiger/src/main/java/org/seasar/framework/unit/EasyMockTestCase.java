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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.framework.unit.annotation.EasyMockType;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * @author koichik
 */
public abstract class EasyMockTestCase extends TestCase {
    // instance fields
    List<Object> mocks = new ArrayList<Object>();

    Set<Field> mockFields = CollectionsUtil.newLinkedHashSet();

    public EasyMockTestCase() {
    }

    public EasyMockTestCase(final String name) {
        super(name);
    }

    @Override
    public void runBare() throws Throwable {
        mocks.clear();
        bindFields();
        try {
            doRunTest();
        } finally {
            unbindFields();
        }
    }

    protected void bindFields() throws Throwable {
        bindMockFields();
    }

    protected void bindMockFields() throws Throwable {
        mockFields.clear();
        for (Class<?> clazz = getClass(); clazz != S2EasyMockTestCase.class
                && clazz != null; clazz = clazz.getSuperclass()) {
            for (final Field field : clazz.getDeclaredFields()) {
                bindMockField(field);
            }
        }
    }

    protected void bindMockField(final Field field) throws Throwable {
        final org.seasar.framework.unit.annotation.EasyMock annotation = field
                .getAnnotation(org.seasar.framework.unit.annotation.EasyMock.class);
        if (annotation == null) {
            return;
        }
        field.setAccessible(true);
        if (FieldUtil.get(field, this) != null) {
            return;
        }
        final Class<?> clazz = field.getType();
        final EasyMockType mockType = annotation.value();
        final Object mock;
        if (mockType == EasyMockType.STRICT) {
            mock = createStrictMock(clazz);
        } else if (mockType == EasyMockType.NICE) {
            mock = createNiceMock(clazz);
        } else {
            mock = createMock(clazz);
        }
        FieldUtil.set(field, this, mock);
        mockFields.add(field);
    }

    protected void doRunTest() throws Throwable {
        final boolean recorded = doRecord();
        if (recorded) {
            replay();
        }
        runTest();
        if (recorded) {
            verify();
            reset();
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
        unbindMockFields();
    }

    protected void unbindMockFields() {
        for (final Field field : mockFields) {
            try {
                field.set(this, null);
            } catch (final IllegalArgumentException e) {
                System.err.println(e);
            } catch (final IllegalAccessException e) {
                System.err.println(e);
            }
        }
        mockFields.clear();
    }

    protected <T> T createMock(final Class<T> clazz) {
        final T mock = EasyMock.createMock(clazz);
        mocks.add(mock);
        return mock;
    }

    protected <T> T createNiceMock(final Class<T> clazz) {
        final T mock = EasyMock.createNiceMock(clazz);
        mocks.add(mock);
        return mock;
    }

    protected <T> T createStrictMock(final Class<T> clazz) {
        final T mock = EasyMock.createStrictMock(clazz);
        mocks.add(mock);
        return mock;
    }

    protected void replay() {
        for (final Object mock : mocks) {
            EasyMock.replay(mock);
        }
    }

    protected void verify() {
        for (final Object mock : mocks) {
            EasyMock.verify(mock);
        }
    }

    protected void reset() {
        for (final Object mock : mocks) {
            EasyMock.reset(mock);
        }
    }

    protected abstract class Subsequence {
        public void doTest() throws Exception {
            record();
            EasyMockTestCase.this.replay();
            replay();
            EasyMockTestCase.this.verify();
            EasyMockTestCase.this.reset();
        }

        protected void record() throws Exception {
        }

        protected abstract void replay() throws Exception;
    }
}
