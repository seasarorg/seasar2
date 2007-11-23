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
package org.seasar.extension.jdbc.parameter;

import java.io.Serializable;

/**
 * <code>byte[]</code>や{@link Serializable}、<code>String</code>の値がラージオブジェクトであることを示すクラスです。
 * 
 * @author koichik
 */
public class LobParameter {

    /** 値 */
    protected Object value;

    /**
     * インスタンスを作成します。
     * 
     * @param value
     *            値
     */
    public LobParameter(final Object value) {
        if (value == null) {
            throw new NullPointerException("value");
        }
        this.value = value;
    }

    /**
     * 値を返します。
     * 
     * @return 値
     */
    public Object getValue() {
        return value;
    }

}
