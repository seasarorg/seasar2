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
package org.seasar.extension.dataset.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.extension.dataset.ColumnType;
import org.seasar.extension.dataset.DataReader;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataSetConstants;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.Base64Util;
import org.seasar.framework.util.FileInputStreamUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.TimestampConversionUtil;

/**
 * Excel用の {@link DataReader}です。
 * 
 * @author higa
 * @author manhole
 * @author azusa
 */
public class XlsReader implements DataReader, DataSetConstants {

    /**
     * データセットです。
     */
    protected DataSet dataSet;

    /**
     * ワークブックです。
     */
    protected HSSFWorkbook workbook;

    /**
     * データフォーマットです。
     */
    protected HSSFDataFormat dataFormat;

    /**
     * 文字列をトリミングするかどうか
     */
    protected boolean trimString = true;

    /**
     * {@link XlsReader}を作成します。
     * 
     * @param path
     *            パス
     */
    public XlsReader(String path) {
        this(path, true);
    }

    /**
     * {@link XlsReader}を作成します。
     * 
     * @param path
     *            パス
     * @param trimString
     *            文字列をトリムするかどうか
     */
    public XlsReader(String path, boolean trimString) {
        this(ResourceUtil.getResourceAsStream(path), trimString);
    }

    /**
     * {@link XlsReader}を作成します。
     * 
     * @param dirName
     *            ディレクトリ名
     * @param fileName
     *            ファイル名
     */
    public XlsReader(String dirName, String fileName) {
        this(dirName, fileName, true);
    }

    /**
     * {@link XlsReader}を作成します。
     * 
     * @param dirName
     *            ディレクトリ名
     * @param fileName
     *            ファイル名
     * @param trimString
     *            文字列をトリムするかどうか
     */
    public XlsReader(String dirName, String fileName, boolean trimString) {
        this(ResourceUtil.getResourceAsFile(dirName), fileName, trimString);
    }

    /**
     * {@link XlsReader}を作成します。
     * 
     * @param dir
     *            ディレクトリ
     * @param fileName
     *            ファイル名
     */
    public XlsReader(File dir, String fileName) {
        this(dir, fileName, true);
    }

    /**
     * {@link XlsReader}を作成します。
     * 
     * @param dir
     *            ディレクトリ
     * @param fileName
     *            ファイル名
     * @param trimString
     *            文字列をトリムするかどうか
     */
    public XlsReader(File dir, String fileName, boolean trimString) {
        this(new File(dir, fileName), trimString);
    }

    /**
     * {@link XlsReader}を作成します。
     * 
     * @param file
     *            ファイル
     */
    public XlsReader(File file) {
        this(file, true);
    }

    /**
     * {@link XlsReader}を作成します。
     * 
     * @param file
     *            ファイル
     * @param trimString
     *            文字列をトリムするかどうか
     */
    public XlsReader(File file, boolean trimString) {
        this(FileInputStreamUtil.create(file), trimString);
    }

    /**
     * {@link XlsReader}を作成します。
     * 
     * @param in
     *            入力ストリーム
     */
    public XlsReader(InputStream in) {
        this(in, true);
    }

    /**
     * {@link XlsReader}を作成します。
     * 
     * @param in
     *            入力ストリーム
     * @param trimString
     *            文字列をトリムするかどうか
     */
    public XlsReader(InputStream in, boolean trimString) {
        this.trimString = trimString;
        try {
            workbook = new HSSFWorkbook(in);
        } catch (IOException ex) {
            throw new IORuntimeException(ex);
        }
        dataFormat = workbook.createDataFormat();
        dataSet = new DataSetImpl();
        for (int i = 0; i < workbook.getNumberOfSheets(); ++i) {
            createTable(workbook.getSheetName(i), workbook.getSheetAt(i));
        }
    }

    public DataSet read() {
        return dataSet;
    }

    /**
     * テーブルを作成します。
     * 
     * @param sheetName
     *            シート名
     * @param sheet
     *            シート
     * @return テーブル
     */
    protected DataTable createTable(String sheetName, HSSFSheet sheet) {
        DataTable table = dataSet.addTable(sheetName);
        int rowCount = sheet.getLastRowNum();
        if (rowCount > 0) {
            setupColumns(table, sheet);
            setupRows(table, sheet);
        } else if (rowCount == 0) {
            setupColumns(table, sheet);
        }
        return table;
    }

