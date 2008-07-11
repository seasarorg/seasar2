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
import org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand;

/**
 * @author taedium
 * 
 */
public class GenerateEntityTestTask extends AbstractTask {

    protected GenerateEntityTestCommand command = new GenerateEntityTestCommand();

    /**
     * @return
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#getClasspathRootDir()
     */
    public File getClasspathRootDir() {
        return command.getClasspathRootDir();
    }

    /**
     * @return
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#getConfigPath()
     */
    public String getConfigPath() {
        return command.getConfigPath();
    }

    /**
     * @return
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#getEntityPackageName()
     */
    public String getEntityPackageName() {
        return command.getEntityPackageName();
    }

    /**
     * @return
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#getEntityTestClassNameSuffix()
     */
    public String getEntityTestClassNameSuffix() {
        return command.getEntityTestClassNameSuffix();
    }

    /**
     * @return
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#getEntityTestTemplateFileName()
     */
    public String getEntityTestTemplateFileName() {
        return command.getEntityTestTemplateFileName();
    }

    /**
     * @return
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#getJavaFileEncoding()
     */
    public String getJavaFileEncoding() {
        return command.getJavaFileEncoding();
    }

    /**
     * @return
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#getJavaTestFileDestDir()
     */
    public File getJavaTestFileDestDir() {
        return command.getJavaTestFileDestDir();
    }

    /**
     * @return
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#getJdbcManagerName()
     */
    public String getJdbcManagerName() {
        return command.getJdbcManagerName();
    }

    /**
     * @return
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#isOverwrite()
     */
    public boolean isOverwrite() {
        return command.isOverwrite();
    }

    /**
     * @return
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#getRootPackageName()
     */
    public String getRootPackageName() {
        return command.getRootPackageName();
    }

    /**
     * @return
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#getTemplateFileEncoding()
     */
    public String getTemplateFileEncoding() {
        return command.getTemplateFileEncoding();
    }

    /**
     * @return
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#getTemplateFileSecondaryDir()
     */
    public File getTemplateFilePrimaryDir() {
        return command.getTemplateFilePrimaryDir();
    }

    /**
     * @param classpathRootDir
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#setClasspathRootDir(java.io.File)
     */
    public void setClasspathRootDir(File classpathRootDir) {
        command.setClasspathRootDir(classpathRootDir);
    }

    /**
     * @param configPath
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#setConfigPath(java.lang.String)
     */
    public void setConfigPath(String configPath) {
        command.setConfigPath(configPath);
    }

    /**
     * @param entityPackageName
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#setEntityPackageName(java.lang.String)
     */
    public void setEntityPackageName(String entityPackageName) {
        command.setEntityPackageName(entityPackageName);
    }

    /**
     * @param entityTestClassNameSuffix
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#setEntityTestClassNameSuffix(java.lang.String)
     */
    public void setEntityTestClassNameSuffix(String entityTestClassNameSuffix) {
        command.setEntityTestClassNameSuffix(entityTestClassNameSuffix);
    }

    /**
     * @param entityTestTemplateFileName
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#setEntityTestTemplateFileName(java.lang.String)
     */
    public void setEntityTestTemplateFileName(String entityTestTemplateFileName) {
        command.setEntityTestTemplateFileName(entityTestTemplateFileName);
    }

    /**
     * @param javaFileEncoding
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#setJavaFileEncoding(java.lang.String)
     */
    public void setJavaFileEncoding(String javaFileEncoding) {
        command.setJavaFileEncoding(javaFileEncoding);
    }

    /**
     * @param javaTestFileDestDir
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#setJavaTestFileDestDir(java.io.File)
     */
    public void setJavaTestFileDestDir(File javaTestFileDestDir) {
        command.setJavaTestFileDestDir(javaTestFileDestDir);
    }

    /**
     * @param jdbcManagerName
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#setJdbcManagerName(java.lang.String)
     */
    public void setJdbcManagerName(String jdbcManagerName) {
        command.setJdbcManagerName(jdbcManagerName);
    }

    /**
     * @param overwrite
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#setOverwrite(boolean)
     */
    public void setOverwrite(boolean overwrite) {
        command.setOverwrite(overwrite);
    }

    /**
     * @param rootPackageName
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#setRootPackageName(java.lang.String)
     */
    public void setRootPackageName(String rootPackageName) {
        command.setRootPackageName(rootPackageName);
    }

    /**
     * @param templateFileEncoding
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#setTemplateFileEncoding(java.lang.String)
     */
    public void setTemplateFileEncoding(String templateFileEncoding) {
        command.setTemplateFileEncoding(templateFileEncoding);
    }

    /**
     * @param templateFileSecondaryDir
     * @see org.seasar.extension.jdbc.gen.command.GenerateEntityTestCommand#setTemplateFileSecondaryDir(java.io.File)
     */
    public void setTemplateFilePrimaryDir(File templateFilePrimaryDir) {
        command.setTemplateFilePrimaryDir(templateFilePrimaryDir);
    }

    @Override
    protected Command getCommand() {
        return command;
    }

}
