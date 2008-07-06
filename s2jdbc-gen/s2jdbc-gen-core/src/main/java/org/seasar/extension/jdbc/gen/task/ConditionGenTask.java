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
import org.seasar.extension.jdbc.gen.command.ConditionGenCommand;

/**
 * S2JDBC用のエンティティを生成するタスクです。
 * 
 * @author taedium
 */
public class ConditionGenTask extends AbstractTask {

    protected File classpathRootDir;

    /** 生成するJavaファイルの出力先ディレクトリ */
    protected File destDir = new File("src/main/java");

    /** Javaファイルのエンコーディング */
    protected String javaFileEncoding = "UTF-8";

    /** 条件クラス名のサフィックス */
    protected String conditionClassNameSuffix = "Condition";

    /** 条件クラスのパッケージ名 */
    protected String conditionPackageName = "condition";

    /** 条件クラスのテンプレート名 */
    protected String conditionTemplateName = "condition.ftl";

    /**
     * インスタンスを構築します。
     */
    public ConditionGenTask() {
        this(ConditionGenCommand.class.getName());
    }

    /**
     * インスタンスを構築します。
     * 
     * @param commandClassName
     *            コマンドクラス名
     */
    public ConditionGenTask(String commandClassName) {
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

    public String getJavaFileEncoding() {
        return javaFileEncoding;
    }

    public void setJavaFileEncoding(String javaFileEncoding) {
        this.javaFileEncoding = javaFileEncoding;
    }

    public String getConditionClassNameSuffix() {
        return conditionClassNameSuffix;
    }

    public void setConditionClassNameSuffix(String conditionClassNameSuffix) {
        this.conditionClassNameSuffix = conditionClassNameSuffix;
    }

    public String getConditionPackageName() {
        return conditionPackageName;
    }

    public void setConditionPackageName(String conditionPackageName) {
        this.conditionPackageName = conditionPackageName;
    }

    public String getConditionTemplateName() {
        return conditionTemplateName;
    }

    public void setConditionTemplateName(String conditionTemplateName) {
        this.conditionTemplateName = conditionTemplateName;
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
