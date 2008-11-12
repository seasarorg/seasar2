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
package org.seasar.extension.jdbc.it.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.seasar.extension.jdbc.it.util.dialect.PostgreDialect;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.env.Env;

/**
 * @author taedium
 * 
 */
public class ExecuteSqlTask extends Task {

    protected static Map<String, Dialect> dialectMap =
        new HashMap<String, Dialect>();
    static {
        dialectMap.put("postgre", new PostgreDialect());
    }

    protected String envFilePath = "env_ut.txt";

    protected String configPath = "s2jdbc.dicon";

    protected boolean haltOnError = true;

    protected String sqlFileEncoding = "UTF-8";

    /** SQLステートメントの区切り文字 */
    protected char statementDelimiter = ';';

    /** SQLブロックの区切り文字 */
    protected String blockDelimiter = null;

    protected File sqlFile = null;

    /**
     * @param envFilePath
     *            The envFilePath to set.
     */
    public void setEnvFilePath(String envFilePath) {
        this.envFilePath = envFilePath;
    }

    /**
     * @param configPath
     *            The configPath to set.
     */
    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    /**
     * @param haltOnError
     *            The haltOnError to set.
     */
    public void setHaltOnError(boolean haltOnError) {
        this.haltOnError = haltOnError;
    }

    /**
     * @param sqlFileEncoding
     *            The sqlFileEncoding to set.
     */
    public void setSqlFileEncoding(String sqlFileEncoding) {
        this.sqlFileEncoding = sqlFileEncoding;
    }

    /**
     * @param statementDelimiter
     *            The statementDelimiter to set.
     */
    public void setStatementDelimiter(char statementDelimiter) {
        this.statementDelimiter = statementDelimiter;
    }

    /**
     * @param blockDelimiter
     *            The blockDelimiter to set.
     */
    public void setBlockDelimiter(String blockDelimiter) {
        this.blockDelimiter = blockDelimiter;
    }

    /**
     * @param sqlFile
     *            The sqlFile to set.
     */
    public void setSqlFile(File sqlFile) {
        this.sqlFile = sqlFile;
    }

    @Override
    public void execute() throws BuildException {
        if (sqlFile == null) {
            throw new BuildException("sqlFile not specified.");
        }

        ClassLoader original = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(
                getClass().getClassLoader());
            Env.setFilePath(envFilePath);
            SingletonS2ContainerFactory.setConfigPath(configPath);
            SingletonS2ContainerFactory.init();
            executeSql();
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }

    protected void executeSql() {
        String env = Env.getValue();
        Dialect dialect = dialectMap.get(env);
        if (dialect == null) {
            throw new BuildException("Dialect not found for env value(" + env
                + ").");
        }

        SqlFileExecutor executor =
            new SqlFileExecutor(
                dialect,
                sqlFileEncoding,
                statementDelimiter,
                blockDelimiter);

        S2Container container = SingletonS2ContainerFactory.getContainer();
        DataSource dataSource =
            (DataSource) container.getComponent(DataSource.class);
        SqlExecutionContext context =
            new SqlExecutionContext(dataSource, haltOnError);

        executor.execute(context, sqlFile);
    }
}