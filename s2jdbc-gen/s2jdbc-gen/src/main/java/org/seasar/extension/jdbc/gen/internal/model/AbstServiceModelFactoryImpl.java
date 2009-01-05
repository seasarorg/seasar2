/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import org.seasar.extension.jdbc.gen.model.AbstServiceModel;
import org.seasar.extension.jdbc.gen.model.AbstServiceModelFactory;
import org.seasar.extension.jdbc.service.S2AbstractService;

/**
 * {@link AbstServiceModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class AbstServiceModelFactoryImpl implements AbstServiceModelFactory {

    /** パッケージ名 */
    protected String packageName;

    /** サービスクラス名のサフィックス */
    protected String serviceClassNameSuffix;

    /** クラスモデルのサポート */
    protected ClassModelSupport classModelSupport = new ClassModelSupport();

    /**
     * インスタンスを構築します。
     * 
     * @param packageName
     *            パッケージ名
     * @param serviceClassNameSuffix
     *            サービスクラス名のサフィックス
     */
    public AbstServiceModelFactoryImpl(String packageName,
            String serviceClassNameSuffix) {
        if (serviceClassNameSuffix == null) {
            throw new NullPointerException("serviceClassNameSuffix");
        }
        this.packageName = packageName;
        this.serviceClassNameSuffix = serviceClassNameSuffix;
    }

    public AbstServiceModel getAbstServiceModel() {
        AbstServiceModel abstServiceModel = new AbstServiceModel();
        abstServiceModel.setPackageName(packageName);
        abstServiceModel.setShortClassName("Abstract" + serviceClassNameSuffix);
        classModelSupport.addImportName(abstServiceModel,
                S2AbstractService.class);
        return abstServiceModel;
    }

}
