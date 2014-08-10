/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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

import org.seasar.framework.container.S2Container;
import org.seasar.framework.unit.annotation.EasyMockType;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * EasyMockとの対話をサポートします。
 * 
 * @author koichik
 */
public class EasyMockSupport {

    // class fields
    /** EasyMockの実装に委譲するオブジェクト */
    protected static final EasyMockDelegator easyMock = getEasyMockDelegator();

    // instance fields
    /** モックのリスト */
    protected List<Object> mocks = CollectionsUtil.newArrayList();

    /** モックがバインディングされたフィールド */
    protected Set<Field> boundFields = CollectionsUtil.newLinkedHashSet();

    /**
     * デフォルトのモックを作成します。
     * 
     * @param <T>
     *            モックの型
     * @param clazz
     *            モックの対象とするクラス
     * @return 作成されたモック
     */
    public <T> T createMock(final Class<T> clazz) {
        final T mock = easyMock.createMock(clazz);
        mocks.add(mock);
        return mock;
    }

    /**
     * Niceモードのモックを作成します。
     * 
     * @param <T>
     *            モックの型
     * @param clazz
     *            モックの対象となるクラス
     * @return 作成されたモック
     */
    public <T> T createNiceMock(final Class<T> clazz) {
        final T mock = easyMock.createNiceMock(clazz);
        mocks.add(mock);
        return mock;
    }

    /**
     * Strictモードのモックを作成します。
     * 
     * @param <T>
     *            モックの型
     * @param clazz
     *            モックの対象となるクラス
     * @return 作成された
     */
    public <T> T createStrictMock(final Class<T> clazz) {
        final T mock = easyMock.createStrictMock(clazz);
        mocks.add(mock);
        return mock;
    }

    /**
     * このオブジェクトで管理するすべてのモックをreplayモードに設定します。
     */
    public void replay() {
        for (final Object mock : mocks) {
            easyMock.replay(mock);
        }
    }

    /**
     * このオブジェクトで管理するすべてのモックとのインタラクションを検証します。
     * 
     */
    public void verify() {
        for (final Object mock : mocks) {
            easyMock.verify(mock);
        }
    }

    /**
     * このオブジェクトで管理するすべてのモックをリセットします。
     */
    public void reset() {
        for (final Object mock : mocks) {
            easyMock.reset(mock);
        }
    }

    /**
     * このオブジェクトの状態を消去します。
     */
    public void clear() {
        mocks.clear();
        boundFields.clear();
    }

    /**
     * モックをフィールドにバインディングします。
     * 
     * @param test
     *            テストクラスのインスタンス
     * @param container
     *            S2コンテナ
     */
    public void bindMockFields(final Object test, final S2Container container) {
        boundFields.clear();
        for (Class<?> clazz = test.getClass(); clazz != Object.class; clazz = clazz
                .getSuperclass()) {
            for (final Field field : clazz.getDeclaredFields()) {
                bindMockField(field, test, container);
            }
        }
    }

    /**
     * モックをフィールドにバインディングします。
     * <p>
     * {@link org.seasar.framework.unit.annotation.EasyMock}
     * が注釈されたフィールドがモックのバインディング対象です。
     * {@link org.seasar.framework.unit.annotation.EasyMock#register()}に
     * <code>true</code>が指定されている場合、 作成されたモックはS2コンテナに登録されます。
     * </p>
     * 
     * @param field
     *            フィールド
     * @param test
     *            テストクラスのインスタンス
     * @param container
     *            S2コンテナ
     */
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

    /**
     * モックとフィールドのバインディングを解除します。
     * 
     * @param test
     *            テストクラスのインスタンス
     */
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

    /**
     * {@link EasyMockDelegator}の実装クラスを返します。
     * 
     * @return {@link EasyMockDelegator}の実装クラス
     */
    protected static EasyMockDelegator getEasyMockDelegator() {
        try {
            Class.forName("org.easymock.classextension.EasyMock");
            Class.forName("net.sf.cglib.proxy.Enhancer");
            return new ClassExtensionEacyMockDelegator();
        } catch (final Throwable t) {
        }
        return new DefaultEacyMockDelegator();
    }

}
