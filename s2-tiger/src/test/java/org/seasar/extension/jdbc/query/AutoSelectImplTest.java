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
package org.seasar.extension.jdbc.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.JoinMeta;
import org.seasar.extension.jdbc.JoinType;
import org.seasar.extension.jdbc.OrderByItem;
import org.seasar.extension.jdbc.PropertyMapper;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.OrderByItem.OrderingSpec;
import org.seasar.extension.jdbc.dialect.Db2Dialect;
import org.seasar.extension.jdbc.dialect.HsqlDialect;
import org.seasar.extension.jdbc.dialect.MssqlDialect;
import org.seasar.extension.jdbc.dialect.OracleDialect;
import org.seasar.extension.jdbc.dialect.PostgreDialect;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.dto.AaaDto;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.entity.Bbb;
import org.seasar.extension.jdbc.entity.Ccc;
import org.seasar.extension.jdbc.entity.Ddd;
import org.seasar.extension.jdbc.entity.Eee;
import org.seasar.extension.jdbc.entity.Emp;
import org.seasar.extension.jdbc.exception.BaseJoinNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.EntityColumnNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.IllegalIdPropertySizeRuntimeException;
import org.seasar.extension.jdbc.exception.JoinDuplicatedRuntimeException;
import org.seasar.extension.jdbc.exception.NonEntityRuntimeException;
import org.seasar.extension.jdbc.exception.PropertyNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.QueryTwiceExecutionRuntimeException;
import org.seasar.extension.jdbc.exception.VersionPropertyNotExistsRuntimeException;
import org.seasar.extension.jdbc.handler.BeanAutoResultSetHandler;
import org.seasar.extension.jdbc.handler.BeanIterationAutoResultSetHandler;
import org.seasar.extension.jdbc.handler.BeanListAutoResultSetHandler;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
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
import org.seasar.extension.jdbc.parameter.Parameter;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.util.DisposableUtil;

