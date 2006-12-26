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
import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.unit.annotation.EasyMockType;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * @author koichik
 * 
 */
public class EasyMockSupport {

    // instance fields
    protected List<Object> mocks = CollectionsUtil.newArrayList();

    protected Set<Field> boundFields = CollectionsUtil.newLinkedHashSet();

    public <T> T createMock(final Class<T> clazz) {
        final T mock = EasyMock.createMock(clazz);
        mocks.add(mock);
        return mock;
    }

    public <T> T createNiceMock(final Class<T> clazz) {
        final T mock = EasyMock.createNiceMock(clazz);
        mocks.add(mock);
        return mock;
    }

    public <T> T createStrictMock(final Class<T> clazz) {
        final T mock = EasyMock.createStrictMock(clazz);
        mocks.add(mock);
        return mock;
    }

    public void replay() {
        for (final Object mock : mocks) {
            EasyMock.replay(mock);
        }
    }

    public void verify() {
        for (final Object mock : mocks) {
            EasyMock.verify(mock);
        }
    }

    public void reset() {
        for (final Object mock : mocks) {
            EasyMock.reset(mock);
        }
    }

    public void clear() {
        mocks.clear();
        boundFields.clear();
    }

    public void bindMockFields(final Object test, final S2Container container) {
        boundFields.clear();
        for (Class<?> clazz = test.getClass(); clazz != Object.class; clazz = clazz
                .getSuperclass()) {
            for (final Field field : clazz.getDeclaredFields()) {
                bindMockField(field, test, container);
            }
        }
    }

    protected void bindMockField(final Field field, final Object test,
            final S2Container container) {
        final org.seasar.framework.unit.annotation.EasyMock annotation = field
                .getAnnotation(org.seasar.framework.unit.annotation.EasyMock.class);
        if (annotation == null) {
            return;
        }
        field.setAccessible(true);
        if (FieldUtil.get(field, test) != null) {
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
        FieldUtil.set(field, test, mock);
        boundFields.add(field);
        if (annotation.register()) {
            container.register(mock, field.getName());
        }
    }

    public void unbindMockFields(final Object test) {
        for (final Field field : boundFields) {
            try {
                field.set(test, null);
            } catch (final IllegalArgumentException e) {
                System.err.println(e);
            } catch (final IllegalAccessException e) {
                System.err.println(e);
            }
        }
        boundFields.clear();
    }

}
