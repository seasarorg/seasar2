/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.framework.ejb.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.ExcludeClassInterceptors;
import javax.interceptor.Interceptors;

import org.seasar.framework.ejb.EJB3BusinessMethodDesc;
import org.seasar.framework.ejb.EJB3Desc;
import org.seasar.framework.ejb.EJB3InterceptorDesc;

/**
 * EJB3セッションビーンのビジネスメソッドを表現するクラスです。
 * 
 * @author koichik
 */
public class EJB3BusinessMethodDescImpl implements EJB3BusinessMethodDesc {

    /** このビジネスメソッドを含むEJB3セッションビーン定義 */
    protected EJB3Desc ejb3desc;

    /** このビジネスメソッドの{@link Method} */
    protected Method method;

    /** このビジネスメソッドの{@link TransactionAttributeType} */
    protected TransactionAttributeType transactionAttributeType;

    /** このビジネスメソッドに適用されるインターセプタ定義の{@link List} */
    protected List<EJB3InterceptorDesc> interceptors = new ArrayList<EJB3InterceptorDesc>();

    /**
     * インスタンスを構築します。
     * 
     * @param ejb3desc
     *            このビジネスメソッドを含むEJB3セッションビーン定義
     * @param method
     *            このビジネスメソッドの{@link Method}
     */
    public EJB3BusinessMethodDescImpl(final EJB3Desc ejb3desc,
            final Method method) {
        this.ejb3desc = ejb3desc;
        this.method = method;
        detectTransactionAttribute();
        detectInterceptors();
    }

    public Method getMethod() {
        return method;
    }

    public TransactionAttributeType getTransactionAttributeType() {
        return transactionAttributeType;
    }

    public List<EJB3InterceptorDesc> getInterceptors() {
        return interceptors;
    }

    /**
     * ビジネスメソッドの{@link TransactionAttributeType}を検出します．
     */
    protected void detectTransactionAttribute() {
        if (!ejb3desc.isCMT()) {
            return;
        }
        TransactionAttribute attribute = method
                .getAnnotation(TransactionAttribute.class);
        if (attribute == null) {
            final Class<?> declaringClass = method.getDeclaringClass();
            attribute = declaringClass
                    .getAnnotation(TransactionAttribute.class);
            if (attribute == null) {
                transactionAttributeType = TransactionAttributeType.REQUIRED;
                return;
            }
        }
        transactionAttributeType = attribute.value();
    }

    /**
     * このビジネスメソッドに適用されるインターセプタを検出します。
     * 
     */
    protected void detectInterceptors() {
        final ExcludeClassInterceptors exclude = method
                .getAnnotation(ExcludeClassInterceptors.class);
        if (exclude == null) {
            interceptors.addAll(ejb3desc.getInterceptors());
        }

        final Interceptors annotation = method
                .getAnnotation(Interceptors.class);
        if (annotation == null) {
            return;
        }

        for (final Class<?> interceptorClass : annotation.value()) {
            interceptors.add(new EJB3InterceptorDescImpl(ejb3desc,
                    interceptorClass));
        }
    }

}
