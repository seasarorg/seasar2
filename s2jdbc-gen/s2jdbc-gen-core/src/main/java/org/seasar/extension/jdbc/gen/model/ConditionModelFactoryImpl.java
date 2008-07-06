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
package org.seasar.extension.jdbc.gen.model;

import org.seasar.extension.jdbc.gen.ConditionModelFactory;
import org.seasar.extension.jdbc.gen.EntityDesc;

/**
 * {@link ConditionModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class ConditionModelFactoryImpl implements ConditionModelFactory {

    public ConditionModel getConditionModel(EntityDesc entityDesc,
            String className, String baseClassName) {
        ConditionModel model = new ConditionModel();
        model.setClassName(className);
        model.setBaseClassName(baseClassName);
        model.setEntityDesc(entityDesc);
        doImportPackageNames(model, entityDesc);
        return model;
    }

    /**
     * インポートするパッケージ名を処理します。
     * 
     * @param model
     *            エンティティ条件クラスのモデル
     * @param entityDesc
     *            エンティティ記述
     */
    protected void doImportPackageNames(ConditionModel model,
            EntityDesc entityDesc) {
        model.addImportPackageName(model.getBaseClassName());
    }
}
