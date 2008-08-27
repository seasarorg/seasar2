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
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.model.NamesModel;
import org.seasar.extension.jdbc.gen.model.NamesModelFactory;

/**
 * {@link NamesModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class NamesModelFactoryImpl implements NamesModelFactory {

    /** パッケージ名 */
    protected String packageName;

    /** 名前インタフェース名のサフィックス */
    protected String namesInterfaceNameSuffix;

    /** クラスモデルのサポート */
    protected ClassModelSupport classModelSupport = new ClassModelSupport();

    /**
     * インスタンスを構築します。
     * 
     * @param namesInterfaceNameSuffix
     *            名前インタフェース名のサフィックス
     * @param packageName
     *            パッケージ名
     */
    public NamesModelFactoryImpl(String packageName,
            String namesInterfaceNameSuffix) {
        if (namesInterfaceNameSuffix == null) {
            throw new NullPointerException("namesInterfaceNameSuffix");
        }
        this.packageName = packageName;
        this.namesInterfaceNameSuffix = namesInterfaceNameSuffix;
    }

    public NamesModel getNamesModel(EntityMeta entityMeta) {
        NamesModel namesModel = new NamesModel();
        namesModel.setPackageName(packageName);
        namesModel.setShortClassName(entityMeta.getName()
                + namesInterfaceNameSuffix);
        namesModel.setShortEntityClassName(entityMeta.getEntityClass()
                .getSimpleName());
        for (PropertyMeta propertyMeta : entityMeta.getAllPropertyMeta()) {
            namesModel.addName(propertyMeta.getName());
        }
        classModelSupport
                .addImportName(namesModel, entityMeta.getEntityClass());
        return namesModel;
    }
}
