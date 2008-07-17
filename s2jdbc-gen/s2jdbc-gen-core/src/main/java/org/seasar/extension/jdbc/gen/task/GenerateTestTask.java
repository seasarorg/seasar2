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
import org.seasar.extension.jdbc.gen.command.GenerateTestCommand;

/**
 * @author taedium
 * 
 */
public class GenerateTestTask extends AbstractTask {

    protected GenerateTestCommand command = new GenerateTestCommand();

    /**
     * @param classpathRootDir
     * @see org.seasar.extension.jdbc.gen.command.GenerateTestCommand#setClasspathRootDir(java.io.File)
     */
    public void setClasspathRootDir(File classpathRootDir) {
        command.setClasspathRootDir(classpathRootDir);
    }

    /**
     * @param configPath
     * @see org.seasar.extension.jdbc.gen.command.GenerateTestCommand#setConfigPath(java.lang.String)
     */
    public void setConfigPath(String configPath) {
        command.setConfigPath(configPath);
    }

    /**
     * @param entityPackageName
     * @see org.seasar.extension.jdbc.gen.command.GenerateTestCommand#setEntityPackageName(java.lang.String)
     */
    public void setEntityPackageName(String entityPackageName) {
        command.setEntityPackageName(entityPackageName);
    }

    /**
     * @param testClassNameSuffix
     * @see org.seasar.extension.jdbc.gen.command.GenerateTestCommand#setTestClassNameSuffix(java.lang.String)
     */
    public void setTestClassNameSuffix(String testClassNameSuffix) {
        command.setTestClassNameSuffix(testClassNameSuffix);
    }

    /**
     * @param testTemplateFileName
     * @see org.seasar.extension.jdbc.gen.command.GenerateTestCommand#setTestTemplateFileName(java.lang.String)
     */
    public void setTestTemplateFileName(String testTemplateFileName) {
        command.setTestTemplateFileName(testTemplateFileName);
    }

    /**
     * @param javaFileEncoding
     * @see org.seasar.extension.jdbc.gen.command.GenerateTestCommand#setJavaFileEncoding(java.lang.String)
     */
    public void setJavaFileEncoding(String javaFileEncoding) {
        command.setJavaFileEncoding(javaFileEncoding);
    }

    /**
     * @param testJavaFileDestDir
     * @see org.seasar.extension.jdbc.gen.command.GenerateTestCommand#setTestJavaFileDestDir(java.io.File)
     */
    public void setTestJavaFileDestDir(File testJavaFileDestDir) {
        command.setTestJavaFileDestDir(testJavaFileDestDir);
    }

    /**
     * @param jdbcManagerName
     * @see org.seasar.extension.jdbc.gen.command.GenerateTestCommand#setJdbcManagerName(java.lang.String)
     */
    public void setJdbcManagerName(String jdbcManagerName) {
        command.setJdbcManagerName(jdbcManagerName);
    }

    /**
     * @param overwrite
     * @see org.seasar.extension.jdbc.gen.command.GenerateTestCommand#setOverwrite(boolean)
     */
    public void setOverwrite(boolean overwrite) {
        command.setOverwrite(overwrite);
    }

    /**
     * @param rootPackageName
     * @see org.seasar.extension.jdbc.gen.command.GenerateTestCommand#setRootPackageName(java.lang.String)
     */
    public void setRootPackageName(String rootPackageName) {
        command.setRootPackageName(rootPackageName);
    }

    /**
     * @param templateFileEncoding
     * @see org.seasar.extension.jdbc.gen.command.GenerateTestCommand#setTemplateFileEncoding(java.lang.String)
     */
    public void setTemplateFileEncoding(String templateFileEncoding) {
        command.setTemplateFileEncoding(templateFileEncoding);
    }

    /**
     * @param templateFilePrimaryDir
     * @see org.seasar.extension.jdbc.gen.command.GenerateTestCommand#setTemplateFilePrimaryDir(java.io.File)
     */
    public void setTemplateFilePrimaryDir(File templateFilePrimaryDir) {
        command.setTemplateFilePrimaryDir(templateFilePrimaryDir);
    }

    @Override
    protected Command getCommand() {
        return command;
    }

}
