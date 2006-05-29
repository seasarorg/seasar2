/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
import java.io.OutputStream;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataSetConstants;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.DataWriter;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.Base64Util;
import org.seasar.framework.util.FileOutputStreamUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringConversionUtil;

/**
 * @author higa
 * 
 */
public class XlsWriter implements DataWriter, DataSetConstants {

    private OutputStream out_;

    private HSSFWorkbook workbook_;

    private HSSFCellStyle dateStyle_;

    private HSSFCellStyle base64Style_;

    public XlsWriter(String path) {
        this(new File(ResourceUtil.getResourceAsFile("."), path));
    }

    public XlsWriter(String dirName, String fileName) {
        this(ResourceUtil.getResourceAsFile(dirName), fileName);
    }

    public XlsWriter(File dir, String fileName) {
        this(new File(dir, fileName));
    }

    public XlsWriter(File file) {
        this(FileOutputStreamUtil.create(file));
    }

    public XlsWriter(OutputStream out) {
        setOutputStream(out);
    }

    public void setOutputStream(OutputStream out) {
        out_ = out;
        workbook_ = new HSSFWorkbook();
        HSSFDataFormat df = workbook_.createDataFormat();
        dateStyle_ = workbook_.createCellStyle();
        dateStyle_.setDataFormat(df.getFormat(DATE_FORMAT));
        base64Style_ = workbook_.createCellStyle();
        base64Style_.setDataFormat(df.getFormat(BASE64_FORMAT));
    }

    /**
     * @see org.seasar.extension.dataset.DataWriter#write(org.seasar.extension.dataset.DataSet)
     */
    public void write(DataSet dataSet) {
        for (int i = 0; i < dataSet.getTableSize(); ++i) {
            DataTable table = dataSet.getTable(i);
            HSSFSheet sheet = workbook_.createSheet();
            workbook_.setSheetName(i, table.getTableName(),
                    HSSFWorkbook.ENCODING_UTF_16);
            HSSFRow headerRow = sheet.createRow(0);
            for (int j = 0; j < table.getColumnSize(); ++j) {
                HSSFCell cell = headerRow.createCell((short) j);
                cell.setCellValue(table.getColumnName(j));
            }
            for (int j = 0; j < table.getRowSize(); ++j) {
                HSSFRow row = sheet.createRow(j + 1);
                for (int k = 0; k < table.getColumnSize(); ++k) {
                    DataRow dataRow = table.getRow(j);
                    Object value = dataRow.getValue(k);
                    if (value != null) {
                        HSSFCell cell = row.createCell((short) k);
                        setValue(cell, value);
                    }
                }
            }
        }
        try {
            workbook_.write(out_);
            out_.flush();
            out_.close();
        } catch (IOException ex) {
            throw new IORuntimeException(ex);
        }
    }

    private void setValue(HSSFCell cell, Object value) {
        if (value instanceof Number) {
            cell.setCellValue(value.toString());
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
            cell.setCellStyle(dateStyle_);
        } else if (value instanceof byte[]) {
            cell.setCellValue(Base64Util.encode((byte[]) value));
            cell.setCellStyle(base64Style_);
        } else if (value instanceof Boolean) {
            cell.setCellValue(((Boolean) value).booleanValue());
        } else {
            cell.setEncoding(HSSFWorkbook.ENCODING_UTF_16);
            cell.setCellValue(StringConversionUtil.toString(value, null));
        }
    }
}
