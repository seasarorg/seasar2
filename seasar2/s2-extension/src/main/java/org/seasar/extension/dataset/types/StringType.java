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
package org.seasar.extension.dataset.types;

import org.seasar.extension.dataset.ColumnType;
import org.seasar.framework.util.StringConversionUtil;

/**
 * 文字列用の {@link ColumnType}です。
 * 
 * @author higa
 * 
 */
public class StringType extends ObjectType {

    /**
     * トリムするかどうか。
     */
    protected boolean trim;

    StringType() {
        this(true);
    }

    StringType(final boolean trim) {
        this.trim = trim;
    }

    public Object convert(Object value, String formatPattern) {
        String s = StringConversionUtil.toString(value, formatPattern);
        if (s != null && trim) {
            s = s.trim();
        }
        if ("".equals(s)) {
            s = null;
        }
        return s;
    }

    public Class getType() {
        return String.class;
    }
}
