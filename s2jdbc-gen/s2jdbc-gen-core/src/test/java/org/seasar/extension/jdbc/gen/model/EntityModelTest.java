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

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class EntityModelTest {

    @Test
    public void testGetPackageName() throws Exception {
        EntityModel model = new EntityModel();
        String name = model.getPackageName(String.class.getName());
        assertEquals("java.lang", name);
    }

    @Test
    public void testGetShortClassName() throws Exception {
        EntityModel model = new EntityModel();
        String name = model.getShortClassName(String.class.getName());
        assertEquals("String", name);
    }

    public void testIsLengthAvailable() throws Exception {
        EntityModel model = new EntityModel();
        AttributeDesc attributeDesc = new AttributeDesc();
        attributeDesc.setAttributeClass(String.class);
        attributeDesc.setLength(10);
        assertTrue(model.isLengthAvailable(attributeDesc));
    }

    public void testIsLengthAvailable_zeroLength() throws Exception {
        EntityModel model = new EntityModel();
        AttributeDesc attributeDesc = new AttributeDesc();
        attributeDesc.setAttributeClass(String.class);
        attributeDesc.setLength(0);
        assertFalse(model.isLengthAvailable(attributeDesc));
    }

    public void testIsLengthAvailable_numberType() throws Exception {
        EntityModel model = new EntityModel();
        AttributeDesc attributeDesc = new AttributeDesc();
        attributeDesc.setAttributeClass(int.class);
        attributeDesc.setLength(10);
        assertFalse(model.isLengthAvailable(attributeDesc));
    }

    public void testIsPrecisionAvailable() throws Exception {
        EntityModel model = new EntityModel();
        AttributeDesc attributeDesc = new AttributeDesc();
        attributeDesc.setAttributeClass(int.class);
        attributeDesc.setPrecision(10);
        assertFalse(model.isLengthAvailable(attributeDesc));
    }

    public void testIsPrecisionAvailable_zeroPrecision() throws Exception {
        EntityModel model = new EntityModel();
        AttributeDesc attributeDesc = new AttributeDesc();
        attributeDesc.setAttributeClass(int.class);
        attributeDesc.setPrecision(0);
        assertFalse(model.isLengthAvailable(attributeDesc));
    }

    public void testIsPrecisionAvailable_notNumberType() throws Exception {
        EntityModel model = new EntityModel();
        AttributeDesc attributeDesc = new AttributeDesc();
        attributeDesc.setAttributeClass(String.class);
        attributeDesc.setPrecision(10);
        assertFalse(model.isLengthAvailable(attributeDesc));
    }

}
