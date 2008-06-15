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
package org.seasar.extension.jdbc.gen.model.factory;

import java.util.Iterator;
import java.util.Set;

import javax.persistence.TemporalType;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.desc.AttributeDesc;
import org.seasar.extension.jdbc.gen.desc.EntityDesc;
import org.seasar.extension.jdbc.gen.model.EntityConditionModel;
import org.seasar.extension.jdbc.gen.model.factory.EntityConditionModelFactoryImpl;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class EntityConditionModelFactoryImplTest {

    private EntityConditionModelFactoryImpl factory = new EntityConditionModelFactoryImpl();

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetEntityConditionModel() throws Exception {
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

        EntityConditionModel model = factory.getEntityConditionModel(
                entityDesc, "aaa.bbb.HogeCondition",
                "aaa.bbb.ccc.AbstractHogeCondition");
        assertEquals("aaa.bbb", model.getPackageName());
        assertEquals("aaa.bbb.HogeCondition", model.getClassName());
        assertEquals("HogeCondition", model.getShortClassName());
        assertEquals("aaa.bbb.ccc.AbstractHogeCondition", model
                .getBaseClassName());
        assertEquals("AbstractHogeCondition", model.getShortBaseClassName());
        Set<String> set = model.getImportPackageNameSet();
        assertEquals(1, set.size());
        Iterator<String> iterator = set.iterator();
        assertEquals("aaa.bbb.ccc.AbstractHogeCondition", iterator.next());
    }
}
