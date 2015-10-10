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
import org.seasar.extension.jdbc.annotation.ReferentialActionType;
import org.seasar.extension.jdbc.gen.desc.ForeignKeyDesc;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.generator.GenerationContext;
import org.seasar.extension.jdbc.gen.internal.dialect.StandardGenDialect;
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
public class GenerateForeignKeyTest {

    private GeneratorImplStub generator;

    private DataSource dataSource;

    private TableModelFactoryImpl factory;

    /**
     * 
     * @throws Exception
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

        factory = new TableModelFactoryImpl(new StandardGenDialect(),
                dataSource, SqlIdentifierCaseType.ORIGINALCASE,
                SqlKeywordCaseType.ORIGINALCASE, ';', null, false) {

            @Override
            protected Long getNextValue(String sequenceName, int allocationSize) {
                return null;
            }
        };

    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreate() throws Exception {
        ForeignKeyDesc foreignKeyDesc = new ForeignKeyDesc();
        foreignKeyDesc.addColumnName("FK1-1");
        foreignKeyDesc.addColumnName("FK1-2");
        foreignKeyDesc.setReferencedCatalogName("CCC");
        foreignKeyDesc.setReferencedSchemaName("DDD");
        foreignKeyDesc.setReferencedTableName("FOO");
        foreignKeyDesc.setReferencedFullTableName("CCC.DDD.FOO");
        foreignKeyDesc.addReferencedColumnName("REF1-1");
        foreignKeyDesc.addReferencedColumnName("REF1-2");

        ForeignKeyDesc foreignKeyDesc2 = new ForeignKeyDesc();
        foreignKeyDesc2.addColumnName("FK2-1");
        foreignKeyDesc2.addColumnName("FK2-2");
        foreignKeyDesc2.setReferencedCatalogName("EEE");
        foreignKeyDesc2.setReferencedSchemaName("FFF");
        foreignKeyDesc2.setReferencedTableName("BAR");
        foreignKeyDesc2.setReferencedFullTableName("EEE.FFF.BAR");
        foreignKeyDesc2.addReferencedColumnName("REF2-1");
        foreignKeyDesc2.addReferencedColumnName("REF2-2");

        TableDesc tableDesc = new TableDesc();
        tableDesc.setCatalogName("AAA");
        tableDesc.setSchemaName("BBB");
        tableDesc.setName("HOGE");
        tableDesc.setCanonicalName("aaa.bbb.hoge");
        tableDesc.addForeignKeyDesc(foreignKeyDesc);
        tableDesc.addForeignKeyDesc(foreignKeyDesc2);

        TableModel model = factory.getTableModel(tableDesc);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "sql/create-foreignkey.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_Create.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreate_referentialAction() throws Exception {
        ForeignKeyDesc foreignKeyDesc = new ForeignKeyDesc();
        foreignKeyDesc.addColumnName("FK1-1");
        foreignKeyDesc.addColumnName("FK1-2");
        foreignKeyDesc.setReferencedCatalogName("CCC");
        foreignKeyDesc.setReferencedSchemaName("DDD");
        foreignKeyDesc.setReferencedTableName("FOO");
        foreignKeyDesc.setReferencedFullTableName("CCC.DDD.FOO");
        foreignKeyDesc.addReferencedColumnName("REF1-1");
        foreignKeyDesc.addReferencedColumnName("REF1-2");
        foreignKeyDesc.setOnDelete(ReferentialActionType.CASCADE);
        foreignKeyDesc.setOnUpdate(ReferentialActionType.RESTRICT);

        ForeignKeyDesc foreignKeyDesc2 = new ForeignKeyDesc();
        foreignKeyDesc2.addColumnName("FK2-1");
        foreignKeyDesc2.addColumnName("FK2-2");
        foreignKeyDesc2.setReferencedCatalogName("EEE");
        foreignKeyDesc2.setReferencedSchemaName("FFF");
        foreignKeyDesc2.setReferencedTableName("BAR");
        foreignKeyDesc2.setReferencedFullTableName("EEE.FFF.BAR");
        foreignKeyDesc2.addReferencedColumnName("REF2-1");
        foreignKeyDesc2.addReferencedColumnName("REF2-2");
        foreignKeyDesc2.setOnDelete(ReferentialActionType.NO_ACTION);
        foreignKeyDesc2.setOnUpdate(ReferentialActionType.NO_ACTION);

        TableDesc tableDesc = new TableDesc();
        tableDesc.setCatalogName("AAA");
        tableDesc.setSchemaName("BBB");
        tableDesc.setName("HOGE");
        tableDesc.setCanonicalName("aaa.bbb.hoge");
        tableDesc.addForeignKeyDesc(foreignKeyDesc);
        tableDesc.addForeignKeyDesc(foreignKeyDesc2);

        TableModel model = factory.getTableModel(tableDesc);

        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "sql/create-foreignkey.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/")
                + "_Create_referentialAction.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testDrop() throws Exception {
        ForeignKeyDesc foreignKeyDesc = new ForeignKeyDesc();
        foreignKeyDesc.addColumnName("FK1-1");
        foreignKeyDesc.addColumnName("FK1-2");
        foreignKeyDesc.setReferencedCatalogName("CCC");
        foreignKeyDesc.setReferencedSchemaName("DDD");
        foreignKeyDesc.setReferencedTableName("FOO");
        foreignKeyDesc.setReferencedFullTableName("CCC.DDD.FOO");
        foreignKeyDesc.addReferencedColumnName("REF1-1");
        foreignKeyDesc.addReferencedColumnName("REF1-2");

        ForeignKeyDesc foreignKeyDesc2 = new ForeignKeyDesc();
        foreignKeyDesc2.addColumnName("FK2-1");
        foreignKeyDesc2.addColumnName("FK2-2");
        foreignKeyDesc2.setReferencedCatalogName("EEE");
        foreignKeyDesc2.setReferencedSchemaName("FFF");
        foreignKeyDesc2.setReferencedTableName("BAR");
        foreignKeyDesc2.setReferencedFullTableName("EEE.FFF.BAR");
        foreignKeyDesc2.addReferencedColumnName("REF2-1");
        foreignKeyDesc2.addReferencedColumnName("REF2-2");

        TableDesc tableDesc = new TableDesc();
        tableDesc.setCatalogName("AAA");
        tableDesc.setSchemaName("BBB");
        tableDesc.setName("HOGE");
        tableDesc.setCanonicalName("aaa.bbb.hoge");
        tableDesc.addForeignKeyDesc(foreignKeyDesc);
        tableDesc.addForeignKeyDesc(foreignKeyDesc2);

        TableModel model = factory.getTableModel(tableDesc);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "sql/drop-foreignkey.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_Drop.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }
}
