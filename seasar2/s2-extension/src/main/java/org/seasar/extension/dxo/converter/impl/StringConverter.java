/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.dxo.converter.impl;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import org.seasar.extension.dxo.converter.ConversionContext;

/**
 * 任意のオブジェクトから{@link String}への変換を行うコンバータです。
 * <p>
 * 変換は次のように行われます。
 * </p>
 * <ul>
 * <li>変換元のオブジェクトが{@link String}なら、変換元をそのまま変換先とします。</li>
 * <li>変換元のオブジェクトが<code>char</code>の配列なら、その配列を内容とする文字列を変換先とします。</li>
 * <li>変換元のオブジェクトが列挙なら、変換元の名前を変換先とします。</li>
 * <li>変換元のオブジェクトが{@link java.sql.Date}・{@link java.sql.Time}・{@link java.sql.Timestamp}・{@link Date}・{@link Calendar}なら、
 * その値をフォーマットに従って文字列化した結果を変換先とします。</li>
 * <li>それ以外の場合は、変換元オブジェクトの文字列表現を変換先とします。</li>
 * </ul>
 * 
 * @author Satoshi Kimura
 * @author koichik
 */
public class StringConverter extends AbstractConverter {

    public Class[] getSourceClasses() {
        return new Class[] { Object.class };
    }

    public Class getDestClass() {
        return String.class;
    }

    public Object convert(final Object source, final Class destClass,
            final ConversionContext context) {
        if (source == null) {
            return null;
        }
        if (source instanceof String) {
            return source;
        }
        if (source instanceof char[]) {
            return new String((char[]) source);
        }
        if (isEnum(source.getClass())) {
            return getEnumName(source);
        }
        if (source instanceof java.sql.Date) {
            final DateFormat formatter = context.getDateFormat();
            if (formatter != null) {
                return formatter.format((java.sql.Date) source);
            }
        }
        if (source instanceof java.sql.Time) {
            final DateFormat formatter = context.getTimeFormat();
            if (formatter != null) {
                return formatter.format((java.sql.Time) source);
            }
        }
        if (source instanceof java.sql.Timestamp) {
            final DateFormat formatter = context.getTimestampFormat();
            if (formatter != null) {
                return formatter.format((java.sql.Timestamp) source);
            }
        }
        if (source instanceof Date) {
            final DateFormat formatter = context.getDateFormat();
            if (formatter != null) {
                return formatter.format((Date) source);
            }
        }
        if (source instanceof Calendar) {
            final DateFormat formatter = context.getDateFormat();
            if (formatter != null) {
                return formatter.format(new Date(((Calendar) source)
                        .getTimeInMillis()));
            }
        }
        return source.toString();
    }

}
