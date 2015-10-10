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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.seasar.framework.exception.IORuntimeException;

/**
 * {@link InputStream}用のユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public class InputStreamUtil {

    /**
     * {@link InputStream}を閉じます。
     * 
     * @param is
     * @throws IORuntimeException
     *             {@link IOException}が発生した場合
     * @see InputStream#close()
     */
    public static void close(InputStream is) throws IORuntimeException {
        if (is == null) {
            return;
        }
        try {
            is.close();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * {@link InputStream}を閉じます。
     * 
     * @param is
     * @throws IORuntimeException
     *             {@link IOException}が発生した場合
     * @see InputStream#close()
     */
    public static void closeSilently(InputStream is) throws IORuntimeException {
        if (is == null) {
            return;
        }
        try {
            is.close();
        } catch (IOException e) {
        }
    }

    /**
     * {@link InputStream}からbyteの配列を取得します。
     * 
     * @param is
     * @return byteの配列
     * @throws IORuntimeException
     *             {@link IOException}が発生した場合
     */
    public static final byte[] getBytes(InputStream is)
            throws IORuntimeException {
        byte[] bytes = null;
        byte[] buf = new byte[8192];
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int n = 0;
            while ((n = is.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, n);
            }
            bytes = baos.toByteArray();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            if (is != null) {
                close(is);
            }
        }
        return bytes;
    }

    /**
     * {@link InputStream}の内容を {@link OutputStream}にコピーします。
     * 
     * @param is
     * @param os
     * @throws IORuntimeException
     *             {@link IOException}が発生した場合
     */
    public static final void copy(InputStream is, OutputStream os)
            throws IORuntimeException {
        byte[] buf = new byte[8192];
        try {
            int n = 0;
            while ((n = is.read(buf, 0, buf.length)) != -1) {
                os.write(buf, 0, n);
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * {@link InputStream#available()}の例外処理をラップしたメソッドです。
     * 
     * @param is
     * @return 可能なサイズ
     * @throws IORuntimeException
     *             {@link IOException}が発生した場合
     */
    public static int available(InputStream is) throws IORuntimeException {
        try {
            return is.available();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * {@link InputStream}をリセットします。
     * 
     * @param is
     * @throws IORuntimeException
     *             {@link IOException}が発生した場合
     * @see InputStream#reset()
     */
    public static void reset(InputStream is) throws IORuntimeException {
        try {
            is.reset();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

}