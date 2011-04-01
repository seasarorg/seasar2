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
package org.seasar.extension.component;

/**
 * 指定されたコンポーネントのメソッドを呼び出すコンポーネントの実装です。
 * <p>
 * このクラスはS2RMIなどから利用されることを意図しています。
 * </p>
 * 
 * @author Kenichiro Murata
 */
public interface ComponentInvoker {

    /**
     * 指定されたコンポーネントのメソッドを実行します。
     * 
     * @param componentName
     *            コンポーネント名
     * @param methodName
     *            メソッド名
     * @param args
     *            メソッドの引数
     * @return メソッドの戻り値
     * @throws Throwable
     *             メソッドの実行中に例外が発生した場合
     */
    public Object invoke(String componentName, String methodName, Object[] args)
            throws Throwable;

}
