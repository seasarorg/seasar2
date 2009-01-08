/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.desc.AttributeDesc;
import org.seasar.extension.jdbc.gen.desc.EntityDesc;
import org.seasar.extension.jdbc.gen.model.EntityModel;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class EntityModelFactoryImplTest {

    private EntityModelFactoryImpl factory = new EntityModelFactoryImpl(
            "aaa.bbb", Superclass.class, new AttributeModelFactoryImpl(true,
                    true, true, new PersistenceConventionImpl()),
            new AssociationModelFactoryImpl(true),
            new CompositeUniqueConstraintModelFactoryImpl(), false, false,
            true, true, true);

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetEntityModel() throws Exception {
        AttributeDesc id = new AttributeDesc();
        id.setName("id");
        id.setId(true);
        id.setGenerationType(GenerationType.AUTO);
        id.setAttributeClass(int.class);

        AttributeDesc name = new AttributeDesc();
        name.setName("name");
        name.setAttributeClass(String.class);

        AttributeDesc lob = new AttributeDesc();
        lob.setName("lob");
        lob.setLob(true);
        lob.setAttributeClass(byte[].class);

        AttributeDesc date = new AttributeDesc();
        date.setName("date");
        date.setAttributeClass(java.sql.Date.class);

        AttributeDesc temp = new AttributeDesc();
        temp.setName("temp");
        temp.setTransient(true);
        temp.setAttributeClass(String.class);

        AttributeDesc version = new AttributeDesc();
        version.setName("version");
        version.setVersion(true);
        version.setAttributeClass(Integer.class);

        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("FOO");
        entityDesc.setName("Foo");
        entityDesc.addAttributeDesc(id);
        entityDesc.addAttributeDesc(name);
        entityDesc.addAttributeDesc(lob);
        entityDesc.addAttributeDesc(date);
        entityDesc.addAttributeDesc(temp);
        entityDesc.addAttributeDesc(version);

        EntityModel model = factory.getEntityModel(entityDesc);
        assertEquals("aaa.bbb", model.getPackageName());
        assertEquals("Foo", model.getShortClassName());
        assertEquals("AAA", model.getCatalogName());
        assertEquals("BBB", model.getSchemaName());
        assertEquals("FOO", model.getTableName());

        Set<String> set = model.getImportNameSet();
        assertEquals(13, set.size());
        Iterator<String> iterator = set.iterator();
        assertEquals(Date.class.getName(), iterator.next());
        assertEquals(Column.class.getName(), iterator.next());
        assertEquals(Entity.class.getName(), iterator.next());
        assertEquals(GeneratedValue.class.getName(), iterator.next());
        assertEquals(GenerationType.class.getName(), iterator.next());
        assertEquals(Id.class.getName(), iterator.next());
        assertEquals(Lob.class.getName(), iterator.next());
        assertEquals(Table.class.getName(), iterator.next());
        assertEquals(Temporal.class.getName(), iterator.next());
        assertEquals(TemporalType.class.getName(), iterator.next());
        assertEquals(Transient.class.getName(), iterator.next());
        assertEquals(Version.class.getName(), iterator.next());
        assertEquals(Superclass.class.getName(), iterator.next());
    }

    /** */
    public static class Superclass {
    }

}
