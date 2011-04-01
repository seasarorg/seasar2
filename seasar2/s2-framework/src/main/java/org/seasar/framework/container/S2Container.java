/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
package org.seasar.framework.container;

/**
 * DIとAOPをサポートしたS2コンテナのインターフェースです。
 * 
 * <h4>S2Containerの役割について</h4>
 * <p>
 * コンポーネントの管理を行う機能を提供します。 コンポーネントとは１つかまたそれ以上のクラスで構成されるJavaオブジェクトです。
 * S2コンテナはコンポーネントの生成、コンポーネントの初期化、コンポーネントの取得を提供します。
 * コンポーネントを取得するキーには、コンポーネント名、コンポーネントのクラス、またはコンポーネントが実装するインターフェースを指定することができます。
 * </p>
 * <h4>S2コンテナのインスタンス階層について</h4>
 * <p>
 * S2コンテナ全体は複数のコンテナにより階層化されています。 一つのコンテナは複数のコンテナをインクルードすることができます。
 * 複数のコンテナが同一のコンテナをインクルードすることができます。
 * </p>
 * <p>
 * インクルードの参照範囲についてのイメージを示します。<br>
 * <img
 * src="http://s2container.seasar.org/ja/images/include_range_20040706.png"/><br>
 * コンテナの検索順についてのイメージを示します。<br>
 * <img
 * src="http://s2container.seasar.org/ja/images/include_search_20040706.png"/>
 * </p>
 * <h4>S2コンテナのインジェクションの種類について</h4>
 * <p>
 * S2コンテナは3種類のインジェクションをサポートします。
 * <dl>
 * <dt>{@link ConstructorAssembler コンストラクタ・インジェクション}</dt>
 * <dd>コンストラクタ引数を利用してコンポーネントをセットします。 </dd>
 * <dt>{@link PropertyAssembler セッター・インジェクション}</dt>
 * <dd>セッターメソッドを利用してコンポーネントをセットします。 </dd>
 * <dt>{@link MethodAssembler メソッド・インジェクション}</dt>
 * <dd>任意のメソッドを利用してコンポーネントをセットします。 </dd>
 * </dl>
 * </p>
 * <h4>S2Containerが持つメソッドの分類について</h4>
 * <p>
 * コンテナへの登録、コンテナからのコンポーネント取得、検索などを行うコンポーネントを管理する機能
 * <ul>
 * <li>{@link S2Container#getComponent getComponent}</li>
 * <li>{@link S2Container#getComponentDefSize getComponentDefSize}</li>
 * <li>{@link S2Container#getComponentDef getComponentDef}</li>
 * <li>{@link S2Container#findComponents findComponents}</li>
 * <li>{@link S2Container#findAllComponents findAllComponents}</li>
 * <li>{@link S2Container#findLocalComponents findLocalComponents}</li>
 * <li>{@link S2Container#findComponentDefs findComponentDefs}</li>
 * <li>{@link S2Container#findAllComponentDefs findAllComponentDefs}</li>
 * <li>{@link S2Container#findLocalComponentDefs findLocalComponentDefs}</li>
 * <li>{@link S2Container#hasComponentDef hasComponentDef}</li>
 * <li>{@link S2Container#register register}</li>
 * <li>{@link S2Container#injectDependency injectDependency}</li>
 * </ul>
 * コンテナの初期化、終了処理、コンテナの階層化、階層化されたコンテナへのアクセスなどコンテナを管理する機能
 * <ul>
 * <li>{@link S2Container#getNamespace getNamespace}</li>
 * <li>{@link S2Container#setNamespace setNamespace}</li>
 * <li>{@link S2Container#getPath getPath getPath getPath}</li>
 * <li>{@link S2Container#setPath setPath setPath setPath}</li>
 * <li>{@link S2Container#getClassLoader getClassLoader}</li>
 * <li>{@link S2Container#setClassLoader setClassLoader}</li>
 * <li>{@link S2Container#init init}</li>
 * <li>{@link S2Container#destroy destroy}</li>
 * <li>{@link S2Container#getExternalContext getExternalContext}</li>
 * <li>{@link S2Container#setExternalContext setExternalContext}</li>
 * <li>{@link S2Container#getExternalContextComponentDefRegister getExternalContextComponentDefRegister}</li>
 * <li>{@link S2Container#setExternalContextComponentDefRegister setExternalContextComponentDefRegister}</li>
 * <li>{@link S2Container#hasDescendant hasDescendant}</li>
 * <li>{@link S2Container#getDescendant getDescendant}</li>
 * <li>{@link S2Container#registerDescendant registerDescendant}</li>
 * <li>{@link S2Container#include include}</li>
 * <li>{@link S2Container#getChildSize getChildSize}</li>
 * <li>{@link S2Container#getChild getChild}</li>
 * <li>{@link S2Container#getParentSize getParentSize}</li>
 * <li>{@link S2Container#getParent getParent}</li>
 * <li>{@link S2Container#addParent addParent}</li>
 * <li>{@link S2Container#getRoot getRoot}</li>
 * <li>{@link S2Container#setRoot setRoot}</li>
 * <li>{@link S2Container#registerMap registerMap}</li>
 * </ul>
 * </p>
 * 
 * @author higa
 * @author vestige &amp; SeasarJavaDoc Committers
 */
