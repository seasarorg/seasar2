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
package org.seasar.extension.jdbc.gen.internal.version;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.seasar.extension.jdbc.gen.internal.exception.IllegalDdlInfoVersionRuntimeException;
import org.seasar.extension.jdbc.gen.internal.exception.NextVersionExceededRuntimeException;
import org.seasar.extension.jdbc.gen.internal.util.CloseableUtil;
import org.seasar.extension.jdbc.gen.internal.util.FileUtil;
import org.seasar.extension.jdbc.gen.version.DdlInfoFile;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.log.Logger;

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

        String line = readLine();
        if (line == null) {
            logger.log("IS2JDBCGen0007", new Object[] { file.getPath() });
            versionNo = 0;
            return versionNo;
        }
        int pos = line.indexOf("=");
        String value = pos > -1 ? line.substring(0, pos) : line;
        versionNo = convertToInt(value.trim());
        return versionNo;
    }

    /**
     * １行を読みます。
     * 
     * @return １行
     */
    protected String readLine() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), ENCODING));
            return reader.readLine();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
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

    public void applyNextVersionNo(String comment) {
        File temp = null;
        if (file.exists()) {
            temp = FileUtil.createTempFile("ddl-info", null);
            temp.deleteOnExit();
            FileUtil.copy(file, temp);
        }

        writeLine(getNextVersionNoInternal() + "=" + comment);

        if (temp != null) {
            FileUtil.append(temp, file);
        }
        this.versionNo = null;
    }

    /**
     * １行を書きます。
     * 
     * @param line
     *            １行
     */
    protected void writeLine(String line) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), ENCODING));
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            CloseableUtil.close(writer);
        }
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
            throw new IllegalDdlInfoVersionRuntimeException(file.getPath(), value);
        }
        if (versionNo < 0) {
            throw new IllegalDdlInfoVersionRuntimeException(file.getPath(), value);
        }
        return versionNo;
    }

}
