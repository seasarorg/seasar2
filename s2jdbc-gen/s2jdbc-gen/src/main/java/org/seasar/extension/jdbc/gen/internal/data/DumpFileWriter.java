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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import org.seasar.extension.jdbc.gen.desc.ColumnDesc;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.util.CloseableUtil;
import org.seasar.extension.jdbc.gen.internal.util.DumpUtil;
import org.seasar.extension.jdbc.gen.sqltype.SqlType;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ArrayMap;
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

    /** 方言 */
    protected GenDialect dialect;

    /** テーブル記述 */
    protected TableDesc tableDesc;

    /** エンコーディング */
    protected String encoding;

    /** カラム名をキー、カラム記述を値とするマップ */
    @SuppressWarnings("unchecked")
    protected Map<String, ColumnDesc> columnDescMap = new ArrayMap();

    /** 区切り文字 */
    protected char delimiter;

    /** ライタ */
    protected BufferedWriter writer;

    /**
     * インスタンスを構築します。
     * 
     * @param dumpFile
     *            ダンプファイル
     * @param tableDesc
     *            テーブル記述
     * @param dialect
     *            方言
     * @param encoding
     *            エンコーディング
     * @param delimiter
     *            区切り文字
     */
    public DumpFileWriter(File dumpFile, TableDesc tableDesc,
            GenDialect dialect, String encoding, char delimiter) {
        if (dumpFile == null) {
            throw new NullPointerException("dumpFile");
        }
        if (tableDesc == null) {
            throw new NullPointerException("tableDesc");
        }
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (encoding == null) {
            throw new NullPointerException("encoding");
        }
        this.dumpFile = dumpFile;
        this.dialect = dialect;
        this.tableDesc = tableDesc;
        this.encoding = encoding;
        this.delimiter = delimiter;
        setupColumnDescMap();
    }

    /**
     * カラム記述のマップを用意します。
     */
    protected void setupColumnDescMap() {
        for (ColumnDesc columnDesc : tableDesc.getColumnDescList()) {
            if (columnDesc.isIdentity() && !dialect.supportsIdentityInsert()) {
                continue;
            }
            columnDescMap.put(columnDesc.getName(), columnDesc);
        }
    }

    /**
     * ヘッダーを書き込みます。
     * 
     * @param metaData
     *            結果セットのメタデータ
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    public void writeHeader(ResultSetMetaData metaData) throws SQLException {
        int columnCount = metaData.getColumnCount();
        StringBuilder buf = new StringBuilder(columnCount * 10);
        for (int i = 0; i < columnCount; i++) {
            String columnLabel = metaData.getColumnLabel(i + 1);
            if (!columnDescMap.containsKey(columnLabel)) {
                continue;
            }
            ColumnDesc columnDesc = columnDescMap.get(columnLabel);
            String columnName = columnDesc.getName();
            buf.append(DumpUtil.quote(columnName));
            buf.append(delimiter);
        }
        if (buf.length() > 0) {
            buf.setLength(buf.length() - 1);
        }
        writeLine(buf.toString());
    }

    /**
     * データ行を書き込みます。
     * 
     * @param resultSet
     *            結果セット
     * @param metaData
     *            結果セットのメタデータ
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    public void writeRowData(ResultSet resultSet, ResultSetMetaData metaData)
            throws SQLException {
        int columnCount = metaData.getColumnCount();
        StringBuilder buf = new StringBuilder(columnCount * 10);
        for (int i = 0; i < columnCount; i++) {
            String columnLabel = metaData.getColumnLabel(i + 1);
            if (!columnDescMap.containsKey(columnLabel)) {
                continue;
            }
            ColumnDesc columnDesc = columnDescMap.get(columnLabel);
            SqlType sqlType = columnDesc.getSqlType();
            String value = sqlType.getValue(resultSet, i + 1);
            buf.append(DumpUtil.encode(value));
            buf.append(delimiter);
        }
        if (buf.length() > 0) {
            buf.setLength(buf.length() - 1);
        }
        writeLine(buf.toString());
    }

    /**
     * 行を書き込みます。
     * 
     * @param line
     *            行
     */
    protected void writeLine(String line) {
        if (writer == null) {
            writer = createBufferdWriter();
        }
        try {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
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
