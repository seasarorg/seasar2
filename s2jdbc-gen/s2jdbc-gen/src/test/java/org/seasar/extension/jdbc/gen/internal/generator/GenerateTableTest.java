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
package org.seasar.extension.jdbc.gen.internal.generator;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.desc.ColumnDesc;
import org.seasar.extension.jdbc.gen.desc.DatabaseDesc;
import org.seasar.extension.jdbc.gen.desc.PrimaryKeyDesc;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.generator.GenerationContext;
import org.seasar.extension.jdbc.gen.internal.dialect.MssqlGenDialect;
import org.seasar.extension.jdbc.gen.internal.model.DdlModelFactoryImpl;
import org.seasar.extension.jdbc.gen.model.DdlModel;
import org.seasar.extension.jdbc.gen.model.SqlIdentifierCaseType;
import org.seasar.extension.jdbc.gen.model.SqlKeywordCaseType;
import org.seasar.framework.util.TextUtil;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class GenerateTableTest {

    private GeneratorImplStub generator;

    private DdlModel model;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        generator = new GeneratorImplStub();

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

        ColumnDesc no2 = new ColumnDesc();
        no2.setName("no");
        no2.setDefinition("integer");
        no2.setNullable(false);
        no2.setUnique(true);

        ColumnDesc name2 = new ColumnDesc();
        name2.setName("name");
        name2.setDefinition("varchar");
        name2.setNullable(false);
        name2.setUnique(false);

        PrimaryKeyDesc primaryKeyDesc2 = new PrimaryKeyDesc();
        primaryKeyDesc2.addColumnName("no");
        primaryKeyDesc2.addColumnName("name");

        TableDesc tableDesc2 = new TableDesc();
        tableDesc2.setCatalogName("AAA");
        tableDesc2.setSchemaName("BBB");
        tableDesc2.setName("FOO");
        tableDesc2.setCanonicalName("aaa.bbb.foo");
        tableDesc2.addColumnDesc(no2);
        tableDesc2.addColumnDesc(name2);
        tableDesc2.setPrimaryKeyDesc(primaryKeyDesc2);

        ColumnDesc no3 = new ColumnDesc();
        no3.setName("no");
        no3.setDefinition("integer");
        no3.setNullable(true);
        no3.setUnique(true);

        ColumnDesc name3 = new ColumnDesc();
        name3.setName("name");
        name3.setDefinition("varchar");
        name3.setNullable(true);
        name3.setUnique(false);

        TableDesc tableDesc3 = new TableDesc();
        tableDesc3.setCatalogName("AAA");
        tableDesc3.setSchemaName("BBB");
        tableDesc3.setName("BAR");
        tableDesc3.setCanonicalName("aaa.bbb.bar");
        tableDesc3.addColumnDesc(no3);
        tableDesc3.addColumnDesc(name3);

        DatabaseDesc databaseDesc = new DatabaseDesc();
        databaseDesc.addTableDesc(tableDesc);
        databaseDesc.addTableDesc(tableDesc2);
        databaseDesc.addTableDesc(tableDesc3);

        DdlModelFactoryImpl factory = new DdlModelFactoryImpl(
                new MssqlGenDialect(), SqlKeywordCaseType.ORIGINALCASE,
                SqlIdentifierCaseType.ORIGINALCASE, ';', "SCHEMA_INFO",
                "VERSION", null);
        model = factory.getDdlModel(databaseDesc, 0);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreate() throws Exception {
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "sql/create-table.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/") + "_Create.txt";
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

        DatabaseDesc databaseDesc = new DatabaseDesc();
        databaseDesc.addTableDesc(tableDesc);

        DdlModelFactoryImpl factory = new DdlModelFactoryImpl(
                new MssqlGenDialect(), SqlKeywordCaseType.ORIGINALCASE,
                SqlIdentifierCaseType.ORIGINALCASE, ';', "SCHEMA_INFO",
                "VERSION", "ENGINE = INNODB");
        model = factory.getDdlModel(databaseDesc, 0);

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
    public void testDrop() throws Exception {
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "sql/drop-table.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/") + "_Drop.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

}
