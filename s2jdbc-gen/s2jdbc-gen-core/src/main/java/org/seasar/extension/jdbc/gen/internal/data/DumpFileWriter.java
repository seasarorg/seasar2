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
package org.seasar.extension.jdbc.gen.internal.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.seasar.extension.jdbc.gen.internal.util.CloseableUtil;
import org.seasar.extension.jdbc.gen.internal.util.DumpUtil;
import org.seasar.extension.jdbc.gen.sqltype.SqlType;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.FileOutputStreamUtil;

/**
 * ダンプファイルのライタです。
 * 
 * @author taedium
 */
public class DumpFileWriter {

    /** ロガー */
    protected static Logger logger = Logger.getLogger(DumpFileWriter.class);

    /** ダンプファイル */
    protected File dumpFile;

    /** エンコーディング */
    protected String encoding;

    /** SQL型のリスト */
    protected List<SqlType> sqlTypeList;

    /** 区切り文字 */
    protected char delimiter;

    /** ライタ */
    protected BufferedWriter writer;

    /**
     * インスタンスを構築します。
     * 
     * @param dumpFile
     *            ダンプファイル
     * @param sqlTypeList
     *            SQL型のリスト
     * @param encoding
     *            エンコーディング
     * @param delimiter
     *            区切り文字
     */
    public DumpFileWriter(File dumpFile, List<SqlType> sqlTypeList,
            String encoding, char delimiter) {
        if (dumpFile == null) {
            throw new NullPointerException("dumpFile");
        }
        if (sqlTypeList == null) {
            throw new NullPointerException("sqlTypeList");
        }
        if (encoding == null) {
            throw new NullPointerException("encoding");
        }
        this.dumpFile = dumpFile;
        this.sqlTypeList = sqlTypeList;
        this.encoding = encoding;
        this.delimiter = delimiter;
    }

    /**
     * ヘッダーを書き込みます。
     * 
     * @param columnNameList
     *            カラム名のリスト
     */
    public void writeHeader(List<String> columnNameList) {
        StringBuilder buf = new StringBuilder(columnNameList.size() * 10);
        try {
            for (String columnName : columnNameList) {
                buf.append(DumpUtil.quote(columnName));
                buf.append(delimiter);
            }
            if (buf.length() > 0) {
                buf.setLength(buf.length() - 1);
            }
            writeLine(buf.toString());
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * データ行を書き込みます。
     * 
     * @param resultSet
     *            結果セット
     */
    public void writeRowData(ResultSet resultSet) {
        StringBuilder buf = new StringBuilder(sqlTypeList.size() * 10);
        try {
            for (int i = 0; i < sqlTypeList.size(); i++) {
                String value = null;
                try {
                    SqlType sqlType = sqlTypeList.get(i);
                    value = sqlType.getValue(resultSet, i + 1);
                } catch (SQLException ignore) {
                    logger.log(ignore);
                }
                buf.append(DumpUtil.encode(value));
                buf.append(delimiter);
            }
            if (buf.length() > 0) {
                buf.setLength(buf.length() - 1);
            }
            writeLine(buf.toString());
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 行を書き込みます。
     * 
     * @param line
     *            行
     * @throws IOException
     *             何らかのIO例外が発生した場合
     */
    protected void writeLine(String line) throws IOException {
        if (writer == null) {
            writer = createBufferdWriter();
        }
        writer.write(line);
        writer.newLine();
    }

    /**
     * {@link BufferedWriter}を作成します。
     * 
     * @return {@link BufferedWriter}
     */
    protected BufferedWriter createBufferdWriter() {
        dumpFile.getParentFile().mkdirs();
        Charset charset = Charset.forName(encoding);
        FileOutputStream fos = FileOutputStreamUtil.create(dumpFile);
        OutputStreamWriter osw = new OutputStreamWriter(fos, charset);
        return new BufferedWriter(osw);
    }

    /**
     * クローズします。
     */
    public void close() {
        CloseableUtil.close(writer);
    }

}
