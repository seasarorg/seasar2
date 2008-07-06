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

import java.util.Iterator;
import java.util.Set;

import javax.persistence.TemporalType;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.AttributeDesc;
import org.seasar.extension.jdbc.gen.EntityDesc;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class EntityBaseModelFactoryImplTest {

    private EntityBaseModelFactoryImpl factory = new EntityBaseModelFactoryImpl();

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetEntityBaseModel() throws Exception {
        AttributeDesc id = new AttributeDesc();
        id.setName("id");
        id.setId(true);
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
        date.setTemporalType(TemporalType.DATE);
        date.setAttributeClass(java.util.Date.class);

        AttributeDesc temp = new AttributeDesc();
        temp.setName("temp");
        temp.setTransient(true);
        temp.setAttributeClass(String.class);

        AttributeDesc version = new AttributeDesc();
        version.setName("version");
        version.setVersion(true);
        version.setAttributeClass(Integer.class);

        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setName("Foo");
        entityDesc.addAttribute(id);
        entityDesc.addAttribute(name);
        entityDesc.addAttribute(lob);
        entityDesc.addAttribute(date);
        entityDesc.addAttribute(temp);
        entityDesc.addAttribute(version);

        EntityBaseModel model = factory.getEntityBaseModel(entityDesc,
                "aaa.bbb.ccc.AbstractHoge");
        assertEquals("aaa.bbb.ccc", model.getPackageName());
        assertEquals("aaa.bbb.ccc.AbstractHoge", model.getClassName());
        assertEquals("AbstractHoge", model.getShortClassName());
        Set<String> set = model.getImportPackageNameSet();
        assertEquals(10, set.size());
        Iterator<String> iterator = set.iterator();
        assertEquals("java.util.Date", iterator.next());
        assertEquals("javax.persistence.Column", iterator.next());
        assertEquals("javax.persistence.GeneratedValue", iterator.next());
        assertEquals("javax.persistence.Id", iterator.next());
        assertEquals("javax.persistence.Lob", iterator.next());
        assertEquals("javax.persistence.MappedSuperclass", iterator.next());
        assertEquals("javax.persistence.Temporal", iterator.next());
        assertEquals("javax.persistence.TemporalType", iterator.next());
        assertEquals("javax.persistence.Transient", iterator.next());
        assertEquals("javax.persistence.Version", iterator.next());
    }
}
