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

import org.junit.Test;
import org.seasar.extension.jdbc.gen.AttributeDesc;
import org.seasar.extension.jdbc.gen.AttributeModel;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class AttributeModelFactoryImplTest {

    private AttributeModelFactoryImpl factory = new AttributeModelFactoryImpl();

    /**
     * 
     */
    @Test
    public void testGetAttributeModel_length() {
        AttributeDesc attributeDesc = new AttributeDesc();
        attributeDesc.setAttributeClass(String.class);
        attributeDesc.setLength(10);
        AttributeModel model = factory.getAttributeModel(attributeDesc);
        assertTrue(model.isLengthAvailable());
        assertFalse(model.isPrecisionAvailable());
        assertFalse(model.isScaleAvailable());
        assertEquals(10, model.getLength());
    }

    /**
     * 
     */
    @Test
    public void testGetAttributeModel_precision() {
        AttributeDesc attributeDesc = new AttributeDesc();
        attributeDesc.setAttributeClass(int.class);
        attributeDesc.setPrecision(10);
        AttributeModel model = factory.getAttributeModel(attributeDesc);
        assertFalse(model.isLengthAvailable());
        assertTrue(model.isPrecisionAvailable());
        assertFalse(model.isScaleAvailable());
        assertEquals(10, model.getPrecision());
    }

    /**
     * 
     */
    @Test
    public void testGetAttributeModel_scale() {
        AttributeDesc attributeDesc = new AttributeDesc();
        attributeDesc.setAttributeClass(double.class);
        attributeDesc.setPrecision(10);
        attributeDesc.setScale(5);
        AttributeModel model = factory.getAttributeModel(attributeDesc);
        assertFalse(model.isLengthAvailable());
        assertTrue(model.isPrecisionAvailable());
        assertTrue(model.isScaleAvailable());
        assertEquals(10, model.getPrecision());
        assertEquals(5, model.getScale());
    }
}