public interface S2Container extends MetaDefAware {

    /**
     * 指定されたキーに対応するコンポーネントを返します。
     * <p>
     * キーが文字列の場合、名前が一致するコンポーネントを返します。
     * キーがクラスまたはインターフェースの場合、キーの型に代入可能なコンポーネントを返します。
     * </p>
     * 
     * @param componentKey
     *            コンポーネントを取得するためのキー
     * @return コンポーネント
     * @throws ComponentNotFoundRuntimeException
     *             コンポーネントが見つからない場合
     * @throws TooManyRegistrationRuntimeException
     *             同じ名前、または同じクラスに複数のコンポーネントが登録されている場合
     * @throws CyclicReferenceRuntimeException
     *             コンストラクタ・インジェクションでコンポーネントの参照が循環している場合
     */
    Object getComponent(Object componentKey)
            throws ComponentNotFoundRuntimeException,
            TooManyRegistrationRuntimeException,
            CyclicReferenceRuntimeException;

    /**
     * 指定されたキーに対応する複数のコンポーネントを検索して返します。
     * <p>
     * 検索の範囲は現在のS2コンテナおよび、インクルードしているS2コンテナの階層全体です。
     * キーに対応するコンポーネントが最初に見つかったS2コンテナを対象とします。
     * このS2コンテナから，キーに対応する全てのコンポーネントを配列で返します。
     * 返される配列に含まれるコンポーネントは全て同一のS2コンテナに登録されたものです。
     * </p>
     * 
     * @param componentKey
     *            コンポーネントを取得するためのキー
     * @return キーに対応するコンポーネントの配列を返します。 キーに対応するコンポーネントが存在しない場合は長さ0の配列を返します。
     * @throws CyclicReferenceRuntimeException
     *             コンストラクタ・インジェクションでコンポーネントの参照が循環している場合
     * @see #findAllComponents
     * @see #findLocalComponents
     */
    Object[] findComponents(Object componentKey)
            throws CyclicReferenceRuntimeException;

    /**
     * 指定されたキーに対応する複数のコンポーネントを検索して返します。
     * <p>
     * 検索の範囲は現在のS2コンテナおよび、インクルードしているS2コンテナの階層全体です。
     * キーに対応するコンポーネントが最初に見つかったS2コンテナとその子孫コンテナの全てを対象とします。
     * 対象になるS2コンテナ全体から、キーに対応する全てのコンポーネントを配列で返します。
     * </p>
     * 
     * @param componentKey
     *            コンポーネントを取得するためのキー
     * @return キーに対応するコンポーネントの配列を返します。 キーに対応するコンポーネントが存在しない場合は長さ0の配列を返します。
     * @throws CyclicReferenceRuntimeException
     *             コンストラクタ・インジェクションでコンポーネントの参照が循環している場合
     * @see #findComponents
     * @see #findLocalComponents
     */
    Object[] findAllComponents(Object componentKey)
            throws CyclicReferenceRuntimeException;

    /**
     * 指定されたキーに対応する複数のコンポーネントを検索して返します。
     * <p>
     * 検索の範囲は現在のS2コンテナのみです。 現在のS2コンテナから、キーに対応する全てのコンポーネントを配列で返します。
     * </p>
     * 
     * @param componentKey
     *            コンポーネントを取得するためのキー
     * @return キーに対応するコンポーネントの配列を返します。 キーに対応するコンポーネントが存在しない場合は長さ0の配列を返します。
     * @throws CyclicReferenceRuntimeException
     *             コンストラクタ・インジェクションでコンポーネントの参照が循環している場合
     * @see #findComponents
     * @see #findAllComponents
     */
    Object[] findLocalComponents(Object componentKey)
            throws CyclicReferenceRuntimeException;

