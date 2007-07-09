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
package org.seasar.extension.jdbc.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * バインド変数用のユーティリティです。
 * 
 * @author higa
 * 
 */
public final class BindVariableUtil {

    private BindVariableUtil() {
    }

    /**
     * バインド変数を文字列として返します。
     * 
     * @param bindVariable
     *            バインド変数
     * @return バインド変数の文字列表現
     */
    public static String getBindVariableText(Object bindVariable) {
        if (bindVariable instanceof String) {
            return "'" + bindVariable + "'";
        } else if (bindVariable instanceof Number) {
            return bindVariable.toString();
        } else if (bindVariable instanceof Timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
            return "'" + sdf.format((java.util.Date) bindVariable) + "'";
        } else if (bindVariable instanceof java.util.Date) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return "'" + sdf.format((java.util.Date) bindVariable) + "'";
        } else if (bindVariable instanceof Boolean) {
            return bindVariable.toString();
        } else if (bindVariable == null) {
            return "null";
        } else {
            return "'" + bindVariable.toString() + "'";
        }
    }
}