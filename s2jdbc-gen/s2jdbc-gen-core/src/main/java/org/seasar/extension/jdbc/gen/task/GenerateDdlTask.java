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
import org.seasar.extension.jdbc.gen.command.GenerateDdlCommand;

/**
 * 
 * @author taedium
 */
public class GenerateDdlTask extends AbstractTask {

    protected GenerateDdlCommand command = new GenerateDdlCommand();

    /**
     * インスタンスを構築します。
     */
    public GenerateDdlTask() {
    }

    public File getClasspathRootDir() {
        return command.getClasspathRootDir();
    }

    public String getConfigPath() {
        return command.getConfigPath();
    }

    public String getCreateConstraintSqlFileName() {
        return command.getCreateConstraintSqlFileName();
    }

    public String getCreateConstraintTemplateFileName() {
        return command.getCreateConstraintTemplateFileName();
    }

    public String getCreateSequenceSqlFileName() {
        return command.getCreateSequenceSqlFileName();
    }

    public String getCreateSequenceTemplateFileName() {
        return command.getCreateSequenceTemplateFileName();
    }

    public String getCreateTableSqlFileName() {
        return command.getCreateTableSqlFileName();
    }

    public String getCreateTableTemplateFileName() {
        return command.getCreateTableTemplateFileName();
    }

    public String getDropConstraintSqlFileName() {
        return command.getDropConstraintSqlFileName();
    }

    public String getDropConstraintTemplateFileName() {
        return command.getDropConstraintTemplateFileName();
    }

    public String getDropSequenceSqlFileName() {
        return command.getDropSequenceSqlFileName();
    }

    public String getDropSequenceTemplateFileName() {
        return command.getDropSequenceTemplateFileName();
    }

    public String getDropTableSqlFileName() {
        return command.getDropTableSqlFileName();
    }

    public String getDropTableTemplateFileName() {
        return command.getDropTableTemplateFileName();
    }

    public String getEntityPackageName() {
        return command.getEntityPackageName();
    }

    public String getJdbcManagerName() {
        return command.getJdbcManagerName();
    }

    public String getRootPackageName() {
        return command.getRootPackageName();
    }

    public File getSqlFileDestDir() {
        return command.getSqlFileDestDir();
    }

    public String getSqlFileEncoding() {
        return command.getSqlFileEncoding();
    }

    public File getTemplateFileDir() {
        return command.getTemplateFileDir();
    }

    public String getTemplateFileEncoding() {
        return command.getTemplateFileEncoding();
    }

    public void setClasspathRootDir(File classpathRootDir) {
        command.setClasspathRootDir(classpathRootDir);
    }

    public void setConfigPath(String configPath) {
        command.setConfigPath(configPath);
    }

    public void setCreateConstraintSqlFileName(
            String createConstraintSqlFileName) {
        command.setCreateConstraintSqlFileName(createConstraintSqlFileName);
    }

    public void setCreateConstraintTemplateFileName(
            String createConstraintTemplateFileName) {
        command
                .setCreateConstraintTemplateFileName(createConstraintTemplateFileName);
    }

    public void setCreateSequenceSqlFileName(String createSequenceSqlFileName) {
        command.setCreateSequenceSqlFileName(createSequenceSqlFileName);
    }

    public void setCreateSequenceTemplateFileName(
            String createSequenceTemplateFileName) {
        command
                .setCreateSequenceTemplateFileName(createSequenceTemplateFileName);
    }

    public void setCreateTableSqlFileName(String createTableSqlFileName) {
        command.setCreateTableSqlFileName(createTableSqlFileName);
    }

    public void setCreateTableTemplateFileName(
            String createTableTemplateFileName) {
        command.setCreateTableTemplateFileName(createTableTemplateFileName);
    }

    public void setDropConstraintSqlFileName(String dropConstraintSqlFileName) {
        command.setDropConstraintSqlFileName(dropConstraintSqlFileName);
    }

    public void setDropConstraintTemplateFileName(
            String dropConstraintTemplateFileName) {
        command
                .setDropConstraintTemplateFileName(dropConstraintTemplateFileName);
    }

    public void setDropSequenceSqlFileName(String dropSequenceSqlFileName) {
        command.setDropSequenceSqlFileName(dropSequenceSqlFileName);
    }

    public void setDropSequenceTemplateFileName(
            String dropSequenceTemplateFileName) {
        command.setDropSequenceTemplateFileName(dropSequenceTemplateFileName);
    }

    public void setDropTableSqlFileName(String dropTableSqlFileName) {
        command.setDropTableSqlFileName(dropTableSqlFileName);
    }

    public void setDropTableTemplateFileName(String dropTableTemplateFileName) {
        command.setDropTableTemplateFileName(dropTableTemplateFileName);
    }

    public void setEntityPackageName(String entityPackageName) {
        command.setEntityPackageName(entityPackageName);
    }

    public void setJdbcManagerName(String jdbcManagerName) {
        command.setJdbcManagerName(jdbcManagerName);
    }

    public void setRootPackageName(String rootPackageName) {
        command.setRootPackageName(rootPackageName);
    }

    public void setSqlFileDestDir(File sqlFileDestDir) {
        command.setSqlFileDestDir(sqlFileDestDir);
    }

    public void setSqlFileEncoding(String sqlFileEncoding) {
        command.setSqlFileEncoding(sqlFileEncoding);
    }

    public void setTemplateFileDir(File templateFileDir) {
        command.setTemplateFileDir(templateFileDir);
    }

    public void setTemplateFileEncoding(String templateFileEncoding) {
        command.setTemplateFileEncoding(templateFileEncoding);
    }

    @Override
    protected Command getCommand() {
        return command;
    }

}
