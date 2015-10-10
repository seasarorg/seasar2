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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import org.seasar.framework.exception.IORuntimeException;

/**
 * {@link Reader}用のユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public class ReaderUtil {

    private static final int BUF_SIZE = 8192;

    /**
     * インスタンスを構築します。
     */
    protected ReaderUtil() {
    }

    /**
     * テキストを読み込みます。
     * 
     * @param reader
     * @return テキスト
     * @throws IORuntimeException
     */
    public static String readText(Reader reader) throws IORuntimeException {
        BufferedReader in = new BufferedReader(reader);
        StringBuffer out = new StringBuffer(100);
        try {
            try {
                char[] buf = new char[BUF_SIZE];
                int n;
                while ((n = in.read(buf)) >= 0) {
                    out.append(buf, 0, n);
                }
            } finally {
                in.close();
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        return out.toString();
    }

}
