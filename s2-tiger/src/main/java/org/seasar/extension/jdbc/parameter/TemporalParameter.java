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

import java.util.Calendar;
import java.util.Date;

import javax.persistence.TemporalType;

import org.seasar.extension.sql.SqlArgWrapper;

/**
 * {@link Date}または{@link Calendar}型の値と{@link TemporalType}の組み合わせをラップするクラスです。
 * 
 * @author taedium
 * 
 */
public class TemporalParameter implements SqlArgWrapper {

    /** 値 */
    protected Object value;

    /** 時制のクラス */
    protected Class<?> temporalClass;

    /** 時制の種別 */
    protected TemporalType temporalType;

    /**
     * インスタンスを作成します。
     * 
     * @param value
     *            値
     * @param temporalType
     *            時制の種別
     */
    public TemporalParameter(Date value, TemporalType temporalType) {
        if (temporalType == null) {
            throw new NullPointerException("temporalType");
        }
        this.value = value;
        this.temporalClass = Date.class;
        this.temporalType = temporalType;
    }

    /**
     * インスタンスを作成します。
     * 
     * @param value
     *            値
     * @param clazz
     *            時制のクラス
     * @param temporalType
     *            時制の種別
     */
    public TemporalParameter(Calendar value, TemporalType temporalType) {
        if (temporalType == null) {
            throw new NullPointerException("temporalType");
        }
        this.value = value;
        this.temporalClass = Calendar.class;
        this.temporalType = temporalType;
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
     * 時制のクラスを返します。
     * 
     * @return 時制のクラス
     */
    public Class<?> getTemporalClass() {
        return temporalClass;
    }

    /**
     * 時制の種別を返します。
     * 
     * @return 時制の種別
     */
    public TemporalType getTemporalType() {
        return temporalType;
    }

}