    /**
     * <code>outerComponent</code>のクラスをキーとして登録された
     * {@link ComponentDef コンポーネント定義}に従って、必要なコンポーネントのインジェクションを実行します。
     * アスペクト、コンストラクタ・インジェクションは適用できません。
     * <p>
     * {@link ComponentDef コンポーネント定義}の{@link InstanceDef インスタンス定義}は
     * {@link InstanceDef#OUTER_NAME outer}でなくてはなりません。
     * </p>
     * 
     * @param outerComponent
     *            外部コンポーネント
     * @throws ClassUnmatchRuntimeException
     *             適合するコンポーネント定義が見つからない場合
     */
    void injectDependency(Object outerComponent)
            throws ClassUnmatchRuntimeException;

    /**
     * <code>componentClass</code>をキーとして登録された {@link ComponentDef コンポーネント定義}に従って、必要なコンポーネントのインジェクションを実行します。
     * アスペクト、コンストラクタ・インジェクションは適用できません。
     * <p>
     * {@link ComponentDef コンポーネント定義}の{@link InstanceDef インスタンス定義}は
     * {@link InstanceDef#OUTER_NAME outer}でなくてはなりません。
     * </p>
     * 
     * @param outerComponent
     *            外部コンポーネント
     * @param componentClass
     *            コンポーネント定義のキー (クラス)
     * @throws ClassUnmatchRuntimeException
     *             適合するコンポーネント定義が見つからない場合
     */
    void injectDependency(Object outerComponent, Class componentClass)
            throws ClassUnmatchRuntimeException;

    /**
     * <code>componentName</code>をキーとして登録された {@link ComponentDef コンポーネント定義}に従って、インジェクションを実行します。
     * アスペクト、コンストラクタ・インジェクションは適用できません。
     * <p>
     * {@link ComponentDef コンポーネント定義}の{@link InstanceDef インスタンス定義}は
     * {@link InstanceDef#OUTER_NAME outer}でなくてはなりません。
     * </p>
     * 
     * @param outerComponent
     *            外部コンポーネント
     * @param componentName
     *            コンポーネント定義のキー (名前)
     * @throws ClassUnmatchRuntimeException
     */
    void injectDependency(Object outerComponent, String componentName)
            throws ClassUnmatchRuntimeException;

    /**
     * コンポーネントを登録します。
     * <p>
     * S2コンテナに無名のコンポーネントとして登録します。 登録されたコンポーネントはインジェクションやアスペクトの適用などは出来ません。
     * 他のコンポーネント構築時に依存オブジェクトとして利用することが可能です。
     * </p>
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
     * クラスをコンポーネント定義として登録します。
     * <p>
     * 登録するコンポーネントは以下のものになります。
     * <dl>
     * <dt>{@link InstanceDef インスタンス定義}</dt>
     * <dd><code>singleton</code></dd>
     * <dt>{@link AutoBindingDef 自動バインディング定義}</dt>
     * <dd><code>auto</code></dd>
     * </dl>
     * </p>
     * 
     * @param componentClass
     *            コンポーネントのクラス
     */
    void register(Class componentClass);

    /**
     * 指定された名前でクラスをコンポーネント定義として登録します。
     * <p>
     * 登録するコンポーネントは以下のものになります。
     * <dl>
     * <dt>{@link InstanceDef インスタンス定義}</dt>
     * <dd><code>singleton</code></dd>
     * <dt>{@link AutoBindingDef 自動バインディング定義}</dt>
     * <dd><code>auto</code></dd>
     * </dl>
     * </p>
     * 
     * @param componentClass
     *            コンポーネントのクラス
     * @param componentName
     *            コンポーネント名
     */
    void register(Class componentClass, String componentName);

    /**
     * コンポーネント定義を登録します。
     * 
     * @param componentDef
     *            コンポーネント定義
     */
    void register(ComponentDef componentDef);

