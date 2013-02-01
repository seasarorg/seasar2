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
package org.seasar.framework.container.factory;

import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InstanceDef;

/**
 * Tigerアノテーションを読み取り、{@link ComponentDef}を作成するインターフェースです。
 * 
 * @author koichik
 */
public interface ComponentDefBuilder {

    /**
     * コンポーネント定義を作成して返します。
     * 
     * @param annotationHandler
     *            このメソッドを呼び出しているアノテーションハンドラ
     * @param componentClass
     *            コンポーネントのクラス
     * @param defaultInstanceDef
     *            デフォルトの{@link InstanceDef}
     * @param defaultAutoBindingDef
     *            デフォルトの{@link AutoBindingDef}
     * @param defaultExternalBinding
     *            デフォルトの外部バインディグ
     * @return コンポーネント定義
     */
    ComponentDef createComponentDef(AnnotationHandler annotationHandler,
            Class<?> componentClass, InstanceDef defaultInstanceDef,
            AutoBindingDef defaultAutoBindingDef, boolean defaultExternalBinding);

}
