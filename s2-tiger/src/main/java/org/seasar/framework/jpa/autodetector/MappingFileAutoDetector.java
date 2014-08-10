/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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
package org.seasar.framework.jpa.autodetector;

import java.io.InputStream;

import org.seasar.framework.autodetector.impl.AbstractResourceAutoDetector;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourcesUtil;
import org.seasar.framework.util.ResourceTraversal.ResourceHandler;
import org.seasar.framework.util.ResourcesUtil.Resources;

/**
 * 規約を利用してJPAのマッピングファイルを自動検出するクラスです。
 * <p>
 * このインスタンスが自動検出を実行するには{@link #namingConvention}に値が設定されていることが必須です。
 * デフォルトで次の条件に合致するマッピングファイルを検出します。
 * </p>
 * <ul>
 * <li>ファイルが{@link NamingConvention#getEntityPackageName()}で決定されるパッケージ、もしくは
 * {@link NamingConvention#getDaoPackageName()} で決定されるパッケージの階層に含まれる</li>
 * <li>ファイルの名称が正規表現 {@code .*Orm.xml} にマッチする</li>
 * </ul>
 * 
 * @author taedium
 */
@Component
public class MappingFileAutoDetector extends AbstractResourceAutoDetector {

    /** 命名規約 */
    protected NamingConvention namingConvention;

    /**
     * インスタンスを構築します。
     * 
     */
    public MappingFileAutoDetector() {
        addTargetDirPath("META-INF");
        addResourceNamePattern(".*Orm.xml");
    }

    /**
     * 命名規約を設定します。
     * 
     * @param namingConvention
     *            命名規約
     */
    @Binding(bindingType = BindingType.MAY)
    public void setNamingConvention(final NamingConvention namingConvention) {
        this.namingConvention = namingConvention;
    }

    /**
     * このインスタンスを初期化します。
     * 
     */
    @InitMethod
    public void init() {
        if (namingConvention != null) {
            final String entityPackageName = namingConvention
                    .getEntityPackageName();
            final String daoPackageName = namingConvention.getDaoPackageName();
            addTargetPackageName(entityPackageName);
            addTargetPackageName(daoPackageName);
        }
    }

    /**
     * 検出対象とするパッケージ名を追加します。
     * 
     * @param packageName
     *            パッケージ名
     */
    protected void addTargetPackageName(final String packageName) {
        for (final String rootPackageName : namingConvention
                .getRootPackageNames()) {
            final String concatedPackageName = ClassUtil.concatName(
                    rootPackageName, packageName);
            addTargetDirPath(concatedPackageName.replace('.', '/'));
        }
    }

    @SuppressWarnings("unchecked")
    public void detect(final ResourceHandler handler) {
        for (int i = 0; i < getTargetDirPathSize(); i++) {
            final String targetDirPath = getTargetDirPath(i);
            for (final Resources resources : ResourcesUtil
                    .getResourcesTypes(targetDirPath)) {
                try {
                    resources.forEach(new ResourceHandler() {

                        public void processResource(final String path,
                                final InputStream is) {
                            if (path.startsWith(targetDirPath)
                                    && isApplied(path) && !isIgnored(path)) {
                                handler.processResource(path, is);
                            }
                        }
                    });
                } finally {
                    resources.close();
                }
            }
        }
    }

}
