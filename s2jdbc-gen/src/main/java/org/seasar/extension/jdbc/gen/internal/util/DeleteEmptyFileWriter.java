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
import java.io.FilterWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.FileInputStreamUtil;

/**
 * 空のファイルを削除する{@link Writer}の実装です。
 * 
 * @author taedium
 */
public class DeleteEmptyFileWriter extends FilterWriter {

    /** 書き込みが行われた場合{@code true} */
    protected boolean written;

    /** 削除された場合{@code true} */
    protected boolean deleted;

    /** 書き込み先のファイル */
    protected File file;

    /**
     * コンストラクタを構築します。
     * 
     * @param writer
     *            ライタ
     * @param file
     *            書き込み先のファイル
     */
    public DeleteEmptyFileWriter(Writer writer, File file) {
        super(writer);
        if (file == null) {
            throw new NullPointerException("file");
        }
        this.file = file;
    }

    @Override
    public void close() throws IOException {
        super.close();
        if (!written && file.exists() && isEmpty()) {
            deleted = file.delete();
        }
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        if (len <= 0) {
            return;
        }
        written = true;
        super.write(cbuf, off, len);
    }

    @Override
    public void write(int c) throws IOException {
        written = true;
        super.write(c);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        if (len <= 0) {
            return;
        }
        written = true;
        super.write(str, off, len);
    }

    /**
     * ファイルが空の場合に{@code true}を返します。
     * 
     * @return ファイルが空の場合は{@code true}、そうでない場合{@code false}
     */
    protected boolean isEmpty() {
        InputStream is = FileInputStreamUtil.create(file);
        try {
            return is.read() == -1;
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * ファイルが削除された場合{@code true}を返します。
     * 
     * @return 削除された場合{@code true}、そうでない場合{@code false}
     */
    public boolean isDeleted() {
        return deleted;
    }

}
