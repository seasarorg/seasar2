/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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

/**
 * EasyMockの実装に委譲するためのインタフェースです。
 * 
 * @author koichik
 */
public interface EasyMockDelegator {

    /**
     * モックを作成して返します。
     * 
     * @param <T>
     *            作成するモックの型
     * @param clazz
     *            作成するモックの型
     * @return 作成されたモック
     */
    <T> T createMock(final Class<T> clazz);

    /**
     * Niceモックを作成して返します。
     * 
     * @param <T>
     *            作成するモックの型
     * @param clazz
     *            作成するモックの型
     * @return 作成されたモック
     */
    <T> T createNiceMock(final Class<T> clazz);

    /**
     * Strictモックを作成して返します。
     * 
     * @param <T>
     *            作成するモックの型
     * @param clazz
     *            作成するモックの型
     * @return 作成されたモック
     */
    <T> T createStrictMock(final Class<T> clazz);

    /**
     * モックをreplayモードに設定します。
     * 
     * @param mocks
     *            モックの配列
     */
    void replay(Object... mocks);

    /**
     * モックとのインタラクションを検証します。
     * 
     * @param mocks
     *            モックの配列
     */
    void verify(Object... mocks);

    /**
     * モックをリセットします。
     * 
     * @param mocks
     *            モックの配列
     */
    void reset(Object... mocks);

}
