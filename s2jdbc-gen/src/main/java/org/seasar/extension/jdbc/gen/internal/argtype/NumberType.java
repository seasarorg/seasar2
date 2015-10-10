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

import org.seasar.framework.util.NumberConversionUtil;
import org.seasar.framework.util.StringConversionUtil;
import org.seasar.framework.util.StringUtil;

/**
 * {@link Number}を扱う{@link ArgumentType}の実装クラスです。
 * 
 * @author taedium
 * @param <T>
 *            {@link Number}のサブタイプ
 */
public class NumberType<T extends Number> implements ArgumentType<T> {

    /** 数値を表すクラス */
    protected Class<T> numberClass;

    /**
     *インスタンスを構築します。
     * 
     * @param numberClass
     *            数値を表すクラス
     */
    public NumberType(Class<T> numberClass) {
        this.numberClass = numberClass;
    }

    public T toObject(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        Object number = NumberConversionUtil.convertNumber(numberClass, value);
        return numberClass.cast(number);
    }

    public String toText(T value) {
        if (value == null) {
            return "";
        }
        return StringConversionUtil.toString(value);
    }

}
