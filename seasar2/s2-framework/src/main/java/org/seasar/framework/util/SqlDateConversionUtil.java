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
package org.seasar.framework.util;

import java.sql.Date;

/**
 * {@link Date}用の変換ユーティリティです。
 * 
 * @author higa
 * 
 */
public class SqlDateConversionUtil {

    /**
     * インスタンスを構築します。
     */
    protected SqlDateConversionUtil() {
    }

    /**
     * {@link Date}に変換します。
     * 
     * @param o
     * @return {@link Date}
     */
    public static Date toDate(Object o) {
        return toDate(o, null);
    }

    /**
     * {@link Date}に変換します。
     * 
     * @param o
     * @param pattern
     * @return {@link Date}
     */
    public static Date toDate(Object o, String pattern) {
        if (o instanceof Date) {
            return (Date) o;
        }
        java.util.Date date = DateConversionUtil.toDate(o, pattern);
        if (date != null) {
            return new Date(date.getTime());
        }
        return null;
    }
}
