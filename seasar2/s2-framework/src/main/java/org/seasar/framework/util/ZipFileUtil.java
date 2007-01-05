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
package org.seasar.framework.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.seasar.framework.exception.IORuntimeException;

/**
 * @author higa
 * 
 */
public class ZipFileUtil {

    private ZipFileUtil() {
    }

    public static ZipFile create(final String file) {
        try {
            return new ZipFile(file);
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static ZipFile create(final File file) {
        try {
            return new ZipFile(file);
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static InputStream getInputStream(ZipFile file, ZipEntry entry) {
        try {
            return file.getInputStream(entry);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static ZipFile toZipFile(final URL zipUrl) {
        return create(new File(toZipFilePath(zipUrl)));
    }

    public static String toZipFilePath(final URL zipUrl) {
        final String urlString = zipUrl.getPath();
        final int pos = urlString.lastIndexOf('!');
        final String zipFilePath = urlString.substring(0, pos);
        final File zipFile = new File(URLUtil.decode(zipFilePath, "UTF8"));
        return FileUtil.getCanonicalPath(zipFile);
    }
}
