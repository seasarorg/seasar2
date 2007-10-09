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
package org.seasar.extension.jdbc;

/**
 * where句の条件タイプです。
 * 
 * @author higa
 * 
 */
public enum ConditionType {

    /**
     * =です。
     */
    EQ,
    /**
     * &lt;&gt;です。
     */
    NE,
    /**
     * &lt;です。
     */
    LT,
    /**
     * &lt;=です。
     */
    LE,
    /**
     * &gt;です。
     */
    GT,
    /**
     * &gt;=です。
     */
    GE,
    /**
     * inです。
     */
    IN,
    /**
     * not inです。
     */
    NOT_IN,
    /**
     * likeです。
     */
    LIKE,
    /**
     * like '?%'です。
     */
    STARTS,
    /**
     * like '%?'です。
     */
    ENDS,
    /**
     * like '%?%'です。
     */
    CONTAINS,
    /**
     * is nullです。
     */
    IS_NULL,
    /**
     * is not nullです。
     */
    IS_NOT_NULL;

    /**
     * 名前に応じた条件タイプを返します。
     * 
     * @param name
     *            名前
     * @return 条件タイプ
     */
    public static ConditionType getConditionType(String name) {
        if (NE.hasSuffix(name)) {
            return NE;
        } else if (LT.hasSuffix(name)) {
            return LT;
        } else if (LE.hasSuffix(name)) {
            return LE;
        } else if (GT.hasSuffix(name)) {
            return GT;
        } else if (GE.hasSuffix(name)) {
            return GE;
        } else if (NOT_IN.hasSuffix(name)) {
            return NOT_IN;
        } else if (IN.hasSuffix(name)) {
            return IN;
        } else if (LIKE.hasSuffix(name)) {
            return LIKE;
        } else if (STARTS.hasSuffix(name)) {
            return STARTS;
        } else if (ENDS.hasSuffix(name)) {
            return ENDS;
        } else if (CONTAINS.hasSuffix(name)) {
            return CONTAINS;
        } else if (IS_NULL.hasSuffix(name)) {
            return IS_NULL;
        } else if (IS_NOT_NULL.hasSuffix(name)) {
            return IS_NOT_NULL;
        }
        return EQ;
    }

    /**
     * サフィックスを返します。
     * 
     * @return サフィックス
     */
    public String getSuffix() {
        return "_" + name();
    }

    /**
     * サフィックスを持っているかどうかを返します。
     * 
     * @param name
     *            名前
     * @return サフィックスを持っているかどうか
     */
    public boolean hasSuffix(String name) {
        return name.endsWith(getSuffix());
    }

    /**
     * サフィックスを削除します。
     * 
     * @param s
     *            文字列
     * @return サフィックスが削除された文字列
     */
    public String removeSuffix(String s) {
        String suffix = getSuffix();
        if (s.endsWith(suffix)) {
            return s.substring(0, s.length() - getSuffix().length());
        }
        return s;
    }
}