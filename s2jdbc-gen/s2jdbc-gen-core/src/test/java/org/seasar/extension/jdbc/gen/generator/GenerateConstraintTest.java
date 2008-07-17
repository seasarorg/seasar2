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
package org.seasar.extension.jdbc.gen.generator;

import java.io.File;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.ForeignKeyDesc;
import org.seasar.extension.jdbc.gen.GenerationContext;
import org.seasar.extension.jdbc.gen.PrimaryKeyDesc;
import org.seasar.extension.jdbc.gen.DbModel;
import org.seasar.extension.jdbc.gen.TableDesc;
import org.seasar.extension.jdbc.gen.UniqueKeyDesc;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;
import org.seasar.extension.jdbc.gen.model.DbModelFactoryImpl;
import org.seasar.framework.util.TextUtil;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class GenerateConstraintTest {

    private GeneratorImplStub generator;

    private DbModel model;

    @Before
    public void setUp() throws Exception {
        generator = new GeneratorImplStub("UTF-8");

        PrimaryKeyDesc primaryKeyDesc = new PrimaryKeyDesc();
        primaryKeyDesc.addColumnName("PK1");
        primaryKeyDesc.addColumnName("PK2");

        UniqueKeyDesc uniqueKeyDesc = new UniqueKeyDesc();
        uniqueKeyDesc.addColumnName("UK1-1");
        uniqueKeyDesc.addColumnName("UK1-2");

        UniqueKeyDesc uniqueKeyDesc2 = new UniqueKeyDesc();
        uniqueKeyDesc2.addColumnName("UK2-1");
        uniqueKeyDesc2.addColumnName("UK2-2");

        ForeignKeyDesc foreignKeyDesc = new ForeignKeyDesc();
        foreignKeyDesc.addColumnName("FK1-1");
        foreignKeyDesc.addColumnName("FK1-2");
        foreignKeyDesc.setReferencedCatalogName("CCC");
        foreignKeyDesc.setReferencedSchemaName("DDD");
        foreignKeyDesc.setReferencedTableName("FOO");
        foreignKeyDesc.addReferencedColumnName("REF1-1");
        foreignKeyDesc.addReferencedColumnName("REF1-2");

        ForeignKeyDesc foreignKeyDesc2 = new ForeignKeyDesc();
        foreignKeyDesc2.addColumnName("FK2-1");
        foreignKeyDesc2.addColumnName("FK2-2");
        foreignKeyDesc2.setReferencedCatalogName("EEE");
        foreignKeyDesc2.setReferencedSchemaName("FFF");
        foreignKeyDesc2.setReferencedTableName("BAR");
        foreignKeyDesc2.addReferencedColumnName("REF2-1");
        foreignKeyDesc2.addReferencedColumnName("REF2-2");

        TableDesc tableDesc = new TableDesc();
        tableDesc.setCatalogName("AAA");
        tableDesc.setSchemaName("BBB");
        tableDesc.setName("HOGE");
        tableDesc.setPrimaryKeyDesc(primaryKeyDesc);
        tableDesc.addUniqueKeyDesc(uniqueKeyDesc);
        tableDesc.addUniqueKeyDesc(uniqueKeyDesc2);
        tableDesc.addForeignKeyDesc(foreignKeyDesc);
        tableDesc.addForeignKeyDesc(foreignKeyDesc2);

        DbModelFactoryImpl factory = new DbModelFactoryImpl(
                new StandardGenDialect(), ';');
        model = factory.getSchemaModel(Arrays.asList(tableDesc));
    }

    @Test
    public void testCreate() throws Exception {
        GenerationContext context = new GenerationContext(model,
                new File("dir"), new File("file"), "sql/create-constraint.ftl",
                "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_Create.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    @Test
    public void testDrop() throws Exception {
        GenerationContext context = new GenerationContext(model,
                new File("dir"), new File("file"), "sql/drop-constraint.ftl",
                "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_Drop.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }
}
