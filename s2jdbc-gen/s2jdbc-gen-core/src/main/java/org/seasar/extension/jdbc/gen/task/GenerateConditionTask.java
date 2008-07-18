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

    public void setJavaFileDir(File javaFileDestDir) {
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
     * @see org.seasar.extension.jdbc.gen.command.GenerateConditionCommand#setOverwrite(boolean)
     */
    public void setOverwrite(boolean overwrite) {
        command.setOverwrite(overwrite);
    }

    public void setRootPackageName(String rootPackageName) {
        command.setRootPackageName(rootPackageName);
    }

    public void setTemplateFileEncoding(String templateFileEncoding) {
        command.setTemplateFileEncoding(templateFileEncoding);
    }

    public void setTemplateFilePrimaryDir(File templateFilePrimaryDir) {
        command.setTemplateFilePrimaryDir(templateFilePrimaryDir);
    }

    @Override
    protected Command getCommand() {
        return command;
    }

}
