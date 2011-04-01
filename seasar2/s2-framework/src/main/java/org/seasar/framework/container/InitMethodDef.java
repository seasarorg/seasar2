/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
 * コンポーネントに対してinitメソッド･インジェクションを定義するためのインターフェースです。
 * <p>
 * initメソッド･インジェクションとは、S2コンテナによってインスタンスが生成された直後に、
 * 1つ以上の任意のメソッド(初期化メソッド)を実行するという機能です。ただし、コンポーネントインスタンス定義が<code>outer</code>の場合には、
 * 以下のメソッドが呼び出されたタイミングで実行されます。
 * 
 * <dl>
 * <dt>{@link S2Container#injectDependency(Object)}</dt>
 * <dt>{@link S2Container#injectDependency(Object,Class)}</dt>
 * <dt>{@link S2Container#injectDependency(Object,String)}</dt>
 * </dl>
 * 
 * </p>
 * 
 * @author higa
 * @author matsunobu
 * 
 * @see S2Container
 */
public interface InitMethodDef extends MethodDef {
}