    /**
     * カラムの情報をセットアップします。
     * 
     * @param table
     *            テーブル
     * @param sheet
     *            シート
     */
    protected void setupColumns(DataTable table, HSSFSheet sheet) {
        HSSFRow nameRow = sheet.getRow(0);
        HSSFRow valueRow = sheet.getRow(1);
        for (int i = 0; i <= Short.MAX_VALUE; ++i) {
            HSSFCell nameCell = nameRow.getCell((short) i);
            if (nameCell == null) {
                break;
            }
            String columnName = nameCell.getRichStringCellValue().getString();
            if (columnName.length() == 0) {
                break;
            }
            HSSFCell valueCell = null;
            if (valueRow != null) {
                for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                    valueCell = sheet.getRow(j).getCell((short) i);
                    if (valueCell != null
                            && !StringUtil.isEmpty(valueCell.toString())) {
                        break;
                    }
                }
            }
            if (valueCell != null) {
                table.addColumn(columnName, getColumnType(valueCell));
            } else {
                table.addColumn(columnName);
            }
        }
    }

    /**
     * シートの行をセットアップします。
     * 
     * @param table
     *            テーブル
     * @param sheet
     *            シート
     */
    protected void setupRows(DataTable table, HSSFSheet sheet) {
        for (int i = 1; i <= RowRecord.MAX_ROW_NUMBER; ++i) {
            HSSFRow row = sheet.getRow(i);
            if (row == null) {
                break;
            }
            setupRow(table, row);
        }
    }

    /**
     * 行をセットアップします。
     * 
     * @param table
     *            テーブル
     * @param row
     *            行
     */
    protected void setupRow(DataTable table, HSSFRow row) {
        DataRow dataRow = table.addRow();
        for (int i = 0; i < table.getColumnSize(); ++i) {
            HSSFCell cell = row.getCell((short) i);
            Object value = getValue(cell);
            dataRow.setValue(i, value);
        }
    }

    /**
     * セルがBase64でフォーマットされているかどうかを返します。
     * 
     * @param cell
     *            セル
     * @return セルがBase64でフォーマットされているかどうか
     */
    public boolean isCellBase64Formatted(HSSFCell cell) {
        HSSFCellStyle cs = cell.getCellStyle();
        short dfNum = cs.getDataFormat();
        return BASE64_FORMAT.equals(dataFormat.getFormat(dfNum));
    }

    /**
     * セルが日付のフォーマットかどうかを返します。
     * 
     * @param cell
     *            セル
     * @return セルが日付のフォーマットかどうか
     */
    public boolean isCellDateFormatted(HSSFCell cell) {
        HSSFCellStyle cs = cell.getCellStyle();
        short dfNum = cs.getDataFormat();
        String format = dataFormat.getFormat(dfNum);
        if (StringUtil.isEmpty(format)) {
            return false;
        }
        if (format.indexOf('/') > 0 || format.indexOf('y') > 0
                || format.indexOf('m') > 0 || format.indexOf('d') > 0) {
            return true;
        }
        return false;
    }

    /**
     * セルの値を返します。
     * 
     * @param cell
     *            セル
     * @return セルの値
     */
    public Object getValue(HSSFCell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
        case HSSFCell.CELL_TYPE_NUMERIC:
            if (isCellDateFormatted(cell)) {
                return TimestampConversionUtil.toTimestamp(cell
                        .getDateCellValue());
            }
            final double numericCellValue = cell.getNumericCellValue();
            if (isInt(numericCellValue)) {
                return new BigDecimal((int) numericCellValue);
            }
            return new BigDecimal(Double.toString(numericCellValue));
        case HSSFCell.CELL_TYPE_STRING:
            String s = cell.getRichStringCellValue().getString();
            if (s != null) {
                s = StringUtil.rtrim(s);
                if (!trimString && s.length() > 1 && s.startsWith("\"")
                        && s.endsWith("\"")) {
                    s = s.substring(1, s.length() - 1);
                }
            }
            if ("".equals(s)) {
                s = null;
            }
            if (isCellBase64Formatted(cell)) {
                return Base64Util.decode(s);
            }
            return s;
        case HSSFCell.CELL_TYPE_BOOLEAN:
            boolean b = cell.getBooleanCellValue();
            return Boolean.valueOf(b);
        default:
            return null;
        }
    }

    /**
     * カラムの型を返します。
     * 
     * @param cell
     *            セル
     * @return カラムの型
     */
    protected ColumnType getColumnType(HSSFCell cell) {
        switch (cell.getCellType()) {
        case HSSFCell.CELL_TYPE_NUMERIC:
            if (isCellDateFormatted(cell)) {
                return ColumnTypes.TIMESTAMP;
            }
            return ColumnTypes.BIGDECIMAL;
        case HSSFCell.CELL_TYPE_BOOLEAN:
            return ColumnTypes.BOOLEAN;
        case HSSFCell.CELL_TYPE_STRING:
            if (isCellBase64Formatted(cell)) {
                return ColumnTypes.BINARY;
            } else if (trimString) {
                return ColumnTypes.STRING;
            } else {
                return ColumnTypes.NOT_TRIM_STRING;
            }
        default:
            return ColumnTypes.STRING;
        }
    }

    /**
     * 整数かどうかを返します。
     * 
     * @param numericCellValue
     *            numericな値
     * @return 整数かどうか
     */
    protected boolean isInt(final double numericCellValue) {
        return ((int) numericCellValue) == numericCellValue;
    }

}
