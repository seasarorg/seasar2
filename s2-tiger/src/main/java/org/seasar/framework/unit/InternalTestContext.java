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

import java.lang.reflect.Method;

import org.seasar.extension.dataset.ColumnType;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.framework.aop.interceptors.MockInterceptor;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.convention.NamingConvention;

/**
 * S2JUnit4の内部的なテストコンテキストです。
 * <p>
 * テストクラスから直接利用してはいけません。
 * </p>
 * 
 * @author taedium
 */
public interface InternalTestContext extends TestContext {

    /**
     * テストクラスを設定します。
     * 
     * @param testClass
     *            テストクラス
     */
    void setTestClass(Class<?> testClass);

    /**
     * テストメソッドを設定します。
     * 
     * @param testMethod
     *            テストメソッド
     */
    void setTestMethod(Method testMethod);

    /**
     * 命名規約を設定します。
     * 
     * @param namingConvention
     *            命名規約
     */
    void setNamingConvention(NamingConvention namingConvention);

    /**
     * S2コンテナを初期化します。
     * 
     */
    void initContainer();

    /**
     * S2コンテナを破棄します。
     * 
     */
    void destroyContainer();

    /**
     * 設定ファイルをインクルードします。
     * 
     */
    void include();

    /**
     * テスト用のデータを準備します。
     * 
     */
    void prepareTestData();

    /**
     * テストのコンテキストで利用するコンテナを返します。
     * 
     * @return コンテナ
     */
    S2Container getContainer();

    /**
     * モックインターセプターを登録します。
     * 
     * @param mockInterceptor
     *            モックインターセプター
     */
    void addMockInterceptor(MockInterceptor mockInterceptor);

    /**
     * コンテナから<code>componentKey</code>をキーにして取得できるコンポーネント定義に<code>aspectDef</code>で表されるアスペクト定義を追加します。
     * 
     * @param componentKey
     *            コンポーネントのキー
     * @param aspectDef
     *            アスペクト定義
     */
    void addAspecDef(Object componentKey, AspectDef aspectDef);

    /**
     * EJB3が使用可能の場合<code>true</code>を返します。
     * 
     * @return EJB3が使用可能の場合<code>true</code>、そうでない場合<code>false</code>
     */
    boolean isEjb3Enabled();

    /**
     * JTAが使用可能の場合<code>true</code>を返します。
     * 
     * @return JTAが使用可能の場合<code>true</code>、そうでない場合<code>false</code>
     */
    boolean isJtaEnabled();

    /**
     * {@link ColumnType カラムの型}を{@link ColumnTypes}に登録します。
     */
    void registerColumnTypes();

    /**
     * {@link #registerColumnTypes()}で登録した{@link ColumnType カラムの型}を元に戻します。
     */
    void revertColumnTypes();
}
