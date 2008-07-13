/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.command;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.SqlExecutor;
import org.seasar.extension.jdbc.gen.SqlScriptReader;
import org.seasar.extension.jdbc.gen.SqlScriptTokenizer;
import org.seasar.extension.jdbc.gen.dialect.GenDialectManager;
import org.seasar.extension.jdbc.gen.exception.RequiredPropertyEmptyRuntimeException;
import org.seasar.extension.jdbc.gen.exception.SqlFailedException;
import org.seasar.extension.jdbc.gen.sql.SqlExecutionContextImpl;
import org.seasar.extension.jdbc.gen.sql.SqlExecutorImpl;
import org.seasar.extension.jdbc.gen.sql.SqlScriptReaderImpl;
import org.seasar.extension.jdbc.gen.sql.SqlScriptTokenizerImpl;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.log.Logger;

/**
 * @author taedium
 * 
 */
public class ExecuteSqlCommand extends AbstractCommand {

    protected Logger logger = Logger.getLogger(ExecuteSqlCommand.class);

    protected String configPath = "s2jdbc.dicon";

    protected char delimiter = ';';

    protected boolean haltOnError = false;

    protected String jdbcManagerName = "jdbcManager";

    protected String sqlFileEncoding = "UTF-8";

    protected List<File> sqlFileList = new ArrayList<File>();

    protected S2ContainerFactorySupport containerFactorySupport;

    protected DataSource dataSource;

    protected GenDialect dialect;

    protected UserTransaction userTransaction;

    protected SqlScriptTokenizer sqlScriptTokenizer;

    protected SqlExecutor sqlExecutor;

    public ExecuteSqlCommand() {
    }

    /**
     * @return Returns the configPath.
     */
    public String getConfigPath() {
        return configPath;
    }

    /**
     * @param configPath
     *            The configPath to set.
     */
    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    /**
     * @return Returns the delimiter.
     */
    public char getDelimiter() {
        return delimiter;
    }

    /**
     * @param delimiter
     *            The delimiter to set.
     */
    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * @return Returns the haltOnError.
     */
    public boolean isHaltOnError() {
        return haltOnError;
    }

    /**
     * @param haltOnError
     *            The haltOnError to set.
     */
    public void setHaltOnError(boolean haltOnError) {
        this.haltOnError = haltOnError;
    }

    /**
     * @return Returns the jdbcManagerName.
     */
    public String getJdbcManagerName() {
        return jdbcManagerName;
    }

    /**
     * @param jdbcManagerName
     *            The jdbcManagerName to set.
     */
    public void setJdbcManagerName(String jdbcManagerName) {
        this.jdbcManagerName = jdbcManagerName;
    }

    /**
     * @return Returns the sqlFileEncoding.
     */
    public String getSqlFileEncoding() {
        return sqlFileEncoding;
    }

    /**
     * @param sqlFileEncoding
     *            The sqlFileEncoding to set.
     */
    public void setSqlFileEncoding(String sqlFileEncoding) {
        this.sqlFileEncoding = sqlFileEncoding;
    }

    /**
     * @return Returns the sqlFileList.
     */
    public List<File> getSqlFileList() {
        return sqlFileList;
    }

    /**
     * @param sqlFileList
     *            The sqlFileList to set.
     */
    public void setSqlFileList(List<File> sqlFileList) {
        this.sqlFileList = sqlFileList;
    }

    @Override
    protected void doValidate() {
        if (sqlFileList.isEmpty()) {
            throw new RequiredPropertyEmptyRuntimeException("sqlFileList");
        }
    }

    @Override
    protected void doInit() {
        containerFactorySupport = new S2ContainerFactorySupport(configPath);
        containerFactorySupport.init();

        JdbcManagerImplementor jdbcManager = SingletonS2Container
                .getComponent(jdbcManagerName);
        dataSource = jdbcManager.getDataSource();
        dialect = GenDialectManager.getGenDialect(jdbcManager.getDialect());
        userTransaction = SingletonS2Container
                .getComponent(UserTransaction.class);

        sqlScriptTokenizer = createScriptTokenizer();
        sqlExecutor = createSqlExecutor();
    }

    @Override
    protected void doExecute() throws Throwable {
        LinkedList<SqlFailedException> exceptionList = new LinkedList<SqlFailedException>();
        try {
            userTransaction.begin();
            SqlExecutionContext context = createSqlExecutionContext();
            try {
                executeSqlFileList(context);
            } finally {
                exceptionList.addAll(context.getExceptionList());
                context.destroy();
            }
            if (exceptionList.isEmpty()) {
                userTransaction.commit();
            } else {
                userTransaction.rollback();
            }
        } catch (Throwable t) {
            try {
                userTransaction.rollback();
            } catch (Throwable th) {
                logger.log(th);
            }
            throw t;
        } finally {
            if (!exceptionList.isEmpty()) {
                for (SqlFailedException e : exceptionList) {
                    logger.error(e.getMessage());
                }
                throw exceptionList.getFirst();
            }
        }
    }

    protected void executeSqlFileList(SqlExecutionContext context) {
        for (File sqlFile : sqlFileList) {
            SqlScriptReader reader = createSqlScriptReader(sqlFile);
            try {
                for (String sql = reader.readSql(); sql != null; sql = reader
                        .readSql()) {
                    context.setSqlFile(sqlFile);
                    context.setSql(sql);
                    sqlExecutor.execute(context);
                }
            } finally {
                reader.close();
            }
        }
    }

    @Override
    protected void doDestroy() {
        if (containerFactorySupport != null) {
            containerFactorySupport.destory();
        }
    }

    protected SqlExecutor createSqlExecutor() {
        return new SqlExecutorImpl(haltOnError);
    }

    protected SqlScriptReader createSqlScriptReader(File sqlFile) {
        return new SqlScriptReaderImpl(sqlFile, sqlFileEncoding,
                sqlScriptTokenizer);
    }

    protected SqlScriptTokenizer createScriptTokenizer() {
        return new SqlScriptTokenizerImpl(dialect, delimiter);
    }

    protected SqlExecutionContext createSqlExecutionContext() {
        return new SqlExecutionContextImpl(DataSourceUtil
                .getConnection(dataSource));
    }

}
