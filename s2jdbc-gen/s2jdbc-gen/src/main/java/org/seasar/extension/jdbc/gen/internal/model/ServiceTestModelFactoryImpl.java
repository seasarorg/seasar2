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
package org.seasar.extension.jdbc.gen.internal.model;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.gen.model.ServiceTestModel;
import org.seasar.extension.jdbc.gen.model.ServiceTestModelFactory;
import org.seasar.extension.unit.S2TestCase;

/**
 * {@link ServiceTestModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class ServiceTestModelFactoryImpl implements ServiceTestModelFactory {

    /** パッケージ名、デフォルトパッケージの場合は{@code null} */
    protected String packageName;

    /** サービスクラス名のサフィックス */
    protected String serviceClassNameSuffix;

    /** テストクラス名のサフィックス */
    protected String testClassNameSuffix;

    /** 設定ファイルのパス */
    protected String configPath;

    /** クラスモデルのサポート */
    protected ClassModelSupport classModelSupport = new ClassModelSupport();

    /**
     * インスタンスを構築します。
     * 
     * @param packageName
     *            パッケージ名、デフォルトパッケージの場合は{@code null}
     * @param serviceClassNameSuffix
     *            サービスクラス名のサフィックス
     * @param testClassNameSuffix
     *            テストクラス名のサフィックス
     * @param configPath
     *            設定ファイルのパス
     */
    public ServiceTestModelFactoryImpl(String configPath, String packageName,
            String serviceClassNameSuffix, String testClassNameSuffix) {
        if (configPath == null) {
            throw new NullPointerException("configPath");
        }
        if (serviceClassNameSuffix == null) {
            throw new NullPointerException("serviceClassNameSuffix");
        }
        if (testClassNameSuffix == null) {
            throw new NullPointerException("testClassNameSuffix");
        }
        this.configPath = configPath;
        this.packageName = packageName;
        this.serviceClassNameSuffix = serviceClassNameSuffix;
        this.testClassNameSuffix = testClassNameSuffix;
    }

    public ServiceTestModel getServiceTestModel(EntityMeta entityMeta) {
        ServiceTestModel serviceTestModel = new ServiceTestModel();
        serviceTestModel.setConfigPath(configPath);
        serviceTestModel.setPackageName(packageName);
        String shortServiceClassName = entityMeta.getEntityClass()
                .getSimpleName()
                + serviceClassNameSuffix;
        serviceTestModel.setShortServiceClassName(shortServiceClassName);
        serviceTestModel.setShortClassName(shortServiceClassName
                + testClassNameSuffix);
        classModelSupport.addImportName(serviceTestModel, S2TestCase.class);
        return serviceTestModel;
    }
}
