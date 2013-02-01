/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
 * コンポーネントの構築に失敗した場合にスローされます。
 * <p>
 * この例外は、 コンポーネント定義でコンストラクタの引数として指定されたコンポーネントの取得に失敗した場合などに発生します。
 * </p>
 * 
 * @author higa
 * @author jundu
 */
public class IllegalConstructorRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1454032979718620824L;

    private Class componentClass_;

    /**
     * <code>IllegalConstructorRuntimeException</code>を構築します。
     * 
     * @param componentClass
     *            構築に失敗したコンポーネントのクラス
     * @param cause
     *            コンポーネントの構築に失敗した原因を表すエラーまたは例外
     */
    public IllegalConstructorRuntimeException(Class componentClass,
            Throwable cause) {
        super("ESSR0058", new Object[] { componentClass.getName(), cause },
                cause);
        componentClass_ = componentClass;
    }

    /**
     * 構築に失敗したコンポーネントのクラスを返します。
     * 
     * @return 構築に失敗したコンポーネントのクラス
     */
    public Class getComponentClass() {
        return componentClass_;
    }
}