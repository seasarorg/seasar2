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
package org.seasar.framework.beans.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.framework.beans.BeanDesc;

/**
 * 引数名を指定します。
 * <p>
 * このアノテーションを使用すると、Diiguによる引数名のエンハンス処理と互換性のある形式で引数名が.classファイルに保存されます
 * (正しくは、Diiguはこのアノテーションを指定した場合と同じ形式で引数名を.classファイルに保存します)。 Diiguを使用できない状況で{@link BeanDesc}から引数名を取得する必要がある場合に利用できます。
 * </p>
 * 
 * @author koichik
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ParameterName {

    /**
     * 引数の名前です。
     * 
     * @return 引数の名前
     */
    String value();

}
