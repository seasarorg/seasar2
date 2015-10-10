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
import javax.annotation.Resource;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.model.NamesModel;
import org.seasar.extension.jdbc.gen.model.NamesModelFactory;
import org.seasar.extension.jdbc.gen.model.ServiceModel;
import org.seasar.extension.jdbc.gen.model.ServiceModelFactory;
import org.seasar.extension.jdbc.operation.Operations;
import org.seasar.framework.util.ClassUtil;

/**
 * {@link ServiceModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class ServiceModelFactoryImpl implements ServiceModelFactory {

    /** デフォルトの{@link JdbcManager}のコンポーネント名 */
    protected static String DEFAULT_JDBC_MANAGER_NAME = "jdbcManager";

    /** パッケージ名、デフォルトパッケージの場合は{@code null} */
    protected String packageName;

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName;

    /** サービスクラス名のサフィックス */
    protected String serviceClassNameSuffix;

    /** 名前モデルのファクトリ */
    protected NamesModelFactory namesModelFactory;

    /** 名前クラスを使用する場合{@code true} */
    protected boolean useNamesClass;

    /** クラスモデルのサポート */
    protected ClassModelSupport classModelSupport = new ClassModelSupport();

    /** 生成モデルのサポート */
    protected GeneratedModelSupport generatedModelSupport = new GeneratedModelSupport();

    /**
     * インスタンスを構築します。
     * 
     * @param packageName
     *            パッケージ名、デフォルトパッケージの場合は{@code null}
     * @param namesModelFactory
     *            名前モデルのファクトリ
     * @param serviceClassNameSuffix
     *            サービスクラス名のサフィックス
     * @param useNamesClass
     *            名前クラスを使用する場合{@code true}
     * @param jdbcManagerName
     *            {@link JdbcManager}のコンポーネント名
     */
    public ServiceModelFactoryImpl(String packageName,
            String serviceClassNameSuffix, NamesModelFactory namesModelFactory,
            boolean useNamesClass, String jdbcManagerName) {
        if (jdbcManagerName == null) {
            throw new NullPointerException("jdbcManagerName");
        }
        if (serviceClassNameSuffix == null) {
            throw new NullPointerException("serviceClassNameSuffix");
        }
        if (namesModelFactory == null) {
            throw new NullPointerException("namesModelFactory");
        }
        this.packageName = packageName;
        this.serviceClassNameSuffix = serviceClassNameSuffix;
        this.namesModelFactory = namesModelFactory;
        this.useNamesClass = useNamesClass;
        this.jdbcManagerName = jdbcManagerName;
    }

    public ServiceModel getServiceModel(EntityMeta entityMeta) {
        ServiceModel serviceModel = new ServiceModel();
        serviceModel.setPackageName(packageName);
        serviceModel.setShortClassName(entityMeta.getName()
                + serviceClassNameSuffix);
        serviceModel.setShortEntityClassName(entityMeta.getEntityClass()
                .getSimpleName());
        serviceModel
                .setShortSuperclassName("Abstract" + serviceClassNameSuffix);
        serviceModel.setJdbcManagerName(jdbcManagerName);
        serviceModel.setJdbcManagerSetterNecessary(!DEFAULT_JDBC_MANAGER_NAME
                .equals(jdbcManagerName));
        for (PropertyMeta idPropertyMeta : entityMeta.getIdPropertyMetaList()) {
            serviceModel.addIdPropertyMeta(idPropertyMeta);
        }
        if (entityMeta.hasVersionPropertyMeta()) {
            serviceModel.setVersionPropertyMeta(entityMeta
                    .getVersionPropertyMeta());
        }
        doNamesModel(serviceModel, entityMeta);
        doImportName(serviceModel, entityMeta);
        doGeneratedInfo(serviceModel, entityMeta);
        return serviceModel;
    }

    /**
     * 名前モデルを処理します。
     * 
     * @param serviceModel
     *            サービスモデル
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doNamesModel(ServiceModel serviceModel, EntityMeta entityMeta) {
        if (entityMeta.getIdPropertyMetaList().size() > 0 && useNamesClass) {
            NamesModel namesModel = namesModelFactory.getNamesModel(entityMeta);
            serviceModel.setNamesModel(namesModel);
        }
    }

    /**
     * インポート名を処理します。
     * 
     * @param serviceModel
     *            サービスモデル
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doImportName(ServiceModel serviceModel, EntityMeta entityMeta) {
        classModelSupport.addImportName(serviceModel, entityMeta
                .getEntityClass());
        classModelSupport.addImportName(serviceModel, Generated.class);
        for (PropertyMeta propertyMeta : serviceModel.getIdPropertyMetaList()) {
            classModelSupport.addImportName(serviceModel, propertyMeta
                    .getPropertyClass());
        }
        PropertyMeta propertyMeta = serviceModel.getVersionPropertyMeta();
        if (propertyMeta != null) {
            classModelSupport.addImportName(serviceModel, propertyMeta
                    .getPropertyClass());
        }
        NamesModel namesModel = serviceModel.getNamesModel();
        if (namesModel != null) {
            String namesClassName = ClassUtil.concatName(namesModel
                    .getPackageName(), namesModel.getShortClassName());
            classModelSupport.addStaticImportName(serviceModel, namesClassName);
            classModelSupport.addStaticImportName(serviceModel,
                    Operations.class);
            classModelSupport.addImportName(serviceModel, List.class);
        }
        if (serviceModel.isJdbcManagerSetterNecessary()) {
            classModelSupport.addImportName(serviceModel, Resource.class);
            classModelSupport.addImportName(serviceModel,
                    TransactionAttribute.class);
            classModelSupport.addImportName(serviceModel,
                    TransactionAttributeType.class);
            classModelSupport.addImportName(serviceModel, JdbcManager.class);
        }
    }

    /**
     * 生成情報を処理します。
     * 
     * @param serviceModel
     *            サービスモデル
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doGeneratedInfo(ServiceModel serviceModel,
            EntityMeta entityMeta) {
        generatedModelSupport.fillGeneratedInfo(this, serviceModel);
    }
}
