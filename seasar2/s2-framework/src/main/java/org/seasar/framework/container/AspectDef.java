/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.container;

import org.seasar.framework.aop.Aspect;
import org.seasar.framework.aop.Pointcut;

/**
 * コンポーネントに適用するアスペクトを定義するインターフェースです。
 * <p>
 * S2AOP(Seasar2 Aspect Oriented Programming)には、基本要素として以下のものがあります。
 * <dl>
 * <dt>アスペクト({@link org.seasar.framework.aop.Aspect Aspect})</dt>
 * <dd>ポイントカットとインターセプタの関連を表します。</dd>
 * <dt>ポイントカット({@link org.seasar.framework.aop.Pointcut Pointcut})</dt>
 * <dd>インターセプタが実行されるメソッドの集合を表します。</dd>
 * <dt>インターセプタ({@link org.aopalliance.intercept.Interceptor Interceptor})</dt>
 * <dd>ポイントカットで実行される共通的な処理を表します。</dd>
 * </dl>
 * インターセプタは、 より一般的にアドバイス({@link org.aopalliance.aop.Advice Advice})と呼ばれます。
 * </p>
 * <p>
 * S2AOPにおけるインターセプタは、
 * {@link org.aopalliance.intercept.MethodInterceptor MethodInterceptor}インターフェースを実装したクラスのコンポーネントとして定義します。
 * {@link org.seasar.framework.aop.interceptors.InterceptorChain InterceptorChain}を使用することで、
 * 複数のインターセプタを1つのインターセプタ・コンポーネントとして定義することが可能です。
 * </p>
 * <p>
 * 1つのコンポーネントに複数のアスペクトを定義することが可能です。 定義した順にアスペクトのインターセプタが実行されます。
 * </p>
 * 
 * @author higa
 * @author belltree (Javadoc)
 */
public interface AspectDef extends ArgDef {

    /**
     * ポイントカットを返します。
     * 
     * @return ポイントカット
     */
    Pointcut getPointcut();

    /**
     * ポイントカットを設定します。
     */
    void setPointcut(Pointcut pointcut);

    /**
     * アスペクトを返します。
     * 
     * @return アスペクト
     */
    Aspect getAspect();
}