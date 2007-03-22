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
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.StatementFactory;
import org.seasar.extension.jdbc.UpdateHandler;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.StatementUtil;

/**
 * @author higa
 * 
 */
public class BasicUpdateHandler extends BasicHandler implements UpdateHandler {

    private static Logger logger_ = Logger.getLogger(BasicUpdateHandler.class);

    public BasicUpdateHandler() {
    }

    public BasicUpdateHandler(DataSource dataSource, String sql) {
        super(dataSource, sql);
    }

    public BasicUpdateHandler(DataSource dataSource, String sql,
            StatementFactory statementFactory) {

        super(dataSource, sql, statementFactory);
    }

    /**
     * @see org.seasar.extension.jdbc.SelectHandler#execute(java.lang.Object[])
     */
    public int execute(Object[] args) throws SQLRuntimeException {
        return execute(args, getArgTypes(args));
    }

    public int execute(Object[] args, Class[] argTypes)
            throws SQLRuntimeException {
        Connection connection = getConnection();
        try {
            return execute(connection, args, argTypes);
        } finally {
            ConnectionUtil.close(connection);
        }
    }

    public int execute(Connection connection, Object[] args, Class[] argTypes) {
        if (logger_.isDebugEnabled()) {
            logger_.debug(getCompleteSql(args));
        }
        PreparedStatement ps = prepareStatement(connection);
        try {
            bindArgs(ps, args, argTypes);
            return PreparedStatementUtil.executeUpdate(ps);
        } finally {
            StatementUtil.close(ps);
        }
    }
}