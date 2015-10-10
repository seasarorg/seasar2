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

import java.sql.Time;
import java.util.Date;

import org.seasar.framework.beans.Converter;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.StringConversionUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.TimeConversionUtil;

/**
 * 時間用のコンバータです。
 * 
 * @author higa
 * 
 */
public class TimeConverter implements Converter {

    /**
     * 時間のパターンです。
     */
    protected String pattern;

    /**
     * インスタンスを構築します。
     * 
     * @param pattern
     *            時間のパターン
     */
    public TimeConverter(String pattern) {
        if (StringUtil.isEmpty(pattern)) {
            throw new EmptyRuntimeException("pattern");
        }
        this.pattern = pattern;
    }

    public Object getAsObject(String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        return TimeConversionUtil.toTime(value, pattern);
    }

    public String getAsString(Object value) {
        return StringConversionUtil.toString((Date) value, pattern);
    }

    public boolean isTarget(Class clazz) {
        return clazz == Time.class;
    }

}
