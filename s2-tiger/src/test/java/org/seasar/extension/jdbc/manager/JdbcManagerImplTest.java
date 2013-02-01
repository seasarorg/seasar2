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
package org.seasar.extension.jdbc.manager;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import junit.framework.TestCase;

import org.seasar.extension.datasource.DataSourceFactory;
import org.seasar.extension.datasource.impl.DataSourceFactoryImpl;
import org.seasar.extension.datasource.impl.SelectableDataSourceProxy;
import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.entity.Eee;
import org.seasar.extension.jdbc.entity.Iii;
import org.seasar.extension.jdbc.exception.NoIdPropertyRuntimeException;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl.SynchronizationImpl;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.EntityMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.TableMetaFactoryImpl;
import org.seasar.extension.jdbc.query.AutoBatchDeleteImpl;
import org.seasar.extension.jdbc.query.AutoBatchInsertImpl;
import org.seasar.extension.jdbc.query.AutoBatchUpdateImpl;
import org.seasar.extension.jdbc.query.AutoDeleteImpl;
import org.seasar.extension.jdbc.query.AutoFunctionCallImpl;
import org.seasar.extension.jdbc.query.AutoInsertImpl;
import org.seasar.extension.jdbc.query.AutoProcedureCallImpl;
import org.seasar.extension.jdbc.query.AutoSelectImpl;
import org.seasar.extension.jdbc.query.AutoUpdateImpl;
import org.seasar.extension.jdbc.query.SqlBatchUpdateImpl;
import org.seasar.extension.jdbc.query.SqlFileBatchUpdateImpl;
import org.seasar.extension.jdbc.query.SqlFileFunctionCallImpl;
import org.seasar.extension.jdbc.query.SqlFileProcedureCallImpl;
import org.seasar.extension.jdbc.query.SqlFileSelectImpl;
import org.seasar.extension.jdbc.query.SqlFileUpdateImpl;
import org.seasar.extension.jdbc.query.SqlFunctionCallImpl;
import org.seasar.extension.jdbc.query.SqlProcedureCallImpl;
import org.seasar.extension.jdbc.query.SqlSelectImpl;
import org.seasar.extension.jdbc.query.SqlUpdateImpl;
import org.seasar.extension.jta.TransactionImpl;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.mock.sql.MockDataSource;

/**
 * @author higa
 * 
 */
public class JdbcManagerImplTest extends TestCase {

    private JdbcManagerImpl manager;

    private TransactionManager transactionManager;

    @Override
    protected void setUp() throws Exception {
        manager = new JdbcManagerImpl();
        manager.setDialect(new StandardDialect());
        transactionManager = new TransactionManagerImpl();
        manager.setSyncRegistry(new TransactionSynchronizationRegistryImpl(
                transactionManager));
        manager.setDataSource(new MockDataSource());

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
        manager = null;
    }

    /**
     * @throws Exception
     * 
     */
    public void testSelectBySql() throws Exception {
        String sql = "select * from aaa";
        SqlSelectImpl<Aaa> select = (SqlSelectImpl<Aaa>) manager.selectBySql(
                Aaa.class, sql);
        assertNotNull(select);
        assertSame(manager, select.getJdbcManager());
        assertEquals(Aaa.class, select.getBaseClass());
        assertEquals(sql, select.getSql());
    }

    /**
     * @throws Exception
     * 
     */
    public void testSelectBySql_parameters() throws Exception {
        String sql = "select * from aaa where id = ?";
        SqlSelectImpl<Aaa> query = (SqlSelectImpl<Aaa>) manager.selectBySql(
                Aaa.class, sql, 1);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(Aaa.class, query.getBaseClass());
        assertEquals(sql, query.getSql());
        Object[] vars = query.getParamValues();
        assertEquals(1, vars.length);
        assertEquals(1, vars[0]);
    }

    /**
     * @throws Exception
     * 
     */
    public void testUpdateBySql() throws Exception {
        String sql = "update aaa set name = ? where id = ?";
        SqlUpdateImpl query = (SqlUpdateImpl) manager.updateBySql(sql,
                String.class, Integer.class);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(sql, query.getExecutedSql());
    }

