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
package org.seasar.framework.container.factory;

import java.lang.reflect.Field;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.PropertyDef;

/**
 * クラスに指定されたアノテーションからコンポーネント定義を作成するためのインターフェースです。 
 * <p>
 * このアノテーションにより、コンポーネント名やインスタンス属性， プロパティやアスペクト，初期化メソッド等の設定が可能です。 
 * </p>
 * <p>
 * 指定されるの定義の優先度について。 <br>
 * 以下の順によって指定された定義が上書きされます。注意してください。 
 * </p>
 * <ul>
 * <li>デフォルト値
 * <li>アノテーション
 * <li>設定ファイル(diconファイル)
 * </ul>
 * 
 * @author vestige
 */
public interface AnnotationHandler {

    /**
     * クラス名、インスタンス定義を指定してコンポーネント定義を作成します。 
     * 
     * @param className
     *            クラス名
     * @param instanceDef
     *            インスタンス定義
     * @return コンポーネント定義
     */
    ComponentDef createComponentDef(String className, InstanceDef instanceDef);

    /**
     * クラス名、インスタンス定義、自動バインディング定義を指定してコンポーネント定義を作成します。 
     * 
     * @param className
     *            クラス名
     * @param instanceDef
     *            インスタンス定義
     * @param autoBindingDef
     *            自動バインディング定義
     * @return コンポーネント定義
     */
    ComponentDef createComponentDef(String className, InstanceDef instanceDef,
            AutoBindingDef autoBindingDef);
    /**
     * クラス名、インスタンス定義、自動バインディング定義、外部バインディングの有無を指定してコンポーネント定義を作成します。 
     * 
     * @param className
     *            クラス名
     * @param instanceDef
     *            インスタンス定義
     * @param autoBindingDef
     *            自動バインディング定義
     * @param externalBinding
     *            外部バインディングの有無
     * @return コンポーネント定義
     */
    ComponentDef createComponentDef(String className, InstanceDef instanceDef,
            AutoBindingDef autoBindingDef, boolean externalBinding);

    /**
     * コンポーネント名、インスタンス定義を指定してコンポーネント定義を作成します。 
     * 
     * @param componentClass
     *            コンポーネントクラス
     * @param instanceDef
     *            インスタンス定義
     * @return コンポーネント定義
     */
    ComponentDef createComponentDef(Class componentClass,
            InstanceDef instanceDef);

    /**
     * コンポーネントクラス、インスタンス定義、自動バインディング定義を指定してコンポーネント定義を作成します。 
     * 
     * @param componentClass
     *            コンポーネントクラス
     * @param instanceDef
     *            インスタンス定義
     * @param autoBindingDef
     *            自動バインディング定義
     * @return コンポーネント定義
     */
    ComponentDef createComponentDef(Class componentClass,
            InstanceDef instanceDef, AutoBindingDef autoBindingDef);

    /**
     * コンポーネントクラス、インスタンス定義、自動バインディング定義、外部バインディングを指定してコンポーネント定義を作成します。 
     * 
     * @param componentClass
     *            コンポーネントクラス
     * @param instanceDef
     *            インスタンス定義
     * @param autoBindingDef
     *            自動バインディング定義
     * @param externalBinding
     *            外部バインディング
     * @return コンポーネント定義
     */
    ComponentDef createComponentDef(Class componentClass,
            InstanceDef instanceDef, AutoBindingDef autoBindingDef,
            boolean externalBinding);

    /**
     * コンポーネント定義にプロパティ定義を追加します。 
     * 
     * @param componentDef
     *            コンポーネント定義
     */
    void appendDI(ComponentDef componentDef);

    /**
     * コンポーネント定義にアスペクト定義を追加します。 
     * 
     * @param componentDef
     *            コンポーネント定義
     */
    void appendAspect(ComponentDef componentDef);

    /**
     * コンポーネント定義にインタータイプ定義を追加します。 
     * 
     * @param componentDef
     *            コンポーネント定義
     */
    void appendInterType(ComponentDef componentDef);

    /**
     * コンポーネント定義に初期化メソッドを追加します。 
     * 
     * @param componentDef
     *            コンポーネント定義
     */
    void appendInitMethod(ComponentDef componentDef);

    /**
     * コンポーネント定義に初期化メソッドが追加可能かどうかを返します。 
     * 
     * @param cd
     *            コンポーネント定義
     * @param methodName
     *            初期化の対象にするメソッド名
     * @return 初期化メソッドとして追加可能な場合、<code>true</true>、そうでない場合は<code>false</code>を返す。
     */
    boolean isInitMethodRegisterable(ComponentDef cd, String methodName);

    /**
     * コンポーネント定義にdestroyMethodを追加します。 
     * 
     * @param componentDef
     *            コンポーネント定義
     */
    void appendDestroyMethod(ComponentDef componentDef);

    /**
     * コンポーネント定義にdestroyメソッドが追加可能かどうかを返します。 
     * 
     * @param cd
     *            コンポーネント定義
     * @param methodName
     *            初期化の対象にするメソッド名
     * @return destroyMethodとして追加可能な場合、<code>true</true>、そうでない場合は<code>false</code>を返す。
     */
    boolean isDestroyMethodRegisterable(ComponentDef cd, String methodName);

    /**
     * プロパティ名を指定してコンポーネント定義に追加するプロパティ定義を作成します。 
     * 
     * @param beanDesc
     *            プロパティに追加するBean名。
     * @param propertyDesc
     *            プロパティ名
     * @return プロパティ定義
     */
    PropertyDef createPropertyDef(BeanDesc beanDesc, PropertyDesc propertyDesc);

    /**
     * フィールドを指定してコンポーネント定義に追加するプロパティ定義を作成します。 
     * 
     * @param beanDesc
     *            プロパティに追加するBean名。
     * @param field
     *            フィールド名
     * @return プロパティ定義
     */
    PropertyDef createPropertyDef(BeanDesc beanDesc, Field field);

}
