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
import java.io.InputStream;
import java.math.BigDecimal;

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
 * @author higa
 *
 */
public class XlsReader implements DataReader, DataSetConstants {

	private DataSet dataSet_;
	private HSSFWorkbook workbook_;
	private HSSFDataFormat dataFormat_;

	public XlsReader(String path) {
		this(ResourceUtil.getResourceAsStream(path));
	}

    public XlsReader(String dirName, String fileName) {
        this(ResourceUtil.getResourceAsFile(dirName), fileName);
    }

    public XlsReader(File dir, String fileName) {
        this(new File(dir, fileName));
    }

    public XlsReader(File file) {
        this(FileInputStreamUtil.create(file));
    }

	public XlsReader(InputStream in) {
		try {
			workbook_ = new HSSFWorkbook(in);
		} catch (IOException ex) {
			throw new IORuntimeException(ex);
		}
		dataFormat_ = workbook_.createDataFormat();
		dataSet_ = new DataSetImpl();
		for (int i = 0; i < workbook_.getNumberOfSheets(); ++i) {
			createTable(workbook_.getSheetName(i), workbook_.getSheetAt(i));
		}
	}

	/**
	 * @see org.seasar.extension.dataset.DataReader#read()
	 */
	public DataSet read() {
		return dataSet_;
	}

	private DataTable createTable(String sheetName, HSSFSheet sheet) {
		DataTable table = dataSet_.addTable(sheetName);
		int rowCount = sheet.getLastRowNum();
        if (rowCount > 0) {
            setupColumns(table, sheet.getRow(0), sheet.getRow(1));
            setupRows(table, sheet);
        } else if (rowCount == 0) {
            setupColumns(table, sheet.getRow(0), null);
        }
		return table;
	}

	private void setupColumns(DataTable table, HSSFRow nameRow, HSSFRow valueRow) {
		for (int i = 0;; ++i) {
			HSSFCell nameCell = nameRow.getCell((short) i);
			if (nameCell == null) {
				break;
			}
			String columnName = nameCell.getStringCellValue().trim();
		    if (columnName.length() == 0) {
		      break;
		    }
            HSSFCell valueCell = null;
            if (valueRow != null) {
                valueCell = valueRow.getCell((short) i);
            }
            if (valueCell != null) {
                table.addColumn(columnName, getColumnType(valueCell));
            } else {
                table.addColumn(columnName);
            }
		}
	}

	private void setupRows(DataTable table, HSSFSheet sheet) {
		for (int i = 1;; ++i) {
			HSSFRow row = sheet.getRow((short) i);
			if (row == null) {
				break;
			}
			setupRow(table, row);
		}
	}

	private void setupRow(DataTable table, HSSFRow row) {
		DataRow dataRow = table.addRow();
		for (int i = 0; i < table.getColumnSize(); ++i) {
			HSSFCell cell = row.getCell((short) i);
			Object value = getValue(cell);
			dataRow.setValue(i, value);
		}
	}

	public boolean isCellBase64Formatted(HSSFCell cell) {
		HSSFCellStyle cs = cell.getCellStyle();
		short dfNum = cs.getDataFormat();
		return BASE64_FORMAT.equals(dataFormat_.getFormat(dfNum));
	}

	public boolean isCellDateFormatted(HSSFCell cell) {
		HSSFCellStyle cs = cell.getCellStyle();
		short dfNum = cs.getDataFormat();
		String format = dataFormat_.getFormat(dfNum);
		if (StringUtil.isEmpty(format)) {
			return false;
		}
		if (format.indexOf('/') > 0
			|| format.indexOf('y') > 0
			|| format.indexOf('m') > 0
			|| format.indexOf('d') > 0) {
			return true;
		}
		return false;
	}

	public Object getValue(HSSFCell cell) {
		if (cell == null) {
			return null;
		}
        switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_NUMERIC :
				if (isCellDateFormatted(cell)) {
					return TimestampConversionUtil.toTimestamp(
						cell.getDateCellValue());
				}
				final double numericCellValue = cell.getNumericCellValue();
                if (isInt(numericCellValue)) {
                    return new BigDecimal((int)numericCellValue);
                }
				return new BigDecimal(Double.toString(numericCellValue));
			case HSSFCell.CELL_TYPE_STRING :
				String s = cell.getStringCellValue();
				if (s != null) {
					s = StringUtil.rtrim(s);
				}
				if ("".equals(s)) {
					s = null;
				}
				if (isCellBase64Formatted(cell)) {
					return Base64Util.decode(s);
				}
				return s;
			case HSSFCell.CELL_TYPE_BOOLEAN :
                boolean b = cell.getBooleanCellValue();
                return Boolean.valueOf(b);
			default :
				return null;
		}
	}
    
    protected ColumnType getColumnType(HSSFCell cell) {
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC :
                if (isCellDateFormatted(cell)) {
                    return ColumnTypes.TIMESTAMP;
                }
                return ColumnTypes.BIGDECIMAL;
            case HSSFCell.CELL_TYPE_STRING :
                if (isCellBase64Formatted(cell)) {
                    return ColumnTypes.BINARY;
                }
                return ColumnTypes.STRING;
            default :
                return ColumnTypes.STRING;
        }
    }

    private boolean isInt(final double numericCellValue) {
        return ((int)numericCellValue) == numericCellValue;
    }

}