    /**
     * @throws Exception
     * 
     */
    public void testUpdateBatchBySql() throws Exception {
        String sql = "update aaa set name = ? where id = ?";
        SqlBatchUpdateImpl query = (SqlBatchUpdateImpl) manager
                .updateBatchBySql(sql, String.class, Integer.class);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(sql, query.getExecutedSql());
    }

    /**
     * @throws Exception
     * 
     */
    public void testSelectBySqlFile() throws Exception {
        String path = "select.sql";
        SqlFileSelectImpl<Aaa> query = (SqlFileSelectImpl<Aaa>) manager
                .selectBySqlFile(Aaa.class, path);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(Aaa.class, query.getBaseClass());
        assertEquals(path, query.getPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testUpdateBySqlFile() throws Exception {
        String path = "update.sql";
        SqlFileUpdateImpl query = (SqlFileUpdateImpl) manager.updateBySqlFile(
                path, 1);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(path, query.getPath());
    }

    /**
     * 
     * @throws Exception
     */
    public void testUpdateBatchBySqlFile() throws Exception {
        String path = "update.sql";
        SqlFileBatchUpdateImpl<String> query = (SqlFileBatchUpdateImpl<String>) manager
                .updateBatchBySqlFile(path, "foo", "bar");
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(path, query.getPath());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCall_procedure() throws Exception {
        manager.maxRows = 100;
        manager.fetchSize = 10;
        manager.queryTimeout = 5;
        AutoProcedureCallImpl query = (AutoProcedureCallImpl) manager.call(
                "myProc", 1);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(100, query.getMaxRows());
        assertEquals(10, query.getFetchSize());
        assertEquals(5, query.getQueryTimeout());
        assertEquals(1, query.getParameter());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCallBySql_procedure() throws Exception {
        manager.maxRows = 100;
        manager.fetchSize = 10;
        manager.queryTimeout = 5;
        String sql = "{call hoge(?)}";
        SqlProcedureCallImpl query = (SqlProcedureCallImpl) manager.callBySql(
                sql, 1);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(100, query.getMaxRows());
        assertEquals(10, query.getFetchSize());
        assertEquals(5, query.getQueryTimeout());
        assertEquals(sql, query.getExecutedSql());
        assertEquals(1, query.getParameter());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCallBySqlFile_procedure() throws Exception {
        manager.maxRows = 100;
        manager.fetchSize = 10;
        manager.queryTimeout = 5;
        String path = "call.sql";
        SqlFileProcedureCallImpl query = (SqlFileProcedureCallImpl) manager
                .callBySqlFile(path, 1);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(100, query.getMaxRows());
        assertEquals(10, query.getFetchSize());
        assertEquals(5, query.getQueryTimeout());
        assertEquals(path, query.getPath());
        assertEquals(1, query.getParameter());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCall_function() throws Exception {
        manager.maxRows = 100;
        manager.fetchSize = 10;
        manager.queryTimeout = 5;
        AutoFunctionCallImpl<String> query = (AutoFunctionCallImpl<String>) manager
                .call(String.class, "myFunc", 1);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(100, query.getMaxRows());
        assertEquals(10, query.getFetchSize());
        assertEquals(5, query.getQueryTimeout());
        assertEquals(1, query.getParameter());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCallBySql_function() throws Exception {
        manager.maxRows = 100;
        manager.fetchSize = 10;
        manager.queryTimeout = 5;
        String sql = "{? = call hoge(?)}";
        SqlFunctionCallImpl<String> query = (SqlFunctionCallImpl<String>) manager
                .callBySql(String.class, sql, 1);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(100, query.getMaxRows());
        assertEquals(10, query.getFetchSize());
        assertEquals(5, query.getQueryTimeout());
        assertEquals(sql, query.getExecutedSql());
        assertEquals(1, query.getParameter());
    }

    /**
     * @throws Exception
     * 
     */
    public void testCallBySqlFile_function() throws Exception {
        manager.maxRows = 100;
        manager.fetchSize = 10;
        manager.queryTimeout = 5;
        String path = "call.sql";
        SqlFileFunctionCallImpl<String> query = (SqlFileFunctionCallImpl<String>) manager
                .callBySqlFile(String.class, path, 1);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(100, query.getMaxRows());
        assertEquals(10, query.getFetchSize());
        assertEquals(5, query.getQueryTimeout());
        assertEquals(path, query.getPath());
        assertEquals(1, query.getParameter());
    }

    /**
     * @throws Exception
     */
    public void testFrom() throws Exception {
        AutoSelectImpl<Aaa> query = (AutoSelectImpl<Aaa>) manager
                .from(Aaa.class);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(Aaa.class, query.getBaseClass());
    }

    /**
     * @throws Exception
     */
    public void testAutoInsert() throws Exception {
        Eee eee = new Eee();
        AutoInsertImpl<Eee> query = (AutoInsertImpl<Eee>) manager.insert(eee);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertSame(eee, query.getEntity());
    }

    /**
     * @throws Exception
     */
    public void testAutoBatchInsert_array() throws Exception {
        Eee[] entities = new Eee[] { new Eee(1, "foo"), new Eee(2, "bar"),
                new Eee(3, "baz") };
        AutoBatchInsertImpl<Eee> query = (AutoBatchInsertImpl<Eee>) manager
                .insertBatch(entities);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(3, query.getEntities().size());
        assertSame(entities[0], query.getEntities().get(0));
        assertSame(entities[1], query.getEntities().get(1));
        assertSame(entities[2], query.getEntities().get(2));
    }

    /**
     * @throws Exception
     */
    public void testAutoBatchInsert_list() throws Exception {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchInsertImpl<Eee> query = (AutoBatchInsertImpl<Eee>) manager
                .insertBatch(entities);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertSame(entities, query.getEntities());
    }

    /**
     * @throws Exception
     * 
     */
    public void testAutoUpdate() throws Exception {
        Eee eee = new Eee();
        AutoUpdateImpl<Eee> query = (AutoUpdateImpl<Eee>) manager.update(eee);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertSame(eee, query.getEntity());

        try {
            manager.update(new Iii());
            fail();
        } catch (NoIdPropertyRuntimeException expected) {
            System.out.println(expected);
        }
    }

    /**
     * @throws Exception
     */
    public void testAutoBatchUpdate_array() throws Exception {
        Eee[] entities = new Eee[] { new Eee(1, "foo"), new Eee(2, "bar"),
                new Eee(3, "baz") };
        AutoBatchUpdateImpl<Eee> query = (AutoBatchUpdateImpl<Eee>) manager
                .updateBatch(entities);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(3, query.getEntities().size());
        assertSame(entities[0], query.getEntities().get(0));
        assertSame(entities[1], query.getEntities().get(1));
        assertSame(entities[2], query.getEntities().get(2));

        try {
            manager.updateBatch(new Iii[] { new Iii() });
            fail();
        } catch (NoIdPropertyRuntimeException expected) {
            System.out.println(expected);
        }
    }

    /**
     * @throws Exception
     */
    public void testAutoBatchUpdate_list() throws Exception {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchUpdateImpl<Eee> query = (AutoBatchUpdateImpl<Eee>) manager
                .updateBatch(entities);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertSame(entities, query.getEntities());

        try {
            manager.updateBatch(Arrays.asList(new Iii()));
            fail();
        } catch (NoIdPropertyRuntimeException expected) {
            System.out.println(expected);
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testAutoDelete() throws Exception {
        Eee eee = new Eee();
        AutoDeleteImpl<Eee> query = (AutoDeleteImpl<Eee>) manager.delete(eee);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertSame(eee, query.getEntity());

        try {
            manager.delete(new Iii());
            fail();
        } catch (NoIdPropertyRuntimeException expected) {
            System.out.println(expected);
        }
    }

    /**
     * @throws Exception
     */
    public void testAutoBatchDelete_array() throws Exception {
        Eee[] entities = new Eee[] { new Eee(1, "foo"), new Eee(2, "bar"),
                new Eee(3, "baz") };
        AutoBatchDeleteImpl<Eee> query = (AutoBatchDeleteImpl<Eee>) manager
                .deleteBatch(entities);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(3, query.getEntities().size());
        assertSame(entities[0], query.getEntities().get(0));
        assertSame(entities[1], query.getEntities().get(1));
        assertSame(entities[2], query.getEntities().get(2));

        try {
            manager.deleteBatch(new Iii[] { new Iii() });
            fail();
        } catch (NoIdPropertyRuntimeException expected) {
            System.out.println(expected);
        }
    }

    /**
     * @throws Exception
     */
    public void testAutoBatchDelete_list() throws Exception {
        List<Eee> entities = Arrays.asList(new Eee(1, "foo"),
                new Eee(2, "bar"), new Eee(3, "baz"));
        AutoBatchDeleteImpl<Eee> query = (AutoBatchDeleteImpl<Eee>) manager
                .deleteBatch(entities);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertSame(entities, query.getEntities());

        try {
            manager.deleteBatch(Arrays.asList(new Iii()));
            fail();
        } catch (NoIdPropertyRuntimeException expected) {
            System.out.println(expected);
        }
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetJdbcContext_tx() throws Exception {
        transactionManager.begin();
        JdbcContext ctx = manager.getJdbcContext();
        assertNotNull(ctx);
        assertTrue(ctx.isTransactional());
        assertSame(ctx, manager.getJdbcContext());
        TransactionImpl tx = (TransactionImpl) transactionManager
                .getTransaction();
        assertEquals(1, tx.getInterposedSynchronizations().size());
        SynchronizationImpl sync = SynchronizationImpl.class.cast(tx
                .getInterposedSynchronizations().get(0));
        assertSame(manager.getJdbcContext(), sync.context);
    }

    /**
     * 
     * @throws Exception
     */
    public void testGetJdbcContext_tx_selectableDataSource() throws Exception {
        DataSourceFactory dataSourceFactory = new MockDataSourceFactory();
        SelectableDataSourceProxy dataSource = new SelectableDataSourceProxy();
        dataSource.setDataSourceFactory(dataSourceFactory);
        manager.setDataSource(dataSource);
        manager.setDataSourceFactory(dataSourceFactory);

        transactionManager.begin();

        dataSourceFactory.setSelectableDataSourceName("hoge");
        JdbcContext hogeCtx = manager.getJdbcContext();
        assertNotNull(hogeCtx);
        assertTrue(hogeCtx.isTransactional());
        assertSame(hogeCtx, manager.getJdbcContext());
        assertEquals("hoge", manager.getSelectableDataSourceName());

        dataSourceFactory.setSelectableDataSourceName("foo");
        JdbcContext fooCtx = manager.getJdbcContext();
        assertNotNull(fooCtx);
        assertTrue(fooCtx.isTransactional());
        assertSame(fooCtx, manager.getJdbcContext());
        assertEquals("foo", manager.getSelectableDataSourceName());

        TransactionImpl tx = (TransactionImpl) transactionManager
                .getTransaction();
        assertEquals(2, tx.getInterposedSynchronizations().size());
        SynchronizationImpl sync = SynchronizationImpl.class.cast(tx
                .getInterposedSynchronizations().get(0));
        assertSame(hogeCtx, sync.context);
        sync = SynchronizationImpl.class.cast(tx
                .getInterposedSynchronizations().get(1));
        assertSame(fooCtx, sync.context);
    }

    /**
     * @throws Exception
     * 
     */
    public void testBeforeCompletion() throws Exception {
        transactionManager.begin();
        JdbcContextImpl ctx = (JdbcContextImpl) manager.getJdbcContext();
        ctx.getStatement();
        transactionManager.commit();
        assertTrue(ctx.isConnectionNull());
        assertTrue(ctx.isStatementNull());
        assertTrue(manager.isJdbcContextNull());
    }

    /**
     * @throws Exception
     * 
     */
    public void testGetJdbcContext_notx() throws Exception {
        JdbcContext ctx = manager.getJdbcContext();
        assertNotNull(ctx);
        assertFalse(ctx.isTransactional());
        assertNotSame(ctx, manager.getJdbcContext());
    }

    /**
     * 
     * @author taedium
     */
    public static class MockDataSourceFactory extends DataSourceFactoryImpl {

        @Override
        public DataSource getDataSource(String name) {
            return new MockDataSource();
        }
    }
}
