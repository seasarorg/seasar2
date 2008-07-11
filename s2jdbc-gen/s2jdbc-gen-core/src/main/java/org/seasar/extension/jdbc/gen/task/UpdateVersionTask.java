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
import org.seasar.extension.jdbc.gen.command.UpdateVersionCommand;

/**
 * @author taedium
 * 
 */
public class UpdateVersionTask extends AbstractTask {

    protected UpdateVersionCommand command = new UpdateVersionCommand();

    /**
     * インスタンスを構築します。
     */
    public UpdateVersionTask() {
    }

    public String getSchemaInfoColumnName() {
        return command.getSchemaInfoColumnName();
    }

    public String getSchemaInfoTableName() {
        return command.getSchemaInfoTableName();
    }

    public File getSqlFileDestDir() {
        return command.getSqlFileDestDir();
    }

    public String getSqlFileEncoding() {
        return command.getSqlFileEncoding();
    }

    public String getTemplateFileEncoding() {
        return command.getTemplateFileEncoding();
    }

    public File getTemplateFileSecondaryDir() {
        return command.getTemplateFileSecondaryDir();
    }

    public String getUpdateVersionSqlFileName() {
        return command.getUpdateVersionSqlFileName();
    }

    public String getUpdateVersionTemplateFileName() {
        return command.getUpdateVersionTemplateFileName();
    }

    public String getVersionFileName() {
        return command.getVersionFileName();
    }

    public String getVersionTemplateFileName() {
        return command.getVersionTemplateFileName();
    }

    public void setSchemaInfoColumnName(String schemaInfoColumnName) {
        command.setSchemaInfoColumnName(schemaInfoColumnName);
    }

    public void setSchemaInfoTableName(String schemaInfoTableName) {
        command.setSchemaInfoTableName(schemaInfoTableName);
    }

    public void setSqlFileDestDir(File sqlFileDestDir) {
        command.setSqlFileDestDir(sqlFileDestDir);
    }

    public void setSqlFileEncoding(String sqlFileEncoding) {
        command.setSqlFileEncoding(sqlFileEncoding);
    }

    public void setTemplateFileEncoding(String templateFileEncoding) {
        command.setTemplateFileEncoding(templateFileEncoding);
    }

    public void setTemplateFileSecondaryDir(File templateFileSecondaryDir) {
        command.setTemplateFileSecondaryDir(templateFileSecondaryDir);
    }

    public void setUpdateVersionSqlFileName(String updateVersionSqlFileName) {
        command.setUpdateVersionSqlFileName(updateVersionSqlFileName);
    }

    public void setUpdateVersionTemplateFileName(
            String updateVersionTemplateFileName) {
        command.setUpdateVersionTemplateFileName(updateVersionTemplateFileName);
    }

    public void setVersionFileName(String versionFileName) {
        command.setVersionFileName(versionFileName);
    }

    public void setVersionTemplateFileName(String versionTemplateFileName) {
        command.setVersionTemplateFileName(versionTemplateFileName);
    }

    @Override
    protected Command getCommand() {
        return command;
    }

}
