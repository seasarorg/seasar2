package org.seasar.extension.jdbc.manager;

import javax.transaction.TransactionManager;

import junit.framework.TestCase;

import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.entity.Aaa;
import org.seasar.extension.jdbc.query.AutoSelectImpl;
import org.seasar.extension.jdbc.query.SqlFileSelectImpl;
import org.seasar.extension.jdbc.query.SqlSelectImpl;
import org.seasar.extension.jdbc.query.SqlUpdateImpl;
import org.seasar.extension.jta.TransactionImpl;
import org.seasar.extension.jta.TransactionManagerImpl;
import org.seasar.extension.jta.TransactionSynchronizationRegistryImpl;
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
        transactionManager = new TransactionManagerImpl();
        manager.setSyncRegistry(new TransactionSynchronizationRegistryImpl(
                transactionManager));
        manager.setDataSource(new MockDataSource());
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
        Object[] vars = query.getBindVariables();
        assertEquals(1, vars.length);
        assertEquals(1, vars[0]);
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
    public void testUpdateBySql() throws Exception {
        String sql = "update aaa set name = ? where id = ?";
        SqlUpdateImpl query = (SqlUpdateImpl) manager.updateBySql(sql,
                String.class, Integer.class);
        assertNotNull(query);
        assertSame(manager, query.getJdbcManager());
        assertEquals(sql, query.getExecutedSql());
        Class<?>[] classes = query.getBindVariableClasses();
        assertEquals(2, classes.length);
        assertEquals(String.class, classes[0]);
        assertEquals(Integer.class, classes[1]);
    }

    /**
     * @throws Exception
     * 
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
        assertSame(manager, tx.getInterposedSynchronizations().get(0));
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
}
