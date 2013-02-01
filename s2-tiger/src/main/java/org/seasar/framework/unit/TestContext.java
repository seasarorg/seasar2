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

import org.seasar.extension.dataset.DataSet;
import org.seasar.framework.aop.interceptors.MockInterceptor;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.unit.annotation.PublishedTestContext;

/**
 * S2JUnit4を使ったテストの実行コンテキストを表すインターフェースです。
 * <p>
 * テストの実行コンテキストはテストケース毎に作成/破棄されます。
 * 実行コンテキストは、以下のように定義するとS2JUnit4の自動フィールドバインディング機能により値が設定されます。
 * 
 * <pre>
 * &#064;RunWith(Seasar2.class)
 * public class HogeTest {
 *   ...
 *   private TestContext testContext;
 *   ...
 * }
 * </pre>
 * 
 * </p>
 * 
 * @author taedium
 * 
 */
@PublishedTestContext
public interface TestContext {

    /**
     * クラスをコンポーネント定義として登録します。
     * 
     * @param componentClass
     *            コンポーネントのクラス
     */
    void register(Class<?> componentClass);

    /**
     * 指定された名前でクラスをコンポーネント定義として登録します。
     * 
     * @param componentClass
     *            コンポーネントのクラス
     * @param componentName
     *            コンポーネント名
     */
    void register(Class<?> componentClass, String componentName);

    /**
     * コンポーネントを登録します。
     * 
     * @param component
     *            コンポーネント
     */
    void register(Object component);

    /**
     * 指定された名前でコンポーネントを登録します。
     * 
     * @param component
     *            コンポーネント
     * @param componentName
     *            コンポーネント名
     */
    void register(Object component, String componentName);

    /**
     * コンポーネント定義を登録します。
     * 
     * @param componentDef
     *            コンポーネント定義
     */
    void register(ComponentDef componentDef);

    /**
     * 指定された設定ファイルのパスからS2コンテナを生成し、子コンテナとしてルートのS2コンテナにインクルードします。
     * 
     * @param path
     *            設定ファイルのパス
     */
    void include(String path);

    /**
     * このコンテキストの初期化時に特定のS2コンテナを子コンテナとしてインクルードする場合は<code>true</code>を設定します。
     * 
     * @param autoIncluding
     *            自動インクルードをする場合<code>true</code>
     */
    void setAutoIncluding(boolean autoIncluding);

    /**
     * テストの実行前にテストデータを事前に準備する場合は<code>true</code>を設定します。
     * <p>
     * 代わりに{@link #setPreparationType(PreparationType)}を使用してください。
     * </p>
     * 
     * @param autoPreparing
     *            自動でテストデータを準備する場合<code>true</code>
     */
    @Deprecated()
    void setAutoPreparing(boolean autoPreparing);

    /**
     * {@link PreparationType}を設定します。
     * 
     * @param preparationType
     *            {@link PreparationType}
     */
    void setPreparationType(PreparationType preparationType);

    /**
     * {@link PreparationType}を返します。
     * 
     * @return {@link PreparationType}
     */
    PreparationType getPreparationType();

    /**
     * テストデータの文字列に含まれる空白を取り除く場合は<code>true</code>を設定します。
     * 
     * @param trimString
     *            テストデータの文字列に含まれる空白を取り除く場合は<code>true</code>
     */
    void setTrimString(boolean trimString);

    /**
     * テストデータの文字列に含まれる空白を取り除く場合は<code>true</code>を返します。
     * 
     * @return テストデータの文字列に含まれる空白を取り除く場合は<code>true</code>、そうでない場合は
     *         <code>false</code>
     */
    boolean isTrimString();

    /**
     * 指定されたキーに対応するコンポーネントを返します。
     * 
     * @param <T>
     *            コンポーネントの型
     * @param componentKey
     *            コンポーネントを取得するためのキー
     * @return コンポーネント
     */
    <T> T getComponent(Class<? extends T> componentKey);

    /**
     * 指定されたキーに対応するコンポーネントを返します。
     * 
     * @param componentKey
     *            コンポーネントを取得するためのキー
     * @return コンポーネント
     */
    Object getComponent(Object componentKey);

    /**
     * 指定されたキーに対応するコンポーネント定義が存在する場合<code>true</code>を返します。
     * 
     * @param componentKey
     *            コンポーネントを取得するためのキー
     * @return キーに対応するコンポーネント定義が存在する場合<code>true</code>、そうでない場合は
     *         <code>false</code>
     */
    boolean hasComponentDef(Object componentKey);

    /**
     * 番号で指定された位置のコンポーネント定義を返します。
     * 
     * @param index
     *            番号
     * @return コンポーネント定義
     */
    ComponentDef getComponentDef(int index);

    /**
     * 指定されたキーに対応するコンポーネント定義を返します。
     * 
     * @param componentKey
     *            コンポーネントを取得するためのキー
     * @return コンポーネント定義
     */
    ComponentDef getComponentDef(Object componentKey);

    /**
     * テストの期待値を返します。
     * 
     * @return 期待値が存在すればその値、存在しなければ<code>null</code>
     */
    DataSet getExpected();

    /**
     * テストクラスのパッケージをパスに変換して返します。
     * 
     * @return テストクラスのパッケージを表すパス
     */
    String getTestClassPackagePath();

    /**
     * テストクラスの単純名を返します。
     * 
     * @return テストクラスの単純名
     */
    String getTestClassShortName();

    /**
     * テストメソッドの名称を返します。
     * 
     * @return テストメソッドの名称
     */
    String getTestMethodName();

    /**
     * 番号で指定された位置のモックインターセプタを返します。
     * <p>
     * モックインターセプタは{@link org.seasar.framework.unit.annotation.Mock}により登録されます。
     * 番号は<code>0</code>から始まります。
     * </p>
     * 
     * @param index
     *            番号
     * @return モックインターセプタ
     */
    MockInterceptor getMockInterceptor(int index);

    /**
     * 登録されたモックインターセプタの数を返します。
     * <p>
     * モックインターセプタは{@link org.seasar.framework.unit.annotation.Mock}により登録されます。
     * </p>
     * 
     * @return モックインターセプタ
     */
    int getMockInterceptorSize();
}
