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
package org.seasar.framework.container;

/**
 * <p>
 * コンポーネントのインスタンスをS2コンテナ上でどのように管理するのかを定義します。
 * </p>
 * <p>
 * インスタンス定義の種類には、以下のものがあります。
 * <dl>
 * <dt><code>singleton</code>(default)</dt>
 * <dd>S2コンテナ上で唯一のインスタンスになります。</dd>
 * <dt><code>prototype</code></dt>
 * <dd>コンポーネントが必要とされる度に異なるインスタンスになります。</dd>
 * <dt><code>application</code></dt>
 * <dd>アプリケーションコンテキスト毎に1つのインスタンスになります。</dd>
 * <dt><code>request</code></dt>
 * <dd>リクエストコンテキスト毎に1つのインスタンスになります。</dd>
 * <dt><code>session</code></dt>
 * <dd>セッションコンテキスト毎に1つのインスタンスになります。</dd>
 * <dt><code>outer</code></dt>
 * <dd>コンポーネントのインスタンスは{@link S2Container}の外で生成し、 インジェクションだけを行ないます。
 * アスペクト、コンストラクタ・インジェクションは適用できません。</dd>
 * </dl>
 * それぞれ、 インスタンスが生成されるタイミングは、そのコンポーネントが必要とされる時になります。 また、
 * その時点で存在する「コンテキスト」に属するコンポーネントのみインジェクションが可能です。
 * </p>
 * <p>
 * インスタンス定義の指定方法には、以下のものがあります。
 * <dl>
 * <dt>diconファイル</dt>
 * <dd><code>&lt;component&gt;</code>の<code>instance</code>属性で指定します。</dd>
 * <dt>Tigerアノテーション</dt>
 * <dd><code>&#064;</code>{@link org.seasar.framework.container.annotation.tiger.Component}の<code>instance</code>値で指定します。</dd>
 * </dl>
 * インスタンス定義を省略した場合は<code>singleton</code>を指定したことになります。
 * </p>
 * <p>
 * <code>application</code>、 <code>request</code>、 <code>session</code>を使う場合は、
 * {@link S2Container#init()}を行なう前に{@link ExternalContext}をS2コンテナに設定する必要があります。
 * </p>
 * <p>
 * Webコンテナ用には{@link org.seasar.framework.container.external.servlet.HttpServletExternalContext}が用意されています。
 * {@link org.seasar.framework.container.servlet.S2ContainerListener}、
 * {@link org.seasar.framework.container.servlet.S2ContainerServlet}のいずれかと{@link org.seasar.framework.container.filter.S2ContainerFilter}をweb.xmlに設定すれば、
 * {@link org.seasar.framework.container.external.servlet.HttpServletExternalContext}がS2コンテナに設定され、
 * <code>application</code>、 <code>request</code>、 <code>session</code>を使うことが出来るようになります。
 * </p>
 * 
 * @author higa
 * @author goto
 */
public interface InstanceDef {

    /**
     * インスタンス定義「<code>singleton</code>」を表す定数です。
     */
    String SINGLETON_NAME = "singleton";

    /**
     * インスタンス定義「<code>prototype</code>」を表す定数です。
     */
    String PROTOTYPE_NAME = "prototype";

    /**
     * インスタンス定義「<code>application</code>」を表す定数です。
     */
    String APPLICATION_NAME = "application";

    /**
     * インスタンス定義「<code>request</code>」を表す定数です。
     */
    String REQUEST_NAME = "request";

    /**
     * インスタンス定義「<code>session</code>」を表す定数です。
     */
    String SESSION_NAME = "session";

    /**
     * インスタンス定義「<code>outer</code>」を表す定数です。
     */
    String OUTER_NAME = "outer";

    /**
     * インスタンス定義の文字列表現を返します。
     * 
     * @return インスタンス定義を表す文字列
     */
    String getName();

    /**
     * インスタンス定義に基づいた、コンポーネント定義<code>componentDef</code>の{@link ComponentDeployer}を返します。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @return {@link ComponentDeployer}オブジェクト
     */
    ComponentDeployer createComponentDeployer(ComponentDef componentDef);
}