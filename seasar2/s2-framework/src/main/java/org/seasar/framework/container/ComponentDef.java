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
package org.seasar.framework.container;

import java.util.Set;

import org.seasar.framework.beans.BeanDesc;

/**
 * S2コンテナが管理するコンポーネントの定義を表すインターフェースです。
 * <p>
 * コンポーネント定義は、 コンポーネントの管理に必要な以下の情報を保持します。
 * <dl>
 * <dt>ライフサイクル</dt>
 * <dd>コンポーネントのスコープや、生成と消滅については、 このコンポーネントの{@link InstanceDef インスタンス定義}で設定します。
 * 生成については、 {@link Expression コンポーネント生成式}により指定することも可能です。</dd>
 * <dt>依存性注入(Dependency Injection)</dt>
 * <dd>このコンポーネントが依存する他のコンポーネントやパラメータは、 {@link ArgDef 引数定義}、
 * {@link InitMethodDef 初期化メソッド定義}、 {@link PropertyDef プロパティ定義}などにより設定します。
 * </dd>
 * <dt>アスペクト</dt>
 * <dd>このコンポーネントの{@link AspectDef アスペクト定義}により設定します。</dd>
 * <dt>メタデータ</dt>
 * <dd>{@link MetaDef メタデータ定義}により、 コンポーネントに付加情報を設定できます。 メタデータは、
 * 特殊なコンポーネントであることを識別する場合などに利用します。</dd>
 * </dl>
 * </p>
 * 
 * @author higa
 * @author belltree (Javadoc)
 * 
 * @see ArgDef
 * @see InterTypeDef
 * @see PropertyDef
 * @see InitMethodDef
 * @see DestroyMethodDef
 * @see AspectDef
 * @see MetaDef
 */
public interface ComponentDef extends ArgDefAware, InterTypeDefAware,
        PropertyDefAware, InitMethodDefAware, DestroyMethodDefAware,
        AspectDefAware, MetaDefAware {

    /**
     * 定義に基づいてコンポーネントを返します。
     * 
     * @return コンポーネント
     * @throws TooManyRegistrationRuntimeException
     *             コンポーネント定義が重複している場合
     * @throws CyclicReferenceRuntimeException
     *             コンポーネント間に循環参照がある場合
     * 
     * @see TooManyRegistrationComponentDef
     */
    Object getComponent() throws TooManyRegistrationRuntimeException,
            CyclicReferenceRuntimeException;

    /**
     * 外部コンポーネント<code>outerComponent</code>に対し、
     * {@link ComponentDef コンポーネント定義}に基づいて、 S2コンテナ上のコンポーネントをインジェクションします。
     * 
     * @param outerComponent
     *            外部コンポーネント
     */
    void injectDependency(Object outerComponent);

    /**
     * このコンポーネント定義を含むS2コンテナを返します。
     * 
     * @return S2コンテナ
     */
    S2Container getContainer();

    /**
     * このコンポーネント定義を含むS2コンテナを設定します。
     * 
     * @param container
     *            S2コンテナ
     */
    void setContainer(S2Container container);

    /**
     * 定義上のクラスを返します。
     * <p>
     * diconファイルの<code>&lt;component&gt;</code>タグにおける、 <code>class</code>属性で指定されたクラスを表します。
     * 自動バインディングされる際には、 このクラス(インターフェース)が使用されます。
     * </p>
     * 
     * @return 定義上のクラス
     */
    Class getComponentClass();

    /**
     * コンポーネント名を返します。
     * 
     * @return コンポーネント名
     */
    String getComponentName();

    /**
     * コンポーネント名を設定します。
     * 
     * @param componentName
     *            コンポーネント名
     */
    void setComponentName(String componentName);

    /**
     * アスペクト適用後の、 実際にインスタンス化されるコンポーネントのクラスを返します。
     * 
     * @return 実際のクラス
     */
    Class getConcreteClass();

    /**
     * 自動バインディング定義を返します。
     * 
     * @return 自動バインディング定義
     */
    AutoBindingDef getAutoBindingDef();

    /**
     * 自動バインディング定義を設定します。
     * 
     * @param autoBindingDef
     *            自動バインディング定義
     */
    void setAutoBindingDef(AutoBindingDef autoBindingDef);

    /**
     * インスタンス定義を返します。
     * 
     * @return インスタンス定義
     */
    InstanceDef getInstanceDef();

    /**
     * インスタンス定義を設定します。
     * 
     * @param instanceDef
     *            インスタンス定義
     */
    void setInstanceDef(InstanceDef instanceDef);

    /**
     * コンポーネントを生成する式を返します。
     * 
     * @return コンポーネント生成式
     */
    Expression getExpression();

    /**
     * コンポーネントを生成する式を設定します。
     * 
     * @param Expression
     *            コンポーネント生成式
     */
    void setExpression(Expression expression);

    /**
     * 外部バインディングが有効な場合<code>true</code>を返します。
     * <p>
     * 外部バインディングとは、 外部コンテキストにあるオブジェクトを、 指定したコンポーネントの対応するプロパティにバインディングする機能です。
     * </p>
     * <p>
     * Webアプリケーションにおいて、 {@link javax.servlet.http.HttpServletRequest リクエスト}コンテキストに入力された値を、
     * {@link org.seasar.framework.container.deployer.InstanceRequestDef リクエストインスタンス}を通して取得し、
     * リクエスト間(ページ間)で透過的に引き継ぐ場合などに利用されます。
     * </p>
     * 
     * @return 外部バインディングが有効な場合<code>true</code>
     * 
     * @see org.seasar.framework.container.assembler.AbstractPropertyAssembler#bindExternally(BeanDesc,
     *      ComponentDef, Object, Set)
     */
    boolean isExternalBinding();

    /**
     * 外部バインディングを有効にする場合<code>true</code>を設定します。
     * 
     * @param externalBinding
     *            外部バインディングを有効にする場合<code>true</code>
     */
    void setExternalBinding(boolean externalBinding);

    /**
     * コンポーネント定義を初期化します。
     * <p>
     * {@link InstanceDef コンポーネントインスタンス定義}が<code>singleton</code>の場合には、
     * {@link AspectDef アスペクト}を適用したインスタンスの生成、 配備、 プロパティ設定の後に、
     * {@link InitMethodDef initMethod}が呼ばれます。
     * </p>
     * 
     * @see org.seasar.framework.container.deployer.SingletonComponentDeployer#init()
     */
    void init();

    /**
     * コンポーネント定義を破棄します。
     * <p>
     * {@link InstanceDef コンポーネントインスタンス定義}が<code>singleton</code>の場合には、
     * {@link DestroyMethodDef destroyMethod}が呼ばれます。
     * </p>
     * 
     * @see org.seasar.framework.container.deployer.SingletonComponentDeployer#destroy()
     */
    void destroy();
}
