/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
package org.seasar.framework.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * インターセプタを適用するためにメソッドに指定されるアノテーションを注釈する、 メタアノテーションです。
 * 
 * @author koichik
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Interceptor {

    /**
     * インターセプタのコンポーネント名です。
     * <p>
     * 省略された場合はこのアノテーションが付与されているアノテーションのクラス名をデキャピタライズした名前をコンポーネント名とします。
     * </p>
     * 
     * @return インターセプタのコンポーネント名
     */
    String value() default "";

}
