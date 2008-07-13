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
import java.util.List;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;
import org.seasar.extension.jdbc.gen.Command;
import org.seasar.extension.jdbc.gen.command.ExecuteSqlCommand;

/**
 * @author taedium
 * 
 */
public class ExecuteSqlTask extends AbstractTask {

    protected ExecuteSqlCommand command = new ExecuteSqlCommand();

    /**
     * @return
     * @see org.seasar.extension.jdbc.gen.command.ExecuteSqlCommand#getConfigPath()
     */
    public String getConfigPath() {
        return command.getConfigPath();
    }

    /**
     * @return
     * @see org.seasar.extension.jdbc.gen.command.ExecuteSqlCommand#getDelimiter()
     */
    public char getDelimiter() {
        return command.getDelimiter();
    }

    /**
     * @return
     * @see org.seasar.extension.jdbc.gen.command.ExecuteSqlCommand#getJdbcManagerName()
     */
    public String getJdbcManagerName() {
        return command.getJdbcManagerName();
    }

    /**
     * @return
     * @see org.seasar.extension.jdbc.gen.command.ExecuteSqlCommand#getSqlFileEncoding()
     */
    public String getSqlFileEncoding() {
        return command.getSqlFileEncoding();
    }

    /**
     * @return
     * @see org.seasar.extension.jdbc.gen.command.ExecuteSqlCommand#getSqlFileList()
     */
    public List<File> getSqlFileList() {
        return command.getSqlFileList();
    }

    /**
     * @return
     * @see org.seasar.extension.jdbc.gen.command.ExecuteSqlCommand#isHaltOnError()
     */
    public boolean isHaltOnError() {
        return command.isHaltOnError();
    }

    /**
     * @param configPath
     * @see org.seasar.extension.jdbc.gen.command.ExecuteSqlCommand#setConfigPath(java.lang.String)
     */
    public void setConfigPath(String configPath) {
        command.setConfigPath(configPath);
    }

    /**
     * @param delimiter
     * @see org.seasar.extension.jdbc.gen.command.ExecuteSqlCommand#setDelimiter(char)
     */
    public void setDelimiter(char delimiter) {
        command.setDelimiter(delimiter);
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

    public void addConfiguredFileSet(FileSet fileSet) {
        DirectoryScanner ds = fileSet.getDirectoryScanner(getProject());
        File baseDir = ds.getBasedir();
        for (String fileName : ds.getIncludedFiles()) {
            File file = new File(baseDir, fileName);
            command.getSqlFileList().add(file);
        }
    }

    protected FileSet fileSet;

    @Override
    protected Command getCommand() {
        return command;
    }

}
