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
package org.seasar.extension.jdbc;

/**
 * プロシージャを呼び出すパラメータのタイプです。
 * 
 * @author higa
 * 
 */
public enum ParamType {

    /**
     * <code>IN</code>パラメータです。
     */
    IN,
    /**
     * <code>OUT</code>パラメータです。
     */
    OUT,
    /**
     * <code>IN OUT</code>パラメータです。
     */
    IN_OUT;

    /**
     * サフィックスを返します。
     * 
     * @return サフィックス
     */
    public String getSuffix() {
        return "_" + name();
    }
}