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
import org.seasar.extension.jdbc.gen.command.DdlGenCommand;

/**
 * データベースのスキーマを作成するタスクです。
 * 
 * @author taedium
 */
public class DdlGenTask extends AbstractTask {

    protected File classpathRootDir;

    protected File destDir;

    protected String ddlFileEncoding;

    protected String createTableDdlName;

    protected String createConstraintDdlName;

    protected String createSequenceDdlName;

    protected String dropTableDdlName;

    protected String dropConstraintDdlName;

    protected String dropSequenceDdlName;

    protected String createTableTemplateName;

    protected String createConstraintTemplateName;

    protected String createSequenceTemplateName;

    protected String dropTableTemplateName;

    protected String dropConstraintTemplateName;

    protected String dropSequenceTemplateName;

    /**
     * インスタンスを構築します。
     */
    public DdlGenTask() {
        this(DdlGenCommand.class.getName());
    }

    /**
     * インスタンスを構築します。
     * 
     * @param commandClassName
     *            コマンドクラス名
     */
    public DdlGenTask(String commandClassName) {
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

    public String getDdlFileEncoding() {
        return ddlFileEncoding;
    }

    public void setDdlFileEncoding(String ddlFileEncoding) {
        this.ddlFileEncoding = ddlFileEncoding;
    }

    public String getCreateTableDdlName() {
        return createTableDdlName;
    }

    public void setCreateTableDdlName(String createTableDdlName) {
        this.createTableDdlName = createTableDdlName;
    }

    public String getCreateConstraintDdlName() {
        return createConstraintDdlName;
    }

    public void setCreateConstraintDdlName(String createConstraintDdlName) {
        this.createConstraintDdlName = createConstraintDdlName;
    }

    public String getCreateSequenceDdlName() {
        return createSequenceDdlName;
    }

    public void setCreateSequenceDdlName(String createSequenceDdlName) {
        this.createSequenceDdlName = createSequenceDdlName;
    }

    public String getDropTableDdlName() {
        return dropTableDdlName;
    }

    public void setDropTableDdlName(String dropTableDdlName) {
        this.dropTableDdlName = dropTableDdlName;
    }

    public String getDropConstraintDdlName() {
        return dropConstraintDdlName;
    }

    public void setDropConstraintDdlName(String dropConstraintDdlName) {
        this.dropConstraintDdlName = dropConstraintDdlName;
    }

    public String getDropSequenceDdlName() {
        return dropSequenceDdlName;
    }

    public void setDropSequenceDdlName(String dropSequenceDdlName) {
        this.dropSequenceDdlName = dropSequenceDdlName;
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
