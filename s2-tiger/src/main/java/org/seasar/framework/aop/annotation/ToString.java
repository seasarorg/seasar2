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
package org.seasar.framework.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.seasar.framework.aop.interceptors.ToStringInterceptor;

/**
 * メソッドに{@link ToStringInterceptor}を適用することを指定します。
 * 
 * @author koichik
 * @see ToStringInterceptor
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE, ElementType.METHOD })
@Interceptor
public @interface ToString {

    /**
     * {@link ToStringInterceptor}を適用するメソッドを選択するポイントカットです。
     * <p>
     * この注釈をクラスに指定した場合、ポイントカットに適合するメソッドに{@link ToStringInterceptor}が適用されます。
     * </p>
     * 
     * @return {@link ToStringInterceptor}を適用するメソッドを選択するポイントカット
     */
    String pointcut() default "";

}
