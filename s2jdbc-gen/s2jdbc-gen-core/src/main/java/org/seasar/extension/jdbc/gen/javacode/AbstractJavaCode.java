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
package org.seasar.extension.jdbc.gen.javacode;

import java.io.File;

import org.seasar.extension.jdbc.gen.JavaCode;
import org.seasar.framework.util.ClassUtil;

/**
 * @author taedium
 * 
 */
public abstract class AbstractJavaCode implements JavaCode {

    protected String packageName;

    protected String className;

    protected String templateName;

    public AbstractJavaCode(String className, String templateName) {
        this.packageName = ClassUtil.splitPackageAndShortClassName(className)[0];
        this.className = className;
        this.templateName = templateName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getTemplateName() {
        return templateName;
    }

    public File getPackageDir(File baseDir) {
        return new File(baseDir, packageName.replace('.', File.separatorChar));
    }

    public File getFile(File baseDir) {
        return new File(baseDir, className.replace('.', File.separatorChar)
                + EXT);
    }

    protected String getShortClassName(String className) {
        return ClassUtil.splitPackageAndShortClassName(className)[1];
    }

}
