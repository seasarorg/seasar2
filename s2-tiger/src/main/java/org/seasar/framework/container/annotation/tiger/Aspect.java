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
package org.seasar.framework.container.annotation.tiger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * クラスまたはメソッドにインターセプタを適用することを示します。
 * <p>
 * diconファイルの<code>&lt;aspect&gt;</code>要素で指定する項目を設定するためのアノテーションです。
 * </p>
 * <p>
 * この注釈がクラスまたはインターフェースに指定された場合で、 {@link #pointcut() ポイントカット}が指定された場合は、
 * ポイントカットに適合するメソッドにのみインターセプタが適用されます。 ポイントカットが指定されなかった場合は、
 * クラスが実装するインターフェースのメンバであるメソッドにのみインターセプタが適用されます。
 * </p>
 * <p>
 * この注釈がメソッドに指定された場合は、そのメソッドにインターセプタが適用されます。ポイントカットは無視されます。
 * </p>
 * 
 * @author higa
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
public @interface Aspect {

    /**
     * 適用するインターセプタを示すOGNL式です。
     * 
     * @return 適用するインターセプタを示すOGNL式
     */
    String value();

    /**
     * インターセプタを適用するメソッドを選択するポイントカットです。
     * 
     * @return インターセプタを適用するメソッドを選択するポイントカット
     */
    String pointcut() default "";

}