    /**
     * コンテナに登録されているコンポーネント定義の数を返します。
     * 
     * @return コンポーネント定義の数
     */
    int getComponentDefSize();

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
     *            キー
     * @return コンポーネント定義
     * @throws ComponentNotFoundRuntimeException
     *             コンポーネント定義が見つからない場合
     */
    ComponentDef getComponentDef(Object componentKey)
            throws ComponentNotFoundRuntimeException;

    /**
     * 指定されたキーに対応する複数のコンポーネント定義を検索して返します。
     * <p>
     * 検索の範囲は現在のS2コンテナおよび、インクルードしているS2コンテナの階層全体です。
     * キーに対応するコンポーネントが最初に見つかったS2コンテナを対象とします。
     * このS2コンテナから，キーに対応する全てのコンポーネント定義を配列で返します。
     * 返される配列に含まれるコンポーネント定義は全て同一のS2コンテナに登録されたものです。
     * </p>
     * 
     * @param componentKey
     *            コンポーネント定義を取得するためのキー
     * @return キーに対応するコンポーネント定義の配列を返します。 キーに対応するコンポーネント定義が存在しない場合は長さ0の配列を返します。
     * @see #findAllComponentDefs
     * @see #findLocalComponentDefs
     */
    ComponentDef[] findComponentDefs(Object componentKey);

    /**
     * 指定されたキーに対応する複数のコンポーネント定義を検索して返します。
     * <p>
     * 検索の範囲は現在のS2コンテナおよび、インクルードしているS2コンテナの階層全体です。
     * キーに対応するコンポーネントが最初に見つかったS2コンテナとその子孫コンテナの全てを対象とします。
     * 対象になるS2コンテナ全体から、キーに対応する全てのコンポーネント定義を配列で返します。
     * </p>
     * 
     * @param componentKey
     *            コンポーネント定義を取得するためのキー
     * @return キーに対応するコンポーネント定義の配列を返します。 キーに対応するコンポーネント定義が存在しない場合は長さ0の配列を返します。
     * @see #findComponentDefs
     * @see #findLocalComponentDefs
     */
    ComponentDef[] findAllComponentDefs(Object componentKey);

    /**
     * 指定されたキーに対応する複数のコンポーネント定義を検索して返します。
     * <p>
     * 検索の範囲は現在のS2コンテナのみです。 現在のS2コンテナから、キーに対応する全てのコンポーネント定義を配列で返します。
     * </p>
     * 
     * @param componentKey
     *            コンポーネント定義を取得するためのキー
     * @return キーに対応するコンポーネント定義の配列を返します。 キーに対応するコンポーネント定義が存在しない場合は長さ0の配列を返します。
     * @see #findComponentDefs
     * @see #findAllComponentDefs
     */
    ComponentDef[] findLocalComponentDefs(Object componentKey);

    /**
     * 指定されたキーに対応するコンポーネント定義が存在する場合<code>true</code>を返します。
     * 
     * @param componentKey
     *            キー
     * @return キーに対応するコンポーネント定義が存在する場合<code>true</code>、そうでない場合は<code>false</code>
     */
    boolean hasComponentDef(Object componentKey);

    /**
     * <code>path</code>を読み込んだS2コンテナが存在する場合<code>true</code>を返します。
     * 
     * @param path
     *            パス
     * @return <code>path</code>を読み込んだS2コンテナが存在する場合<code>true</code>、そうでない場合は<code>false</code>
     */
    boolean hasDescendant(String path);

    /**
     * <code>path</code>を読み込んだS2コンテナを返します。
     * 
     * @param path
     *            パス
     * @return S2コンテナ
     * @throws ContainerNotRegisteredRuntimeException
     *             S2コンテナが見つからない場合
     */
    S2Container getDescendant(String path)
            throws ContainerNotRegisteredRuntimeException;

    /**
     * <code>descendant</code>を子孫コンテナとして登録します。
     * <p>
     * 子孫コンテナとは、このコンテナに属する子のコンテナや、その子であるコンテナです。
     * </p>
     * 
     * @param descendant
     *            子孫コンテナ
     */
    void registerDescendant(S2Container descendant);

    /**
     * コンテナを子としてインクルードします。
     * 
     * @param child
     *            インクルードするS2コンテナ
     */
    void include(S2Container child);

    /**
     * インクルードしている子コンテナの数を返します。
     * 
     * @return 子コンテナの数
     */
    int getChildSize();

    /**
     * 番号で指定された位置の子コンテナを返します。
     * 
     * @param index
     *            子コンテナの番号
     * @return 子コンテナ
     */
    S2Container getChild(int index);

