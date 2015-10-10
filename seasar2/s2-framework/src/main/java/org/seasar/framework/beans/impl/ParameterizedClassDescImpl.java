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
package org.seasar.framework.beans.impl;

import org.seasar.framework.beans.ParameterizedClassDesc;

/**
 * {@link ParameterizedClassDesc}の実装クラスです。
 * 
 * @since 2.4.18
 * @author koichik
 */
public class ParameterizedClassDescImpl implements ParameterizedClassDesc {

    /** 原型となるクラス */
    protected Class rawClass;

    /** 型引数を表す{@link ParameterizedClassDesc}の配列 */
    protected ParameterizedClassDesc[] arguments;

    /**
     * インスタンスを構築します。
     */
    public ParameterizedClassDescImpl() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param rawClass
     *            原型となるクラス
     */
    public ParameterizedClassDescImpl(final Class rawClass) {
        this.rawClass = rawClass;
    }

    /**
     * インスタンスを構築します。
     * 
     * @param rawClass
     *            原型となるクラス
     * @param arguments
     *            型引数を表す{@link ParameterizedClassDesc}の配列
     */
    public ParameterizedClassDescImpl(final Class rawClass,
            final ParameterizedClassDesc[] arguments) {
        this.rawClass = rawClass;
        this.arguments = arguments;
    }

    public boolean isParameterizedClass() {
        return arguments != null;
    }

    public Class getRawClass() {
        return rawClass;
    }

    /**
     * 原型となるクラスを設定します。
     * 
     * @param rawClass
     *            原型となるクラス
     */
    public void setRawClass(final Class rawClass) {
        this.rawClass = rawClass;
    }

    public ParameterizedClassDesc[] getArguments() {
        return arguments;
    }

    /**
     * 型引数を表す{@link ParameterizedClassDesc}の配列を設定します。
     * 
     * @param arguments
     *            型引数を表す{@link ParameterizedClassDesc}の配列
     */
    public void setArguments(final ParameterizedClassDesc[] arguments) {
        this.arguments = arguments;
    }

}
