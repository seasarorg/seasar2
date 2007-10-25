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
package org.seasar.extension.jdbc.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.JoinMeta;
import org.seasar.extension.jdbc.JoinType;
import org.seasar.extension.jdbc.PropertyMapper;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.dialect.PostgreDialect;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.dto.AaaDto;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.entity.Bbb;
import org.seasar.extension.jdbc.entity.Ccc;
import org.seasar.extension.jdbc.entity.Ddd;
import org.seasar.extension.jdbc.exception.BaseJoinNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.EntityColumnNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.JoinDuplicatedRuntimeException;
import org.seasar.extension.jdbc.exception.NonEntityRuntimeException;
import org.seasar.extension.jdbc.exception.PropertyNotFoundRuntimeException;
import org.seasar.extension.jdbc.handler.BeanAutoResultSetHandler;
import org.seasar.extension.jdbc.handler.BeanListAutoResultSetHandler;
import org.seasar.extension.jdbc.handler.BeanListSupportLimitAutoResultSetHandler;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jdbc.mapper.AbstractEntityMapper;
import org.seasar.extension.jdbc.mapper.AbstractRelationshipEntityMapper;
import org.seasar.extension.jdbc.mapper.ManyToOneEntityMapperImpl;
import org.seasar.extension.jdbc.mapper.OneToManyEntityMapperImpl;
import org.seasar.extension.jdbc.mapper.OneToOneEntityMapperImpl;
import org.seasar.extension.jdbc.mapper.PropertyMapperImpl;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.EntityMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.TableMetaFactoryImpl;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.util.DisposableUtil;

/**
 * @author higa
 * 
 */
public class AutoSelectImplTest extends TestCase {

    private JdbcManagerImpl manager;

    @Override
    protected void setUp() throws Exception {
        manager = new JdbcManagerImpl();
        manager.setSyncRegistry(new TransactionSynchronizationRegistryImpl(
                new TransactionManagerImpl()));
        manager.setDataSource(new MockDataSource());
        manager.setDialect(new StandardDialect());

        PersistenceConventionImpl convention = new PersistenceConventionImpl();
        EntityMetaFactoryImpl emFactory = new EntityMetaFactoryImpl();
        emFactory.setPersistenceConvention(convention);
        TableMetaFactoryImpl tableMetaFactory = new TableMetaFactoryImpl();
        tableMetaFactory.setPersistenceConvention(convention);
        emFactory.setTableMetaFactory(tableMetaFactory);

        PropertyMetaFactoryImpl pFactory = new PropertyMetaFactoryImpl();
        pFactory.setPersistenceConvention(convention);
        ColumnMetaFactoryImpl cmFactory = new ColumnMetaFactoryImpl();
        cmFactory.setPersistenceConvention(convention);
        pFactory.setColumnMetaFactory(cmFactory);
        emFactory.setPropertyMetaFactory(pFactory);
        emFactory.initialize();
        manager.setEntityMetaFactory(emFactory);
    }

    @Override
    protected void tearDown() throws Exception {
        DisposableUtil.dispose();
        SqlLogRegistry regisry = SqlLogRegistryLocator.getInstance();
        regisry.clear();
        manager = null;
    }