import static org.seasar.extension.jdbc.parameter.Parameter.*;

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
    public void testIncludes() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.includes("name", "bbb");
        assertEquals(2, query.includesProperties.size());
        assertTrue(query.includesProperties.contains("name"));
        assertTrue(query.includesProperties.contains("bbb"));
    }

    /**
     * 
     */
    public void testExcludes() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.excludes("name", "bbb");
        assertEquals(2, query.excludesProperties.size());
        assertTrue(query.excludesProperties.contains("name"));
        assertTrue(query.excludesProperties.contains("bbb"));
    }

    /**
     * 
     */
    public void testIsTargetProperty_includes() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.includes("name");
        query.includes("lazyName"); // eager()を指定しなくても対象

        EntityMeta aaaMeta = query.prepareEntityMeta(Aaa.class, null);
        assertTrue(query.isTargetProperty(aaaMeta.getPropertyMeta("id"), null));
        assertTrue(query.isTargetProperty(aaaMeta.getPropertyMeta("name"), null));
        assertTrue(query.isTargetProperty(aaaMeta.getPropertyMeta("lazyName"), null));
        assertFalse(query.isTargetProperty(aaaMeta.getPropertyMeta("dto"), null));
        assertFalse(query.isTargetProperty(aaaMeta.getPropertyMeta("bbbId"), null));
    }

    /**
     * 
     */
    public void testIsTargetProperty_includes_eager() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.includes("name");
        query.eager("lazyName"); // includes()に含まれていないので対象外

        EntityMeta aaaMeta = query.prepareEntityMeta(Aaa.class, null);
        assertTrue(query.isTargetProperty(aaaMeta.getPropertyMeta("id"), null));
        assertTrue(query.isTargetProperty(aaaMeta.getPropertyMeta("name"), null));
        assertFalse(query.isTargetProperty(aaaMeta.getPropertyMeta("lazyName"), null));
        assertFalse(query.isTargetProperty(aaaMeta.getPropertyMeta("dto"), null));
        assertFalse(query.isTargetProperty(aaaMeta.getPropertyMeta("bbbId"), null));
    }

    /**
     * 
     */
    public void testIsTargetProperty_excludes() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.excludes("name");
        query.eager("lazyName"); // 対象

        EntityMeta aaaMeta = query.prepareEntityMeta(Aaa.class, null);
        assertTrue(query.isTargetProperty(aaaMeta.getPropertyMeta("id"), null));
        assertFalse(query.isTargetProperty(aaaMeta.getPropertyMeta("name"), null));
        assertTrue(query.isTargetProperty(aaaMeta.getPropertyMeta("lazyName"), null));
        assertTrue(query.isTargetProperty(aaaMeta.getPropertyMeta("dto"), null));
        assertTrue(query.isTargetProperty(aaaMeta.getPropertyMeta("bbbId"), null));
    }

    /**
     * 
     */
    public void testIsTargetProperty_join() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.leftOuterJoin("bbb");
        query.leftOuterJoin("bbb.ccc");
        query.includes("name");
        query.excludes("name"); // includes()に含まれてるので対象
        query.excludes("dto"); // 対象外
        query.includes("lazyName"); // eager()しなくても対象
        query.excludes("bbb");
        query.includes("bbb.ccc"); // bbbは対象外だが bbb.cccは対象
        query.eager("bbb.lazyName"); // bbbがexcludes()なのでeager()しても対象外

        EntityMeta aaaMeta = query.prepareEntityMeta(Aaa.class, null);
        assertTrue(query.isTargetProperty(aaaMeta.getPropertyMeta("id"), null));
        assertTrue(query.isTargetProperty(aaaMeta.getPropertyMeta("name"), null));
        assertTrue(query.isTargetProperty(aaaMeta.getPropertyMeta("lazyName"), null));
        assertFalse(query.isTargetProperty(aaaMeta.getPropertyMeta("dto"), null));
        assertFalse(query.isTargetProperty(aaaMeta.getPropertyMeta("bbbId"), null));

        EntityMeta bbbMeta = query.prepareEntityMeta(Bbb.class, null);
        JoinMeta bbbJoinMeta = query.getJoinMeta(0);
        assertTrue(query.isTargetProperty(bbbMeta.getPropertyMeta("id"), bbbJoinMeta));
        assertFalse(query.isTargetProperty(bbbMeta.getPropertyMeta("name"), bbbJoinMeta));
        assertFalse(query.isTargetProperty(bbbMeta.getPropertyMeta("lazyName"), bbbJoinMeta));
        assertFalse(query.isTargetProperty(bbbMeta.getPropertyMeta("cccId"), bbbJoinMeta));

        EntityMeta cccMeta = query.prepareEntityMeta(Ccc.class, null);
        JoinMeta cccJoinMeta = query.getJoinMeta(1);
        assertTrue(query.isTargetProperty(cccMeta.getPropertyMeta("id"), cccJoinMeta));
        assertTrue(query.isTargetProperty(cccMeta.getPropertyMeta("name"), cccJoinMeta));
    }

    /**
     * 
     */
    public void testInnerJoin() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.innerJoin("bbb");
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.INNER, joinMeta.getJoinType());
        assertTrue(joinMeta.isFetch());
    }

    /**
     * 
     */
    public void testInnerJoin_condition() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.innerJoin("bbb", "bbb.id < 100");
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.INNER, joinMeta.getJoinType());
        assertTrue(joinMeta.isFetch());
        assertEquals("bbb.id < 100", joinMeta.getCondition());
    }

    /**
     * 
     */
    public void testInnerJoin_where() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.innerJoin("bbb", new SimpleWhere().isNull("bbb.ccc", true));
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.INNER, joinMeta.getJoinType());
        assertTrue(joinMeta.isFetch());
        assertEquals("bbb.ccc is null", joinMeta.getCondition());
    }

    /**
     * 
     */
    public void testInnerJoin_wheres() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.innerJoin("bbb", new SimpleWhere().isNull("bbb.ccc", true),
                new SimpleWhere().eq("bbb.id", 100));
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.INNER, joinMeta.getJoinType());
        assertTrue(joinMeta.isFetch());
        assertEquals("(bbb.ccc is null) and (bbb.id = ?)", joinMeta.getCondition());
    }

    /**
     * 
     */
    public void testInnerJoin_fetch() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.innerJoin("bbb", false);
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.INNER, joinMeta.getJoinType());
        assertFalse(joinMeta.isFetch());
    }

    /**
     * 
     */
    public void testInnerJoin_fetch_condition() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.innerJoin("bbb", false, "bbb.id < 100");
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.INNER, joinMeta.getJoinType());
        assertFalse(joinMeta.isFetch());
        assertEquals("bbb.id < 100", joinMeta.getCondition());
    }

    /**
     * 
     */
    public void testInnerJoin_fetch_where() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query
                .innerJoin("bbb", false, new SimpleWhere().isNull("bbb.ccc",
                        true));
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.INNER, joinMeta.getJoinType());
        assertFalse(joinMeta.isFetch());
        assertEquals("bbb.ccc is null", joinMeta.getCondition());
    }

    /**
     * 
     */
    public void testInnerJoin_fetch_wheres() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query
                .innerJoin("bbb", false, new SimpleWhere().isNull("bbb.ccc",
                        true), new SimpleWhere().eq("bbb.id", 100));
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.INNER, joinMeta.getJoinType());
        assertFalse(joinMeta.isFetch());
        assertEquals("(bbb.ccc is null) and (bbb.id = ?)", joinMeta.getCondition());
    }

    /**
     * 
     */
    public void testLeftOuterJoin() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.leftOuterJoin("bbb");
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.LEFT_OUTER, joinMeta.getJoinType());
        assertTrue(joinMeta.isFetch());
    }

    /**
     * 
     */
    public void testLeftOuterJoin_condition() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.leftOuterJoin("bbb", "bbb.id < 100");
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.LEFT_OUTER, joinMeta.getJoinType());
        assertTrue(joinMeta.isFetch());
        assertEquals("bbb.id < 100", joinMeta.getCondition());
    }

    /**
     * 
     */
    public void testLeftOuterJoin_where() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.leftOuterJoin("bbb", new SimpleWhere().isNull("bbb.ccc", true));
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.LEFT_OUTER, joinMeta.getJoinType());
        assertTrue(joinMeta.isFetch());
        assertEquals("bbb.ccc is null", joinMeta.getCondition());
    }

    /**
     * 
     */
    public void testLeftOuterJoin_wheres() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.leftOuterJoin("bbb", new SimpleWhere().isNull("bbb.ccc", true), new SimpleWhere().eq("bbb.id", 100));
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.LEFT_OUTER, joinMeta.getJoinType());
        assertTrue(joinMeta.isFetch());
        assertEquals("(bbb.ccc is null) and (bbb.id = ?)", joinMeta.getCondition());
    }

    /**
     * 
     */
    public void testLeftOuterJoin_fetch() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.leftOuterJoin("bbb", false);
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.LEFT_OUTER, joinMeta.getJoinType());
        assertFalse(joinMeta.isFetch());
    }

    /**
     * 
     */
    public void testLeftOuterJoin_fetch_condition() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.leftOuterJoin("bbb", false, new SimpleWhere().isNull("bbb.ccc",
                true));
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.LEFT_OUTER, joinMeta.getJoinType());
        assertFalse(joinMeta.isFetch());
        assertEquals("bbb.ccc is null", joinMeta.getCondition());
    }

    /**
     * 
     */
    public void testLeftOuterJoin_fetch_where() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.leftOuterJoin("bbb", false, new SimpleWhere().isNull("bbb.ccc",
                true));
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.LEFT_OUTER, joinMeta.getJoinType());
        assertFalse(joinMeta.isFetch());
        assertEquals("bbb.ccc is null", joinMeta.getCondition());
    }

    /**
     * 
     */
    public void testLeftOuterJoin_fetch_wheres() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.leftOuterJoin("bbb", false, new SimpleWhere().isNull("bbb.ccc",
                true), new SimpleWhere().eq("bbb.id", 100));
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.LEFT_OUTER, joinMeta.getJoinType());
        assertFalse(joinMeta.isFetch());
        assertEquals("(bbb.ccc is null) and (bbb.id = ?)", joinMeta.getCondition());
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
    public void testJoin_joinType_condition() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb", JoinType.INNER, "bbb.id < 100");
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.INNER, joinMeta.getJoinType());
        assertTrue(joinMeta.isFetch());
        assertEquals("bbb.id < 100", joinMeta.getCondition());
    }

    /**
     * 
     */
    public void testJoin_joinType_where() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb", JoinType.INNER, new SimpleWhere().isNull("bbb.ccc",
                true));
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.INNER, joinMeta.getJoinType());
        assertTrue(joinMeta.isFetch());
        assertEquals("bbb.ccc is null", joinMeta.getCondition());
    }

    /**
     * 
     */
    public void testJoin_joinType_wheres() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb", JoinType.INNER, new SimpleWhere().isNull("bbb.ccc",
                true), new SimpleWhere().eq("bbb.id", 100));
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.INNER, joinMeta.getJoinType());
        assertTrue(joinMeta.isFetch());
        assertEquals("(bbb.ccc is null) and (bbb.id = ?)", joinMeta.getCondition());
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
    public void testJoin_joinType_fetch_condition() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb", JoinType.INNER, false, "bbb.id < 100");
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.INNER, joinMeta.getJoinType());
        assertFalse(joinMeta.isFetch());
        assertEquals("bbb.id < 100", joinMeta.getCondition());
    }

    /**
     * 
     */
    public void testJoin_joinType_fetch_where() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb", JoinType.INNER, false, new SimpleWhere().isNull(
                "bbb.ccc", true));
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.INNER, joinMeta.getJoinType());
        assertFalse(joinMeta.isFetch());
        assertEquals("bbb.ccc is null", joinMeta.getCondition());
    }

    /**
     * 
     */
    public void testJoin_joinType_fetch_wheres() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb", JoinType.INNER, false, new SimpleWhere().isNull(
                "bbb.ccc", true), new SimpleWhere().eq("bbb.id", 100));
        assertEquals(1, query.getJoinMetaSize());
        JoinMeta joinMeta = query.getJoinMeta(0);
        assertEquals("bbb", joinMeta.getName());
        assertEquals(JoinType.INNER, joinMeta.getJoinType());
        assertFalse(joinMeta.isFetch());
        assertEquals("(bbb.ccc is null) and (bbb.id = ?)", joinMeta.getCondition());
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
        query.prepareEntity(entityMeta, null, tableAlias, propertyMapperList,
                idIndexList);
        assertEquals(
                "T1_.ID as C1_, T1_.NAME as C2_, T1_.BBB_ID as C3_, T1_.DTO as C4_",
                query.selectClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareEntity_selectClause_includes() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.includes("name", "bbbId");
        query.prepareCallerClassAndMethodName("getResultList");
        List<PropertyMapper> propertyMapperList = new ArrayList<PropertyMapper>(
                50);
        List<Integer> idIndexList = new ArrayList<Integer>();
        EntityMeta entityMeta = query.prepareEntityMeta(query.baseClass, null);
        String tableAlias = query.prepareTableAlias(null);
        query.prepareEntity(entityMeta, null, tableAlias, propertyMapperList,
                idIndexList);
        assertEquals("T1_.ID as C1_, T1_.NAME as C2_, T1_.BBB_ID as C3_",
                query.selectClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareEntity_selectClause_excludes() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.excludes("name", "bbbId");
        query.prepareCallerClassAndMethodName("getResultList");
        List<PropertyMapper> propertyMapperList = new ArrayList<PropertyMapper>(
                50);
        List<Integer> idIndexList = new ArrayList<Integer>();
        EntityMeta entityMeta = query.prepareEntityMeta(query.baseClass, null);
        String tableAlias = query.prepareTableAlias(null);
        query.prepareEntity(entityMeta, null, tableAlias, propertyMapperList,
                idIndexList);
        assertEquals("T1_.ID as C1_, T1_.DTO as C2_", query.selectClause
                .toSql());
    }

    /**
     * 
     */
    public void testPrepareEntity_selectClause_includesAndExcludes() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.includes("name", "bbbId");
        query.excludes("id", "name");
        query.prepareCallerClassAndMethodName("getResultList");
        List<PropertyMapper> propertyMapperList = new ArrayList<PropertyMapper>(
                50);
        List<Integer> idIndexList = new ArrayList<Integer>();
        EntityMeta entityMeta = query.prepareEntityMeta(query.baseClass, null);
        String tableAlias = query.prepareTableAlias(null);
        query.prepareEntity(entityMeta, null, tableAlias, propertyMapperList,
                idIndexList);
        assertEquals("T1_.ID as C1_, T1_.NAME as C2_, T1_.BBB_ID as C3_",
                query.selectClause.toSql());
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
        query.prepareEntity(entityMeta, null, tableAlias, propertyMapperList,
                idIndexList);
        ValueType[] valueTypes = query.getValueTypes();
        assertEquals(4, valueTypes.length);
        assertEquals(ValueTypes.INTEGER, valueTypes[0]);
        assertEquals(ValueTypes.STRING, valueTypes[1]);
        assertEquals(ValueTypes.INTEGER, valueTypes[2]);
        assertEquals(ValueTypes.SERIALIZABLE_BLOB, valueTypes[3]);
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
        query.prepareEntity(entityMeta, null, tableAlias, propertyMapperList,
                idIndexList);
        PropertyMapperImpl[] propertyMappers = query
                .toPropertyMapperArray(propertyMapperList);
        assertEquals(4, propertyMappers.length);
        assertEquals(0, propertyMappers[0].getPropertyIndex());
        assertEquals(Aaa.class.getDeclaredField("id"), propertyMappers[0]
                .getField());
        assertEquals(1, propertyMappers[1].getPropertyIndex());
        assertEquals(Aaa.class.getDeclaredField("name"), propertyMappers[1]
                .getField());
        assertEquals(2, propertyMappers[2].getPropertyIndex());
        assertEquals(Aaa.class.getDeclaredField("bbbId"), propertyMappers[2]
                .getField());
        assertEquals(3, propertyMappers[3].getPropertyIndex());
        assertEquals(Aaa.class.getDeclaredField("dto"), propertyMappers[3]
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
        query.prepareEntity(entityMeta, null, tableAlias, propertyMapperList,
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
        query.prepareEntity(entityMeta, null, tableAlias, propertyMapperList,
                idIndexList);
        ValueType[] valueTypes = query.getValueTypes();
        assertEquals(2, valueTypes.length);
        assertEquals(ValueTypes.INTEGER, valueTypes[0]);
        assertEquals(ValueTypes.CLOB, valueTypes[1]);
    }

    /**
     * 
     */
    public void testPrepareEntity_getCount() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.count = true;
        query.prepareCallerClassAndMethodName("getCount");
        List<PropertyMapper> propertyMapperList = new ArrayList<PropertyMapper>(
                50);
        List<Integer> idIndexList = new ArrayList<Integer>();
        EntityMeta entityMeta = query.prepareEntityMeta(query.baseClass, null);
        String tableAlias = query.prepareTableAlias(null);
        query.prepareEntity(entityMeta, null, tableAlias, propertyMapperList,
                idIndexList);
        assertEquals("count(*)", query.selectClause.toSql());
        ValueType[] valueTypes = query.getValueTypes();
        assertEquals(1, valueTypes.length);
        assertEquals(ValueTypes.LONG, valueTypes[0]);
    }

    /**
     * 
     */
    public void testPrepareEntity_selectClause_eager() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.eager("aaa.lazyName");
        query.prepareCallerClassAndMethodName("getResultList");
        List<PropertyMapper> propertyMapperList = new ArrayList<PropertyMapper>(
                50);
        List<Integer> idIndexList = new ArrayList<Integer>();
        EntityMeta entityMeta = query.prepareEntityMeta(query.baseClass, null);
        JoinMeta joinMeta = new JoinMeta("aaa");
        String tableAlias = query.prepareTableAlias(null);
        query.prepareEntity(entityMeta, joinMeta, tableAlias,
                propertyMapperList, idIndexList);
        assertEquals(
                "T1_.ID as C1_, T1_.NAME as C2_, T1_.BBB_ID as C3_, T1_.DTO as C4_, T1_.LAZY_NAME as C5_",
                query.selectClause.toSql());
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
        assertEquals(4, propertyMappers.length);
        assertEquals(0, propertyMappers[0].getPropertyIndex());
        assertEquals(Aaa.class.getDeclaredField("id"), propertyMappers[0]
                .getField());
        assertEquals(1, propertyMappers[1].getPropertyIndex());
        assertEquals(Aaa.class.getDeclaredField("name"), propertyMappers[1]
                .getField());
        assertEquals(2, propertyMappers[2].getPropertyIndex());
        assertEquals(Aaa.class.getDeclaredField("bbbId"), propertyMappers[2]
                .getField());
        assertEquals(3, propertyMappers[3].getPropertyIndex());
        assertEquals(Aaa.class.getDeclaredField("dto"), propertyMappers[3]
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
        String expected = "select T1_.ID as C1_, T1_.NAME as C2_, T1_.BBB_ID as C3_, T1_.DTO as C4_, T2_.ID as C5_, T2_.NAME as C6_, T2_.CCC_ID as C7_ from AAA T1_ left outer join BBB T2_ on T1_.BBB_ID = T2_.ID";
        assertEquals(expected, query.toSql());
    }

    /**
     * @throws Exception
     * 
     */
    public void testPrepareJoin_sql_includes() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.includes("name", "bbbId");
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        query.prepareJoin(new JoinMeta("bbb"));
        String expected = "select T1_.ID as C1_, T1_.NAME as C2_, T1_.BBB_ID as C3_, T2_.ID as C4_ from AAA T1_ left outer join BBB T2_ on T1_.BBB_ID = T2_.ID";
        assertEquals(expected, query.toSql());
    }

    /**
     * @throws Exception
     * 
     */
    public void testPrepareJoin_sql_excludes() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.excludes("name", "bbbId");
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        query.prepareJoin(new JoinMeta("bbb"));
        String expected = "select T1_.ID as C1_, T1_.DTO as C2_, T2_.ID as C3_, T2_.NAME as C4_, T2_.CCC_ID as C5_ from AAA T1_ left outer join BBB T2_ on T1_.BBB_ID = T2_.ID";
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
        String expected = "select T1_.ID as C1_, T1_.NAME as C2_, T1_.CCC_ID as C3_, T2_.ID as C4_, T2_.NAME as C5_, T2_.BBB_ID as C6_, T2_.DTO as C7_ from BBB T1_ left outer join AAA T2_ on T2_.BBB_ID = T1_.ID";
        assertEquals(expected, query.toSql());
    }

    /**
     * 
     */
    public void testPrepareJoin_sql_mappedBy_includes() {
        AutoSelectImpl<Bbb> query = new AutoSelectImpl<Bbb>(manager, Bbb.class);
        query.includes("name", "aaa");
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        query.prepareJoin(new JoinMeta("aaa"));
        String expected = "select T1_.ID as C1_, T1_.NAME as C2_, T2_.ID as C3_, T2_.NAME as C4_, T2_.BBB_ID as C5_, T2_.DTO as C6_ from BBB T1_ left outer join AAA T2_ on T2_.BBB_ID = T1_.ID";
        assertEquals(expected, query.toSql());
    }

    /**
     * 
     */
    public void testPrepareJoin_sql_mappedBy_excludes() {
        AutoSelectImpl<Bbb> query = new AutoSelectImpl<Bbb>(manager, Bbb.class);
        query.excludes("name", "aaa");
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        query.prepareJoin(new JoinMeta("aaa"));
        String expected = "select T1_.ID as C1_, T1_.CCC_ID as C2_, T2_.ID as C3_ from BBB T1_ left outer join AAA T2_ on T2_.BBB_ID = T1_.ID";
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
        String expected = "select T1_.ID as C1_, T1_.NAME as C2_, T1_.BBB_ID as C3_, T1_.DTO as C4_, T2_.ID as C5_, T2_.NAME as C6_, T2_.CCC_ID as C7_, T3_.ID as C8_, T3_.NAME as C9_ from AAA T1_ left outer join BBB T2_ on T1_.BBB_ID = T2_.ID left outer join CCC T3_ on T2_.CCC_ID = T3_.ID";
        assertEquals(expected, query.toSql());
    }

    /**
     * 
     */
    public void testPrepareJoin_sql_nest_includesAndExcludes() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.includes("name", "bbb.name", "bbb.ccc.name");
        query.excludes("name", "bbb", "bbb.ccc");
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        query.prepareJoin(new JoinMeta("bbb"));
        query.prepareJoin(new JoinMeta("bbb.ccc"));
        String expected = "select T1_.ID as C1_, T1_.NAME as C2_, T2_.ID as C3_, T2_.NAME as C4_, T3_.ID as C5_, T3_.NAME as C6_ from AAA T1_ left outer join BBB T2_ on T1_.BBB_ID = T2_.ID left outer join CCC T3_ on T2_.CCC_ID = T3_.ID";
        assertEquals(expected, query.toSql());
    }

    /**
     * 
     */
    public void testPrepareJoin_sql_nest_includesAndExcludes2() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.includes("id", "name", "bbb", "bbb.ccc");
        query.excludes("name", "bbb.name", "bbb.ccc.name");
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        query.prepareJoin(new JoinMeta("bbb"));
        query.prepareJoin(new JoinMeta("bbb.ccc"));
        String expected = "select T1_.ID as C1_, T1_.NAME as C2_, T2_.ID as C3_, T2_.CCC_ID as C4_, T3_.ID as C5_ from AAA T1_ left outer join BBB T2_ on T1_.BBB_ID = T2_.ID left outer join CCC T3_ on T2_.CCC_ID = T3_.ID";
        assertEquals(expected, query.toSql());
    }

    /**
     * 
     */
    public void testPrepareJoin_sql_nest_includesAndExcludes3() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.includes("id", "name", "bbb");
        query.excludes("bbb.ccc");
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        query.prepareJoin(new JoinMeta("bbb"));
        query.prepareJoin(new JoinMeta("bbb.ccc"));
        String expected = "select T1_.ID as C1_, T1_.NAME as C2_, T2_.ID as C3_, T2_.NAME as C4_, T2_.CCC_ID as C5_, T3_.ID as C6_ from AAA T1_ left outer join BBB T2_ on T1_.BBB_ID = T2_.ID left outer join CCC T3_ on T2_.CCC_ID = T3_.ID";
        assertEquals(expected, query.toSql());
    }

    /**
     * 
     */
    public void testPrepareJoin_sql_nest_includesAndExcludes4() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.includes("id", "name", "bbb", "bbb.ccc");
        query.excludes("bbb.ccc");
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        query.prepareJoin(new JoinMeta("bbb"));
        query.prepareJoin(new JoinMeta("bbb.ccc"));
        String expected = "select T1_.ID as C1_, T1_.NAME as C2_, T2_.ID as C3_, T2_.NAME as C4_, T2_.CCC_ID as C5_, T3_.ID as C6_, T3_.NAME as C7_ from AAA T1_ left outer join BBB T2_ on T1_.BBB_ID = T2_.ID left outer join CCC T3_ on T2_.CCC_ID = T3_.ID";
        assertEquals(expected, query.toSql());
    }

    /**
     * 
     */
    public void testPrepareJoin_sql_nest_includesAndExcludes5() {
        AutoSelectImpl<Bbb> query = new AutoSelectImpl<Bbb>(manager, Bbb.class);
        query.includes("id", "name", "aaa");
        query.excludes("aaa.bbb");
        query.includes("aaa.bbb.ccc");
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        query.prepareJoin(new JoinMeta("aaa"));
        query.prepareJoin(new JoinMeta("aaa.bbb"));
        query.prepareJoin(new JoinMeta("aaa.bbb.ccc"));
        String expected = "select T1_.ID as C1_, T1_.NAME as C2_, T2_.ID as C3_, T2_.NAME as C4_, T2_.BBB_ID as C5_, T2_.DTO as C6_, T3_.ID as C7_, T4_.ID as C8_, T4_.NAME as C9_ from BBB T1_ left outer join AAA T2_ on T2_.BBB_ID = T1_.ID left outer join BBB T3_ on T2_.BBB_ID = T3_.ID left outer join CCC T4_ on T3_.CCC_ID = T4_.ID";
        assertEquals(expected, query.toSql());
    }

    /**
     * @throws Exception
     * 
     */
    public void testPrepareJoin_sql_condition() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepareCallerClassAndMethodName("getResultList");
        query.prepareTarget();
        query.prepareJoin(new JoinMeta("bbb", "bbb.id = 100", new Object[0]));
        String expected = "select T1_.ID as C1_, T1_.NAME as C2_, T1_.BBB_ID as C3_, T1_.DTO as C4_, T2_.ID as C5_, T2_.NAME as C6_, T2_.CCC_ID as C7_ from AAA T1_ left outer join BBB T2_ on T1_.BBB_ID = T2_.ID and T2_.ID = 100";
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
        assertEquals(BeanListAutoResultSetHandler.class, handler.getClass());
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
    public void testCreateIterateResultSetHandler() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.prepare("getResultList");
        ResultSetHandler handler = query
                .createIterateResultSetHandler(new IterationCallback<Aaa, Object>() {

                    public Object iterate(Aaa entity, IterationContext context) {
                        return null;
                    }
                });
        assertEquals(BeanIterationAutoResultSetHandler.class, handler
                .getClass());
    }

    /**
     * 
     */
    public void testPrepareId() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.id(1);
        query.prepare("getSingleResult");
        assertEquals(" where T1_.ID = ?", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(1, variables.length);
        assertEquals(new Integer(1), variables[0]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(1, variableClasses.length);
        assertEquals(Integer.class, variableClasses[0]);
    }

    /**
     * 
     */
    public void testPrepareId_invalidLength() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        try {
            query.id(1, 2);
            fail();
        } catch (IllegalIdPropertySizeRuntimeException e) {
            System.out.println(e);
        }
    }

    /**
     * 
     */
    public void testIdSql() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.id(1);
        query.prepare("getResultList");
        assertEquals(
                "select T1_.ID as C1_, T1_.NAME as C2_, T1_.BBB_ID as C3_, T1_.DTO as C4_ "
                        + "from AAA T1_ " + "where T1_.ID = ?",
                query.executedSql);
    }

    /**
     * 
     */
    public void testPrepareVersion() {
        AutoSelectImpl<Eee> query = new AutoSelectImpl<Eee>(manager, Eee.class);
        query.id(1);
        query.version(2);
        query.prepare("getSingleResult");
        assertEquals(" where T1_.ID = ? and T1_.VERSION = ?", query.whereClause
                .toSql());
        Object[] variables = query.getParamValues();
        assertEquals(2, variables.length);
        assertEquals(new Integer(1), variables[0]);
        assertEquals(new Integer(2), variables[1]);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(2, variableClasses.length);
        assertEquals(Integer.class, variableClasses[0]);
        assertEquals(Integer.class, variableClasses[1]);
    }

    /**
     * 
     */
    public void testPrepareVersion_noVersionProperty() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.id(1);
        try {
            query.version(2);
            fail();
        } catch (VersionPropertyNotExistsRuntimeException e) {
            System.out.println(e);
        }
    }

    /**
     * 
     */
    public void testPrepareVersion_withoutIdProperty() {
        AutoSelectImpl<Eee> query = new AutoSelectImpl<Eee>(manager, Eee.class);
        query.version(2);
        try {
            query.prepare("getSingleResult");
            fail();
        } catch (UnsupportedOperationException e) {
            System.out.println(e);
        }
    }

    /**
     * 
     */
    public void testIdVersionSql() {
        AutoSelectImpl<Eee> query = new AutoSelectImpl<Eee>(manager, Eee.class);
        query.id(1);
        query.version(2);
        query.prepare("getResultList");
        assertEquals(
                "select T1_.ID as C1_, T1_.NAME as C2_, T1_.LONG_TEXT as C3_, T1_.FFF_ID as C4_, T1_.VERSION as C5_, T1_.LAST_UPDATED as C6_ "
                        + "from EEE T1_ "
                        + "where T1_.ID = ? and T1_.VERSION = ?",
                query.executedSql);
    }

    /**
     * 
     */
    public void testIdVersionSql_withCondition() {
        AutoSelectImpl<Eee> query = new AutoSelectImpl<Eee>(manager, Eee.class);
        query.id(1).version(2).where("lastUpdated = ?",
                Parameter.date(new Date()));
        query.prepare("getResultList");
        assertEquals(
                "select T1_.ID as C1_, T1_.NAME as C2_, T1_.LONG_TEXT as C3_, T1_.FFF_ID as C4_, T1_.VERSION as C5_, T1_.LAST_UPDATED as C6_ "
                        + "from EEE T1_ "
                        + "where T1_.ID = ? and T1_.VERSION = ? and (T1_.LAST_UPDATED = ?)",
                query.executedSql);
        assertEquals(new Integer(1), query.paramList.get(0).value);
        assertEquals(new Integer(2), query.paramList.get(1).value);
        assertTrue(query.paramList.get(2).value instanceof Date);
    }

    /**
     * 
     */
    public void testIdVersionSql_withJoinCondition() {
        AutoSelectImpl<Eee> query = new AutoSelectImpl<Eee>(manager, Eee.class);
        query.leftOuterJoin("fff", false, "fff.id = 100").id(1).version(2)
                .where("lastUpdated = ?", Parameter.date(new Date()));
        query.prepare("getResultList");
        assertEquals(
                "select T1_.ID as C1_, T1_.NAME as C2_, T1_.LONG_TEXT as C3_, T1_.FFF_ID as C4_, T1_.VERSION as C5_, T1_.LAST_UPDATED as C6_ "
                        + "from EEE T1_ left outer join FFF T2_ on T1_.FFF_ID = T2_.ID and T2_.ID = 100 "
                        + "where T1_.ID = ? and T1_.VERSION = ? and (T1_.LAST_UPDATED = ?)",
                query.executedSql);
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
        assertEquals(1, query.whereParams.size());
        assertEquals(1, query.whereParams.get(0));
    }

    /**
     * @throws Exception
     * 
     */
    public void testWhere_where() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        assertSame(query, query.where(new SimpleWhere().eq("id", 1)));
        query.prepare("getResultList");
        assertEquals("id = ?", query.criteria);
        assertEquals(1, query.getParamSize());
        assertEquals(1, query.getParam(0).value);
    }

    /**
     * @throws Exception
     * 
     */
    public void testWhere_wheres() throws Exception {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        assertSame(query, query.where(new SimpleWhere().eq("id", 1),
                new SimpleWhere().eq("name", "aaa")));
        query.prepare("getResultList");
        assertEquals("(id = ?) and (name = ?)", query.criteria);
        assertEquals(2, query.getParamSize());
        assertEquals(1, query.getParam(0).value);
        assertEquals("aaa", query.getParam(1).value);
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
    public void testWhere_wrapper() throws Exception {
        AutoSelectImpl<Emp> query = new AutoSelectImpl<Emp>(manager, Emp.class);
        assertSame(query, query.where(new SimpleWhere().eq("hiredate",
                timestamp(new Date(0)))));
        query.prepare("getResultList");
        assertEquals("hiredate = ?", query.criteria);
        assertEquals(1, query.getParamSize());
        assertEquals(new Date(0), query.getParam(0).value);
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
        assertEquals(" where (T1_.ID = ?)", query.whereClause.toSql());
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
        assertEquals(" where (T1_.ID = ?)", query.whereClause.toSql());
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
        query.leftOuterJoin("bbb").where(w);
        query.prepare("getResultList");
        assertEquals(" where (T2_.ID = ?)", query.whereClause.toSql());
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
        assertEquals(" where (T1_.ID <> ?)", query.whereClause.toSql());
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
        query.leftOuterJoin("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.id_NE", 1);
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where (T2_.ID <> ?)", query.whereClause.toSql());
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
        assertEquals(" where (T1_.ID < ?)", query.whereClause.toSql());
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
        query.leftOuterJoin("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.id_LT", 1);
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where (T2_.ID < ?)", query.whereClause.toSql());
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
        assertEquals(" where (T1_.ID <= ?)", query.whereClause.toSql());
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
        query.leftOuterJoin("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.id_LE", 1);
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where (T2_.ID <= ?)", query.whereClause.toSql());
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
        assertEquals(" where (T1_.ID > ?)", query.whereClause.toSql());
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
        query.leftOuterJoin("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.id_GT", 1);
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where (T2_.ID > ?)", query.whereClause.toSql());
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
        assertEquals(" where (T1_.ID >= ?)", query.whereClause.toSql());
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
        query.leftOuterJoin("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.id_GE", 1);
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where (T2_.ID >= ?)", query.whereClause.toSql());
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
        assertEquals(" where (T1_.ID in (?, ?))", query.whereClause.toSql());
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
    public void testPrepareCondition_IN_List() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("id_IN", Arrays.asList(1, 2));
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where (T1_.ID in (?, ?))", query.whereClause.toSql());
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
        query.leftOuterJoin("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.id_IN", new Object[] { 1, 2 });
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where (T2_.ID in (?, ?))", query.whereClause.toSql());
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
        assertEquals(" where (T1_.ID not in (?, ?))", query.whereClause.toSql());
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
    public void testPrepareCondition_NOT_IN_List() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("id_NOT_IN", Arrays.asList(1, 2));
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where (T1_.ID not in (?, ?))", query.whereClause.toSql());
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
        query.leftOuterJoin("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.id_NOT_IN", new Object[] { 1, 2 });
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where (T2_.ID not in (?, ?))", query.whereClause.toSql());
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
        assertEquals(" where (T1_.NAME like ?)", query.whereClause.toSql());
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
        query.leftOuterJoin("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.name_LIKE", "aaa");
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where (T2_.NAME like ?)", query.whereClause.toSql());
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
        assertEquals(" where (T1_.NAME like ?)", query.whereClause.toSql());
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
        query.leftOuterJoin("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.name_STARTS", "aaa");
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where (T2_.NAME like ?)", query.whereClause.toSql());
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
        assertEquals(" where (T1_.NAME like ?)", query.whereClause.toSql());
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
        query.leftOuterJoin("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.name_ENDS", "aaa");
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where (T2_.NAME like ?)", query.whereClause.toSql());
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
        assertEquals(" where (T1_.NAME like ?)", query.whereClause.toSql());
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
        query.leftOuterJoin("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.name_CONTAINS", "aaa");
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where (T2_.NAME like ?)", query.whereClause.toSql());
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
        assertEquals(" where (T1_.NAME is null)", query.whereClause.toSql());
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
        query.leftOuterJoin("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.name_IS_NULL", true);
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where (T2_.NAME is null)", query.whereClause.toSql());
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
        assertEquals(" where (T1_.NAME is not null)", query.whereClause.toSql());
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
        query.leftOuterJoin("bbb");
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("bbb.name_IS_NOT_NULL", true);
        query.where(w);
        query.prepare("getResultList");
        assertEquals(" where (T2_.NAME is not null)", query.whereClause.toSql());
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
    public void testPrepareCondition_empty() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        Map<String, Object> w = new HashMap<String, Object>();
        w.put("id", null);
        query.where(w);
        query.prepare("getResultList");
        assertEquals("", query.whereClause.toSql());
        Object[] variables = query.getParamValues();
        assertEquals(0, variables.length);
        Class<?>[] variableClasses = query.getParamClasses();
        assertEquals(0, variableClasses.length);
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
    public void testPrepareOrderBy() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.leftOuterJoin("bbb").orderBy("name, bbb.id desc");
        query.prepare("getResultList");
        assertEquals(" order by C2_, C5_ desc", query.orderByClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareOrderByItems() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.leftOuterJoin("bbb").orderBy(new OrderByItem("name"),
                new OrderByItem("bbb.id", OrderingSpec.DESC));
        assertEquals("name, bbb.id desc", query.orderBy);
        query.prepare("getResultList");
        assertEquals(" order by C2_, C5_ desc", query.orderByClause.toSql());
    }

    /**
     * 
     */
    public void testPrepareOrderBy_sql() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.leftOuterJoin("bbb").orderBy("bbb.id desc");
        query.prepare("getResultList");
        assertEquals(
                "select T1_.ID as C1_, T1_.NAME as C2_, T1_.BBB_ID as C3_, T1_.DTO as C4_, T2_.ID as C5_, T2_.NAME as C6_, T2_.CCC_ID as C7_ from AAA T1_ left outer join BBB T2_ on T1_.BBB_ID = T2_.ID order by C5_ desc",
                query.toSql());
    }

    /**
     * 
     */
    public void testPrepareCriteria() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.leftOuterJoin("bbb").where("bbb.id = ?", 1);
        query.prepare("getResultList");
        assertEquals(
                "select T1_.ID as C1_, T1_.NAME as C2_, T1_.BBB_ID as C3_, T1_.DTO as C4_, T2_.ID as C5_, T2_.NAME as C6_, T2_.CCC_ID as C7_ from AAA T1_ left outer join BBB T2_ on T1_.BBB_ID = T2_.ID where (T2_.ID = ?)",
                query.toSql());
    }

    /**
     * 
     */
    public void testForUpdate() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.forUpdate();
        query.prepare(null);
        assertEquals(" for update", query.forUpdate);
        assertEquals(" from AAA T1_", query.fromClause.toSql());
    }

    /**
     * 
     */
    public void testForUpdate_join() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.leftOuterJoin("bbb").leftOuterJoin("bbb.ccc");
        query.forUpdate();
        query.prepare(null);
        assertEquals(" for update", query.forUpdate);
        assertEquals(" from AAA T1_"
                + " left outer join BBB T2_ on T1_.BBB_ID = T2_.ID"
                + " left outer join CCC T3_ on T2_.CCC_ID = T3_.ID",
                query.fromClause.toSql());
    }

    /**
     * 
     */
    public void testForUpdate_withLockHint() {
        manager.setDialect(new MssqlDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.forUpdate();
        query.prepare(null);
        assertEquals("", query.forUpdate);
        assertEquals(" from AAA T1_ with (updlock, rowlock)", query.fromClause
                .toSql());
    }

    /**
     * 
     */
    public void testForUpdate_withLockHint_join() {
        manager.setDialect(new MssqlDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.leftOuterJoin("bbb").leftOuterJoin("bbb.ccc");
        query.forUpdate();
        query.prepare(null);
        assertEquals("", query.forUpdate);
        assertEquals(
                " from AAA T1_ with (updlock, rowlock)"
                        + " left outer join BBB T2_ with (updlock, rowlock) on T1_.BBB_ID = T2_.ID"
                        + " left outer join CCC T3_ with (updlock, rowlock) on T2_.CCC_ID = T3_.ID",
                query.fromClause.toSql());
    }

    /**
     * 
     */
    public void testForUpdate_notSupported() {
        manager.setDialect(new HsqlDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        try {
            query.forUpdate();
            fail();
        } catch (UnsupportedOperationException expected) {
            expected.printStackTrace();
        }
    }

    /**
     * 
     */
    public void testForUpdate_innerJoin_notSupported() {
        manager.setDialect(new Db2Dialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        try {
            query.innerJoin("bbb").forUpdate();
            query.prepare(null);
            fail();
        } catch (UnsupportedOperationException expected) {
            expected.printStackTrace();
        }
    }

    /**
     * 
     */
    public void testForUpdate_outerJoin_notSupported() {
        manager.setDialect(new PostgreDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        try {
            query.leftOuterJoin("bbb").forUpdate();
            query.prepare(null);
            fail();
        } catch (UnsupportedOperationException expected) {
            expected.printStackTrace();
        }
    }

    /**
     * 
     */
    public void testForUpdate_withPaging() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        try {
            query.forUpdate();
            query.offset(10).limit(10);
            query.prepare(null);
            fail();
        } catch (UnsupportedOperationException expected) {
            expected.printStackTrace();
        }
    }

    /**
     * 
     */
    public void testForUpdateWithProperty_columnAlias() {
        manager.setDialect(new OracleDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.forUpdate("name");
        query.prepare(null);
        assertEquals(" for update of T1_.NAME", query.forUpdate);
        assertEquals(" from AAA T1_", query.fromClause.toSql());
    }

    /**
     * 
     */
    public void testForUpdateWithProperty_columnAlias_join() {
        manager.setDialect(new OracleDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.leftOuterJoin("bbb").leftOuterJoin("bbb.ccc");
        query.forUpdate("name", "bbb.ccc.name");
        query.prepare(null);
        assertEquals(" for update of T1_.NAME, T3_.NAME", query.forUpdate);
        assertEquals(" from AAA T1_"
                + " left outer join BBB T2_ on T1_.BBB_ID = T2_.ID"
                + " left outer join CCC T3_ on T2_.CCC_ID = T3_.ID",
                query.fromClause.toSql());
    }

    /**
     * 
     */
    public void testForUpdateWithProperty_tableAlias() {
        manager.setDialect(new PostgreDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.forUpdate("name");
        query.prepare(null);
        assertEquals(" for update of T1_", query.forUpdate);
        assertEquals(" from AAA T1_", query.fromClause.toSql());
    }

    /**
     * 
     */
    public void testForUpdateWithProperty_tableAlias_join() {
        manager.setDialect(new PostgreDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.join("bbb", JoinType.INNER).join("bbb.ccc", JoinType.INNER);
        query.forUpdate("name", "bbb.ccc.name");
        query.prepare(null);
        assertEquals(" for update of T1_, T3_", query.forUpdate);
        assertEquals(" from AAA T1_"
                + " inner join BBB T2_ on T1_.BBB_ID = T2_.ID"
                + " inner join CCC T3_ on T2_.CCC_ID = T3_.ID",
                query.fromClause.toSql());
    }

    /**
     * 
     */
    public void testForUpdateWithProperty_lockHint() {
        manager.setDialect(new MssqlDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.forUpdate("name");
        query.prepare(null);
        assertEquals("", query.forUpdate);
        assertEquals(" from AAA T1_ with (updlock, rowlock)", query.fromClause
                .toSql());
    }

    /**
     * 
     */
    public void testForUpdateWithProperty_lockHint_join() {
        manager.setDialect(new MssqlDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.leftOuterJoin("bbb").leftOuterJoin("bbb.ccc");
        query.forUpdate("name", "bbb.ccc.name");
        query.prepare(null);
        assertEquals("", query.forUpdate);
        assertEquals(
                " from AAA T1_ with (updlock, rowlock)"
                        + " left outer join BBB T2_ on T1_.BBB_ID = T2_.ID"
                        + " left outer join CCC T3_ with (updlock, rowlock) on T2_.CCC_ID = T3_.ID",
                query.fromClause.toSql());
    }

    /**
     * 
     */
    public void testForUpdateWithProperty_notSupported() {
        manager.setDialect(new HsqlDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        try {
            query.forUpdate("name");
            fail();
        } catch (UnsupportedOperationException expected) {
            expected.printStackTrace();
        }
    }

    /**
     * 
     */
    public void testForUpdateWithProperty_join_notSupported() {
        manager.setDialect(new PostgreDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        try {
            query.leftOuterJoin("bbb").leftOuterJoin("bbb.ccc");
            query.forUpdate("name", "bbb.ccc.name");
            query.prepare(null);
            fail();
        } catch (UnsupportedOperationException expected) {
            expected.printStackTrace();
        }
    }

    /**
     * 
     */
    public void testForUpdateWithProperty_invalidProperty() {
        manager.setDialect(new OracleDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        try {
            query.forUpdate("hogehoge");
            query.prepare(null);
            fail();
        } catch (PropertyNotFoundRuntimeException expected) {
            expected.printStackTrace();
        }
    }

    /**
     * 
     */
    public void testForUpdateWithProperty_invalidRelationship() {
        manager.setDialect(new OracleDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        try {
            query.leftOuterJoin("bbb");
            query.forUpdate("bbb");
            query.prepare(null);
            fail();
        } catch (PropertyNotFoundRuntimeException expected) {
            expected.printStackTrace();
        }
    }

    /**
     * 
     */
    public void testForUpdateWithProperty_invalidJoin() {
        manager.setDialect(new OracleDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        try {
            query.leftOuterJoin("bbb");
            query.forUpdate("ccc.hogehoge");
            query.prepare(null);
            fail();
        } catch (BaseJoinNotFoundRuntimeException expected) {
            expected.printStackTrace();
        }
    }

    /**
     * 
     */
    public void testForUpdateNowait() {
        manager.setDialect(new OracleDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.forUpdateNowait();
        query.prepare(null);
        assertEquals(" for update nowait", query.forUpdate);
    }

    /**
     * 
     */
    public void testForUpdateNowait_lockHint() {
        manager.setDialect(new MssqlDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.forUpdateNowait();
        query.prepare(null);
        assertEquals("", query.forUpdate);
        assertEquals(" from AAA T1_ with (updlock, rowlock, nowait)",
                query.fromClause.toSql());
    }

    /**
     * 
     */
    public void testForUpdateNowait_notSupported() {
        manager.setDialect(new HsqlDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        try {
            query.forUpdateNowait();
            fail();
        } catch (UnsupportedOperationException expected) {
            expected.printStackTrace();
        }
    }

    /**
     * 
     */
    public void testForUpdateNowaitWithProperty() {
        manager.setDialect(new OracleDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.forUpdateNowait("name");
        query.prepare(null);
        assertEquals(" for update of T1_.NAME nowait", query.forUpdate);
    }

    /**
     * 
     */
    public void testForUpdateNowaitWithProperty_lockHint() {
        manager.setDialect(new MssqlDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.forUpdateNowait("name");
        query.prepare(null);
        assertEquals("", query.forUpdate);
        assertEquals(" from AAA T1_ with (updlock, rowlock, nowait)",
                query.fromClause.toSql());
    }

    /**
     * 
     */
    public void testForUpdateNowaitWithProperty_notSupported() {
        manager.setDialect(new HsqlDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        try {
            query.forUpdateNowait("name");
            fail();
        } catch (UnsupportedOperationException expected) {
            expected.printStackTrace();
        }
    }

    /**
     * 
     */
    public void testForUpdateWait() {
        manager.setDialect(new OracleDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.forUpdateWait(10);
        query.prepare(null);
        assertEquals(" for update wait 10", query.forUpdate);
    }

    /**
     * 
     */
    public void testForUpdateWait_notSupported() {
        manager.setDialect(new HsqlDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        try {
            query.forUpdateWait(10);
            fail();
        } catch (UnsupportedOperationException expected) {
            expected.printStackTrace();
        }
    }

    /**
     * 
     */
    public void testForUpdateWaitWithProperty() {
        manager.setDialect(new OracleDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.forUpdateWait(10, "name");
        query.prepare(null);
        assertEquals(" for update of T1_.NAME wait 10", query.forUpdate);
    }

    /**
     * 
     */
    public void testForUpdateWaitWithProperty_notSupported() {
        manager.setDialect(new HsqlDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        try {
            query.forUpdateWait(10, "name");
            fail();
        } catch (UnsupportedOperationException expected) {
            expected.printStackTrace();
        }
    }

    /**
     * 
     */
    public void testForUpdateSql() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.leftOuterJoin("bbb").orderBy("bbb.id desc").forUpdate();
        query.prepare("getResultList");
        assertEquals(
                "select T1_.ID as C1_, T1_.NAME as C2_, T1_.BBB_ID as C3_, T1_.DTO as C4_, T2_.ID as C5_, T2_.NAME as C6_, T2_.CCC_ID as C7_ "
                        + "from AAA T1_ "
                        + "left outer join BBB T2_ on T1_.BBB_ID = T2_.ID "
                        + "order by C5_ desc " + "for update",
                query.executedSql);
    }

    /**
     * 
     */
    public void testForUpdateSql_lockHint() {
        manager.setDialect(new MssqlDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.leftOuterJoin("bbb").orderBy("bbb.id desc").forUpdate();
        query.prepare("getResultList");
        assertEquals(
                "select T1_.ID as C1_, T1_.NAME as C2_, T1_.BBB_ID as C3_, T1_.DTO as C4_, T2_.ID as C5_, T2_.NAME as C6_, T2_.CCC_ID as C7_ "
                        + "from AAA T1_ with (updlock, rowlock) "
                        + "left outer join BBB T2_ with (updlock, rowlock) on T1_.BBB_ID = T2_.ID "
                        + "order by C5_ desc", query.executedSql);
    }

    /**
     * 
     */
    public void testEagerSql() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.eager("lazyName");
        query.prepare("getResultList");
        assertEquals(
                "select T1_.ID as C1_, T1_.NAME as C2_, T1_.BBB_ID as C3_, T1_.DTO as C4_, T1_.LAZY_NAME as C5_ "
                        + "from AAA T1_", query.executedSql);
    }

    /**
     * 
     */
    public void testEagerSql_withJoin() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.leftOuterJoin("bbb").eager("bbb.lazyName");
        query.prepare("getResultList");
        assertEquals(
                "select T1_.ID as C1_, T1_.NAME as C2_, T1_.BBB_ID as C3_, T1_.DTO as C4_, "
                        + "T2_.ID as C5_, T2_.NAME as C6_, T2_.CCC_ID as C7_, T2_.LAZY_NAME as C8_ "
                        + "from AAA T1_ left outer join BBB T2_ on T1_.BBB_ID = T2_.ID",
                query.executedSql);
    }

    /**
     * 
     */
    public void testHint() {
        manager.setDialect(new OracleDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.hint("index(Aaa index)");
        query.prepare("getResultList");
        assertEquals(
                "select /*+ index(T1_ index) */ T1_.ID as C1_, T1_.NAME as C2_, T1_.BBB_ID as C3_, T1_.DTO as C4_ "
                        + "from AAA T1_", query.executedSql);
    }

    /**
     * 
     */
    public void testHint_notSupport() {
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.hint("index(Aaa index)");
        query.prepare("getResultList");
        assertEquals(
                "select T1_.ID as C1_, T1_.NAME as C2_, T1_.BBB_ID as C3_, T1_.DTO as C4_ "
                        + "from AAA T1_", query.executedSql);
    }

    /**
     * 
     */
    public void testHint_withJoin() {
        manager.setDialect(new OracleDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.leftOuterJoin("bbb", false);
        query.hint("index(bbb index)");
        query.prepare("getResultList");
        assertEquals(
                "select /*+ index(T2_ index) */ T1_.ID as C1_, T1_.NAME as C2_, T1_.BBB_ID as C3_, T1_.DTO as C4_ "
                        + "from AAA T1_ left outer join BBB T2_ on T1_.BBB_ID = T2_.ID",
                query.executedSql);
    }

    /**
     * 
     */
    public void testConvertEntityNameToTableAlias() {
        manager.setDialect(new OracleDialect());
        AutoSelectImpl<Aaa> query = new AutoSelectImpl<Aaa>(manager, Aaa.class);
        query.leftOuterJoin("bbb").leftOuterJoin("bbb.ddds");
        query.prepare("getResultList");
        assertEquals("T1_", query.convertEntityNameToTableAlias("Aaa"));
        assertEquals("T2_", query.convertEntityNameToTableAlias("bbb"));
        assertEquals("T3_", query.convertEntityNameToTableAlias("bbb.ddds"));
    }

    /**
     * 
     */
    public void testCompleted_getResultList() {

        AutoSelectImplDummy<Aaa> query = new AutoSelectImplDummy<Aaa>(manager,
                Aaa.class);
        query.getResultList();

        try {
            query.getResultList();
            fail();
        } catch (QueryTwiceExecutionRuntimeException expected) {
        }
    }

    /**
     * 
     */
    public void testCompleted_getSingleResult() {

        AutoSelectImplDummy<Aaa> query = new AutoSelectImplDummy<Aaa>(manager,
                Aaa.class);
        query.getSingleResult();

        try {
            query.getSingleResult();
            fail();
        } catch (QueryTwiceExecutionRuntimeException expected) {
        }
    }

    /**
     * 
     */
    public void testCompleted_getCount() {

        AutoSelectImplDummy<Aaa> query = new AutoSelectImplDummy<Aaa>(manager,
                Aaa.class);
        query.getCount();

        try {
            query.getCount();
            fail();
        } catch (QueryTwiceExecutionRuntimeException expected) {
        }
    }

    /**
    *
    */
    class AutoSelectImplDummy<E> extends AutoSelectImpl<E> {

        /**
         * @param jdbcManager
         * @param baseClass
         */
        public AutoSelectImplDummy(JdbcManagerImplementor jdbcManager,
                Class<E> baseClass) {
            super(jdbcManager, baseClass);
        }

        @Override
        protected List<E> getResultListInternal() {
            return Collections.emptyList();
        }

        @Override
        protected E getSingleResultInternal() {
            return null;
        }

        @Override
        public long getCount() {
            try {
                return super.getCount();
            } catch (NullPointerException e) {
                return 0;
            }
        }

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

        /**
         * 
         */
        @Lob
        @Basic(fetch = FetchType.LAZY)
        public String lazyName;
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
