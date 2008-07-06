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
import org.seasar.extension.jdbc.where.ComplexWhere;
import org.seasar.extension.jdbc.where.condition.AbstractEntityCondition;
import org.seasar.extension.jdbc.where.condition.NotNullableCondition;
import org.seasar.extension.jdbc.where.condition.NotNullableStringCondition;
import org.seasar.extension.jdbc.where.condition.NullableCondition;
import org.seasar.extension.jdbc.where.condition.NullableStringCondition;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class ConditionModelFactoryImplTest {

    private ConditionModelFactoryImpl factory = new ConditionModelFactoryImpl();

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetConditionModel() throws Exception {
        AttributeDesc id = new AttributeDesc();
        id.setName("id");
        id.setId(true);
        id.setAttributeClass(int.class);
        id.setNullable(false);

        AttributeDesc name = new AttributeDesc();
        name.setName("name");
        name.setAttributeClass(String.class);
        name.setNullable(false);

        AttributeDesc nullableName = new AttributeDesc();
        nullableName.setName("nullableName");
        nullableName.setAttributeClass(String.class);
        nullableName.setNullable(true);

        AttributeDesc date = new AttributeDesc();
        date.setName("date");
        date.setTemporalType(TemporalType.DATE);
        date.setAttributeClass(java.util.Date.class);
        date.setNullable(false);

        AttributeDesc nullableDate = new AttributeDesc();
        nullableDate.setName("nullableDate");
        nullableDate.setTemporalType(TemporalType.DATE);
        nullableDate.setAttributeClass(java.util.Date.class);
        nullableDate.setNullable(true);

        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setName("Foo");
        entityDesc.addAttribute(id);
        entityDesc.addAttribute(name);
        entityDesc.addAttribute(nullableName);
        entityDesc.addAttribute(date);
        entityDesc.addAttribute(nullableDate);

        ConditionModel model = factory.getConditionModel(entityDesc,
                "aaa.bbb.HogeCondition");
        assertEquals("aaa.bbb.HogeCondition", model.getClassName());
        assertEquals(AbstractEntityCondition.class.getName(), model
                .getBaseClassName());
        Set<String> set = model.getImportPackageNameSet();
        assertEquals(7, set.size());
        Iterator<String> iterator = set.iterator();
        assertEquals("java.util.Date", iterator.next());
        assertEquals(ComplexWhere.class.getName(), iterator.next());
        assertEquals(AbstractEntityCondition.class.getName(), iterator.next());
        assertEquals(NotNullableCondition.class.getName(), iterator.next());
        assertEquals(NotNullableStringCondition.class.getName(), iterator
                .next());
        assertEquals(NullableCondition.class.getName(), iterator.next());
        assertEquals(NullableStringCondition.class.getName(), iterator.next());
    }
}
