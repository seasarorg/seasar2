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
 * このインターフェースはインタータイプ定義を登録および取得する方法を定義するオブジェクトを表します。
 * <p>
 * インタータイプ定義は複数登録することが出来ます。 インタータイプ定義の取得はインデックス番号を指定して行います。
 * </p>
 * 
 * @author koichik
 * @author Maneno
 * 
 * @see InterTypeDef
 */
public interface InterTypeDefAware {

    /**
     * {@link InterTypeDef インタータイプ定義}を追加します。
     * 
     * @param interTypeDef
     *            インタータイプ定義
     */
    public void addInterTypeDef(InterTypeDef interTypeDef);

    /**
     * 登録されている{@link InterTypeDef インタータイプ定義}の数を返します。
     * 
     * @return 登録されているインタータイプ定義の数
     */
    public int getInterTypeDefSize();

    /**
     * 指定されたインデックス番号<code>index</code>の{@link InterTypeDef インタータイプ定義}を返します。
     * 
     * @param index
     *            インタータイプ定義を指定するインデックス番号
     * @return インタータイプ定義
     */
    public InterTypeDef getInterTypeDef(int index);

}
