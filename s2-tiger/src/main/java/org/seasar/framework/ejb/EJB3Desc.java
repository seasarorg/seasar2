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
package org.seasar.framework.ejb;

import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.interceptor.AroundInvoke;

/**
 * EJB3のセッションビーンを表現するインターフェースです。
 * 
 * @author koichik
 */
public interface EJB3Desc {

    /**
     * このセッションビーンがステートレスなら{@code true}を返します。
     * 
     * @return このセッションビーンがステートレスなら{@code true}
     */
    boolean isStateless();

    /**
     * このセッションビーンがステートフルなら{@code true}を返します。
     * 
     * @return このセッションビーンがステートフルなら{@code true}
     */
    boolean isStateful();

    /**
     * このセッションビーンの名前を返します。
     * 
     * @return このセッションビーンの名前
     * @see Stateless#name()
     * @see Stateful#name()
     */
    String getName();

    /**
     * このセッションビーンのクラスを返します。
     * 
     * @return このセッションビーンのクラス
     */
    Class<?> getBeanClass();

    /**
     * このセッションビーンが実装するビジネスインターフェースの{@link List}を返します。
     * 
     * @return このセッションビーンが実装するビジネスインターフェースの{@link List}
     */
    List<Class<?>> getBusinessInterfaces();

    /**
     * このセッションビーンがコンテナ管理トランザクションを使用する場合は{@code true}を返します。
     * 
     * @return このセッションビーンがコンテナ管理トランザクションを使用する場合は{@code true}
     */
    boolean isCMT();

    /**
     * このセッションビーンに適用されるインターセプタ定義の{@link List}を返します。
     * 
     * @return このセッションビーンに適用されるインターセプタ定義の{@link List}
     */
    List<EJB3InterceptorDesc> getInterceptors();

    /**
     * {@code method}に対応するビジネスメソッド定義を返します。
     * <p>
     * {@code method}に対応するビジネスメソッドが存在しない場合は{@code null}を返します。
     * </p>
     * 
     * @param method
     *            このセッションビーンのメソッド
     * @return {@code method}に対応するビジネスメソッド定義
     */
    EJB3BusinessMethodDesc getBusinessMethod(Method method);

    /**
     * このセッションビーンの全てのビジネスメソッド定義の{@link List}を返します。
     * 
     * @return このセッションビーンの全てのビジネスメソッド定義の{@link List}
     */
    List<EJB3BusinessMethodDesc> getBusinessMethods();

    /**
     * {@link AroundInvoke}で注釈されたメソッドの{@link List}を返します。
     * <p>
     * このセッションビーンに{@link AroundInvoke}で注釈されたメソッドが存在しない場合は空の{@link List}を返します。
     * </p>
     * 
     * @return {@link AroundInvoke}で注釈されたメソッドのリスト
     */
    List<Method> getAroundInvokeMethods();

    /**
     * {@link PostConstruct}で注釈されたメソッドの{@link List}を返します。
     * <p>
     * このセッションビーンに{@link PostConstruct}で注釈されたメソッドが存在しない場合は空の{@link List}を返します。
     * </p>
     * 
     * @return {@link PostConstruct}で注釈されたメソッドのリスト
     */
    List<Method> getPostConstructMethods();

}
