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

import org.junit.Test;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.internal.model.ConditionAttributeModelFactoryImpl;
import org.seasar.extension.jdbc.gen.model.ConditionAttributeModel;
import org.seasar.extension.jdbc.where.condition.NullableStringCondition;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class ConditionAttributeModelFactoryImplTest {

    private ConditionAttributeModelFactoryImpl factory = new ConditionAttributeModelFactoryImpl();

    @SuppressWarnings("unused")
    private String property;

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetConditionAttributeModel() throws Exception {
        PropertyMeta propertyMeta = new PropertyMeta();
        propertyMeta.setName("property");
        propertyMeta.setField(getClass().getDeclaredField("property"));
        ConditionAttributeModel model = factory
                .getConditionAttributeModel(propertyMeta);
        assertEquals("property", model.getName());
        assertEquals(String.class, model.getAttributeClass());
        assertEquals(NullableStringCondition.class, model.getConditionClass());
        assertFalse(model.isParameterized());
    }
}
