/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
 * S2コンテナ内に1つのキーで複数登録されたコンポーネントの定義を表すインターフェースです。
 * <p>
 * S2コンテナにコンポーネントが登録される際に、 そのキー(コンポーネントのクラス、
 * インターフェース、あるいは名前)に対応するコンポーネントがすでに登録されていると、 コンポーネント定義が<code>TooManyRegistrationComponentDef</code>になります。
 * </p>
 * <p>
 * <code>TooManyRegistrationComponentDef</code>で定義されているコンポーネントを取得しようとすると、
 * {@link TooManyRegistrationRuntimeException}がスローされます。
 * </p>
 * 
 * @author koichik
 * @author azusa
 * 
 * @see TooManyRegistrationRuntimeException
 */
public interface TooManyRegistrationComponentDef extends ComponentDef {

    /**
     * 同じキーで登録されたコンポーネント定義を追加します。
     * 
     * @param cd
     *            同じキーで登録されたコンポーネント定義
     */
    void addComponentDef(ComponentDef cd);

    /**
     * 複数登録されたコンポーネントの定義上のクラスの配列を返します。
     * 
     * @return 複数登録されたコンポーネントの定義上のクラスの配列
     */
    Class[] getComponentClasses();

    /**
     * 複数登録されたコンポーネント定義の配列を返します。
     * 
     * @return 複数登録されたコンポーネント定義の配列
     */
    ComponentDef[] getComponentDefs();
}
