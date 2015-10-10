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
package org.seasar.extension.jdbc.gen.internal.argtype;

import org.seasar.framework.util.StringUtil;

/**
 * {@link Enum}を扱う{@link ArgumentType}の実装クラスです。
 * 
 * @author taedium
 * @param <T>
 *            {@link Enum}のサブタイプ
 */
public class EnumType<T extends Enum<T>> implements ArgumentType<T> {

    /** 列挙型のクラス */
    protected Class<T> enumClass;

    /**
     * インスタンスを構築します。
     * 
     * @param enumClass
     *            列挙型のクラス
     */
    public EnumType(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    public T toObject(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        return Enum.valueOf(enumClass, value);
    }

    public String toText(T value) {
        if (value == null) {
            return "";
        }
        return value.name();
    }

}
