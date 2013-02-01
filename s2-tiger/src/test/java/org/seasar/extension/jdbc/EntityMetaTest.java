/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc;

import java.util.List;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.exception.ColumnDuplicatedRuntimeException;
import org.seasar.extension.jdbc.exception.EntityColumnNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.PropertyDuplicatedRuntimeException;

/**
 * @author higa
 * 
 */
public class EntityMetaTest extends TestCase {

    /**
     * 
     */
    public void testGetIdPropertyMetaList() {
        EntityMeta em = new EntityMeta();
        PropertyMeta pm = new PropertyMeta();
        pm.setName("aaa");
        pm.setId(true);
        em.addPropertyMeta(pm);
        pm = new PropertyMeta();
        pm.setName("bbb");
        em.addPropertyMeta(pm);

        List<PropertyMeta> idMetaList = em.getIdPropertyMetaList();
        assertEquals(1, idMetaList.size());
        assertEquals("aaa", idMetaList.get(0).getName());
    }

    /**
     * 
     */
    public void testGetVersionPropertyMetaList() {
        EntityMeta em = new EntityMeta();
        PropertyMeta pm = new PropertyMeta();
        pm.setName("aaa");
        pm.setId(true);
        em.addPropertyMeta(pm);
        pm = new PropertyMeta();
        pm.setName("bbb");
        em.addPropertyMeta(pm);
        pm = new PropertyMeta();
        assertFalse(em.hasVersionPropertyMeta());
        assertNull(em.getVersionPropertyMeta());

        pm.setName("ccc");
        pm.setVersion(true);
        em.addPropertyMeta(pm);
        assertTrue(em.hasVersionPropertyMeta());
        assertEquals("ccc", em.getVersionPropertyMeta().getName());
    }

    /**
     * 
     */
    public void testGetMappedByPropertyMeta() {
        EntityMeta em = new EntityMeta();
        PropertyMeta pm = new PropertyMeta();
        pm.setName("aaa");
        pm.setMappedBy("bbb");
        pm.setRelationshipClass(Aaa.class);
        em.addPropertyMeta(pm);
        assertSame(pm, em.getMappedByPropertyMeta("bbb", Aaa.class));
        assertSame(pm, em.getMappedByPropertyMeta("BBB", Aaa.class));
        assertNull(em.getMappedByPropertyMeta("xxx", Aaa.class));
        assertNull(em.getMappedByPropertyMeta("bbb", String.class));
    }

    /**
     * 
     */
    public void testGetColumnPropertyMeta() {
        EntityMeta em = new EntityMeta();
        PropertyMeta pm = new PropertyMeta();
        pm.setName("aaaName");
        ColumnMeta columnMeta = new ColumnMeta();
        columnMeta.setName("aaa_name");
        pm.setColumnMeta(columnMeta);
        em.addPropertyMeta(pm);
        assertSame(pm, em.getColumnPropertyMeta("aaa_name"));
    }

    /**
     * 
     */
    public void testGetColumnPropertyMeta_index() {
        EntityMeta em = new EntityMeta();
        PropertyMeta pm = new PropertyMeta();
        pm.setName("aaaName");
        ColumnMeta columnMeta = new ColumnMeta();
        columnMeta.setName("aaa_name");
        pm.setColumnMeta(columnMeta);
        em.addPropertyMeta(pm);
        assertSame(pm, em.getColumnPropertyMeta(0));
    }

    /**
     * 
     */
    public void testGetColumnPropertyMeta_notFound() {
        EntityMeta em = new EntityMeta();
        em.setName("Hoge");
        PropertyMeta pm = new PropertyMeta();
        pm.setName("aaaName");
        ColumnMeta columnMeta = new ColumnMeta();
        columnMeta.setName("aaa_name");
        pm.setColumnMeta(columnMeta);
        em.addPropertyMeta(pm);
        try {
            em.getColumnPropertyMeta("xxx");
            fail();
        } catch (EntityColumnNotFoundRuntimeException e) {
            System.out.println(e);
            assertEquals("Hoge", e.getEntityName());
            assertEquals("xxx", e.getColumnName());
        }
    }

    /**
     * 
     */
    public void testHasColumnPropertyMeta() {
        EntityMeta em = new EntityMeta();
        PropertyMeta pm = new PropertyMeta();
        pm.setName("aaaName");
        ColumnMeta columnMeta = new ColumnMeta();
        columnMeta.setName("aaa_name");
        pm.setColumnMeta(columnMeta);
        em.addPropertyMeta(pm);
        assertTrue(em.hasColumnPropertyMeta("aaa_name"));
    }

    /**
     * 
     */
    public void testHasColumnPropertyMeta_caseInsensitive() {
        EntityMeta em = new EntityMeta();
        PropertyMeta pm = new PropertyMeta();
        pm.setName("aaaName");
        ColumnMeta columnMeta = new ColumnMeta();
        columnMeta.setName("AAA_NAME");
        pm.setColumnMeta(columnMeta);
        em.addPropertyMeta(pm);
        assertTrue(em.hasColumnPropertyMeta("aaa_name"));
    }

    /**
     * 
     */
    public void testGetColumnPropertyMetaSize() {
        EntityMeta em = new EntityMeta();
        PropertyMeta pm = new PropertyMeta();
        pm.setName("aaaName");
        ColumnMeta columnMeta = new ColumnMeta();
        columnMeta.setName("aaa_name");
        pm.setColumnMeta(columnMeta);
        em.addPropertyMeta(pm);
        assertEquals(1, em.getColumnPropertyMetaSize());
    }

    /**
     * 
     */
    public void testGetColumnPropertyMetaSize_notColumn() {
        EntityMeta em = new EntityMeta();
        PropertyMeta pm = new PropertyMeta();
        pm.setName("aaaName");
        em.addPropertyMeta(pm);
        assertEquals(0, em.getColumnPropertyMetaSize());
    }

    /**
     * 
     */
    public void testAddPropertyMeta_columnDuplicated() {
        EntityMeta em = new EntityMeta();
        em.setName("Hoge");
        PropertyMeta pm = new PropertyMeta();
        pm.setName("aaaName");
        ColumnMeta columnMeta = new ColumnMeta();
        columnMeta.setName("aaa_name");
        pm.setColumnMeta(columnMeta);
        em.addPropertyMeta(pm);
        try {
            PropertyMeta pm2 = new PropertyMeta();
            pm2.setName("aaaName2");
            ColumnMeta columnMeta2 = new ColumnMeta();
            columnMeta2.setName("aaa_name");
            pm2.setColumnMeta(columnMeta2);
            em.addPropertyMeta(pm2);
            fail();
        } catch (ColumnDuplicatedRuntimeException e) {
            System.out.println(e);
            assertEquals("Hoge", e.getEntityName());
            assertEquals("aaaName", e.getPropertyName());
            assertEquals("aaaName2", e.getPropertyName2());
            assertEquals("aaa_name", e.getColumnName());
        }
    }

    /**
     * 
     */
    public void testAddPropertyMeta_propertyDuplicated() {
        EntityMeta em = new EntityMeta();
        em.setName("Hoge");
        PropertyMeta pm = new PropertyMeta();
        pm.setName("aaaName");
        em.addPropertyMeta(pm);
        try {
            PropertyMeta pm2 = new PropertyMeta();
            pm2.setName("aaaname");
            em.addPropertyMeta(pm2);
            fail();
        } catch (PropertyDuplicatedRuntimeException e) {
            System.out.println(e);
            assertEquals("Hoge", e.getEntityName());
            assertEquals("aaaname", e.getPropertyName());
        }
    }
}
