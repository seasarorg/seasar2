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
package org.seasar.framework.ejb.impl;

import java.util.ArrayList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import org.seasar.framework.aop.intertype.AbstractInterType;

/**
 * EJB3インターセプタをサポートするためのインタータイプです。
 * <p>
 * EJB3のインターセプタとAOP Allianceに準拠したSeasar2のインターセプタは互換性がないため、 EJB3のインターセプタはAOP
 * Alliance準拠の{@link EJB3InterceptorSupportInterceptor}を介して呼び出されます。
 * そのために必要な情報を保持するために、 セッションビーンをエンハンスします。
 * </p>
 * 
 * @author koichik
 */
public class EJB3InterceptorSupportInterType extends AbstractInterType {

    /** セッションビーンに適用するEJB3インターセプタの{@link List} */
    protected List<Class<?>> interceptorClasses = new ArrayList<Class<?>>();

    /**
     * インスタンスを構築します。
     */
    public EJB3InterceptorSupportInterType() {
    }

    /**
     * EJB3インターセプタを追加します。
     * 
     * @param interceptorClass
     *            EJB3インターセプタ
     */
    public void addInterceptor(final Class<?> interceptorClass) {
        interceptorClasses.add(interceptorClass);
    }

    /**
     * EJB3インターセプタを一つ以上持っている場合は{@code true}を返します。
     * 
     * @return EJB3インターセプタを一つ以上持っている場合は{@code true}
     */
    public boolean hasInterceptor() {
        return !interceptorClasses.isEmpty();
    }

    @Override
    protected void introduce() throws CannotCompileException, NotFoundException {
        for (final Class<?> clazz : interceptorClasses) {
            final String name = getFieldName(clazz);
            addField(clazz, name);
            addMethod("set" + name, new Class[] { clazz }, "this." + name
                    + "=$1;");
        }
    }

    /**
     * EJB3インターセプタクラスのインスタンスを保持するセッションビーンのフィールド名を返します。
     * 
     * @param clazz
     *            EJB3インターセプタのクラス
     * @return EJB3インターセプタクラスのインスタンスを保持するセッションビーンのフィールド名
     */
    public static String getFieldName(final Class<?> clazz) {
        return "$$S2EJB3$$" + clazz.getName().replace('.', '$') + "$$";
    }

}
