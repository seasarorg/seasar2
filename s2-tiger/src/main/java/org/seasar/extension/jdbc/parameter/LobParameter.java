/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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

import org.seasar.extension.sql.SqlArgWrapper;

/**
 * <code>byte[]</code>や{@link Serializable}、<code>String</code>
 * の値がラージオブジェクトであることを示すクラスです。
 * 
 * @author koichik
 */
public class LobParameter implements SqlArgWrapper {

    /** 値 */
    protected Object value;

    /** ラージオブジェクトのクラス */
    protected Class<?> lobClass;

    /**
     * インスタンスを作成します。
     * 
     * @param value
     *            値
     */
    public LobParameter(final byte[] value) {
        this.value = value;
        this.lobClass = byte[].class;
    }

    /**
     * インスタンスを作成します。
     * 
     * @param value
     *            値
     */
    public LobParameter(final Serializable value) {
        this.value = value;
        if (value != null) {
            this.lobClass = value.getClass();
        } else {
            this.lobClass = Serializable.class;
        }
    }

    /**
     * インスタンスを作成します。
     * 
     * @param value
     *            値
     */
    public LobParameter(final String value) {
        this.value = value;
        this.lobClass = String.class;
    }

    /**
     * 値を返します。
     * 
     * @return 値
     */
    public Object getValue() {
        return value;
    }

    /**
     * ラージオブジェクトのクラスを返します。
     * 
     * @return ラージオブジェクトのクラス
     */
    public Class<?> getLobClass() {
        return lobClass;
    }

}
