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

import java.lang.reflect.Field;

import org.seasar.framework.beans.PropertyDesc;

/**
 * コンポーネントをインジェクションする時の動作を表すバインディングタイプを定義します。 バインディングタイプには、 以下のようなものがあります。
 * 
 * <dl>
 * <dt><code>must</code></dt>
 * <dd>自動バインディングが適用できなかった場合、 例外が発生します。</dd>
 * <dt><code>should</code></dt>
 * <dd>自動バインディングが適用できなかった場合、 警告を通知します。</dd>
 * <dt><code>may</code></dt>
 * <dd>自動バインディングが適用できなかった場合でも何もおきません。</dd>
 * <dt><code>none</code></dt>
 * <dd>{@link org.seasar.framework.container.AutoBindingDef 自動バインディングタイプ定義}が<code>auto</code>や<code>property</code>の場合でも自動バインディングを適用しません。</dd>
 * </dl>
 * 
 * @author higa
 * @author jundu (Javadoc)
 */
public interface BindingTypeDef {

    /**
     * バインディングタイプ「<code>must</code>」を表す定数です。
     */
    String MUST_NAME = "must";

    /**
     * バインディングタイプ「<code>should</code>」を表す定数です。
     */
    String SHOULD_NAME = "should";

    /**
     * バインディングタイプ「<code>may</code>」を表す定数です。
     */
    String MAY_NAME = "may";

    /**
     * バインディングタイプ「<code>none</code>」を表す定数です。
     */
    String NONE_NAME = "none";

    /**
     * バインディングタイプの文字列表現を返します。
     * 
     * @return バインディングタイプを表す文字列
     */
    String getName();

    /**
     * バインディングタイプに基づいて、 <code>component</code>に対してS2コンテナ上のコンポーネントをプロパティ経由でインジェクションします。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param propertyDef
     *            プロパティに対する設定方法や設定値の定義
     * @param propertyDesc
     *            対象となるコンポーネントのプロパティ情報
     * @param component
     *            対象となるコンポーネントのインスタンス
     */
    void bind(ComponentDef componentDef, PropertyDef propertyDef,
            PropertyDesc propertyDesc, Object component);

    /**
     * バインディングタイプに基づいて、 <code>component</code>に対してS2コンテナ上のコンポーネントをフィールドに直接インジェクションします。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param propertyDef
     *            プロパティに対する設定方法や設定値の定義
     * @param field
     *            対象となるコンポーネントのフィールド情報
     * @param component
     *            対象となるコンポーネントのインスタンス
     */
    void bind(ComponentDef componentDef, PropertyDef propertyDef, Field field,
            Object component);
}