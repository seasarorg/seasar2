/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.autoregister;

import java.io.File;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import org.seasar.framework.exception.ResourceNotFoundRuntimeException;
import org.seasar.framework.util.ClassTraversal;
import org.seasar.framework.util.JarFileUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;

/**
 * jarファイルの中にあるコンポーネントを自動登録するための抽象クラスです。
 * 
 * @author koichik
 * @author higa
 */
public abstract class AbstractJarComponentAutoRegister extends
        AbstractComponentAutoRegister {

    private String baseDir;

    private Pattern[] jarFileNamePatterns;

    /**
     * ベースとなるディレクトリを返します。
     * 
     * @return
     */
    public String getBaseDir() {
        return baseDir;
    }

    /**
     * ベースとなるディレクトリを設定します。
     * 
     * @param baseDir
     */
    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public void registerAll() {
        if (baseDir == null) {
            setupBaseDir();
        }
        File dir = new File(baseDir);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new ResourceNotFoundRuntimeException(baseDir);
        }
        String[] jars = dir.list();
        for (int i = 0; i < jars.length; ++i) {
            if (!isAppliedJar(jars[i])) {
                continue;
            }
            final JarFile jarFile = JarFileUtil.create(findJar(jars[i]));
            ClassTraversal.forEach(jarFile, this);
        }
    }

    /**
     * ベースディレクトリを設定します。
     */
    protected abstract void setupBaseDir();

    /**
     * jarファイルが適用されるかどうか返します。
     * 
     * @param jarFileName
     * @return jarファイルが適用されるかどうか
     */
    protected boolean isAppliedJar(final String jarFileName) {
        if (jarFileNamePatterns == null) {
            return true;
        }
        String extention = ResourceUtil.getExtension(jarFileName);
        if (extention == null || !extention.equalsIgnoreCase("jar")) {
            return false;
        }
        String name = ResourceUtil.removeExtension(jarFileName);
        for (int i = 0; i < jarFileNamePatterns.length; ++i) {
            if (jarFileNamePatterns[i].matcher(name).matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * jarファイルを検索します。
     * 
     * @param jarFileName
     * @return jarファイル
     */
    protected File findJar(final String jarFileName) {
        return new File(baseDir, jarFileName);
    }

    /**
     * jarファイル名を設定します。複数設定する場合は、','で区切ります。
     * 
     * @param jarFileNames
     */
    public void setJarFileNames(String jarFileNames) {
        String[] array = StringUtil.split(jarFileNames, ",");
        jarFileNamePatterns = new Pattern[array.length];
        for (int i = 0; i < array.length; ++i) {
            String s = array[i].trim();
            jarFileNamePatterns[i] = Pattern.compile(s);
        }
    }
}
