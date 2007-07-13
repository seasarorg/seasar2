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
package org.seasar.framework.container;

/**
 * コンポーネントに対して<var>destroy</var>メソッド・インジェクションを定義するためのインターフェースです。
 * <p>
 * <var>destroy</var>メソッド・インジェクションとは、 S2コンテナによって管理されているコンポーネントが破棄される際に、
 * 1個以上の任意のメソッド(終了処理メソッド)を実行するという機能です。
 * </p>
 * <p>
 * コンポーネントの{@link InstanceDef インスタンス定義}が<code>singleton</code>の場合には、
 * S2コンテナが終了する際に<var>destroy</var>メソッド・インジェクションが実行されます。
 * </p>
 * 
 * @author higa
 * @author belltree
 * 
 * @see ComponentDeployer#destroy()
 * @see ComponentDef#destroy()
 * @see S2Container#destroy()
 * @see org.seasar.framework.container.factory.S2ContainerFactory#destroy()
 * @see org.seasar.framework.container.factory.SingletonS2ContainerFactory#destroy()
 */
public interface DestroyMethodDef extends MethodDef {

}