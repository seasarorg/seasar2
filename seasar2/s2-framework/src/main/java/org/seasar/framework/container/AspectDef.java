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
 * 1つのコンポーネントに複数のアスペクトを定義することが可能です。 定義した順にアスペクトのインターセプタが実行されます。
 * </p>
 * <p>
 * S2AOPにおけるインターセプタは、
 * {@link org.aopalliance.intercept.MethodInterceptor MethodInterceptor}インターフェースを実装したクラスのコンポーネントとして定義します。
 * インターセプターのセットを、複数のコンポーネントに適用する場合には、 複数のインターセプタを1つのインターセプタ・コンポーネントとして定義できる、
 * {@link org.seasar.framework.aop.interceptors.InterceptorChain InterceptorChain}を使用すると設定を簡略化できます。
 * </p>
 * <p>
 * S2AOPの詳細については<a
 * href="http://s2container.seasar.org/ja/aop.html">Seasar2公式サイト</a>を参照して下さい。
 * </p>
 * 
 * @author higa
 * @author belltree
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