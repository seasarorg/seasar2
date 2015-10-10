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
package org.seasar.extension.jdbc.gen.internal.generator;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.desc.ColumnDesc;
import org.seasar.extension.jdbc.gen.desc.PrimaryKeyDesc;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.generator.GenerationContext;
import org.seasar.extension.jdbc.gen.internal.dialect.MssqlGenDialect;
import org.seasar.extension.jdbc.gen.internal.dialect.MysqlGenDialect;
import org.seasar.extension.jdbc.gen.internal.dialect.OracleGenDialect;
import org.seasar.extension.jdbc.gen.internal.model.TableModelFactoryImpl;
import org.seasar.extension.jdbc.gen.model.SqlIdentifierCaseType;
import org.seasar.extension.jdbc.gen.model.SqlKeywordCaseType;
import org.seasar.extension.jdbc.gen.model.TableModel;
import org.seasar.framework.mock.sql.MockConnection;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.mock.sql.MockPreparedStatement;
import org.seasar.framework.mock.sql.MockResultSet;
import org.seasar.framework.util.ArrayMap;
import org.seasar.framework.util.TextUtil;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class GenerateTableTest {

    private GeneratorImplStub generator;

    private DataSource dataSource;

    /**
     * @throws Exception
     * 
     */
    @Before
    public void setUp() throws Exception {
        generator = new GeneratorImplStub();
        dataSource = new MockDataSource() {

            @Override
            public Connection getConnection() throws SQLException {
                return new MockConnection() {

                    @Override
                    public MockPreparedStatement prepareMockStatement(String sql) {
                        return new MockPreparedStatement(this, sql) {

                            @Override
                            public ResultSet executeQuery() throws SQLException {
                                MockResultSet resultSet = new MockResultSet();
                                ArrayMap map = new ArrayMap();
                                map.put(null, 200);
                                resultSet.addRowData(map);
                                return resultSet;
                            }
                        };
                    }
                };
            }
        };
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreate_singleId() throws Exception {
        ColumnDesc no = new ColumnDesc();
        no.setName("no");
        no.setDefinition("integer");
        no.setIdentity(true);
        no.setNullable(false);
        no.setUnique(true);

        ColumnDesc name = new ColumnDesc();
        name.setName("name");
        name.setDefinition("varchar");
        name.setNullable(false);
        name.setUnique(false);

        PrimaryKeyDesc primaryKeyDesc = new PrimaryKeyDesc();
        primaryKeyDesc.addColumnName("no");

        TableDesc tableDesc = new TableDesc();
        tableDesc.setCatalogName("AAA");
        tableDesc.setSchemaName("BBB");
        tableDesc.setName("HOGE");
        tableDesc.setCanonicalName("aaa.bbb.hoge");
        tableDesc.addColumnDesc(no);
        tableDesc.addColumnDesc(name);
        tableDesc.setPrimaryKeyDesc(primaryKeyDesc);

        TableModelFactoryImpl factory = new TableModelFactoryImpl(
                new MssqlGenDialect(), new MockDataSource(),
                SqlIdentifierCaseType.ORIGINALCASE,
                SqlKeywordCaseType.ORIGINALCASE, ';', null, false) {

            @Override
            protected Long getNextValue(String sequenceName, int allocationSize) {
                return null;
            }
        };
        TableModel model = factory.getTableModel(tableDesc);

        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "sql/create-table.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/")
                + "_Create_singleId.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreate_compositeId() throws Exception {
        ColumnDesc no = new ColumnDesc();
        no.setName("no");
        no.setDefinition("integer");
        no.setNullable(false);
        no.setUnique(true);

        ColumnDesc name = new ColumnDesc();
        name.setName("name");
        name.setDefinition("varchar");
        name.setNullable(false);
        name.setUnique(false);

        PrimaryKeyDesc primaryKeyDesc = new PrimaryKeyDesc();
        primaryKeyDesc.addColumnName("no");
        primaryKeyDesc.addColumnName("name");

        TableDesc tableDesc = new TableDesc();
        tableDesc.setCatalogName("AAA");
        tableDesc.setSchemaName("BBB");
        tableDesc.setName("FOO");
        tableDesc.setCanonicalName("aaa.bbb.foo");
        tableDesc.addColumnDesc(no);
        tableDesc.addColumnDesc(name);
        tableDesc.setPrimaryKeyDesc(primaryKeyDesc);

        TableModelFactoryImpl factory = new TableModelFactoryImpl(
                new MssqlGenDialect(), dataSource,
                SqlIdentifierCaseType.ORIGINALCASE,
                SqlKeywordCaseType.ORIGINALCASE, ';', null, false);
        TableModel model = factory.getTableModel(tableDesc);

        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "sql/create-table.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/")
                + "_Create_compositeId.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreate_noId() throws Exception {
        ColumnDesc no = new ColumnDesc();
        no.setName("no");
        no.setDefinition("integer");
        no.setNullable(true);
        no.setUnique(true);

        ColumnDesc name = new ColumnDesc();
        name.setName("name");
        name.setDefinition("varchar");
        name.setNullable(true);
        name.setUnique(false);

        TableDesc tableDesc = new TableDesc();
        tableDesc.setCatalogName("AAA");
        tableDesc.setSchemaName("BBB");
        tableDesc.setName("BAR");
        tableDesc.setCanonicalName("aaa.bbb.bar");
        tableDesc.addColumnDesc(no);
        tableDesc.addColumnDesc(name);

        TableModelFactoryImpl factory = new TableModelFactoryImpl(
                new MssqlGenDialect(), dataSource,
                SqlIdentifierCaseType.ORIGINALCASE,
                SqlKeywordCaseType.ORIGINALCASE, ';', null, false);
        TableModel model = factory.getTableModel(tableDesc);

        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "sql/create-table.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/")
                + "_Create_noId.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreate_tableOption() throws Exception {
        ColumnDesc no = new ColumnDesc();
        no.setName("no");
        no.setDefinition("integer");
        no.setIdentity(true);
        no.setNullable(false);
        no.setUnique(true);

        ColumnDesc name = new ColumnDesc();
        name.setName("name");
        name.setDefinition("varchar");
        name.setNullable(false);
        name.setUnique(false);

        PrimaryKeyDesc primaryKeyDesc = new PrimaryKeyDesc();
        primaryKeyDesc.addColumnName("no");

        TableDesc tableDesc = new TableDesc();
        tableDesc.setCatalogName("AAA");
        tableDesc.setSchemaName("BBB");
        tableDesc.setName("HOGE");
        tableDesc.setCanonicalName("aaa.bbb.hoge");
        tableDesc.addColumnDesc(no);
        tableDesc.addColumnDesc(name);
        tableDesc.setPrimaryKeyDesc(primaryKeyDesc);

        TableModelFactoryImpl factory = new TableModelFactoryImpl(
                new MssqlGenDialect(), dataSource,
                SqlIdentifierCaseType.ORIGINALCASE,
                SqlKeywordCaseType.ORIGINALCASE, ';', "ENGINE = INNODB", false);
        TableModel model = factory.getTableModel(tableDesc);

        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "sql/create-table.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/")
                + "_Create_tableOption.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreate_commentOn() throws Exception {
        ColumnDesc no = new ColumnDesc();
        no.setName("no");
        no.setDefinition("integer");
        no.setComment("番号カラム");
        no.setNullable(false);
        no.setUnique(true);

        ColumnDesc name = new ColumnDesc();
        name.setName("name");
        name.setDefinition("varchar");
        name.setComment("'名前'カラム");
        name.setNullable(false);
        name.setUnique(false);

        PrimaryKeyDesc primaryKeyDesc = new PrimaryKeyDesc();
        primaryKeyDesc.addColumnName("no");

        TableDesc tableDesc = new TableDesc();
        tableDesc.setCatalogName("AAA");
        tableDesc.setSchemaName("BBB");
        tableDesc.setName("HOGE");
        tableDesc.setComment("HOGEテーブル");
        tableDesc.setCanonicalName("aaa.bbb.hoge");
        tableDesc.addColumnDesc(no);
        tableDesc.addColumnDesc(name);
        tableDesc.setPrimaryKeyDesc(primaryKeyDesc);

        TableModelFactoryImpl factory = new TableModelFactoryImpl(
                new OracleGenDialect(), dataSource,
                SqlIdentifierCaseType.ORIGINALCASE,
                SqlKeywordCaseType.ORIGINALCASE, ';', null, true);
        TableModel model = factory.getTableModel(tableDesc);

        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "sql/create-table.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/")
                + "_Create_commentOn.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreate_commentInCreateTable() throws Exception {
        ColumnDesc no = new ColumnDesc();
        no.setName("no");
        no.setDefinition("integer");
        no.setComment("番号カラム");
        no.setNullable(false);
        no.setUnique(true);

        ColumnDesc name = new ColumnDesc();
        name.setName("name");
        name.setDefinition("varchar");
        name.setComment("'名前'カラム");
        name.setNullable(false);
        name.setUnique(false);

        PrimaryKeyDesc primaryKeyDesc = new PrimaryKeyDesc();
        primaryKeyDesc.addColumnName("no");

        TableDesc tableDesc = new TableDesc();
        tableDesc.setCatalogName("AAA");
        tableDesc.setSchemaName("BBB");
        tableDesc.setName("HOGE");
        tableDesc.setComment("HOGEテーブル");
        tableDesc.setCanonicalName("aaa.bbb.hoge");
        tableDesc.addColumnDesc(no);
        tableDesc.addColumnDesc(name);
        tableDesc.setPrimaryKeyDesc(primaryKeyDesc);

        TableModelFactoryImpl factory = new TableModelFactoryImpl(
                new MysqlGenDialect(), dataSource,
                SqlIdentifierCaseType.ORIGINALCASE,
                SqlKeywordCaseType.ORIGINALCASE, ';', null, true);
        TableModel model = factory.getTableModel(tableDesc);

        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "sql/create-table.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/")
                + "_Create_commentInCreateTable.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testDrop() throws Exception {
        ColumnDesc no = new ColumnDesc();
        no.setName("no");
        no.setDefinition("integer");
        no.setIdentity(true);
        no.setNullable(false);
        no.setUnique(true);

        ColumnDesc name = new ColumnDesc();
        name.setName("name");
        name.setDefinition("varchar");
        name.setNullable(false);
        name.setUnique(false);

        PrimaryKeyDesc primaryKeyDesc = new PrimaryKeyDesc();
        primaryKeyDesc.addColumnName("no");

        TableDesc tableDesc = new TableDesc();
        tableDesc.setCatalogName("AAA");
        tableDesc.setSchemaName("BBB");
        tableDesc.setName("HOGE");
        tableDesc.setCanonicalName("aaa.bbb.hoge");
        tableDesc.addColumnDesc(no);
        tableDesc.addColumnDesc(name);
        tableDesc.setPrimaryKeyDesc(primaryKeyDesc);

        TableModelFactoryImpl factory = new TableModelFactoryImpl(
                new MssqlGenDialect(), dataSource,
                SqlIdentifierCaseType.ORIGINALCASE,
                SqlKeywordCaseType.ORIGINALCASE, ';', null, false);
        TableModel model = factory.getTableModel(tableDesc);

        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "sql/drop-table.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/") + "_Drop.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

}
