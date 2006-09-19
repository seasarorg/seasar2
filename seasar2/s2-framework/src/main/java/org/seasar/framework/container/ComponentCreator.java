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
 * Smart deployにおいて、 自動登録されるコンポーネントの{@link ComponentDef コンポーネント定義}を作成するためのインターフェースです。
 * <p>
 * コンポーネント定義は{@link org.seasar.framework.convention.NamingConvention 命名規約}に基づいて作成され、
 * {@link ComponentCustomizer コンポーネント定義カスタマイザ}によってアスペクト定義の追加などのカスタマイズを施してから返却されます。
 * </p>
 * 
 * @author higa
 * @author jundu (Javadoc)
 */
public interface ComponentCreator {

    /**
     * 指定されたクラスから、 {@link org.seasar.framework.convention.NamingConvention 命名規約}に従ってコンポーネント定義を作成します。
     * 
     * @param componentClass
     *            コンポーネント定義を作成する対象のクラス
     * @return 作成されたコンポーネント定義。 指定されたクラスがこのCreatorの対象でなかった場合は、 <code>null</code>を返す
     */
    ComponentDef createComponentDef(Class componentClass);

    /**
     * 指定されたコンポーネント名から、
     * {@link org.seasar.framework.convention.NamingConvention 命名規約}に従ってコンポーネント定義を作成します。
     * 
     * @param componentName
     *            コンポーネント定義を作成する対象のコンポーネント名
     * @return 作成されたコンポーネント定義。 指定されたクラスがこのCreatorの対象でなかった場合、
     *         またはコンポーネント名に対応するクラスが存在しなかった場合は、 <code>null</code>を返す
     * @throws org.seasar.framework.exception.EmptyRuntimeException
     *             コンポーネント名に<code>null</code>または空文字列を指定した場合
     * 
     * @see org.seasar.framework.convention.NamingConvention#fromComponentNameToClass(String)
     */
    ComponentDef createComponentDef(String componentName);
}