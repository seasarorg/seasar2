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
package org.seasar.extension.jdbc.gen;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.ForeignKeyDesc;
import org.seasar.extension.jdbc.gen.SchemaModel;
import org.seasar.extension.jdbc.gen.TableDesc;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class SchemaModelTest {

    @Test
    public void testQuote() throws Exception {
        SchemaModel model = new SchemaModel();
        model.setDialect(new StandardGenDialect());
        assertEquals("\"HOGE\"", model.quote("HOGE"));
    }

    @Test
    public void testGetQuotedTableName() throws Exception {
        SchemaModel model = new SchemaModel();
        model.setDialect(new StandardGenDialect());
        TableDesc tableDesc = new TableDesc();
        tableDesc.setCatalogName("AAA");
        tableDesc.setSchemaName("BBB");
        tableDesc.setName("HOGE");
        assertEquals("\"AAA\".\"BBB\".\"HOGE\"", model
                .getQuotedTableName(tableDesc));
    }

    @Test
    public void testGetQuotedReferencedTableName() throws Exception {
        SchemaModel model = new SchemaModel();
        model.setDialect(new StandardGenDialect());
        ForeignKeyDesc foreignKeyDesc = new ForeignKeyDesc();
        foreignKeyDesc.setReferencedCatalogName("AAA");
        foreignKeyDesc.setReferencedSchemaName("BBB");
        foreignKeyDesc.setReferencedTableName("HOGE");
        assertEquals("\"AAA\".\"BBB\".\"HOGE\"", model
                .getQuotedReferencedTableName(foreignKeyDesc));
    }

}
