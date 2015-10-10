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
package org.seasar.extension.jdbc.gen.internal.util;

import javax.persistence.Column;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class AnnotationUtilTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetDefaultTable() throws Exception {
        assertNotNull(AnnotationUtil.getDefaultTable());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetDefaultColumn() throws Exception {
        Column column = AnnotationUtil.getDefaultColumn();
        assertNotNull(column);
        assertEquals(255, column.length());
        assertEquals(19, column.precision());
        assertEquals(2, column.scale());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetDefaultSequenceGenerator() throws Exception {
        assertNotNull(AnnotationUtil.getDefaultSequenceGenerator());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetDefaultTableGenerator() throws Exception {
        assertNotNull(AnnotationUtil.getDefaultTableGenerator());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetDefaultReferentialConstraint() throws Exception {
        assertNotNull(AnnotationUtil.getDefaultReferentialConstraint());
    }
}
