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
package org.seasar.framework.beans.converter;

import java.text.DecimalFormat;
import java.text.ParseException;

import org.seasar.framework.beans.Converter;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.exception.ParseRuntimeException;
import org.seasar.framework.util.StringUtil;

/**
 * 数値用のコンバータです。
 * 
 * @author higa
 * 
 */
public class NumberConverter implements Converter {

    /**
     * 数値のパターンです。
     */
    protected String pattern;

    /**
     * インスタンスを構築します。
     * 
     * @param pattern
     *            数値のパターン
     */
    public NumberConverter(String pattern) {
        if (StringUtil.isEmpty(pattern)) {
            throw new EmptyRuntimeException("pattern");
        }
        this.pattern = pattern;
    }

    public Object getAsObject(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        try {
            return new DecimalFormat(pattern).parse(value);
        } catch (ParseException e) {
            throw new ParseRuntimeException(e);
        }

    }

    public String getAsString(Object value) {
        return new DecimalFormat(pattern).format(value);
    }

    public boolean isTarget(Class clazz) {
        return Number.class.isAssignableFrom(clazz);
    }

}
