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

import java.math.BigDecimal;

import org.seasar.extension.dxo.converter.ConversionContext;

/**
 * @author Satoshi Kimura
 * @author koichik
 */
public class BooleanConverter extends AbstractConverter {

    protected static final String[] TRUES = new String[] { "yes", "y", "true",
            "on", "1", };

    protected static final BigDecimal ZERO = new BigDecimal("0");

    public Object convert(final Object source, final Class destClass,
            final ConversionContext context) {
        if (source == null) {
            return null;
        }
        if (source instanceof Boolean) {
            return source;
        }
        if (source instanceof Number) {
            return toBoolean((Number) source);
        }
        return toBoolean(source.toString());
    }

    protected Object toBoolean(final String value) {
        for (int i = 0; i < TRUES.length; ++i) {
            if (TRUES[i].equalsIgnoreCase(value)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    protected Object toBoolean(final Number value) {
        final BigDecimal decimal = new BigDecimal(value.toString());
        if (decimal.compareTo(ZERO) > 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
