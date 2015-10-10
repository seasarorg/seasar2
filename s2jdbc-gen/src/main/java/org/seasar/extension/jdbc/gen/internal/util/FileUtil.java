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
package org.seasar.extension.jdbc.gen.internal.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Comparator;

import org.seasar.framework.exception.IORuntimeException;

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
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dest);
            copyInternal(in, out);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            CloseableUtil.close(in);
            CloseableUtil.close(out);
        }
    }

    /**
     * ファイルをコピーし追加します。
     * 
     * @param src
     *            コピー元ファイル
     * @param dest
     *            コピー先ファイル
     */
    public static void append(File src, File dest) {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dest, true);
            copyInternal(in, out);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            CloseableUtil.close(in);
            CloseableUtil.close(out);
        }
    }

    /**
     * 内部的にコピーします。
     * 
     * @param in
     *            コピー元
     * @param out
     *            コピー先
     * @throws IOException
     *             IO例外が発生した場合
     */
    protected static void copyInternal(FileInputStream in, FileOutputStream out)
            throws IOException {
        FileChannel src = in.getChannel();
        FileChannel dest = out.getChannel();
        src.transferTo(0, src.size(), dest);
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
     * 一時ファイルを作成します。
     * 
     * @param prefix
     *            接頭辞文字列。3 文字以上の長さが必要である
     * @param suffix
     *            接尾辞文字列。null も指定でき、その場合は、接尾辞 ".tmp" が使用される
     * @return
     */
    public static File createTempFile(String prefix, String suffix) {
        try {
            return File.createTempFile(prefix, suffix);
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
