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
package org.seasar.extension.jdbc.gen.desc.factory;

import java.util.List;

import javax.persistence.GenerationType;

import org.seasar.extension.jdbc.ColumnMeta;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.PrimaryKeyDescFactory;
import org.seasar.extension.jdbc.gen.desc.PrimaryKeyDesc;

/**
 * {@link PrimaryKeyDescFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class PrimaryKeyDescFactoryImpl implements PrimaryKeyDescFactory {

    /** 方言 */
    protected GenDialect dialect;

    /**
     * インスタンスを構築します。
     * 
     * @param dialect
     *            方言
     */
    public PrimaryKeyDescFactoryImpl(GenDialect dialect) {
        this.dialect = dialect;
    }

    public PrimaryKeyDesc getPrimaryKeyDesc(EntityMeta entityMeta) {
        PrimaryKeyDesc primaryKeyDesc = new PrimaryKeyDesc();
        doColumnName(entityMeta, primaryKeyDesc);
        doIdentity(entityMeta, primaryKeyDesc);
        if (primaryKeyDesc.getColumnNameList().isEmpty()) {
            return null;
        }
        return primaryKeyDesc;
    }

    /**
     * カラムの名前を処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param primaryKeyDesc
     *            主キー記述
     */
    protected void doColumnName(EntityMeta entityMeta,
            PrimaryKeyDesc primaryKeyDesc) {
        for (PropertyMeta propertyMeta : entityMeta.getIdPropertyMetaList()) {
            ColumnMeta columnMeta = propertyMeta.getColumnMeta();
            primaryKeyDesc.addColumnName(columnMeta.getName());
        }
    }

    /**
     * {@link GenerationType#IDENTITY}で識別子が生成されるかどうかを処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param primaryKeyDesc
     *            主キー記述
     */
    protected void doIdentity(EntityMeta entityMeta,
            PrimaryKeyDesc primaryKeyDesc) {
        List<PropertyMeta> idPropertyMetaList = entityMeta
                .getIdPropertyMetaList();
        if (idPropertyMetaList.size() == 1) {
            PropertyMeta propertyMeta = idPropertyMetaList.get(0);
            GenerationType generationType = propertyMeta.getGenerationType();
            if (generationType == GenerationType.AUTO) {
                generationType = dialect.getDefaultGenerationType();
            }
            if (generationType == GenerationType.IDENTITY) {
                primaryKeyDesc.setIdentity(true);
            }
        }
    }
}
