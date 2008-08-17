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
package org.seasar.extension.jdbc.gen.version;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import org.seasar.extension.jdbc.gen.DdlInfoFile;
import org.seasar.extension.jdbc.gen.exception.IllegalVersionRuntimeException;
import org.seasar.extension.jdbc.gen.exception.NextVersionExceededRuntimeException;
import org.seasar.extension.jdbc.gen.util.CloseableUtil;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.FileInputStreamUtil;
import org.seasar.framework.util.FileOutputStreamUtil;
import org.seasar.framework.util.InputStreamReaderUtil;
import org.seasar.framework.util.ReaderUtil;

/**
 * {@link DdlInfoFile}の実装クラスです。
 * 
 * @author taedium
 */
public class DdlInfoFileImpl implements DdlInfoFile {

    /** ロガー */
    protected static Logger logger = Logger.getLogger(DdlInfoFileImpl.class);

    /** エンコーディング */
    protected static final String ENCODING = "UTF-8";

    /** 最新バージョンを表す文字列 */
    protected static String LATEST_VERSION = "latest";

    /** DDLファイル */
    protected File file;

    /** バージョン番号 */
    protected Integer versionNo;

    /**
     * インスタンスを構築します。
     * 
     * @param file
     *            ファイル
     */
    public DdlInfoFileImpl(File file) {
        if (file == null) {
            throw new NullPointerException("file");
        }
        this.file = file;
    }

    public int getCurrentVersionNo() {
        return getCurrentVersionNoInternal();
    }

    /**
     * バージョン番号を返します。
     * 
     * @return バージョン番号
     */
    protected int getCurrentVersionNoInternal() {
        if (versionNo != null) {
            return versionNo;
        }
        if (!file.exists()) {
            logger.log("IS2JDBCGen0003", new Object[] { file.getPath() });
            versionNo = 0;
            return versionNo;
        }
        InputStream is = FileInputStreamUtil.create(file);
        Reader reader = InputStreamReaderUtil.create(is, ENCODING);
        String value = ReaderUtil.readText(reader).trim();
        return convertToInt(value);
    }

    public int getNextVersionNo() {
        return getNextVersionNoInternal();
    }

    /**
     * 次のバージョン番号を返します。
     * 
     * @return 次のバージョン番号
     */
    protected int getNextVersionNoInternal() {
        long nextVersionNo = (long) getCurrentVersionNoInternal() + 1;
        if (nextVersionNo > Integer.MAX_VALUE) {
            throw new NextVersionExceededRuntimeException(file.getPath());
        }
        return (int) nextVersionNo;
    }

    public int getVersionNo(String version) {
        if (LATEST_VERSION.equalsIgnoreCase(version)) {
            return getCurrentVersionNoInternal();
        }
        return convertToInt(version);
    }

    public void applyNextVersionNo() {
        int versionNo = getNextVersionNoInternal();
        OutputStream os = FileOutputStreamUtil.create(file);
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(os, ENCODING);
            writer.write(String.valueOf(versionNo));
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            CloseableUtil.close(writer);
        }
        this.versionNo = null;
    }

    /**
     * バージョン番号を表す文字列をint型に変換します。
     * 
     * @param value
     *            バージョン番号を表す文字列
     * @return バージョン番号
     */
    protected int convertToInt(String value) {
        int versionNo;
        try {
            versionNo = Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new IllegalVersionRuntimeException(value);
        }
        if (versionNo < 0) {
            throw new IllegalVersionRuntimeException(value);
        }
        return versionNo;
    }

}
