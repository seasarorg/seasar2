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

import java.util.Arrays;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.DbModel;
import org.seasar.extension.jdbc.gen.SequenceDesc;
import org.seasar.extension.jdbc.gen.TableDesc;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class DbModelFactoryImplTest {

    private DbModelFactoryImpl factory = new DbModelFactoryImpl(
            new StandardGenDialect(), ';');

    @Test
    public void testGetSchemaModel() throws Exception {
        SequenceDesc sequenceDesc = new SequenceDesc();
        sequenceDesc.setSequenceName("HOGE");
        TableDesc tableDesc = new TableDesc();
        tableDesc.setName("AAA");
        tableDesc.addSequenceDesc(sequenceDesc);

        SequenceDesc sequenceDesc2 = new SequenceDesc();
        sequenceDesc2.setSequenceName("FOO");
        TableDesc tableDesc2 = new TableDesc();
        tableDesc2.setName("BBB");
        tableDesc2.addSequenceDesc(sequenceDesc2);

        DbModel model = factory.getSchemaModel(Arrays.asList(tableDesc,
                tableDesc2));
        assertNotNull(model);
        assertNotNull(model.getDialect());
        assertEquals(2, model.getTableDescList().size());
        assertEquals(2, model.getSequenceDescList().size());
    }

    @Test
    public void testGetSchemaModel_duplicatedSequenceDesc() throws Exception {
        SequenceDesc sequenceDesc = new SequenceDesc();
        sequenceDesc.setSequenceName("HOGE");
        TableDesc tableDesc = new TableDesc();
        tableDesc.setName("AAA");
        tableDesc.addSequenceDesc(sequenceDesc);

        SequenceDesc sequenceDesc2 = new SequenceDesc();
        sequenceDesc2.setSequenceName("HOGE");
        TableDesc tableDesc2 = new TableDesc();
        tableDesc2.setName("BBB");
        tableDesc2.addSequenceDesc(sequenceDesc2);

        DbModel model = factory.getSchemaModel(Arrays.asList(tableDesc,
                tableDesc2));
        assertNotNull(model);
        assertNotNull(model.getDialect());
        assertEquals(2, model.getTableDescList().size());
        assertEquals(1, model.getSequenceDescList().size());
    }

    @Test
    public void testGetSchemaModel_idTableDesc() throws Exception {
        TableDesc tableDesc = new TableDesc();
        tableDesc.setName("AAA");

        TableDesc idTableDesc = new TableDesc();
        idTableDesc.setName("CCC");

        TableDesc tableDesc2 = new TableDesc();
        tableDesc2.setName("BBB");
        tableDesc2.addIdTableDesc(idTableDesc);

        DbModel model = factory.getSchemaModel(Arrays.asList(tableDesc,
                tableDesc2));
        assertNotNull(model);
        assertNotNull(model.getDialect());
        assertEquals(3, model.getTableDescList().size());
    }
}
