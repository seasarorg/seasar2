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
import org.seasar.extension.jdbc.gen.command.DumpDataCommand;

/**
 * @author taedium
 * 
 */
public class DumpDataTask extends AbstractTask {

    /** コマンド */
    protected DumpDataCommand command = new DumpDataCommand();

    /**
     * @param classpathDir
     * @see org.seasar.extension.jdbc.gen.command.DumpDataCommand#setClasspathDir(java.io.File)
     */
    public void setClasspathDir(File classpathDir) {
        command.setClasspathDir(classpathDir);
    }

    /**
     * @param configPath
     * @see org.seasar.extension.jdbc.gen.command.DumpDataCommand#setConfigPath(java.lang.String)
     */
    public void setConfigPath(String configPath) {
        command.setConfigPath(configPath);
    }

    /**
     * @param dumpDir
     * @see org.seasar.extension.jdbc.gen.command.DumpDataCommand#setDumpDir(java.io.File)
     */
    public void setDumpDir(File dumpDir) {
        command.setDumpDir(dumpDir);
    }

    /**
     * @param dumpFileEncoding
     * @see org.seasar.extension.jdbc.gen.command.DumpDataCommand#setDumpFileEncoding(java.lang.String)
     */
    public void setDumpFileEncoding(String dumpFileEncoding) {
        command.setDumpFileEncoding(dumpFileEncoding);
    }

    /**
     * @param dumpTemplateFileName
     * @see org.seasar.extension.jdbc.gen.command.DumpDataCommand#setDumpTemplateFileName(java.lang.String)
     */
    public void setDumpTemplateFileName(String dumpTemplateFileName) {
        command.setDumpTemplateFileName(dumpTemplateFileName);
    }

    /**
     * @param entityNamePattern
     * @see org.seasar.extension.jdbc.gen.command.DumpDataCommand#setEntityNamePattern(java.lang.String)
     */
    public void setEntityNamePattern(String entityNamePattern) {
        command.setEntityNamePattern(entityNamePattern);
    }

    /**
     * @param entityPackageName
     * @see org.seasar.extension.jdbc.gen.command.DumpDataCommand#setEntityPackageName(java.lang.String)
     */
    public void setEntityPackageName(String entityPackageName) {
        command.setEntityPackageName(entityPackageName);
    }

    /**
     * @param env
     * @see org.seasar.extension.jdbc.gen.command.DumpDataCommand#setEnv(java.lang.String)
     */
    public void setEnv(String env) {
        command.setEnv(env);
    }

    /**
     * @param ignoreEntityNamePattern
     * @see org.seasar.extension.jdbc.gen.command.DumpDataCommand#setIgnoreEntityNamePattern(java.lang.String)
     */
    public void setIgnoreEntityNamePattern(String ignoreEntityNamePattern) {
        command.setIgnoreEntityNamePattern(ignoreEntityNamePattern);
    }

    /**
     * @param jdbcManagerName
     * @see org.seasar.extension.jdbc.gen.command.DumpDataCommand#setJdbcManagerName(java.lang.String)
     */
    public void setJdbcManagerName(String jdbcManagerName) {
        command.setJdbcManagerName(jdbcManagerName);
    }

    /**
     * @param rootPackageName
     * @see org.seasar.extension.jdbc.gen.command.DumpDataCommand#setRootPackageName(java.lang.String)
     */
    public void setRootPackageName(String rootPackageName) {
        command.setRootPackageName(rootPackageName);
    }

    /**
     * @param templateFileEncoding
     * @see org.seasar.extension.jdbc.gen.command.DumpDataCommand#setTemplateFileEncoding(java.lang.String)
     */
    public void setTemplateFileEncoding(String templateFileEncoding) {
        command.setTemplateFileEncoding(templateFileEncoding);
    }

    /**
     * @param templateFilePrimaryDir
     * @see org.seasar.extension.jdbc.gen.command.DumpDataCommand#setTemplateFilePrimaryDir(java.io.File)
     */
    public void setTemplateFilePrimaryDir(File templateFilePrimaryDir) {
        command.setTemplateFilePrimaryDir(templateFilePrimaryDir);
    }

    @Override
    protected Command getCommand() {
        return command;
    }

}
