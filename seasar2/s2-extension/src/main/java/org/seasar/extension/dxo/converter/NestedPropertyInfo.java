/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
package org.seasar.extension.dxo.converter;

import org.seasar.framework.beans.PropertyDesc;

/**
 * ネストしたプロパティの情報を保持するクラスです。
 * <p>
 * このクラスは、 あるJavaBeansがプロパティとして持つ、別のJavaBeansのプロパティの値を取得するための情報を保持します。
 * </p>
 * 
 * @author koichik
 */
public class NestedPropertyInfo {

    /** 変換元JavaBeansのプロパティ記述子 */
    protected PropertyDesc propertyDesc;

    /** ネストしたJavaBeansのプロパティ記述子 */
    protected PropertyDesc nestedPropertyDesc;

    /**
     * <code>NestedPropertyInfo</code>のインスタンスを構築します。
     * 
     * @param propertyDesc
     *            変換元JavaBeansのプロパティ記述子
     * @param nestedPropertyDesc
     *            ネストしたJavaBeansのプロパティ記述子
     */
    public NestedPropertyInfo(final PropertyDesc propertyDesc,
            final PropertyDesc nestedPropertyDesc) {
        this.propertyDesc = propertyDesc;
        this.nestedPropertyDesc = nestedPropertyDesc;
    }

    /**
     * <code>source</code>からネストしたプロパティの値を取得して返します。
     * 
     * @param source
     *            変換元のオブジェクト
     * @return ネストしたプロパティの値
     */
    public Object getValue(final Object source) {
        if (source == null) {
            return null;
        }
        final Object propertyValue = propertyDesc.getValue(source);
        if (propertyValue == null) {
            return null;
        }
        return nestedPropertyDesc.getValue(propertyValue);
    }

}