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
package org.seasar.framework.ejb;

import java.lang.reflect.Method;
import java.util.List;

import javax.ejb.TransactionAttributeType;

/**
 * EJB3セッションビーンのビジネスメソッドを表現するインターフェースです。
 * 
 * @author koichik
 */
public interface EJB3BusinessMethodDesc {

    /**
     * このビジネスメソッドの{@link Method}を返します。
     * 
     * @return このビジネスメソッドの{@link Method}
     */
    Method getMethod();

    /**
     * このビジネスメソッドの{@link TransactionAttributeType}を返します。
     * 
     * @return このビジネスメソッドの{@link TransactionAttributeType}
     */
    TransactionAttributeType getTransactionAttributeType();

    /**
     * このビジネスメソッドに適用されるインターセプタ定義のリストを返します。
     * 
     * @return このビジネスメソッドに適用されるインターセプタ定義のリスト
     */
    List<EJB3InterceptorDesc> getInterceptors();

}
