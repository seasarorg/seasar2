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
package org.seasar.extension.jdbc.gen.internal.model;

import java.util.Iterator;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.gen.model.NamesAssociationModel;
import org.seasar.extension.jdbc.gen.model.NamesAttributeModel;
import org.seasar.extension.jdbc.gen.model.NamesModel;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.EntityMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.TableMetaFactoryImpl;
import org.seasar.extension.jdbc.name.PropertyName;
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

        assertEquals(3, namesModel.getNamesAttributeModelList().size());
        NamesAttributeModel attributeModel = namesModel
                .getNamesAttributeModelList().get(0);
        assertEquals("id", attributeModel.getName());
        assertEquals(Integer.class, attributeModel.getAttributeClass());
        attributeModel = namesModel.getNamesAttributeModelList().get(1);
        assertEquals("name", attributeModel.getName());
        assertEquals(String.class, attributeModel.getAttributeClass());
        attributeModel = namesModel.getNamesAttributeModelList().get(2);
        assertEquals("bbbId", attributeModel.getName());
        assertEquals(Integer.class, attributeModel.getAttributeClass());

        assertEquals(1, namesModel.getNamesAssociationModelList().size());
        NamesAssociationModel associationModel = namesModel
                .getNamesAssociationModelList().get(0);
        assertEquals("bbb", associationModel.getName());
        assertEquals("_BbbNames", associationModel.getShortClassName());
        assertEquals("aaa.bbb.BbbNames._BbbNames", associationModel
                .getClassName());

        assertEquals(4, namesModel.getImportNameSet().size());
        Iterator<String> iterator = namesModel.getImportNameSet().iterator();
        assertEquals("aaa.bbb.BbbNames._BbbNames", iterator.next());
        assertEquals(Generated.class.getCanonicalName(), iterator.next());
        assertEquals(Aaa.class.getCanonicalName(), iterator.next());
        assertEquals(PropertyName.class.getCanonicalName(), iterator.next());
    }

    /** */
    @Entity
    public static class Aaa {

        /** */
        @Id
        public int id;

        /** */
        public String name;

        /** */
        public int bbbId;

        /** */
        @ManyToOne
        public Bbb bbb;
    }

    /** */
    @Entity
    public static class Bbb {

        /** */
        @Id
        public int id;
    }
}
