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

import org.seasar.framework.aop.InterType;

/**
 * コンポーネントに組み込むインタータイプを定義するインターフェースです。
 * <p>
 * インタータイプ定義は、diconファイルにおける<code>&lt;interType&gt;</code>要素で指定されます。
 * <code>&lt;interType&gt;</code>要素にはclass属性が含まれています。
 * </p>
 * <p>
 * class属性には{@link org.seasar.framework.aop.InterType インタータイプ}を実装したクラスを指定します。
 * </p>
 * <p>
 * InterTypeは「静的な構造の変更」を実現します。 「静的な構造の変更」は下記のものを含みます。
 * </p>
 * <ul>
 * <li>スーパークラスの変更</li>
 * <li>実装インターフェースの追加</li>
 * <li>フィールドの追加</li>
 * <li>コンストラクタの追加</li>
 * <li>メソッドの追加</li>
 * </ul>
 * 
 * @author koichik
 * @author Maeno
 */
public interface InterTypeDef extends ArgDef {

    /**
     * インタータイプを返します。
     * 
     * @return インタータイプ
     */
    InterType getInterType();

}
