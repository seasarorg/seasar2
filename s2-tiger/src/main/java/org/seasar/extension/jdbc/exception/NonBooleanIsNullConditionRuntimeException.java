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
 * is null用の条件がBooleanではない場合の例外です。
 * 
 * @author higa
 * 
 */
public class NonBooleanIsNullConditionRuntimeException extends
        SRuntimeException {

    private static final long serialVersionUID = 1L;

    private String conditionName;

    private Class<?> valueClass;

    /**
     * {@link NonBooleanIsNullConditionRuntimeException}を作成します。
     * 
     * @param conditionName
     *            条件名
     * @param valueClass
     *            値のクラス
     */
    public NonBooleanIsNullConditionRuntimeException(String conditionName,
            Class<?> valueClass) {
        super("ESSR0743", new Object[] { conditionName, valueClass.getName() });
        this.conditionName = conditionName;
        this.valueClass = valueClass;
    }

    /**
     * 条件名を返します。
     * 
     * @return 条件名
     */
    public String getConditionName() {
        return conditionName;
    }

    /**
     * 値のクラスを返します。
     * 
     * @return 値のクラス
     */
    public Class<?> getValueClass() {
        return valueClass;
    }
}