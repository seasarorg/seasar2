/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.factory;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.seasar.framework.aop.Pointcut;
import org.seasar.framework.aop.impl.PointcutImpl;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.impl.AspectDefImpl;
import org.seasar.framework.container.ognl.OgnlExpression;
import org.seasar.framework.util.StringUtil;

/**
 * {@link org.seasar.framework.container.AspectDef アスペクト定義}および{@link org.seasar.framework.aop.Pointcut ポイントカット}を構築するためのファクトリクラスです。
 * 
 * @author jundu
 */
public class AspectDefFactory {

    /**
     * AspectDefFactoryを構築します。
     */
    protected AspectDefFactory() {
    }

    /**
     * 指定された{@link org.aopalliance.intercept.MethodInterceptor インターセプタ}と{@link org.seasar.framework.aop.Pointcut ポイントカット}から、
     * {@link org.seasar.framework.container.AspectDef アスペクト定義}を構築して返します。
     * 
     * @param interceptor
     *            インターセプタ
     * @param pointcut
     *            ポイントカット
     * @return アスペクト定義
     */
    public static AspectDef createAspectDef(MethodInterceptor interceptor,
            Pointcut pointcut) {
        AspectDef aspectDef = new AspectDefImpl(pointcut);
        aspectDef.setValue(interceptor);
        return aspectDef;
    }

    /**
     * 指定されたインターセプタ名と{@link org.seasar.framework.aop.Pointcut ポイントカット}から、
     * {@link org.seasar.framework.container.AspectDef アスペクト定義}を構築して返します。
     * 
     * @param interceptorName
     *            インターセプタ名
     * @param pointcut
     *            ポイントカット
     * @return アスペクト定義
     */
    public static AspectDef createAspectDef(String interceptorName,
            Pointcut pointcut) {
        AspectDef aspectDef = new AspectDefImpl(pointcut);
        aspectDef.setExpression(new OgnlExpression(interceptorName));
        return aspectDef;
    }

    /**
     * 指定された{@link org.seasar.framework.container.ComponentDef コンポーネント定義}と{@link org.seasar.framework.aop.Pointcut ポイントカット}から、
     * {@link org.seasar.framework.container.AspectDef アスペクト定義}を構築して返します。
     * 
     * @param cd
     *            インターセプタのコンポーネント定義
     * @param pointcut
     *            ポイントカット
     * @return アスペクト定義
     */
    public static AspectDef createAspectDef(ComponentDef cd, Pointcut pointcut) {
        AspectDef aspectDef = new AspectDefImpl(pointcut);
        aspectDef.setChildComponentDef(cd);
        return aspectDef;
    }

    /**
     * 指定されたインターセプタ名とポイントカットを表す文字列から、
     * {@link org.seasar.framework.container.AspectDef アスペクト定義}を構築して返します。
     * 
     * @param interceptorName
     *            インターセプタ名
     * @param pointcutStr
     *            ポイントカットを表す文字列
     * @return アスペクト定義
     */
    public static AspectDef createAspectDef(String interceptorName,
            String pointcutStr) {
        Pointcut pointcut = createPointcut(pointcutStr);
        return createAspectDef(interceptorName, pointcut);
    }

    /**
     * 指定された{@link org.aopalliance.intercept.MethodInterceptor インターセプタ}とポイントカットを表す文字列から、
     * {@link org.seasar.framework.container.AspectDef アスペクト定義}を構築して返します。
     * 
     * @param interceptor
     *            インターセプタ
     * @param pointcutStr
     *            ポイントカットを表す文字列
     * @return アスペクト定義
     */
    public static AspectDef createAspectDef(MethodInterceptor interceptor,
            String pointcutStr) {
        Pointcut pointcut = createPointcut(pointcutStr);
        return createAspectDef(interceptor, pointcut);
    }

    /**
     * 指定された{@link org.seasar.framework.container.ComponentDef コンポーネント定義}とポイントカットを表す文字列から、
     * {@link org.seasar.framework.container.AspectDef アスペクト定義}を構築して返します。
     * 
     * @param cd
     *            インターセプタのコンポーネント定義
     * @param pointcutStr
     *            ポイントカットを表す文字列
     * @return アスペクト定義
     */
    public static AspectDef createAspectDef(ComponentDef cd, String pointcutStr) {
        Pointcut pointcut = createPointcut(pointcutStr);
        return createAspectDef(cd, pointcut);
    }

    /**
     * 指定されたインターセプタ名と{@link java.lang.reflect.Method メソッド}から、
     * {@link org.seasar.framework.container.AspectDef アスペクト定義}を構築して返します。
     * 
     * @param interceptorName
     *            インターセプタ名
     * @param method
     *            メソッド
     * @return アスペクト定義
     */
    public static AspectDef createAspectDef(String interceptorName,
            Method method) {
        Pointcut pointcut = createPointcut(method);
        return createAspectDef(interceptorName, pointcut);
    }

    /**
     * 指定された{@link org.aopalliance.intercept.MethodInterceptor インターセプタ}と{@link java.lang.reflect.Method メソッド}から、
     * {@link org.seasar.framework.container.AspectDef アスペクト定義}を構築して返します。
     * 
     * @param interceptor
     *            インターセプタ
     * @param method
     *            メソッド
     * @return アスペクト定義
     */
    public static AspectDef createAspectDef(MethodInterceptor interceptor,
            Method method) {
        Pointcut pointcut = createPointcut(method);
        return createAspectDef(interceptor, pointcut);
    }

    /**
     * 指定された{@link org.seasar.framework.container.ComponentDef コンポーネント定義}と{@link java.lang.reflect.Method メソッド}から、
     * {@link org.seasar.framework.container.AspectDef アスペクト定義}を構築して返します。
     * 
     * @param cd
     *            インターセプタのコンポーネント定義
     * @param method
     *            メソッド
     * @return アスペクト定義
     */
    public static AspectDef createAspectDef(ComponentDef cd, Method method) {
        Pointcut pointcut = createPointcut(method);
        return createAspectDef(cd, pointcut);
    }

    /**
     * 指定されたポイントカットを表す文字列から、 {@link org.seasar.framework.aop.Pointcut ポイントカット}を構築して返します。
     * 
     * @param pointcutStr
     *            ポイントカットを表す文字列
     * @return ポイントカット
     */
    public static Pointcut createPointcut(String pointcutStr) {
        if (!StringUtil.isEmpty(pointcutStr)) {
            String[] methodNames = StringUtil.split(pointcutStr, ", \n");
            return new PointcutImpl(methodNames);
        }
        return null;
    }

    /**
     * 指定された{@link Class クラス}から、
     * {@link org.seasar.framework.aop.Pointcut ポイントカット}を構築して返します。
     * 
     * @param clazz
     *            クラス
     * @return ポイントカット
     */
    public static Pointcut createPointcut(Class clazz) {
        return new PointcutImpl(clazz);
    }

    /**
     * 指定された{@link java.lang.reflect.Method メソッド}から、
     * {@link org.seasar.framework.aop.Pointcut ポイントカット}を構築して返します。
     * 
     * @param method
     *            メソッド
     * @return ポイントカット
     */
    public static Pointcut createPointcut(Method method) {
        if (method != null) {
            return new PointcutImpl(method);
        }
        return null;
    }
}
