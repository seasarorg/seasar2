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
package org.seasar.framework.util;

import java.io.File;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * リソースをトラバースするためのクラスです。
 * 
 * @author taedium
 */
public class ResourceTraversal {

    /**
     * リソースを処理するインターフェースです。
     * 
     */
    public interface ResourceHandler {
        /**
         * リソースを処理します。
         * 
         * @param path
         * @param is
         */
        void processResource(String path, InputStream is);
    }

    private ResourceTraversal() {
    }

    /**
     * トラバースします。
     * 
     * @param rootDir
     * @param handler
     */
    public static void forEach(final File rootDir, final ResourceHandler handler) {
        forEach(rootDir, null, handler);
    }

    /**
     * トラバースします。
     * 
     * @param rootDir
     * @param baseDirectory
     * @param handler
     */
    public static void forEach(final File rootDir, final String baseDirectory,
            final ResourceHandler handler) {
        final File baseDir = getBaseDir(rootDir, baseDirectory);
        if (baseDir.exists()) {
            traverseFileSystem(rootDir, baseDir, handler);
        }
    }

    /**
     * トラバースします。
     * 
     * @param jarFile
     * @param handler
     */
    public static void forEach(final JarFile jarFile,
            final ResourceHandler handler) {
        final Enumeration enumeration = jarFile.entries();
        while (enumeration.hasMoreElements()) {
            final JarEntry entry = (JarEntry) enumeration.nextElement();
            if (!entry.isDirectory()) {
                final String entryName = entry.getName().replace('\\', '/');
                final InputStream is = JarFileUtil.getInputStream(jarFile,
                        entry);
                try {
                    handler.processResource(entryName, is);
                } finally {
                    InputStreamUtil.close(is);
                }
            }
        }
    }

    private static void traverseFileSystem(final File rootDir,
            final File baseDir, final ResourceHandler handler) {
        final File[] files = baseDir.listFiles();
        for (int i = 0; i < files.length; ++i) {
            final File file = files[i];
            if (file.isDirectory()) {
                traverseFileSystem(rootDir, file, handler);
            } else {
                final int pos = FileUtil.getCanonicalPath(rootDir).length();
                final String filePath = FileUtil.getCanonicalPath(file);
                final String resourcePath = filePath.substring(pos + 1)
                        .replace('\\', '/');
                final InputStream is = FileInputStreamUtil.create(file);
                try {
                    handler.processResource(resourcePath, is);
                } finally {
                    InputStreamUtil.close(is);
                }
            }
        }
    }

    private static File getBaseDir(final File rootDir,
            final String baseDirectory) {
        File baseDir = rootDir;
        if (baseDirectory != null) {
            final String[] names = baseDirectory.split("/");
            for (int i = 0; i < names.length; i++) {
                baseDir = new File(baseDir, names[i]);
            }
        }
        return baseDir;
    }
}
