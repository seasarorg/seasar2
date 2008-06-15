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
package org.seasar.extension.jdbc.gen.model.factory;

import org.seasar.extension.jdbc.gen.EntityConditionModelFactory;
import org.seasar.extension.jdbc.gen.desc.EntityDesc;
import org.seasar.extension.jdbc.gen.model.EntityConditionModel;
import org.seasar.framework.util.ClassUtil;

/**
 * {@link EntityConditionModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class EntityConditionModelFactoryImpl implements
        EntityConditionModelFactory {

    public EntityConditionModel getEntityConditionModel(EntityDesc entityDesc,
            String className, String baseClassName) {
        EntityConditionModel model = new EntityConditionModel();
        model.setClassName(className);
        String[] elements = ClassUtil.splitPackageAndShortClassName(className);
        model.setPackageName(elements[0]);
        model.setShortClassName(elements[1]);
        model.setBaseClassName(baseClassName);
        String[] elements2 = ClassUtil
                .splitPackageAndShortClassName(baseClassName);
        model.setShortBaseClassName(elements2[1]);
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
    protected void doImportPackageNames(EntityConditionModel model,
            EntityDesc entityDesc) {
        model.addImportPackageName(model.getBaseClassName());
    }
}
