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

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.desc.ColumnDesc;
import org.seasar.extension.jdbc.gen.desc.TableDesc;
import org.seasar.extension.jdbc.gen.generator.GenerationContext;
import org.seasar.extension.jdbc.gen.internal.dialect.PostgreGenDialect;
import org.seasar.extension.jdbc.gen.internal.model.TableModelFactoryImpl;
import org.seasar.extension.jdbc.gen.model.SqlIdentifierCaseType;
import org.seasar.extension.jdbc.gen.model.SqlKeywordCaseType;
import org.seasar.extension.jdbc.gen.model.TableModel;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.util.TextUtil;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class GenerateAuxiliaryTest {

    private GeneratorImplStub generator;

    /**
     * @throws Exception
     * 
     */
    @Before
    public void setUp() throws Exception {
        generator = new GeneratorImplStub();
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCreate() throws Exception {
        ColumnDesc no1 = new ColumnDesc();
        no1.setName("no1");
        no1.setDefinition("serial");
        no1.setIdentity(false);
        no1.setNullable(false);
        no1.setUnique(true);

        ColumnDesc no2 = new ColumnDesc();
        no2.setName("no2");
        no2.setDefinition(" BIGSERIAL ");
        no2.setIdentity(false);
        no2.setNullable(false);
        no2.setUnique(true);

        ColumnDesc name = new ColumnDesc();
        name.setName("name");
        name.setDefinition("varchar");
        name.setNullable(false);
        name.setUnique(false);

        TableDesc tableDesc = new TableDesc();
        tableDesc.setCatalogName("AAA");
        tableDesc.setSchemaName("BBB");
        tableDesc.setName("HOGE");
        tableDesc.setCanonicalName("aaa.bbb.hoge");
        tableDesc.addColumnDesc(no1);
        tableDesc.addColumnDesc(no2);
        tableDesc.addColumnDesc(name);

        TableModelFactoryImpl factory = new TableModelFactoryImpl(
                new PostgreGenDialect(), new MockDataSource(),
                SqlIdentifierCaseType.ORIGINALCASE,
                SqlKeywordCaseType.ORIGINALCASE, ';', null, false) {

            @Override
            protected Long getNextValue(String sequenceName, int allocationSize) {
                return null;
            }
        };
        TableModel model = factory.getTableModel(tableDesc);

        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "sql/create-auxiliary.ftl", "UTF-8", false);
        generator.generate(context);

        System.out.println(generator.getResult());

        String path = getClass().getName().replace(".", "/") + "_Create.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testDrop() throws Exception {
        ColumnDesc no1 = new ColumnDesc();
        no1.setName("no1");
        no1.setDefinition("serial");
        no1.setIdentity(false);
        no1.setNullable(false);
        no1.setUnique(true);

        ColumnDesc no2 = new ColumnDesc();
        no2.setName("no2");
        no2.setDefinition(" BIGSERIAL ");
        no2.setIdentity(false);
        no2.setNullable(false);
        no2.setUnique(true);

        ColumnDesc name = new ColumnDesc();
        name.setName("name");
        name.setDefinition("varchar");
        name.setNullable(false);
        name.setUnique(false);

        TableDesc tableDesc = new TableDesc();
        tableDesc.setCatalogName("AAA");
        tableDesc.setSchemaName("BBB");
        tableDesc.setName("HOGE");
        tableDesc.setCanonicalName("aaa.bbb.hoge");
        tableDesc.addColumnDesc(no1);
        tableDesc.addColumnDesc(no2);
        tableDesc.addColumnDesc(name);

        TableModelFactoryImpl factory = new TableModelFactoryImpl(
                new PostgreGenDialect(), new MockDataSource(),
                SqlIdentifierCaseType.ORIGINALCASE,
                SqlKeywordCaseType.ORIGINALCASE, ';', null, false) {

            @Override
            protected Long getNextValue(String sequenceName, int allocationSize) {
                return null;
            }
        };
        TableModel model = factory.getTableModel(tableDesc);

        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "sql/drop-auxiliary.ftl", "UTF-8", false);
        generator.generate(context);

        System.out.println(generator.getResult());

        String path = getClass().getName().replace(".", "/") + "_Drop.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }
}
