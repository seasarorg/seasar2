/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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
 * このインターフェースは、 destroyメソッド定義を追加および取得することができるオブジェクトを表します。
 * <p>
 * destroyメソッド定義は複数追加することが出来ます。 destroyメソッド定義の取得はインデックス番号を指定して行います。
 * </p>
 * 
 * @author higa
 * @author belltree
 * 
 * @see DestroyMethodDef
 */
public interface DestroyMethodDefAware {

    /**
     * destroyメソッド定義を追加します。
     * 
     * @param methodDef
     *            destroyメソッド定義
     */
    public void addDestroyMethodDef(DestroyMethodDef methodDef);

    /**
     * {@link DestroyMethodDef destroyメソッド定義}の数を返します。
     * 
     * @return destroyメソッド定義の数
     */
    public int getDestroyMethodDefSize();

    /**
     * 指定されたインデックス番号<code>index</code>のdestroyメソッド定義を返します。
     * <p>
     * インデックス番号は、 追加した順番に 0,1,2,… となります。
     * </p>
     * 
     * @param index
     *            destroyメソッド定義を指定するインデックス番号
     * @return destroyメソッド定義
     */
    public DestroyMethodDef getDestroyMethodDef(int index);
}
