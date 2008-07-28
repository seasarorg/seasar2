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
     * @param createTableDdlFileName
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setCreateTableDdlFileName(java.lang.String)
     */
    public void setCreateTableDdlFileName(String createTableDdlFileName) {
        command.setCreateTableDdlFileName(createTableDdlFileName);
    }

    /**
     * @param ddlFileEncoding
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setDdlFileEncoding(java.lang.String)
     */
    public void setDdlFileEncoding(String ddlFileEncoding) {
        command.setDdlFileEncoding(ddlFileEncoding);
    }

    /**
     * @param ddlVersionFileName
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setDdlVersionFileName(java.lang.String)
     */
    public void setDdlVersionFileName(String ddlVersionFileName) {
        command.setDdlVersionFileName(ddlVersionFileName);
    }

    /**
     * @param env
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setEnv(java.lang.String)
     */
    public void setEnv(String env) {
        command.setEnv(env);
    }

    /**
     * @param from
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setFrom(java.lang.Integer)
     */
    public void setFrom(Integer from) {
        command.setFrom(from);
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
     * @param migrationRootDir
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setMigrationRootDir(java.io.File)
     */
    public void setMigrationRootDir(File migrationRootDir) {
        command.setMigrationRootDir(migrationRootDir);
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
     * @param to
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setTo(java.lang.Integer)
     */
    public void setTo(Integer to) {
        command.setTo(to);
    }

    /**
     * @param versionNoPattern
     * @see org.seasar.extension.jdbc.gen.command.MigrateCommand#setVersionNoPattern(java.lang.String)
     */
    public void setVersionNoPattern(String versionNoPattern) {
        command.setVersionNoPattern(versionNoPattern);
    }

    @Override
    protected Command getCommand() {
        return command;
    }

}
