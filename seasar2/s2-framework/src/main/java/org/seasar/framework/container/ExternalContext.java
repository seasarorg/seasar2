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

import java.util.Map;

/**
 * <p>
 * S2コンテナ上で、 Webコンテナなどの外部コンテキストを扱うためのインターフェースです。
 * </p>
 * <p>
 * {@link InstanceDef インスタンス定義}で、 <code>application</code>、
 * <code>request</code>、 <code>session</code>を使う場合、
 * {@link S2Container#init()}を行なう前に<code>ExternalContext</code>をS2コンテナに設定する必要があります。
 * </p>
 * 
 * @author higa
 * @author goto
 */
public interface ExternalContext {

    /**
     * リクエストコンテキストを返します。
     * 
     * @return リクエストコンテキスト
     * @see InstanceDef#REQUEST_NAME
     * @see org.seasar.framework.container.deployer.InstanceRequestDef
     */
    Object getRequest();

    /**
     * リクエストコンテキストを設定します。
     * 
     * @param request
     *            リクエストコンテキスト
     * @see InstanceDef#REQUEST_NAME
     * @see org.seasar.framework.container.deployer.InstanceRequestDef
     */
    void setRequest(Object request);

    /**
     * レスポンスコンテキストを返します。
     * 
     * @return レスポンスコンテキスト
     */
    Object getResponse();

    /**
     * レスポンスコンテキストを設定します。
     * 
     * @param response
     *            レスポンスコンテキスト
     */
    void setResponse(Object response);

    /**
     * セッションコンテキストを返します。
     * 
     * @return セッションコンテキスト
     * @see InstanceDef#SESSION_NAME
     * @see org.seasar.framework.container.deployer.InstanceSessionDef
     */
    Object getSession();

    /**
     * アプリケーションコンテキストを返します。
     * 
     * @return アプリケーションコンテキスト
     * @see InstanceDef#APPLICATION_NAME
     * @see org.seasar.framework.container.deployer.InstanceApplicationDef
     */
    Object getApplication();

    /**
     * アプリケーションコンテキストを設定します。
     * 
     * @param application
     *            アプリケーションコンテキスト
     * @see InstanceDef#APPLICATION_NAME
     * @see org.seasar.framework.container.deployer.InstanceApplicationDef
     */
    void setApplication(Object application);

    /**
     * アプリケーションコンテキストを{@link java.util.Map}インターフェースで返します。
     * 
     * @return アプリケーションコンテキスト
     * @see InstanceDef#APPLICATION_NAME
     * @see org.seasar.framework.container.deployer.InstanceApplicationDef
     */
    Map getApplicationMap();

    /**
     * 初期設定値を{@link java.util.Map}インターフェースで返します。
     * 
     * @return 初期設定値
     */
    Map getInitParameterMap();

    /**
     * セッションコンテキストを{@link java.util.Map}インターフェースで返します。
     * 
     * @return セッションコンテキスト
     * @see InstanceDef#SESSION_NAME
     * @see org.seasar.framework.container.deployer.InstanceSessionDef
     */
    Map getSessionMap();

    /**
     * リクエストクッキーを{@link java.util.Map}インターフェースで返します。
     * 
     * @return リクエストクッキー
     */
    Map getRequestCookieMap();

    /**
     * キーに対する値を1つ持つリクエストヘッダーを{@link java.util.Map}インターフェースで返します。
     * 
     * @return キーに対する値を1つ持つリクエストヘッダー
     */
    Map getRequestHeaderMap();

    /**
     * キーに対する値を複数持つリクエストヘッダーを{@link java.util.Map}インターフェースで返します。
     * 
     * @return キーに対する値を複数持つリクエストヘッダー
     */
    Map getRequestHeaderValuesMap();

    /**
     * リクエストコンテキストを{@link java.util.Map}インターフェースで返します。
     * 
     * @return リクエストコンテキスト
     * @see InstanceDef#REQUEST_NAME
     * @see org.seasar.framework.container.deployer.InstanceRequestDef
     */
    Map getRequestMap();

    /**
     * キーに対する値を1つ持つリクエストパラメータを{@link java.util.Map}インターフェースで返します。
     * 
     * @return キーに対する値を1つ持つリクエストパラメータ
     */
    Map getRequestParameterMap();

    /**
     * キーに対する値を複数持つリクエストパラメータを{@link java.util.Map}インターフェースで返します。
     * 
     * @return キーに対する値を複数持つリクエストパラメータ
     */
    Map getRequestParameterValuesMap();
}
