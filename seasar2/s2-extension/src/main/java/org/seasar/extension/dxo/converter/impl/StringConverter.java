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

import org.seasar.extension.dxo.DxoConstants;
import org.seasar.extension.dxo.converter.ConversionContext;

/**
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
        if (source instanceof java.sql.Date) {
            final DateFormat formatter = (DateFormat) context
                    .getContextInfo(DxoConstants.DATE_PATTERN);
            if (formatter != null) {
                return formatter.format((java.sql.Date) source);
            }
        }
        if (source instanceof java.sql.Time) {
            final DateFormat formatter = (DateFormat) context
                    .getContextInfo(DxoConstants.TIME_PATTERN);
            if (formatter != null) {
                return formatter.format((java.sql.Time) source);
            }
        }
        if (source instanceof java.sql.Timestamp) {
            final DateFormat formatter = (DateFormat) context
                    .getContextInfo(DxoConstants.TIMESTAMP_PATTERN);
            if (formatter != null) {
                return formatter.format((java.sql.Timestamp) source);
            }
        }
        if (source instanceof Date) {
            final DateFormat formatter = (DateFormat) context
                    .getContextInfo(DxoConstants.DATE_PATTERN);
            if (formatter != null) {
                return formatter.format((Date) source);
            }
        }
        if (source instanceof Calendar) {
            final DateFormat formatter = (DateFormat) context
                    .getContextInfo(DxoConstants.DATE_PATTERN);
            if (formatter != null) {
                return formatter.format(new Date(((Calendar) source)
                        .getTimeInMillis()));
            }
        }
        return source.toString();
    }

}
