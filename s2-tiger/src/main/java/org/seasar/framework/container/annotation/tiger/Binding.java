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
package org.seasar.framework.container.annotation.tiger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * バインディング(Dependency Injection)の詳細を示します。
 * <p>
 * diconファイルの<code>&lt;property&gt;</code>要素で指定する項目を設定するためのアノテーションです。
 * </p>
 * 
 * @author higa
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD, ElementType.FIELD })
public @interface Binding {

    /**
     * バインディングするコンポーネントを示すOGNL式です。
     * 
     * @return バインディングするコンポーネントを示すOGNL式
     */
    String value() default "";

    /**
     * バインディングタイプです。
     * 
     * @return バインディングタイプ
     */
    BindingType bindingType() default BindingType.SHOULD;

}
