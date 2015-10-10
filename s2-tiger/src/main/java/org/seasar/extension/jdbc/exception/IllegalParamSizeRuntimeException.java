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
package org.seasar.extension.jdbc.exception;

import org.seasar.framework.exception.SRuntimeException;

/**
 * <p>
 * パラメータの数が不正な場合の例外です。
 * </p>
 * 
 * @author higa
 * 
 */
public class IllegalParamSizeRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    private int paramSize;

    private int paramClassSize;

    /**
     * {@link IllegalParamSizeRuntimeException}を作成します。
     * 
     * @param paramSize
     *            パラメータの値の数
     * @param paramClassSize
     *            パラメータのクラスの数
     * 
     */
    public IllegalParamSizeRuntimeException(int paramSize, int paramClassSize) {
        super("ESSR0734", new Object[] { String.valueOf(paramSize),
                String.valueOf(paramClassSize) });
        this.paramSize = paramSize;
        this.paramClassSize = paramClassSize;
    }

    /**
     * パラメータの数を返します。
     * 
     * @return パラメータの数
     */
    public int getParamSize() {
        return paramSize;
    }

    /**
     * パラメータのクラスの数を返します。
     * 
     * @return パラメータのクラスの数
     */
    public int getParamClassSize() {
        return paramClassSize;
    }
}