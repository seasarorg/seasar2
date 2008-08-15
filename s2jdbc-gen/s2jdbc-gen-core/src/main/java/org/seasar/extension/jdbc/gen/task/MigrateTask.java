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
import org.seasar.extension.jdbc.gen.command.MigrateCommand;

/**
 * @author taedium
 * 
 */
public class MigrateTask extends AbstractTask {

    /** コマンド */
    protected MigrateCommand command = new MigrateCommand();

    /**
     * インスタンスを構築します。
     */
    public MigrateTask() {
    }

    /**
     * @param blockDelimiter
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setBlockDelimiter(java.lang.String)
     */
    public void setBlockDelimiter(String blockDelimiter) {
        command.setBlockDelimiter(blockDelimiter);
    }

    /**
     * @param configPath
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setConfigPath(java.lang.String)
     */
    public void setConfigPath(String configPath) {
        command.setConfigPath(configPath);
    }

    /**
     * @param ddlFileEncoding
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setDdlFileEncoding(java.lang.String)
     */
    public void setDdlFileEncoding(String ddlFileEncoding) {
        command.setDdlFileEncoding(ddlFileEncoding);
    }

    /**
     * @param ddlInfoFile
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setDdlVersionFileName(java.lang.String)
     */
    public void setDdlInfoFile(File ddlInfoFile) {
        command.setDdlInfoFile(ddlInfoFile);
    }

    /**
     * @param env
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setEnv(java.lang.String)
     */
    public void setEnv(String env) {
        command.setEnv(env);
    }

    /**
     * @param haltOnError
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setHaltOnError(boolean)
     */
    public void setHaltOnError(boolean haltOnError) {
        command.setHaltOnError(haltOnError);
    }

    /**
     * @param jdbcManagerName
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setJdbcManagerName(java.lang.String)
     */
    public void setJdbcManagerName(String jdbcManagerName) {
        command.setJdbcManagerName(jdbcManagerName);
    }

    /**
     * @param migrateDir
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setMigrateDir(java.io.File)
     */
    public void setMigrateDir(File migrateDir) {
        command.setMigrateDir(migrateDir);
    }

    /**
     * @param schemaInfoColumnName
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setSchemaInfoColumnName(java.lang.String)
     */
    public void setSchemaInfoColumnName(String schemaInfoColumnName) {
        command.setSchemaInfoColumnName(schemaInfoColumnName);
    }

    /**
     * @param schemaInfoFullTableName
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setSchemaInfoFullTableName(java.lang.String)
     */
    public void setSchemaInfoFullTableName(String schemaInfoFullTableName) {
        command.setSchemaInfoFullTableName(schemaInfoFullTableName);
    }

    /**
     * @param statementDelimiter
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setStatementDelimiter(char)
     */
    public void setStatementDelimiter(char statementDelimiter) {
        command.setStatementDelimiter(statementDelimiter);
    }

    /**
     * @param version
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setTo(java.lang.Integer)
     */
    public void setVersion(String version) {
        command.setVersion(version);
    }

    /**
     * @param versionNoPattern
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setVersionNoPattern(java.lang.String)
     */
    public void setVersionNoPattern(String versionNoPattern) {
        command.setVersionNoPattern(versionNoPattern);
    }

    /**
     * @param classpathDir
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setClasspathDir(java.io.File)
     */
    public void setClasspathDir(File classpathDir) {
        command.setClasspathDir(classpathDir);
    }

    /**
     * @param dumpFileEncoding
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setDumpFileEncoding(java.lang.String)
     */
    public void setDumpFileEncoding(String dumpFileEncoding) {
        command.setDumpFileEncoding(dumpFileEncoding);
    }

    /**
     * @param entityNamePattern
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setEntityNamePattern(java.lang.String)
     */
    public void setEntityNamePattern(String entityNamePattern) {
        command.setEntityNamePattern(entityNamePattern);
    }

    /**
     * @param entityPackageName
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setEntityPackageName(java.lang.String)
     */
    public void setEntityPackageName(String entityPackageName) {
        command.setEntityPackageName(entityPackageName);
    }

    /**
     * @param ignoreEntityNamePattern
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setIgnoreEntityNamePattern(java.lang.String)
     */
    public void setIgnoreEntityNamePattern(String ignoreEntityNamePattern) {
        command.setIgnoreEntityNamePattern(ignoreEntityNamePattern);
    }

    /**
     * @param rootPackageName
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setRootPackageName(java.lang.String)
     */
    public void setRootPackageName(String rootPackageName) {
        command.setRootPackageName(rootPackageName);
    }

    @Override
    protected Command getCommand() {
        return command;
    }

}
