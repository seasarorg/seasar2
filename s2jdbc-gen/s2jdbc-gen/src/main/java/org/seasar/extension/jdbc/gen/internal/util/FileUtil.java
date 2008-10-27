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
     * ファイルをコピーします。
     * 
     * @param src
     *            コピー元ファイル
     * @param dest
     *            コピー先ファイル
     */
    public static void copy(File src, File dest) {
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
     * ファイルの正規のパス名文字列を返します。
     * 
     * @param file
     *            ファイル
     * @return ファイルの正規パス名文字列
     */
    public static String getCanonicalPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 新しいファイルを不可分 (atomic) に生成します。
     * 
     * @param file
     *            ファイル
     * @return 指定されたファイルが存在せず、ファイルの生成に成功した場合は{@code true}、示されたファイルがすでに存在する場合は
     *         {@code false}
     */
    public static boolean createNewFile(File file) {
        try {
            return file.createNewFile();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * ファイルの正規の形式を返します。
     * 
     * @param file
     *            ファイル
     * @return 正規の形式
     */
    public static File getCanonicalFile(File file) {
        try {
            return file.getCanonicalFile();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * ディレクトリを横断します。
     * 
     * @param dir
     *            ディレクトリ
     * @param filter
     *            フィルタ
     * @param comparator
     *            コンパレータ
     * @param handler
     *            ハンドラ
     */
    public static void traverseDirectory(File dir, FilenameFilter filter,
            Comparator<File> comparator, FileHandler handler) {
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles(filter);
        if (files == null) {
            return;
        }
        Arrays.sort(files, comparator);
        for (File file : files) {
            if (file.isDirectory()) {
                traverseDirectory(file, filter, comparator, handler);
            }
            handler.handle(file);
        }
    }

    /**
     * Javaファイルを作成します。
     * 
     * @param baseDir
     *            ベースディレクトリ
     * @param packageName
     *            パッケージ名
     * @param shortClassName
     *            クラスの単純名
     * @return Javaファイル
     */
    public static File createJavaFile(File baseDir, String packageName,
            String shortClassName) {
        File packageDir;
        if (packageName == null) {
            packageDir = baseDir;
        } else {
            packageDir = new File(baseDir, packageName.replace('.',
                    File.separatorChar));
        }
        return new File(packageDir, shortClassName + ".java");
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
