/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.factory.aspect;

import java.lang.reflect.Method;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptors;

import org.aopalliance.intercept.MethodInterceptor;
import org.seasar.framework.aop.impl.PointcutImpl;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.factory.AnnotationHandler;
import org.seasar.framework.container.factory.AspectDefFactory;
import org.seasar.framework.ejb.EJB3BusinessMethodDesc;
import org.seasar.framework.ejb.EJB3Desc;
import org.seasar.framework.ejb.EJB3DescFactory;
import org.seasar.framework.ejb.EJB3InterceptorDesc;
import org.seasar.framework.ejb.impl.AroundInvokeSupportInterceptor;
import org.seasar.framework.ejb.impl.EJB3InterceptorSupportInterceptor;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * EJB3の{@link TransactionAttribute}、{@link Interceptors}、{@link AroundInvoke}アノテーションを読み取り{@link AspectDef}を作成するコンポーネントの実装クラスです。
 * 
 * @author koichik
 */
public class EJB3AnnotationAspectDefBuilder extends AbstractAspectDefBuilder {

    private static final Map<TransactionAttributeType, String> txInterceptors = CollectionsUtil
            .newHashMap();
    static {
        txInterceptors.put(TransactionAttributeType.MANDATORY,
                "ejb3tx.mandatoryTx");
        txInterceptors.put(TransactionAttributeType.REQUIRED,
                "ejb3tx.requiredTx");
        txInterceptors.put(TransactionAttributeType.REQUIRES_NEW,
                "ejb3tx.requiresNewTx");
        txInterceptors.put(TransactionAttributeType.NOT_SUPPORTED,
                "ejb3tx.notSupportedTx");
        txInterceptors.put(TransactionAttributeType.NEVER, "ejb3tx.neverTx");
    }

    public void appendAspectDef(final AnnotationHandler annotationHandler,
            final ComponentDef componentDef) {
        final Class<?> componentClass = componentDef.getComponentClass();
        if (componentClass == null) {
            return;
        }

        final EJB3Desc ejb3desc = EJB3DescFactory.getEJB3Desc(componentClass);
        if (ejb3desc == null) {
            return;
        }

        appendEJB3TxAspect(componentDef, ejb3desc);
        appendEJB3InterceptorsAspect(componentDef, ejb3desc);
        appendEJB3AroundInvokeAspect(componentDef, ejb3desc);
    }

    /**
     * {@link TransactionAttribute}アノテーションを読み取り{@link AspectDef アスペクト定義}を作成して{@link ComponentDef コンポーネント定義}に追加します。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param ejb3desc
     *            {@link EJB3Desc}
     */
    protected void appendEJB3TxAspect(final ComponentDef componentDef,
            final EJB3Desc ejb3desc) {
        if (!ejb3desc.isCMT()) {
            return;
        }

        for (final EJB3BusinessMethodDesc methodDesc : ejb3desc
                .getBusinessMethods()) {
            final String txInterceptor = txInterceptors.get(methodDesc
                    .getTransactionAttributeType());
            if (txInterceptor == null) {
                continue;
            }
            appendAspect(componentDef, txInterceptor, methodDesc.getMethod());
        }
    }

    /**
     * {@link Interceptors}アノテーションを読み取り{@link AspectDef アスペクト定義}を作成して{@link ComponentDef コンポーネント定義}に追加します。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param ejb3desc
     *            {@link EJB3Desc}
     */
    protected void appendEJB3InterceptorsAspect(
            final ComponentDef componentDef, final EJB3Desc ejb3desc) {
        for (final EJB3BusinessMethodDesc methodDesc : ejb3desc
                .getBusinessMethods()) {
            for (final EJB3InterceptorDesc interceptorDesc : methodDesc
                    .getInterceptors()) {
                for (final Method interceptorMethod : interceptorDesc
                        .getInterceptorMethods()) {
                    final EJB3InterceptorSupportInterceptor interceptor = new EJB3InterceptorSupportInterceptor(
                            interceptorDesc.getInterceptorClass(),
                            interceptorMethod);
                    final AspectDef aspectDef = AspectDefFactory
                            .createAspectDef(interceptor, new PointcutImpl(
                                    methodDesc.getMethod()));
                    componentDef.addAspectDef(aspectDef);
                }
            }
        }
    }

    /**
     * {@link AroundInvoke}アノテーションを読み取り{@link AspectDef アスペクト定義}を作成して{@link ComponentDef コンポーネント定義}に追加します。
     * 
     * @param componentDef
     *            コンポーネント定義
     * @param ejb3desc
     *            {@link EJB3Desc}
     */
    protected void appendEJB3AroundInvokeAspect(
            final ComponentDef componentDef, final EJB3Desc ejb3desc) {
        for (final Method aroundInvokeMethod : ejb3desc
                .getAroundInvokeMethods()) {
            final MethodInterceptor interceptor = new AroundInvokeSupportInterceptor(
                    aroundInvokeMethod);
            for (final EJB3BusinessMethodDesc methodDesc : ejb3desc
                    .getBusinessMethods()) {
                final AspectDef aspectDef = AspectDefFactory.createAspectDef(
                        interceptor, new PointcutImpl(methodDesc.getMethod()));
                componentDef.addAspectDef(aspectDef);
            }
        }
    }

}
