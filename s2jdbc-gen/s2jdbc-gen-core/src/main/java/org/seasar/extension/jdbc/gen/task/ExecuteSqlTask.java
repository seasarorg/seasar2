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
package org.seasar.extension.jdbc.gen.task;

import java.io.File;

import org.apache.tools.ant.types.FileList;
import org.seasar.extension.jdbc.gen.Command;
import org.seasar.extension.jdbc.gen.command.ExecuteSqlCommand;

/**
 * @author taedium
 * 
 */
public class ExecuteSqlTask extends AbstractTask {

    protected ExecuteSqlCommand command = new ExecuteSqlCommand();

    protected FileList sqlFileList;

    public void addConfiguredSqlFileList(FileList sqlFileList) {
        File dir = sqlFileList.getDir(getProject());
        for (String fileName : sqlFileList.getFiles(getProject())) {
            File file = new File(dir, fileName);
            command.getSqlFileList().add(file);
        }
    }

    /**
     * @param blockDelimiter
     * @see org.seasar.extension.jdbc.gen.command.ExecuteSqlCommand#setBlockDelimiter(java.lang.String)
     */
    public void setBlockDelimiter(String blockDelimiter) {
        command.setBlockDelimiter(blockDelimiter);
    }

    /**
     * @param configPath
     * @see org.seasar.extension.jdbc.gen.command.ExecuteSqlCommand#setConfigPath(java.lang.String)
     */
    public void setConfigPath(String configPath) {
        command.setConfigPath(configPath);
    }

    /**
     * @param statementDelimiter
     * @see org.seasar.extension.jdbc.gen.command.ExecuteSqlCommand#setDelimiter(char)
     */
    public void setStatementDelimiter(char statementDelimiter) {
        command.setStatementDelimiter(statementDelimiter);
    }

    /**
     * @param haltOnError
     * @see org.seasar.extension.jdbc.gen.command.ExecuteSqlCommand#setHaltOnError(boolean)
     */
    public void setHaltOnError(boolean haltOnError) {
        command.setHaltOnError(haltOnError);
    }

    /**
     * @param jdbcManagerName
     * @see org.seasar.extension.jdbc.gen.command.ExecuteSqlCommand#setJdbcManagerName(java.lang.String)
     */
    public void setJdbcManagerName(String jdbcManagerName) {
        command.setJdbcManagerName(jdbcManagerName);
    }

    /**
     * @param sqlFileEncoding
     * @see org.seasar.extension.jdbc.gen.command.ExecuteSqlCommand#setSqlFileEncoding(java.lang.String)
     */
    public void setSqlFileEncoding(String sqlFileEncoding) {
        command.setSqlFileEncoding(sqlFileEncoding);
    }

    /**
     * @param transactional
     * @see org.seasar.extension.jdbc.gen.command.ExecuteSqlCommand#setTransactional(boolean)
     */
    public void setTransactional(boolean transactional) {
        command.setTransactional(transactional);
    }

    @Override
    protected Command getCommand() {
        return command;
    }

}
