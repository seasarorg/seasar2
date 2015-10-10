/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
 * S2コンテナで使用される定数を定義するインターフェースです。
 * <p>
 * セパレータ文字や定義済みコンポーネントキー(コンポーネント名)などの定数を定義しています。
 * </p>
 * 
 * @author higa
 * @author belltree
 */
public interface ContainerConstants {

    /**
     * 名前空間とコンポーネント名の区切り(char)を表す定数です。
     */
    char NS_SEP = '.';

    /**
     * パッケージ名(Javaのパッケージとは異なる)付きコンポーネント名における、
     * パッケージ名と自動バインディング用コンポーネント名の区切り(char)を表す定数です。
     */
    char PACKAGE_SEP = '_';

    /**
     * 名前空間とコンポーネント名の区切り(String)を表す定数です。
     */
    String NS_SEP_STR = Character.toString(NS_SEP);

    /**
     * S2コンテナのコンポーネントキーを表す定数です。
     * 
     * @see org.seasar.framework.container.impl.S2ContainerImpl
     */
    String CONTAINER_NAME = "container";

    /**
     * 外部コンテキストが提供するリクエストオブジェクトを取得するための、 コンポーネントキーを表す定数です。
     * 
     * @see org.seasar.framework.container.external.RequestComponentDef
     * @see org.seasar.framework.container.external.servlet.HttpServletRequestComponentDef
     * @see org.seasar.framework.container.external.portlet.PortletRequestComponentDef
     */
    String REQUEST_NAME = "request";

    /**
     * 外部コンテキストが提供するレスポンスオブジェクトを取得するための、 コンポーネントキーを表す定数です。
     * 
     * @see org.seasar.framework.container.external.servlet.HttpServletResponseComponentDef
     * @see org.seasar.framework.container.external.portlet.PortletResponseComponentDef
     */
    String RESPONSE_NAME = "response";

    /**
     * 外部コンテキストが提供するセッションオブジェクトを取得するため、 コンポーネントキーを表す定数です。
     * 
     * @see org.seasar.framework.container.external.servlet.HttpSessionComponentDef
     * @see org.seasar.framework.container.external.portlet.PortletSessionComponentDef
     */
    String SESSION_NAME = "session";

    /**
     * 外部コンテキストが提供するアプリケーションコンテキストを取得するための、 コンポーネントキーを表す定数です。
     * 
     * @see org.seasar.framework.container.external.servlet.ServletContextComponentDef
     * @see org.seasar.framework.container.external.portlet.PortletContextComponentDef
     */
    String SERVLET_CONTEXT_NAME = "application";

    /**
     * 外部コンテキストが提供するアプリケーションスコープを{@link java.util.Map}インターフェースで取得するための、
     * コンポーネントキーを表す定数です。
     * 
     * @see org.seasar.framework.container.external.ApplicationComponentDef
     * @see org.seasar.framework.container.external.ApplicationMapComponentDef
     */
    String APPLICATION_SCOPE = "applicationScope";

    /**
     * 外部コンテキストの初期化パラメータを{@link java.util.Map}インターフェースで取得するための、
     * コンポーネントキーを表す定数です。
     * 
     * @see org.seasar.framework.container.external.InitParameterMapComponentDef
     */
    String INIT_PARAM = "initParam";

    /**
     * 外部コンテキストが提供するセッションスコープを{@link java.util.Map}インターフェースで取得するための、
     * コンポーネントキーを表す定数です。
     * 
     * @see org.seasar.framework.container.external.SessionMapComponentDef
     */
    String SESSION_SCOPE = "sessionScope";

    /**
     * 外部コンテキストが提供するリクエストコープを{@link java.util.Map}インターフェースで取得するための、
     * コンポーネントキーを表す定数です。
     * 
     * @see org.seasar.framework.container.external.RequestMapComponentDef
     */
    String REQUEST_SCOPE = "requestScope";

    /**
     * 外部コンテキストが提供するクッキー(cookie)の内容を{@link java.util.Map}インターフェースで取得するための、
     * コンポーネントキーを表す定数です。
     * 
     * @see org.seasar.framework.container.external.servlet.CookieMapComponentDef
     */
    String COOKIE = "cookie";

    /**
     * 外部コンテキストが提供するリクエストヘッダの内容を{@link java.util.Map}インターフェースで取得するための、
     * コンポーネントキーを表す定数です。
     * 
     * @see org.seasar.framework.container.external.RequestHeaderMapComponentDef
     */
    String HEADER = "header";

    /**
     * 外部コンテキストが提供するリクエストヘッダの内容を値の配列で取得するための、 コンポーネントキーを表す定数です。
     * 
     * @see org.seasar.framework.container.external.RequestHeaderValuesMapComponentDef
     */
    String HEADER_VALUES = "headerValues";

    /**
     * 外部コンテキストが提供するリクエストパラメータの内容を{@link java.util.Map}インターフェースで取得するための、
     * コンポーネントキーを表す定数です。
     * 
     * @see org.seasar.framework.container.external.RequestParameterMapComponentDef
     */
    String PARAM = "param";

    /**
     * 外部コンテキストが提供するリクエストパラメータの内容を値の配列で取得するための、 コンポーネントキーを表す定数です。
     * 
     * @see org.seasar.framework.container.external.RequestParameterValuesMapComponentDef
     */
    String PARAM_VALUES = "paramValues";

    /**
     * {@link ComponentDef コンポーネント定義}を{@link java.util.Map}に保持する場合などに、
     * キーとして使用する定数です。
     * 
     * @see org.seasar.framework.container.util.AopProxyUtil#getConcreteClass(ComponentDef)
     */
    String COMPONENT_DEF_NAME = "componentDef";

    /**
     * コンフィグレーションオブジェクト(設定情報を内包するオブジェクト)を取得するための、 コンポーネントキーを表す定数です。
     * 
     * @see org.seasar.framework.container.external.portlet.PortletConfigComponentDef
     */
    String CONFIG_NAME = "config";
}
