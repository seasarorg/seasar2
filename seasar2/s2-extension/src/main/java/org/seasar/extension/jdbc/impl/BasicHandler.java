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
import java.sql.SQLException;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.SqlLog;
import org.seasar.extension.jdbc.SqlLogRegistry;
import org.seasar.extension.jdbc.SqlLogRegistryLocator;
import org.seasar.extension.jdbc.StatementFactory;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.extension.jdbc.util.BindVariableUtil;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.log.Logger;

/**
 * @author higa
 * 
 */
public class BasicHandler {

    private DataSource dataSource_;

    private String sql_;

    private StatementFactory statementFactory_ = BasicStatementFactory.INSTANCE;

    protected Class loggerClass = BasicHandler.class;

    public BasicHandler() {
    }

    public BasicHandler(DataSource ds, String sql) {
        this(ds, sql, BasicStatementFactory.INSTANCE);
    }

    public BasicHandler(DataSource ds, String sql,
            StatementFactory statementFactory) {

        setDataSource(ds);
        setSql(sql);
        setStatementFactory(statementFactory);
    }

    public DataSource getDataSource() {
        return dataSource_;
    }

    public void setDataSource(DataSource dataSource) {
        dataSource_ = dataSource;
    }

    public String getSql() {
        return sql_;
    }

    public void setSql(String sql) {
        sql_ = sql;
    }

    public StatementFactory getStatementFactory() {
        return statementFactory_;
    }

    public void setStatementFactory(StatementFactory statementFactory) {
        statementFactory_ = statementFactory;
    }

    protected Connection getConnection() {
        if (dataSource_ == null) {
            throw new EmptyRuntimeException("dataSource");
        }
        return DataSourceUtil.getConnection(dataSource_);
    }

    protected PreparedStatement prepareStatement(Connection connection) {
        if (sql_ == null) {
            throw new EmptyRuntimeException("sql");
        }
        return statementFactory_.createPreparedStatement(connection, sql_);
    }

    protected void bindArgs(PreparedStatement ps, Object[] args,
            Class[] argTypes) {

        if (args == null) {
            return;
        }
        for (int i = 0; i < args.length; ++i) {
            ValueType valueType = getValueType(argTypes[i]);
            try {
                valueType.bindValue(ps, i + 1, args[i]);
            } catch (SQLException ex) {
                throw new SQLRuntimeException(ex);
            }
        }
    }

    protected Class[] getArgTypes(Object[] args) {
        if (args == null) {
            return null;
        }
        Class[] argTypes = new Class[args.length];
        for (int i = 0; i < args.length; ++i) {
            Object arg = args[i];
            if (arg != null) {
                argTypes[i] = arg.getClass();
            }
        }
        return argTypes;
    }

    protected String getCompleteSql(Object[] args) {
        if (args == null || args.length == 0) {
            return sql_;
        }
        StringBuffer buf = new StringBuffer(200);
        int pos = 0;
        int pos2 = 0;
        int pos3 = 0;
        int pos4 = 0;
        int pos5 = 0;
        int pos6 = 0;
        int index = 0;
        while (true) {
            pos = sql_.indexOf('?', pos2);
            pos3 = sql_.indexOf('\'', pos2);
            pos4 = sql_.indexOf('\'', pos3 + 1);
            pos5 = sql_.indexOf("/*", pos2);
            pos6 = sql_.indexOf("*/", pos5 + 1);
            if (pos > 0) {
                if (pos3 >= 0 && pos3 < pos && pos < pos4) {
                    buf.append(sql_.substring(pos2, pos4 + 1));
                    pos2 = pos4 + 1;
                } else if (pos5 >= 0 && pos5 < pos && pos < pos6) {
                    buf.append(sql_.substring(pos2, pos6 + 1));
                    pos2 = pos6 + 1;
                } else {
                    buf.append(sql_.substring(pos2, pos));
                    buf.append(getBindVariableText(args[index++]));
                    pos2 = pos + 1;
                }
            } else {
                buf.append(sql_.substring(pos2));
                break;
            }
        }
        return buf.toString();
    }

    protected String getBindVariableText(Object bindVariable) {
        return BindVariableUtil.getBindVariableText(bindVariable);
    }

    protected ValueType getValueType(Class clazz) {
        return ValueTypes.getValueType(clazz);
    }

    /**
     * SQLをログ出力します。
     * 
     * @param args
     *            SQLにバインドされる値の配列
     * @param argTypes
     *            SQLにバインドされる値の型の配列
     */
    protected void logSql(Object[] args, Object[] argTypes) {
        Logger logger = Logger.getLogger(loggerClass);
        SqlLogRegistry sqlLogRegistry = SqlLogRegistryLocator.getInstance();
        if (logger.isDebugEnabled() || sqlLogRegistry != null) {
            String completeSql = getCompleteSql(args);
            if (logger.isDebugEnabled()) {
                logger.debug(completeSql);
            }
            if (sqlLogRegistry != null) {
                SqlLog sqlLog = new SqlLogImpl(getSql(), completeSql, args,
                        argTypes);
                sqlLogRegistry.add(sqlLog);
            }
        }
    }

    public Class getLoggerClass() {
        return loggerClass;
    }

    public void setLoggerClass(Class loggerClass) {
        this.loggerClass = loggerClass;
    }
}