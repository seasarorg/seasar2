/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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
 * S2コンテナで管理されるコンポーネントであることを示します。
 * <p>
 * diconファイルの<code>&lt;component&gt;</code>要素で指定する項目を設定するためのアノテーションです。
 * </p>
 * 
 * @author higa
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {

    /**
     * コンポーネントの名前です。
     * 
     * @return コンポーネントの名前
     */
    String name() default "";

    /**
     * インスタンスタイプです。
     * 
     * @return インスタンスタイプ
     */
    InstanceType instance() default InstanceType.SINGLETON;

    /**
     * 自動バインディングタイプです。
     * 
     * @return 自動バインディングタイプ
     */
    AutoBindingType autoBinding() default AutoBindingType.AUTO;

    /**
     * 外部バインディングを有効にする場合は{@code null}
     * 
     * @return 外部バインディングを有効にする場合は{@code null}
     */
    boolean externalBinding() default false;

}
