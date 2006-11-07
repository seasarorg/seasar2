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

/**
 * コンポーネントのコンストラクタおよびメソッドに与えられる引数定義のためのインターフェースです。
 * 
 * @author higa
 * @author vestige
 */
public interface ArgDef extends MetaDefAware {

    /**
     * 引数定義の値を返します。
     * <p>
     * 引数定義の値とは、diconファイルに記述した<code>&lt;arg&gt;</code>要素の内容です。
     * インジェクションする際に、コンストラクタや初期化メソッド等の引数値になります。
     * </p>
     * 
     * @return 引数定義の値
     */
    public Object getValue();

    /**
     * 引数定義の値を設定します。
     * 
     * @param value
     *            引数定義の値
     */
    public void setValue(Object value);

    /**
     * 引数を評価するコンテキストとなるS2コンテナを返します。
     * 
     * @return 引数を評価するコンテキストとなるS2コンテナ
     */
    public S2Container getContainer();

    /**
     * 引数を評価するコンテキストとなるS2コンテナを設定します。
     * 
     * @param container
     *            引数を評価するコンテキストとなるS2コンテナ
     */
    public void setContainer(S2Container container);

    /**
     * 引数定義の値となる式を返します。
     * 
     * @return 引数定義の値となる式
     */
    public Expression getExpression();

    /**
     * 引数定義の値となる式を設定します。
     * 
     * @param expression
     *            引数定義の値となる式
     */
    public void setExpression(Expression expression);

    /**
     * 引数定義の値となる式、引数定義の値、引数定義の値となるコンポーネント定義のいずれかが存在し、値の取得が可能かどうかを返します。
     * 
     * @return 値の取得が可能な場合、<code>true</code>、そうでない場合は<code>false</code>
     */
    public boolean isValueGettable();

    /**
     * 引数定義の値となるコンポーネント定義を設定します。
     * 
     * @param componentDef
     *            引数定義の値となるコンポーネント定義
     */
    public void setChildComponentDef(ComponentDef componentDef);

}
