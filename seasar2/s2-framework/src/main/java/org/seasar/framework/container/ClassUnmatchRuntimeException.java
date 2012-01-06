/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
 * コンポーネントのインスタンスを、 {@link ComponentDef コンポーネント定義}に指定されたクラスにキャスト出来ない場合にスローされます。
 * <p>
 * {@link  ComponentDef#setExpression(Expression)}でインスタンスの生成を定義している場合は、
 * そのインスタンスをコンポーネント定義に指定されたクラスにキャスト出来ないことを表します。
 * </p>
 * <p>
 * 外部コンポーネントを{@link S2Container#injectDependency(Object)}などでインジェクションする場合は、
 * そのコンポーネントを、 コンポーネント定義に指定されたクラスにキャストできないことを表します。
 * </p>
 * 
 * @author higa
 * @author belltree
 * 
 * @see org.seasar.framework.container.ConstructorAssembler#assemble()
 * @see org.seasar.framework.container.S2Container#injectDependency(Object,
 *      Class)
 * @see org.seasar.framework.container.S2Container#injectDependency(Object,
 *      String)
 */
public class ClassUnmatchRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1967770604202235241L;

    private Class componentClass_;

    private Class realComponentClass_;

    /**
     * <code>ClassUnmatchRuntimeException</code>を構築します。
     * 
     * @param componentClass
     *            コンポーネント定義に指定されたクラス
     * @param realComponentClass
     *            コンポーネントの実際の型
     */
    public ClassUnmatchRuntimeException(Class componentClass,
            Class realComponentClass) {
        super("ESSR0069", new Object[] {
                componentClass.getName(),
                realComponentClass != null ? realComponentClass.getName()
                        : "null" });
        componentClass_ = componentClass;
        realComponentClass_ = realComponentClass;
    }

    /**
     * コンポーネント定義に指定されたクラスを返します。
     * 
     * @return コンポーネント定義に指定されたクラス
     */
    public Class getComponentClass() {
        return componentClass_;
    }

    /**
     * コンポーネントの実際の型を返します。
     * 
     * @return コンポーネントの実際の型
     */
    public Class getRealComponentClass() {
        return realComponentClass_;
    }
}