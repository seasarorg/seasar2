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
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.seasar.extension.dxo.converter.ConversionContext;
import org.seasar.framework.util.StringUtil;

/**
 * @author Satoshi Kimura
 * @author koichik
 */
public class SqlTimeConverter extends AbstractConverter {

    public Class[] getSourceClasses() {
        return new Class[] { Object.class };
    }

    public Class getDestClass() {
        return java.sql.Time.class;
    }

    public Object convert(final Object source, final Class destClass,
            final ConversionContext context) {
        if (source == null) {
            return null;
        }
        if (source instanceof java.sql.Time) {
            return source;
        }
        if (source instanceof Date) {
            return toTime((Date) source);
        }
        if (source instanceof Calendar) {
            return toTime((Calendar) source);
        }
        if (source instanceof Number) {
            return toTime((Number) source);
        }
        if (source instanceof String) {
            final DateFormat dateFormat = context.getTimeFormat();
            if (dateFormat != null) {
                return toTime((String) source, dateFormat);
            }
        }
        return null;
    }

    protected java.sql.Time toTime(final Date date) {
        return new java.sql.Time(date.getTime());
    }

    protected java.sql.Time toTime(final Calendar calendar) {
        return toTime(calendar.getTime());
    }

    protected java.sql.Time toTime(final Number date) {
        return new java.sql.Time(date.longValue());
    }

    protected java.sql.Time toTime(final String date,
            final DateFormat dateFormat) {
        if (StringUtil.isEmpty(date)) {
            return null;
        }
        try {
            return toTime(dateFormat.parse(date));
        } catch (final ParseException e) {
            return null;
        }
    }

}
