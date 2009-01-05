/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
 * 式を表わすインターフェースです。
 * </p>
 * <p>
 * 式とは、 オブジェクトの生成、 プロパティへのアクセス、 メソッドの呼び出し、 定義済みオブジェクトの指定、 リテラルの記述、
 * 演算、などが出来る表現方法です。 また、 式の実装によっては(OGNLでは)変数の使用なども出来ます。
 * </p>
 * <p>
 * diconファイルの<code>&lt;property&gt;</code>、 <code>&lt;component&gt;</code>、
 * <code>&lt;initMethod&gt;</code>、 <code>&lt;destroyMethod&gt;</code>、
 * <code>&lt;arg&gt;</code>、 <code>&lt;meta&gt;</code>、 に式を記述することが出来ます。
 * </p>
 * <p>
 * 定義済みオブジェクトは、 以下のものがあります。
 * <dl>
 * <dt>{@link ContainerConstants#CONTAINER_NAME container}</dt>
 * <dd>現在のdiconファイルを処理しているS2コンテナです。</dd>
 * <dt>{@link ContainerConstants#REQUEST_NAME request}</dt>
 * <dd>Webコンテナなどで実行されている場合、 現在のスレッドで処理しているリクエストです。</dd>
 * <dt>{@link ContainerConstants#RESPONSE_NAME response}</dt>
 * <dd>Webコンテナなどで実行されている場合、 現在のスレッドで処理しているレスポンスです。</dd>
 * <dt>{@link ContainerConstants#SESSION_NAME session}</dt>
 * <dd>Webコンテナなどで実行されている場合、 現在のスレッドで処理しているセッションです。</dd>
 * <dt>{@link ContainerConstants#SERVLET_CONTEXT_NAME application}</dt>
 * <dd>Webコンテナなどで実行されている場合、 現在のS2コンテナに関連づけられたアプリケーションコンテキストです。</dd>
 * </dl>
 * 定義済みオブジェクトの他にも、 S2コンテナに登録されているコンポーネントを<code>name</code>属性で参照することが出来ます。
 * </p>
 * 
 * @author koichik
 * @author goto
 */
public interface Expression {

    /**
     * 式を評価した結果を返します。
     * 
     * @param container
     *            式を評価するコンテキストとなるS2コンテナ
     * @param context
     *            式を評価するコンテキストに追加できるコンテキスト
     * @return 式を評価した結果
     */
    Object evaluate(S2Container container, Map context);
}
