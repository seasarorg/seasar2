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
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.util.tiger.Maps;

/**
 * 値をラップし特別な意味を持たせるクラスです。
 * 
 * @author taedium
 * 
 */
public class Parameter {

    /**
     * {@link TemporalType#DATE}用のパラメータを作成します。
     * 
     * @param value
     *            値
     * 
     * @return 時制パラメータ
     */
    public static TemporalParameter date(Date value) {
        return new TemporalParameter(value, TemporalType.DATE);
    }

    /**
     * {@link TemporalType#DATE}用のパラメータを作成します。
     * 
     * @param value
     *            値
     * @return 時制パラメータ
     */
    public static TemporalParameter date(Calendar value) {
        return new TemporalParameter(value, TemporalType.DATE);
    }

    /**
     * {@link TemporalType#TIME}用のパラメータを作成します。
     * 
     * @param value
     *            値
     * @return 時制パラメータ
     */
    public static TemporalParameter time(Date value) {
        return new TemporalParameter(value, TemporalType.TIME);
    }

    /**
     * {@link TemporalType#TIME}用のパラメータを作成します。
     * 
     * @param value
     *            値
     * @return 時制パラメータ
     */
    public static TemporalParameter time(Calendar value) {
        return new TemporalParameter(value, TemporalType.TIME);
    }

    /**
     * {@link TemporalType#TIMESTAMP}用のパラメータを作成します。
     * 
     * @param value
     *            値
     * @return 時制パラメータ
     */
    public static TemporalParameter timestamp(Date value) {
        return new TemporalParameter(value, TemporalType.TIMESTAMP);
    }

    /**
     * {@link TemporalType#TIMESTAMP}用のパラメータを作成します。
     * 
     * @param value
     *            値
     * @return 時制パラメータ
     */
    public static TemporalParameter timestamp(Calendar value) {
        return new TemporalParameter(value, TemporalType.TIMESTAMP);
    }

    /**
     * ラージオブジェクト用のパラメータを作成します。
     * 
     * @param value
     *            値
     * @return LOBパラメータ
     */
    public static LobParameter lob(byte[] value) {
        return new LobParameter(value);
    }

    /**
     * ラージオブジェクト用のパラメータを作成します。
     * 
     * @param value
     *            値
     * @return LOBパラメータ
     */
    public static LobParameter lob(Serializable value) {
        return new LobParameter(value);
    }

    /**
     * ラージオブジェクト用のパラメータを作成します。
     * 
     * @param value
     *            値
     * @return LOBパラメータ
     */
    public static LobParameter lob(String value) {
        return new LobParameter(value);
    }

    /**
     * プロパティ記述に従い必要ならば値をラップします。
     * 
     * @param propertyDesc
     *            プロパティ記述
     * @param value
     *            値
     * @return
     */
    public static Object wrapIfNecessary(PropertyDesc propertyDesc, Object value) {
        Field field = propertyDesc.getField();
        if (field == null) {
            return value;
        }
        Class<?> clazz = propertyDesc.getPropertyType();
        if (field.isAnnotationPresent(Lob.class)) {
            if (String.class == clazz) {
                return new LobParameter(String.class.cast(value));
            } else if (byte[].class == clazz) {
                return new LobParameter(byte[].class.cast(value));
            } else if (Serializable.class.isAssignableFrom(clazz)) {
                return new LobParameter(Serializable.class.cast(value));
            }
        }
        Temporal temporal = field.getAnnotation(Temporal.class);
        if (temporal != null) {
            if (Date.class == clazz) {
                return new TemporalParameter(Date.class.cast(value), temporal
                        .value());
            } else if (Calendar.class == clazz) {
                return new TemporalParameter(Calendar.class.cast(value),
                        temporal.value());
            }
        }
        return value;
    }

    /**
     * 流れるようなインタフェースでSQLファイルやストアドに渡す{@link Map}型のパラメータを組み立てます。
     * <p>
     * 以下のように利用します。
     * </p>
     * <code>
     * jdbcManager
     *   .selectBySqlFile(Employee.class, PATH, 
     *     <strong>params("name", name).$("job", job).$("limit", 10).$()</strong>)
     *   .getResultList();
     * </code>
     * <p>
     * 最初に{@link #params(String, Object)}を呼び出し、 続けて
     * {@link Maps#$(String, Object)}でパラメータを追加し、 最後に
     * {@link Maps#$()}を呼び出すことで{@link Map}になります。
     * </p>
     * 
     * @param name
     *            パラメータの名前
     * @param param
     *            パラメータの値
     * @return 指定されたパラメータを持つ{@link Map}を構築するための{@link Maps}
     */
    public static Maps<String, Object> params(final String name,
            final Object param) {
        return Maps.map(name, param);
    }

}
