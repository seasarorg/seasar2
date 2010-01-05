/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
 * コンストラクタ・インジェクションを実行してコンポーネントを組み立てます。
 * <p>
 * {@link ComponentDef コンポーネント定義}に対して明示的にコンストラクタの引数が指定されなかった時の動作は、
 * {@link AutoBindingDef 自動バインディングタイプ定義}に基づきます。
 * </p>
 * 
 * @author higa
 * @author jundu
 * 
 * @see org.seasar.framework.container.assembler.AbstractConstructorAssembler
 * @see org.seasar.framework.container.assembler.AutoConstructorAssembler
 * @see org.seasar.framework.container.assembler.DefaultConstructorConstructorAssembler
 */
public interface ConstructorAssembler {

    /**
     * コンストラクタ・インジェクションを実行して、 組み立てたコンポーネントを返します。
     * <p>
     * また、 {@link ComponentDef コンポーネント定義}に{@link Expression 式}が指定されていた場合、
     * 式の評価結果をコンポーネントとして返します。
     * </p>
     * 
     * @return コンストラクタ・インジェクション済みのコンポーネントのインスタンス
     * @throws org.seasar.framework.beans.ConstructorNotFoundRuntimeException
     *             適切なコンストラクタが見つからなかった場合
     * @throws IllegalConstructorRuntimeException
     *             コンストラクタの引数となるコンポーネントが見つからなかった場合
     * @throws ClassUnmatchRuntimeException
     *             組み立てたコンポーネントの型がコンポーネント定義のクラス指定に適合しなかった場合
     */
    public Object assemble() throws IllegalConstructorRuntimeException;
}
