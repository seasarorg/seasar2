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

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.entity.Eee;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.EntityMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.TableMetaFactoryImpl;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.mock.sql.MockPreparedStatement;

/**
 * @author koichik
 */
public class AbsAutoBatchUpdateTest extends TestCase {

    private JdbcManagerImpl manager;

    private int added;

    private int executed;

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
        SqlLogRegistry regisry = SqlLogRegistryLocator.getInstance();
        regisry.clear();
        manager = null;
    }

    /**
     * 
     */
    public void testBatchSize1() {
        List<Eee> entities = Arrays.asList(new Eee(), new Eee(), new Eee());
        MyBatchUpdate<Eee> query = new MyBatchUpdate<Eee>(manager, entities)
                .batchSize(2);
        assertEquals(2, query.batchSize);

        int[] rows = query.executeBatch(new MockPreparedStatement(null, null) {

            @Override
            public void addBatch() throws SQLException {
                ++added;
            }

            @Override
            public int[] executeBatch() throws SQLException {
                ++executed;
                return executed == 1 ? new int[] { 1, 2 } : new int[] { 3 };
            }

        });
        assertEquals(3, added);
        assertEquals(2, executed);
        assertEquals(3, rows.length);
        assertEquals(1, rows[0]);
        assertEquals(2, rows[1]);
        assertEquals(3, rows[2]);
    }

    /**
     * 
     */
    public void testBatchSize2() {
        List<Eee> entities = Arrays.asList(new Eee(), new Eee(), new Eee(),
                new Eee());
        MyBatchUpdate<Eee> query = new MyBatchUpdate<Eee>(manager, entities)
                .batchSize(2);
        assertEquals(2, query.batchSize);

        int[] rows = query.executeBatch(new MockPreparedStatement(null, null) {

            @Override
            public void addBatch() throws SQLException {
                ++added;
            }

            @Override
            public int[] executeBatch() throws SQLException {
                ++executed;
                return executed == 1 ? new int[] { 1, 2 } : new int[] { 3, 4 };
            }

        });
        assertEquals(4, added);
        assertEquals(2, executed);
        assertEquals(4, rows.length);
        assertEquals(1, rows[0]);
        assertEquals(2, rows[1]);
        assertEquals(3, rows[2]);
        assertEquals(4, rows[3]);
    }

    /**
     * 
     */
    public void testNoBatchSize() {
        List<Eee> entities = Arrays.asList(new Eee(), new Eee(), new Eee());
        MyBatchUpdate<Eee> query = new MyBatchUpdate<Eee>(manager, entities);
        assertEquals(0, query.batchSize);

        int[] rows = query.executeBatch(new MockPreparedStatement(null, null) {

            @Override
            public void addBatch() throws SQLException {
                ++added;
            }

            @Override
            public int[] executeBatch() throws SQLException {
                ++executed;
                return new int[] { 1, 2, 3 };
            }

        });
        assertEquals(3, added);
        assertEquals(1, executed);
        assertEquals(3, rows.length);
        assertEquals(1, rows[0]);
        assertEquals(2, rows[1]);
        assertEquals(3, rows[2]);
    }

    private static class MyBatchUpdate<T> extends
            AbstractAutoBatchUpdate<T, MyBatchUpdate<T>> {

        /**
         * @param jdbcManager
         * @param entities
         */
        public MyBatchUpdate(JdbcManagerImplementor jdbcManager,
                List<T> entities) {
            super(jdbcManager, entities);
        }

        @Override
        protected boolean isOptimisticLock() {
            return false;
        }

        @Override
        protected void prepareParams(T entity) {
        }

        @Override
        protected String toSql() {
            return null;
        }

        @Override
        protected void prepare(String methodName) {
        }

        @Override
        protected void logSql() {
        }

    }
}
