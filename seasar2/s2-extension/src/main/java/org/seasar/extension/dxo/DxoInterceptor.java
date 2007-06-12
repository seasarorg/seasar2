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
package org.seasar.extension.dxo;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.extension.dxo.command.DxoCommand;
import org.seasar.extension.dxo.meta.DxoMetadata;
import org.seasar.extension.dxo.meta.DxoMetadataFactory;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.framework.util.MethodUtil;

/**
 * S2Dxoの機能を提供するインターセプタです。
 * 
 * @author koichik
 */
public class DxoInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 1L;

    /** Dxoメタデータファクトリ */
    protected DxoMetadataFactory metadataFactory;

    /**
     * インスタンスを構築します。
     * 
     * @param metadataFactory
     *            Dxoメタデータファクトリ
     */
    public DxoInterceptor(final DxoMetadataFactory metadataFactory) {
        this.metadataFactory = metadataFactory;
    }

    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final Method method = invocation.getMethod();
        if (MethodUtil.isAbstract(method)) {
            final Class targetClass = getTargetClass(invocation);
            final DxoMetadata metadata = metadataFactory
                    .getMetadata(targetClass);
            final DxoCommand command = metadata.getCommand(method);
            if (command != null) {
                return command.execute(invocation.getArguments());
            }
        }
        return invocation.proceed();
    }

}
