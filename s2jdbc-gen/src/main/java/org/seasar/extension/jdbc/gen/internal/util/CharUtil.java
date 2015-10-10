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
package org.seasar.extension.jdbc.gen.internal.util;

/**
 * charに関するユーティリティクラスです。
 * 
 * @author taedium
 */
public class CharUtil {

    /**
     * 
     */
    protected CharUtil() {
    }

    /**
     * 文字がAsciiの場合{@code true}を返します。
     * 
     * @param ch
     *            文字
     * @return Asciiの場合{@code true}
     */
    public static boolean isAscii(char ch) {
        return ((ch & 0xFFFFFF80) == 0);
    }
}
