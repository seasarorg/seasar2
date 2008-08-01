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

import org.seasar.extension.jdbc.gen.exception.IllegalVersionValueRuntimeException;
import org.seasar.framework.util.StringConversionUtil;

/**
 * バージョンに関するユーティリティクラスです。
 * 
 * @author taedium
 */
public class VersionUtil {

    /** */
    protected VersionUtil() {
    }

    /**
     * バージョン番号の文字列をint型に変換します。
     * 
     * @param value
     *            バージョン番号の文字列
     * @return int型のバージョン番号
     */
    public static int toInt(String value) {
        int versionNo;
        try {
            versionNo = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new IllegalVersionValueRuntimeException(value);
        }
        if (versionNo < 0) {
            throw new IllegalVersionValueRuntimeException(value);
        }
        return versionNo;
    }

    /**
     * バージョン番号の文字列をint型に変換します。
     * 
     * @param versionFilePath
     *            バージョンファイルのパス
     * @param value
     *            バージョン番号の文字列
     * @return int型のバージョン番号
     */
    public static int toInt(String versionFilePath, String value) {
        int versionNo;
        try {
            versionNo = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new IllegalVersionValueRuntimeException(versionFilePath,
                    value);
        }
        if (versionNo < 0) {
            throw new IllegalVersionValueRuntimeException(versionFilePath,
                    value);
        }
        return versionNo;
    }

    /**
     * int型のバージョン番号を文字列に変換します。
     * 
     * @param versionNo
     *            int型のバージョン番号
     * @param pattern
     *            パターン
     * @return 文字列のバージョン番号
     */
    public static String toString(int versionNo, String pattern) {
        return StringConversionUtil.toString(versionNo, pattern);
    }
}
