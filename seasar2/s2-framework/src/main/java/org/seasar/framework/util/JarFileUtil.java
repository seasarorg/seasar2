/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.seasar.framework.exception.IORuntimeException;

/**
 * @author higa
 * 
 */
public class JarFileUtil {

    private JarFileUtil() {
    }

    public static JarFile create(final File file) {
        try {
            return new JarFile(file);
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static InputStream getInputStream(JarFile file, ZipEntry entry) {
        try {
            return file.getInputStream(entry);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static JarFile toJarFile(final URL jarUrl) {
        return create(new File(toJarFilePath(jarUrl)));
    }

    public static String toJarFilePath(final URL jarUrl) {
        final URL nestedUrl = URLUtil.create(jarUrl.getPath());
        final String nestedUrlPath = nestedUrl.getPath();
        final int pos = nestedUrlPath.lastIndexOf('!');
        final String jarFilePath = nestedUrlPath.substring(0, pos);
        final File jarFile = new File(URLUtil.decode(jarFilePath, "UTF8"));
        return FileUtil.getCanonicalPath(jarFile);
    }
}
