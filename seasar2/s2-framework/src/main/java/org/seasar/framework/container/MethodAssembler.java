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

import org.seasar.framework.exception.IllegalAccessRuntimeException;
import org.seasar.framework.exception.InvocationTargetRuntimeException;

/**
 * メソッド・インジェクションを実行してコンポーネントを組み立てます。
 * <p>
 * インジェクションの実行は、 {@link MethodDef メソッド定義}に基づいて行います。
 * </p>
 * 
 * @author higa
 * @author azusa
 */
public interface MethodAssembler {

    /**
     * 指定された<code>component</code>に対して、 メソッド・インジェクションを実行します。
     * <p>
     * メソッドの引数として指定されたコンポーネントが見つからない場合には、 IllegalMethodRuntimeExceptionがスローされます。
     * </p>
     * 
     * @param component
     *            S2コンテナ上のコンポーネントがセットされる対象
     * @throws IllegalMethodRuntimeException
     *             メソッドの引数として指定されたコンポーネントが見つからない場合、 およびメソッド実行時に{@link NumberFormatException}が発生した場合
     * @throws InvocationTargetRuntimeException
     *             メソッド実行時に検査例外が発生した場合(実行時例外とエラーが発生した場合にはそのままスローされます)
     * @throws IllegalAccessRuntimeException
     *             メソッド実行時に{@link IllegalAccessException}が発生した場合
     * 
     */
    public void assemble(Object component) throws IllegalMethodRuntimeException;
}
