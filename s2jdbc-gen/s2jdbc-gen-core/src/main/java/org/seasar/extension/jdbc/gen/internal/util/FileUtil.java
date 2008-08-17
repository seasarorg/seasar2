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
package org.seasar.extension.jdbc.gen.internal.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.FileInputStreamUtil;
import org.seasar.framework.util.FileOutputStreamUtil;
import org.seasar.framework.util.InputStreamUtil;
import org.seasar.framework.util.OutputStreamUtil;

/**
 * {@link File}に関するユーティリティクラスです。
 * 
 * @author taedium
 */
public class FileUtil {

    /**
     * 
     */
    protected FileUtil() {
    }

    /**
     * ディレクトリをコピーします。
     * 
     * @param srcDir
     *            コピー元ディレクトリ
     * @param destDir
     *            コピー先ディレクトリ
     * @param filter
     *            フィルタ
     */
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

    /**
     * ディレクトリを再帰的にコピーします。
     * 
     * @param srcDir
     *            コピー元ディレクトリ
     * @param destDir
     *            コピー先ディレクトリ
     * @param filter
     *            フィルタ
     */
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

    /**
     * ファイルをコピーします。
     * 
     * @param src
     *            コピー元ファイル
     * @param dest
     *            コピー先ファイル
     */
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

    /**
     * ディレクトリを削除します。
     * 
     * @param dir
     *            ディレクトリ
     */
    public static void deleteDirectory(File dir) {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("dir");
        }
        if (!dir.exists()) {
            throw new IllegalArgumentException("dir");
        }
        deleteDir(dir);
    }

    /**
     * ディレクトリを再帰的に削除します。
     * 
     * @param dir
     *            ディレクトリ
     */
    protected static void deleteDir(File dir) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                deleteDir(file);
                file.delete();
            } else {
                file.delete();
            }
        }
        dir.delete();
    }

    /**
     * この抽象パス名の正規の形式を返します。
     * 
     * @param file
     *            ファイル
     * @return この抽象パス名と同じファイルまたはディレクトリを示す正規パス名文字列
     */
    public static String getCanonicalPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * ディレクトリを横断します。
     * 
     * @param dir
     * @param filter
     * @param comparator
     * @param handler
     */
    public static void traverseDirectory(File dir, FilenameFilter filter,
            Comparator<File> comparator, FileHandler handler) {
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles(filter);
        Arrays.sort(files, comparator);
        for (File file : files) {
            if (file.isDirectory()) {
                traverseDirectory(file, filter, comparator, handler);
            }
            handler.handle(file);
        }

    }

    /**
     * ファイルを扱うインタフェースです・
     * 
     * @author taedium
     */
    public interface FileHandler {

        /**
         * 処理します。
         * 
         * @param file
         */
        void handle(File file);
    }
}
