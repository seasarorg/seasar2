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
package org.seasar.extension.jdbc.gen.meta;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.DbColumnMeta;
import org.seasar.extension.jdbc.gen.DbTableMeta;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.mock.sql.MockResultSet;
import org.seasar.framework.util.ArrayMap;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class DbTableMetaReaderImplTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetPrimaryKeySet() throws Exception {
        final MockResultSet resultSet = new MockResultSet();

        ArrayMap rowData = new ArrayMap();
        rowData.put("TABLE_CAT", "catalog");
        rowData.put("TABLE_SCHEM", "schemaName");
        rowData.put("TABLE_NAME", "table");
        rowData.put("COLUMN_NAME", "pk1");
        resultSet.addRowData(rowData);

        rowData = new ArrayMap();
        rowData.put("TABLE_CAT", "catalog");
        rowData.put("TABLE_SCHEM", "schemaName");
        rowData.put("TABLE_NAME", "table");
        rowData.put("COLUMN_NAME", "pk2");
        resultSet.addRowData(rowData);

        MockDatabaseMetaData metaData = new MockDatabaseMetaData() {

            @Override
            public ResultSet getPrimaryKeys(String catalog, String schema,
                    String table) throws SQLException {
                return resultSet;
            }

        };
        DbTableMetaReaderImpl reader = new DbTableMetaReaderImpl(
                new MockDataSource(), new StandardGenDialect(), "schemaName",
                "table");
        Set<String> list = reader.getPrimaryKeySet(metaData, "catalogName",
                "schemaName", "table");
        assertEquals(2, list.size());
        assertTrue(list.contains("pk1"));
        assertTrue(list.contains("pk2"));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetDbColumnMetaList() throws Exception {
        final MockResultSet resultSet = new MockResultSet();

        ArrayMap rowData = new ArrayMap();
        rowData.put("TABLE_CAT", "catalogName");
        rowData.put("TABLE_SCHEM", "schemaName");
        rowData.put("TABLE_NAME", "table");
        rowData.put("COLUMN_NAME", "column1");
        rowData.put("DATA_TYPE", Types.DECIMAL);
        rowData.put("TYPE_NAME", "DECIMAL");
        rowData.put("COLUMN_SIZE", 10);
        rowData.put("BUFFER_LENGTH", null);
        rowData.put("DECIMAL_DIGITS", 3);
        rowData.put("NUM_PREC_RADIX", 10);
        rowData.put("NULLABLE ", DatabaseMetaData.columnNoNulls);
        resultSet.addRowData(rowData);

        rowData = new ArrayMap();
        rowData.put("TABLE_CAT", "catalogName");
        rowData.put("TABLE_SCHEM", "schemaName");
        rowData.put("TABLE_NAME", "table");
        rowData.put("COLUMN_NAME", "column2");
        rowData.put("DATA_TYPE", Types.VARCHAR);
        rowData.put("TYPE_NAME", "VARCHAR");
        rowData.put("COLUMN_SIZE", 10);
        rowData.put("BUFFER_LENGTH", null);
        rowData.put("DECIMAL_DIGITS", 0);
        rowData.put("NUM_PREC_RADIX", 10);
        rowData.put("NULLABLE ", DatabaseMetaData.columnNullable);
        resultSet.addRowData(rowData);

        MockDatabaseMetaData metaData = new MockDatabaseMetaData() {

            @Override
            public ResultSet getColumns(String catalog, String schemaPattern,
                    String tableNamePattern, String columnNamePattern)
                    throws SQLException {
                return resultSet;
            }
        };

        DbTableMetaReaderImpl reader = new DbTableMetaReaderImpl(
                new MockDataSource(), new StandardGenDialect(), "schemaName",
                "table");
        List<DbColumnMeta> list = reader.getDbColumnMetaList(metaData,
                "catalogName", "schemaName", "tableName");
        assertEquals(2, list.size());
        DbColumnMeta columnMeta = list.get(0);
        assertEquals("column1", columnMeta.getName());
        assertEquals(Types.DECIMAL, columnMeta.getSqlType());
        assertEquals("DECIMAL", columnMeta.getTypeName());
        assertEquals(10, columnMeta.getLength());
        assertEquals(3, columnMeta.getScale());
        assertFalse(columnMeta.isNullable());

        columnMeta = list.get(1);
        assertEquals("column2", columnMeta.getName());
        assertEquals(Types.VARCHAR, columnMeta.getSqlType());
        assertEquals("VARCHAR", columnMeta.getTypeName());
        assertEquals(10, columnMeta.getLength());
        assertEquals(0, columnMeta.getScale());
        assertTrue(columnMeta.isNullable());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetTableMetaList_schemaSpecified() throws Exception {
        final MockResultSet resultSet = new MockResultSet();

        ArrayMap rowData = new ArrayMap();
        rowData.put("TABLE_CAT", "catalog1");
        rowData.put("TABLE_SCHEM", "schemaName1");
        rowData.put("TABLE_NAME", "table1");
        resultSet.addRowData(rowData);

        rowData = new ArrayMap();
        rowData.put("TABLE_CAT", "catalog2");
        rowData.put("TABLE_SCHEM", "schemaName2");
        rowData.put("TABLE_NAME", "table2");
        resultSet.addRowData(rowData);

        MockDatabaseMetaData metaData = new MockDatabaseMetaData() {

            @Override
            public ResultSet getTables(String catalog, String schemaPattern,
                    String tableNamePattern, String[] types)
                    throws SQLException {
                return resultSet;
            }
        };

        DbTableMetaReaderImpl reader = new DbTableMetaReaderImpl(
                new MockDataSource(), new StandardGenDialect(), "schemaName",
                "table");
        List<DbTableMeta> list = reader.getDbTableMetaList(metaData,
                "schemaName");
        assertEquals(2, list.size());
        assertEquals("catalog1", list.get(0).getCatalogName());
        assertEquals("schemaName1", list.get(0).getSchemaName());
        assertEquals("table1", list.get(0).getName());
        assertEquals("catalog2", list.get(1).getCatalogName());
        assertEquals("schemaName2", list.get(1).getSchemaName());
        assertEquals("table2", list.get(1).getName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetTableMetaList_schemaNotSpecified() throws Exception {
        final MockResultSet resultSet = new MockResultSet();

        ArrayMap rowData = new ArrayMap();
        rowData.put("TABLE_CAT", "catalog1");
        rowData.put("TABLE_SCHEM", "schemaName1");
        rowData.put("TABLE_NAME", "table1");
        resultSet.addRowData(rowData);

        rowData = new ArrayMap();
        rowData.put("TABLE_CAT", "catalog2");
        rowData.put("TABLE_SCHEM", "schemaName2");
        rowData.put("TABLE_NAME", "table2");
        resultSet.addRowData(rowData);

        MockDatabaseMetaData metaData = new MockDatabaseMetaData() {

            @Override
            public ResultSet getTables(String catalog, String schemaPattern,
                    String tableNamePattern, String[] types)
                    throws SQLException {
                return resultSet;
            }
        };

        DbTableMetaReaderImpl reader = new DbTableMetaReaderImpl(
                new MockDataSource(), new StandardGenDialect(), null, "table");
        List<DbTableMeta> list = reader.getDbTableMetaList(metaData,
                "schemaName");
        assertEquals(2, list.size());
        assertEquals("catalog1", list.get(0).getCatalogName());
        assertNull(list.get(0).getSchemaName());
        assertEquals("table1", list.get(0).getName());
        assertEquals("catalog2", list.get(1).getCatalogName());
        assertNull(list.get(1).getSchemaName());
        assertEquals("table2", list.get(1).getName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testFilterDbTableMetaList() throws Exception {
        List<DbTableMeta> list = new ArrayList<DbTableMeta>();
        DbTableMeta tableMeta = new DbTableMeta();
        tableMeta.setName("AAA");
        list.add(tableMeta);
        DbTableMeta tableMeta2 = new DbTableMeta();
        tableMeta2.setName("BBB");
        list.add(tableMeta2);
        DbTableMeta tableMeta3 = new DbTableMeta();
        tableMeta3.setName("abc");
        list.add(tableMeta3);

        DbTableMetaReaderImpl reader = new DbTableMetaReaderImpl(
                new MockDataSource(), new StandardGenDialect(), "schemaName",
                "A.*");
        List<DbTableMeta> result = reader.filterDbTableMetaList(list, "A.*");
        assertEquals(2, result.size());
        assertEquals("AAA", result.get(0).getName());
        assertEquals("abc", result.get(1).getName());
    }

}
