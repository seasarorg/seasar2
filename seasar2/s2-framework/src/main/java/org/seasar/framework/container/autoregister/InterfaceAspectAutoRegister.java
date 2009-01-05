/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.autoregister;

import org.aopalliance.intercept.MethodInterceptor;
import org.seasar.framework.aop.Pointcut;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.AspectDefFactory;

/**
 * 特定のインターフェースを実装しているクラスに対してアスペクトを自動登録するクラスです。
 * 
 * @author higa
 * 
 */
public class InterfaceAspectAutoRegister {

    /**
     * INIT_METHODアノテーションの定義です。
     */
    public static final String INIT_METHOD = "registerAll";

    private S2Container container;

    private MethodInterceptor interceptor;

    private Class targetInterface;

    private Pointcut pointcut;

    /**
     * コンテナを設定します。
     * 
     * @param container
     */
    public void setContainer(S2Container container) {
        this.container = container;
    }

    /**
     * インタセプタを設定します。
     * 
     * @param interceptor
     */
    public void setInterceptor(MethodInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    /**
     * ターゲットインターフェースを設定します。このインターフェースを実装したクラスにアスペクトが設定されます。
     * 
     * @param targetInterface
     */
    public void setTargetInterface(Class targetInterface) {
        if (!targetInterface.isInterface()) {
            throw new IllegalArgumentException(targetInterface.getName());
        }
        this.targetInterface = targetInterface;
        this.pointcut = AspectDefFactory.createPointcut(targetInterface);
    }

    /**
     * 自動登録を行います。
     */
    public void registerAll() {
        for (int i = 0; i < container.getComponentDefSize(); ++i) {
            ComponentDef cd = container.getComponentDef(i);
            register(cd);
        }
    }

    /**
     * コンポーネントを登録します。
     * 
     * @param componentDef
     */
    protected void register(ComponentDef componentDef) {
        Class componentClass = componentDef.getComponentClass();
        if (componentClass == null) {
            return;
        }
        if (!targetInterface.isAssignableFrom(componentClass)) {
            return;
        }
        registerInterceptor(componentDef);
    }

    /**
     * インターセプタを登録します。
     * 
     * @param componentDef
     */
    protected void registerInterceptor(ComponentDef componentDef) {
        AspectDef aspectDef = AspectDefFactory.createAspectDef(interceptor,
                pointcut);
        componentDef.addAspectDef(aspectDef);
    }
}