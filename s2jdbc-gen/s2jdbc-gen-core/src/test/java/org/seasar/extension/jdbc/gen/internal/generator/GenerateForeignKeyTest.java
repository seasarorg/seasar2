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
import org.seasar.extension.jdbc.gen.desc.DatabaseDesc;
import org.seasar.extension.jdbc.gen.desc.ForeignKeyDesc;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.generator.GenerationContext;
import org.seasar.extension.jdbc.gen.internal.dialect.StandardGenDialect;
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
public class GenerateForeignKeyTest {

    private GeneratorImplStub generator;

    private DdlModel model;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        generator = new GeneratorImplStub();

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

        DatabaseDesc databaseDesc = new DatabaseDesc();
        databaseDesc.addTableDesc(tableDesc);

        DdlModelFactoryImpl factory = new DdlModelFactoryImpl(
                new StandardGenDialect(), SqlKeywordCaseType.ORIGINALCASE,
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
                "dir"), new File("file"), "sql/create-foreignkey.ftl", "UTF-8",
                false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_Create.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testDrop() throws Exception {
        GenerationContext context = new GenerationContextImpl(model, new File(
                "dir"), new File("file"), "sql/drop-foreignkey.ftl", "UTF-8",
                false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_Drop.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }
}
