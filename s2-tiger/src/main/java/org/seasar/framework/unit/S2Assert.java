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
package org.seasar.framework.unit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import junit.framework.AssertionFailedError;

import org.junit.Assert;
import org.seasar.extension.dataset.ColumnType;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.extension.unit.BeanListReader;
import org.seasar.extension.unit.BeanReader;
import org.seasar.extension.unit.MapListReader;
import org.seasar.extension.unit.MapReader;

/**
 * @author taedium
 * 
 */
public class S2Assert extends Assert {

    protected S2Assert() {
    }

    public static void assertEquals(DataSet expected, DataSet actual) {
        assertEquals(null, expected, actual);
    }

    public static void assertEquals(String message, DataSet expected,
            DataSet actual) {
        message = message == null ? "" : message;
        assertEquals(message + ":TableSize", expected.getTableSize(), actual
                .getTableSize());
        for (int i = 0; i < expected.getTableSize(); ++i) {
            assertEquals(message, expected.getTable(i), actual.getTable(i));
        }
    }

    public static void assertEquals(DataTable expected, DataTable actual) {
        assertEquals(null, expected, actual);
    }

    public static void assertEquals(String message, DataTable expected,
            DataTable actual) {

        message = message == null ? "" : message;
        message = message + ":TableName=" + expected.getTableName();
        assertEquals(message + ":RowSize", expected.getRowSize(), actual
                .getRowSize());
        for (int i = 0; i < expected.getRowSize(); ++i) {
            DataRow expectedRow = expected.getRow(i);
            DataRow actualRow = actual.getRow(i);
            List<String> errorMessages = new ArrayList<String>();
            for (int j = 0; j < expected.getColumnSize(); ++j) {
                try {
                    String columnName = expected.getColumnName(j);
                    Object expectedValue = expectedRow.getValue(columnName);
                    ColumnType ct = ColumnTypes.getColumnType(expectedValue);
                    Object actualValue = actualRow.getValue(columnName);
                    if (!ct.equals(expectedValue, actualValue)) {
                        assertEquals(message + ":Row=" + i + ":columnName="
                                + columnName, expectedValue, actualValue);
                    }
                } catch (AssertionFailedError e) {
                    errorMessages.add(e.getMessage());
                }
            }
            if (!errorMessages.isEmpty()) {
                fail(message + errorMessages);
            }
        }
    }

    public static void assertEquals(DataSet expected, Object actual) {
        assertEquals(null, expected, actual);
    }

    public static void assertEquals(String message, DataSet expected,
            Object actual) {
        if (expected == null || actual == null) {
            Assert.assertEquals(message, expected, actual);
            return;
        }
        if (actual instanceof List) {
            List actualList = (List) actual;
            Assert.assertFalse(actualList.isEmpty());
            Object actualItem = actualList.get(0);
            if (actualItem instanceof Map) {
                assertMapListEquals(message, expected, actualList);
            } else {
                assertBeanListEquals(message, expected, actualList);
            }
        } else if (actual instanceof Object[]) {
            assertEquals(message, expected, Arrays.asList((Object[]) actual));
        } else {
            if (actual instanceof Map) {
                assertMapEquals(message, expected, (Map) actual);
            } else {
                assertBeanEquals(message, expected, actual);
            }
        }
    }

    public static void assertMapEquals(String message, DataSet expected, Map map) {

        MapReader reader = new MapReader(map);
        assertEquals(message, expected, reader.read());
    }

    public static void assertMapListEquals(String message, DataSet expected,
            List list) {

        MapListReader reader = new MapListReader(list);
        assertEquals(message, expected, reader.read());
    }

    public static void assertBeanEquals(String message, DataSet expected,
            Object bean) {

        BeanReader reader = new BeanReader(bean);
        assertEquals(message, expected, reader.read());
    }

    public static void assertBeanListEquals(String message, DataSet expected,
            List list) {

        BeanListReader reader = new BeanListReader(list);
        assertEquals(message, expected, reader.read());
    }

}
