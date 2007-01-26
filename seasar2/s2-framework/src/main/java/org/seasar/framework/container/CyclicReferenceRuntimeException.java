/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
 * コンポーネントの循環参照が発生した場合にスローされます。
 * 
 * <p>
 * コンポーネントのコンストラクタ引数に、 同じコンポーネントを指定した場合などに発生します。
 * </p>
 * 
 * @author higa
 * @author belltree
 * 
 * @see org.seasar.framework.container.deployer.SingletonComponentDeployer
 */
public class CyclicReferenceRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = -5993824919440261214L;

    private Class componentClass_;

    /**
     * 循環参照を引き起こしたコンポーネントのクラスを指定して、 <code>CyclicReferenceRuntimeException</code>を構築します。
     * 
     * @param componentClass
     *            循環参照を引き起こしたコンポーネントのクラス
     */
    public CyclicReferenceRuntimeException(Class componentClass) {
        super("ESSR0047", new Object[] { componentClass.getName() });
        componentClass_ = componentClass;
    }

    /**
     * 循環参照を引き起こしたコンポーネントのクラスを返します。
     * 
     * @return 循環参照を引き起こしたコンポーネントのクラス
     */
    public Class getComponentClass() {
        return componentClass_;
    }
}