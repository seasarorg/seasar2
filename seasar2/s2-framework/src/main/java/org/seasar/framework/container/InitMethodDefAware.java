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
 * このインターフェースは、 initメソッド定義を登録および取得することができるオブジェクトを表します。
 * <p>
 * initメソッド定義は複数登録することが出来ます。 initメソッド定義の取得はインデックス番号を指定して行います。
 * </p>
 * 
 * @author higa
 * @author belltree
 * 
 * @see InitMethodDef
 */
public interface InitMethodDefAware {

    /**
     * initメソッド定義を追加します。
     * 
     * @param methodDef
     *            initメソッド定義
     */
    public void addInitMethodDef(InitMethodDef methodDef);

    /**
     * 登録されている{@link InitMethodDef initメソッド定義}の数を返します。
     * 
     * @return 登録されているinitメソッド定義の数
     */
    public int getInitMethodDefSize();

    /**
     * 指定されたインデックス番号<code>index</code>のinitメソッド定義を返します。
     * <p>
     * インデックス番号は、 登録した順番に 0,1,2,… となります。
     * </p>
     * 
     * @param index
     *            initメソッド定義を指定するインデックス番号
     * @return initメソッド定義
     */
    public InitMethodDef getInitMethodDef(int index);
}
