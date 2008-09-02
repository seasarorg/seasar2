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
package org.seasar.extension.jdbc.gen.internal.model;

import java.util.Iterator;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.gen.model.NamesModel;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.EntityMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.TableMetaFactoryImpl;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class NamesModelFactoryImplTest {

    private EntityMetaFactoryImpl entityMetaFactory;

    private NamesModelFactoryImpl namesModelFactory;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        PersistenceConvention pc = new PersistenceConventionImpl();
        ColumnMetaFactoryImpl cmf = new ColumnMetaFactoryImpl();
        cmf.setPersistenceConvention(pc);
        PropertyMetaFactoryImpl propertyMetaFactory = new PropertyMetaFactoryImpl();
        propertyMetaFactory.setPersistenceConvention(pc);
        propertyMetaFactory.setColumnMetaFactory(cmf);
        TableMetaFactoryImpl tmf = new TableMetaFactoryImpl();
        tmf.setPersistenceConvention(pc);
        entityMetaFactory = new EntityMetaFactoryImpl();
        entityMetaFactory.setPersistenceConvention(pc);
        entityMetaFactory.setPropertyMetaFactory(propertyMetaFactory);
        entityMetaFactory.setTableMetaFactory(tmf);
        namesModelFactory = new NamesModelFactoryImpl("aaa.bbb", "Names");
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetNamesModel() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        NamesModel namesModel = namesModelFactory.getNamesModel(entityMeta);
        assertNotNull(namesModel);
        assertEquals("aaa.bbb", namesModel.getPackageName());
        assertEquals("NamesModelFactoryImplTest$AaaNames", namesModel
                .getShortClassName());
        assertEquals(2, namesModel.getNameList().size());
        assertEquals("id", namesModel.getNameList().get(0));
        assertEquals("name", namesModel.getNameList().get(1));
        assertEquals(1, namesModel.getImportNameSet().size());
        Iterator<String> iterator = namesModel.getImportNameSet().iterator();
        assertEquals(
                "org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImplTest$Aaa",
                iterator.next());
    }

    /** */
    @Entity
    public static class Aaa {

        /** */
        @Id
        protected Integer id;

        /** */
        protected String name;
    }
}
