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
package org.seasar.extension.jdbc.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.ResultSetFactory;
import org.seasar.extension.jdbc.ResultSetHandler;
import org.seasar.extension.jdbc.SelectHandler;
import org.seasar.extension.jdbc.StatementFactory;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ResultSetUtil;
import org.seasar.framework.util.StatementUtil;

/**
 * @author higa
 * 
 */
public class BasicSelectHandler extends BasicHandler implements SelectHandler {

    private static Logger logger_ = Logger.getLogger(BasicSelectHandler.class);

    private ResultSetFactory resultSetFactory_ = BasicResultSetFactory.INSTANCE;

    private ResultSetHandler resultSetHandler_;

    private int fetchSize_ = 100;

    private int maxRows_ = -1;

    public BasicSelectHandler() {
    }

    public BasicSelectHandler(DataSource dataSource, String sql,
            ResultSetHandler resultSetHandler) {

        this(dataSource, sql, resultSetHandler, BasicStatementFactory.INSTANCE,
                BasicResultSetFactory.INSTANCE);
    }

    public BasicSelectHandler(DataSource dataSource, String sql,
            ResultSetHandler resultSetHandler,
            StatementFactory statementFactory, ResultSetFactory resultSetFactory) {

        setDataSource(dataSource);
        setSql(sql);
        setResultSetHandler(resultSetHandler);
        setStatementFactory(statementFactory);
        setResultSetFactory(resultSetFactory);
    }

    public ResultSetFactory getResultSetFactory() {
        return resultSetFactory_;
    }

    public void setResultSetFactory(ResultSetFactory resultSetFactory) {
        resultSetFactory_ = resultSetFactory;
    }

    public ResultSetHandler getResultSetHandler() {
        return resultSetHandler_;
    }

    public void setResultSetHandler(ResultSetHandler resultSetHandler) {
        resultSetHandler_ = resultSetHandler;
    }

    public int getFetchSize() {
        return fetchSize_;
    }

    public void setFetchSize(int fetchSize) {
        fetchSize_ = fetchSize;
    }

    public int getMaxRows() {
        return maxRows_;
    }

    public void setMaxRows(int maxRows) {
        maxRows_ = maxRows;
    }

    /**
     * @see org.seasar.extension.jdbc.SelectHandler#execute(java.lang.Object[])
     */
    public Object execute(Object[] args) throws SQLRuntimeException {
        return execute(args, getArgTypes(args));
    }

    public Object execute(Object[] args, Class[] argTypes)
            throws SQLRuntimeException {
        Connection con = getConnection();
        try {
            return execute(con, args, argTypes);
        } finally {
            ConnectionUtil.close(con);
        }
    }

    public Object execute(Connection connection, Object[] args, Class[] argTypes)
            throws SQLRuntimeException {
        if (logger_.isDebugEnabled()) {
            logger_.debug(getCompleteSql(args));
        }
        PreparedStatement ps = null;
        try {
            ps = prepareStatement(connection);
            bindArgs(ps, args, argTypes);
            return execute(ps);
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        } finally {
            StatementUtil.close(ps);
        }
    }

    protected Object[] setup(Connection con, Object[] args) {
        return args;
    }

    protected PreparedStatement prepareStatement(Connection connection) {
        PreparedStatement ps = super.prepareStatement(connection);
        if (fetchSize_ > -1) {
            StatementUtil.setFetchSize(ps, fetchSize_);
        }
        if (maxRows_ > -1) {
            StatementUtil.setMaxRows(ps, maxRows_);
        }
        return ps;
    }

    protected Object execute(PreparedStatement ps) throws SQLException {
        if (resultSetHandler_ == null) {
            throw new EmptyRuntimeException("resultSetHandler");
        }
        ResultSet resultSet = null;
        try {
            resultSet = createResultSet(ps);
            return resultSetHandler_.handle(resultSet);
        } finally {
            ResultSetUtil.close(resultSet);
        }
    }

    protected void setupDatabaseMetaData(DatabaseMetaData dbMetaData) {
    }

    protected ResultSet createResultSet(PreparedStatement ps) {
        return resultSetFactory_.createResultSet(ps);
    }
}