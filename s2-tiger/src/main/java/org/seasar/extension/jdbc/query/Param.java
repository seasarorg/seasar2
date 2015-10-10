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
package org.seasar.extension.jdbc.query;

import java.lang.reflect.Field;

import org.seasar.extension.jdbc.ParamType;
import org.seasar.extension.jdbc.ValueType;

/**
 * JDBCのパラメータです。
 * 
 * @author higa
 * 
 */
public class Param {

    /**
     * パラメータの値です。
     */
    public Object value;

    /**
     * パラメータのクラスです。
     */
    public Class<?> paramClass;

    /**
     * パラメータのタイプです。
     */
    public ParamType paramType = ParamType.IN;

    /**
     * 値タイプです。
     */
    public ValueType valueType;

    /**
     * フィールドです。
     */
    public Field field;

    /**
     * {@link Param}を作成します。
     */
    public Param() {
    }

    /**
     * {@link Param}を作成します。
     * 
     * @param value
     *            パラメータの値です。
     * @param paramClass
     *            パラメータのクラスです。
     */
    public Param(Object value, Class<?> paramClass) {
        this.value = value;
        this.paramClass = paramClass;
    }
}
