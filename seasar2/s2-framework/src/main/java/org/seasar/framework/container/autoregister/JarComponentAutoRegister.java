/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
import java.net.URL;

import org.aopalliance.intercept.MethodInterceptor;
import org.seasar.framework.util.FileUtil;
import org.seasar.framework.util.JarFileUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.ZipFileUtil;

/**
 * jarファイルの中にあるコンポーネントを自動登録するクラスです。
 * 
 * @author koichik
 * @author higa
 */
public class JarComponentAutoRegister extends AbstractJarComponentAutoRegister {

    private Class referenceClass = MethodInterceptor.class;

    /**
     * 参照クラスを返します。
     * 
     * @return
     */
    public Class getReferenceClass() {
        return referenceClass;
    }

    /**
     * jarファイルに含まれているクラスを設定します。jarファイルに含まれているならどのクラスでもOKです。
     * このクラスを参照してjarファイルの物理的な位置を特定します。
     * 
     * @param referenceClass
     */
    public void setReferenceClass(Class referenceClass) {
        this.referenceClass = referenceClass;
    }

    protected void setupBaseDir() {
        String path = ResourceUtil.getResourcePath(referenceClass);
        URL url = ResourceUtil.getResource(path);
        String fileName = null;
        if ("zip".equals(url.getProtocol())) {
            fileName = ZipFileUtil.toZipFilePath(url);
        } else {
            fileName = JarFileUtil.toJarFilePath(url);
        }
        File jarFile = new File(fileName);
        File dir = jarFile.getParentFile();
        setBaseDir(FileUtil.getCanonicalPath(dir));
    }

}