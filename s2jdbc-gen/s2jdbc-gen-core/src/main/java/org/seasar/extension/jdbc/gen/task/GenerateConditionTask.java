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
import org.seasar.extension.jdbc.gen.command.GenerateConditionCommand;

/**
 * 
 * @author taedium
 */
public class GenerateConditionTask extends AbstractTask {

    protected GenerateConditionCommand command = new GenerateConditionCommand();

    public GenerateConditionTask() {
    }

    public File getClasspathRootDir() {
        return command.getClasspathRootDir();
    }

    public String getConditionClassNameSuffix() {
        return command.getConditionClassNameSuffix();
    }

    public String getConditionPackageName() {
        return command.getConditionPackageName();
    }

    public String getConditionTemplateFileName() {
        return command.getConditionTemplateFileName();
    }

    public String getConfigPath() {
        return command.getConfigPath();
    }

    public String getEntityPackageName() {
        return command.getEntityPackageName();
    }

    public File getJavaFileDir() {
        return command.getJavaFileDir();
    }

    public String getJavaFileEncoding() {
        return command.getJavaFileEncoding();
    }

    public String getJdbcManagerName() {
        return command.getJdbcManagerName();
    }

    public String getRootPackageName() {
        return command.getRootPackageName();
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

    public void setConditionClassNameSuffix(String conditionClassNameSuffix) {
        command.setConditionClassNameSuffix(conditionClassNameSuffix);
    }

    public void setConditionPackageName(String conditionPackageName) {
        command.setConditionPackageName(conditionPackageName);
    }

    public void setConditionTemplateFileName(String conditionTemplateFileName) {
        command.setConditionTemplateFileName(conditionTemplateFileName);
    }

    public void setConfigPath(String configPath) {
        command.setConfigPath(configPath);
    }

    public void setEntityPackageName(String entityPackageName) {
        command.setEntityPackageName(entityPackageName);
    }

    public void setJavaFileDir(File javaFileDir) {
        command.setJavaFileDir(javaFileDir);
    }

    public void setJavaFileEncoding(String javaFileEncoding) {
        command.setJavaFileEncoding(javaFileEncoding);
    }

    public void setJdbcManagerName(String jdbcManagerName) {
        command.setJdbcManagerName(jdbcManagerName);
    }

    public void setRootPackageName(String rootPackageName) {
        command.setRootPackageName(rootPackageName);
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