    /**
     * このコンテナをインクルードしている親コンテナの数を返します。
     * 
     * @return 親コンテナの数
     */
    int getParentSize();

    /**
     * 番号で指定された位置の親コンテナを返します。
     * 
     * @param index
     *            親コンテナの番号
     * @return 親コンテナ
     */
    S2Container getParent(int index);

    /**
     * 親コンテナを追加します。
     * 
     * @param parent
     *            親として追加するS2コンテナ
     */
    void addParent(S2Container parent);

    /**
     * コンテナの初期化を行います。 子コンテナを持つ場合、子コンテナを全て初期化した後、自分の初期化を行います。
     */
    void init();

    /**
     * コンテナの終了処理をおこないます。 子コンテナを持つ場合、自分の終了処理を実行した後、子コンテナ全ての終了処理を行います。
     */
    void destroy();

    /**
     * 名前空間を返します。
     * 
     * @return 名前空間
     */
    String getNamespace();

    /**
     * 名前空間を設定します。
     * 
     * @param namespace
     *            名前空間
     */
    void setNamespace(String namespace);

    /**
     * コンテナ作成時に初期化する場合<code>true</code>を返します。
     * 
     * @return コンテナ作成時に初期化する場合<code>true</code>
     */
    boolean isInitializeOnCreate();

    /**
     * コンテナ作成時に初期化する場合<code>true</code>を設定します。
     * 
     * @param initializeOnCreate
     *            コンテナ作成時に初期化する場合<code>true</code>
     */
    void setInitializeOnCreate(boolean initializeOnCreate);

    /**
     * 設定ファイルの<code>path</code>を返します。
     * 
     * @return 設定ファイルの<code>path</code>
     */
    String getPath();

    /**
     * 設定ファイルの<code>path</code>を設定します。
     * 
     * @param path
     *            設定ファイルの<code>path</code>
     */
    void setPath(String path);

    /**
     * ルートのS2コンテナを返します。
     * 
     * @return ルートのS2コンテナ
     */
    S2Container getRoot();

    /**
     * ルートのS2コンテナを設定します。
     * 
     * @param root
     *            S2コンテナ
     */
    void setRoot(S2Container root);

    /**
     * 外部コンテキストを返します。
     * 
     * @return 外部コンテキスト
     */
    ExternalContext getExternalContext();

    /**
     * 外部コンテキストを設定します。
     * <p>
     * {@link ExternalContext 外部コンテキスト}は、
     * {@link InstanceDef#APPLICATION_NAME application},
     * {@link InstanceDef#REQUEST_NAME request}，
     * {@link InstanceDef#SESSION_NAME session}など 各{@link InstanceDef インスタンス定義}を提供するものです。
     * これらのインスタンス定義を使用するには {@link ExternalContext 外部コンテキスト}を設定する必要があります。
     * </p>
     * 
     * @param externalContext
     *            <code>ExternalContext</code>
     */
    void setExternalContext(ExternalContext externalContext);

    /**
     * {@link ExternalContext 外部コンテキスト}が提供する コンポーネントを登録するオブジェクトを返します。
     * 
     * @return 外部コンテキストが提供するコンポーネントを登録するオブジェクト
     */
    ExternalContextComponentDefRegister getExternalContextComponentDefRegister();

    /**
     * {@link ExternalContext 外部コンテキスト}が提供する コンポーネントを登録するオブジェクトを設定します。
     * 
     * @param externalContextComponentDefRegister
     *            外部コンテキストが提供するコンポーネントを登録するオブジェクト
     */
    void setExternalContextComponentDefRegister(
            ExternalContextComponentDefRegister externalContextComponentDefRegister);

    /**
     * クラスローダーを返します。
     * 
     * @return クラスローダー
     */
    ClassLoader getClassLoader();

    /**
     * クラスローダーを設定します。
     * 
     * @param classLoader
     */
    void setClassLoader(ClassLoader classLoader);

    /**
     * 子コンテナ（<code>container</code>）に登録された コンポーネント定義（<code>componentDef</code>）をこのコンテナから検索できるよう
     * コンポーネント定義を管理するマップに登録します。
     * 
     * @param key
     *            キー
     * @param componentDef
     *            コンポーネント定義
     * @param container
     *            S2コンテナ
     */
    void registerMap(Object key, ComponentDef componentDef,
            S2Container container);
}