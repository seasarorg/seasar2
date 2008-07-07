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
package org.seasar.extension.jdbc.gen;

import javax.persistence.Column;

import org.junit.Test;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.ConditionModel;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class ConditionModelTest {

    @Column(nullable = false)
    private int property;

    @Test
    public void testGetPackageName() throws Exception {
        ConditionModel model = new ConditionModel();
        String name = model.getPackageName(String.class.getName());
        assertEquals("java.lang", name);
    }

    @Test
    public void testGetShortClassName() throws Exception {
        ConditionModel model = new ConditionModel();
        String name = model.getShortClassName(String.class.getName());
        assertEquals("String", name);
    }

    @Test
    public void testGetWrapperShortClassName() throws Exception {
        ConditionModel model = new ConditionModel();
        PropertyMeta propertyMeta = new PropertyMeta();
        propertyMeta.setField(getClass().getDeclaredField("property"));
        String name = model.getWrapperShortClassName(propertyMeta);
        assertEquals("Integer", name);
    }

    @Test
    public void testIsNullable() throws Exception {
        ConditionModel model = new ConditionModel();
        PropertyMeta propertyMeta = new PropertyMeta();
        propertyMeta.setField(getClass().getDeclaredField("property"));
        assertFalse(model.isNullable(propertyMeta));
    }

}
