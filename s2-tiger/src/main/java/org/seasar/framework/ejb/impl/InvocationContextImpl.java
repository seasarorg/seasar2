/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBException;
import javax.interceptor.InvocationContext;

import org.aopalliance.intercept.MethodInvocation;

/**
 * {@link InvocationContext}の実装クラスです。
 * <p>
 * Seasar2はEJB3のインターセプタをAOP Alliance準拠のインターセプタ経由で呼び出すため、 このクラスはAOP
 * Alliance準拠のメソッド呼び出しコンテキストである{@link MethodInvocation}のラッパーとして実装されています。
 * </p>
 * 
 * @author koichik
 */
public class InvocationContextImpl implements InvocationContext {

    /** AOP Alliance準拠のメソッド呼び出しコンテキスト */
    protected MethodInvocation context;

    /** ライフサイクルコールバックの場合は{@code true} */
    protected boolean lifecycleCallback;

    /** コンテキスト情報 */
    protected Map<String, Object> contextData = new HashMap<String, Object>();

    /**
     * インスタンスを構築します。
     * 
     * @param context
     *            AOP Alliance準拠のメソッド呼び出しコンテキスト
     */
    public InvocationContextImpl(final MethodInvocation context) {
        this(context, false);
    }

    /**
     * 
     * インスタンスを構築します。
     * 
     * @param context
     *            AOP Alliance準拠のメソッド呼び出しコンテキスト
     * @param lifecycleCallback
     *            ライフサイクルコールバックの場合は{@code true}
     */
    public InvocationContextImpl(final MethodInvocation context,
            final boolean lifecycleCallback) {
        this.context = context;
        this.lifecycleCallback = lifecycleCallback;
    }

    public Object getTarget() {
        return context.getThis();
    }

    public Method getMethod() {
        return context.getMethod();
    }

    public Object[] getParameters() {
        if (lifecycleCallback) {
            throw new IllegalStateException();
        }
        return context.getArguments();
    }

    public void setParameters(final Object[] newParameters) {
        if (lifecycleCallback) {
            throw new IllegalStateException();
        }
        final Object[] oldParameters = getParameters();
        if (newParameters.length != oldParameters.length) {
            throw new EJBException();
        }
        System.arraycopy(newParameters, 0, oldParameters, 0,
                newParameters.length);
    }

    public Map<String, Object> getContextData() {
        return contextData;
    }

    public Object proceed() throws Exception {
        try {
            return context.proceed();
        } catch (final Exception e) {
            throw e;
        } catch (final Error e) {
            throw e;
        } catch (final Throwable t) {
            final EJBException e = new EJBException();
            e.initCause(t);
            throw e;
        }
    }
}
