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

import org.apache.tools.ant.BuildException;
import org.seasar.extension.jdbc.gen.command.GenerateDdlCommand;

/**
 * データベースのスキーマを作成するタスクです。
 * 
 * @author taedium
 */
public class GenerateDdlTask extends AbstractTask {

    protected File classpathRootDir;

    protected File destDir;

    protected String sqlFileEncoding;

    protected String createTableSqlFileName;

    protected String createConstraintSqlFileName;

    protected String createSequenceSqlFileName;

    protected String dropTableSqlFileName;

    protected String dropConstraintSqlFileName;

    protected String dropSequenceSqlFileName;

    protected String createTableTemplateName;

    protected String createConstraintTemplateName;

    protected String createSequenceTemplateName;

    protected String dropTableTemplateName;

    protected String dropConstraintTemplateName;

    protected String dropSequenceTemplateName;

    /**
     * インスタンスを構築します。
     */
    public GenerateDdlTask() {
        this(GenerateDdlCommand.class.getName());
    }

    /**
     * インスタンスを構築します。
     * 
     * @param commandClassName
     *            コマンドクラス名
     */
    public GenerateDdlTask(String commandClassName) {
        super(commandClassName);
    }

    public File getClasspathRootDir() {
        return classpathRootDir;
    }

    public void setClasspathRootDir(File classpathRootDir) {
        this.classpathRootDir = classpathRootDir;
    }

    public File getDestDir() {
        return destDir;
    }

    public void setDestDir(File destDir) {
        this.destDir = destDir;
    }

    public String getSqlFileEncoding() {
        return sqlFileEncoding;
    }

    public void setSqlFileEncoding(String sqlFileEncoding) {
        this.sqlFileEncoding = sqlFileEncoding;
    }

    public String getCreateTableSqlFileName() {
        return createTableSqlFileName;
    }

    public void setCreateTableSqlFileName(String createTableSqlFileName) {
        this.createTableSqlFileName = createTableSqlFileName;
    }

    public String getCreateConstraintSqlFileName() {
        return createConstraintSqlFileName;
    }

    public void setCreateConstraintSqlFileName(
            String createConstraintSqlFileName) {
        this.createConstraintSqlFileName = createConstraintSqlFileName;
    }

    public String getCreateSequenceSqlFileName() {
        return createSequenceSqlFileName;
    }

    public void setCreateSequenceSqlFileName(String createSequenceSqlFileName) {
        this.createSequenceSqlFileName = createSequenceSqlFileName;
    }

    public String getDropTableSqlFileName() {
        return dropTableSqlFileName;
    }

    public void setDropTableSqlFileName(String dropTableSqlFileName) {
        this.dropTableSqlFileName = dropTableSqlFileName;
    }

    public String getDropConstraintSqlFileName() {
        return dropConstraintSqlFileName;
    }

    public void setDropConstraintSqlFileName(String dropConstraintSqlFileName) {
        this.dropConstraintSqlFileName = dropConstraintSqlFileName;
    }

    public String getDropSequenceSqlFileName() {
        return dropSequenceSqlFileName;
    }

    public void setDropSequenceSqlFileName(String dropSequenceSqlFileName) {
        this.dropSequenceSqlFileName = dropSequenceSqlFileName;
    }

    public String getCreateTableTemplateName() {
        return createTableTemplateName;
    }

    public void setCreateTableTemplateName(String createTableTemplateName) {
        this.createTableTemplateName = createTableTemplateName;
    }

    public String getCreateConstraintTemplateName() {
        return createConstraintTemplateName;
    }

    public void setCreateConstraintTemplateName(
            String createConstraintTemplateName) {
        this.createConstraintTemplateName = createConstraintTemplateName;
    }

    public String getCreateSequenceTemplateName() {
        return createSequenceTemplateName;
    }

    public void setCreateSequenceTemplateName(String createSequenceTemplateName) {
        this.createSequenceTemplateName = createSequenceTemplateName;
    }

    public String getDropTableTemplateName() {
        return dropTableTemplateName;
    }

    public void setDropTableTemplateName(String dropTableTemplateName) {
        this.dropTableTemplateName = dropTableTemplateName;
    }

    public String getDropConstraintTemplateName() {
        return dropConstraintTemplateName;
    }

    public void setDropConstraintTemplateName(String dropConstraintTemplateName) {
        this.dropConstraintTemplateName = dropConstraintTemplateName;
    }

    public String getDropSequenceTemplateName() {
        return dropSequenceTemplateName;
    }

    public void setDropSequenceTemplateName(String dropSequenceTemplateName) {
        this.dropSequenceTemplateName = dropSequenceTemplateName;
    }

    @Override
    protected void validate() {
        super.validate();
        if (classpathRootDir == null) {
            throw new BuildException("classpathRootDir is not specified for '"
                    + getTaskName() + "' task");
        }
    }
}
