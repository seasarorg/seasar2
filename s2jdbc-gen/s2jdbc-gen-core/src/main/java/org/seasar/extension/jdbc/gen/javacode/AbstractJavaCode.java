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
 * {@link JavaCode}の抽象クラスです。
 * 
 * @author taedium
 */
public abstract class AbstractJavaCode implements JavaCode {

    /** クラス名 */
    protected String className;

    /** パッケージ名 */
    protected String packageName;

    /** パッケージ名を除いたクラス名 */
    protected String shortClassName;

    /** テンプレート名 */
    protected String templateName;

    /**
     * インスタンスを構築します。
     * 
     * @param className
     *            クラス名
     * @param templateName
     *            テンプレート名
     */
    public AbstractJavaCode(String className, String templateName) {
        this.className = className;
        String[] elements = ClassUtil.splitPackageAndShortClassName(className);
        this.packageName = elements[0];
        this.shortClassName = elements[1];
        this.templateName = templateName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getShortClassName() {
        return shortClassName;
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

}
