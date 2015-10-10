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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import org.seasar.framework.exception.IORuntimeException;

/**
 * {@link File}を扱うユーティリティ・クラスです。
 * 
 * @author higa
 */
public class FileUtil {

    /**
     * インスタンスを構築します。
     */
    protected FileUtil() {
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
     * この抽象パス名を<code>file:</code> URLに変換します。
     * 
     * @param file
     *            ファイル
     * @return ファイルURLを表すURLオブジェクト
     */
    public static URL toURL(final File file) {
        try {
            return file.toURL();
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * ファイルの内容をバイト配列に読み込んで返します。
     * 
     * @param file
     *            ファイル
     * @return ファイルの内容を読み込んだバイト配列
     */
    public static byte[] getBytes(File file) {
        return InputStreamUtil.getBytes(FileInputStreamUtil.create(file));
    }

    /**
     * <code>src</code>の内容を<code>dest</code>にコピーします。
     * 
     * @param src
     *            コピー元のファイル
     * @param dest
     *            コピー先のファイル
     */
    public static void copy(File src, File dest) {
        if (dest.exists() && !dest.canWrite()) {
            return;
        }
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
     * バイトの配列をファイルに書き出します。
     * 
     * @param path
     *            ファイルのパス
     * @param data
     *            バイトの配列
     * @param offset
     *            オフセット
     * @param length
     *            配列の長さ
     * @throws NullPointerException
     *             pathやdataがnullの場合。
     */
    public static void write(String path, byte[] data) {
        if (path == null) {
            throw new NullPointerException("path");
        }
        if (data == null) {
            throw new NullPointerException("data");
        }
        write(path, data, 0, data.length);
    }

    /**
     * バイトの配列をファイルに書き出します。
     * 
     * @param path
     *            ファイルのパス
     * @param data
     *            バイトの配列
     * @param offset
     *            オフセット
     * @param length
     *            配列の長さ
     * @throws NullPointerException
     *             pathやdataがnullの場合。
     */
    public static void write(String path, byte[] data, int offset, int length) {
        if (path == null) {
            throw new NullPointerException("path");
        }
        if (data == null) {
            throw new NullPointerException("data");
        }
        try {
            OutputStream out = new BufferedOutputStream(new FileOutputStream(
                    path));
            try {
                out.write(data, offset, length);
            } finally {
                out.close();
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
}
