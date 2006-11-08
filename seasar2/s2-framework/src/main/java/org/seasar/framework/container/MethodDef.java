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

import java.lang.reflect.Method;

/**
 * メソッド・インジェクションを定義するためのインターフェースです。
 * <p>
 * メソッド・インジェクションとは、任意のメソッドや式の呼び出しによりコンポーネントをインジェクションすることです。
 * </p>
 * <p>
 * 例として、<code>addFoo(Foo)</code> メソッドを通じて <code>Foo</code>をインジェクションする場合に利用することができます。
 * 引数のないメソッドや任意の式を呼び出すこともできます。
 * </p>
 * <p>
 * コンポーネントが初期化されるときに実行されるinitMethodインジェクションと、
 * コンテナの終了時に実行されるdesoryMethodインジェクションがあります。 destroyMethodインジェクションが適用されるのは、
 * コンポーネントのinstance要素が<code>singleton</code>の場合だけです。
 * </p>
 * 
 * @author higa
 * @author azusa
 * 
 */
public interface MethodDef extends ArgDefAware {

    /**
     * 実行するメソッドを返します。
     * 
     * @return 実行するメソッド
     */
    public Method getMethod();

    /**
     * メソッド名を返します。
     * 
     * @return メソッド名
     */
    public String getMethodName();

    /**
     * メソッド引数を返します。
     * 
     * @return メソッド引数
     */
    public Object[] getArgs();

    /**
     * 引数および式を評価するコンテキストとなるS2コンテナを返します。
     * 
     * @return 引数および式を評価するコンテキストとなるS2コンテナ
     */
    public S2Container getContainer();

    /**
     * 引数および式を評価するコンテキストとなるS2コンテナを設定します。
     * 
     * @param container
     *            引数および式を評価するコンテキストとなるS2コンテナ
     */
    public void setContainer(S2Container container);

    /**
     * 実行される式を返します。
     * 
     * @return 実行される式
     */
    public Expression getExpression();

    /**
     * 実行される式を設定します。
     * 
     * @param expression
     *            実行される式
     */
    public void setExpression(Expression expression);
}
