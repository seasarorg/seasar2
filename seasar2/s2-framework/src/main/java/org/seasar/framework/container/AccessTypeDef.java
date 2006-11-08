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

/**
 * コンポーネントの状態に対するアクセスタイプを定義するインターフェースです。
 * <p>
 * 与えられたコンポーネントに対し、 アクセスタイプに基づいて、 S2コンテナ上のコンポーネントをインジェクションする機能も提供します。
 * </p>
 * <p>
 * アクセスタイプには、 以下のものがあります。
 * <dl>
 * <dt>{@link org.seasar.framework.container.assembler.AccessTypePropertyDef property}</dt>
 * <dd><var>getter</var>/<var>setter</var>メソッドによるアクセスを表します。</dd>
 * <dt>{@link org.seasar.framework.container.assembler.AccessTypeFieldDef field}</dt>
 * <dd>フィールドへの直接アクセスを表します。</dd>
 * </dl>
 * </p>
 * <p>
 * アクセスタイプ定義は、{@link org.seasar.framework.container.assembler.AccessTypeDefFactory ファクトリ}経由で取得します。
 * </p>
 * 
 * @author koichik
 * @author belltree
 * 
 * @see PropertyDef
 */
public interface AccessTypeDef {

    /**
     * アクセスタイプ{@link org.seasar.framework.container.assembler.AccessTypePropertyDef property}を表す定数です。
     */
    String PROPERTY_NAME = "property";

    /**
     * アクセスタイプ{@link org.seasar.framework.container.assembler.AccessTypeFieldDef field}を表す定数です。
     */
    String FIELD_NAME = "field";

    /**
     * アクセスタイプの文字列表現を返します。
     * 
     * @return アクセスタイプの文字列表現
     * 
     * @see #PROPERTY_NAME
     * @see #FIELD_NAME
     */
    String getName();

    /**
     * アクセスタイプに基づいて、 <code>component</code>のプロパティまたはフィールドにS2コンテナ上のコンポーネントをインジェクションします。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param propertyDef
     *            プロパティ定義
     * @param component
     *            コンポーネント
     */
    void bind(ComponentDef componentDef, PropertyDef propertyDef,
            Object component);

    /**
     * アクセスタイプに基づいて、 <code>component</code>のプロパティまたはフィールドにS2コンテナ上のコンポーネントをインジェクションします。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param propertyDef
     *            プロパティ定義
     * @param bindingTypeDef
     *            バインディングタイプ定義
     * @param component
     *            コンポーネント
     */
    void bind(ComponentDef componentDef, PropertyDef propertyDef,
            BindingTypeDef bindingTypeDef, Object component);
}
