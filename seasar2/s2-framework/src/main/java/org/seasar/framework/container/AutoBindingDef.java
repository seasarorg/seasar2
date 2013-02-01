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
package org.seasar.framework.container;

/**
 * 自動バインディングを適用する範囲を表す自動バインディング定義のインターフェースです。
 * <p>
 * 自動バインディング定義には、 以下のものがあります。
 * </p>
 * <dl>
 * <dt><code>auto</code></dt>
 * <dd>コンストラクタとプロパティの両方で、 自動バインディングを適用します。</dd>
 * <dt><code>semiauto</code></dt>
 * <dd>明示的に指定したプロパティに対してのみ自動バインディングを適用します。</dd>
 * <dt><code>constructor</code></dt>
 * <dd>コンストラクタの自動バインディングのみ適用します。</dd>
 * <dt><code>property</code></dt>
 * <dd>プロパティの自動バインディングのみ適用します。</dd>
 * <dt><code>none</code></dt>
 * <dd>すべての自動バインディングを適用しません。</dd>
 * </dl>
 * 
 * @author higa
 * @author jundu
 */
public interface AutoBindingDef {

    /**
     * 自動バインディング定義名「<code>auto</code>」を表す定数です。
     */
    String AUTO_NAME = "auto";

    /**
     * 自動バインディング定義名「<code>semiauto</code>」を表す定数です。
     */
    String SEMIAUTO_NAME = "semiauto";

    /**
     * 自動バインディング定義名「<code>constructor</code>」を表す定数です。
     */
    String CONSTRUCTOR_NAME = "constructor";

    /**
     * 自動バインディング定義名「<code>property</code>」を表す定数です。
     */
    String PROPERTY_NAME = "property";

    /**
     * 自動バインディング定義名「<code>none</code>」を表す定数です。
     */
    String NONE_NAME = "none";

    /**
     * 自動バインディング定義名を返します。
     * 
     * @return 自動バインディング定義名
     */
    String getName();

    /**
     * 自動バインディング定義に基づき、 <code>componentDef</code>に対する{@link ConstructorAssembler}を返します。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @return 自動バインディングの範囲が設定された{@link ConstructorAssembler}
     */
    ConstructorAssembler createConstructorAssembler(ComponentDef componentDef);

    /**
     * 自動バインディング定義に基づき、 <code>componentDef</code>に対する{@link PropertyAssembler}を返します。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @return 自動バインディングの範囲が設定された{@link PropertyAssembler}
     */
    PropertyAssembler createPropertyAssembler(ComponentDef componentDef);
}
