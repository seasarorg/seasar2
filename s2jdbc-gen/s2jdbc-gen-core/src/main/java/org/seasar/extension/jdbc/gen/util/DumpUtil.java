/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.util;

import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
public class DumpUtil {

    protected static String ONE_QUOTE = "\"";

    protected static String TWO_QUOTES = "\"\"";

    /**
     * 
     */
    protected DumpUtil() {
    }

    public static String encode(String value) {
        if (value == null) {
            return null;
        }
        return ONE_QUOTE + value.replace(ONE_QUOTE, TWO_QUOTES) + ONE_QUOTE;
    }

    public static String decode(String value) {
        if (value == null) {
            return null;
        }
        value = StringUtil.trimPrefix(value, ONE_QUOTE);
        value = StringUtil.trimSuffix(value, ONE_QUOTE);
        return value.replace(TWO_QUOTES, ONE_QUOTE);
    }

}