    /**
     * 
     */
    public void testCallerClass() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        assertSame(query, query.callerClass(getClass()));
        assertEquals(getClass(), query.callerClass);
    }

    /**
     * 
     */
    public void testCallerMethodName() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        assertSame(query, query.callerMethodName("hoge"));
        assertEquals("hoge", query.callerMethodName);
    }

    /**
     * 
     */
    public void testMaxRows() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        assertSame(query, query.maxRows(100));
        assertEquals(100, query.maxRows);
    }

    /**
     * 
     */
    public void testFetchSize() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        assertSame(query, query.fetchSize(100));
        assertEquals(100, query.fetchSize);
    }

    /**
     * 
     */
    public void testQueryTimeout() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        assertSame(query, query.queryTimeout(100));
        assertEquals(100, query.queryTimeout);
    }

    /**
     * 
     */
    public void testLimit() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        assertSame(query, query.limit(100));
        assertEquals(100, query.limit);
    }

    /**
     * 
     */
    public void testOffset() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        assertSame(query, query.offset(100));
        assertEquals(100, query.offset);
    }

    /**
     * 
     */
    public void testJoin() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb");
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.LEFT_OUTER, joinMeta.getJoinType());
        assertTrue(joinMeta.isFetch());
    }

    /**
     * 
     */
    public void testJoin_joinType() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb", JoinType.INNER);
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.INNER, joinMeta.getJoinType());
        assertTrue(joinMeta.isFetch());
    }

    /**
     * 
     */
    public void testJoin_fetch() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb", false);
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.LEFT_OUTER, joinMeta.getJoinType());
        assertFalse(joinMeta.isFetch());
    }

    /**
     * 
     */
    public void testJoin_joinType_fetch() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb", JoinType.INNER, false);
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.INNER, joinMeta.getJoinType());
        assertFalse(joinMeta.isFetch());
    }

    /**
     * 
     */
    public void testCreateTableAlias() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        assertEquals("T1_", query.createTableAlias());
        assertEquals("T2_", query.createTableAlias());
    }

    /**
     * 
     */
    public void testPrepareTableAlias() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        String tableAlias = query.prepareTableAlias(null);
        assertEquals("T1_", tableAlias);
        assertSame(tableAlias, query.getTableAlias(null));
    }

    /**
     * 
     */
    public void testPrepareEntityMeta() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        EntityMeta entityMeta = query.prepareEntityMeta(Aaa.class, null);
        assertEquals("Aaa", entityMeta.getName());
        assertSame(entityMeta, query.getEntityMeta(null));
    }

    /**
     * 
     */
    public void testPrepareEntityMeta_nonEntity() {
        AutoSelectImpl<AaaDto> query = new AutoSelectImpl<AaaDto>(manager,
                AaaDto.class);
        query.prepareCallerClassAndMethodName("getResultList");
        try {
            query.prepareEntityMeta(query.baseClass, null);
            fail();
        } catch (NonEntityRuntimeException e) {
            System.out.println(e);
        }
    }

    /**
     * 
     */
    public void testPrepareEntity_selectClause() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        List<PropertyMapper> propertyMapperList = new ArrayList<PropertyMapper>(
                50);
        List<Integer> idIndexList = new ArrayList<Integer>();
        EntityMeta entityMeta = query.prepareEntityMeta(query.baseClass, null);
        String tableAlias = query.prepareTableAlias(null);
        query.prepareEntity(entityMeta, tableAlias, propertyMapperList,
                idIndexList);
        assertEquals("select T1_.ID, T1_.NAME, T1_.BBB_ID", query.selectClause
                .toSql());
    }

    /**
     * 
     */
    public void testPrepareEntity_valueTypes() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        List<PropertyMapper> propertyMapperList = new ArrayList<PropertyMapper>(
                50);
        List<Integer> idIndexList = new ArrayList<Integer>();
        EntityMeta entityMeta = query.prepareEntityMeta(query.baseClass, null);
        String tableAlias = query.prepareTableAlias(null);
        query.prepareEntity(entityMeta, tableAlias, propertyMapperList,
                idIndexList);
        ValueType[] valueTypes = query.getValueTypes();
        assertEquals(3, valueTypes.length);
        assertEquals(ValueTypes.INTEGER, valueTypes[0]);
        assertEquals(ValueTypes.STRING, valueTypes[1]);
        assertEquals(ValueTypes.INTEGER, valueTypes[2]);
    }

    /**
     * @throws Exception
     * 
     */
    public void testPrepareEntity_propertyMappers() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        List<PropertyMapper> propertyMapperList = new ArrayList<PropertyMapper>(
                50);
        List<Integer> idIndexList = new ArrayList<Integer>();
        EntityMeta entityMeta = query.prepareEntityMeta(query.baseClass, null);
        String tableAlias = query.prepareTableAlias(null);
        query.prepareEntity(entityMeta, tableAlias, propertyMapperList,
                idIndexList);
        PropertyMapperImpl[] propertyMappers = query
                .toPropertyMapperArray(propertyMapperList);
        assertEquals(3, propertyMappers.length);
        assertEquals(0, propertyMappers[0].getPropertyIndex());
        assertEquals(Aaa.class.getDeclaredField("id"), propertyMappers[0]
                .getField());
        assertEquals(1, propertyMappers[1].getPropertyIndex());
        assertEquals(Aaa.class.getDeclaredField("name"), propertyMappers[1]
                .getField());
        assertEquals(2, propertyMappers[2].getPropertyIndex());
        assertEquals(Aaa.class.getDeclaredField("bbbId"), propertyMappers[2]
                .getField());
    }

    /**
     * @throws Exception
     * 
     */
    public void testPrepareEntity_idIndices() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        List<PropertyMapper> propertyMapperList = new ArrayList<PropertyMapper>(
                50);
        List<Integer> idIndexList = new ArrayList<Integer>();
        EntityMeta entityMeta = query.prepareEntityMeta(query.baseClass, null);
        String tableAlias = query.prepareTableAlias(null);
        query.prepareEntity(entityMeta, tableAlias, propertyMapperList,
                idIndexList);
        int[] idIndices = query.toIdIndexArray(idIndexList);
        assertEquals(1, idIndices.length);
        assertEquals(0, idIndices[0]);
    }

    /**
     * @throws Exception
     * 
     */
    public void testPrepareEntity_clob() throws Exception {
        AutoSelectImpl<MyAaa> query = new AutoSelectImpl<MyAaa>(manager,
                MyAaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        List<PropertyMapper> propertyMapperList = new ArrayList<PropertyMapper>(
                50);
        List<Integer> idIndexList = new ArrayList<Integer>();
        EntityMeta entityMeta = query.prepareEntityMeta(query.baseClass, null);
        String tableAlias = query.prepareTableAlias(null);
        query.prepareEntity(entityMeta, tableAlias, propertyMapperList,
                idIndexList);
        ValueType[] valueTypes = query.getValueTypes();
        assertEquals(2, valueTypes.length);
        assertEquals(ValueTypes.INTEGER, valueTypes[0]);
        assertEquals(ValueTypes.CLOB, valueTypes[1]);
    }

    /**
     * @throws Exception
     * 
     */
    public void testPrepareTarget_entityMapper() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        AbstractEntityMapper entityMapper = query.getEntityMapper(null);
        assertNotNull(entityMapper);
        PropertyMapperImpl[] propertyMappers = (PropertyMapperImpl[]) entityMapper
                .getPropertyMappers();
        assertEquals(3, propertyMappers.length);
        assertEquals(0, propertyMappers[0].getPropertyIndex());
        assertEquals(Aaa.class.getDeclaredField("id"), propertyMappers[0]
                .getField());
        assertEquals(1, propertyMappers[1].getPropertyIndex());
        assertEquals(Aaa.class.getDeclaredField("name"), propertyMappers[1]
                .getField());
        assertEquals(2, propertyMappers[2].getPropertyIndex());
        assertEquals(Aaa.class.getDeclaredField("bbbId"), propertyMappers[2]
                .getField());
        int[] idIndices = entityMapper.getIdIndices();
        assertEquals(1, idIndices.length);
        assertEquals(0, idIndices[0]);
    }

    /**
     * @throws Exception
     * 
     */
    public void testPrepareTarget_fromClause() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        assertEquals(" from AAA T1_", query.fromClause.toSql());
    }

    /**
     * @throws Exception
     * 
     */
    public void testPrepareJoin_tableAlias() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        query.prepareJoin(new JoinMeta("bbb"));
        assertEquals("T2_", query.getTableAlias("bbb"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testSplitBaseAndProperty() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        String[] names = query.splitBaseAndProperty("bbb");
        assertEquals(2, names.length);
        assertNull(names[0]);
        assertEquals("bbb", names[1]);
    }

    /**
     * @throws Exception
     * 
     */
    public void testSplitBaseAndProperty_nest() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        String[] names = query.splitBaseAndProperty("bbb.ccc");
        assertEquals(2, names.length);
        assertEquals("bbb", names[0]);
        assertEquals("ccc", names[1]);
    }

    /**
     * @throws Exception
     * 
     */
    public void testSplitBaseAndProperty_nest2() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        String[] names = query.splitBaseAndProperty("bbb.ccc.ddd");
        assertEquals(2, names.length);
        assertEquals("bbb.ccc", names[0]);
        assertEquals("ddd", names[1]);
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetBaseEntityMeta() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        EntityMeta entityMeta = query.getBaseEntityMeta("bbb", null);
        assertEquals("Aaa", entityMeta.getName());
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetBaseEntityMeta_baseJoinNotFound() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        try {
            query.getBaseEntityMeta("bbb.ccc", "bbb");
            fail();
        } catch (BaseJoinNotFoundRuntimeException e) {
            System.out.println(e);
            assertEquals("Aaa", e.getEntityName());
            assertEquals("bbb.ccc", e.getJoin());
            assertEquals("bbb", e.getBaseJoin());
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetBaseEntityMapper() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        AbstractEntityMapper entityMapper = query.getBaseEntityMapper("bbb",
                null);
        assertEquals(Aaa.class, entityMapper.getEntityClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetBaseEntityMapper_baseJoinNotFound() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        try {
            query.getBaseEntityMapper("bbb.ccc", "bbb");
            fail();
        } catch (BaseJoinNotFoundRuntimeException e) {
            System.out.println(e);
            assertEquals("Aaa", e.getEntityName());
            assertEquals("bbb.ccc", e.getJoin());
            assertEquals("bbb", e.getBaseJoin());
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetPropertyMeta() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        EntityMeta entityMeta = query.getBaseEntityMeta("bbb", null);
        PropertyMeta propertyMeta = query.getPropertyMeta(entityMeta, "bbb",
                "bbb");
        assertEquals("bbb", propertyMeta.getName());
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetPropertyMeta_propertyMetaNotFound() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        EntityMeta entityMeta = query.getBaseEntityMeta("xxx", null);
        try {
            query.getPropertyMeta(entityMeta, "xxx", "xxx");
            fail();
        } catch (PropertyNotFoundRuntimeException e) {
            System.out.println(e);
            assertEquals("Aaa", e.getEntityName());
            assertEquals("xxx", e.getPropertyName());
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetInverseEntityMeta() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        EntityMeta entityMeta = query.getInverseEntityMeta(Bbb.class, "bbb");
        assertEquals("Bbb", entityMeta.getName());
        assertSame(entityMeta, query.getEntityMeta("bbb"));
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetInverseEntityMeta_badEntity() throws Exception {
        AutoSelectImpl<BadAaa> query = new AutoSelectImpl<BadAaa>(manager,
                BadAaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        try {
            query.getInverseEntityMeta(BadBbb.class, "bbb");
            fail();
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetInverseEntityMeta_joinDuplicated() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        query.getInverseEntityMeta(Bbb.class, "bbb");
        try {
            query.getInverseEntityMeta(Bbb.class, "bbb");
            fail();
        } catch (JoinDuplicatedRuntimeException e) {
            System.out.println(e);
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetInversePropertyMeta() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        EntityMeta baseEntityMeta = query.getBaseEntityMeta("bbb", null);
        PropertyMeta relationshipPropertyMeta = query.getPropertyMeta(
                baseEntityMeta, "bbb", "bbb");
        EntityMeta inverseEntityMeta = query.getInverseEntityMeta(Bbb.class,
                "bbb");
        PropertyMeta inversePropertyMeta = query.getInversePropertyMeta(
                inverseEntityMeta, relationshipPropertyMeta);
        assertNotNull(inversePropertyMeta);
        assertEquals("aaa", inversePropertyMeta.getName());
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetInversePropertyMeta_nonOwner() throws Exception {
        AutoSelectImpl<Bbb> query = new AutoSelectImpl<Bbb>(manager, Bbb.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        EntityMeta baseEntityMeta = query.getBaseEntityMeta("aaa", null);
        PropertyMeta relationshipPropertyMeta = query.getPropertyMeta(
                baseEntityMeta, "aaa", "aaa");
        EntityMeta inverseEntityMeta = query.getInverseEntityMeta(Aaa.class,
                "aaa");
        PropertyMeta inversePropertyMeta = query.getInversePropertyMeta(
                inverseEntityMeta, relationshipPropertyMeta);
        assertNotNull(inversePropertyMeta);
        assertEquals("bbb", inversePropertyMeta.getName());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateRelationshipEntityMapper_manyToOne() throws Exception {
        AutoSelectImpl<Ddd> query = new AutoSelectImpl<Ddd>(manager, Ddd.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        query.prepareJoin(new JoinMeta("bbb"));
        AbstractRelationshipEntityMapper mapper = (AbstractRelationshipEntityMapper) query
                .getEntityMapper("bbb");
        assertNotNull(mapper);
        assertEquals(ManyToOneEntityMapperImpl.class, mapper.getClass());
        assertEquals(Bbb.class, mapper.getEntityClass());
        assertEquals(Ddd.class.getDeclaredField("bbb"), mapper.getField());
        assertEquals(Bbb.class.getDeclaredField("ddds"), mapper
                .getInverseField());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateRelationshipEntityMapper_manyToOne_nullInverse()
            throws Exception {
        AutoSelectImpl<Bbb> query = new AutoSelectImpl<Bbb>(manager, Bbb.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        query.prepareJoin(new JoinMeta("ccc"));
        AbstractRelationshipEntityMapper mapper = (AbstractRelationshipEntityMapper) query
                .getEntityMapper("ccc");
        assertNotNull(mapper);
        assertEquals(OneToOneEntityMapperImpl.class, mapper.getClass());
        assertEquals(Ccc.class, mapper.getEntityClass());
        assertEquals(Bbb.class.getDeclaredField("ccc"), mapper.getField());
        assertNull(mapper.getInverseField());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateRelationshipEntityMapper_oneToOne() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        query.prepareJoin(new JoinMeta("bbb"));
        AbstractRelationshipEntityMapper mapper = (AbstractRelationshipEntityMapper) query
                .getEntityMapper("bbb");
        assertNotNull(mapper);
        assertEquals(OneToOneEntityMapperImpl.class, mapper.getClass());
        assertEquals(Bbb.class, mapper.getEntityClass());
        assertEquals(Aaa.class.getDeclaredField("bbb"), mapper.getField());
        assertEquals(Bbb.class.getDeclaredField("aaa"), mapper
                .getInverseField());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateRelationshipEntityMapper_oneToOne_inverse()
            throws Exception {
        AutoSelectImpl<Bbb> query = new AutoSelectImpl<Bbb>(manager, Bbb.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        query.prepareJoin(new JoinMeta("aaa"));
        AbstractRelationshipEntityMapper mapper = (AbstractRelationshipEntityMapper) query
                .getEntityMapper("aaa");
        assertNotNull(mapper);
        assertEquals(OneToOneEntityMapperImpl.class, mapper.getClass());
        assertEquals(Aaa.class, mapper.getEntityClass());
        assertEquals(Bbb.class.getDeclaredField("aaa"), mapper.getField());
        assertEquals(Aaa.class.getDeclaredField("bbb"), mapper
                .getInverseField());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateRelationshipEntityMapper_oneToMany() throws Exception {
        AutoSelectImpl<Bbb> query = new AutoSelectImpl<Bbb>(manager, Bbb.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        query.prepareJoin(new JoinMeta("ddds"));
        AbstractRelationshipEntityMapper mapper = (AbstractRelationshipEntityMapper) query
                .getEntityMapper("ddds");
        assertNotNull(mapper);
        assertEquals(OneToManyEntityMapperImpl.class, mapper.getClass());
        assertEquals(Ddd.class, mapper.getEntityClass());
        assertEquals(Bbb.class.getDeclaredField("ddds"), mapper.getField());
        assertEquals(Ddd.class.getDeclaredField("bbb"), mapper
                .getInverseField());
    }

    /**
     * @throws Exception
     * 
     */
    public void testPrepareJoin_sql() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        query.prepareJoin(new JoinMeta("bbb"));
        String expected = "select T1_.ID, T1_.NAME, T1_.BBB_ID, T2_.ID, T2_.NAME, T2_.CCC_ID from AAA T1_ left outer join BBB T2_ on T1_.BBB_ID = T2_.ID";
        assertEquals(expected, query.toSql());
    }

    /**
     * @throws Exception
     * 
     */
    public void testPrepareJoin_sql_mappedBy() throws Exception {
        AutoSelectImpl<Bbb> query = new AutoSelectImpl<Bbb>(manager, Bbb.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        query.prepareJoin(new JoinMeta("aaa"));
        String expected = "select T1_.ID, T1_.NAME, T1_.CCC_ID, T2_.ID, T2_.NAME, T2_.BBB_ID from BBB T1_ left outer join AAA T2_ on T2_.BBB_ID = T1_.ID";
        assertEquals(expected, query.toSql());
    }

    /**
     * @throws Exception
     * 
     */
    public void testPrepareJoin_sql_nest() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        query.prepareJoin(new JoinMeta("bbb"));
        query.prepareJoin(new JoinMeta("bbb.ccc"));
        String expected = "select T1_.ID, T1_.NAME, T1_.BBB_ID, T2_.ID, T2_.NAME, T2_.CCC_ID, T3_.ID, T3_.NAME from AAA T1_ left outer join BBB T2_ on T1_.BBB_ID = T2_.ID left outer join CCC T3_ on T2_.CCC_ID = T3_.ID";
        assertEquals(expected, query.toSql());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateResultListResultSetHandler_nopaging()
            throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepare("getResultList");
        ResultSetHandler handler = query.createResultListResultSetHandler();
        assertEquals(BeanListAutoResultSetHandler.class, handler.getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateResultListResultSetHandler_paging_notSupportsLimit()
            throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.limit(10);
        query.prepare("getResultList");
        ResultSetHandler handler = query.createResultListResultSetHandler();
        assertEquals(BeanListSupportLimitAutoResultSetHandler.class, handler
                .getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateResultListResultSetHandler_paging_supportsLimit()
            throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        manager.setDialect(new PostgreDialect());
        query.limit(10);
        query.prepare("getResultList");
        ResultSetHandler handler = query.createResultListResultSetHandler();
        assertEquals(BeanListAutoResultSetHandler.class, handler.getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCreateSingleResultResultSetHandler() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepare("getResultList");
        ResultSetHandler handler = query.createSingleResultResultSetHandler();
        assertEquals(BeanAutoResultSetHandler.class, handler.getClass());
    }

    /**
     * @throws Exception
     * 
     */
    public void testWhere_map() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("aaa", 1);
        assertSame(query, query.where(m));
        assertSame(m, query.conditions);
    }

    /**
     * @throws Exception
     * 
     */
    public void testWhere_criteria() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        assertSame(query, query.where("id = ?", 1));
        assertEquals("id = ?", query.criteria);
        assertEquals(1, query.getParamSize());
        assertEquals(1, query.getParam(0).value);
    }

    /**
     * @throws Exception
     * 
     */
    public void testWhere_where() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        assertSame(query, query.where(new SimpleWhere().eq("id", 1)));
        assertEquals("id = ?", query.criteria);
        assertEquals(1, query.getParamSize());
        assertEquals(1, query.getParam(0).value);
    }

    /**
     * @throws Exception
     * 
     */
    public void testWhere_emptyWhere() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        assertSame(query, query.where(new SimpleWhere().eq("name", null)));
        assertNull(query.criteria);
        assertEquals(0, query.getParamSize());
    }

    /**
     * @throws Exception
     * 
     */
    public void testAddCondition() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepare("getResultList");
        query.addCondition("id = ?", 1, Integer.class);
        assertEquals(" where id = ?", query.whereClause.toSql());
        assertEquals(1, query.paramList.size());
        assertEquals(1, query.paramList.get(0).value);
        assertEquals(Integer.class, query.paramList.get(0).paramClass);
    }

    /**
     * 
     */
    public void testAddInCondition() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepare("getResultList");
        int[] vars = new int[] { 1, 2 };
        query.addInCondition("T1_.AAA_ID in (?, ?)", vars, int.class, 2);
        assertEquals(" where T1_.AAA_ID in (?, ?)", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(2, variables.length);
        assertEquals(1, variables[0]);
        assertEquals(2, variables[1]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(2, variableClasses.length);
        assertEquals(int.class, variableClasses[0]);
        assertEquals(int.class, variableClasses[1]);
    }

    /**
     * 
     */
    public void testPrepareCondition_EQ() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("id", "1");
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T1_.ID = ?", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(1, variables.length);
        assertEquals("1", variables[0]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(1, variableClasses.length);
        assertEquals(Integer.class, variableClasses[0]);
    }

    /**
     * 
     */
    public void testPrepareCondition_EQ2() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("id_EQ", 1);
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T1_.ID = ?", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(1, variables.length);
        assertEquals(1, variables[0]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(1, variableClasses.length);
        assertEquals(Integer.class, variableClasses[0]);
    }

    /**
     * 
     */
    public void testPrepareCondition_EQ_NEST() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.id", 1);
        query.join("bbb").where(w);
        query.prepare("getResultList");
        assertEquals(" where T2_.ID = ?", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(1, variables.length);
        assertEquals(1, variables[0]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(1, variableClasses.length);
        assertEquals(Integer.class, variableClasses[0]);
    }

    /**
     * 
     */
    public void testPrepareCondition_NE() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("id_NE", 1);
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T1_.ID <> ?", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(1, variables.length);
        assertEquals(1, variables[0]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(1, variableClasses.length);
        assertEquals(Integer.class, variableClasses[0]);
    }

    /**
     * 
     */
    public void testPrepareCondition_NE_NEST() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.id_NE", 1);
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T2_.ID <> ?", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(1, variables.length);
        assertEquals(1, variables[0]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(1, variableClasses.length);
        assertEquals(Integer.class, variableClasses[0]);
    }

    /**
     * 
     */
    public void testPrepareCondition_LT() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("id_LT", 1);
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T1_.ID < ?", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(1, variables.length);
        assertEquals(1, variables[0]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(1, variableClasses.length);
        assertEquals(Integer.class, variableClasses[0]);
    }

    /**
     * 
     */
    public void testPrepareCondition_LT_NEST() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.id_LT", 1);
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T2_.ID < ?", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(1, variables.length);
        assertEquals(1, variables[0]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(1, variableClasses.length);
        assertEquals(Integer.class, variableClasses[0]);
    }

    /**
     * 
     */
    public void testPrepareCondition_LE() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("id_LE", 1);
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T1_.ID <= ?", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(1, variables.length);
        assertEquals(1, variables[0]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(1, variableClasses.length);
        assertEquals(Integer.class, variableClasses[0]);
    }

    /**
     * 
     */
    public void testPrepareCondition_LE_NEST() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.id_LE", 1);
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T2_.ID <= ?", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(1, variables.length);
        assertEquals(1, variables[0]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(1, variableClasses.length);
        assertEquals(Integer.class, variableClasses[0]);
    }

    /**
     * 
     */
    public void testPrepareCondition_GT() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("id_GT", 1);
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T1_.ID > ?", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(1, variables.length);
        assertEquals(1, variables[0]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(1, variableClasses.length);
        assertEquals(Integer.class, variableClasses[0]);
    }

    /**
     * 
     */
    public void testPrepareCondition_GT_NEST() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.id_GT", 1);
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T2_.ID > ?", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(1, variables.length);
        assertEquals(1, variables[0]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(1, variableClasses.length);
        assertEquals(Integer.class, variableClasses[0]);
    }

    /**
     * 
     */
    public void testPrepareCondition_GE() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("id_GE", 1);
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T1_.ID >= ?", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(1, variables.length);
        assertEquals(1, variables[0]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(1, variableClasses.length);
        assertEquals(Integer.class, variableClasses[0]);
    }

    /**
     * 
     */
    public void testPrepareCondition_GE_NEST() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.id_GE", 1);
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T2_.ID >= ?", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(1, variables.length);
        assertEquals(1, variables[0]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(1, variableClasses.length);
        assertEquals(Integer.class, variableClasses[0]);
    }

    /**
     * 
     */
    public void testPrepareCondition_IN() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("id_IN", new Object[] { 1, 2 });
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T1_.ID in (?, ?)", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(2, variables.length);
        assertEquals(1, variables[0]);
        assertEquals(2, variables[1]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(2, variableClasses.length);
        assertEquals(Integer.class, variableClasses[0]);
        assertEquals(Integer.class, variableClasses[1]);
    }

    /**
     * 
     */
    public void testPrepareCondition_IN_nest() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.id_IN", new Object[] { 1, 2 });
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T2_.ID in (?, ?)", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(2, variables.length);
        assertEquals(1, variables[0]);
        assertEquals(2, variables[1]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(2, variableClasses.length);
        assertEquals(Integer.class, variableClasses[0]);
        assertEquals(Integer.class, variableClasses[1]);
    }

    /**
     * 
     */
    public void testPrepareCondition_NOT_IN() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("id_NOT_IN", new Object[] { 1, 2 });
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T1_.ID not in (?, ?)", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(2, variables.length);
        assertEquals(1, variables[0]);
        assertEquals(2, variables[1]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(2, variableClasses.length);
        assertEquals(Integer.class, variableClasses[0]);
        assertEquals(Integer.class, variableClasses[1]);
    }

    /**
     * 
     */
    public void testPrepareCondition_NOT_IN_nest() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.id_NOT_IN", new Object[] { 1, 2 });
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T2_.ID not in (?, ?)", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(2, variables.length);
        assertEquals(1, variables[0]);
        assertEquals(2, variables[1]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(2, variableClasses.length);
        assertEquals(Integer.class, variableClasses[0]);
        assertEquals(Integer.class, variableClasses[1]);
    }

    /**
     * 
     */
    public void testPrepareCondition_LIKE() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("name_LIKE", "aaa");
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T1_.NAME like ?", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(1, variables.length);
        assertEquals("aaa", variables[0]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(1, variableClasses.length);
        assertEquals(String.class, variableClasses[0]);
    }

    /**
     * 
     */
    public void testPrepareCondition_LIKE_nest() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.name_LIKE", "aaa");
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T2_.NAME like ?", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(1, variables.length);
        assertEquals("aaa", variables[0]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(1, variableClasses.length);
        assertEquals(String.class, variableClasses[0]);
    }

    /**
     * 
     */
    public void testPrepareCondition_STARTS() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("name_STARTS", "aaa");
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T1_.NAME like ?", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(1, variables.length);
        assertEquals("aaa%", variables[0]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(1, variableClasses.length);
        assertEquals(String.class, variableClasses[0]);
    }

    /**
     * 
     */
    public void testPrepareCondition_STARTS_nest() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.name_STARTS", "aaa");
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T2_.NAME like ?", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(1, variables.length);
        assertEquals("aaa%", variables[0]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(1, variableClasses.length);
        assertEquals(String.class, variableClasses[0]);
    }

    /**
     * 
     */
    public void testPrepareCondition_ENDS() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("name_ENDS", "aaa");
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T1_.NAME like ?", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(1, variables.length);
        assertEquals("%aaa", variables[0]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(1, variableClasses.length);
        assertEquals(String.class, variableClasses[0]);
    }

    /**
     * 
     */
    public void testPrepareCondition_ENDS_nest() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.name_ENDS", "aaa");
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T2_.NAME like ?", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(1, variables.length);
        assertEquals("%aaa", variables[0]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(1, variableClasses.length);
        assertEquals(String.class, variableClasses[0]);
    }

    /**
     * 
     */
    public void testPrepareCondition_CONTAINS() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("name_CONTAINS", "aaa");
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T1_.NAME like ?", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(1, variables.length);
        assertEquals("%aaa%", variables[0]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(1, variableClasses.length);
        assertEquals(String.class, variableClasses[0]);
    }

    /**
     * 
     */
    public void testPrepareCondition_CONTAINS_nest() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.name_CONTAINS", "aaa");
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T2_.NAME like ?", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(1, variables.length);
        assertEquals("%aaa%", variables[0]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(1, variableClasses.length);
        assertEquals(String.class, variableClasses[0]);
    }

    /**
     * 
     */
    public void testPrepareCondition_IS_NULL() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("name_IS_NULL", true);
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T1_.NAME is null", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(0, variables.length);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(0, variableClasses.length);
    }

    /**
     * 
     */
    public void testPrepareCondition_IS_NULL_nest() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.name_IS_NULL", true);
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T2_.NAME is null", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(0, variables.length);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(0, variableClasses.length);
    }

    /**
     * 
     */
    public void testPrepareCondition_IS_NOT_NULL() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("name_IS_NOT_NULL", true);
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T1_.NAME is not null", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(0, variables.length);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(0, variableClasses.length);
    }

    /**
     * 
     */
    public void testPrepareCondition_IS_NOT_NULL_nest() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.name_IS_NOT_NULL", true);
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where T2_.NAME is not null", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(0, variables.length);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(0, variableClasses.length);
    }

    /**
     * 
     */
    public void testPrepareCondition_columnNotFound() {
        AutoSelectImpl<BadAaa> query = new AutoSelectImpl<BadAaa>(manager,
                BadAaa.class);
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("dummy", "hoge");
        query.where(w);
        try {
            query.prepare("getResultList");
            fail();
        } catch (EntityColumnNotFoundRuntimeException e) {
            System.out.println(e);
            assertEquals("BadAaa", e.getEntityName());
            assertEquals("dummy", e.getColumnName());
        }
    }

    /**
     * 
     */
    public void testPrepareSql() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepare("getResultList");
        assertNotNull(query.executedSql);
    }

    /**
     * 
     */
    public void testConvertCriteria() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb");
        query.prepare("getResultList");
        assertEquals("T1_.ID=? or T2_.ID=?", query
                .convertCriteria("id=? or bbb.id=?"));
    }

    /**
     * 
     */
    public void testPrepareOrderBy() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb").orderBy("bbb.id desc");
        query.prepare("getResultList");
        assertEquals(" order by T2_.ID desc", query.orderByClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareOrderBy_sql() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb").orderBy("bbb.id desc");
        query.prepare("getResultList");
        assertEquals(
                "select T1_.ID, T1_.NAME, T1_.BBB_ID, T2_.ID, T2_.NAME, T2_.CCC_ID from AAA T1_ left outer join BBB T2_ on T1_.BBB_ID = T2_.ID order by T2_.ID desc",
                query.toSql());
    }

    /**
     * 
     */
    public void testPrepareCriteria() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb").where("bbb.id = ?", 1);
        query.prepare("getResultList");
        assertEquals(
                "select T1_.ID, T1_.NAME, T1_.BBB_ID, T2_.ID, T2_.NAME, T2_.CCC_ID from AAA T1_ left outer join BBB T2_ on T1_.BBB_ID = T2_.ID where (T2_.ID = ?)",
                query.toSql());
    }

    @Entity
    private static class MyAaa {

        /**
         * 
         */
        public Integer id;

        /**
         * 
         */
        @Lob
        public String largeName;
    }

    @Entity(name = "BadAaa")
    private static class BadAaa {

        /**
         * 
         */
        @Id
        public Integer id;

        /**
         * 
         */
        public Integer bbbId;

        /**
         * 
         */
        @OneToOne
        public BadBbb bbb;

        /**
         * 
         */
        @Transient
        public String dummy;
    }

    @Entity(name = "BadBbb")
    private static class BadBbb {

        /**
         * 
         */
        @Id
        public Integer id;

        /**
         * 
         */
        @OneToOne
        public BadCcc ccc;
    }

    @Entity
    private static class BadCcc {
    }
}