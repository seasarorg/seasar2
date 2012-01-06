/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
 * コンポーネントのプロパティまたはフィールドにインジェクションする方法を定義するインターフェースです。
 * 
 * <p>
 * プロパティ定義は、diconファイルにおける<code>&lt;property&gt;</code>要素で指定されます。
 * <code>&lt;property&gt;</code>要素にはname属性とbindingType属性が含まれています。
 * </p>
 * <p>
 * <ul>
 * <li>name属性はコンポーネントのプロパティ名またはフィールド名を指定します。</li>
 * <li>bindingType属性はname属性にて指定されたプロパティまたはフィールドに、
 * S2コンテナ内に格納されているコンポーネントをバインディングする際の挙動を指定します。</li>
 * </ul>
 * </p>
 * <p>
 * <code>&lt;property&gt;</code>要素の内容に指定された式またはコンポーネントが、
 * <code>&lt;property&gt;</code>要素のname属性で指定されたプロパティまたはフィールドに設定されます。
 * </p>
 * <p>
 * プロパティ定義が存在する場合のプロパティインジェクションまたはフィールドインジェクションは、
 * diconファイルに記述されているプロパティ定義に従って行われます。
 * プロパティ定義が存在しない場合、{@link AutoBindingDef}の定義によって自動バインディングが行われる事があります。
 * </p>
 * 
 * 
 * @author higa
 * @author Maeno
 * 
 */
public interface PropertyDef extends ArgDef {

    /**
     * インジェクション対象となるプロパティ名またはフィールド名を返します。
     * 
     * @return 設定対象となるプロパティ名
     */
    public String getPropertyName();

    /**
     * バインディングタイプ定義を返します。
     * 
     * @return バインディングタイプ定義
     */
    public BindingTypeDef getBindingTypeDef();

    /**
     * バインディングタイプ定義を設定します。
     * 
     * @param bindingTypeDef
     *            バインディングタイプ定義
     */
    public void setBindingTypeDef(BindingTypeDef bindingTypeDef);

    /**
     * アクセスタイプ定義を返します。
     * 
     * @return アクセスタイプ定義
     */
    public AccessTypeDef getAccessTypeDef();

    /**
     * アクセスタイプ定義を設定します。
     * 
     * @param accessTypeDef
     *            アクセスタイプ定義
     */
    public void setAccessTypeDef(AccessTypeDef accessTypeDef);
}