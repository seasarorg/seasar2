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
import org.seasar.extension.jdbc.gen.model.ServiceModel;
import org.seasar.extension.jdbc.gen.model.ServiceModelFactory;
import org.seasar.framework.util.ClassUtil;

/**
 * {@link ServiceModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class ServiceModelFactoryImpl implements ServiceModelFactory {

    /** パッケージ名 */
    protected String packageName;

    /** サービスクラス名のサフィックス */
    protected String serviceClassNameSuffix;

    /**
     * インスタンスを構築します。
     * 
     * @param packageName
     *            パッケージ名
     * @param serviceClassNameSuffix
     *            サービスクラス名のサフィックス
     */
    public ServiceModelFactoryImpl(String packageName,
            String serviceClassNameSuffix) {
        if (serviceClassNameSuffix == null) {
            throw new NullPointerException("serviceClassNameSuffix");
        }
        this.packageName = packageName;
        this.serviceClassNameSuffix = serviceClassNameSuffix;
    }

    public ServiceModel getServiceModel(EntityMeta entityMeta) {
        ServiceModel serviceModel = new ServiceModel();
        serviceModel.setPackageName(packageName);
        serviceModel.setShortClassName(entityMeta.getName()
                + serviceClassNameSuffix);
        serviceModel.setShortEntityClassName(entityMeta.getEntityClass()
                .getSimpleName());
        for (PropertyMeta idPropertyMeta : entityMeta.getIdPropertyMetaList()) {
            serviceModel.addIdPropertyMeta(idPropertyMeta);
        }
        doImportName(serviceModel, entityMeta);
        return serviceModel;
    }

    /**
     * インポート名を処理します。
     * 
     * @param serviceModel
     *            サービスクラスのモデル
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doImportName(ServiceModel serviceModel, EntityMeta entityMeta) {
        addImportName(serviceModel, entityMeta.getEntityClass());
        for (PropertyMeta propertyMeta : entityMeta.getIdPropertyMetaList()) {
            addImportName(serviceModel, propertyMeta.getPropertyClass());
        }
    }

    /**
     * インポート名を追加します。
     * 
     * @param serviceModel
     *            サービスクラスのモデル
     * @param clazz
     *            インポートするクラス
     */
    protected void addImportName(ServiceModel serviceModel, Class<?> clazz) {
        String pakcageName = ClassUtil.getPackageName(clazz);
        if (pakcageName != null && !"java.lang".equals(pakcageName)) {
            serviceModel.addImportName(clazz.getName());
        }
    }
}
