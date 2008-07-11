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
import org.seasar.extension.jdbc.gen.command.GenerateEntityCommand;

/**
 * 
 * @author taedium
 */
public class GenerateEntityTask extends AbstractTask {

    protected GenerateEntityCommand command = new GenerateEntityCommand();

    /**
     * インスタンスを構築します。
     */
    public GenerateEntityTask() {
    }

    public String getConfigPath() {
        return command.getConfigPath();
    }

    public String getEntityPackageName() {
        return command.getEntityPackageName();
    }

    public String getEntityTemplateFileName() {
        return command.getEntityTemplateFileName();
    }

    public File getJavaFileDestDir() {
        return command.getJavaFileDestDir();
    }

    public String getJavaFileEncoding() {
        return command.getJavaFileEncoding();
    }

    public String getJdbcManagerName() {
        return command.getJdbcManagerName();
    }

    /**
     * @return
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityCommand#isOverwrite()
     */
    public boolean isOverwrite() {
        return command.isOverwrite();
    }

    public String getRootPackageName() {
        return command.getRootPackageName();
    }

    public String getSchemaName() {
        return command.getSchemaName();
    }

    public String getTableNamePattern() {
        return command.getTableNamePattern();
    }

    public String getTemplateFileEncoding() {
        return command.getTemplateFileEncoding();
    }

    public File getTemplateFileSecondaryDir() {
        return command.getTemplateFileSecondaryDir();
    }

    public String getVersionColumnName() {
        return command.getVersionColumnName();
    }

    public void setConfigPath(String configPath) {
        command.setConfigPath(configPath);
    }

    public void setEntityPackageName(String entityPackageName) {
        command.setEntityPackageName(entityPackageName);
    }

    public void setEntityTemplateFileName(String entityTemplateFileName) {
        command.setEntityTemplateFileName(entityTemplateFileName);
    }

    public void setJavaFileDestDir(File javaFileDestDir) {
        command.setJavaFileDestDir(javaFileDestDir);
    }

    public void setJavaFileEncoding(String javaFileEncoding) {
        command.setJavaFileEncoding(javaFileEncoding);
    }

    public void setJdbcManagerName(String jdbcManagerName) {
        command.setJdbcManagerName(jdbcManagerName);
    }

    /**
     * @param overwrite
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityCommand#setOverwrite(boolean)
     */
    public void setOverwrite(boolean overwrite) {
        command.setOverwrite(overwrite);
    }

    public void setRootPackageName(String rootPackageName) {
        command.setRootPackageName(rootPackageName);
    }

    public void setSchemaName(String schemaName) {
        command.setSchemaName(schemaName);
    }

    public void setTableNamePattern(String tableNamePattern) {
        command.setTableNamePattern(tableNamePattern);
    }

    public void setTemplateFileEncoding(String templateFileEncoding) {
        command.setTemplateFileEncoding(templateFileEncoding);
    }

    public void setTemplateFileSecondaryDir(File templateFileSecondaryDir) {
        command.setTemplateFileSecondaryDir(templateFileSecondaryDir);
    }

    public void setVersionColumnName(String versionColumnName) {
        command.setVersionColumnName(versionColumnName);
    }

    @Override
    protected Command getCommand() {
        return command;
    }

}
