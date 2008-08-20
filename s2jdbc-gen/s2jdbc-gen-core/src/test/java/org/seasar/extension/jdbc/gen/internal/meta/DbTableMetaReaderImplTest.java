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

        GenMockDatabaseMetaData metaData = new GenMockDatabaseMetaData() {

            @Override
            public ResultSet getPrimaryKeys(String catalog, String schema,
                    String table) throws SQLException {
                return resultSet;
            }

        };
        DbTableMetaReaderImpl reader = new DbTableMetaReaderImpl(
                new MockDataSource(), new StandardGenDialect(), "schemaName",
                ".*", "");
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
                ".*", "");
        List<DbColumnMeta> list = reader.getDbColumnMetaList(metaData,
                "catalogName", "schemaName", "SchemaInfoTableName");
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
    public void testGetDbTableMetaList() throws Exception {
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

        rowData = new ArrayMap();
        rowData.put("TABLE_CAT", "catalog3");
        rowData.put("TABLE_SCHEM", "schemaName3");
        rowData.put("TABLE_NAME", "table3");
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
                ".*", "TABLE3");
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

    @Test
    public void testGetDbForeignKeyMetaList() throws Exception {
        final MockResultSet resultSet = new MockResultSet();

        ArrayMap rowData = new ArrayMap();
        rowData.put("PKTABLE_CAT", "dept_catalog");
        rowData.put("PKTABLE_SCHEM", "dept_schema");
        rowData.put("PKTABLE_NAME", "dept");
        rowData.put("PKCOLUMN_NAME", "dept_no");
        rowData.put("FKTABLE_CAT", "emp_catalog");
        rowData.put("FKTABLE_SCHEM ", "emp_schema");
        rowData.put("FKTABLE_NAME", "emp");
        rowData.put("FKCOLUMN_NAME", "dept_no_fk");
        rowData.put("KEY_SEQ", null);
        rowData.put("UPDATE_RULE", null);
        rowData.put("DELETE_RULE", null);
        rowData.put("FK_NAME", "emp_fk1");
        rowData.put("PK_NAME", "dept_pk1");
        rowData.put("DEFERRABILITY ", null);
        resultSet.addRowData(rowData);

        rowData = new ArrayMap();
        rowData.put("PKTABLE_CAT", "dept_catalog");
        rowData.put("PKTABLE_SCHEM", "dept_schema");
        rowData.put("PKTABLE_NAME", "dept");
        rowData.put("PKCOLUMN_NAME", "dept_name");
        rowData.put("FKTABLE_CAT", "emp_catalog");
        rowData.put("FKTABLE_SCHEM ", "emp_schema");
        rowData.put("FKTABLE_NAME", "emp");
        rowData.put("FKCOLUMN_NAME", "dept_name_fk");
        rowData.put("KEY_SEQ", null);
        rowData.put("UPDATE_RULE", null);
        rowData.put("DELETE_RULE", null);
        rowData.put("FK_NAME", "emp_fk1");
        rowData.put("PK_NAME", "dept_pk1");
        rowData.put("DEFERRABILITY ", null);
        resultSet.addRowData(rowData);

        rowData = new ArrayMap();
        rowData.put("PKTABLE_CAT", "address_catalog");
        rowData.put("PKTABLE_SCHEM", "address_schema");
        rowData.put("PKTABLE_NAME", "address");
        rowData.put("PKCOLUMN_NAME", "address_name");
        rowData.put("FKTABLE_CAT", "emp_catalog");
        rowData.put("FKTABLE_SCHEM ", "emp_schema");
        rowData.put("FKTABLE_NAME", "emp");
        rowData.put("FKCOLUMN_NAME", "address_name_fk");
        rowData.put("KEY_SEQ", null);
        rowData.put("UPDATE_RULE", null);
        rowData.put("DELETE_RULE", null);
        rowData.put("FK_NAME", "emp_fk2");
        rowData.put("PK_NAME", "address_pk1");
        rowData.put("DEFERRABILITY ", null);
        resultSet.addRowData(rowData);

        GenMockDatabaseMetaData metaData = new GenMockDatabaseMetaData() {

            @Override
            public ResultSet getImportedKeys(String catalog, String schema,
                    String table) throws SQLException {
                return resultSet;
            }
        };

        DbTableMetaReaderImpl reader = new DbTableMetaReaderImpl(
                new MockDataSource(), new StandardGenDialect(), null, ".*", "");
        List<DbForeignKeyMeta> list = reader.getDbForeignKeyMetaList(metaData,
                "emp_catalog", "emp_schema", "emp");

        assertEquals(2, list.size());

        DbForeignKeyMeta fkMeta = list.get(0);
        assertEquals("emp_fk1", fkMeta.getForeignKeyName());
        assertEquals("dept_catalog", fkMeta.getPrimaryKeyCatalogName());
        assertEquals("dept_schema", fkMeta.getPrimaryKeySchemaName());
        assertEquals("dept", fkMeta.getPrimaryKeyTableName());
        assertEquals("emp_catalog", fkMeta.getForeignKeyCatalogName());
        assertEquals("emp_schema", fkMeta.getForeignKeySchemaName());
        assertEquals("emp", fkMeta.getForeignKeyTableName());
        assertEquals(2, fkMeta.getPrimaryKeyColumnNameList().size());
        assertEquals(Arrays.asList("dept_no", "dept_name"), fkMeta
                .getPrimaryKeyColumnNameList());
        assertEquals(2, fkMeta.getForeignKeyColumnNameList().size());
        assertEquals(Arrays.asList("dept_no_fk", "dept_name_fk"), fkMeta
                .getForeignKeyColumnNameList());

        fkMeta = list.get(1);
        assertEquals("emp_fk2", fkMeta.getForeignKeyName());
        assertEquals("address_catalog", fkMeta.getPrimaryKeyCatalogName());
        assertEquals("address_schema", fkMeta.getPrimaryKeySchemaName());
        assertEquals("address", fkMeta.getPrimaryKeyTableName());
        assertEquals("emp_catalog", fkMeta.getForeignKeyCatalogName());
        assertEquals("emp_schema", fkMeta.getForeignKeySchemaName());
        assertEquals("emp", fkMeta.getForeignKeyTableName());
        assertEquals(1, fkMeta.getPrimaryKeyColumnNameList().size());
        assertEquals(Arrays.asList("address_name"), fkMeta
                .getPrimaryKeyColumnNameList());
        assertEquals(1, fkMeta.getForeignKeyColumnNameList().size());
        assertEquals(Arrays.asList("address_name_fk"), fkMeta
                .getForeignKeyColumnNameList());
    }
}
