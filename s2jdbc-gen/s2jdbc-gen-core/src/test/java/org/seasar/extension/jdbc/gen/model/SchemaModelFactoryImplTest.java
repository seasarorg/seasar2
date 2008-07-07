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
package org.seasar.extension.jdbc.gen.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.SchemaModel;
import org.seasar.extension.jdbc.gen.TableDesc;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class SchemaModelFactoryImplTest {

    private SchemaModelFactoryImpl factory = new SchemaModelFactoryImpl(
            new StandardGenDialect());

    @Test
    public void testGetSchemaModel() throws Exception {
        List<TableDesc> tableDescList = new ArrayList<TableDesc>();
        tableDescList.add(new TableDesc());
        tableDescList.add(new TableDesc());
        SchemaModel model = factory.getSchemaModel(tableDescList);
        assertNotNull(model);
        assertNotNull(model.getDialect());
        assertEquals(2, model.getTableDescList().size());
    }

}
