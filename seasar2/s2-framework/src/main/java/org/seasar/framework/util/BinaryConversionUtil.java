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
package org.seasar.framework.util;

/**
 * byte配列用の変換ユーティリティです。
 * 
 * @author higa
 * 
 */
public class BinaryConversionUtil {

    /**
     * インスタンスを構築します。
     */
    protected BinaryConversionUtil() {
    }

    /**
     * byteの配列に変換します。
     * 
     * @param o
     * @return byteの配列
     */
    public static byte[] toBinary(Object o) {
        if (o instanceof byte[]) {
            return (byte[]) o;
        } else if (o == null) {
            return null;
        } else {
            if (o instanceof String) {
                return ((String) o).getBytes();
            }
            throw new IllegalArgumentException(o.getClass().toString());
        }
    }
}