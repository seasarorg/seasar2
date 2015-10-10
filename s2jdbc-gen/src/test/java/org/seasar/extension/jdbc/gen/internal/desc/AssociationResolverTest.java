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
package org.seasar.extension.jdbc.gen.internal.desc;

import java.util.Arrays;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.desc.AssociationDesc;
import org.seasar.extension.jdbc.gen.desc.AssociationType;
import org.seasar.extension.jdbc.gen.desc.AttributeDesc;
import org.seasar.extension.jdbc.gen.desc.EntityDesc;
import org.seasar.extension.jdbc.gen.desc.EntitySetDesc;
import org.seasar.extension.jdbc.gen.meta.DbForeignKeyMeta;
import org.seasar.extension.jdbc.gen.meta.DbTableMeta;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class AssociationResolverTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    public void test_OneToMany() throws Exception {
        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("CCC");
        entityDesc.setName("Ccc");

        EntityDesc entityDesc2 = new EntityDesc();
        entityDesc2.setCatalogName("DDD");
        entityDesc2.setSchemaName("EEE");
        entityDesc2.setTableName("FFF");
        entityDesc2.setName("Fff");

        EntitySetDesc entitySetDesc = new EntitySetDesc();
        entitySetDesc.addEntityDesc(entityDesc);
        entitySetDesc.addEntityDesc(entityDesc2);

        DbTableMeta tableMeta = new DbTableMeta();
        tableMeta.setCatalogName("DDD");
        tableMeta.setSchemaName("EEE");
        tableMeta.setName("FFF");

        DbForeignKeyMeta fkMeta = new DbForeignKeyMeta();
        fkMeta.setPrimaryKeyCatalogName("AAA");
        fkMeta.setPrimaryKeySchemaName("BBB");
        fkMeta.setPrimaryKeyTableName("CCC");
        fkMeta.addPrimaryKeyColumnName("GGG_ID");
        fkMeta.addPrimaryKeyColumnName("HHH_ID");
        fkMeta.addForeignKeyColumnName("GGG");
        fkMeta.addForeignKeyColumnName("HHH");

        AssociationResolver resolver = new AssociationResolver(entitySetDesc,
                new PluralFormDictinary(), new PersistenceConventionImpl());
        resolver.resolve(tableMeta, fkMeta);

        assertEquals(1, entityDesc.getAssociationDescList().size());
        AssociationDesc assoDesc = entityDesc.getAssociationDescList().get(0);
        assertSame(entityDesc2, assoDesc.getReferencedEntityDesc());
        assertEquals(AssociationType.ONE_TO_MANY, assoDesc.getAssociationType());
        assertEquals("fffList", assoDesc.getName());
        assertEquals("ccc", assoDesc.getMappedBy());
        assertTrue(assoDesc.getColumnNameList().isEmpty());
        assertNull(assoDesc.getReferencedCatalogName());
        assertNull(assoDesc.getReferencedSchemaName());
        assertNull(assoDesc.getReferencedTableName());
        assertTrue(assoDesc.getReferencedColumnNameList().isEmpty());

        assertEquals(1, entityDesc2.getAssociationDescList().size());
        assoDesc = entityDesc2.getAssociationDescList().get(0);
        assertSame(entityDesc, assoDesc.getReferencedEntityDesc());
        assertEquals(AssociationType.MANY_TO_ONE, assoDesc.getAssociationType());
        assertEquals("ccc", assoDesc.getName());
        assertNull(assoDesc.getMappedBy());
        assertEquals(Arrays.asList("GGG", "HHH"), assoDesc.getColumnNameList());
        assertEquals("AAA", assoDesc.getReferencedCatalogName());
        assertEquals("BBB", assoDesc.getReferencedSchemaName());
        assertEquals("CCC", assoDesc.getReferencedTableName());
        assertEquals(Arrays.asList("GGG_ID", "HHH_ID"), assoDesc
                .getReferencedColumnNameList());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void test_OneToOne() throws Exception {
        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("CCC");
        entityDesc.setName("Ccc");

        EntityDesc entityDesc2 = new EntityDesc();
        entityDesc2.setCatalogName("DDD");
        entityDesc2.setSchemaName("EEE");
        entityDesc2.setTableName("FFF");
        entityDesc2.setName("Fff");

        EntitySetDesc entitySetDesc = new EntitySetDesc();
        entitySetDesc.addEntityDesc(entityDesc);
        entitySetDesc.addEntityDesc(entityDesc2);

        DbTableMeta tableMeta = new DbTableMeta();
        tableMeta.setCatalogName("DDD");
        tableMeta.setSchemaName("EEE");
        tableMeta.setName("FFF");

        DbForeignKeyMeta fkMeta = new DbForeignKeyMeta();
        fkMeta.setPrimaryKeyCatalogName("AAA");
        fkMeta.setPrimaryKeySchemaName("BBB");
        fkMeta.setPrimaryKeyTableName("CCC");
        fkMeta.addPrimaryKeyColumnName("GGG_ID");
        fkMeta.addPrimaryKeyColumnName("HHH_ID");
        fkMeta.addForeignKeyColumnName("GGG");
        fkMeta.addForeignKeyColumnName("HHH");
        fkMeta.setUnique(true);

        AssociationResolver resolver = new AssociationResolver(entitySetDesc,
                new PluralFormDictinary(), new PersistenceConventionImpl());
        resolver.resolve(tableMeta, fkMeta);

        assertEquals(1, entityDesc.getAssociationDescList().size());
        AssociationDesc assoDesc = entityDesc.getAssociationDescList().get(0);
        assertSame(entityDesc2, assoDesc.getReferencedEntityDesc());
        assertEquals(AssociationType.ONE_TO_ONE, assoDesc.getAssociationType());
        assertEquals("fff", assoDesc.getName());
        assertEquals("ccc", assoDesc.getMappedBy());
        assertTrue(assoDesc.getColumnNameList().isEmpty());
        assertNull(assoDesc.getReferencedCatalogName());
        assertNull(assoDesc.getReferencedSchemaName());
        assertNull(assoDesc.getReferencedTableName());
        assertTrue(assoDesc.getReferencedColumnNameList().isEmpty());

        assertEquals(1, entityDesc2.getAssociationDescList().size());
        assoDesc = entityDesc2.getAssociationDescList().get(0);
        assertSame(entityDesc, assoDesc.getReferencedEntityDesc());
        assertEquals(AssociationType.ONE_TO_ONE, assoDesc.getAssociationType());
        assertEquals("ccc", assoDesc.getName());
        assertNull(assoDesc.getMappedBy());
        assertEquals(Arrays.asList("GGG", "HHH"), assoDesc.getColumnNameList());
        assertEquals("AAA", assoDesc.getReferencedCatalogName());
        assertEquals("BBB", assoDesc.getReferencedSchemaName());
        assertEquals("CCC", assoDesc.getReferencedTableName());
        assertEquals(Arrays.asList("GGG_ID", "HHH_ID"), assoDesc
                .getReferencedColumnNameList());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testMultiAssociations() throws Exception {
        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("CCC");
        entityDesc.setName("Ccc");

        EntityDesc entityDesc2 = new EntityDesc();
        entityDesc2.setCatalogName("DDD");
        entityDesc2.setSchemaName("EEE");
        entityDesc2.setTableName("FFF");
        entityDesc2.setName("Fff");

        EntitySetDesc entitySetDesc = new EntitySetDesc();
        entitySetDesc.addEntityDesc(entityDesc);
        entitySetDesc.addEntityDesc(entityDesc2);

        DbTableMeta tableMeta = new DbTableMeta();
        tableMeta.setCatalogName("DDD");
        tableMeta.setSchemaName("EEE");
        tableMeta.setName("FFF");

        DbForeignKeyMeta fkMeta = new DbForeignKeyMeta();
        fkMeta.setPrimaryKeyCatalogName("AAA");
        fkMeta.setPrimaryKeySchemaName("BBB");
        fkMeta.setPrimaryKeyTableName("CCC");
        fkMeta.addPrimaryKeyColumnName("GGG_ID");
        fkMeta.addPrimaryKeyColumnName("HHH_ID");
        fkMeta.addForeignKeyColumnName("GGG");
        fkMeta.addForeignKeyColumnName("HHH");

        DbForeignKeyMeta fkMeta2 = new DbForeignKeyMeta();
        fkMeta2.setPrimaryKeyCatalogName("AAA");
        fkMeta2.setPrimaryKeySchemaName("BBB");
        fkMeta2.setPrimaryKeyTableName("CCC");
        fkMeta2.addPrimaryKeyColumnName("GGG_ID");
        fkMeta2.addPrimaryKeyColumnName("HHH_ID");
        fkMeta2.addForeignKeyColumnName("III");
        fkMeta2.addForeignKeyColumnName("JJJ");

        AssociationResolver resolver = new AssociationResolver(entitySetDesc,
                new PluralFormDictinary(), new PersistenceConventionImpl());
        resolver.resolve(tableMeta, fkMeta);
        resolver.resolve(tableMeta, fkMeta2);

        assertEquals(2, entityDesc.getAssociationDescList().size());
        AssociationDesc assoDesc = entityDesc.getAssociationDescList().get(0);
        assertEquals(AssociationType.ONE_TO_MANY, assoDesc.getAssociationType());
        assertEquals("fffList", assoDesc.getName());
        assoDesc = entityDesc.getAssociationDescList().get(1);
        assertEquals(AssociationType.ONE_TO_MANY, assoDesc.getAssociationType());
        assertEquals("fffList2", assoDesc.getName());

        assertEquals(2, entityDesc2.getAssociationDescList().size());
        assoDesc = entityDesc2.getAssociationDescList().get(0);
        assertEquals("ccc", assoDesc.getName());
        assoDesc = entityDesc2.getAssociationDescList().get(1);
        assertEquals("ccc2", assoDesc.getName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testAssociationName_fkColumnName_endsWith_pkColumnName()
            throws Exception {
        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("CCC");
        entityDesc.setName("Ccc");

        EntityDesc entityDesc2 = new EntityDesc();
        entityDesc2.setCatalogName("DDD");
        entityDesc2.setSchemaName("EEE");
        entityDesc2.setTableName("FFF");
        entityDesc2.setName("Fff");

        AttributeDesc attributeDesc = new AttributeDesc();
        attributeDesc.setName("gggId");
        entityDesc2.addAttributeDesc(attributeDesc);

        EntitySetDesc entitySetDesc = new EntitySetDesc();
        entitySetDesc.addEntityDesc(entityDesc);
        entitySetDesc.addEntityDesc(entityDesc2);

        DbTableMeta tableMeta = new DbTableMeta();
        tableMeta.setCatalogName("DDD");
        tableMeta.setSchemaName("EEE");
        tableMeta.setName("FFF");

        DbForeignKeyMeta fkMeta = new DbForeignKeyMeta();
        fkMeta.setPrimaryKeyCatalogName("AAA");
        fkMeta.setPrimaryKeySchemaName("BBB");
        fkMeta.setPrimaryKeyTableName("CCC");
        fkMeta.addPrimaryKeyColumnName("ID");
        fkMeta.addForeignKeyColumnName("GGG_ID");

        AssociationResolver resolver = new AssociationResolver(entitySetDesc,
                new PluralFormDictinary(), new PersistenceConventionImpl());
        resolver.resolve(tableMeta, fkMeta);

        assertEquals(1, entityDesc.getAssociationDescList().size());
        AssociationDesc assoDesc = entityDesc.getAssociationDescList().get(0);
        assertEquals(AssociationType.ONE_TO_MANY, assoDesc.getAssociationType());
        assertEquals("fffList", assoDesc.getName());

        assertEquals(1, entityDesc2.getAssociationDescList().size());
        assoDesc = entityDesc2.getAssociationDescList().get(0);
        assertEquals("ggg", assoDesc.getName());
        assertEquals("gggId", attributeDesc.getName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testAssociationName_fkColumnName_endsWith_pkColumnName_and_pkColumnName_startsWith_pkTableName()
            throws Exception {
        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("CCC");
        entityDesc.setName("Ccc");

        EntityDesc entityDesc2 = new EntityDesc();
        entityDesc2.setCatalogName("DDD");
        entityDesc2.setSchemaName("EEE");
        entityDesc2.setTableName("FFF");
        entityDesc2.setName("Fff");

        AttributeDesc attributeDesc = new AttributeDesc();
        attributeDesc.setName("gggCccId");
        entityDesc2.addAttributeDesc(attributeDesc);

        EntitySetDesc entitySetDesc = new EntitySetDesc();
        entitySetDesc.addEntityDesc(entityDesc);
        entitySetDesc.addEntityDesc(entityDesc2);

        DbTableMeta tableMeta = new DbTableMeta();
        tableMeta.setCatalogName("DDD");
        tableMeta.setSchemaName("EEE");
        tableMeta.setName("FFF");

        DbForeignKeyMeta fkMeta = new DbForeignKeyMeta();
        fkMeta.setPrimaryKeyCatalogName("AAA");
        fkMeta.setPrimaryKeySchemaName("BBB");
        fkMeta.setPrimaryKeyTableName("CCC");
        fkMeta.addPrimaryKeyColumnName("CCC_ID");
        fkMeta.addForeignKeyColumnName("GGG_CCC_ID");

        AssociationResolver resolver = new AssociationResolver(entitySetDesc,
                new PluralFormDictinary(), new PersistenceConventionImpl());
        resolver.resolve(tableMeta, fkMeta);

        assertEquals(1, entityDesc.getAssociationDescList().size());
        AssociationDesc assoDesc = entityDesc.getAssociationDescList().get(0);
        assertEquals(AssociationType.ONE_TO_MANY, assoDesc.getAssociationType());
        assertEquals("fffList", assoDesc.getName());

        assertEquals(1, entityDesc2.getAssociationDescList().size());
        assoDesc = entityDesc2.getAssociationDescList().get(0);
        assertEquals("gggCcc", assoDesc.getName());
        assertEquals("gggCccId", attributeDesc.getName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testAssociationName_fkColumnName_equals_pkColumnName()
            throws Exception {
        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("CCC");
        entityDesc.setName("Ccc");

        EntityDesc entityDesc2 = new EntityDesc();
        entityDesc2.setCatalogName("DDD");
        entityDesc2.setSchemaName("EEE");
        entityDesc2.setTableName("FFF");
        entityDesc2.setName("Fff");

        AttributeDesc attributeDesc = new AttributeDesc();
        attributeDesc.setName("id");
        entityDesc2.addAttributeDesc(attributeDesc);

        EntitySetDesc entitySetDesc = new EntitySetDesc();
        entitySetDesc.addEntityDesc(entityDesc);
        entitySetDesc.addEntityDesc(entityDesc2);

        DbTableMeta tableMeta = new DbTableMeta();
        tableMeta.setCatalogName("DDD");
        tableMeta.setSchemaName("EEE");
        tableMeta.setName("FFF");

        DbForeignKeyMeta fkMeta = new DbForeignKeyMeta();
        fkMeta.setPrimaryKeyCatalogName("AAA");
        fkMeta.setPrimaryKeySchemaName("BBB");
        fkMeta.setPrimaryKeyTableName("CCC");
        fkMeta.addPrimaryKeyColumnName("ID");
        fkMeta.addForeignKeyColumnName("ID");

        AssociationResolver resolver = new AssociationResolver(entitySetDesc,
                new PluralFormDictinary(), new PersistenceConventionImpl());
        resolver.resolve(tableMeta, fkMeta);

        assertEquals(1, entityDesc.getAssociationDescList().size());
        AssociationDesc assoDesc = entityDesc.getAssociationDescList().get(0);
        assertEquals(AssociationType.ONE_TO_MANY, assoDesc.getAssociationType());
        assertEquals("fffList", assoDesc.getName());

        assertEquals(1, entityDesc2.getAssociationDescList().size());
        assoDesc = entityDesc2.getAssociationDescList().get(0);
        assertEquals("ccc", assoDesc.getName());
        assertEquals("id", attributeDesc.getName());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testAssociationName_fkColumnName_equals_pkTableName()
            throws Exception {
        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("CCC");
        entityDesc.setName("Ccc");

        EntityDesc entityDesc2 = new EntityDesc();
        entityDesc2.setCatalogName("DDD");
        entityDesc2.setSchemaName("EEE");
        entityDesc2.setTableName("FFF");
        entityDesc2.setName("Fff");

        AttributeDesc attributeDesc = new AttributeDesc();
        attributeDesc.setName("ccc");
        entityDesc2.addAttributeDesc(attributeDesc);

        EntitySetDesc entitySetDesc = new EntitySetDesc();
        entitySetDesc.addEntityDesc(entityDesc);
        entitySetDesc.addEntityDesc(entityDesc2);

        DbTableMeta tableMeta = new DbTableMeta();
        tableMeta.setCatalogName("DDD");
        tableMeta.setSchemaName("EEE");
        tableMeta.setName("FFF");

        DbForeignKeyMeta fkMeta = new DbForeignKeyMeta();
        fkMeta.setPrimaryKeyCatalogName("AAA");
        fkMeta.setPrimaryKeySchemaName("BBB");
        fkMeta.setPrimaryKeyTableName("CCC");
        fkMeta.addPrimaryKeyColumnName("ID");
        fkMeta.addForeignKeyColumnName("CCC");

        AssociationResolver resolver = new AssociationResolver(entitySetDesc,
                new PluralFormDictinary(), new PersistenceConventionImpl());
        resolver.resolve(tableMeta, fkMeta);

        assertEquals(1, entityDesc.getAssociationDescList().size());
        AssociationDesc assoDesc = entityDesc.getAssociationDescList().get(0);
        assertEquals(AssociationType.ONE_TO_MANY, assoDesc.getAssociationType());
        assertEquals("fffList", assoDesc.getName());

        assertEquals(1, entityDesc2.getAssociationDescList().size());
        assoDesc = entityDesc2.getAssociationDescList().get(0);
        assertEquals("ccc", assoDesc.getName());
        assertEquals("cccId", attributeDesc.getName());
    }

}
