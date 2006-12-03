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
import org.seasar.extension.dxo.exception.ConversionRuntimeException;
import org.seasar.framework.util.StringUtil;

/**
 * @author Satoshi Kimura
 * @author koichik
 */
public abstract class NumberConverter extends AbstractConverter {

    protected static final Integer TRUE = new Integer(1);

    protected static final Integer FALSE = new Integer(0);

    public Class[] getSourceClasses() {
        return new Class[] { Number.class, CharSequence.class, Boolean.class,
                Object.class };
    }

    public Object convert(final Object source, final Class destClass,
            final ConversionContext context) {
        if (source == null) {
            return null;
        }
        if (source instanceof Number) {
            return convert((Number) source);
        }
        if (source instanceof CharSequence) {
            return convert(source.toString());
        }
        if (source instanceof Boolean) {
            final boolean b = ((Boolean) source).booleanValue();
            if (b) {
                return convert(TRUE);
            }
            return convert(FALSE);
        }
        throw new ConversionRuntimeException(source.getClass());
    }

    protected abstract Number convert(Number number);

    protected Number convert(final String number) {
        if (StringUtil.isEmpty(number)) {
            return null;
        }
        final BigDecimal decimal = new BigDecimal(number);
        return convert(decimal);
    }

}
