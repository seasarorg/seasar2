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

import java.util.regex.Pattern;

/**
 * @author taedium
 * 
 */
public class DumpUtil {

    protected static String QUOTE = "\"";

    protected static String ESCAPED_QUOTE = "\"\"";

    protected static Pattern ENCODE_TARGET_PATTERN = Pattern
            .compile(".*(\"|\\r\\n|,).*");

    protected static Pattern DECODE_TARGET_PATTERN = Pattern.compile("^\".+\"");

    /**
     * 
     */
    protected DumpUtil() {
    }

    public static String encode(String value) {
        if (value == null) {
            return null;
        }
        if (ENCODE_TARGET_PATTERN.matcher(value).matches()) {
            return QUOTE + value.replace(QUOTE, ESCAPED_QUOTE) + QUOTE;
        }
        return value;
    }

    public static String decode(String value) {
        if (value == null) {
            return null;
        }
        if (DECODE_TARGET_PATTERN.matcher(value).matches()) {
            String s = value.substring(1, value.length() - 1);
            return s.replace(ESCAPED_QUOTE, QUOTE);
        }
        return value;
    }
}
