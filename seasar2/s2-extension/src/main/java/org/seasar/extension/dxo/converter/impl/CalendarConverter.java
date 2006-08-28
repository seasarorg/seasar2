/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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

import org.seasar.extension.dxo.DxoConstants;
import org.seasar.extension.dxo.converter.ConversionContext;
import org.seasar.framework.util.StringUtil;

/**
 * @author Satoshi Kimura
 * @author koichik
 */
public class CalendarConverter extends AbstractConverter {

    public Object convert(final Object source, final Class destClass,
            final ConversionContext context) {
        if (source == null) {
            return null;
        }
        if (source instanceof Calendar) {
            return source;
        }
        if (source instanceof Date) {
            return toCalendar((Date) source);
        }
        if (source instanceof Number) {
            return toCalendar((Number) source);
        }
        if (source instanceof String) {
            final DateFormat dateFormat = (DateFormat) context
                    .getContextInfo(DxoConstants.DATE_PATTERN);
            if (dateFormat != null) {
                return toCalendar((String) source, dateFormat);
            }
        }
        return null;
    }

    protected Calendar toCalendar(final Date date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    protected Calendar toCalendar(final Number date) {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(date.longValue()));
        return cal;
    }

    protected Calendar toCalendar(final String date, final DateFormat dateFormat) {
        if (StringUtil.isEmpty(date)) {
            return null;
        }
        try {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(dateFormat.parse(date));
            return cal;
        } catch (final ParseException e) {
            return null;
        }
    }

}
