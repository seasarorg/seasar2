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

import java.lang.reflect.Field;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.TableMeta;
import org.seasar.extension.jdbc.dialect.OracleDialect;
import org.seasar.extension.jdbc.gen.desc.SequenceDesc;
import org.seasar.extension.jdbc.gen.internal.dialect.OracleGenDialect;
import org.seasar.extension.jdbc.gen.internal.provider.ValueTypeProviderImpl;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
@SequenceGenerator(name = "generator2", sequenceName = "bbb", initialValue = 10, allocationSize = 20)
public class SequenceDescFactoryImplTest {

    private PropertyMetaFactoryImpl propertyMetaFactory;

    private SequenceDescFactoryImpl sequenceDescFactoryImpl;

    @SuppressWarnings("unused")
    @Id
    @SequenceGenerator(name = "generator", sequenceName = "aaa", initialValue = 10, allocationSize = 20)
    @GeneratedValue(generator = "generator", strategy = GenerationType.SEQUENCE)
    private Integer sequenceId;

    @SuppressWarnings("unused")
    @Id
    @GeneratedValue(generator = "generator2", strategy = GenerationType.SEQUENCE)
    private Integer classAnnotatedSequenceId;

    @SuppressWarnings("unused")
    @Id
    @GeneratedValue
    private Integer autoId;

    @SuppressWarnings("unused")
    @Id
    private Integer assignId;

    @SuppressWarnings("unused")
    private Integer nonId;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        PersistenceConvention pc = new PersistenceConventionImpl();
        ColumnMetaFactoryImpl cmf = new ColumnMetaFactoryImpl();
        cmf.setPersistenceConvention(pc);
        propertyMetaFactory = new PropertyMetaFactoryImpl();
        propertyMetaFactory.setPersistenceConvention(pc);
        propertyMetaFactory.setColumnMetaFactory(cmf);
        sequenceDescFactoryImpl = new SequenceDescFactoryImpl(
                new OracleGenDialect(), new ValueTypeProviderImpl(
                        new OracleDialect()));
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetSequenceDesc_sequenceId() throws Exception {
        Field field = getClass().getDeclaredField("sequenceId");
        EntityMeta entityMeta = new EntityMeta();
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, entityMeta);
        SequenceDesc sequenceDesc = sequenceDescFactoryImpl.getSequenceDesc(
                entityMeta, propertyMeta);
        assertNotNull(sequenceDesc);
        assertEquals("aaa", sequenceDesc.getSequenceName());
        assertEquals(10, sequenceDesc.getInitialValue());
        assertEquals(20, sequenceDesc.getAllocationSize());
        assertEquals("number(19,0)", sequenceDesc.getDataType());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetSequenceDesc_classAnnotatedSequenceId() throws Exception {
        Field field = getClass().getDeclaredField("classAnnotatedSequenceId");
        EntityMeta entityMeta = new EntityMeta();
        entityMeta.setEntityClass(getClass());
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, entityMeta);
        SequenceDesc sequenceDesc = sequenceDescFactoryImpl.getSequenceDesc(
                entityMeta, propertyMeta);
        assertNotNull(sequenceDesc);
        assertEquals("bbb", sequenceDesc.getSequenceName());
        assertEquals(10, sequenceDesc.getInitialValue());
        assertEquals(20, sequenceDesc.getAllocationSize());
        assertEquals("number(19,0)", sequenceDesc.getDataType());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetSequenceDesc_autoId() throws Exception {
        Field field = getClass().getDeclaredField("autoId");
        TableMeta tableMeta = new TableMeta();
        tableMeta.setName("HOGE");
        EntityMeta entityMeta = new EntityMeta();
        entityMeta.setEntityClass(getClass());
        entityMeta.setTableMeta(tableMeta);
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, entityMeta);
        SequenceDesc sequenceDesc = sequenceDescFactoryImpl.getSequenceDesc(
                entityMeta, propertyMeta);
        assertNotNull(sequenceDesc);
        assertEquals("HOGE_AUTO_ID", sequenceDesc.getSequenceName());
        assertEquals(1, sequenceDesc.getInitialValue());
        assertEquals(50, sequenceDesc.getAllocationSize());
        assertEquals("number(19,0)", sequenceDesc.getDataType());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetSequenceDesc_assignId() throws Exception {
        Field field = getClass().getDeclaredField("assignId");
        EntityMeta entityMeta = new EntityMeta();
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, entityMeta);
        SequenceDesc sequenceDesc = sequenceDescFactoryImpl.getSequenceDesc(
                entityMeta, propertyMeta);
        assertNull(sequenceDesc);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetSequenceDesc_nonId() throws Exception {
        Field field = getClass().getDeclaredField("nonId");
        EntityMeta entityMeta = new EntityMeta();
        PropertyMeta propertyMeta = propertyMetaFactory.createPropertyMeta(
                field, entityMeta);
        SequenceDesc sequenceDesc = sequenceDescFactoryImpl.getSequenceDesc(
                entityMeta, propertyMeta);
        assertNull(sequenceDesc);
    }

}
