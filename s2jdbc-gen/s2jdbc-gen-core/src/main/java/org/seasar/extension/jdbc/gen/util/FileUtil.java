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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.FileInputStreamUtil;
import org.seasar.framework.util.FileOutputStreamUtil;
import org.seasar.framework.util.InputStreamUtil;
import org.seasar.framework.util.OutputStreamUtil;

/**
 * @author taedium
 * 
 */
public class FileUtil {

    private FileUtil() {
    }

    public static void copyDirectory(File srcDir, File destDir,
            FilenameFilter filter) {
        if (!srcDir.isDirectory()) {
            throw new IllegalArgumentException("srcDir");
        }
        if (destDir.exists()) {
            throw new IllegalArgumentException("destDir");
        }
        if (!getCanonicalPath(srcDir).equals(getCanonicalPath(srcDir))
                && getCanonicalPath(srcDir)
                        .startsWith(getCanonicalPath(srcDir))) {
            throw new IllegalArgumentException("destDir");
        }
        copyDir(srcDir, destDir, filter);
    }

    protected static void copyDir(File srcDir, File destDir,
            FilenameFilter filter) {
        destDir.mkdirs();
        File[] srcFiles = srcDir.listFiles(filter);
        for (File src : srcFiles) {
            File dest = new File(destDir, src.getName());
            if (src.isDirectory()) {
                copyDir(src, dest, filter);
            } else {
                copyFile(src, dest);
            }
        }
    }

    protected static void copyFile(File src, File dest) {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(FileInputStreamUtil.create(src));
            out = new BufferedOutputStream(FileOutputStreamUtil.create(dest));
            byte[] buf = new byte[1024];
            int length;
            while (-1 < (length = in.read(buf))) {
                out.write(buf, 0, length);
                out.flush();
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            InputStreamUtil.close(in);
            OutputStreamUtil.close(out);
        }
    }

    public static void deleteDirectory(File dir) {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("dir");
        }
        if (!dir.exists()) {
            throw new IllegalArgumentException("dir");
        }
        deleteDir(dir);
    }

    protected static void deleteDir(File dir) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                deleteDir(file);
                file.delete();
            } else {
                deleteFile(file);
            }
        }
        dir.delete();
    }

    protected static void deleteFile(File file) {
        file.delete();
    }

    public static boolean createNewFile(File file) {
        try {
            return file.createNewFile();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static String getCanonicalPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
}
