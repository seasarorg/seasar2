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

/**
 * @author taedium
 * 
 */
public class GenEntitiesTask extends GenTask {

    protected String diconName;

    protected String jdbcManagerName;

    protected String rootPackageName;

    protected String entityPackageName;

    protected String entityBasePackageName;

    protected String baseClassNameSuffix;

    protected File templateDir;

    protected String templateEncoding;

    protected File destDir;

    protected String javaCodeEncoding;

    protected String schemaName;

    protected String versionColumnName;

    /**
     * インスタンスを構築します。
     */
    public GenEntitiesTask() {
        super("org.seasar.extension.jdbc.gen.command.GenEntitiesCommand");
    }

    public void setDiconName(String diconName) {
        this.diconName = diconName;
    }

    public void setJdbcManagerName(String jdbcManagerName) {
        this.jdbcManagerName = jdbcManagerName;
    }

    public void setRootPackageName(String rootPackageName) {
        this.rootPackageName = rootPackageName;
    }

    public void setEntityPackageName(String entityPackageName) {
        this.entityPackageName = entityPackageName;
    }

    public void setEntityBasePackageName(String entityBasePackageName) {
        this.entityBasePackageName = entityBasePackageName;
    }

    public void setBaseClassNameSuffix(String baseClassNameSuffix) {
        this.baseClassNameSuffix = baseClassNameSuffix;
    }

    public void setTemplateDir(File templateDir) {
        this.templateDir = templateDir;
    }

    public void setTemplateEncoding(String templateEncoding) {
        this.templateEncoding = templateEncoding;
    }

    public void setDestDir(File destDir) {
        this.destDir = destDir;
    }

    public void setJavaCodeEncoding(String javaCodeEncoding) {
        this.javaCodeEncoding = javaCodeEncoding;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public void setVersionColumnName(String versionColumnName) {
        this.versionColumnName = versionColumnName;
    }

}
