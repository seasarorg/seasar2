/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.framework.convention.impl;

import junit.framework.TestCase;

/**
 * @author higa
 */
public class PersistenceConventionImplTest extends TestCase {

    private PersistenceConventionImpl convention = new PersistenceConventionImpl();

    public void testFromTableNameToEntityName() {
        assertEquals("Emp", convention.fromTableNameToEntityName("EMP"));
        assertEquals("AaaBbb", convention.fromTableNameToEntityName("AAA_BBB"));
    }

    public void testFromTableNameToEntityName_ignorePrefix() {
        convention.setIgnoreTablePrefix("T_");
        assertEquals("Emp", convention.fromTableNameToEntityName("T_EMP"));
        assertEquals("AaaBbb", convention
                .fromTableNameToEntityName("T_AAA_BBB"));
    }

    public void testFromTableNameToEntityName_noConversion() {
        convention.setNoNameConversion(true);
        assertEquals("Aaa", convention.fromTableNameToEntityName("Aaa"));
    }

    public void testFromEntityNameToTableName() {
        convention.setIgnoreTablePrefix("T_");
        assertEquals("T_EMP", convention.fromEntityNameToTableName("Emp"));
        assertEquals("T_AAA_BBB", convention
                .fromEntityNameToTableName("AaaBbb"));
    }

    public void testFromEntityNameToTableName_ignorePrefix() {
        assertEquals("EMP", convention.fromEntityNameToTableName("Emp"));
        assertEquals("AAA_BBB", convention.fromEntityNameToTableName("AaaBbb"));
    }

    public void testFromEntityNameToTableName_noConversion() {
        convention.setNoNameConversion(true);
        assertEquals("Aaa", convention.fromEntityNameToTableName("Aaa"));
    }

    public void testFromColumnNameToPropertyName() {
        assertEquals("aaa", convention.fromColumnNameToPropertyName("AAA"));
        assertEquals("aaaBbb", convention
                .fromColumnNameToPropertyName("AAA_BBB"));
    }

    public void testFromColumnNameToPropertyName_noConversion() {
        convention.setNoNameConversion(true);
        assertEquals("aaa", convention.fromColumnNameToPropertyName("aaa"));
    }

    public void testFromPropertyNameToColumnName() {
        assertEquals("AAA", convention.fromPropertyNameToColumnName("aaa"));
        assertEquals("AAA_BBB", convention
                .fromPropertyNameToColumnName("aaaBbb"));
    }

    public void testFromPropertyNameToColumnName_noConversion() {
        convention.setNoNameConversion(true);
        assertEquals("aaa", convention.fromPropertyNameToColumnName("aaa"));
    }

    public void testIsDeleted() {
        assertTrue(convention.isDeleted("Aaa", "aaaDeleted"));
        assertFalse(convention.isDeleted("Aaa", "deleted"));
    }

    public void testIsDeleted_noUseEntityName() {
        convention.setUseEntityNameForDeleted(false);
        assertFalse(convention.isDeleted("Aaa", "aaaDeleted"));
        assertTrue(convention.isDeleted("Aaa", "deleted"));
    }

    public void testIsId() {
        assertTrue(convention.isId("Aaa", "aaaId"));
        assertFalse(convention.isId("Aaa", "id"));
    }

    public void testIsId_noUseEntityName() {
        convention.setUseEntityNameForId(false);
        assertFalse(convention.isId("Aaa", "aaaId"));
        assertTrue(convention.isId("Aaa", "id"));
    }

    public void testIsInserted() {
        assertTrue(convention.isInserted("Aaa", "aaaInserted"));
        assertFalse(convention.isInserted("Aaa", "inserted"));
    }

    public void testIsInserted_noUseEntityName() {
        convention.setUseEntityNameForInserted(false);
        assertFalse(convention.isInserted("Aaa", "aaaInserted"));
        assertTrue(convention.isInserted("Aaa", "inserted"));
    }

    public void testIsUpdated() {
        assertTrue(convention.isUpdated("Aaa", "aaaUpdated"));
        assertFalse(convention.isUpdated("Aaa", "updated"));
    }

    public void testIsUpdated_noUseEntityName() {
        convention.setUseEntityNameForUpdated(false);
        assertFalse(convention.isUpdated("Aaa", "aaaUpdated"));
        assertTrue(convention.isUpdated("Aaa", "updated"));
    }

    public void testIsVersion() {
        assertTrue(convention.isVersion("Aaa", "aaaVersion"));
        assertFalse(convention.isVersion("Aaa", "version"));
    }

    public void testIsVersion_noUseEntityName() {
        convention.setUseEntityNameForVersion(false);
        assertFalse(convention.isVersion("Aaa", "aaaVersion"));
        assertTrue(convention.isVersion("Aaa", "version"));
    }
}