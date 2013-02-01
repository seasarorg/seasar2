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
 * 取得しようとしたコンポーネントがS2コンテナ上に見つからなかった場合にスローされます。
 * <p>
 * コンポーネントの検索には、 コンポーネントキー(キーオブジェクト)として、 クラス(インターフェース)またはコンポーネント名が使用できますが、
 * どちらの場合でもコンポーネントが見つからなかった場合には、 この例外がスローされます。
 * </p>
 * 
 * @author higa
 * @author belltree
 */
public class ComponentNotFoundRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 7584682915737273760L;

    private Object componentKey_;

    /**
     * コンポーネントの検索に用いたコンポーネントキーを指定して、
     * <code>ComponentNotFoundRuntimeException</code>を構築します。
     * 
     * @param componentKey
     *            コンポーネントキー
     */
    public ComponentNotFoundRuntimeException(Object componentKey) {
        super("ESSR0046", new Object[] { componentKey });
        componentKey_ = componentKey;
    }

    /**
     * コンポーネントの検索に用いたコンポーネントキーを返します。
     * 
     * @return コンポーネントキー
     */
    public Object getComponentKey() {
        return componentKey_;
    }
}