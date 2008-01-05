/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.framework.unit.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * コンポーネントに{@link org.seasar.framework.aop.interceptors.MockInterceptor}を適用することを示します。
 * 
 * @author nakamura
 * 
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface Mock {

    /**
     * インターセプタ適用対象のコンポーネントクラスです。
     */
    Class<?> target();

    /**
     * インターセプタ適用対象のコンポーネント名です。
     */
    String targetName() default "";

    /**
     * ポイントカットを表す正規表現です。
     */
    String pointcut() default "";

    /**
     * 戻り値を表す式です。
     */
    String returnValue() default "";

    /**
     * {@link Throwable}を表す式です。
     */
    String throwable() default "";

}
