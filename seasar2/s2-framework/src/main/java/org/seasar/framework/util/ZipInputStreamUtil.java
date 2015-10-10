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

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.seasar.framework.exception.IORuntimeException;

/**
 * {@link java.util.zip.ZipInputStream}を扱うユーティリティクラスです。
 * 
 * @author koichik
 */
public class ZipInputStreamUtil {

    /**
     * {@link ZipInputStream#getNextEntry()}の例外処理をラップするメソッドです。
     * 
     * @param zis
     * @return {@link ZipEntry}
     * @see ZipInputStream#getNextEntry()
     */
    public static ZipEntry getNextEntry(final ZipInputStream zis) {
        try {
            return zis.getNextEntry();
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * {@link ZipInputStream#reset()}の例外処理をラップするメソッドです。
     * 
     * @param zis
     * @see ZipInputStream#reset()
     */
    public static void reset(final ZipInputStream zis) {
        try {
            zis.reset();
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * {@link ZipInputStream#closeEntry()}の例外処理をラップするメソッドです。
     * 
     * @param zis
     * @see ZipInputStream#closeEntry()
     */
    public static void closeEntry(final ZipInputStream zis) {
        try {
            zis.closeEntry();
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

}
