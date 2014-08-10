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
 * このインターフェースはプロパティ定義を登録および取得する方法を定義するオブジェクトを表します。
 * <p>
 * プロパティ定義は複数登録することが出来ます。 プロパティ定義の取得はインデックス番号を指定して行います。
 * </p>
 * 
 * @author higa
 * @author Maeno
 * 
 * @see PropertyDef
 * 
 */
public interface PropertyDefAware {

    /**
     * {@link PropertyDef プロパティ定義}を追加します。
     * 
     * @param propertyDef
     *            プロパティ定義
     */
    public void addPropertyDef(PropertyDef propertyDef);

    /**
     * 登録されている{@link PropertyDef プロパティ定義}の数を返します。
     * 
     * @return 登録されているプロパティ定義の数
     */
    public int getPropertyDefSize();

    /**
     * 指定されたインデックス番号<code>index</code>の{@link PropertyDef プロパティ定義}を返します。
     * 
     * @param index
     *            プロパティ定義を指定するインデックス番号
     * @return プロパティ定義
     */
    public PropertyDef getPropertyDef(int index);

    /**
     * 指定したプロパティ名で登録されている{@link PropertyDef プロパティ定義}を返します。
     * 
     * @param propertyName
     *            プロパティ名
     * @return プロパティ定義
     */
    public PropertyDef getPropertyDef(String propertyName);

    /**
     * 指定したプロパティ名のプロパティ定義があれば<code>true</code>を返します。
     * 
     * @param propertyName
     *            プロパティ名
     * @return プロパティ定義が存在していれば<code>true</code>、存在していなければ<code>false</code>
     */
    public boolean hasPropertyDef(String propertyName);
}
