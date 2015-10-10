/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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

import java.util.List;

import javax.annotation.Generated;

import org.seasar.extension.jdbc.gen.model.NamesAggregateModel;
import org.seasar.extension.jdbc.gen.model.NamesAggregateModelFactory;
import org.seasar.extension.jdbc.gen.model.NamesModel;
import org.seasar.framework.util.ClassUtil;

/**
 * {@link NamesAggregateModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class NamesAggregateModelFactoryImpl implements
        NamesAggregateModelFactory {

    /** パッケージ名、デフォルトパッケージの場合は{@code null} */
    protected String packageName;

    /** クラスの単純名 */
    protected String shortClassName;

    /** クラスモデルのサポート */
    protected ClassModelSupport classModelSupport = new ClassModelSupport();

    /** 生成モデルのサポート */
    protected GeneratedModelSupport generatedModelSupport = new GeneratedModelSupport();

    /**
     * インスタンスを構築します。
     * 
     * @param packageName
     *            パッケージ名、デフォルトパッケージの場合は{@code null}
     * @param shortClassName
     *            クラスの単純名
     */
    public NamesAggregateModelFactoryImpl(String packageName,
            String shortClassName) {
        if (shortClassName == null) {
            throw new NullPointerException("shortClassName");
        }
        this.packageName = packageName;
        this.shortClassName = shortClassName;
    }

    public NamesAggregateModel getNamesAggregateModel(
            List<NamesModel> namesModelList) {
        NamesAggregateModel namesAggregateModel = new NamesAggregateModel();
        namesAggregateModel.setPackageName(packageName);
        namesAggregateModel.setShortClassName(shortClassName);
        for (NamesModel namesModel : namesModelList) {
            namesAggregateModel.addNamesModel(namesModel);
            doImportName(namesAggregateModel, namesModel);
        }
        doGeneratedInfo(namesAggregateModel);
        return namesAggregateModel;
    }

    /**
     * インポート名を処理します。
     * 
     * @param namesAggregateModel
     *            名前集約モデル
     * @param namesModel
     *            名前モデル
     */
    protected void doImportName(NamesAggregateModel namesAggregateModel,
            NamesModel namesModel) {
        String className = ClassUtil.concatName(ClassUtil.concatName(namesModel
                .getPackageName(), namesModel.getShortClassName()), namesModel
                .getShortInnerClassName());
        classModelSupport.addImportName(namesAggregateModel, className);
        classModelSupport.addImportName(namesAggregateModel, namesModel
                .getEntityClassName());
        classModelSupport.addImportName(namesAggregateModel, Generated.class);
    }

    /**
     * 生成情報を処理します。
     * 
     * @param namesAggregateModel
     *            名前集約モデル
     */
    protected void doGeneratedInfo(NamesAggregateModel namesAggregateModel) {
        generatedModelSupport.fillGeneratedInfo(this, namesAggregateModel);
    }
}
