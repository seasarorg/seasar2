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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.seasar.extension.jdbc.gen.desc.ColumnDesc;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.util.CloseableUtil;
import org.seasar.extension.jdbc.gen.internal.util.DumpUtil;
import org.seasar.extension.jdbc.gen.sqltype.SqlType;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.CaseInsensitiveMap;
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
    protected Map<String, ColumnDesc> columnDescMap = new CaseInsensitiveMap();

    /** 区切り文字 */
    protected char delimiter;

    /** ライタ */
    protected BufferedWriter writer;

    /** 行番号 */
    protected int lineNumber;

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
            if (isIgnoreColumn(columnDesc)) {
                continue;
            }
            columnDescMap.put(columnDesc.getName(), columnDesc);
        }
    }

    /**
     * ヘッダーのみを書き込みます。
     */
    public void writeHeaderOnly() {
        int size = tableDesc.getColumnDescList().size();
        StringBuilder buf = new StringBuilder(size * 10);
        for (ColumnDesc columnDesc : tableDesc.getColumnDescList()) {
            if (isIgnoreColumn(columnDesc)) {
                continue;
            }
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
     * 無視するカラムの場合{@code true}を返します。
     * 
     * @param columnDesc
     *            カラム記述
     * @return 無視するカラムの場合{@code true}
     */
    protected boolean isIgnoreColumn(ColumnDesc columnDesc) {
        return columnDesc.isIdentity() && !dialect.supportsIdentityInsert();
    }

    /**
     * ヘッダーとデータ行を書き込みます。
     * 
     * @param rs
     *            結果セット
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    public void writeRows(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        Header header = createHeader(metaData);
        writeHeader(header);
        while (rs.next()) {
            writeRowData(rs, header);
        }
    }

    /**
     * ヘッダーを書き込みます。
     * 
     * @param header
     *            ヘッダー
     */
    protected void writeHeader(Header header) {
        StringBuilder buf = new StringBuilder(header.columnList.size() * 10);
        for (HeaderColumn headerColumn : header.columnList) {
            String columnName = headerColumn.columnDesc.getName();
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
     * @param header
     *            ヘッダー
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    protected void writeRowData(ResultSet resultSet, Header header)
            throws SQLException {
        StringBuilder buf = new StringBuilder(header.columnList.size() * 10);
        for (HeaderColumn headerColumn : header.columnList) {
            String value = null;
            if (headerColumn.present) {
                SqlType sqlType = headerColumn.columnDesc.getSqlType();
                value = sqlType.getValue(resultSet, headerColumn.index);
            }
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
        lineNumber++;
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

    /**
     * 行番号を返します。
     * 
     * @return 行番号
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * ヘッダーを作成します。
     * 
     * @param metaData
     *            結果セットのメタデータ
     * @return ヘッダー
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    protected Header createHeader(ResultSetMetaData metaData)
            throws SQLException {
        Header header = new Header();
        @SuppressWarnings("unchecked")
        Map<String, Integer> indexMap = new CaseInsensitiveMap();
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            int index = i + 1;
            String columnLabel = metaData.getColumnLabel(index);
            indexMap.put(columnLabel, index);
        }
        for (Map.Entry<String, ColumnDesc> entry : columnDescMap.entrySet()) {
            String columnLabel = entry.getKey();
            if (indexMap.containsKey(columnLabel)) {
                int index = indexMap.get(columnLabel);
                HeaderColumn headerColumn = new HeaderColumn();
                headerColumn.columnDesc = entry.getValue();
                headerColumn.index = index;
                headerColumn.present = true;
                header.columnList.add(headerColumn);
            } else {
                HeaderColumn headerColumn = new HeaderColumn();
                headerColumn.columnDesc = entry.getValue();
                header.columnList.add(headerColumn);
            }
        }
        return header;
    }

    /**
     * ダンプファイルのヘッダーです。
     * 
     * @author taedium
     */
    protected static class Header {

        /** ダンプファイルのヘッダーカラムのリスト */
        protected List<HeaderColumn> columnList = new ArrayList<HeaderColumn>();
    }

    /**
     * ダンプファイルのヘッダーのカラムです。
     * 
     * @author taedium
     */
    protected static class HeaderColumn {

        /** カラム記述 */
        protected ColumnDesc columnDesc;

        /** 対応するカラムがデータベースに存在する場合{@code true} */
        protected boolean present;

        /** データベースのカラムのインデックス */
        protected int index;
    }

}
