/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
 * <p>
 * このインターフェースは、 アスペクト定義を登録および取得することができるオブジェクトを表します。
 * </p>
 * <p>
 * アスペクト定義は複数登録することが出来ます。 アスペクト定義の取得はインデックス番号を指定して行います。 アスペクト定義は登録されている順に適用されます。
 * </p>
 * 
 * @author higa
 * @author belltree
 * 
 * @see AspectDef
 */
public interface AspectDefAware {

    /**
     * アスペクト定義を追加します。
     * 
     * @param aspectDef
     *            アスペクト定義
     */
    public void addAspectDef(AspectDef aspectDef);

    /**
     * アスペクト定義を指定の位置に追加します。
     * 
     * @param index
     *            アスペクト定義を追加する位置
     * @param aspectDef
     *            アスペクト定義
     */
    public void addAspectDef(int index, AspectDef aspectDef);

    /**
     * 登録されている{@link AspectDef アスペクト定義}の数を返します。
     * <p>
     * 登録されている{@link org.aopalliance.intercept.MethodInterceptor インターセプタ}の数ではなく、
     * アスペクト定義の数を返します。 アスペクト定義のコンポーネント(インターセプタ)のクラスが
     * {@link org.seasar.framework.aop.interceptors.InterceptorChain InterceptorChain}で、
     * その中に複数のインターセプタが含まれる場合も、 1つのアスペクト定義としてカウントします。
     * </p>
     * 
     * @return 登録されているアスペクト定義の数
     */
    public int getAspectDefSize();

    /**
     * 指定されたインデックス番号<code>index</code>のアスペクト定義を返します。
     * <p>
     * インデックス番号は、 登録した順番に 0,1,2,… となります。
     * </p>
     * 
     * @param index
     *            アスペクト定義を指定するインデックス番号
     * @return アスペクト定義
     */
    public AspectDef getAspectDef(int index);
}
