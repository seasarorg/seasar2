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
 * このインターフェースは、メタデータ定義を追加および取得することのできるオブジェクトを表します。 
 * <p>
 * メタデータ定義は複数追加することができます。 メタデータ定義の取得は、メタデータ定義名またはインデックス番号を指定して行います。
 * </p>
 * @author higa
 * @author Tsuyoshi Yamamoto
 * @see MetaDef
 * 
 */
public interface MetaDefAware {

    /**
     * メタデータ定義を追加します。
     * 
     * @param metaDef
     *            メタデータ定義
     */
    public void addMetaDef(MetaDef metaDef);

    /**
     * {@link MetaDef メタデータ定義}の数を返します。
     * 
     * @return メタデータ定義の数
     */
    public int getMetaDefSize();

    /**
     * インデックス番号<code>index</code>で指定されたメタデータ定義を返します。
     * <p>
     * インデックス番号は、追加した順に0, 1, 2…となります。
     * </p>
     * 
     * @param index
     *            メタデータ定義を指定するインデックス番号
     * @return メタデータ定義
     */
    public MetaDef getMetaDef(int index);

    /**
     * 指定したメタデータ定義名で登録されているメタデータ定義を取得します。 <br>
     * メタデータ定義が登録されていない場合、<code>null</code>を返します。
     * 
     * @param name
     *            メタデータ定義名
     * @return メタデータ定義
     */
    public MetaDef getMetaDef(String name);

    /**
     * 指定したメタデータ定義名で登録されているメタデータ定義を取得します。 <br>
     * メタデータ定義が登録されていない場合、要素数0の配列を返します。
     * @param name
     *            メタデータ定義名
     * @return メタデータ定義を格納した配列
     */
    public MetaDef[] getMetaDefs(String name);
}
