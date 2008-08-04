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

import org.seasar.extension.jdbc.gen.Command;
import org.seasar.extension.jdbc.gen.command.LoadDataCommand;

/**
 * @author taedium
 * 
 */
public class LoadDataTask extends AbstractTask {

    /** コマンド */
    protected LoadDataCommand command = new LoadDataCommand();

    /**
     * @param classpathDir
     * @see org.seasar.extension.jdbc.gen.command.LoadDataCommand#setClasspathDir(java.io.File)
     */
    public void setClasspathDir(File classpathDir) {
        command.setClasspathDir(classpathDir);
    }

    /**
     * @param configPath
     * @see org.seasar.extension.jdbc.gen.command.LoadDataCommand#setConfigPath(java.lang.String)
     */
    public void setConfigPath(String configPath) {
        command.setConfigPath(configPath);
    }

    /**
     * @param dumpDir
     * @see org.seasar.extension.jdbc.gen.command.LoadDataCommand#setDumpDir(java.io.File)
     */
    public void setDumpDir(File dumpDir) {
        command.setDumpDir(dumpDir);
    }

    /**
     * @param dumpFileEncoding
     * @see org.seasar.extension.jdbc.gen.command.LoadDataCommand#setDumpFileEncoding(java.lang.String)
     */
    public void setDumpFileEncoding(String dumpFileEncoding) {
        command.setDumpFileEncoding(dumpFileEncoding);
    }

    /**
     * @param entityNamePattern
     * @see org.seasar.extension.jdbc.gen.command.LoadDataCommand#setEntityNamePattern(java.lang.String)
     */
    public void setEntityNamePattern(String entityNamePattern) {
        command.setEntityNamePattern(entityNamePattern);
    }

    /**
     * @param entityPackageName
     * @see org.seasar.extension.jdbc.gen.command.LoadDataCommand#setEntityPackageName(java.lang.String)
     */
    public void setEntityPackageName(String entityPackageName) {
        command.setEntityPackageName(entityPackageName);
    }

    /**
     * @param env
     * @see org.seasar.extension.jdbc.gen.command.LoadDataCommand#setEnv(java.lang.String)
     */
    public void setEnv(String env) {
        command.setEnv(env);
    }

    /**
     * @param ignoreEntityNamePattern
     * @see org.seasar.extension.jdbc.gen.command.LoadDataCommand#setIgnoreEntityNamePattern(java.lang.String)
     */
    public void setIgnoreEntityNamePattern(String ignoreEntityNamePattern) {
        command.setIgnoreEntityNamePattern(ignoreEntityNamePattern);
    }

    /**
     * @param jdbcManagerName
     * @see org.seasar.extension.jdbc.gen.command.LoadDataCommand#setJdbcManagerName(java.lang.String)
     */
    public void setJdbcManagerName(String jdbcManagerName) {
        command.setJdbcManagerName(jdbcManagerName);
    }

    /**
     * @param rootPackageName
     * @see org.seasar.extension.jdbc.gen.command.LoadDataCommand#setRootPackageName(java.lang.String)
     */
    public void setRootPackageName(String rootPackageName) {
        command.setRootPackageName(rootPackageName);
    }

    @Override
    protected Command getCommand() {
        return command;
    }
}
