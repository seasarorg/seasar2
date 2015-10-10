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
package org.seasar.extension.jdbc.gen.internal.meta;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.internal.dialect.StandardGenDialect;
import org.seasar.extension.jdbc.gen.meta.DbColumnMeta;
import org.seasar.extension.jdbc.gen.meta.DbForeignKeyMeta;
import org.seasar.extension.jdbc.gen.meta.DbTableMeta;
import org.seasar.extension.jdbc.gen.meta.DbUniqueKeyMeta;
import org.seasar.extension.jdbc.gen.mock.sql.GenMockDatabaseMetaData;
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
        rowData.put("COLUMN_NAME", "pk1");
        resultSet.addRowData(rowData);

        rowData = new ArrayMap();
        rowData.put("COLUMN_NAME", "pk2");
        resultSet.addRowData(rowData);

        GenMockDatabaseMetaData metaData = new GenMockDatabaseMetaData() {

            @Override
            public ResultSet getPrimaryKeys(String catalog, String schema,
                    String table) throws SQLException {
                return resultSet;
            }

        };
        DbTableMetaReaderImpl reader = new DbTableMetaReaderImpl(
                new MockDataSource(), new StandardGenDialect(), "schemaName",
                ".*", "", false);
        Set<String> list = reader.getPrimaryKeySet(metaData, new DbTableMeta());
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
        rowData.put("COLUMN_NAME", "column1");
        rowData.put("DATA_TYPE", Types.DECIMAL);
        rowData.put("TYPE_NAME", "DECIMAL");
        rowData.put("COLUMN_SIZE", 10);
        rowData.put("DECIMAL_DIGITS", 3);
        rowData.put("NULLABLE", DatabaseMetaData.columnNoNulls);
        rowData.put("COLUMN_DEF", "10.5");
        rowData.put("REMARKS", "comment1");
        resultSet.addRowData(rowData);

        rowData = new ArrayMap();
        rowData.put("COLUMN_NAME", "column2");
        rowData.put("DATA_TYPE", Types.VARCHAR);
        rowData.put("TYPE_NAME", "VARCHAR");
        rowData.put("COLUMN_SIZE", 10);
        rowData.put("DECIMAL_DIGITS", 0);
        rowData.put("NULLABLE", DatabaseMetaData.columnNullable);
        rowData.put("COLUMN_DEF", "aaa");
        rowData.put("REMARKS", "comment2");
        resultSet.addRowData(rowData);

        GenMockDatabaseMetaData metaData = new GenMockDatabaseMetaData() {

            @Override
            public ResultSet getColumns(String catalog, String schemaPattern,
                    String tableNamePattern, String columnNamePattern)
                    throws SQLException {
                return resultSet;
            }
        };

        DbTableMetaReaderImpl reader = new DbTableMetaReaderImpl(
                new MockDataSource(), new StandardGenDialect(), "schemaName",
                ".*", "", true);
        List<DbColumnMeta> list = reader.getDbColumnMetaList(metaData,
                new DbTableMeta());
        assertEquals(2, list.size());
        DbColumnMeta columnMeta = list.get(0);
        assertEquals("column1", columnMeta.getName());
        assertEquals(Types.DECIMAL, columnMeta.getSqlType());
        assertEquals("DECIMAL", columnMeta.getTypeName());
        assertEquals(10, columnMeta.getLength());
        assertEquals(3, columnMeta.getScale());
        assertFalse(columnMeta.isNullable());
        assertEquals("10.5", columnMeta.getDefaultValue());
        assertEquals("comment1", columnMeta.getComment());

        columnMeta = list.get(1);
        assertEquals("column2", columnMeta.getName());
        assertEquals(Types.VARCHAR, columnMeta.getSqlType());
        assertEquals("VARCHAR", columnMeta.getTypeName());
        assertEquals(10, columnMeta.getLength());
        assertEquals(0, columnMeta.getScale());
        assertTrue(columnMeta.isNullable());
        assertEquals("aaa", columnMeta.getDefaultValue());
        assertEquals("comment2", columnMeta.getComment());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetDbTableMetaList() throws Exception {
        final MockResultSet resultSet = new MockResultSet();

        ArrayMap rowData = new ArrayMap();
        rowData.put("TABLE_CAT", "catalog1");
        rowData.put("TABLE_SCHEM", "schemaName1");
        rowData.put("TABLE_NAME", "table1");
        rowData.put("REMARKS", "comment1");
        resultSet.addRowData(rowData);

        rowData = new ArrayMap();
        rowData.put("TABLE_CAT", "catalog2");
        rowData.put("TABLE_SCHEM", "schemaName2");
        rowData.put("TABLE_NAME", "table2");
        rowData.put("REMARKS", "comment2");
        resultSet.addRowData(rowData);

        rowData = new ArrayMap();
        rowData.put("TABLE_CAT", "catalog3");
        rowData.put("TABLE_SCHEM", "schemaName3");
        rowData.put("TABLE_NAME", "table3");
        rowData.put("REMARKS", "comment3");
        resultSet.addRowData(rowData);

        GenMockDatabaseMetaData metaData = new GenMockDatabaseMetaData() {

            @Override
            public ResultSet getTables(String catalog, String schemaPattern,
                    String tableNamePattern, String[] types)
                    throws SQLException {
                return resultSet;
            }
        };

        DbTableMetaReaderImpl reader = new DbTableMetaReaderImpl(
                new MockDataSource(), new StandardGenDialect(), "schemaName",
                ".*", "TABLE3", true);
        List<DbTableMeta> list = reader.getDbTableMetaList(metaData,
                "schemaName");
        assertEquals(2, list.size());
        assertEquals("catalog1", list.get(0).getCatalogName());
        assertEquals("schemaName1", list.get(0).getSchemaName());
        assertEquals("table1", list.get(0).getName());
        assertEquals("comment1", list.get(0).getComment());
        assertEquals("catalog2", list.get(1).getCatalogName());
        assertEquals("schemaName2", list.get(1).getSchemaName());
        assertEquals("table2", list.get(1).getName());
        assertEquals("comment2", list.get(1).getComment());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetDbForeignKeyMetaList() throws Exception {
        final MockResultSet resultSet = new MockResultSet();

        ArrayMap rowData = new ArrayMap();
        rowData.put("PKTABLE_CAT", "dept_catalog");
        rowData.put("PKTABLE_SCHEM", "dept_schema");
        rowData.put("PKTABLE_NAME", "dept");
        rowData.put("PKCOLUMN_NAME", "dept_no");
        rowData.put("FKCOLUMN_NAME", "dept_no_fk");
        rowData.put("FK_NAME", "emp_fk1");
        resultSet.addRowData(rowData);

        rowData = new ArrayMap();
        rowData.put("PKTABLE_CAT", "dept_catalog");
        rowData.put("PKTABLE_SCHEM", "dept_schema");
        rowData.put("PKTABLE_NAME", "dept");
        rowData.put("PKCOLUMN_NAME", "dept_name");
        rowData.put("FKCOLUMN_NAME", "dept_name_fk");
        rowData.put("FK_NAME", "emp_fk1");
        resultSet.addRowData(rowData);

        rowData = new ArrayMap();
        rowData.put("PKTABLE_CAT", "address_catalog");
        rowData.put("PKTABLE_SCHEM", "address_schema");
        rowData.put("PKTABLE_NAME", "address");
        rowData.put("PKCOLUMN_NAME", "address_name");
        rowData.put("FKCOLUMN_NAME", "address_name_fk");
        rowData.put("FK_NAME", "emp_fk2");
        resultSet.addRowData(rowData);

        GenMockDatabaseMetaData metaData = new GenMockDatabaseMetaData() {

            @Override
            public ResultSet getImportedKeys(String catalog, String schema,
                    String table) throws SQLException {
                return resultSet;
            }
        };

        DbTableMetaReaderImpl reader = new DbTableMetaReaderImpl(
                new MockDataSource(), new StandardGenDialect(), null, ".*", "",
                false);
        List<DbForeignKeyMeta> list = reader.getDbForeignKeyMetaList(metaData,
                new DbTableMeta());

        assertEquals(2, list.size());

        DbForeignKeyMeta fkMeta = list.get(0);
        assertEquals("emp_fk1", fkMeta.getName());
        assertEquals("dept_catalog", fkMeta.getPrimaryKeyCatalogName());
        assertEquals("dept_schema", fkMeta.getPrimaryKeySchemaName());
        assertEquals("dept", fkMeta.getPrimaryKeyTableName());
        assertEquals(2, fkMeta.getPrimaryKeyColumnNameList().size());
        assertEquals(Arrays.asList("dept_no", "dept_name"), fkMeta
                .getPrimaryKeyColumnNameList());
        assertEquals(2, fkMeta.getForeignKeyColumnNameList().size());
        assertEquals(Arrays.asList("dept_no_fk", "dept_name_fk"), fkMeta
                .getForeignKeyColumnNameList());

        fkMeta = list.get(1);
        assertEquals("emp_fk2", fkMeta.getName());
        assertEquals("address_catalog", fkMeta.getPrimaryKeyCatalogName());
        assertEquals("address_schema", fkMeta.getPrimaryKeySchemaName());
        assertEquals("address", fkMeta.getPrimaryKeyTableName());
        assertEquals(1, fkMeta.getPrimaryKeyColumnNameList().size());
        assertEquals(Arrays.asList("address_name"), fkMeta
                .getPrimaryKeyColumnNameList());
        assertEquals(1, fkMeta.getForeignKeyColumnNameList().size());
        assertEquals(Arrays.asList("address_name_fk"), fkMeta
                .getForeignKeyColumnNameList());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetDbUniqueKeyMetaList() throws Exception {
        final MockResultSet resultSet = new MockResultSet();

        ArrayMap rowData = new ArrayMap();
        rowData.put("INDEX_NAME", "hoge");
        rowData.put("COLUMN_NAME", "aaa");
        resultSet.addRowData(rowData);

        rowData = new ArrayMap();
        rowData.put("INDEX_NAME", "hoge");
        rowData.put("COLUMN_NAME", "bbb");
        resultSet.addRowData(rowData);

        rowData = new ArrayMap();
        rowData.put("INDEX_NAME", "foo");
        rowData.put("COLUMN_NAME", "ccc");
        resultSet.addRowData(rowData);

        GenMockDatabaseMetaData metaData = new GenMockDatabaseMetaData() {

            @Override
            public ResultSet getIndexInfo(String catalog, String schema,
                    String table, boolean unique, boolean approximate)
                    throws SQLException {
                return resultSet;
            }
        };

        DbTableMetaReaderImpl reader = new DbTableMetaReaderImpl(
                new MockDataSource(), new StandardGenDialect(), null, ".*", "",
                false);
        List<DbUniqueKeyMeta> list = reader.getDbUniqueKeyMetaList(metaData,
                new DbTableMeta());

        assertEquals(2, list.size());

        DbUniqueKeyMeta ukMeta = list.get(0);
        assertEquals("hoge", ukMeta.getName());
        assertEquals(2, ukMeta.getColumnNameList().size());
        assertEquals("aaa", ukMeta.getColumnNameList().get(0));
        assertEquals("bbb", ukMeta.getColumnNameList().get(1));

        ukMeta = list.get(1);
        assertEquals("foo", ukMeta.getName());
        assertEquals(1, ukMeta.getColumnNameList().size());
        assertEquals("ccc", ukMeta.getColumnNameList().get(0));
    }
}
