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
public class EntityConditionBaseModelFactoryImplTest {

    private EntityConditionBaseModelFactoryImpl factory = new EntityConditionBaseModelFactoryImpl();

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetEntityConditionBaseModel() throws Exception {
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
        date.setNullable(true);

        AttributeDesc temp = new AttributeDesc();
        temp.setName("temp");
        temp.setTransient(true);
        temp.setAttributeClass(String.class);
        temp.setNullable(true);

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

        ConditionBaseModel model = factory.getConditionBaseModel(entityDesc,
                "aaa.bbb.ccc.AbstractHogeCondition");
        assertEquals("aaa.bbb.ccc", model.getPackageName());
        assertEquals("aaa.bbb.ccc.AbstractHogeCondition", model.getClassName());
        assertEquals("AbstractHogeCondition", model.getShortClassName());
        Set<String> set = model.getImportPackageNameSet();
        assertEquals(7, set.size());
        Iterator<String> iterator = set.iterator();
        assertEquals("java.util.Date", iterator.next());
        assertEquals("org.seasar.extension.jdbc.where.ComplexWhere", iterator
                .next());
        assertEquals(
                "org.seasar.extension.jdbc.where.condition.AbstractEntityCondition",
                iterator.next());
        assertEquals(
                "org.seasar.extension.jdbc.where.condition.NotNullableCondition",
                iterator.next());
        assertEquals(
                "org.seasar.extension.jdbc.where.condition.NotNullableStringCondition",
                iterator.next());
        assertEquals(
                "org.seasar.extension.jdbc.where.condition.NullableCondition",
                iterator.next());
        assertEquals(
                "org.seasar.extension.jdbc.where.condition.NullableStringCondition",
                iterator.next());
    }
}
