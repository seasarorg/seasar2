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

import org.seasar.framework.exception.SRuntimeException;

/**
 * 不正なメソッド・インジェクション定義が指定されていた場合にスローされます。
 * <p>
 * メソッド・インジェクションを実行した際に、 メソッドの引数として指定されたコンポーネントが見つからない場合や、
 * 引数を適切な型に変換出来ない場合などに発生します。
 * </p>
 * 
 * @author higa
 * @author belltree
 * 
 * @see MethodDef
 * @see InitMethodDef
 * @see DestroyMethodDef
 * @see MethodAssembler
 * @see org.seasar.framework.container.assembler.AbstractMethodAssembler
 */
public class IllegalMethodRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = -9114586009590848186L;

    private Class componentClass_;

    private String methodName_;

    /**
     * <code>IllegalMethodRuntimeException</code>を構築します。
     * 
     * @param componentClass
     *            不正なメソッド・インジェクション定義を含むコンポーネントのクラス
     * @param methodName
     *            不正なメソッド・インジェクション定義のメソッド名
     * @param cause
     *            原因となった例外
     */
    public IllegalMethodRuntimeException(Class componentClass,
            String methodName, Throwable cause) {
        super("ESSR0060", new Object[] { componentClass.getName(), methodName,
                cause }, cause);
        componentClass_ = componentClass;
        methodName_ = methodName;
    }

    /**
     * 不正なメソッド・インジェクション定義を含むコンポーネントのクラスを返します。
     * 
     * @return コンポーネントのクラス
     */
    public Class getComponentClass() {
        return componentClass_;
    }

    /**
     * 不正なメソッド・インジェクション定義のメソッド名を返します。
     * 
     * @return メソッド名
     */
    public String getMethodName() {
        return methodName_;
    }
}